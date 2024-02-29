package pt.unl.fct.di.project.data.dao

import jakarta.persistence.*
import pt.unl.fct.di.project.data.dao.Client


@Entity
@Table(name = "REVIEW")
data class Review(

    @Id @GeneratedValue
    val id : Long,
    val description : String,
    val rating : Long,

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "client_id")
    val clientReviews : Client,

    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "booking_id")
    val bookingReview : Booking
)
