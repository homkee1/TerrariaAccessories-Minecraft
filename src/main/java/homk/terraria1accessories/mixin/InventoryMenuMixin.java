package homk.terraria1accessories.mixin;

import com.mojang.datafixers.util.Pair;
import homk.terraria1accessories.VisualArmorHolder;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(InventoryMenu.class)
public abstract class InventoryMenuMixin {

	@Unique
	private static final EquipmentSlot[] VISUAL_SLOT_TYPES = {
			EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
	};

	@Unique
	private static final ResourceLocation[] VISUAL_ICONS = {
			ResourceLocation.withDefaultNamespace("container/slot/helmet"),
			ResourceLocation.withDefaultNamespace("container/slot/chestplate"),
			ResourceLocation.withDefaultNamespace("container/slot/leggings"),
			ResourceLocation.withDefaultNamespace("container/slot/boots")
	};

	@Inject(method = "<init>", at = @At("TAIL"))
	private void addVisualSlots(Inventory inventory, boolean active, Player player, CallbackInfo ci) {
		VisualArmorHolder holder = (VisualArmorHolder) player;
		SimpleContainer visualInv = holder.getVisualArmorContainer();
		AbstractContainerMenuAccessor menu = (AbstractContainerMenuAccessor) this;

		for (int i = 0; i < 4; i++) {
			final EquipmentSlot targetSlot = VISUAL_SLOT_TYPES[i];
			final ResourceLocation icon = VISUAL_ICONS[i];

			menu.callAddSlot(new Slot(visualInv, i, 30, 8 + i * 18) {
				@Override
				public void set(ItemStack stack) {
					super.set(stack);
					if (player instanceof ServerPlayer sp) {
						broadcastVisualChange(sp, targetSlot, stack);
					}
				}
				@Override
				public boolean mayPlace(ItemStack stack) {
					Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
					return equippable != null && equippable.slot() == targetSlot;
				}
				@Override
				public ResourceLocation getNoItemIcon() {
					return icon;
				}
			});
		}
	}

	@Inject(method = "quickMoveStack", at = @At("HEAD"), cancellable = true)
	private void onQuickMoveStack(Player player, int index, CallbackInfoReturnable<ItemStack> cir) {
		AbstractContainerMenuAccessor menu = (AbstractContainerMenuAccessor) this;
		NonNullList<Slot> allSlots = menu.getSlots();
		Slot clickedSlot = allSlots.get(index);

		if (clickedSlot != null && clickedSlot.hasItem()) {
			ItemStack stack = clickedSlot.getItem();
			EquipmentSlot eqSlot = player.getEquipmentSlotForItem(stack);

			if (eqSlot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
				if (index >= 9 && index < 45) {
					int vanillaArmorIndex = 8 - eqSlot.getIndex();
					Slot vanillaSlot = allSlots.get(vanillaArmorIndex);

					if (vanillaSlot != null && vanillaSlot.hasItem()) {
						int visualIndex = switch (eqSlot) {
							case HEAD -> 46; case CHEST -> 47; case LEGS -> 48; case FEET -> 49;
							default -> -1;
						};

						if (visualIndex != -1) {
							Slot visualSlot = allSlots.get(visualIndex);
							if (!visualSlot.hasItem()) {
								ItemStack copy = stack.copy();
								copy.setCount(1);
								visualSlot.setByPlayer(copy);
								stack.shrink(1);
								cir.setReturnValue(ItemStack.EMPTY);
							}
						}
					}
				} else if (index >= 46 && index <= 49) {
					if (!menu.callMoveItemStackTo(stack, 9, 45, false)) {
						cir.setReturnValue(ItemStack.EMPTY);
						return;
					}
					if (stack.isEmpty()) {
						clickedSlot.setByPlayer(ItemStack.EMPTY);
						if (player instanceof ServerPlayer sp) {
							broadcastVisualChange(sp, VISUAL_SLOT_TYPES[index - 46], ItemStack.EMPTY);
						}
					}
					cir.setReturnValue(ItemStack.EMPTY);
				}
			}
		}
	}

	@Unique
	private void broadcastVisualChange(ServerPlayer player, EquipmentSlot slot, ItemStack stack) {
		// Создаем пакет для других игроков
		ClientboundSetEquipmentPacket packet = new ClientboundSetEquipmentPacket(
				player.getId(),
				List.of(Pair.of(slot, stack.copy()))
		);

		// УДАЛЕНО: player.connection.send(packet);
		// Мы больше не шлем этот пакет самому себе, чтобы не дублировать слоты.

		// Шлем пакет только тем, кто видит игрока
		for (ServerPlayer tracker : PlayerLookup.tracking(player)) {
			tracker.connection.send(packet);
		}
	}
}