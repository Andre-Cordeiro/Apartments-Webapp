package pt.unl.fct.di.project.data.dao

import jakarta.persistence.*
import org.hibernate.engine.internal.Cascade

@Entity
@Table(name = "APARTMENT")
data class Apartment(

    @Id @GeneratedValue
    val id: Long,
    val name: String,
    val description: String,
    val location: String,
    val amenities: String,
    val pictures: String,
    val price: Long,

    @ManyToOne
    val owner : Owner,

    @OneToMany(mappedBy = "apartment", cascade = [CascadeType.ALL])
    var periods: MutableList<Period>,

    @OneToMany(mappedBy = "bookedApartment", cascade = [CascadeType.ALL])
    var bookings: MutableList<Booking>

)