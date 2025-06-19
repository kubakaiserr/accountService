// src/test/kotlin/accountService/service/BankAccountServiceTest.kt
package accountService.service

import accountService.dto.BankAccountCreationDTO
import accountService.dto.BankAccountDTO
import accountService.exception.AccountNotFoundException
import accountService.exception.UserNotFoundException
import accountService.model.BankAccount
import accountService.model.User
import accountService.repository.BankAccountRepository
import accountService.repository.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Sort
import java.util.*

@SpringBootTest
class BankAccountServiceTest {
    private val bankAccountRepository = mock(BankAccountRepository::class.java)
    private val findAll = bankAccountRepository.findAll()
    private val userRepository = mock(UserRepository::class.java)
    private val bankAccountService = BankAccountService(bankAccountRepository, userRepository)




    @Test
    fun `getAccountById should return BankAccountDTO when account exists`() {
        val user = User(id = UUID.randomUUID(), name = "Test User")
        val account = BankAccount(id = 1L, name = "Account 1", balance = 100.0, user = user)
        `when`(bankAccountRepository.findById(1L)).thenReturn(Optional.of(account))

        val result = bankAccountService.getAccountById(1L)

        assertNotNull(result)
        assertEquals("Account 1", result?.name)
        assertEquals(100.0, result?.balance)
    }

    @Test
    fun `createAccount should save and return BankAccountDTO when user exists`() {
        val user = User(id = UUID.fromString("00000000-0000-0000-0000-000000000001"), name = "Test User")
        val accountCreationDTO = BankAccountCreationDTO(userId = UUID.fromString("00000000-0000-0000-0000-000000000001"), name = "Test Account", balance = 100.0)
        val bankAccount = BankAccount(id = 1L, name = "Test Account", balance = 100.0, user = user)
        `when`(userRepository.findById(UUID.fromString("00000000-0000-0000-0000-000000000001"))).thenReturn(Optional.of(user))
        `when`(bankAccountRepository.save(any(BankAccount::class.java))).thenReturn(bankAccount)

        val result = bankAccountService.createAccount(accountCreationDTO)

        assertNotNull(result)
        assertEquals("Test Account", result.name)
        assertEquals(100.0, result.balance)
    }

    @Test
    fun `createAccount should throw UserNotFoundException when user does not exist`() {
        val accountCreationDTO = BankAccountCreationDTO(userId = UUID.fromString("00000000-0000-0000-0000-000000000001"), name = "Test Account", balance = 100.0)
        `when`(userRepository.findById(UUID.randomUUID())).thenThrow(UserNotFoundException("User with ID 00000000-0000-0000-0000-000000000001 not found"))

        val exception = assertThrows<UserNotFoundException> {
            bankAccountService.createAccount(accountCreationDTO)
        }

        assertEquals("User with ID 00000000-0000-0000-0000-000000000001 not found", exception.message)
    }

    @Test
    fun `updateAccountName should throw AccountNotFoundException when account does not exist`() {
        `when`(bankAccountRepository.findById(1L)).thenThrow(AccountNotFoundException("Account with ID 1 not found"))

        val exception = assertThrows<AccountNotFoundException> {
            bankAccountService.updateAccountName(1L, "New Name")
        }

        assertEquals("Account with ID 1 not found", exception.message)
    }

    @Test
    fun `updateAccountName should save and return BankAccountDTO when account exists`() {
        val account = BankAccount(id = 1L, name = "Test Account", balance = 100.0, user = User(id = UUID.randomUUID(), name = "Test User"))
        `when`(bankAccountRepository.findById(1L)).thenReturn(Optional.of(account))
        `when`(bankAccountRepository.save(any(BankAccount::class.java))).thenReturn(account)

        val result = bankAccountService.updateAccountName(1L, "New Name")

        assertNotNull(result)
        assertEquals("New Name", result?.name)
        assertEquals(100.0, result?.balance)
    }

    @Test
    fun `updateAccountBalance should throw AccountNotFoundException when account does not exist`() {
        `when`(bankAccountRepository.findById(1L)).thenThrow(AccountNotFoundException("Account with ID 1 not found"))

        val exception = assertThrows<AccountNotFoundException> {
            bankAccountService.updateAccountBalance(1L, 200.0)
        }

        assertEquals("Account with ID 1 not found", exception.message)
    }

