package pt.unl.fct.di.project.presentation.api

import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import pt.unl.fct.di.project.presentation.dto.ClientInfoDTO
import org.springframework.web.bind.annotation.*
import pt.unl.fct.di.project.config.SecurityRules
import pt.unl.fct.di.project.presentation.dto.*


@RequestMapping("/clients")
interface ClientAPI {

    @PostMapping("/")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Client was successfully created", content = [Content()]),
        ApiResponse(responseCode = "400", description = "Invalid Client data", content = [Content()]),
        ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()]),
        ApiResponse(responseCode = "409", description = "Username unavailable", content = [Content()]),
    ])
    fun createClient(@RequestBody client: ClientInfoDTO)

    @GetMapping("/{username}")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Client that is in the system", content = [
            (Content(mediaType = "application/json", schema = Schema(implementation = ClientInfoDTO::class)))]),
        ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()]),
        ApiResponse(responseCode = "404", description = "Client not found.", content = [Content()])
    ])
    fun getClient(@PathVariable username : Long) : ClientInfoDTO


    @GetMapping("/{username}/bookings")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "List of client bookings", content = [
            (Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = BookingInfoDTO::class)))))]),
        ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()]),
        ApiResponse(responseCode = "404", description = "Client not found.", content = [Content()])]
    )
    @SecurityRules.CanGetClientBookings
    fun getClientBookings(@PathVariable username : Long) : Collection<BookingInfoDTO>


    @GetMapping("/{username}/reviews")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "List of client reviews", content = [
            (Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ReviewInfoDTO::class)))))]),
        ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()]),
        ApiResponse(responseCode = "404", description = "Client not found.", content = [Content()])]
    )
    @SecurityRules.CanGetClientReviews
    fun getClientReviews(@PathVariable username : Long) : Collection<ReviewInfoDTO>


    @PostMapping("/{username}/{bookingId}/checkedIn")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Guest Checked In", content = [Content()]),
        ApiResponse(responseCode = "400", description = "Booking has not been accepted yet or has been rejected; Check-in day hasn't arrived yet.", content = [Content()]),
        ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()]),
        ApiResponse(responseCode = "404", description = "Booking not found; Client not found", content = [Content()]),
    ])
    @SecurityRules.CanCheckIn
    fun checkIn(@PathVariable username : Long, @PathVariable bookingId: Long)

    @PostMapping("/{username}/{bookingId}/checkedOut")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Guest Checked Out", content = [Content()]),
        ApiResponse(responseCode = "400", description = "Client has not checked in yet or booking has been rejected; Check-out day hasn't arrived yet.", content = [Content()]),
        ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()]),
        ApiResponse(responseCode = "404", description = "Booking not found; Client not found.", content = [Content()])]
    )
    @SecurityRules.CanCheckOut
    fun checkOut(@PathVariable username : Long, @PathVariable bookingId: Long)

    @PostMapping("/{username}/{bookingId}/cancelled")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Booking canceled successfully", content = [Content()]),
        ApiResponse(responseCode = "400", description = "Booking cant be cancelled", content = [Content()]),
        ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()]),
        ApiResponse(responseCode = "404", description = "Booking not found; Client not found", content = [Content()]),
    ])
    @SecurityRules.CanCancel
    fun cancelBooking(@PathVariable username : Long, @PathVariable bookingId: Long)
}