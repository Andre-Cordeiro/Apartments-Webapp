import {useParams} from "react-router-dom";
import {useAppDispatch, useBookingSelector, useReviewSelector, useUserSelector} from "../../store/hooks";
import {Role, Status} from "../types";
import {columnStyle} from "../HomePage";
import React, {useEffect} from "react";
import {CreateReview} from "../Reviews/CreateReview";
import {loadBookingReview} from "../../store/reviews";
import {cancelBooking, changeBookingStatus, checkIn, checkOut} from "../../store/bookings";
import {BookingInfoDTO} from "../../api";
import {cardStyle, StarRating} from "../Reviews/ReviewsList";


export const BookingInfo = ({ booking } : { booking: BookingInfoDTO }) => {
    return (
        <div style={{ margin: '16px auto', maxWidth: '600px', marginRight: '200px' }}>
            <h1 style={{ fontSize: '24px', paddingBottom: '8px', marginBottom: '16px' }}>Details</h1>
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(200px, 1fr))', gap: '8px' ,textAlign: 'start'}}>
                <p style={{ margin: '4px 0' }}> <strong> Client name: </strong> {booking.clientName}</p>
                <p style={{ margin: '4px 0' }}> <strong>Check In: </strong> {booking.checkIn}</p>
                <p style={{ margin: '4px 0' }}> <strong>Check Out: </strong> {booking.checkOut}</p>
                <p style={{ margin: '4px 0' }}> <strong>Guests: </strong> {booking.guests}</p>
                <p style={{ margin: '4px 0' }}> <strong>State: </strong>{booking.state}</p>
                <p style={{ margin: '4px 0' }}> <strong>Name: </strong>{booking.apartmentInfo.name}</p>
                <p style={{ margin: '4px 0' }}> <strong>Description: </strong>{booking.apartmentInfo.description}</p>
                <p style={{ margin: '4px 0' }}> <strong>Location: </strong>{booking.apartmentInfo.location}</p>
                <p style={{ margin: '4px 0' }}> <strong>Amenities: </strong>{booking.apartmentInfo.amenities}</p>
                <p style={{ margin: '4px 0' }}> <strong>Price: </strong>{booking.apartmentInfo.price}$</p>
            </div>
        </div>
    )
}

export const BookingDetails = () => {
    let { id } = useParams();
    let bookingId = parseInt(id!);

    const user = useUserSelector(state => state.username)
    const role = useUserSelector(state => state.role)
    const booking = useBookingSelector((state) =>
        state.bookings.find((B) => B.id === bookingId)
    );

    if (booking === undefined) {
        return <h1>Apartment not found</h1>;
    }

    return (
        <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', marginTop: '16px'}}>
            <h1>{booking.clientName}'s Booking</h1>
            <div style={{ ...columnStyle, marginTop: '16px' }}>
                <BookingInfo booking={booking} />
                <BookingReview booking={booking} role={role}/>
                <UpdateBookingStatus booking={booking} role={role} user={user}/>
            </div>
        </div>
    );
}

const BookingReview = ({ booking, role } : { booking: BookingInfoDTO, role: string|undefined }) => {
    const review = useReviewSelector(state => state.reviews);
    const dispatch = useAppDispatch();

    useEffect(() => {
        if (booking.id && booking.state === Status.CLOSED) {
            dispatch(loadBookingReview(booking.id));
        }
    }, [booking.id, booking.state, dispatch]);


    if(booking.state === Status.REJECTED){
        return <text>Booking was rejected.</text>
    }

    if (booking.state !== Status.CLOSED && role === Role.CLIENT) {
        return (
            <div>
                <h2>Review</h2>
                <CreateReview booking={booking} />
            </div>
        );
    }

    if (booking.id && booking.state === Status.CLOSED && review.length > 0) {
        return (
            <div style={cardStyle()}>
                <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginInline: "20px" }}>
                    <h4 style={{ marginRight: "10px", fontSize: 18 }}>{review[0].clientName}</h4>
                    <StarRating rating={review[0].rating} />
                </div>
                <div style={{ display: "flex", flexDirection: "column", alignItems: "flex-start", textAlign: "justify", paddingInline: "20px", marginTop: "-20px" }}>
                    <p>{review[0].description}</p>
                </div>
            </div>
        );
    }

    return (
        <div style={{ margin: '16px auto', maxWidth: '600px', padding: '16px' }}>
            <text>Not reviewed yet</text>
        </div>
    );
}

