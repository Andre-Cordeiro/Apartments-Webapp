import React, {useEffect, useState} from "react";
import {useApartmentSelector, useAppDispatch, useBookingSelector, useUserSelector} from "../../store/hooks";
import {Role} from "../types";
import {useLocation, useNavigate} from "react-router-dom";
import {loadOwnerApartments} from "../../store/apartments";
import {loadClientBookings, loadOwnerBookings,} from "../../store/bookings";
import {ApartmentInfoDTO, BookingInfoDTO} from "../../api";

const textFieldComponent = (textField: string | undefined) => ( <text style={{fontWeight:"bold"}}>{textField}</text> );

//Constants
const MAX_APARTMENTS_PER_PAGE = 3
const MAX_BOOKINGS_PER_PAGE = 3
const CHECK_IN = "Check In:"
const CHECK_OUT = "Check Out:"
const NUMBER_GUESTS = "Nº Guests:"
const NO_ACTIVE_BOOKINGS= "No active bookings!"
const CLIENTS =  "Clients:"

const BookingsCardView = ({booking, onClick}: {
    booking?: BookingInfoDTO,
    onClick?: () => void
}) => {
    const userRole = useUserSelector(state => state.role)

    switch (userRole) {
        case Role.CLIENT:
            return (
                <li style={bookingItemStyle()} onClick={onClick}>
                    <div style={bookingFieldStyle}>
                        <p>{textFieldComponent(booking?.apartmentInfo.name)}</p>
                        <p>{textFieldComponent(CHECK_IN)} {booking?.checkIn}</p>
                    </div>
                    <div style={bookingFieldStyle}>
                        <p>{textFieldComponent(NUMBER_GUESTS)} {booking?.guests}</p>
                        <p>{textFieldComponent(CHECK_OUT)} {booking?.checkOut}</p>
                    </div>
                </li>
            )
        case Role.OWNER:
            if(booking === undefined)
                return (
                    <li style={bookingItemStyle(true)} onClick={onClick}>
                        <p>{textFieldComponent(NO_ACTIVE_BOOKINGS)}</p>
                    </li>
                )
            else
            return (
                <li style={bookingItemStyle()} onClick={onClick}>
                    <div style={bookingItemOwnerRow1Style}>
                        <div style={bookingItemOwnerRow2Style}>
                            <p>{textFieldComponent(CLIENTS)} {booking?.clientName}</p>
                        </div>
                        <div>
                            <p>{textFieldComponent(CHECK_IN)} {booking?.checkIn}</p>
                            <p>{textFieldComponent(CHECK_OUT)} {booking?.checkOut}</p>
                        </div>
                    </div>
                </li>
            )
        default:
            return <div></div>
    }
};

const BookingsListView = ({userRole, bookings, ownerApartments}:
                              {
                                  userRole: string | undefined,
                                  bookings: BookingInfoDTO[],
                                  ownerApartments: ApartmentInfoDTO[] | undefined
                              }) => {

    const navigate = useNavigate()
    const path = useLocation().pathname

    switch(userRole){
        case Role.CLIENT:
            return( <div>
                <ul style={bookingsListClient}>
                    {bookings.map((booking, i) =>
                        <BookingsCardView
                            key={i}
                            booking={booking}
                            onClick={() => navigate(path + "/" + booking.id)}/>)}
                </ul>
            </div>
            )
        case Role.OWNER:
            return <div>
                <ul>
                    {ownerApartments?.map((ap) => (
                        <li style={{listStyleType: "none"}} key={ap.id}>
                            <h2>{ap.name}</h2>
                            <ul>
                                {bookings.filter((book) => book.apartmentInfo.name === ap.name)
                                    .map((apartmentBooking) => (
                                        <BookingsCardView
                                            key={apartmentBooking.id}
                                            booking={apartmentBooking}
                                            onClick={() => navigate(path + "/" + apartmentBooking.id)}/>
                                    ))
                                }
                                {bookings.every((book) => book.apartmentInfo.name !== ap.name) && (
                                    <BookingsCardView/>
                                )}
                            </ul>
                        </li>
                    ))}
                </ul>
            </div>
        default:
            return <div></div>
    }
}

interface PaginationProps {
    currentPage: number;
    totalPages: number;
    setCurrentPage: (page: number) => void;
}


