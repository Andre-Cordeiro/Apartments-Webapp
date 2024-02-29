package pt.unl.fct.di.project.data.dao

import pt.unl.fct.di.project.data.Role
import jakarta.persistence.*

@Entity
@Table(name="MANAGER")
data class Manager(


    override val username: Long,
    val email : String,
    val pictureUrl: String,
    val phoneNumber: Long,

    ) : Person(username,"secret", Role.MANAGER)