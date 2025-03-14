package accountService.mapper

import accountService.dto.BankAccountDTO
import accountService.model.BankAccount

fun BankAccount.toDTO(): BankAccountDTO {
    return BankAccountDTO(
        id = this.id,
        name = this.name,
        balance = this.balance,
        user = this.user.toDTO()
    )
}

fun BankAccountDTO.toEntity(): BankAccount {
    return BankAccount(
        id = this.id,
        name = this.name,
        balance = this.balance,
        user = this.user.toEntity()
    )
}