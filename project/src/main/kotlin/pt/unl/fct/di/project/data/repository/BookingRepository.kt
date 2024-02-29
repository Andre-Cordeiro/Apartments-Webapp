package pt.unl.fct.di.project.data.repository

import jakarta.persistence.Id
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import pt.unl.fct.di.project.data.dao.Booking
import pt.unl.fct.di.project.data.dao.Period
import pt.unl.fct.di.project.data.dao.Review

import java.util.*

interface BookingRepository : CrudRepository<Booking, Long> {

    override fun findById(id: Long): Optional<Booking>

    override fun existsById(bookingID: Long) : Boolean

    @Query("select r from Booking b join b.bookingReview r where b.id = :bookingId")
    fun getBookingReview(bookingId: Long) : Review
}