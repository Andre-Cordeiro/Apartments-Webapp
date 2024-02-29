import React, { SetStateAction, useEffect, useState } from "react";
import { useAppDispatch, useApartmentSelector, useUserSelector } from "../../store/hooks";
import {loadApartments, loadOwnerApartments, setFilter} from "../../store/apartments";
import { Role } from "../types";
import { useLocation, useNavigate } from "react-router-dom";
import {ApartmentInfoDTO} from "../../api";


export const useInput = (
  initialValue: string,
  placeholder: string
): [JSX.Element, string, React.Dispatch<SetStateAction<string>>] => {
  const [search, setSearch] = useState<string>(initialValue);

  const input = (
    <input
      type="text"
      placeholder={placeholder}
      value={search}
      onChange={(e) => setSearch(e.target.value)}
    />
  );

  return [input, search, setSearch];
};

const MAX_APARTMENTS_PER_PAGE = 10;

const ApartCardView = ({ apart, selected, onClick }: { apart: ApartmentInfoDTO, selected?: boolean, onClick: ()=> void }) => {
    const userRole = useUserSelector(state => state.role)
    const isClient = userRole === Role.CLIENT;
    const isOwner = userRole === Role.OWNER;

    if (isClient || isOwner) {
        return (
            <li style={cardStyle(selected)} onClick={onClick}>
                <img style={imageStyle} src={apart.pictures} alt={""}/>
                <p>{apart.name}</p>
                {isClient ? (
                    <div style={{ display: "flex", justifyContent: "space-evenly" }}>
                        <p>{apart.location}</p>
                        <p style={{ color: "green" }}>${apart.price}</p>
                    </div>
                ) : (
                    <p>by {apart.location}</p>
                )}
            </li>
        );
    }
    return null;
}

const ApartmentsListView = ({ aparts, selected, setSelected }: { aparts: ApartmentInfoDTO[], selected: number | undefined, setSelected: React.Dispatch<SetStateAction<number | undefined>> }) => {
    const navigate = useNavigate()
    const path = useLocation().pathname

    return <div style={{}}>
        <ul style={{display:"flex", flexWrap:"wrap", justifyContent:"space-between"}}>
            {aparts.map((apart, i) =>
            <ApartCardView
                key={i}
                apart={apart}
                selected={i === selected}
                onClick={() => navigate(path + "/"+ apart.id)} />)}
        </ul>
    </div>
}

export const ApartmentsList = () => {

    const user = useUserSelector(state => state.username)
    const userRole = useUserSelector(state => state.role)
    const apartments = useApartmentSelector(state => state.apartments)
    const dispatch = useAppDispatch()
    
    const [selected, setSelected] = useState<number | undefined>(undefined)
    
    const [inputTitle, searchTitle, setSearchTitle] = useInput("", "Search by name")
    
    useEffect(() => {
        if(user){
            switch (userRole) {
                case Role.CLIENT:
                    dispatch(loadApartments())
                    break;
                case Role.OWNER:
                    dispatch(loadOwnerApartments(user))
                    break;
            }
        }
    }, [userRole, user, dispatch]);


    useEffect(() => { dispatch(setFilter(searchTitle)) }, [searchTitle, dispatch])

    const filteredAparts = apartments.filter(apart => apart.name?.includes(searchTitle))
    const [currentPage, setCurrentPage] = useState(1);
    const getApartmentsForPage = (page: number): ApartmentInfoDTO[] => {
        const startIndex = (page - 1) * MAX_APARTMENTS_PER_PAGE;
        const endIndex = startIndex + MAX_APARTMENTS_PER_PAGE;
        return filteredAparts.slice(startIndex, endIndex);
    };

    const totalPages = Math.ceil(filteredAparts.length / MAX_APARTMENTS_PER_PAGE);
    const apartmentsForPage = getApartmentsForPage(currentPage);

    return (
        <div>
            <div>
                <span>Search: </span>
                <input
                    type="text"
                    placeholder="Search by name"
                    value={searchTitle}
                    onChange={(e) => setSearchTitle(e.target.value)}
                    style={inputTitleStyle}
                />
            </div>
            <div style={{ display: 'flex', justifyContent: 'center', marginTop: '20px' }}>
                <ApartmentsListView
                    aparts={apartmentsForPage}
                    selected={selected}
                    setSelected={setSelected} />
            </div>
            <div>
                {Array.from({ length: totalPages }, (_, index) => index + 1).map(page => (
                    <button key={page} onClick={() => setCurrentPage(page)} disabled={currentPage === page} style={paginationButtonStyle}>
                        {page}
                    </button>
                ))}
            </div>
        </div>
    );
}

const inputTitleStyle = {
    marginBottom: '20px',
    padding: '8px',
    borderRadius: '4px',
    border: '1px solid #ccc',
    width: '200px',
    outline: 'none',
};

const cardStyle = (selected?: boolean) => ({
    width: '200px',
    backgroundColor: '#FFFFFF',
    padding: '20px',
    margin: '10px',
    borderRadius: '8px',
    boxShadow: selected ? '0px 0px 16px 0px rgba(255,0,0,0.75)' : '0px 0px 8px 0px rgba(0,0,0,0.1)',
    cursor: 'pointer',
    transition: 'box-shadow 0.3s ease',
    listStyleType: 'none',
});

const imageStyle: React.CSSProperties = {
    width: '100%', // Make the image take up 100% of the container width
    height: '150px', // Set a fixed height for uniformity
    objectFit: 'cover', // Maintain aspect ratio and cover the container
    borderRadius: '8px', // Apply border radius to the image
};

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