package pt.unl.fct.di.project.service

import pt.unl.fct.di.project.data.repository.ClientRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pt.unl.fct.di.project.data.State
import pt.unl.fct.di.project.data.dao.Client
import pt.unl.fct.di.project.data.repository.BookingRepository
import java.security.InvalidKeyException
import java.time.LocalDate


@Service
class ClientService(val clients: ClientRepository, val bookings: BookingRepository) {

    fun getClientWithBookings(username: Long): Client {
        return clients.findById(username).orElseThrow { InvalidKeyException("Client not found with id: $username") }
    }

    fun getClientWithReviews(username: Long) : Client {
        return clients.findClientWithReviews(username).orElseThrow { InvalidKeyException("Client not found with id: $username") }
    }


    @Transactional
    fun checkIn(username : Long, bookingId: Long) {

        clients.findById(username).orElseThrow { InvalidKeyException("Client not found with id: $username") }
        val booking = bookings.findById(bookingId).orElseThrow { InvalidKeyException("Booking not found") }
        if(booking.state != State.BOOKED)
            throw IllegalArgumentException("Booking has not been accepted yet or has been rejected.")

        if(booking.checkIn != LocalDate.now())
            throw IllegalArgumentException("Check-in day hasn't arrived yet.")

        booking.state = State.OCCUPIED
    }


    @Transactional
    fun checkOut(username : Long, bookingId: Long) {

        clients.findById(username).orElseThrow { InvalidKeyException("Client not found with id: $username") }
        val booking = bookings.findById(bookingId).orElseThrow { InvalidKeyException("Booking not found") }
        if(booking.state != State.OCCUPIED)
            throw IllegalArgumentException("Client has not checked in yet or booking has been rejected.")

        if(booking.checkOut != LocalDate.now())
            throw IllegalArgumentException("Check-out day hasn't arrived yet.")

        booking.state = State.AWAITING_REVIEW
    }

    @Transactional
    fun cancelBooking(username : Long, bookingId: Long) {

        clients.findById(username).orElseThrow { InvalidKeyException("Client not found with id: $username") }
        val booking = bookings.findById(bookingId).orElseThrow { InvalidKeyException("Booking not found") }
        if(booking.state == State.OCCUPIED || booking.state == State.REJECTED || booking.state == State.AWAITING_REVIEW || booking.state == State.CLOSED)
            throw IllegalArgumentException("Booking cant be cancelled")

        val periods = booking.bookedApartment.periods
        periods.filter { it.date >= booking.checkIn && it.date <= booking.checkOut }
                .onEach { it.available = true }
        booking.state = State.REJECTED
    }
}
