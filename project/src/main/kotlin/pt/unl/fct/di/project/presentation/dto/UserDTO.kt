package pt.unl.fct.di.project.presentation.dto

data class UserLoginDTO(val username : Long, val password : String) {
    constructor() : this(7, "secret")
}