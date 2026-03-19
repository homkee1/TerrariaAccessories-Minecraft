package homk.terraria1accessories;

import homk.terraria1accessories.network.DummyPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Terraria1accessories implements ModInitializer {
	public static final String MOD_ID = "terraria1accessories";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// Регистрируем сетевой канал, чтобы отличать модовых игроков от ванильных
		PayloadTypeRegistry.playS2C().register(DummyPayload.TYPE, DummyPayload.CODEC);

		LOGGER.info("Terraria Accessories initialized!");
	}
}