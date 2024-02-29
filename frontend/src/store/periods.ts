import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import {AvailablePeriodInfoDTO, Configuration, PeriodCreateDTO, PeriodInfoDTO} from "../api";
import * as api from "../api";


export interface State {
    periods: PeriodInfoDTO[];
    availablePeriods: AvailablePeriodInfoDTO[];
    loading: boolean;
    uploading: boolean,
}

const initialState: State = {
    periods: [],
    availablePeriods: [],
    loading: false,
    uploading: false,
};

export const slice = createSlice({
    name: "periods",
    initialState,
    reducers: {
        addPeriods: (state, action:PayloadAction<PeriodInfoDTO[]>) => {
            action.payload.map(period => state.periods.push(period))
        },
        setApartmentPeriods: (state, action: PayloadAction<PeriodInfoDTO[]>) => {
            state.periods = action.payload;
            state.loading = false;
        },
        setApartmentAvailPeriods: (state, action: PayloadAction<AvailablePeriodInfoDTO[]>) => {
            state.availablePeriods = action.payload;
            state.loading = false;
        },
        setLoading: (state, action: PayloadAction<boolean>) => {
            state.loading = action.payload;
        },
        setUploadingPeriods: (state, action:PayloadAction<boolean>) => {
            state.uploading = action.payload
        },
    },
});

export const { addPeriods, setApartmentPeriods, setApartmentAvailPeriods, setLoading, setUploadingPeriods } = slice.actions;

export const createPeriod = (apartmentId: number, period: PeriodCreateDTO) => (dispatch: any, getState: any) =>{
    dispatch(setUploadingPeriods(true))

    const token = getState().user.token

    let config : Configuration = {
        accessToken: token
    }

    let instance = new api.ApartmentControllerApi(config)
    instance.createApartmentPeriod(period, apartmentId).then((period) => {
        dispatch(setLoading(false))
        dispatch(addPeriods(period))
    })
}

export const loadApartmentPeriods = (apartmentId: number) => (dispatch: any, getState: any) => {
    dispatch(setLoading(true))
    const token = getState().user.token

    let config : Configuration = {
        accessToken: token
    }

    let instance = new api.ApartmentControllerApi(config)
    instance.getApartmentPeriods(apartmentId).then((periods) => {
        dispatch(setLoading(false))
        dispatch(setApartmentPeriods(periods))
    })
}

export const loadApartmentAvailablePeriods = (apartmentId: number) => (dispatch: any, getState: any) => {
    dispatch(setLoading(true))
    const token = getState().user.token

    let config : Configuration = {
        accessToken: token
    }

    let instance = new api.ApartmentControllerApi(config)
    instance.getApartmentAvailablePeriods(apartmentId).then((periods) => {
        dispatch(setLoading(false))
        dispatch(setApartmentAvailPeriods(periods))
    })
}

export default slice.reducer;
