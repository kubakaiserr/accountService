package accountService.controller

import accountService.dto.BankAccountCreationDTO
import accountService.dto.BankAccountDTO
import accountService.dto.UserDTO
import accountService.exception.UserNotFoundException
import accountService.service.BankAccountService
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.*

@WebMvcTest(BankAccountController::class)
class BankAccountControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var bankAccountService: BankAccountService

    @Test
    fun `getAllAccounts should return list of accounts`() {
        val testUserDTO = UserDTO(id = UUID.fromString("00000000-0000-0000-0000-000000000001"), name = "Test User")
        val accounts = listOf(
            BankAccountDTO(id = 1L, name = "Account 1", balance = 100.0, user = testUserDTO),
            BankAccountDTO(id = 2L, name = "Account 2", balance = 200.0, user = testUserDTO)
        )
        `when`(bankAccountService.getAllAccounts(null, null)).thenReturn(accounts)

        mockMvc.perform(get("/accounts"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].name").value("Account 1"))
            .andExpect(jsonPath("$[0].user.id").value("00000000-0000-0000-0000-000000000001"))
            .andExpect(jsonPath("$[0].user.name").value("Test User"))
            .andExpect(jsonPath("$[1].name").value("Account 2"))
            .andExpect(jsonPath("$[1].user.id").value("00000000-0000-0000-0000-000000000001"))
            .andExpect(jsonPath("$[1].user.name").value("Test User"))
    }

    @Test
    fun `getAccountById should return account with given ID`() {
        val testUserDTO = UserDTO(id = UUID.fromString("00000000-0000-0000-0000-000000000001"), name = "Test User")
        val account = BankAccountDTO(id = 1L, name = "Account 1", balance = 100.0, user = testUserDTO)
        `when`(bankAccountService.getAccountById(1L)).thenReturn(account)

        mockMvc.perform(get("/accounts/1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Account 1"))
            .andExpect(jsonPath("$.user.id").value("00000000-0000-0000-0000-000000000001"))
            .andExpect(jsonPath("$.user.name").value("Test User"))
    }

    @Test
    fun `getAccountById should return 404 when account with given ID does not exist`() {
        `when`(bankAccountService.getAccountById(1L)).thenThrow(UserNotFoundException("User with ID 1 not found"))

        mockMvc.perform(get("/accounts/1"))
            .andExpect(status().isNotFound)
            //and expects response to be "User with ID 1 not found", but not in json body
            .andExpect(content().string("User with ID 1 not found"))
    }

    @Test
    fun `getAccountsByUserId should return list of accounts for given user ID`() {
        val testUserDTO = UserDTO(id = UUID.fromString("00000000-0000-0000-0000-000000000001"), name = "Test User")
        val accounts = listOf(
            BankAccountDTO(id = 1L, name = "Account 1", balance = 100.0, user = testUserDTO),
            BankAccountDTO(id = 2L, name = "Account 2", balance = 200.0, user = testUserDTO)
        )
        `when`(bankAccountService.getAccountsByUserId(UUID.fromString("00000000-0000-0000-0000-000000000001"))).thenReturn(accounts)

        mockMvc.perform(get("/accounts/user/00000000-0000-0000-0000-000000000001"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].name").value("Account 1"))
            .andExpect(jsonPath("$[0].user.id").value("00000000-0000-0000-0000-000000000001"))
            .andExpect(jsonPath("$[0].user.name").value("Test User"))
            .andExpect(jsonPath("$[1].name").value("Account 2"))
            .andExpect(jsonPath("$[1].user.id").value("00000000-0000-0000-0000-000000000001"))
            .andExpect(jsonPath("$[1].user.name").value("Test User"))
    }

    @Test
    fun `getAccountsByUserId should return 404 when user with given ID does not exist`() {
        `when`(bankAccountService.getAccountsByUserId(UUID.fromString("00000000-0000-0000-0000-000000000001"))).thenThrow(UserNotFoundException("User with ID 00000000-0000-0000-0000-000000000001 not found"))

        mockMvc.perform(get("/accounts/user/00000000-0000-0000-0000-000000000001"))
            .andExpect(status().isNotFound)
            //and expects response to be "User with ID 1 not found", but not in json body
            .andExpect(content().string("User with ID 00000000-0000-0000-0000-000000000001 not found"))
    }

    @Test
    fun `createAccount should return created account`() {
        val testUserDTO = UserDTO(id = UUID.fromString("00000000-0000-0000-0000-000000000001"), name = "Test User")
        val newAccount = BankAccountCreationDTO(userId = UUID.fromString("00000000-0000-0000-0000-000000000001"), name = "Account 1", balance = 100.0)
        val newAccountJson = """
            {
                "userId": "00000000-0000-0000-0000-000000000001",
                "name": "Account 1",
                "balance": 100.0
            }
        """.trimIndent()
        val account = BankAccountDTO(id = 1L, name = "Account 1", balance = 100.0, user = testUserDTO)
        `when`(bankAccountService.createAccount(newAccount)).thenReturn(account)

        mockMvc.perform(
            post("/accounts")
                .contentType("application/json")
                .content(newAccountJson)
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.name").value("Account 1"))
            .andExpect(jsonPath("$.user.id").value("00000000-0000-0000-0000-000000000001"))
            .andExpect(jsonPath("$.user.name").value("Test User"))
    }

    @Test
    fun `updateAccountName should return updated account`() {
        val testUserDTO = UserDTO(id = UUID.fromString("00000000-0000-0000-0000-000000000001"), name = "Test User")
        val updatedAccount = BankAccountDTO(id = 1L, name = "Updated Account", balance = 100.0, user = testUserDTO)
        val updatedAccountJson = """
            {
                "name": "Updated Account"
            }
        """.trimIndent()
        `when`(bankAccountService.updateAccountName(1L, "Updated Account")).thenReturn(updatedAccount)

        mockMvc.perform(
            put("/accounts/1/name")
                .contentType("application/json")
                .content(updatedAccountJson)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Updated Account"))
            .andExpect(jsonPath("$.user.id").value("00000000-0000-0000-0000-000000000001"))
            .andExpect(jsonPath("$.user.name").value("Test User"))
    }

    @Test
    fun `updateAccountBalance should return updated account`() {
        val testUserDTO = UserDTO(id = UUID.fromString("00000000-0000-0000-0000-000000000001"), name = "Test User")
        val updatedAccount = BankAccountDTO(id = 1L, name = "Account 1", balance = 200.0, user = testUserDTO)
        val updatedAccountJson = """
            {
                "balance": 200.0
            }
        """.trimIndent()
        `when`(bankAccountService.updateAccountBalance(1L, 200.0)).thenReturn(updatedAccount)

        mockMvc.perform(
            put("/accounts/1/balance")
                .contentType("application/json")
                .content(updatedAccountJson)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Account 1"))
            .andExpect(jsonPath("$.balance").value(200.0))
            .andExpect(jsonPath("$.user.id").value("00000000-0000-0000-0000-000000000001"))
            .andExpect(jsonPath("$.user.name").value("Test User"))
    }

    @Test
    fun `deleteAccount should return 204`() {
        mockMvc.perform(delete("/accounts/1"))
            .andExpect(status().isNoContent)
    }

    @Test
    fun `deleteAccount should return 404 when account with given ID does not exist`() {
        `when`(bankAccountService.deleteAccount(1L)).thenThrow(UserNotFoundException("User with ID 1 not found"))

        mockMvc.perform(delete("/accounts/1"))
            .andExpect(status().isNotFound)
            //and expects response to be "User with ID 1 not found", but not in json body
            .andExpect(content().string("User with ID 1 not found"))
    }


}