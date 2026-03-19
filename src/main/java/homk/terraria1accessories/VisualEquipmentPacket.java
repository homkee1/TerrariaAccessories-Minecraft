package homk.terraria1accessories;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import java.util.List;

public interface VisualEquipmentPacket {
    int terraria$getEntityId();
    List<Pair<EquipmentSlot, ItemStack>> terraria$getSlots();
}