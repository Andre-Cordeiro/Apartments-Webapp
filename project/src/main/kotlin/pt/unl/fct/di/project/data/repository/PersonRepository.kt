package pt.unl.fct.di.project.data.repository

import org.springframework.data.repository.CrudRepository
import pt.unl.fct.di.project.data.dao.Person
import java.util.*


interface PersonRepository : CrudRepository<Person, Long> {

    override fun existsById(username : Long) : Boolean

    override fun findById(username : Long) : Optional<Person>
}