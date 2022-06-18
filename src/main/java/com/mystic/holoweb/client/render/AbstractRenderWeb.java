package com.mystic.holoweb.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.net.MalformedURLException;

public abstract class AbstractRenderWeb {

    protected AbstractRenderWeb() {}

    @Environment(EnvType.CLIENT)
    public abstract void render(MatrixStack matrices, VertexConsumerProvider.Immediate immediate, float tickDelta, int light, int overlay, BlockEntity be) throws MalformedURLException;

    public static void registerDefaultProviders() {
        HoloProviderRegistry.register(RenderHoloWeb.ID, RenderHoloWeb::new);
    }

    public abstract Identifier getTypeId();
}

