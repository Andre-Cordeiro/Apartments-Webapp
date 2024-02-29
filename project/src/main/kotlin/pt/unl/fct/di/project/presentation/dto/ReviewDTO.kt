package pt.unl.fct.di.project.presentation.dto

data class ReviewInfoDTO(val clientName : String, val description : String?, val rating : Long?)

data class CreateReviewDTO(val description : String, val rating : Long)