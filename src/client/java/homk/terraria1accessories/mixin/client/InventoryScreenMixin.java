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
		// Проверяем, что в методе действительно 10 аргументов (от 0 до 9)
		if (args.size() >= 10) {
			// Согласно твоему коду:
			// args.get(1) это k + 26 (левая граница)
			// args.get(3) это k + 75 (правая граница)

			int currentX1 = args.get(1);
			int currentX2 = args.get(3);

			// Сдвигаем на 30 пикселей вправо, чтобы освободить место под новые слоты
			args.set(1, currentX1 + 10);
			args.set(3, currentX2 + 10);
		}
	}
}