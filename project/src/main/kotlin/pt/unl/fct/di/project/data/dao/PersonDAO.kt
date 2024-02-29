package pt.unl.fct.di.project.data.dao


import jakarta.persistence.*
import pt.unl.fct.di.project.data.Role

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
abstract class Person(
        @Id @GeneratedValue
        open val username : Long,
        open val password : String,
        @Enumerated(EnumType.STRING) open val role : Role,
)