package pt.unl.fct.di.project.presentation.controller

import pt.unl.fct.di.project.presentation.dto.ClientInfoDTO
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import pt.unl.fct.di.project.service.BookingService
import org.springframework.web.bind.annotation.RestController
import pt.unl.fct.di.project.presentation.api.*
import pt.unl.fct.di.project.presentation.dto.*
import java.security.InvalidKeyException

@RestController
class BookingController(val bookingService : BookingService) : BookingAPI {

    override fun getBooking(bookingID: Long): BookingInfoDTO {
        val booking = bookingService.getBooking(bookingID)
        val apart = booking.bookedApartment
        return BookingInfoDTO(booking.id, booking.client.name, booking.checkIn, booking.checkOut, booking.guests, booking.state.toString(), ApartmentInfoDTO(apart.id, apart.name, apart.description, apart.location, apart.amenities, apart.pictures, apart.price))
    }

    override fun getBookingReview(bookingID: Long): ReviewInfoDTO {
        val review = bookingService.getBookingReview(bookingID)
        return ReviewInfoDTO(review.clientReviews.name, review.description, review.rating)
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(InvalidKeyException::class)
    fun notFound(){}

}