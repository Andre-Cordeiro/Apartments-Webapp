import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import {jwtDecode} from "jwt-decode";
import {User} from "../components/types";

export type State = User;

const initialState: State = {
    username: undefined,
    role: undefined,
    token: undefined,
};

export const slice = createSlice({
    name: "user",
    initialState,
    reducers: {
        setUser: (state, action: PayloadAction<User>) => {
            return action.payload;
        },
        setUsername: (state, action: PayloadAction<number>) => {
            state.username = action.payload;
        },
        setRole: (state, action: PayloadAction<string>) => {
            state.role = action.payload;
        },
        setToken: (state, action: PayloadAction<string>) => {
            state.token = action.payload;
        },
        removeUser: (state) => {
            return initialState;
        }
    },
});

export const { setUser, setUsername, setRole, setToken, removeUser } = slice.actions;

export const sendLogin = (username: number, password: string) => (dispatch: any) => {
    fetch("/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json",
            "X-Requested-With" : "XMLHttpRequest"
        },
        body: JSON.stringify({
            username: username,
            password: password,
        }),
    })
    .then(async (response) => {
        if (response.ok) {
            if (response.headers.get("authorization") != null) {
                let tokenString = response.headers.get("authorization")!.substring(7);
                let decoded: { roles: string[] } = jwtDecode(tokenString)

                let role = decoded.roles[0];
                let u = ({username: username, role: role, token: tokenString}) as User;
                dispatch(setUser(u));
                dispatch(setToken(tokenString));
            }
            alert("User Logged-In")
        } else {
            alert("Invalid credentials");
            return;
        }
    })
};

export default slice.reducer;
