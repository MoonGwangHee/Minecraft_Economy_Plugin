package teddy.economyplugin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teddy.economyplugin.entity.ShopItem;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShopItemRepository extends JpaRepository<ShopItem, Long> {
    List<ShopItem> findByItemTag(String itemTag);
    Optional<ShopItem> findByItemName(String itemName);
}
