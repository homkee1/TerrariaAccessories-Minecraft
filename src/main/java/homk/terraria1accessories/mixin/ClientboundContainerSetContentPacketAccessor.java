package homk.terraria1accessories.mixin;

import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(ClientboundContainerSetContentPacket.class)
public interface ClientboundContainerSetContentPacketAccessor {
    @Accessor("containerId") int callGetContainerId();
    @Accessor("items") List<ItemStack> callGetItems();
    @Accessor("stateId") int callGetStateId();
    @Accessor("carriedItem") ItemStack callGetCarriedItem();
}