package accountService.dto

data class BankAccountDTO(
    val id: Long,
    val name: String,
    val balance: Double,
    val user: UserDTO
)