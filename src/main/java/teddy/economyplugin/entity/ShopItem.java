package teddy.economyplugin.entity;


import com.google.gson.Gson;
import jakarta.persistence.*;
import lombok.Data;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "shop_items")
public class ShopItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false)
    private String itemData;  // ItemStack을 JSON으로 저장

    @Column(nullable = false)
    private BigDecimal buyPrice;

    @Column(nullable = false)
    private BigDecimal sellPrice;

    @Column
    private String itemTag;

    @Column
    private int soldAmount;

    // ItemStack 변환 메소드
    public ItemStack getItemStack() {
        return new Gson().fromJson(this.itemData, ItemStack.class);
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemData = new Gson().toJson(itemStack);
    }
}
