package teddy.economyplugin.component;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.springframework.stereotype.Component;
import teddy.economyplugin.entity.PlayerAccount;
import teddy.economyplugin.repository.PlayerAccountRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class PlayerListener implements Listener {

    private final PlayerAccountRepository playerAccountRepository;

    @EventHandler
    @Transactional
    public void onPlayerJoin(PlayerJoinEvent event) {
        String uuid = event.getPlayer().getUniqueId().toString();
        String playerName = event.getPlayer().getName();

        PlayerAccount account = playerAccountRepository.findById(uuid)
                .orElse(new PlayerAccount(uuid, playerName, BigDecimal.ZERO, LocalDateTime.now()));

        // 기존 계정이면 이름과 마지막 로그인 시간 업데이트
        account.setPlayerName(playerName);
        account.setLastLogin(LocalDateTime.now());

        playerAccountRepository.save(account);
    }
}
