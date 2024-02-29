import React, {useEffect} from "react";
import {useAppDispatch, useReviewSelector} from "../../store/hooks";
import {loadApartmentReviews, } from "../../store/reviews";
import {ReviewInfoDTO} from "../../api";


export const cardStyle = (selected?: boolean) => ({
    width: "500px",
    backgroundColor: "white",
    padding: "20px",
    margin: "10px",
    borderRadius: '8px',
    boxShadow: selected ? '0px 0px 16px 0px rgba(255,0,0,0.75)' : '0px 0px 8px 0px rgba(0,0,0,0.1)',
    listStyleType: "none",
    transition: "box-shadow 0.3s ease, border-color 0.3s ease",
    marginBottom: "40px"
});

export const StarRating = ({ rating }: {rating?: number}) => {
    const stars: JSX.Element[] = [];
    const maxRating = 5;
    const starSize = 25;

    for (let i = 1; i <= maxRating; i++) {
        if (rating && i <= rating) {
            stars.push(<span key={i} style={{ fontSize: `${starSize}px` }}>★</span>);
        } else {
            stars.push(<span key={i} style={{ fontSize: `${starSize}px` }}>☆</span>);
        }
    }
    return <div style={{ color: "#FFD700" }}>{stars}</div>;
};

const ReviewDetails= ({ review }: { review: ReviewInfoDTO }) => {
    return(
        <li style={cardStyle()}>
            <div style={{display:"flex", justifyContent:"space-between", alignItems: "center", marginInline:"20px"}}>
                <h4 style={{ marginRight: "10px", fontSize: 18 }}>{review.clientName}</h4>
                <StarRating rating={review.rating}/>
            </div>
            <div style={{display:"flex", flexDirection:"column",alignItems:"flex-start", textAlign:"justify", paddingInline:"20px", marginTop: "-20px"}}>
                <p>{review.description}</p>
            </div>
        </li>
    )
};

const hasReviews = (reviews: ReviewInfoDTO[]) => {
    return(reviews.length!==0)
}

export const ReviewsList = ({ apartmentId }: { apartmentId: number }) => {
    const reviews = useReviewSelector(state => state.reviews)
    const dispatch = useAppDispatch()

    useEffect(() => {
        dispatch(loadApartmentReviews(apartmentId))
    }, [apartmentId, dispatch]);

    return(
        <div style={{display:"flex", flexDirection:"column", alignItems:"left"}}>
            {hasReviews(reviews) ? (
                reviews.map((review, i) => (
                    <ReviewDetails key={i} review={review} />
                ))
            ) : (
                <div style={{marginBottom:"50px"}}>No reviews available for this apartment.</div>
            )}
        </div>)
}
