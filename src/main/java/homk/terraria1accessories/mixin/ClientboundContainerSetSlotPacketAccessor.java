package homk.terraria1accessories.mixin;

import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientboundContainerSetSlotPacket.class)
public interface ClientboundContainerSetSlotPacketAccessor {
    @Accessor("containerId") int callGetContainerId();
    @Accessor("slot") int callGetSlot();
}