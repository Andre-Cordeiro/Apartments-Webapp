import React from "react";
import { Link } from "react-router-dom";
import {Role} from "./types";
import {useUserSelector} from "../store/hooks";

export const columnStyle: React.CSSProperties = {
    display: 'flex',
    justifyContent: 'space-evenly',
    marginTop: '24px',
};

export const HomePage = () => {
    const user = useUserSelector((state) => state.username);
    const role = useUserSelector((state) => state.role);

    const landingPage = (
        <div>
            <section style={sectionStyle}>
                <h2 style={h2Style}>Discover UrbanLux Apartments</h2>
                <p style={pStyle}>
                    Explore our exclusive apartments and immerse yourself in luxury within a sophisticated and elegant environment.
                </p>
                <button style={buttonStyle}>Learn More</button>
            </section>
            <section style={sectionStyle}>
                <h2 style={h2Style}>About UrbanLux Apartments</h2>
                <p style={pStyle}>
                    UrbanLux Apartments is committed to delivering the finest in accommodations, with apartments designed to provide unmatched comfort and elegance.
                </p>
            </section>
            <section style={sectionStyle}>
                <h2 style={h2Style}>What Our Clients Say</h2>
                <div>
                    <p style={testimonialStyle}>
                        "My stay at UrbanLux Apartments was simply amazing. The luxury and exceptional service made my trip memorable."
                    </p>
                    <p>- Maria S., Satisfied Customer</p>
                </div>
            </section>
        </div>
    );

    const commonContent = (
        <div style={{ ...columnStyle, flexDirection: 'column' }}>
            <div style={buttonContainerStyle}>
                <Link to="/apartments" style={buttonStyle}>
                    {role === Role.CLIENT ? 'Check Apartments' : 'My Apartments'}
                </Link>
            </div>
            <div style={buttonContainerStyle}>
                <Link to="/bookings" style={buttonStyle}>
                    {role === Role.CLIENT ? 'My Bookings' : 'My Apartment Bookings'}
                </Link>
            </div>
            <div>{landingPage}</div>
        </div>
    );

    const manager = (
        <div style={columnStyle}>
            <div>
            </div>
        </div>
    );

    const renderSwitch = (role: string): JSX.Element => {
        switch (role) {
            case Role.CLIENT:
                return commonContent;
            case Role.OWNER:
                return commonContent;
            case Role.MANAGER:
                return manager;
            default:
                return <div></div>;
        }
    };

    return (user && role) ? renderSwitch(role): landingPage;
};

const sectionStyle: React.CSSProperties = {
    padding: '50px',
    textAlign: 'center',
    borderBottom: '1px solid #ddd',
};

const h2Style: React.CSSProperties = {
    marginBottom: '20px',
    fontSize: '32px',
    color: '#333',
};

const pStyle: React.CSSProperties = {
    fontSize: '18px',
    lineHeight: '1.6',
    color: '#555',
};

const buttonContainerStyle: React.CSSProperties = {
    marginBottom: '16px',
};

const buttonStyle: React.CSSProperties = {
    display: 'inline-block',
    padding: '12px 24px',
    fontSize: '18px',
    color: '#fff',
    backgroundColor: '#4CAF50',
    textDecoration: 'none',
    border: '1px solid #4CAF50',
    borderRadius: '4px',
    cursor: 'pointer',
    transition: 'background-color 0.3s ease',
    marginRight: '12px',
};

const testimonialStyle: React.CSSProperties = {
    fontStyle: 'italic',
    fontSize: '16px',
    marginBottom: '10px',
};