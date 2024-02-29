package pt.unl.fct.di.project.presentation.api

import org.springframework.web.bind.annotation.*
import pt.unl.fct.di.project.data.dao.Period

@RequestMapping("/periods")
interface PeriodAPI {

    @GetMapping("/{periodID}")
    fun getPeriod(@PathVariable periodID: Long)
}