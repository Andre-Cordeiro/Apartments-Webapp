package pt.unl.fct.di.project.service

import org.springframework.stereotype.Service
import pt.unl.fct.di.project.data.dao.Booking
import pt.unl.fct.di.project.data.dao.Review
import pt.unl.fct.di.project.data.repository.BookingRepository
import pt.unl.fct.di.project.presentation.dto.*
import java.security.InvalidKeyException
import java.util.NoSuchElementException

@Service
class BookingService(val bookings : BookingRepository) {

    fun getBooking(bookingID: Long): Booking {
        return bookings.findById(bookingID).orElseThrow { InvalidKeyException("Booking not found.") }
    }

    fun getBookingReview(bookingID: Long): Review {
        return bookings.getBookingReview(bookingID)
    }
}