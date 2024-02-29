package pt.unl.fct.di.project.presentation.api

import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.*
import pt.unl.fct.di.project.config.SecurityRules
import pt.unl.fct.di.project.presentation.dto.ApartmentInfoDTO
import pt.unl.fct.di.project.presentation.dto.BookingInfoDTO

@RequestMapping("/owners")
interface OwnerAPI {

    @PostMapping("/")
    fun createOwner()

    @GetMapping("/{ownerId}/apartments")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Owner Apartments.", content = [
            (Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ApartmentInfoDTO::class)))))]),
        ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()]),
        ApiResponse(responseCode = "404", description = "Owner not found.", content = [Content()])]
    )
    @SecurityRules.CanGetOwnerApartments
    fun getOwnerApartments(@PathVariable ownerId: Long) : Collection<ApartmentInfoDTO>

    @GetMapping("/{ownerId}/apartments-bookings")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Bookings for each apartment owned.", content = [
            (Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = BookingInfoDTO::class)))))]),
        ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()]),
        ApiResponse(responseCode = "404", description = "Owner not found.", content = [Content()])]
    )
    @SecurityRules.CanGetOwnerApartments
    fun getOwnerBookings(@PathVariable ownerId: Long) : Collection<BookingInfoDTO>

    @GetMapping("/{ownerId}")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Owner.", content = [Content()]),
        ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()]),
        ApiResponse(responseCode = "404", description = "Owner not found.", content = [Content()])]
    )
    fun getOwner(@PathVariable ownerId: Long)


    @PutMapping("/{ownerId}/bookings/{bookingId}")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Status changed successfully.", content = [Content()]),
        ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()]),
        ApiResponse(responseCode = "400", description = "Invalid decision.", content = [Content()]),
        ApiResponse(responseCode = "404", description = "Booking not found; Owner not found.", content = [Content()]),
        ApiResponse(responseCode = "409", description = "Booking is not Under Consideration.", content = [Content()]),
    ])
    @SecurityRules.CanChangeBookingStatus
    fun changeBookingStatus(@PathVariable ownerId: Long, @PathVariable bookingId: Long, @RequestBody decision : String)
}