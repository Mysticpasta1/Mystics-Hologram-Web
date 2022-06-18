package com.mystic.holoweb.client.render.block;

import com.mystic.holoweb.client.render.HologramRenderLayer;
import com.mystic.holoweb.client.render.RenderHoloWeb;
import com.mystic.holoweb.core.blockentities.HoloScreenBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

import java.net.MalformedURLException;

public class HoloWebBlockEntityRenderer extends GeoBlockRenderer<HoloScreenBlockEntity> {

    private static VertexConsumerProvider.Immediate immediate;

    public HoloWebBlockEntityRenderer() {
        super(new HoloWebBlockModel());
    }

    @Override
    public void render(BlockEntity tile, float partialTicks, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int combinedLightIn, int combinedOverlayIn) {
        if (immediate == null) {
            immediate = HologramRenderLayer.initBuffers(MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers());
        }

        matrices.push();

        matrices.translate(0.5, 0.5, 0.5);
        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90));
        matrices.translate(-0.5, -0.5, -0.5);

        final BufferBuilder buffer = (BufferBuilder) vertexConsumers.getBuffer(RenderLayer.getLightning());
        final Matrix4f matrix4f = matrices.peek().getPositionMatrix();

        final float r = 0.5f;
        final float g = 0.5f;
        final float b = 1;

        final float bottomY = 0.3f;
        final float topY = 0.7f;
        final float startAlpha = 0.65f;

        vertex(matrix4f, buffer, 0.1f, bottomY, 0.125f, r, g, b, startAlpha);
        vertex(matrix4f, buffer, 0.9f, bottomY, 0.125f, r, g, b, startAlpha);
        vertex(matrix4f, buffer, 1, topY, -0.25f, r, g, b, 0);
        vertex(matrix4f, buffer, 0, topY, -0.25f, r, g, b, 0);

        vertex(matrix4f, buffer, 0, topY, -0.25f, r, g, b, 0);
        vertex(matrix4f, buffer, 1, topY, -0.25f, r, g, b, 0);
        vertex(matrix4f, buffer, 0.9f, bottomY, 0.125f, r, g, b, startAlpha);
        vertex(matrix4f, buffer, 0.1f, bottomY, 0.125f, r, g, b, startAlpha);


        vertex(matrix4f, buffer, 0.1f, bottomY, 0.875f, r, g, b, startAlpha);
        vertex(matrix4f, buffer, 0.9f, bottomY, 0.875f, r, g, b, startAlpha);
        vertex(matrix4f, buffer, 1, topY, 1.25f, r, g, b, 0);
        vertex(matrix4f, buffer, 0, topY, 1.25f, r, g, b, 0);

        vertex(matrix4f, buffer, 0, topY, 1.25f, r, g, b, 0);
        vertex(matrix4f, buffer, 1, topY, 1.25f, r, g, b, 0);
        vertex(matrix4f, buffer, 0.9f, bottomY, 0.875f, r, g, b, startAlpha);
        vertex(matrix4f, buffer, 0.1f, bottomY, 0.875f, r, g, b, startAlpha);


        vertex(matrix4f, buffer, 0.875f, bottomY, 0.1f, r, g, b, startAlpha);
        vertex(matrix4f, buffer, 0.875f, bottomY, 0.9f, r, g, b, startAlpha);
        vertex(matrix4f, buffer, 1.25f, topY, 1, r, g, b, 0);
        vertex(matrix4f, buffer, 1.25f, topY, 0, r, g, b, 0);

        vertex(matrix4f, buffer, 1.25f, topY, 0, r, g, b, 0);
        vertex(matrix4f, buffer, 1.25f, topY, 1, r, g, b, 0);
        vertex(matrix4f, buffer, 0.875f, bottomY, 0.9f, r, g, b, startAlpha);
        vertex(matrix4f, buffer, 0.875f, bottomY, 0.1f, r, g, b, startAlpha);


        vertex(matrix4f, buffer, 0.125f, bottomY, 0.1f, r, g, b, startAlpha);
        vertex(matrix4f, buffer, 0.125f, bottomY, 0.9f, r, g, b, startAlpha);
        vertex(matrix4f, buffer, -0.25f, topY, 1, r, g, b, 0);
        vertex(matrix4f, buffer, -0.25f, topY, 0, r, g, b, 0);

        vertex(matrix4f, buffer, -0.25f, topY, 0, r, g, b, 0);
        vertex(matrix4f, buffer, -0.25f, topY, 1, r, g, b, 0);
        vertex(matrix4f, buffer, 0.125f, bottomY, 0.9f, r, g, b, startAlpha);
        vertex(matrix4f, buffer, 0.125f, bottomY, 0.1f, r, g, b, startAlpha);

        matrices.pop();

        matrices.push();
        new RenderHoloWeb().render(matrices, immediate, partialTicks, combinedLightIn, combinedOverlayIn, tile);
        matrices.pop();
    }
    private void vertex(Matrix4f matrix, VertexConsumer buffer, float x, float y, float z, float r, float g, float b, float a) {
        buffer.vertex(matrix, x, y, z).color(r, g, b, a).next();
    }
}
