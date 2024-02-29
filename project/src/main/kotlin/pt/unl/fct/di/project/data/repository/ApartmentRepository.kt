package pt.unl.fct.di.project.data.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import pt.unl.fct.di.project.data.dao.Apartment
import pt.unl.fct.di.project.data.dao.Booking
import pt.unl.fct.di.project.data.dao.Period
import java.time.LocalDate
import java.util.*

interface ApartmentRepository : CrudRepository<Apartment, Long>  {

    override fun findById(id: Long) : Optional<Apartment>

    override fun findAll(): MutableIterable<Apartment>

    @Query("select p from Apartment a join a.periods p where a.id = :apartmentId")
    fun getApartPeriods(apartmentId: Long) : Collection<Period>

    @Query("select p from Apartment a join a.periods p where a.id = :apartmentId and p.available = true")
    fun getApartAvailablePeriods(apartmentId: Long) : Collection<Period>
    @Query("select a from Apartment a join a.periods p where p.available = true and (p.date between :startDate and :endDate)")
    fun getAvailableApartmentsForPeriod(startDate: LocalDate, endDate: LocalDate) : MutableList<Apartment>

    @Query("select a from Apartment a left join fetch a.periods where a.id = :apartmentId")
    fun findByIdWithPeriods(apartmentId: Long) : Optional<Apartment>
}