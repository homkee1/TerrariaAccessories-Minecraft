package homk.terraria1accessories.mixin.client;

import homk.terraria1accessories.VisualArmorHolder;
import homk.terraria1accessories.VisualStateAccessor;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public class AvatarRendererMixin {

	@Inject(method = "extractRenderState", at = @At("TAIL"))
	private void copyVisuals(net.minecraft.world.entity.Avatar avatar, AvatarRenderState state, float f, CallbackInfo ci) {

		if (avatar instanceof Player player) {
			VisualArmorHolder holder = (VisualArmorHolder) player;
			VisualStateAccessor stateAccessor = (VisualStateAccessor) (Object) state;

			for (int i = 0; i < 4; i++) {
				stateAccessor.setVisualArmor(i, holder.getVisualArmorContainer().getItem(i));
			}
		}
	}
}