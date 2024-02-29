package pt.unl.fct.di.project.config

import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import pt.unl.fct.di.project.data.repository.ApartmentRepository
import pt.unl.fct.di.project.data.repository.BookingRepository

@Component("securityService")
class SecurityService(val bookings : BookingRepository, val apartments : ApartmentRepository) {

    fun isTheOneRequesting(principal: Authentication, userId: Long): Boolean {
        val cond = userId.toString() == principal.name

        if (!cond)
            println("Logged User: " + principal.name + "; Impostor: " + userId)
        return cond
    }

    fun isApartmentOwner(principal: Authentication, apartmentId: Long): Boolean {
        val apart = apartments.findById(apartmentId).get()
        val cond = apart.owner.username.toString() == principal.name

        if (!cond)
            println("Not the owner of the apartment")
        return cond
    }

    fun isApartmentOwnerOfBooking(principal: Authentication, bookingId: Long): Boolean {
        val b = bookings.findById(bookingId).get()
        val cond = b.bookedApartment.owner.username.toString() == principal.name

        if (!cond)
            println("Not the apartment owner of the booking")
        return cond
    }

    fun isBookingClient(principal: Authentication, bookingId: Long): Boolean {
        val b = bookings.findById(bookingId).get()
        val cond = b.client.username.toString() == principal.name

        if (!cond)
            println("Not the client that booked")
        return cond
    }
}