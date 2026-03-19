package homk.terraria1accessories.mixin.client;

import homk.terraria1accessories.VisualStateAccessor;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import com.mojang.blaze3d.vertex.PoseStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin {
	@ModifyVariable(method = "renderArmorPiece", at = @At("HEAD"), argsOnly = true, ordinal = 0)
	private ItemStack swapArmor(ItemStack original, PoseStack poseStack, SubmitNodeCollector collector, ItemStack stack, EquipmentSlot slot, int light, HumanoidRenderState state) {
		VisualStateAccessor accessor = (VisualStateAccessor) (Object) state;
		int idx = switch (slot) {
			case HEAD -> 0;
			case CHEST -> 1;
			case LEGS -> 2;
			case FEET -> 3;
			default -> -1;
		};

		if (idx != -1) {
			ItemStack visual = accessor.getVisualArmor(idx);
			if (!visual.isEmpty()) {
				return visual;
			}
		}
		return original;
	}
}