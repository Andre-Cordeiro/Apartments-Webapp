package pt.unl.fct.di.project.presentation.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import pt.unl.fct.di.project.presentation.api.OwnerAPI
import org.springframework.web.bind.annotation.RestController
import pt.unl.fct.di.project.presentation.dto.ApartmentInfoDTO
import pt.unl.fct.di.project.presentation.dto.BookingInfoDTO
import pt.unl.fct.di.project.service.OwnerService
import java.security.InvalidKeyException
import java.security.InvalidParameterException
import java.security.KeyStoreException

@RestController
class OwnerController(val ownerService : OwnerService) : OwnerAPI {

    override fun createOwner() {
        TODO("Not yet implemented")
    }

    override fun getOwnerApartments(ownerId: Long) : Collection<ApartmentInfoDTO> {
        val owner = ownerService.getOwnerWithApartments(ownerId)
        return owner.apartments.map { ApartmentInfoDTO(it.id, it.name, it.description, it.location, it.amenities, it.pictures, it.price) }
    }

    override fun getOwnerBookings(ownerId: Long): Collection<BookingInfoDTO> {
        val bookings = ownerService.getOwnerBookings(ownerId)
        return bookings.map { val apartment = it.bookedApartment; BookingInfoDTO(it.id, it.client.name, it.checkIn, it.checkOut, it.guests, it.state.toString(), ApartmentInfoDTO(apartment.id, apartment.name, apartment.description, apartment.location, apartment.amenities, apartment.pictures, apartment.price ))}
    }

    override fun getOwner(ownerId: Long) {
        TODO("Not yet implemented")
    }

    override fun changeBookingStatus(ownerId: Long, bookingId: Long, decision: String) {
        ownerService.changeBookingStatus(ownerId, bookingId, decision)
    }

    @ResponseStatus(value= HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidParameterException::class)
    fun badRequest() {}

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(InvalidKeyException::class)
    fun notFound(){}

    @ResponseStatus(value= HttpStatus.CONFLICT)
    @ExceptionHandler(KeyStoreException::class)
    fun stateConflict() {}
}