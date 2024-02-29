package pt.unl.fct.di.project.presentation.dto

import java.time.LocalDate

data class BookingInfoDTO(val id : Long, val clientName : String, val checkIn : LocalDate, val checkOut : LocalDate, val guests : Long, val state : String, val apartmentInfo : ApartmentInfoDTO)