export const BookingsList = () => {
    const username = useUserSelector(state => state.username)
    const userRole = useUserSelector(state => state.role)
    const bookings = useBookingSelector(state => state.bookings)
    const dispatch = useAppDispatch()
    const ownerAparts = useApartmentSelector(state => state.apartments)

    useEffect(() => {
        if(username) {
            switch (userRole) {
            case Role.CLIENT:
                dispatch(loadClientBookings(username))
                break;
            case Role.OWNER:
                dispatch(loadOwnerBookings(username))
                dispatch(loadOwnerApartments(username))
                break;
            }
        }
    }, [userRole, dispatch, username]);

    const hasBookings = () => {
        return bookings !== null && bookings.length > 0
    }

    const [currentPage, setCurrentPage] = useState(1);
    const getApartmentsForPage = (page: number): ApartmentInfoDTO[] => {
        const startIndex = (page - 1) * MAX_APARTMENTS_PER_PAGE;
        const endIndex = startIndex + MAX_APARTMENTS_PER_PAGE;
        return ownerAparts.slice(startIndex, endIndex);
    };

    const totalPages = Math.ceil(ownerAparts.length / MAX_APARTMENTS_PER_PAGE);
    const apartmentsForPage = getApartmentsForPage(currentPage);


    const [currentPage2, setCurrentPage2] = useState(1);
    const getBookingsForPage = (page: number): BookingInfoDTO[] => {
        const startIndex2 = (page - 1) * MAX_BOOKINGS_PER_PAGE;
        const endIndex2 = startIndex2 + MAX_BOOKINGS_PER_PAGE;
        return bookings.slice(startIndex2, endIndex2);
    };

    const totalPages2 = Math.ceil(bookings.length / MAX_BOOKINGS_PER_PAGE);
    const bookingsForPage = getBookingsForPage(currentPage2);

    const renderPagination = (
        totalPages: number,
        currentPage: number,
        setCurrentPage: (page: number) => void
    ) => {
        return (
            <div>
                {Array.from({ length: totalPages }, (_, index) => index + 1).map((page) => (
                    <button
                        key={page}
                        onClick={() => setCurrentPage(page)}
                        disabled={currentPage === page}
                        style={paginationButtonStyle}
                    >
                        {page}
                    </button>
                ))}
            </div>
        );
    };

    const pagination = renderPagination(
        userRole === Role.CLIENT ? totalPages2 : totalPages,
        userRole === Role.CLIENT ? currentPage2 : currentPage,
        userRole === Role.CLIENT ? setCurrentPage2 : setCurrentPage
    );

    return (
        <div>
            <div style={bookingsListHeaderStyle}>
                <h2>My Bookings: </h2>
            </div>
            <div style={bookingsListStyle}>
                <div>
                    {userRole === Role.CLIENT ? (
                        hasBookings() ? (
                            <BookingsListView
                                userRole={userRole}
                                bookings={bookingsForPage}
                                ownerApartments={apartmentsForPage}
                            />
                        ) : (
                            <text>You don't have bookings yet</text>
                        )
                    ) : (
                        <BookingsListView
                            userRole={userRole}
                            bookings={bookings}
                            ownerApartments={apartmentsForPage}
                        />
                    )}
                </div>
            </div>
            {pagination}
        </div>
    );
}

const bookingsListHeaderStyle = ({
    display:"flex",
    justifyContent:"flex-start",
    marginLeft:"70px"
})

const bookingsListStyle: React.CSSProperties = {
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
};

const bookingFieldStyle = ({
    display: "flex",
    justifyContent: "space-between",
    marginInline: "40px"
});

const bookingsListClient: React.CSSProperties = {
    display:"flex",
    flexDirection:"column"
}

const bookingItemStyle = (hasNoBookings?: boolean) => ({
    width: "600px",
    backgroundColor: "#FFFFFF",
    padding: "10px",
    margin: "30px",
    borderRadius: '8px',
    boxShadow: '0px 0px 8px 0px rgba(0,0,0,0.3)',
    listStyleType: "none",
    cursor: hasNoBookings ? "" : "pointer",

});

const bookingItemOwnerRow1Style: React.CSSProperties = {
    display:"flex",
    justifyContent:"space-between",
    marginInline:"40px"
}

const bookingItemOwnerRow2Style: React.CSSProperties = {
    display:"flex",
    flexDirection:"row",
    alignItems:"center"
}

const paginationButtonStyle = {
    padding: '8px 16px',
    margin: '4px',
    backgroundColor: '#4CAF50',
    color: 'white',
    border: 'none',
    borderRadius: '50%',
    width: "40px",
    height: "40px",
    cursor: 'pointer',
    outline: 'none',
    fontSize: 18
};