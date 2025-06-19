package accountService.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Account response DTO")
data class BankAccountDTO(
    @Schema(description = "Unique identifier of the bank account", example = "1")
    val id: Long,
    @Schema(description = "Name of the bank account", example = "Savings Account")
    val name: String,
    @Schema(description = "Current balance of the bank account", example = "1000.50")
    val balance: Double,
    @Schema(description = "User associated with the bank account")
    val user: UserDTO
)