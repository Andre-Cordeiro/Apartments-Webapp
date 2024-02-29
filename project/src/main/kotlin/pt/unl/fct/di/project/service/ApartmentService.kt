package pt.unl.fct.di.project.service

import pt.unl.fct.di.project.data.repository.ClientRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pt.unl.fct.di.project.data.State
import pt.unl.fct.di.project.data.dao.Apartment
import pt.unl.fct.di.project.data.dao.Booking
import pt.unl.fct.di.project.data.dao.Period
import pt.unl.fct.di.project.data.dao.Review
import pt.unl.fct.di.project.data.repository.ApartmentRepository
import pt.unl.fct.di.project.data.repository.BookingRepository
import pt.unl.fct.di.project.data.repository.PeriodRepository
import pt.unl.fct.di.project.presentation.dto.PeriodCreateDTO
import java.security.InvalidKeyException
import java.security.InvalidParameterException
import java.security.KeyStoreException
import java.time.LocalDate
import java.time.LocalDate.now

@Service
class ApartmentService(val clients : ClientRepository, val bookings : BookingRepository, val apartments : ApartmentRepository) {


    fun getApartments() : MutableIterable<Apartment> {
        return apartments.findAll()
    }

    fun getApartmentById(apartmentId: Long) : Apartment {
        return apartments.findById(apartmentId).get()
    }

    fun getApartmentPeriods(apartmentId: Long) : Collection<Period> {
        return apartments.getApartPeriods(apartmentId)
    }

    fun getApartmentAvailablePeriods(apartmentId: Long) : Collection<Period> {
        return apartments.getApartAvailablePeriods(apartmentId)
    }

    fun getAvailableApartmentsForPeriod(startDate: LocalDate, endDate: LocalDate) : MutableList<Apartment> {
        if(endDate.isBefore(startDate) || startDate.isBefore(now()))
            throw InvalidParameterException("Invalid period data.")

        val datesList = startDate.datesUntil(endDate).toList().plus(endDate)
        val availableAparts = apartments.getAvailableApartmentsForPeriod(startDate, endDate)
        val result = mutableListOf<Apartment>()
        availableAparts.forEach { apartment ->
            if (datesList.all { date -> apartment.periods.any { it.date == date }})
                result.add(apartment)
        }
        return result
    }

    @Transactional
    fun bookApartment(username : Long, apartmentId: Long, checkIn : LocalDate, checkOut : LocalDate, guests : Long) : Booking {
        if(checkOut.isBefore(checkIn) || checkIn.isBefore(now().minusDays(1)) || guests > 4 || guests < 0)
            throw InvalidParameterException("Invalid booking data.")

        val client = clients.findById(username).orElseThrow { InvalidKeyException("Client not found with id: $username") }
        val apart = apartments.findById(apartmentId).orElseThrow { InvalidKeyException("Apartment not found with id: $apartmentId") }
        val availablePeriods = apartments.getApartAvailablePeriods(apartmentId)

        if(availablePeriods.isEmpty())
            throw InvalidParameterException("Apartment has no available periods.")

        val bookingDatesList = checkIn.datesUntil(checkOut).toList().plus(checkOut)

        if (!bookingDatesList.all { date -> availablePeriods.any { it.date == date && it.available } } ||
            !availablePeriods.any { it.date.isEqual(checkIn) } || !availablePeriods.any { it.date.isEqual(checkOut) }) {
            throw KeyStoreException("Booking period is not available for this apartment.")
        }

        val periodsToUpdate = availablePeriods.filter { it.date.isEqual(checkIn) || it.date.isEqual(checkOut) || (it.date.isAfter(checkIn) && it.date.isBefore(checkOut)) }
        periodsToUpdate.forEach { it.available = false }

        val booking = Booking(0, checkIn, checkOut, guests, State.UNDER_CONSIDERATION, client, apart, null)
        bookings.save(booking)
        return booking
    }

    @Transactional
    fun createApartmentPeriod(apartmentId: Long, startDate: LocalDate, endDate: LocalDate) : MutableList<Period> {

        if(endDate.isBefore(startDate) || startDate.isBefore(now()))
            throw InvalidParameterException("Invalid period date.")

        val apartment = apartments.findByIdWithPeriods(apartmentId).orElseThrow { InvalidKeyException("Apartment not found") }
        val apartPeriods = apartment.periods

        val periodDatesList = startDate.datesUntil(endDate).toList().plus(endDate)
        if (apartPeriods.any { it.date in periodDatesList }) {
            throw KeyStoreException("Period already exists in this date.")
        }

        periodDatesList.forEach {
            val period = Period(0, it, true, apartment)
            apartPeriods.add(period)
        }
        return apartPeriods
    }

    fun getApartmentReviews(apartmentId: Long) : MutableList<Review> {
        val apartment = apartments.findById(apartmentId).orElseThrow{InvalidKeyException("Apartment not found.")}
        val apartmentReviews = mutableListOf<Review>()
        apartment.bookings.map { it.bookingReview?.let { it1 -> apartmentReviews.add(it1) } }
        return apartmentReviews
    }
}