package pt.unl.fct.di.project.presentation.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import pt.unl.fct.di.project.service.ReviewService
import pt.unl.fct.di.project.presentation.api.ReviewAPI
import org.springframework.web.bind.annotation.RestController
import pt.unl.fct.di.project.presentation.dto.CreateReviewDTO
import pt.unl.fct.di.project.presentation.dto.ReviewInfoDTO
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.InvalidParameterException
import java.security.KeyStoreException

@RestController
class ReviewController(val reviewService: ReviewService) : ReviewAPI {
    override fun createReview(username: Long, bookingId: Long, review: CreateReviewDTO) : ReviewInfoDTO {
        val rev = reviewService.createReview(username, bookingId, review.description, review.rating)
        val client = rev.clientReviews
        return ReviewInfoDTO(client.name, review.description, review.rating)
    }

    override fun getReview(reviewId: Long): ReviewInfoDTO {
        val review = reviewService.getReview(reviewId)
        val client = review.clientReviews
        return ReviewInfoDTO(client.name, review.description, review.rating)
    }

    override fun getAllReviews(): Collection<ReviewInfoDTO> {
        val allReviews = reviewService.getAllReviews()
        return allReviews.map { ReviewInfoDTO(it.clientReviews.name, it.description, it.rating) }
    }

    @ResponseStatus(value= HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidParameterException::class)
    fun badRequest() {}

    @ResponseStatus(value= HttpStatus.CONFLICT)
    @ExceptionHandler(KeyStoreException::class)
    fun alreadyExists() {}

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(InvalidKeyException::class)
    fun notFound(){}

    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ExceptionHandler(InvalidAlgorithmParameterException::class)
    fun forbidden(){}

}