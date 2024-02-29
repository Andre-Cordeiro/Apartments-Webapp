package pt.unl.fct.di.project.data.repository


import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import pt.unl.fct.di.project.data.dao.Booking
import pt.unl.fct.di.project.data.dao.Period
import pt.unl.fct.di.project.data.dao.Review

interface PeriodRepository : CrudRepository<Period, Long> {

    override fun existsById(periodID: Long) : Boolean


}