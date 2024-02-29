package pt.unl.fct.di.project.presentation.controller

import pt.unl.fct.di.project.presentation.dto.ClientInfoDTO
import pt.unl.fct.di.project.service.ClientService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import pt.unl.fct.di.project.presentation.api.*
import pt.unl.fct.di.project.presentation.dto.*
import java.security.InvalidKeyException

@RestController
class ClientController(val clientService : ClientService) : ClientAPI {

    override fun createClient(client: ClientInfoDTO) {
        TODO("Not yet implemented")
    }

    override fun getClient(username: Long): ClientInfoDTO {
        TODO("Not yet implemented")
    }

    override fun getClientBookings(username: Long): Collection<BookingInfoDTO> {
        val client = clientService.getClientWithBookings(username)
        val cBookings = client.bookings
        return cBookings.map { val apart = it.bookedApartment; BookingInfoDTO(it.id, it.client.name, it.checkIn, it.checkOut, it.guests, it.state.toString(), ApartmentInfoDTO(apart.id, apart.name, apart.description, apart.location, apart.amenities, apart.pictures, apart.price)) }
    }

    override fun getClientReviews(username: Long): Collection<ReviewInfoDTO> {
        val client = clientService.getClientWithReviews(username)
        val cReviews = client.reviews
        return cReviews.map { ReviewInfoDTO(client.name, it.description, it.rating) }
    }

    override fun checkIn(username: Long, bookingId: Long) {
        clientService.checkIn(username, bookingId)
    }

    override fun checkOut(username: Long, bookingId: Long) {
        clientService.checkOut(username, bookingId)
    }

    override fun cancelBooking(username: Long, bookingId: Long) {
        clientService.cancelBooking(username, bookingId)
    }

    @ResponseStatus(value= HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException::class)
    fun badRequest() {}


    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(InvalidKeyException::class)
    fun notFound(){}

}