package pt.unl.fct.di.project.data.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import pt.unl.fct.di.project.data.dao.Apartment
import pt.unl.fct.di.project.data.dao.Review
import java.util.*

interface ReviewRepository : CrudRepository<Review, Long> {


    override fun findAll() : MutableIterable<Review>
}