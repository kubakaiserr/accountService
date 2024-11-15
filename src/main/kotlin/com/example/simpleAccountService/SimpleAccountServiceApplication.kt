package com.example.simpleAccountService

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SimpleAccountServiceApplication

fun main(args: Array<String>) {
	runApplication<SimpleAccountServiceApplication>(*args)
}
