package accountService.repository

import accountService.model.BankAccount
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface BankAccountRepository : JpaRepository<BankAccount, Long>{
    fun findByUserId(userId: UUID): List<BankAccount>
}