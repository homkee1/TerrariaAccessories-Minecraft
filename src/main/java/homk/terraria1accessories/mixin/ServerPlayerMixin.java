package homk.terraria1accessories.mixin;

import homk.terraria1accessories.Terraria1accessories;
import homk.terraria1accessories.VisualArmorHolder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {

	/**
	 * ЛОГИКА ПРИ СМЕРТИ: Копируем аксессуары из старого тела игрока в новое.
	 */
	@Inject(method = "restoreFrom", at = @At("HEAD"))
	private void copyVisualArmorOnRespawn(ServerPlayer oldPlayer, boolean alive, CallbackInfo ci) {
		SimpleContainer oldInv = ((VisualArmorHolder) oldPlayer).getVisualArmorContainer();
		SimpleContainer newInv = ((VisualArmorHolder) this).getVisualArmorContainer();

		for (int i = 0; i < 4; i++) {
			if (!oldInv.getItem(i).isEmpty()) {
				newInv.setItem(i, oldInv.getItem(i).copy());
			}
		}
	}

	/**
	 * ЛОГИКА ПРИ ВЫХОДЕ: Удаляем UUID игрока из списка доверенных (модовых).
	 */
	@Inject(method = "disconnect", at = @At("HEAD"))
	private void onDisconnect(CallbackInfo ci) {
		ServerPlayer player = (ServerPlayer) (Object) this;
		Terraria1accessories.MODDED_PLAYERS.remove(player.getUUID());
	}
}