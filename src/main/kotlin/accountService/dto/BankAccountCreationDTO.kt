package accountService.dto

data class BankAccountCreationDTO(
    val name: String,
    val balance: Double,
    val userId: Long
)