import "./App.css";
import {BrowserRouter as Router, Routes, Route, useNavigate} from "react-router-dom";
import {PreviousPage} from "./components/util/previousPage";
import {HomePage} from "./components/HomePage";
import {Login} from "./components/util/login";
import {store} from "./store";
import {Provider} from "react-redux";
import React, {ReactNode} from "react";
import {ApartmentsList} from "./components/Apartments/Apartments";
import {BookingsList} from "./components/Bookings/BookingsList";
import {ApartmentDetails} from "./components/Apartments/ApartmentDetails";
import {BookingDetails} from "./components/Bookings/BookingDetails";


function App() {
  return (
    <div className="App">
      <Router>
        <Header title="UrbanLux Apartments">
            <Login/>
        </Header>
        <Container>
          <PreviousPage/>
          <Routes>
              <Route path="" element={<HomePage/>}/>
              <Route path="/apartments" element={<ApartmentsList/>}/>
              <Route path="/bookings" element={<BookingsList/>}/>
              <Route path="/apartments/:id" element={<ApartmentDetails/>}/>
              <Route path="/bookings/:id" element={<BookingDetails/>}/>
          </Routes>
        </Container>
      </Router>
    </div>
  );
}

export const Container = (props: { children?: ReactNode }) => (
    <div>{props.children}</div>
);

export const Header = (props: { title: string; children: ReactNode }) => (
    <header style={headerStyle}>
      <Banner title={props.title} />
      {props.children}
    </header>
);

const headerStyle : React.CSSProperties = {
    backgroundColor: '#222',
    fontFamily: 'Arial, sans-serif',
    fontStyle: "sans-serif",
    color: 'white',
    padding: '40px',
    justifyContent: "space-between",
    display: "flex",
};

interface BannerProps { title: string; }
const Banner = ({ title }: BannerProps) => {
  const navigate = useNavigate();

  return (
      <div style={{cursor:"pointer"}} onClick={() => navigate("/")}>
        <h2>{title}</h2>
      </div>
  );
};

const RdxApp = () => (
    <Provider store={store}>
        <App />
    </Provider>
);

export default RdxApp;
