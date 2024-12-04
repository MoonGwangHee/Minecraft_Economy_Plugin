package teddy.economyplugin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import teddy.economyplugin.entity.PlayerAccount;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerAccountRepository extends JpaRepository<PlayerAccount, String> {
    Optional<PlayerAccount> findByPlayerName(String playerName);

    @Query("SELECT a FROM PlayerAccount a WHERE a.balance >= :minBalance ORDER BY a.balance DESC")
    List<PlayerAccount> findTopBalances(@Param("minBalance") BigDecimal minBalance);

    @Query("SELECT COUNT(a) > 0 FROM PlayerAccount a WHERE a.playerName = :playerName")
    boolean existsByPlayerName(@Param("playerName") String playerName);

    @Query("SELECT a FROM PlayerAccount a ORDER BY a.balance DESC LIMIT 10")
    List<PlayerAccount> findTop10ByOrderByBalanceDesc();

    @Query("SELECT SUM(a.balance) FROM PlayerAccount a")
    BigDecimal getTotalEconomy();
}
