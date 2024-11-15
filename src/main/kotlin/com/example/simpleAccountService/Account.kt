package com.example.simpleAccountService

import com.fasterxml.jackson.annotation.JsonIgnore

data class Account(


    val id: Int?= null,
    val name: String,
    val balance: Double

)
