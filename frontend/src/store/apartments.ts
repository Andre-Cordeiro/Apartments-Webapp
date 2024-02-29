import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import * as api from "../api"
import {ApartmentInfoDTO, Configuration} from "../api";


export interface State {
  apartments: ApartmentInfoDTO[];
  filter: string;
  loading: boolean;
}

const initialState: State = {
  apartments: [],
  filter: "",
  loading: false,
};

export const slice = createSlice({
  name: "apartments",
  initialState,
  reducers: {
    setApartments: (state, action: PayloadAction<ApartmentInfoDTO[]>) => {
      state.apartments = action.payload;
      state.loading = false;
    },
    setFilter: (state, action: PayloadAction<string>) => {
      state.filter = action.payload;
    },
    setLoading: (state, action: PayloadAction<boolean>) => {
      state.loading = action.payload;
    },
  },
});

export const { setApartments, setFilter, setLoading } =
  slice.actions;

export const loadApartments = () => (dispatch: any, getState: any) => {
  const token = getState().user.token

  let config : Configuration = {
    accessToken: token
  }
  let instance = new api.ApartmentControllerApi(config)
  instance.getApartments().then((apartments) => {
    dispatch(setApartments(apartments));
  })
};


export const loadOwnerApartments = (ownerId: number) => (dispatch: any, getState: any) => {
  const token = getState().user.token

  let config : Configuration = {
    accessToken: token,
  }
  let instance = new api.OwnerControllerApi(config)
  instance.getOwnerApartments(ownerId).then((apartments) => {
    dispatch(setApartments(apartments));
  })
};

export default slice.reducer;
