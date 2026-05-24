package homk.terraria1accessories;

import com.mojang.datafixers.util.Pair;
import homk.terraria1accessories.network.DummyPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Terraria1accessories implements ModInitializer {
	public static final String MOD_ID = "terraria1accessories";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final Set<UUID> MODDED_PLAYERS = new HashSet<>();

	@Override
	public void onInitialize() {
		PayloadTypeRegistry.playS2C().register(DummyPayload.TYPE, DummyPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(DummyPayload.TYPE, DummyPayload.CODEC);

		ServerPlayNetworking.registerGlobalReceiver(DummyPayload.TYPE, (payload, context) -> {
			context.server().execute(() -> {
				MODDED_PLAYERS.add(context.player().getUUID());
				context.player().inventoryMenu.broadcastFullState();

				syncPlayerEquipment(context.player());
			});
		});

		EntityTrackingEvents.START_TRACKING.register((trackedEntity, trackerPlayer) -> {
			if (trackedEntity instanceof ServerPlayer wearer) {
				syncPlayerToObserver(wearer, trackerPlayer);
			}
		});

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayer player = handler.player;

			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					server.execute(() -> {

						if (player.connection != null) {
							syncPlayerEquipment(player);
						}
					});
				}
			}, 2000);
		});

		LOGGER.info("Terraria Accessories initialized!");
	}


	public static void syncPlayerEquipment(ServerPlayer player) {
		List<Pair<EquipmentSlot, ItemStack>> slots = new ArrayList<>();
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
				slots.add(Pair.of(slot, player.getItemBySlot(slot).copy()));
			}
		}

		ClientboundSetEquipmentPacket packet = new ClientboundSetEquipmentPacket(player.getId(), slots);

		for (ServerPlayer tracker : PlayerLookup.tracking(player)) {
			tracker.connection.send(packet);
		}
	}


	public static void syncPlayerToObserver(ServerPlayer wearer, ServerPlayer observer) {
		List<Pair<EquipmentSlot, ItemStack>> slots = new ArrayList<>();
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
				slots.add(Pair.of(slot, wearer.getItemBySlot(slot).copy()));
			}
		}
		observer.connection.send(new ClientboundSetEquipmentPacket(wearer.getId(), slots));
	}
}