    @Test
    fun `updateAccountBalance should save and return BankAccountDTO when account exists`() {
        val account = BankAccount(id = 1L, name = "Test Account", balance = 100.0, user = User(id = UUID.randomUUID(), name = "Test User"))
        `when`(bankAccountRepository.findById(1L)).thenReturn(Optional.of(account))
        `when`(bankAccountRepository.save(any(BankAccount::class.java))).thenReturn(account)

        val result = bankAccountService.updateAccountBalance(1L, 200.0)

        assertNotNull(result)
        assertEquals("Test Account", result?.name)
        assertEquals(200.0, result?.balance)
    }

    @Test
    fun `deleteAccount should throw AccountNotFoundException when account does not exist`() {
        `when`(bankAccountRepository.deleteById(1L)).thenThrow(AccountNotFoundException("Account with ID 1 not found"))

        val exception = assertThrows<AccountNotFoundException> {
            bankAccountService.deleteAccount(1L)
        }

        assertEquals("Account with ID 1 not found", exception.message)
    }

    @Test
    fun `deleteAccount should delete account when account exists`() {
        val account = BankAccount(id = 1L, name = "Test Account", balance = 100.0, user = User(id = UUID.randomUUID(), name = "Test User"))
        `when`(bankAccountRepository.findById(1L)).thenReturn(Optional.of(account))

        bankAccountService.deleteAccount(1L)

        verify(bankAccountRepository, times(1)).deleteById(1L)
    }

    @Test
    fun `getAccountsByUserId should throw UserNotFoundException when user does not exist`() {
        `when`(userRepository.findById(UUID.fromString("00000000-0000-0000-0000-000000000001"))).thenThrow(UserNotFoundException("User with ID 00000000-0000-0000-0000-000000000001 not found"))

        val exception = assertThrows<UserNotFoundException> {
            bankAccountService.getAccountsByUserId(UUID.fromString("00000000-0000-0000-0000-000000000001"))
        }

        assertEquals("User with ID 00000000-0000-0000-0000-000000000001 not found", exception.message)
    }

    @Test
    fun `getAccountsByUserId should return list of BankAccountDTO when user exists`() {
        val user = User(id = UUID.fromString("00000000-0000-0000-0000-000000000001"), name = "Test User")
        val accounts = listOf(
            BankAccount(id = 1L, name = "Account 1", balance = 100.0, user = user),
            BankAccount(id = 2L, name = "Account 2", balance = 200.0, user = user)
        )
        `when`(userRepository.findById(UUID.fromString("00000000-0000-0000-0000-000000000001"))).thenReturn(Optional.of(user))
        `when`(bankAccountRepository.findByUserId(UUID.fromString("00000000-0000-0000-0000-000000000001"))).thenReturn(accounts)

        val result = bankAccountService.getAccountsByUserId(UUID.fromString("00000000-0000-0000-0000-000000000001"))

        assertNotNull(result)
        assertEquals(2, result.size)
        assertEquals("Account 1", result[0].name)
        assertEquals(100.0, result[0].balance)
        assertEquals("Account 2", result[1].name)
        assertEquals(200.0, result[1].balance)
    }

    @Test
    fun `getAccountById should throw AccountNotFoundException when account does not exist`() {
        `when`(bankAccountRepository.findById(1L)).thenThrow(AccountNotFoundException("Account with ID 1 not found"))

        val exception = assertThrows<AccountNotFoundException> {
            bankAccountService.getAccountById(1L)
        }

        assertEquals("Account with ID 1 not found", exception.message)
    }

    @Test
    fun `getAllAccounts should return list of BankAccountDTO`() {
        val accounts = listOf(
            BankAccount(id = 1L, name = "Account 1", balance = 100.0, user = User(id = UUID.randomUUID(), name = "Test User")),
            BankAccount(id = 2L, name = "Account 2", balance = 200.0, user = User(id = UUID.randomUUID(), name = "Test User"))
        )
        val sort = Sort.by(Sort.Direction.ASC, "id")
        `when`(bankAccountRepository.findAll(sort)).thenReturn(accounts)

        val repositoryResult = bankAccountRepository.findAll()
        println(repositoryResult)

        val result = bankAccountService.getAllAccounts(null, null)

        println(bankAccountRepository.findAll()) // Should print the mocked list
        println(result) // To check what is returned


        assertNotNull(result)
        assertEquals(2, result.size)
        assertEquals("Account 1", result[0].name)
        assertEquals(100.0, result[0].balance)
        assertEquals("Account 2", result[1].name)
        assertEquals(200.0, result[1].balance)
    }

