package accountService.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "DTO for updating the account balance")
data class AccountBalance(
    @Schema(description = "New account balance", example = "500.0")
    val balance: Double
)
