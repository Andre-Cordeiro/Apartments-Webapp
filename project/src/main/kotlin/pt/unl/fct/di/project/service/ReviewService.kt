package pt.unl.fct.di.project.service

import pt.unl.fct.di.project.data.repository.ClientRepository
import pt.unl.fct.di.project.data.repository.ReviewRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pt.unl.fct.di.project.data.State
import pt.unl.fct.di.project.data.dao.Review
import pt.unl.fct.di.project.data.repository.BookingRepository
import pt.unl.fct.di.project.presentation.dto.ClientInfoDTO
import pt.unl.fct.di.project.presentation.dto.ReviewInfoDTO
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.InvalidParameterException
import java.security.KeyStoreException

@Service
class ReviewService(val reviews : ReviewRepository, val clients : ClientRepository, val bookings: BookingRepository) {

    @Transactional
    fun createReview(username : Long, bookingId: Long, description : String, rating : Long) : Review {

        val reviewRange = 1..5
        if(!reviewRange.contains(rating))
            throw InvalidParameterException("Invalid Review data.")

        val booking = bookings.findById(bookingId).orElseThrow {InvalidKeyException("Booking not found.")}
        val opClient = clients.findById(username)

        if (opClient.isEmpty || !opClient.get().bookings.contains(booking))
            throw InvalidAlgorithmParameterException("Wrong client.")

        if (booking.state != State.AWAITING_REVIEW)
            throw KeyStoreException("Booking already reviewed.")

        val client = opClient.get()

        val review = Review(0, description, rating, client, booking)
        booking.bookingReview = review
        booking.state = State.CLOSED
        client.reviews.add(review)
        return review
    }

    fun getReview(reviewId: Long): Review {
        return reviews.findById(reviewId).orElseThrow { InvalidKeyException("Review not found.") }

    }

    fun getAllReviews() : MutableIterable<Review> {
        return reviews.findAll()
    }
}