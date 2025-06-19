package accountService.controller

import accountService.dto.BankAccountCreationDTO
import accountService.dto.BankAccountDTO
import accountService.model.BankAccount
import accountService.service.BankAccountService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/accounts")
class BankAccountController(private val service: BankAccountService) {

    @GetMapping
    fun getAllAccounts(
        @RequestParam sortBy: String?,
        @RequestParam sortOrder: String?
    ): List<BankAccountDTO> = service.getAllAccounts(sortBy, sortOrder)

    @GetMapping("/{id}")
    fun getAccountById(@PathVariable id: Long): ResponseEntity<BankAccountDTO> {
        val account = service.getAccountById(id) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        return ResponseEntity.ok(account)
    }

    @PostMapping
    fun createAccount(@RequestBody @Valid accountCreationDTO: BankAccountCreationDTO): ResponseEntity<BankAccountDTO> {
        val createdAccount = service.createAccount(accountCreationDTO)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount)
    }

    @PutMapping("/{id}/name")
    fun updateAccountName(@PathVariable id: Long, @RequestBody @Valid newName: AccountName): ResponseEntity<BankAccountDTO> {
        val updatedAccount = service.updateAccountName(id, newName.name) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        return ResponseEntity.ok(updatedAccount)
    }

    @PutMapping("/{id}/balance")
    fun updateAccountBalance(@PathVariable id: Long, @RequestBody @Valid newBalance: AccountBalance): ResponseEntity<BankAccountDTO> {
        val updatedAccount = service.updateAccountBalance(id, newBalance.balance) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        return ResponseEntity.ok(updatedAccount)
    }

    @DeleteMapping("/{id}")
    fun deleteAccount(@PathVariable id: Long): ResponseEntity<Void> {
        service.deleteAccount(id)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @GetMapping("/user/{userId}")
    fun getAccountsByUserId(@PathVariable userId: UUID): ResponseEntity<List<BankAccountDTO>> {
        val accounts = service.getAccountsByUserId(userId)
        return ResponseEntity.ok(accounts)
    }


    data class AccountName(
        @field:NotBlank(message = "Name must not be blank")
        @field:Size(max = 50, message = "Name must not exceed 50 characters")
        val name: String)
    data class AccountBalance(
        @field:NotBlank(message = "Balance must not be blank")
        val balance: Double)
}