package pt.unl.fct.di.project.data.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import pt.unl.fct.di.project.data.dao.Booking
import pt.unl.fct.di.project.data.dao.Client
import pt.unl.fct.di.project.data.dao.Review
import java.util.*

interface ClientRepository : CrudRepository<Client, Long> {

    override fun findById(username: Long) : Optional<Client>

    @Query("select c from Client c inner join fetch c.reviews where c.username = :username")
    fun findClientWithReviews(username: Long) : Optional<Client>
}
