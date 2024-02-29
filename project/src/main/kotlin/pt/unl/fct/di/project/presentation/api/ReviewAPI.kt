package pt.unl.fct.di.project.presentation.api

import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.*
import pt.unl.fct.di.project.config.SecurityRules
import pt.unl.fct.di.project.presentation.dto.CreateReviewDTO
import pt.unl.fct.di.project.presentation.dto.ReviewInfoDTO


@RequestMapping("/reviews")
interface ReviewAPI {

    @PostMapping("/{username}/review/{bookingId}")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Review was successfully created.", content = [
            (Content(mediaType = "application/json", schema = Schema(implementation = ReviewInfoDTO::class)))]),
        ApiResponse(responseCode = "400", description = "Invalid Review data.", content = [Content()]),
        ApiResponse(responseCode = "403", description = "Wrong client.", content = [Content()]),
        ApiResponse(responseCode = "404", description = "Booking not found.", content = [Content()]),
        ApiResponse(responseCode = "409", description = "Booking already reviewed.", content = [Content()]),
    ])
    @SecurityRules.CanCreateReview
    fun createReview(@PathVariable username: Long, @PathVariable bookingId: Long , @RequestBody review: CreateReviewDTO) : ReviewInfoDTO

    @GetMapping("/{reviewId}")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Review with given Id", content = [
            (Content(mediaType = "application/json", schema = Schema(implementation = ReviewInfoDTO::class)))]),
        ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()]),
        ApiResponse(responseCode = "404", description = "Review not found.", content = [Content()])]
    )
    @SecurityRules.CanGetReview
    fun getReview(@PathVariable reviewId: Long) : ReviewInfoDTO

    @GetMapping("/")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Found all reviews", content = [
            (Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ReviewInfoDTO::class)))))]),
        ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()]),
    ])
    @SecurityRules.CanGetAllReviews
    fun getAllReviews() : Collection<ReviewInfoDTO>
}