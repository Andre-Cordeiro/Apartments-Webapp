package pt.unl.fct.di.project.presentation.api

import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.*
import pt.unl.fct.di.project.config.SecurityRules
import pt.unl.fct.di.project.presentation.dto.BookingInfoDTO
import pt.unl.fct.di.project.presentation.dto.ReviewInfoDTO


@RequestMapping("/bookings")
interface BookingAPI {

    @GetMapping("/{bookingID}")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Booking with given Id", content = [
            (Content(mediaType = "application/json", schema = Schema(implementation = BookingInfoDTO::class)))]),
        ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()]),
        ApiResponse(responseCode = "404", description = "Booking not found.", content = [Content()])]
    )
    @SecurityRules.CanGetBooking
    fun getBooking(@PathVariable bookingID: Long) : BookingInfoDTO

    @GetMapping("/{bookingID}/review")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Found booking review", content = [
            (Content(mediaType = "application/json", schema = Schema(implementation = ReviewInfoDTO::class)))]),
        ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()]),
        ApiResponse(responseCode = "404", description = "Not reviewed yet.", content = [Content()])
    ])
    @SecurityRules.CanGetBookingReview
    fun getBookingReview(@PathVariable bookingID: Long) : ReviewInfoDTO
}