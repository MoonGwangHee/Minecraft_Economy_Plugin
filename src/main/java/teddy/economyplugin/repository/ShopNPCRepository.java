package teddy.economyplugin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teddy.economyplugin.entity.ShopNPC;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShopNPCRepository extends JpaRepository<ShopNPC, Long> {
    List<ShopNPC> findByNpcTag(String npcTag);
    Optional<ShopNPC> findByNpcName(String npcName);
}
