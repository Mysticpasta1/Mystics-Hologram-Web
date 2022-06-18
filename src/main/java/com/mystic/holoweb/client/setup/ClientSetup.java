package com.mystic.holoweb.client.setup;

import com.mystic.holoweb.Common;
import com.mystic.holoweb.HoloWeb;
import com.mystic.holoweb.client.HoloScreen;
import com.mystic.holoweb.client.HoloScreenRoot;
import com.mystic.holoweb.client.render.AbstractRenderWeb;
import com.mystic.holoweb.client.render.block.HoloWebBlockEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.montoyo.mcef.api.*;

@Environment(EnvType.CLIENT)
public class ClientSetup implements ClientModInitializer, IDisplayHandler{
    private static net.montoyo.mcef.api.API mcef;

    @Override
    public void onInitializeClient() {
        AbstractRenderWeb.registerDefaultProviders();
        Common.textScreenRunnable = (hand -> MinecraftClient.getInstance().setScreen(new HoloScreen(new HoloScreenRoot(hand))));
        mcef = MCEFApi.getAPI();
        /*-------------------------------------NEXT-STAGE-------------------------------------------*/
        if(mcef == null)
            throw new RuntimeException("MCEF is missing");

        mcef.registerDisplayHandler(this);
        BlockEntityRendererRegistry.register(HoloWeb.HOLO_BLOCK_ENTITY, (BlockEntityRendererFactory.Context rendererDispatcherIn) -> new HoloWebBlockEntityRenderer());
    }

    public static net.montoyo.mcef.api.API getMCEF() {
        return mcef;
    }

    @Override
    public void onAddressChange(IBrowser browser, String url) {
        if(browser != null) {
            long t = System.currentTimeMillis();
        }
        HoloScreenRoot.Screen scr = new HoloScreenRoot.Screen();
        scr.updateClientSideURL(browser, url);
    }

    @Override
    public void onTitleChange(IBrowser browser, String title) {
    }

    @Override
    public void onTooltip(IBrowser browser, String text) {
    }

    @Override
    public void onStatusMessage(IBrowser browser, String value) {
    }
}
