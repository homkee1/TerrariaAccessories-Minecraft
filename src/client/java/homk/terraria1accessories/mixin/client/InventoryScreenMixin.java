package homk.terraria1accessories.mixin.client;

import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin {

	@ModifyArgs(
			method = "renderBg",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/screens/inventory/InventoryScreen;renderEntityInInventoryFollowsMouse(Lnet/minecraft/client/gui/GuiGraphics;IIIIIFFFLnet/minecraft/world/entity/LivingEntity;)V"
			)
	)
	private void shiftPlayerModel(Args args) {
		if (args.size() >= 10) {
			// args.get(1)левая граница
			// args.get(3)правая граница

			int currentX1 = args.get(1);
			int currentX2 = args.get(3);

			args.set(1, currentX1 + 10);
			args.set(3, currentX2 + 10);
		}
	}
}