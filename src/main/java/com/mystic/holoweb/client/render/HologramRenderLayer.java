package com.mystic.holoweb.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mystic.holoweb.HoloWeb;
import com.mystic.holoweb.mixin.VertexConsumerProviderImmediateAccessor;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class HologramRenderLayer extends RenderLayer {

    //TODO refactor this and make it not shit

    private static final Map<RenderLayer, RenderLayer> remappedTypes = new IdentityHashMap<>();
    private static float alpha = 0.6f;

    public static final Runnable beginAction = () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.CONSTANT_ALPHA, GlStateManager.DstFactor.ONE_MINUS_CONSTANT_ALPHA);
        //TODO figure out RenderSystem.blendColor(1, 1, 1, 1);
    };

    public static final Runnable endAction = () -> {
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    };

    public static void setAlpha(float alpha) {
        HologramRenderLayer.alpha = alpha;
    }

    private HologramRenderLayer(RenderLayer original) {
        super(String.format("%s_%s_hologram", original.toString(), HoloWeb.MOD_ID), original.getVertexFormat(), original.getDrawMode(), original.getExpectedBufferSize(), original.hasCrumbling(), true, () -> {
            original.startDrawing();
            beginAction.run();
        }, () -> {
            endAction.run();
            original.endDrawing();
        });
    }

    public static RenderLayer remap(RenderLayer in) {
        if (in instanceof HologramRenderLayer) {
            return in;
        } else {
            return remappedTypes.computeIfAbsent(in, HologramRenderLayer::new);
        }
    }

    public static VertexConsumerProvider.Immediate initBuffers(VertexConsumerProvider.Immediate original) {
        Map<RenderLayer, BufferBuilder> layerBuffers = ((VertexConsumerProviderImmediateAccessor) original).getLayerBuffers(); //TODO see about the VertexConsumerProviderImmediateAccessor cast!
        Map<RenderLayer, BufferBuilder> remapped = new Object2ObjectLinkedOpenHashMap<>();
        for (Map.Entry<RenderLayer, BufferBuilder> e : layerBuffers.entrySet()) {
            remapped.put(HologramRenderLayer.remap(e.getKey()), new BufferBuilder(e.getKey().getExpectedBufferSize()));
        }
        return new HologramVertexConsumerProvider(new BufferBuilder(256), remapped);
    }

    public static class HologramVertexConsumerProvider extends VertexConsumerProvider.Immediate {

        protected HologramVertexConsumerProvider(BufferBuilder fallback, Map<RenderLayer, BufferBuilder> layerBuffers) {
            super(fallback, layerBuffers);
        }

        @Override
        public VertexConsumer getBuffer(RenderLayer type) {

            type = HologramRenderLayer.remap(type);

            Optional<RenderLayer> optional = type.asOptional();
            BufferBuilder bufferBuilder = this.layerBuffers.getOrDefault(type, this.fallbackBuffer);

            if (!Objects.equals(this.currentLayer, optional)) {
                if (this.currentLayer.isPresent()) {
                    RenderLayer renderLayer2 = this.currentLayer.get();
                    if (!this.layerBuffers.containsKey(renderLayer2)) {
                        this.draw(renderLayer2);
                    }
                }

                if (this.activeConsumers.add(bufferBuilder)) {
                    if (!bufferBuilder.isBuilding()) {
                        bufferBuilder.begin(type.getDrawMode(), type.getVertexFormat());
                    }
                }

                this.currentLayer = optional;
            }

            return bufferBuilder;
        }
    }
}
