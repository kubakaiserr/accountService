package accountService.dto

import java.util.UUID
import jakarta.validation.constraints.*

data class BankAccountCreationDTO(
    @field:NotBlank(message = "Account name must not be blank")
    val name: String,

    @field:PositiveOrZero(message = "Balance must be zero or positive")
    val balance: Double,

    @field:NotNull(message = "User ID must not be null")
    val userId: UUID
)