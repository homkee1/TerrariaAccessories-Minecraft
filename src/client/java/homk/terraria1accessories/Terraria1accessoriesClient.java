package homk.terraria1accessories;

import homk.terraria1accessories.network.DummyPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class Terraria1accessoriesClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// Регистрируем приемник на клиенте (даже если он ничего не делает),
		// это нужно для корректной работы ServerPlayNetworking.canSend
		ClientPlayNetworking.registerGlobalReceiver(DummyPayload.TYPE, (payload, context) -> {});

		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
			// Отправляем серверу сигнал "Я тут, у меня есть мод"
			ClientPlayNetworking.send(new DummyPayload());
		});
	}
}