package pt.unl.fct.di.project.presentation.dto

import java.time.LocalDate

data class ApartmentInfoDTO(val id: Long, val name: String, val description: String, val location: String, val amenities: String, val pictures: String, val price: Long)

data class BookApartmentDTO(val checkIn : LocalDate, val checkOut : LocalDate, val guests : Long)
