package homk.terraria1accessories;

import homk.terraria1accessories.network.DummyPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Terraria1accessories implements ModInitializer {
	public static final String MOD_ID = "terraria1accessories";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	// Список игроков с установленным модом
	public static final Set<UUID> MODDED_PLAYERS = new HashSet<>();

	@Override
	public void onInitialize() {
		PayloadTypeRegistry.playS2C().register(DummyPayload.TYPE, DummyPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(DummyPayload.TYPE, DummyPayload.CODEC);

		ServerPlayNetworking.registerGlobalReceiver(DummyPayload.TYPE, (payload, context) -> {
			context.server().execute(() -> {
				// Добавляем игрока в список доверенных
				MODDED_PLAYERS.add(context.player().getUUID());

				// Принудительно обновляем инвентарь, теперь аксессуары не отрежутся
				context.player().inventoryMenu.broadcastFullState();

				LOGGER.info("Player {} confirmed mod presence. Syncing 50 slots.", context.player().getName().getString());
			});
		});

		LOGGER.info("Terraria Accessories initialized!");
	}
}