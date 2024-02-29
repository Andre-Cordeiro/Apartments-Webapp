package pt.unl.fct.di.project.data.dao

import jakarta.persistence.CascadeType
import pt.unl.fct.di.project.data.Role
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "CLIENT")
data class Client(

        override val username : Long,
        val name : String,
        val email : String,
        val pictureUrl: String,
        val phoneNumber: Long,

        @OneToMany(mappedBy = "client", cascade = [CascadeType.ALL])
        val bookings : MutableList<Booking>,

        @OneToMany(mappedBy = "clientReviews", cascade = [CascadeType.ALL])
        val reviews : MutableList<Review>,

        ) : Person(username,"secret", Role.CLIENT) {
        override fun toString(): String = "Client(username=${username}, name=${name}, email=${email}, pictureUrl=${pictureUrl}, phoneNumber=${phoneNumber}, bookings=${bookings}, reviews=${reviews})"
}