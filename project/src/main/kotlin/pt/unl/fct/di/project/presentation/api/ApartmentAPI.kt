package pt.unl.fct.di.project.presentation.api

import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.*
import pt.unl.fct.di.project.config.SecurityRules
import pt.unl.fct.di.project.presentation.dto.*
import java.time.LocalDate

@RequestMapping("/apartments")
interface ApartmentAPI {

    @GetMapping("/")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "List of apartments", content = [
            (Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ApartmentInfoDTO::class)))))]),
        ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()]),
    ])
    @SecurityRules.CanListApartments
    fun getApartments() : Collection<ApartmentInfoDTO>

    @GetMapping("/{apartmentId}")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Apartment info", content = [
            (Content(mediaType = "application/json", schema = Schema(implementation = ApartmentInfoDTO::class)))]),
        ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()]),
    ])
    @SecurityRules.CanListApartments
    fun getApartmentById(@PathVariable apartmentId: Long) : ApartmentInfoDTO

    @GetMapping("/{apartmentId}/periods/{periodId}")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "List of apartments", content = []),
        ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()]),
    ])
    fun getApartmentPeriod(@PathVariable apartmentId: Long, @PathVariable periodId: Long)

    @GetMapping("/{apartmentId}/periods")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "List of apartment periods", content = [
            (Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = PeriodInfoDTO::class)))))]),
        ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()]),
        ApiResponse(responseCode = "404", description = "Apartment not found.", content = [Content()]),
    ])
    @SecurityRules.CanGetApartmentPeriods
    fun getApartmentPeriods(@PathVariable apartmentId: Long) : Collection<PeriodInfoDTO>

    @GetMapping("/{apartmentId}/availablePeriods")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "List of available apartment periods", content = [
            (Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = AvailablePeriodInfoDTO::class)))))]),
        ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()]),
        ApiResponse(responseCode = "404", description = "Apartment not found.", content = [Content()]),
    ])
    @SecurityRules.CanListApartmentAvailablePeriods
    fun getApartmentAvailablePeriods(@PathVariable apartmentId: Long) : Collection<AvailablePeriodInfoDTO>

    @GetMapping("/periods")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "List of apartments", content = [
            (Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ApartmentInfoDTO::class)))))]),
        ApiResponse(responseCode = "403", description = "Forbidden.", content = [Content()]),
        ApiResponse(responseCode = "400", description = "Invalid period data.", content = [Content()]),
    ])
    @SecurityRules.CanGetApartmentPeriods
    fun getAvailableApartmentsForPeriod(@RequestParam startDate: LocalDate, @RequestParam endDate: LocalDate) : Collection<ApartmentInfoDTO>

    @PostMapping("/{apartmentId}/booking/{username}")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Apartment was successfully booked.", content = [
            (Content(mediaType = "application/json", schema = Schema(implementation = BookingInfoDTO::class)))]),
        ApiResponse(responseCode = "400", description = "Invalid booking data; Apartment has no available periods.", content = [Content()]),
        ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()]),
        ApiResponse(responseCode = "404", description = "Client not found; Apartment not found", content = [Content()]),
        ApiResponse(responseCode = "409", description = "Booking is unavailable for this period.", content = [Content()]),
    ])
    @SecurityRules.CanBookApartment
    fun bookApartment(@PathVariable username: Long, @PathVariable apartmentId: Long, @RequestBody booking: BookApartmentDTO) : BookingInfoDTO

    @PostMapping("/{apartmentId}/period")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Period added successfully.", content = [
            (Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = PeriodInfoDTO::class)))))]),
        ApiResponse(responseCode = "400", description = "Invalid period date.", content = [Content()]),
        ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()]),
        ApiResponse(responseCode = "404", description = "Apartment not found.", content = [Content()]),
        ApiResponse(responseCode = "409", description = "Period already exists in this date.", content = [Content()]),
    ])
    @SecurityRules.CanCreateApartmentPeriod
    fun createApartmentPeriod(@PathVariable apartmentId: Long, @RequestBody period: PeriodCreateDTO) : Collection<PeriodInfoDTO>

    @GetMapping("/{apartmentId}/reviews")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "List of Apartment's Reviews.", content = [
            (Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ReviewInfoDTO::class)))))]),
        ApiResponse(responseCode = "404", description = "Apartment not found.")
    ])
    @SecurityRules.CanListApartmentReviews
    fun getApartmentReviews(@PathVariable apartmentId: Long) : Collection<ReviewInfoDTO>
}