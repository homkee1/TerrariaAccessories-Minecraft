package homk.terraria1accessories.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record DummyPayload() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<DummyPayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("terraria1accessories", "dummy"));
    public static final StreamCodec<FriendlyByteBuf, DummyPayload> CODEC = StreamCodec.unit(new DummyPayload());

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}