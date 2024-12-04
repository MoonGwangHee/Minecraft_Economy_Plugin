package teddy.economyplugin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import teddy.economyplugin.entity.Transaction;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByPlayerUuid(String playerUuid);

    @Query("SELECT t FROM Transaction t WHERE t.playerUuid = ?1 ORDER BY t.transactionTime DESC LIMIT 10")
    List<Transaction> findRecentTransactions(String playerUuid);
}
