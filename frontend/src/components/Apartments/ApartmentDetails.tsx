import {useNavigate, useParams} from "react-router-dom";
import {useApartmentSelector, useAppDispatch, usePeriodSelector, useUserSelector} from "../../store/hooks";
import {Role} from "../types";
import {ReviewsList} from "../Reviews/ReviewsList";
import Calendar from 'react-calendar'
import './ProjectCalendar.css'
import React, {ChangeEvent, useEffect, useState} from "react";
import {createPeriod, loadApartmentAvailablePeriods, loadApartmentPeriods,} from "../../store/periods";
import {createBooking} from "../../store/bookings";
import {ApartmentInfoDTO, AvailablePeriodInfoDTO, BookApartmentDTO, PeriodCreateDTO} from "../../api";

enum ActionType {
    Booking = "booking",
    Period = "period",
}

const hasAvailablePeriods = (periods: AvailablePeriodInfoDTO[]) => {
    return periods.length!==0
}
const buildFormattedDate = (date: Date) => {
    return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`;
};

function checkIfDatesExist(dates: Date[], dateToCheck: Date): boolean {
    return dates.some(date => dateToCheck.getTime() === date.getTime());
}

const buildStandardDate = (date:  Date) => {
    return new Date(date.getFullYear(), date.getMonth(), date.getDate())
};

const generateDatesRange = (start: string, end: string): Date[] => {
    const datesRange: Date[] = [];
    let currentDate = new Date(start);
    const endFormatted = new Date(end);

    while (currentDate <= endFormatted) {
        datesRange.push(new Date(currentDate));
        currentDate.setDate(currentDate.getDate() + 1);
    }

    return datesRange;
};

export const ApartmentInfo = ({ apart } : { apart: ApartmentInfoDTO }) => {
    return (
        <div style={{ marginTop: '20px', textAlign: 'start'}}>
            <h2>Details</h2>
            <p><strong>Name:</strong> {apart.name}</p>
            <p><strong>Description:</strong> {apart.description}</p>
            <p><strong>Location:</strong> {apart.location}</p>
            <p><strong>Amenities:</strong> {apart.amenities}</p>
            <p><strong>Price:</strong> {apart.price}</p>
        </div>
    )
}

export const ApartmentDetails = () => {
    let { id } = useParams();
    let apartmentId = parseInt(id!);

    const user = useUserSelector(state => state.username)
    const role = useUserSelector(state => state.role)
    const apart = useApartmentSelector((state) =>
        state.apartments.find((p) => p.id === apartmentId)
    );
    const periods = usePeriodSelector((state) => state.periods);
    const availablePeriods = usePeriodSelector((state => state.availablePeriods))
    const dispatch = useAppDispatch()

    useEffect(() => { dispatch(loadApartmentPeriods(apartmentId)) ; dispatch(loadApartmentAvailablePeriods(apartmentId))}, [apart, apartmentId, dispatch])
    const navigate = useNavigate();
    const [guests, setGuests] = useState<number | null>(null);

    type ValuePiece = Date | null;
    type Value = ValuePiece | [ValuePiece, ValuePiece];
    const [dates, setDates] = useState<Value>(null);


    const isOwner = () => {
        return role === Role.OWNER
    }

    if (apart === undefined) {
        return <h1>Apartment not found</h1>;
    }

    const handleInputChange = (e: ChangeEvent<HTMLInputElement>) => {
        const value = e.target.value;
        const inputGuests = value !== '' ? parseInt(value, 10) : null;

        if (inputGuests !== null) {
            if (inputGuests > 4) {
                alert("Maximum number of guests is 4");
            } else if (inputGuests < 1) {
                alert("Minimum number of guests is 1");
            } else {
                setGuests(inputGuests);
            }
        } else {
            setGuests(null);
        }
    };

    const validateDates = (date1: Date, date2: Date, action: string) => {
        const currentDate = buildStandardDate(new Date());
        const providedDate = buildStandardDate(date1);

        if (providedDate < currentDate) {
            alert("Invalid dates");
            return false;
        }

        const startDate = buildFormattedDate(date1);
        const endDate = buildFormattedDate(date2);
        const datesRange: Date[] = generateDatesRange(startDate, endDate);

        if(action === ActionType.Booking) {
            const periodAvailable = datesRange.every(date =>
                availablePeriods.some(period => new Date(period.date).getTime() === date.getTime())
            );

            if (!periodAvailable) {
                alert("Period unavailable");
                return false
            }
        }

        if(action === ActionType.Period) {
            const periodUnavailable = periods.some(period =>
                checkIfDatesExist(datesRange, new Date(period.date))
            );

            if (periodUnavailable) {
                alert("Period unavailable");
                return false;
            }
        }
        return true;
    };

    const handleButtonClick = () => {
        if(guests === null) {
            alert("Specify number of guests")
            return;
        }

        if(Array.isArray(dates)) {
            const [date1, date2] = dates;

            if(user && date1 && date2 && validateDates(date1, date2, "booking")) {
                const booking : BookApartmentDTO = {
                    checkIn : buildFormattedDate(date1),
                    checkOut : buildFormattedDate(date2),
                    guests : guests
                }
                dispatch(createBooking(apartmentId, user, booking))
                alert(`Booking registered for ${apart.name} with ${guests} guests.`);
                navigate(-1)
            }
        }
    };

    const handlePeriodButtonClick = () => {
        if(Array.isArray(dates)) {
            const [date1, date2] = dates;

            if(date1 && date2 && validateDates(date1, date2, "period")) {
                const period : PeriodCreateDTO = {
                    startDate : buildFormattedDate(date1),
                    endDate : buildFormattedDate(date2)
                }
                dispatch(createPeriod(apartmentId, period))
                alert(`Period ${date1.toLocaleDateString()}, ${date2.toLocaleDateString()} created for ${apart.name}`);
                navigate(-1)
            }
        }
    };

    const PeriodsCalendar = () => {
        const parseDateStringToDate = (dateString: string): Date => {
            const [year, month, day] = dateString.split('-').map(Number);
            return new Date(year, month - 1, day);
        };

        const datesToHighlightAsDate: Date[] = availablePeriods.map(period => parseDateStringToDate(period.date));
        const allPeriodsDates: Date[] = periods.map(period => parseDateStringToDate(period.date));

        const tileClassName = ({ date }: { date: Date }): string | null => {
            const currentDate = buildStandardDate(new Date());
            const providedDate = buildStandardDate(date);

            if(providedDate < currentDate) {
                return 'unavailable'
            }
            if (datesToHighlightAsDate.some(highlightedDate => highlightedDate.getTime() === date.getTime())) {
                return isOwner() ? 'unavailable' : 'available'
            }
            else if(isOwner() && allPeriodsDates.some(periodDate =>  periodDate.getTime() === date.getTime())) {
                return 'unavailable'
            }
            else {
                return isOwner() ? 'available' : 'unavailable'
            }
        };

        const handleDateChange = (value: Value) => {
            setDates(value);
        };

        return (
            <div style={nome}>
                <div className="calendar" style={calendarDiv}>
                    <Calendar onChange={handleDateChange} value={dates} tileClassName={tileClassName} selectRange />
                </div>
                <div style={periodDiv}>
                    {Array.isArray(dates) ? (
                        <div>
                            <strong>Selected Period:</strong> <br/> {dates.map((date, index) => (
                            index === dates.length - 1 ? date?.toLocaleDateString() : `${date?.toLocaleDateString()}, `
                        ))}
                        </div>
                    ) : dates instanceof Date ? (
                        <div>
                            <strong>Selected Period:</strong> <br/> {dates.toLocaleDateString()}
                        </div>
                    ) : (
                        <div><strong>Selected Period:</strong></div>
                    )}
                </div>
            </div>
        );
    };

    switch(role) {
        case Role.CLIENT:
            return (
                <div style={container}>
                    <h1 style={{ marginBottom: '50px'}}>{apart.name}</h1>
                    <div style={innerDiv}>
                        <div style={apartDetails}>
                            <div style={{ display: 'flex', flexDirection: 'row' }}>
                                <img style={image} src={apart.pictures} alt={""}/>
                                <div style={{ width: '100%', flex: '1' }}>
                                    <ApartmentInfo apart={apart} />
                                </div>
                            </div>
                        </div>
                        { hasAvailablePeriods(availablePeriods) ?
                        <div>
                            <div style={{ marginRight: '20px' }}>
                                <PeriodsCalendar/>
                            </div>
                            <div style={buttonsLayout}>
                                <label htmlFor="guests" style={{ display: 'block', marginBottom: '10px' }}>Guests:</label>
                                <input
                                    style={guestButton}
                                    type="number"
                                    id="guests"
                                    name="guests"
                                    min="0"
                                    max="4"
                                    onChange={handleInputChange}
                                    value={guests !== null ? guests.toString() : ''}
                                />
                                <button
                                    style={periodButton}
                                    onClick={handleButtonClick}
                                >Confirm Booking
                                </button>
                            </div>
                        </div> : <text>Apartment Unavailable!</text>}
                    </div>
                    <div style={{ marginTop: '20px' }}>
                        <ApartmentReviews apart={apart} />
                    </div>
                </div>
            );
        case Role.OWNER:
            return (
                <div style={container}>
                    <h1 style={{ marginBottom: '50px'}}>{apart.name}</h1>
                    <div style={innerDiv}>
                        <div style={apartDetails}>
                            <div style={{ display: 'flex', flexDirection: 'row' }}>
                                <img style={image} src={apart.pictures} alt={""}/>
                                <div style={{ width: '100%', flex: '1' }}>
                                    <ApartmentInfo apart={apart} />
                                </div>
                            </div>
                        </div>
                        <div style={{ marginRight: '20px' }}>
                            <PeriodsCalendar/>
                        </div>
                        <div style={buttonsLayout}>
                            <button
                                style={periodButton}
                                onClick={handlePeriodButtonClick}
                            >Confirm Period
                            </button>
                        </div>
                    </div>
                    <div style={{ marginTop: '20px' }}>
                        <ApartmentReviews apart={apart} />
                    </div>
                </div>
            );
        default:
            return <div></div>
    }
}

const ApartmentReviews = ({ apart } : { apart: ApartmentInfoDTO}) => {
    return (
        <div>
            <h1>Reviews</h1>
            <ReviewsList apartmentId={apart.id!} />
        </div>
    );
}


const container: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    fontFamily: 'Arial, sans-serif',
    color: '#333',
    maxWidth: '1200px',
    margin: '0 auto',
};

const innerDiv: React.CSSProperties = {
    display: 'flex', flexDirection: 'row', width: '100%'
};

const apartDetails: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    padding: '20px',
    boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
    borderRadius: '8px',
    marginBottom: '20px',
    marginRight: '80px',
};

const image: React.CSSProperties = {
    width: '100%', maxWidth: '300px', borderRadius: '8px', marginRight: '40px'
};

const buttonsLayout: React.CSSProperties = {
    display: 'flex', flexDirection: 'column', alignItems: 'flex-end',
};

const periodButton: React.CSSProperties = {
    padding: '5px 20px',
    backgroundColor: '#3498db',
    color: '#fff',
    border: 'none',
    borderRadius: '4px',
    cursor: 'pointer',
    transition: 'background-color 0.3s ease',
    fontWeight: "bold",
};

const guestButton: React.CSSProperties = {
    width: '50%', padding: '8px', borderRadius: '4px', border: '1px solid #ccc', marginBottom: '10px'
};

const periodDiv: React.CSSProperties = {
    padding: '10px',
    borderRadius: '8px',
    boxShadow: '0px 0px 8px 0px rgba(0,0,0,0.1)',
    backgroundColor: 'white',
};

const calendarDiv: React.CSSProperties = {
    marginRight: '20px',
    borderRadius: '8px',
    boxShadow:'0px 0px 8px 0px rgba(0,0,0,0.1)',
};

const nome: React.CSSProperties = {
    display: 'flex',
    alignItems: 'flex-start',
    fontFamily: 'Arial, sans-serif',
    color: '#333',
    maxWidth: '800px',
    margin: '0 auto',
};