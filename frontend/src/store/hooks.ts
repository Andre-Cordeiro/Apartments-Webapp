import { TypedUseSelectorHook, useDispatch, useSelector } from 'react-redux'
import type { State, AppDispatch } from './index'
import type { State as UserState } from './user'
import type { State as ApartmentState } from './apartments'
import type { State as BookingState } from './bookings'
import type { State as ReviewState } from './reviews'
import type { State as PeriodState } from './periods'

// Typed versions of useDispatch and useSelector hooks
export const useAppDispatch: () => AppDispatch = useDispatch
export const useAppSelector: TypedUseSelectorHook<State> = useSelector

export const useUserSelector: TypedUseSelectorHook<UserState> = 
    <T>(f:(state:UserState) => T) => useAppSelector((state:State) => f(state.user))

export const useApartmentSelector: TypedUseSelectorHook<ApartmentState> =
    <T>(f:(state:ApartmentState) => T) => useAppSelector((state:State) => f(state.apartments))

export const useBookingSelector: TypedUseSelectorHook<BookingState> =
    <T>(f:(state:BookingState) => T) => useAppSelector((state:State) => f(state.bookings))

export const useReviewSelector: TypedUseSelectorHook<ReviewState> =
    <T>(f:(state:ReviewState) => T) => useAppSelector((state:State) => f(state.reviews))

export const usePeriodSelector: TypedUseSelectorHook<PeriodState> =
    <T>(f:(state:PeriodState) => T) => useAppSelector((state:State) => f(state.periods))