const UpdateBookingStatus = ({ booking, role, user} : { booking: BookingInfoDTO, role: string|undefined, user: number|undefined}) => {

    const dispatch = useAppDispatch()

    function onCancelBooking() {
        if(user && booking.id){
            dispatch(cancelBooking(user,booking.id))
            alert("Booking has been cancelled")
        }
    }

    function onToggleCheckInOut() {
        if (user && booking.id) {
            const currentDate = new Date().setHours(0, 0, 0, 0);

            switch (booking.state) {
                case Status.BOOKED:
                    if (new Date(booking.checkIn).getTime() === currentDate) {
                        dispatch(checkIn(user, booking.id));
                        alert("Guest has Checked-In");
                    } else {
                        alert("Check-In day hasn't arrived yet");
                    }
                    break;

                case Status.OCCUPIED:
                    if (new Date(booking.checkOut).getTime() === currentDate) {
                        dispatch(checkOut(user, booking.id));
                        alert("Guest has Checked-Out");
                    } else {
                        alert("Check-Out day hasn't arrived yet");
                    }
                    break;
                default:
                    break;
            }
        }
    }

    function onHandleBooking(status: Status) {
        if(user && booking.id) {
            dispatch(changeBookingStatus(user, booking.id, status));
            alert(`Booking status changed to: ${status}`)
        }
    }

    const buttonStyle = {
        backgroundColor: '#4CAF50',
        color: 'white',
        padding: '10px',
        border: 'none',
        borderRadius: '10px',
        cursor: 'pointer',
        marginBottom: '8px',
        marginTop: '24px',
        margin: '10px', // Add this line
    };

    switch(role) {
        case Role.CLIENT:
            if (booking.state === Status.UNDER_CONSIDERATION) {
                return (
                    <div style={{ display: 'flex', flexDirection: 'column', gap: '4px' , marginLeft:'200px', alignItems: 'flex-start'}}>
                        <button onClick={onCancelBooking} style={{ ...buttonStyle, backgroundColor: '#E57373' }}>
                            Cancel Booking
                        </button>
                    </div>
                );
            }

            if (booking.state === Status.BOOKED) {
                return (
                    <div style={{ display: 'flex', flexDirection: 'row', gap: '4px' , marginLeft:'200px', alignItems: 'flex-start'}}>
                        <button onClick={onToggleCheckInOut} style={{ ...buttonStyle, backgroundColor: '#4CAF50' }}>
                            Check In
                        </button>
                        <button onClick={onCancelBooking} style={{ ...buttonStyle, backgroundColor: '#E57373' }}>
                            Cancel Booking
                        </button>
                    </div>
                );
            }

            if (booking.state === Status.OCCUPIED) {
                return (
                    <div style={{ display: 'flex', flexDirection: 'column', gap: '4px' , marginLeft:'200px', alignItems: 'flex-start'}}>
                        <button onClick={onToggleCheckInOut} style={{ ...buttonStyle, backgroundColor: '#4CAF50' }}>
                            Check Out
                        </button>
                    </div>
                );
            }
            return null;
        case Role.OWNER:
            if (booking.state === Status.UNDER_CONSIDERATION) {
                return (
                    <div style={{ display: 'flex', flexDirection: 'row', gap: '4px' , marginLeft:'200px', alignItems: 'flex-start'}}>
                        <button onClick={() => onHandleBooking(Status.BOOKED)} style={{ ...buttonStyle, backgroundColor: '#4CAF50'}}>
                            Accept Booking
                        </button>
                        <button onClick={() => onHandleBooking(Status.REJECTED)} style={{ ...buttonStyle, backgroundColor: '#E57373' }}>
                            Reject Booking
                        </button>
                    </div>
                );
            }
            return null;
        default:
            return <div></div>
    }
}