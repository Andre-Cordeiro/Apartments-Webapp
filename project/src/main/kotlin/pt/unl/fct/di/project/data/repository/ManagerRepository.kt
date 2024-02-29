package pt.unl.fct.di.project.data.repository

import org.springframework.data.repository.CrudRepository
import pt.unl.fct.di.project.data.dao.Manager
import java.util.*

interface ManagerRepository : CrudRepository<Manager, Long> {

    override fun findById(username: Long) : Optional<Manager>
}