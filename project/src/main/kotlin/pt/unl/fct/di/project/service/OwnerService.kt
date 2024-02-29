package pt.unl.fct.di.project.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pt.unl.fct.di.project.data.State
import pt.unl.fct.di.project.data.dao.Booking
import pt.unl.fct.di.project.data.dao.Owner
import pt.unl.fct.di.project.data.repository.BookingRepository
import java.security.InvalidKeyException
import pt.unl.fct.di.project.data.repository.OwnerRepository
import java.security.InvalidParameterException
import java.security.KeyStoreException


@Service
class OwnerService(val bookings : BookingRepository, val owners : OwnerRepository) {

    fun getOwnerWithApartments(ownerId: Long) : Owner {
        return owners.findOwnerWithAparts(ownerId).orElseThrow { InvalidKeyException("Owner not found.") }
    }

    fun getOwnerBookings(ownerId: Long): Collection<Booking> {
        val owner = owners.findOwnerWithAparts(ownerId).orElseThrow { InvalidKeyException("Owner not found.") }
        return owner.apartments.flatMap { it.bookings }
    }

    @Transactional
    fun changeBookingStatus(ownerId: Long, bookingId: Long, decision: String) {
        owners.findById(ownerId).orElseThrow { InvalidKeyException("Owner not found.") }
        val booking = bookings.findById(bookingId).orElseThrow { InvalidKeyException("Booking not found.") }
        if(booking.state != State.UNDER_CONSIDERATION)
            throw KeyStoreException("Booking is not Under Consideration.")

        if (decision.uppercase().removeSurrounding("\"") == State.REJECTED.toString()){
            val periods = booking.bookedApartment.periods
            periods.filter { it.date >= booking.checkIn && it.date <= booking.checkOut }
                    .onEach { it.available = true }
            booking.state = State.REJECTED
        } else if(decision.uppercase().removeSurrounding("\"") == State.BOOKED.toString())
            booking.state = State.BOOKED
        else
            throw InvalidParameterException("Invalid decision.")
    }
}