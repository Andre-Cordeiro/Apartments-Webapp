package pt.unl.fct.di.project.data.dao

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name="PERIOD")
data class Period(

    @Id @GeneratedValue
    val periodID : Long,
    val date : LocalDate,
    var available : Boolean,

    @ManyToOne @JoinColumn(name = "apartment_id")
    val apartment : Apartment,

    )