package teddy.economyplugin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.springframework.transaction.annotation.Transactional;
import teddy.economyplugin.entity.PlayerAccount;
import teddy.economyplugin.entity.ShopItem;
import teddy.economyplugin.entity.Transaction;
import teddy.economyplugin.repository.PlayerAccountRepository;
import teddy.economyplugin.repository.ShopItemRepository;
import teddy.economyplugin.repository.ShopNPCRepository;
import teddy.economyplugin.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ShopService {

    private final ShopItemRepository shopItemRepository;
    private final ShopNPCRepository shopNPCRepository;
    private final TransactionRepository transactionRepository;
    private final PlayerAccountRepository playerAccountRepository;

    @Transactional
    public void buyItem(Player player, String itemName, int amount) {
        ShopItem item = shopItemRepository.findByItemName(itemName)
                .orElseThrow(() -> new IllegalArgumentException("아이템을 찾을 수 없습니다. : " + itemName));

        BigDecimal totalCost = item.getBuyPrice().multiply(BigDecimal.valueOf(amount));
        PlayerAccount account = playerAccountRepository.findById(player.getUniqueId().toString())
                .orElseThrow(() -> new IllegalStateException("플레이어 계정을 찾을 수 없습니다."));

        if (account.getBalance().compareTo(totalCost) < 0) {
            throw new IllegalStateException("돈이 충분하지 않습니다. ");
        }

        // 거래 처리
        account.setBalance(account.getBalance().subtract(totalCost));
        ItemStack itemStack = item.getItemStack();
        itemStack.setAmount(amount);
        player.getInventory().addItem(itemStack);

        // 거래 기록
        Transaction transaction = new Transaction();
        transaction.setPlayerUuid(player.getUniqueId().toString());
        transaction.setItem(item);
        transaction.setAmount(amount);
        transaction.setPrice(totalCost);
        transaction.setType(Transaction.TransactionType.BUY);
        transaction.setTransactionTime(LocalDateTime.now());

        // 저장
        transactionRepository.save(transaction);
        playerAccountRepository.save(account);
    }

    @Transactional
    public void addItem(ItemStack item, String itemName, BigDecimal buyPrice,
                        BigDecimal sellPrice, String itemTag) {
        ShopItem shopItem = new ShopItem();
        shopItem.setItemName(itemName);
        shopItem.setItemStack(item);
        shopItem.setBuyPrice(buyPrice);
        shopItem.setSellPrice(sellPrice);
        shopItem.setItemTag(itemTag);
        shopItem.setSoldAmount(0);

        shopItemRepository.save(shopItem);
    }

    @Transactional
    public void removeItem(String itemName) {
        ShopItem item = shopItemRepository.findByItemName(itemName)
                .orElseThrow(() -> new IllegalArgumentException("아이템을 찾을 수 없습니다: " + itemName));

        shopItemRepository.delete(item);
    }
}
