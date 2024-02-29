import React, { useState } from "react";
import {useAppDispatch} from "../../store/hooks";
import {sendLogin} from "../../store/user";
import {useNavigate} from "react-router-dom";


export const Login = () => {
    const navigate = useNavigate()
    const dispatch = useAppDispatch();

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const handleUsernameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setUsername(e.target.value);
    };

    const handlePasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setPassword(e.target.value);
    };

    const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        const numericUsername = parseInt(username, 10);
        dispatch(sendLogin(numericUsername, password));
        setUsername("");
        setPassword("");
        navigate("");
    };

    return (
        <form onSubmit={handleSubmit} style={{ display: "flex", flexDirection: "column", alignItems: "right" }}>
            <label>
                Username:
                <input
                    type="number"
                    value={username}
                    onChange={handleUsernameChange}
                    required
                    style={inputStyle}
                />
            </label>
            <label>
                Password:
                <input
                    type="password"
                    value={password}
                    onChange={handlePasswordChange}
                    required
                    style={inputStyle}
                />
            </label>
            <button type="submit" style={submitStyle}>
                Login
            </button>
        </form>
    );
}

const inputStyle: React.CSSProperties = {
    padding: '2px',
    marginBottom: '10px',
    width: '100%',
};

const submitStyle: React.CSSProperties = {
    padding: '10px 20px',
    backgroundColor: '#4CAF50',
    color: 'white',
    border: 'none',
    borderRadius: '4px',
    cursor: 'pointer',
    transition: 'background-color 0.3s ease',
    width: '100%',
    marginTop: "10px"
};