package pt.unl.fct.di.project.data.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import pt.unl.fct.di.project.data.dao.Apartment
import pt.unl.fct.di.project.data.dao.Owner
import java.util.*

interface OwnerRepository : CrudRepository<Owner, Long> {

    override fun findById(ownerId: Long) : Optional<Owner>

    @Query("select o from Owner o inner join fetch o.apartments where o.username = :ownerId")
    fun findOwnerWithAparts(ownerId: Long) : Optional<Owner>
}