    @Test
    fun `getAllAccounts should return list of BankAccountDTO sorted by name in ascending order`() {
        val accounts = listOf(
            BankAccount(id = 1L, name = "Account 2", balance = 100.0, user = User(id = UUID.randomUUID(), name = "Test User")),
            BankAccount(id = 2L, name = "Account 1", balance = 200.0, user = User(id = UUID.randomUUID(), name = "Test User"))
        )
        val sortedAccounts = accounts.sortedBy { it.name }
        val sort = Sort.by(Sort.Direction.ASC, "name")
        `when`(bankAccountRepository.findAll(sort)).thenReturn(sortedAccounts)

        val result = bankAccountService.getAllAccounts("name", "asc")

        assertNotNull(result)
        assertEquals(2, result.size)
        assertEquals("Account 1", result[0].name)
        assertEquals(200.0, result[0].balance)
        assertEquals("Account 2", result[1].name)
        assertEquals(100.0, result[1].balance)
    }

    @Test
    fun `getAllAccounts should return list of BankAccountDTO sorted by name in descending order`() {
        val accounts = listOf(
            BankAccount(id = 1L, name = "Account 1", balance = 100.0, user = User(id = UUID.randomUUID(), name = "Test User")),
            BankAccount(id = 2L, name = "Account 2", balance = 200.0, user = User(id = UUID.randomUUID(), name = "Test User"))
        )
        val sortedAccounts = accounts.sortedByDescending { it.name }
        val sort = Sort.by(Sort.Direction.DESC, "name")
        `when`(bankAccountRepository.findAll(sort)).thenReturn(sortedAccounts)

        val result = bankAccountService.getAllAccounts("name", "desc")

        assertNotNull(result)
        assertEquals(2, result.size)
        assertEquals("Account 2", result[0].name)
        assertEquals(200.0, result[0].balance)
        assertEquals("Account 1", result[1].name)
        assertEquals(100.0, result[1].balance)
    }



    @Test
    fun `getAllAccounts should return list of BankAccountDTO sorted by balance in ascending order`() {
        val accounts = listOf(
            BankAccount(id = 1L, name = "Account 1", balance = 200.0, user = User(id = UUID.randomUUID(), name = "Test User")),
            BankAccount(id = 2L, name = "Account 2", balance = 100.0, user = User(id = UUID.randomUUID(), name = "Test User"))
        )
        val sortedAccounts = accounts.sortedBy { it.balance }
        val sort = Sort.by(Sort.Direction.ASC, "balance")
        `when`(bankAccountRepository.findAll(sort)).thenReturn(sortedAccounts)

        val result = bankAccountService.getAllAccounts("balance", "asc")

        assertNotNull(result)
        assertEquals(2, result.size)
        assertEquals("Account 2", result[0].name)
        assertEquals(100.0, result[0].balance)
        assertEquals("Account 1", result[1].name)
        assertEquals(200.0, result[1].balance)
    }

    @Test
    fun `getAllAccounts should return list of BankAccountDTO sorted by balance in descending order`() {
        val accounts = listOf(
            BankAccount(id = 1L, name = "Account 1", balance = 100.0, user = User(id = UUID.randomUUID(), name = "Test User")),
            BankAccount(id = 2L, name = "Account 2", balance = 200.0, user = User(id = UUID.randomUUID(), name = "Test User"))
        )
        val sortedAccounts = accounts.sortedByDescending { it.balance }
        val sort = Sort.by(Sort.Direction.DESC, "balance")
        `when`(bankAccountRepository.findAll(sort)).thenReturn(sortedAccounts)

        val result = bankAccountService.getAllAccounts("balance", "desc")

        assertNotNull(result)
        assertEquals(2, result.size)
        assertEquals("Account 2", result[0].name)
        assertEquals(200.0, result[0].balance)
        assertEquals("Account 1", result[1].name)
        assertEquals(100.0, result[1].balance)
    }


}