package teddy.economyplugin.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.springframework.stereotype.Service;
import teddy.economyplugin.entity.ShopNPC;
import teddy.economyplugin.repository.ShopNPCRepository;

@Service
@RequiredArgsConstructor
public class NPCService {

    private final ShopNPCRepository shopNPCRepository;
    private final CitizensAPI citizensAPI;  // Citizens 플러그인 API

    @Transactional
    public void createNPC(String npcName, World world, Location location, String npcTag) {
        // 이미 존재하는 NPC 이름인지 확인
        if (shopNPCRepository.findByNpcName(npcName).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 NPC 이름입니다: " + npcName);
        }

        try {
            // Citizens API를 사용하여 NPC 생성
            NPCRegistry registry = CitizensAPI.getNPCRegistry();
            // 먼저 NPCRegistry를 가져온 후
            if (registry != null) {
                // 여기서 NPC를 생성합니다
                NPC npc = registry.createNPC(, npcName, location);

                if (npc != null) {
                    // DB에 NPC 정보 저장
                    ShopNPC shopNPC = new ShopNPC();
                    shopNPC.setNpcId(npc.getId());
                    shopNPC.setNpcName(npcName);
                    shopNPC.setWorld(world.getName());
                    shopNPC.setLocation(location);
                    shopNPC.setNpcTag(npcTag);

                    shopNPCRepository.save(shopNPC);
                } else {
                    throw new IllegalStateException("NPC 생성에 실패했습니다.");
                }
            } else {
                throw new IllegalStateException("Citizens API를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            throw new IllegalStateException("NPC 생성 중 오류 발생: " + e.getMessage());
        }
    }

    @Transactional
    public void removeNPC(String npcName) {
        ShopNPC shopNPC = shopNPCRepository.findByNpcName(npcName)
                .orElseThrow(() -> new IllegalArgumentException("NPC를 찾을 수 없습니다: " + npcName));

        // Citizens API를 사용하여 NPC 제거
        NPC npc = CitizensAPI.getNPCRegistry().getById(shopNPC.getNpcId());
        if (npc != null) {
            npc.destroy();
        }

        shopNPCRepository.delete(shopNPC);
    }

    @Transactional
    public void reloadNPCs() {
        // 모든 NPC를 데이터베이스에서 불러와 다시 스폰
        shopNPCRepository.findAll().forEach(shopNPC -> {
            NPC npc = CitizensAPI.getNPCRegistry().getById(shopNPC.getNpcId());
            if (npc != null) {
                npc.destroy();
                NPC newNPC = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, shopNPC.getNpcName());
                newNPC.spawn(shopNPC.getLocation());
            }
        });
    }
}
