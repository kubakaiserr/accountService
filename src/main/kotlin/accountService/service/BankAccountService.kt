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
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.util.UUID
import org.slf4j.Logger
import org.springframework.transaction.annotation.Transactional

@Service
class BankAccountService(
    private val bankAccountRepository: BankAccountRepository,
    private val userRepository: UserRepository
) {

    private val logger: Logger = LoggerFactory.getLogger(BankAccountService::class.java)

    @Transactional(readOnly = true)
    fun getAllAccounts(sortBy: String?, sortOrder: String?): List<BankAccountDTO> {
        logger.info("Fetching all accounts")
        val sort = when (sortOrder?.toLowerCase()) {
            "desc" -> Sort.by(Sort.Direction.DESC, sortBy ?: "id")
            else -> Sort.by(Sort.Direction.ASC, sortBy ?: "id")
        }
        return bankAccountRepository.findAll(sort).map { it.toDTO() }
    }

    @Transactional(readOnly = true)
    fun getAccountById(id: Long): BankAccountDTO? {
        logger.info("Fetching account with ID: $id")
        val account = bankAccountRepository.findById(id).orElseThrow {
            AccountNotFoundException("Account with ID $id not found")
        }
        return account.toDTO()
    }

    @Transactional
    fun createAccount(accountCreationDTO: BankAccountCreationDTO): BankAccountDTO {
        if (accountCreationDTO.balance < 100) {
            throw IllegalArgumentException("Initial balance must be at least 100")
        }
        val user = userRepository.findById(accountCreationDTO.userId).orElseThrow {
            UserNotFoundException("User with ID ${accountCreationDTO.userId} not found")
        }
        val newAccount = BankAccount(
            name = accountCreationDTO.name,
            balance = accountCreationDTO.balance,
            user = user
        )
        logger.info("Create new account with ID: ${newAccount.id}")
        return bankAccountRepository.save(newAccount).toDTO()
    }

    @Transactional
    fun updateAccountName(id: Long, newName: String): BankAccountDTO? {
        val account = bankAccountRepository.findById(id).orElseThrow {
            AccountNotFoundException("Account with ID $id not found")
        }
        account.name = newName
        logger.info("Updating account with ID: $id to new name: $newName")
        return bankAccountRepository.save(account).toDTO()
    }

    @Transactional
    fun deleteAccount(id: Long) {
        logger.info("Deleting account with ID: $id")
        try {
            bankAccountRepository.deleteById(id)
        } catch (e: Exception) {
            throw AccountNotFoundException("Account with ID $id not found")
        }
    }

    @Transactional
    fun updateAccountBalance(id: Long, newBalance: Double): BankAccountDTO? {
        val account = bankAccountRepository.findById(id).orElseThrow {
            AccountNotFoundException("Account with ID $id not found")
        }
        account.balance = newBalance
        logger.info("Updating balance for account with ID: $id to new balance: $newBalance")
        return bankAccountRepository.save(account).toDTO()
    }

    @Transactional(readOnly = true)
    fun getAccountsByUserId(userId: UUID): List<BankAccountDTO> {
        logger.info("Fetching accounts for user with ID: $userId")
        val user = userRepository.findById(userId).orElseThrow {
            UserNotFoundException("User with ID $userId not found")
        }
        return bankAccountRepository.findByUserId(user.id ?: throw IllegalStateException("User ID cannot be null")).map { it.toDTO() }
    }
}