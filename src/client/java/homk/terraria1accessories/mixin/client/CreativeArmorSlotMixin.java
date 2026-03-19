package homk.terraria1accessories.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Constructor;
import java.util.List;

@Mixin(CreativeModeInventoryScreen.class)
public abstract class CreativeArmorSlotMixin {

	@Inject(method = "selectTab", at = @At("TAIL"))
	private void addVisualSlotsToCreative(CreativeModeTab tab, CallbackInfo ci) {
		if (tab.getType() == CreativeModeTab.Type.INVENTORY) {
			Minecraft mc = Minecraft.getInstance();
			if (mc.player == null) return;

			// Используем (Object) this для безопасного каста
			CreativeModeInventoryScreen screen = (CreativeModeInventoryScreen) (Object) this;
			var menu = screen.getMenu();
			if (menu == null) return;

			List<Slot> creativeSlots = menu.slots;
			var playerMenu = mc.player.inventoryMenu;

			for (int i = 0; i < 4; i++) {
				int slotIndex = 46 + i;
				int x = (i < 2) ? 126 : 144;
				int y = (i % 2 == 0) ? 6 : 33;

				Slot targetSlot = playerMenu.getSlot(slotIndex);

				// Создаем SlotWrapper через улучшенную рефлексию
				Slot wrapper = createVanillaSlotWrapper(targetSlot, slotIndex, x, y);
				if (wrapper != null) {
					creativeSlots.add(wrapper);
				}
			}
		}
	}

	@Unique
	private Slot createVanillaSlotWrapper(Slot target, int index, int x, int y) {
		try {
			Class<?> wrapperClass = null;
			for (Class<?> inner : CreativeModeInventoryScreen.class.getDeclaredClasses()) {
				if (inner.getSimpleName().equals("SlotWrapper")) {
					wrapperClass = inner;
					break;
				}
			}

			if (wrapperClass == null) {
				wrapperClass = Class.forName("net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen$SlotWrapper");
			}

			Constructor<?> constructor = wrapperClass.getDeclaredConstructor(Slot.class, int.class, int.class, int.class);
			constructor.setAccessible(true);

			return (Slot) constructor.newInstance(target, index, x, y);
		} catch (Exception e) {
			System.err.println("[TerrariaMod] Failed to instantiate SlotWrapper: " + e.getMessage());
			return null;
		}
	}
}