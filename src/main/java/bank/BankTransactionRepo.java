package bank;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BankTransactionRepo  extends JpaRepository<BankTransaction,Long> {
}
