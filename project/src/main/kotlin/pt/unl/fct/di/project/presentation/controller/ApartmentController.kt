package pt.unl.fct.di.project.presentation.controller

import pt.unl.fct.di.project.service.ApartmentService
import pt.unl.fct.di.project.presentation.api.ApartmentAPI
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import pt.unl.fct.di.project.presentation.dto.*
import java.security.InvalidKeyException
import java.security.InvalidParameterException
import java.security.KeyStoreException
import java.time.LocalDate

@RestController
class ApartmentController(val apartmentService : ApartmentService) : ApartmentAPI {

    override fun getApartments() : Collection<ApartmentInfoDTO> {
        return apartmentService.getApartments().map { ApartmentInfoDTO(it.id, it.name, it.description, it.location, it.amenities, it.pictures, it.price) }
    }

    override fun getApartmentById(apartmentId: Long) : ApartmentInfoDTO {
        val apartment = apartmentService.getApartmentById(apartmentId)
        return ApartmentInfoDTO (apartment.id, apartment.name, apartment.description, apartment.location, apartment.amenities, apartment.pictures, apartment.price)
    }

    override fun getApartmentPeriod(apartmentId: Long, periodId: Long) {
        TODO("Not yet implemented")
    }

    override fun getApartmentPeriods(apartmentId: Long) : Collection<PeriodInfoDTO> {
        val apartPeriods = apartmentService.getApartmentPeriods(apartmentId)
        return apartPeriods.map { PeriodInfoDTO(apartmentId, it.date, it.available) }
    }

    override fun getApartmentAvailablePeriods(apartmentId: Long) : Collection<AvailablePeriodInfoDTO> {
        val apartPeriods = apartmentService.getApartmentAvailablePeriods(apartmentId)
        return apartPeriods.map { AvailablePeriodInfoDTO(apartmentId, it.date) }
    }

    override fun getAvailableApartmentsForPeriod(startDate: LocalDate, endDate: LocalDate) : Collection<ApartmentInfoDTO> {
        val apartments = apartmentService.getAvailableApartmentsForPeriod(startDate, endDate)
        return apartments.map {ApartmentInfoDTO(it.id, it.name, it.description, it.location, it.amenities, it.pictures, it.price)}
    }


    override fun bookApartment(username : Long, apartmentId: Long, booking: BookApartmentDTO) : BookingInfoDTO {
        val b = apartmentService.bookApartment(username, apartmentId, booking.checkIn, booking.checkOut, booking.guests)
        val apart = b.bookedApartment
        return BookingInfoDTO(b.id, b.client.name, b.checkIn, b.checkOut, b.guests, b.state.toString(), ApartmentInfoDTO(apart.id, apart.name, apart.description, apart.location, apart.amenities, apart.pictures, apart.price))
    }

    override fun createApartmentPeriod(apartmentId: Long, period: PeriodCreateDTO) : Collection<PeriodInfoDTO> {
        val periods = apartmentService.createApartmentPeriod(apartmentId, period.startDate, period.endDate)
        return periods.map { PeriodInfoDTO(apartmentId, it.date, it.available) }
    }

    override fun getApartmentReviews(apartmentId: Long) : Collection<ReviewInfoDTO>{
        val reviews = apartmentService.getApartmentReviews(apartmentId)
        return reviews.map { ReviewInfoDTO (it.clientReviews.name, it.description, it.rating) }
    }

    @ResponseStatus(value= HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidParameterException::class)
    fun badRequest() {}

    @ResponseStatus(value= HttpStatus.NOT_FOUND)
    @ExceptionHandler(InvalidKeyException::class)
    fun notFound() {}

    @ResponseStatus(value= HttpStatus.CONFLICT)
    @ExceptionHandler(KeyStoreException::class)
    fun alreadyExists() {}

}