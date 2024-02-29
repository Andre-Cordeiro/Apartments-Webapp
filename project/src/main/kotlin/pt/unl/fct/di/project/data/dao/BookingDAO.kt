package pt.unl.fct.di.project.data.dao

import jakarta.persistence.*
import pt.unl.fct.di.project.data.State
import java.time.LocalDate


@Entity
@Table(name= "BOOKING")
data class Booking(

    @Id @GeneratedValue
    val id : Long,
    val checkIn : LocalDate,
    val checkOut : LocalDate,
    val guests : Long,

    @Enumerated(EnumType.STRING)
    var state : State,

    @ManyToOne @JoinColumn(name = "client_id")
    val client : Client,

    @ManyToOne @JoinColumn(name = "apartment_booking")
    val bookedApartment : Apartment,

    @OneToOne(mappedBy = "bookingReview", cascade = [CascadeType.ALL])
    var bookingReview : Review?
)