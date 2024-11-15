package com.example.simpleAccountService

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/accounts")
class AccountController {

    private val accounts = mutableMapOf<Int, Account>()

    @GetMapping
    fun getAccounts(): List<Account> = accounts.values.toList()

    @PostMapping
    fun addAccount(@RequestBody account: Account): Account {
        val id = accounts.size
        val newAccount = Account(id, account.name, account.balance)
        accounts[id] = newAccount
        return newAccount
    }

    data class BalanceUpdateRequest(val balance: Double)

    @PutMapping("/{id}/balance")
    fun updateBalance(@PathVariable id: Int, @RequestBody balance: BalanceUpdateRequest): Account {
        val account = accounts[id] ?: throw IllegalArgumentException("Account not found")
        accounts[id] = account.copy(balance = balance.balance)
        return accounts[id] ?: throw IllegalArgumentException("Account not found")
    }


}