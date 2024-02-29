import { createSlice, PayloadAction } from '@reduxjs/toolkit'
import { Configuration, CreateReviewDTO, ReviewInfoDTO} from "../api";
import * as api from "../api";


export interface State {
    reviews: ReviewInfoDTO[],
    loading: boolean,
    uploading: boolean
}

const initialState: State = {
    reviews: [],
    loading: false,
    uploading: false,
};

export const slice = createSlice({
    name: 'reviews',
    initialState,
    reducers: {
        addReview: (state, action:PayloadAction<ReviewInfoDTO>) => {
            state.reviews.push(action.payload)
        },
        setReviews: (state, action: PayloadAction<ReviewInfoDTO[]>) => {
            state.reviews = action.payload
        },
        setLoading: (state, action:PayloadAction<boolean>) => {
            state.loading = action.payload
        },
        setUploadingReview: (state, action:PayloadAction<boolean>) => {
            state.uploading = action.payload
        },
    }
});

export const { addReview, setReviews, setLoading, setUploadingReview } = slice.actions

export const loadApartmentReviews = (apartmentId: number) => (dispatch: any, getState: any) => {

    dispatch(setLoading(true))
    const token = getState().user.token

    let config : Configuration = {
        accessToken: token
    }
    let instance = new api.ApartmentControllerApi(config)
    instance.getApartmentReviews(apartmentId).then((reviews) => {
        dispatch(setLoading(false));
        dispatch(setReviews(reviews));
    })
}

export const loadBookingReview = (bookingId: number) => (dispatch: any, getState: any) => {

    dispatch(setLoading(true))
    const token = getState().user.token

    let config : Configuration = {
        accessToken: token
    }
    let instance = new api.BookingControllerApi(config)
    instance.getBookingReview(bookingId).then((review) => {
        dispatch(setLoading(false));
        const reviews: ReviewInfoDTO[] = [review];
        dispatch(setReviews(reviews));
    })
}

export const createReview = (username: number, bookingId: number, review: CreateReviewDTO) => (dispatch: any, getState: any) => {

    dispatch(setUploadingReview(true))
    const token = getState().user.token

    let config : Configuration = {
        accessToken: token
    }
    let instance = new api.ReviewControllerApi(config)
    instance.createReview(review, username, bookingId).then((review) => {
        dispatch(setUploadingReview(false));
        dispatch(addReview(review));
    })
}

export default slice.reducer