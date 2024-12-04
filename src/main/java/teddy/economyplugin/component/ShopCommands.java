package teddy.economyplugin.component;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.springframework.stereotype.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import teddy.economyplugin.service.ShopService;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class ShopCommands implements CommandExecutor {
    private final ShopService shopService;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players!");
            return true;
        }

        Player player = (Player) sender;

        switch (command.getName().toLowerCase()) {
            case "additem":
                return handleAddItem(player, args);
            case "removeitem":
                return handleRemoveItem(player, args);
            case "createnpc":
                return handleCreateNPC(player, args);
            // 기타 명령어들...
        }

        return false;
    }

    private boolean handleAddItem(Player player, String[] args) {
        if (!player.isOp()) {
            player.sendMessage("You don't have permission to use this command!");
            return true;
        }

        if (args.length != 4) {
            player.sendMessage("Usage: /additem <name> <buyPrice> <sellPrice> <tag>");
            return true;
        }

        try {
            String itemName = args[0];
            BigDecimal buyPrice = new BigDecimal(args[1]);
            BigDecimal sellPrice = new BigDecimal(args[2]);
            String itemTag = args[3];

            shopService.addItem(
                    player.getInventory().getItemInMainHand(),
                    itemName,
                    buyPrice,
                    sellPrice,
                    itemTag
            );

            player.sendMessage("Item added successfully!");
            return true;
        } catch (Exception e) {
            player.sendMessage("Error: " + e.getMessage());
            return true;
        }
    }


    private boolean handleRemoveItem(Player player, String[] args) {
        if (!player.isOp()) {
            player.sendMessage("§c권한이 없습니다.");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage("§c사용법: /removeitem <아이템명>");
            return true;
        }

        String itemName = args[0];
        try {
            shopService.removeItem(itemName);
            player.sendMessage("§a아이템이 성공적으로 삭제되었습니다: " + itemName);
        } catch (IllegalArgumentException e) {
            player.sendMessage("§c해당 아이템을 찾을 수 없습니다: " + itemName);
        } catch (Exception e) {
            player.sendMessage("§c오류가 발생했습니다: " + e.getMessage());
            e.printStackTrace();
        }
        return true;
    }

    private boolean handleCreateNPC(Player player, String[] args) {
        if (!player.isOp()) {
            player.sendMessage("§c권한이 없습니다.");
            return true;
        }

        if (args.length != 3) {
            player.sendMessage("§c사용법: /createnpc <NPC명> <월드> <NPC태그>");
            return true;
        }

        String npcName = args[0];
        String worldType = args[1].toLowerCase();
        String npcTag = args[2];

        // 월드 타입 검증
        World targetWorld;
        try {
            targetWorld = switch (worldType) {
                case "네더" -> {
                    World nether = Bukkit.getWorld("world_nether");
                    if (nether == null) throw new IllegalArgumentException("네더 월드를 찾을 수 없습니다.");
                    yield nether;
                }
                case "엔더" -> {
                    World end = Bukkit.getWorld("world_the_end");
                    if (end == null) throw new IllegalArgumentException("엔더 월드를 찾을 수 없습니다.");
                    yield end;
                }
                case "오버월드" -> {
                    World overworld = Bukkit.getWorld("world");
                    if (overworld == null) throw new IllegalArgumentException("오버월드를 찾을 수 없습니다.");
                    yield overworld;
                }
                default -> throw new IllegalArgumentException("잘못된 월드 타입입니다. (네더/엔더/오버월드)");
            };
        } catch (IllegalArgumentException e) {
            player.sendMessage("§c" + e.getMessage());
            return true;
        }

        try {
            // 플레이어의 현재 위치와 방향을 기준으로 NPC 생성
            Location spawnLocation = player.getLocation();

            // NPC가 플레이어를 바라보도록 설정
            Location playerLoc = player.getLocation();
            spawnLocation.setYaw(calculateYaw(spawnLocation, playerLoc));

            npcService.createNPC(npcName, targetWorld, spawnLocation, npcTag);
            player.sendMessage("§aNPC가 생성되었습니다: " + npcName);
        } catch (IllegalArgumentException e) {
            player.sendMessage("§c" + e.getMessage());
        } catch (Exception e) {
            player.sendMessage("§c오류가 발생했습니다: " + e.getMessage());
            e.printStackTrace();
        }
        return true;
    }

    // NPC가 플레이어를 바라보도록 Yaw 계산
    private float calculateYaw(Location npcLoc, Location playerLoc) {
        double xDiff = playerLoc.getX() - npcLoc.getX();
        double zDiff = playerLoc.getZ() - npcLoc.getZ();
        return (float) (Math.atan2(zDiff, xDiff) * (180 / Math.PI)) - 90;
    }
}
