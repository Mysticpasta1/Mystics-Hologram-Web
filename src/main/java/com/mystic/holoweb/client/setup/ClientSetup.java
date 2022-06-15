package com.mystic.holoweb.client.setup;

import com.mystic.holoweb.HoloWeb;
import com.mystic.holoweb.client.HoloScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

@Environment(EnvType.CLIENT)
public class ClientSetup implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ScreenRegistry.register(HoloWeb.HOLO_SCREEN_HANDLER, HoloScreen::new);
    }
}
