package pt.unl.fct.di.project.data.dao

import pt.unl.fct.di.project.data.Role
import jakarta.persistence.*


@Entity
@Table(name = "OWNER")
data class Owner(
    
    override val username: Long,
    val name: String,
    val email : String,
    val pictureUrl: String,
    val phoneNumber: Long,

    @OneToMany(mappedBy = "owner", cascade = [CascadeType.ALL])
    var apartments: MutableList<Apartment>,

    ) : Person(username, "secret", Role.OWNER) {
    override fun toString(): String = "Owner(username=${username}, name=${name}, email=${email}, pictureUrl=${pictureUrl}, phoneNumber=${phoneNumber}, apartments=${apartments})"
}