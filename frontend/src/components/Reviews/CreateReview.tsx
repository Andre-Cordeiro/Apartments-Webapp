import {useAppDispatch, useUserSelector} from "../../store/hooks";
import {createReview} from "../../store/reviews";
import React, {useState} from "react";
import {Status} from "../types";
import {BookingInfoDTO} from "../../api";
import {useNavigate} from "react-router-dom";

export const CreateReview = ({ booking } : { booking: BookingInfoDTO }) => {

    const dispatch = useAppDispatch();
    const user = useUserSelector(state => state.username)

    const [description, setDescription] = useState<string>('');
    const [rating, setRating] = useState<number | undefined>(undefined);
    const [clientName, setClientName] = useState<string | undefined>(undefined);


    const navigate = useNavigate()

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();

        if (booking.state !== Status.AWAITING_REVIEW) {
            alert('You can only submit a review for a awaiting review booking.');
            return;
        }

        if (!description || rating === undefined) {
            alert('Please fill in both description and rating.');
            return;
        }

        if (rating < 1 && rating > 5 ) {
            alert('Rating must be between 1 and 5.');
            return;
        }

        if(user){
            dispatch(createReview(user, booking.id, {description, rating}));
            setClientName(booking.clientName);
            alert(`Review created for booking ${booking.id}`);
            navigate(-1)
        }

        setDescription('');
        setRating(undefined);
        setClientName('');
    };

    if (booking.state === Status.UNDER_CONSIDERATION || booking.state === Status.BOOKED || booking.state === Status.OCCUPIED) {
        return <p>You can submit a review after checkout.</p>;
    }

    const buttonStyle =  ({
        backgroundColor: '#4CAF50',
        color: 'white',
        padding: '10px',
        border: 'none',
        borderRadius: '4px',
        cursor: 'pointer',
        marginTop: '20px',
        fontWeight: "bold"
    });

    return (
        <form onSubmit={handleSubmit} style={{ marginTop: '16px', maxWidth: '400px', margin: 'auto' }}>
            <label style={{ marginBottom: '8px', display: 'block'}}>
                <span style={{ display: 'flex', marginBottom: '4px', alignItems: 'flex-start' }}> <strong>Description:</strong></span>
                <textarea
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                    style={{
                        width: '100%',
                        minHeight: '80px',
                        padding: '8px',
                        borderRadius: '4px',
                        border: '1px solid #ccc',
                        resize: 'vertical', fontSize:18 }}
                />
            </label>
            <label style={{ display: 'flex', flexDirection: 'column', marginBottom: '8px', alignItems: 'flex-start' }}>
                <strong>Rating:</strong>
                <select
                    value={rating !== undefined ? rating : ''}
                    onChange={(e) => setRating(Number(e.target.value))}
                    style={{ width: '50%', marginTop: '4px' }}
                >
                    <option value="" disabled selected>
                        Choose Rating
                    </option>
                    <option value="1">1</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="4">4</option>
                    <option value="5">5</option>
                </select>
            </label>
            <button type="submit" style={buttonStyle}>
                Submit Review
            </button>
        </form>
    );
};
