package homk.terraria1accessories.mixin;

import homk.terraria1accessories.VisualArmorHolder;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.equipment.Equippable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryMenu.class)
public abstract class InventoryMenuMixin {
	@Unique
	private static final EquipmentSlot[] VISUAL_SLOT_TYPES = {
			EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
	};

	@Inject(method = "<init>", at = @At("TAIL"))
	private void addVisualSlots(Inventory inventory, boolean active, Player player, CallbackInfo ci) {
		VisualArmorHolder holder = (VisualArmorHolder) player;
		SimpleContainer visualInv = holder.getVisualArmorContainer();
		AbstractContainerMenuAccessor menu = (AbstractContainerMenuAccessor) this;

		for (int i = 0; i < 4; i++) {
			final EquipmentSlot targetSlot = VISUAL_SLOT_TYPES[i];
			menu.callAddSlot(new Slot(visualInv, i, 30, 8 + i * 18) {
				@Override
				public boolean mayPlace(ItemStack stack) {
					Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
					return equippable != null && equippable.slot() == targetSlot;
				}
			});
		}
	}
}