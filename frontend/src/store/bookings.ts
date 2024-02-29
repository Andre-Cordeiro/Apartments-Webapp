import { createSlice, PayloadAction } from '@reduxjs/toolkit'
import {Status} from "../components/types";
import {BookApartmentDTO, BookingInfoDTO, Configuration} from "../api";
import * as api from "../api";

export type UpdateBookingState = {
    id: number;
    state: string;
}

export interface State {
    bookings: BookingInfoDTO[];
    loading: boolean,
    uploading: boolean,
    error: boolean;
}

const initialState: State = {
    bookings: [],
    loading: false,
    uploading: false,
    error: false,
};
export const slice = createSlice({
    name: 'bookings',
    initialState,
    reducers: {
        addBooking: (state, action:PayloadAction<BookingInfoDTO>) => {
            state.bookings.push(action.payload)
        },
        setBookings: (state, action: PayloadAction<BookingInfoDTO[]>) => {
            state.bookings = action.payload
        },
        updateBookingState: (state, action: PayloadAction<UpdateBookingState>) => {
            const ind = state.bookings.findIndex((item) => item.id === action.payload.id);
            state.bookings[ind].state = action.payload.state;
        },
        setLoading: (state, action:PayloadAction<boolean>) => {
            state.loading = action.payload
        },
        setUploadingBooking: (state, action:PayloadAction<boolean>) => {
            state.uploading = action.payload
        },
        setError: (state, action: PayloadAction<boolean>) => {
            state.error = action.payload;
        },
    }
});

export const { addBooking, updateBookingState,setBookings,  setLoading,  setUploadingBooking, setError} = slice.actions

export const loadClientBookings = (username: number) => (dispatch: any, getState: any) =>{
    dispatch(setLoading(true))
    const token = getState().user.token

    let config : Configuration = {
        accessToken: token
    }

    let instance = new api.ClientControllerApi(config)
    instance.getClientBookings(username).then((bookings ) => {
        dispatch(setLoading(false))
        dispatch(setBookings(bookings))
    })
}

export const createBooking = (apartmentId: number, username: number, booking: BookApartmentDTO) => (dispatch: any, getState: any) => {
    dispatch(setUploadingBooking(true))
    const token = getState().user.token

    let config : Configuration = {accessToken: token}
    let instance = new api.ApartmentControllerApi(config)

    instance.bookApartment(booking, username, apartmentId).then((booking) => {
        dispatch(setUploadingBooking(false));
        dispatch(addBooking(booking))
    })
}

export const cancelBooking = (username: number, bookingId: number) => (dispatch: any, getState: any) => {
    dispatch(setUploadingBooking(true))
    const token = getState().user.token
    let config : Configuration = {accessToken: token}
    let instance = new api.ClientControllerApi(config)
    instance.cancelBooking(username,bookingId).then(() => {
        dispatch(setUploadingBooking(false))
        dispatch(updateBookingState({id: bookingId, state: Status.REJECTED}))
    })
}

export const changeBookingStatus = (username: number, bookingId: number, decision: string) => (dispatch: any, getState: any) => {
    dispatch(setUploadingBooking(true))
    const token = getState().user.token
    let config : Configuration = {accessToken: token}
    let instance = new api.OwnerControllerApi(config)
    instance.changeBookingStatus(decision, username,bookingId).then(() => {
        dispatch(setUploadingBooking(false))
        dispatch(updateBookingState({id: bookingId, state: decision.toUpperCase()}))
    })
}

export const loadOwnerBookings = (ownerId: number) => (dispatch: any, getState: any) => {
    dispatch(setLoading(true))

    const token = getState().user.token

    let config : Configuration = {
        accessToken: token
    }

    let instance = new api.OwnerControllerApi(config)
    instance.getOwnerBookings(ownerId).then((bookings ) => {
        dispatch(setLoading(false))
        dispatch(setBookings(bookings))
    })
}

export const checkIn = (username: number, bookingId: number) => (dispatch: any, getState: any) => {
    dispatch(setUploadingBooking(true))
    const token = getState().user.token

    let config : Configuration = {accessToken: token}
    let instance = new api.ClientControllerApi(config)

    instance.checkIn(username,bookingId).then(() => {
        dispatch(setUploadingBooking(false))
        dispatch(updateBookingState({id: bookingId, state: Status.OCCUPIED}))
    })
}

export const checkOut = (username: number, bookingId: number) => (dispatch: any, getState: any) => {
    dispatch(setUploadingBooking(true))
    const token = getState().user.token

    let config : Configuration = {accessToken: token}
    let instance = new api.ClientControllerApi(config)

    instance.checkOut(username,bookingId).then(() => {
        dispatch(setUploadingBooking(false))
        dispatch(updateBookingState({id: bookingId, state: Status.AWAITING_REVIEW}))
    })
}


export const throwError = () => (dispatch: any) => {
    dispatch(setError(true));
    setTimeout(() => {
        dispatch(setError(false));
    }, 2000);
}

export default slice.reducer