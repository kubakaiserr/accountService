package accountService.service

import accountService.dto.BankAccountCreationDTO
import accountService.dto.BankAccountDTO
import accountService.exception.AccountNotFoundException
import accountService.exception.UserNotFoundException
import accountService.mapper.toDTO
import accountService.mapper.toEntity
import accountService.model.BankAccount
import accountService.repository.BankAccountRepository
import accountService.repository.UserRepository
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class BankAccountService(
    private val bankAccountRepository: BankAccountRepository,
    private val userRepository: UserRepository
    ) {

    fun getAllAccounts(sortBy: String?, sortOrder: String?): List<BankAccountDTO> {
        val sort = when (sortOrder?.toLowerCase()) {
            "desc" -> Sort.by(Sort.Direction.DESC, sortBy ?: "id")
            else -> Sort.by(Sort.Direction.ASC, sortBy ?: "id")
        }
        return bankAccountRepository.findAll(sort).map { it.toDTO() }
    }

    fun getAccountById(id: Long): BankAccountDTO? {
        val account = bankAccountRepository.findById(id).orElseThrow {
            AccountNotFoundException("Account with ID $id not found")
        }
        return account.toDTO()
    }

    fun createAccount(accountCreationDTO: BankAccountCreationDTO): BankAccountDTO {
        val user = userRepository.findById(accountCreationDTO.userId).orElseThrow {
            UserNotFoundException("User with ID ${accountCreationDTO.userId} not found")
        }
        val newAccount = BankAccount(
            name = accountCreationDTO.name,
            balance = accountCreationDTO.balance,
            user = user
        )
        return bankAccountRepository.save(newAccount).toDTO()
    }

    fun updateAccountName(id: Long, newName: String): BankAccountDTO? {
        val account = bankAccountRepository.findById(id).orElseThrow {
            AccountNotFoundException("Account with ID $id not found")
        }
        account.name = newName
        return bankAccountRepository.save(account).toDTO()
    }

    fun deleteAccount(id: Long) {
        try {
            bankAccountRepository.deleteById(id)
        } catch (e: Exception) {
            throw AccountNotFoundException("Account with ID $id not found")
        }
    }

    fun updateAccountBalance(id: Long, newBalance: Double): BankAccountDTO? {
        val account = bankAccountRepository.findById(id).orElseThrow {
            AccountNotFoundException("Account with ID $id not found")
        }
        account.balance = newBalance
        return bankAccountRepository.save(account).toDTO()
    }

    fun getAccountsByUserId(userId: Long): List<BankAccountDTO> {
        val user = userRepository.findById(userId).orElseThrow {
            UserNotFoundException("User with ID $userId not found")
        }
        return bankAccountRepository.findByUserId(user.id).map { it.toDTO() }
    }
}