package homk.terraria1accessories.mixin;

import homk.terraria1accessories.VisualArmorHolder;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.network.protocol.game.ServerboundSetCreativeModeSlotPacket;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.server.level.ServerPlayer;

@Mixin(ServerGamePacketListenerImpl.class)
public class CreativeSyncMixin {
    @Shadow public ServerPlayer player;

    @Inject(method = "handleSetCreativeModeSlot", at = @At("HEAD"))
    private void syncVisualSlots(ServerboundSetCreativeModeSlotPacket packet, CallbackInfo ci) {
        int slotNum = packet.slotNum();
        // Индексы наших слотов в InventoryMenu (46, 47, 48, 49)
        if (slotNum >= 46 && slotNum <= 49) {
            Slot slot = this.player.inventoryMenu.getSlot(slotNum);
            slot.set(packet.itemStack()); // Принудительно ставим предмет на сервере
            this.player.inventoryMenu.broadcastChanges();
        }
    }
}