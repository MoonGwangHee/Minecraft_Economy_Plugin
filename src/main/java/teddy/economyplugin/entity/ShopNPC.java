package teddy.economyplugin.entity;

import com.google.gson.Gson;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.bukkit.Location;

@Entity
@Table(name = "shop_npcs")
@Data
public class ShopNPC {

    @Id
    private Integer npcId;  // Citizens NPC ID

    @Column(nullable = false)
    private String npcName;

    @Column(nullable = false)
    private String world;

    @Column
    private String npcTag;

    @Column(nullable = false)
    private String location;  // Location을 JSON으로 저장

    // Location 변환 메소드
    public Location getLocation() {
        return new Gson().fromJson(this.location, Location.class);
    }

    public void setLocation(Location location) {
        this.location = new Gson().toJson(location);
    }
}
