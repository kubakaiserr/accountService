package accountService.mapper

import accountService.dto.BankAccountDTO
import accountService.dto.UserDTO
import accountService.model.BankAccount
import accountService.model.User
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.mockito.Mockito

@SpringBootTest
class AccountMapperTest {

    val testUser = User(id = 1L, name = "Test User")
    val testuserDTO = UserDTO(id = 1L, name = "Test User")


    @Test
    fun `toDTO should map BankAccount to BankAccountDTO`() {
        val account = BankAccount(id = 1L, name = "Test Account", balance = 100.0, testUser)
        val accountDTO = account.toDTO()

        assert(accountDTO.id == 1L)
        assert(accountDTO.name == "Test Account")
        assert(accountDTO.balance == 100.0)
    }

    @Test
    fun `toEntity should map BankAccountDTO to BankAccount`() {
        val accountDTO = BankAccountDTO(id = 1L, name = "Test Account", balance = 100.0, testuserDTO)
        val account = accountDTO.toEntity()

        assert(account.id == 1L)
        assert(account.name == "Test Account")
        assert(account.balance == 100.0)
    }



}