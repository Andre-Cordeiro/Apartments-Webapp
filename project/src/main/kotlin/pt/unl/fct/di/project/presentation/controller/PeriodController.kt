package pt.unl.fct.di.project.presentation.controller

import org.springframework.web.bind.annotation.RestController
import pt.unl.fct.di.project.data.dao.Period
import pt.unl.fct.di.project.presentation.api.PeriodAPI

@RestController
class PeriodController() : PeriodAPI {

    override fun getPeriod(periodID: Long) {
        TODO("Not yet implemented")
    }
}