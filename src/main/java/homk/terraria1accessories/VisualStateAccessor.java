package homk.terraria1accessories;

import net.minecraft.world.item.ItemStack;

public interface VisualStateAccessor {
    void setVisualArmor(int slot, ItemStack stack);
    ItemStack getVisualArmor(int slot);
}