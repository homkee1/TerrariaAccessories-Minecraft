package homk.terraria1accessories.mixin;

import homk.terraria1accessories.VisualArmorHolder;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.ItemStackWithSlot;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.ArrayList;
import java.util.List;

@Mixin(Player.class)
public abstract class PlayerEntityMixin implements VisualArmorHolder {
	@Unique
	private final SimpleContainer visualArmor = new SimpleContainer(4);

	@Override
	public SimpleContainer getVisualArmorContainer() { return this.visualArmor; }

	@Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
	private void saveVisualArmor(ValueOutput output, CallbackInfo ci) {
		List<ItemStackWithSlot> list = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			ItemStack stack = this.visualArmor.getItem(i);
			if (!stack.isEmpty()) {
				list.add(new ItemStackWithSlot(i, stack));
			}
		}
		output.store("VisualArmor", ItemStackWithSlot.CODEC.listOf(), list);
	}

	@Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
	private void loadVisualArmor(ValueInput input, CallbackInfo ci) {
		// Используем listOrEmpty, как в ванильном коде 1.21.10
		List<ItemStackWithSlot> list = (List<ItemStackWithSlot>) input.listOrEmpty("VisualArmor", ItemStackWithSlot.CODEC);
		if (!list.isEmpty()) {
			this.visualArmor.clearContent();
			for (ItemStackWithSlot entry : list) {
				if (entry.slot() >= 0 && entry.slot() < 4) {
					this.visualArmor.setItem(entry.slot(), entry.stack());
				}
			}
		}
	}
}