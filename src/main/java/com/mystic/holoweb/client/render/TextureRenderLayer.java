package com.mystic.holoweb.client.render;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mystic.holoweb.HoloWeb;
import com.mystic.holoweb.mixin.VertexConsumerProviderImmediateAccessor;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.client.render.*;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static net.minecraft.client.render.VertexFormats.POSITION_ELEMENT;
import static net.minecraft.client.render.VertexFormats.TEXTURE_0_ELEMENT;

public class TextureRenderLayer extends RenderLayer {

    //TODO refactor this and make it not shit

    private static final Map<RenderLayer, RenderLayer> remappedTypes = new IdentityHashMap<>();

    public static final Runnable beginAction = () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.CONSTANT_ALPHA, GlStateManager.DstFactor.ONE_MINUS_CONSTANT_ALPHA);
    };

    public static final Runnable endAction = () -> {
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    };


    TextureRenderLayer(RenderLayer original) {
        super(String.format("%s_%s_hologram", original.toString(), HoloWeb.MOD_ID), original.getVertexFormat(), original.getDrawMode(), original.getExpectedBufferSize(), original.hasCrumbling(), true, () -> {
            original.startDrawing();
            beginAction.run();
        }, () -> {
            endAction.run();
            original.endDrawing();
        });
    }

    public static RenderLayer remap(RenderLayer in) {
        if (in instanceof TextureRenderLayer) {
            return in;
        } else {
            return remappedTypes.computeIfAbsent(in, TextureRenderLayer::new);
        }
    }

    public static VertexConsumerProvider.Immediate initBuffers(VertexConsumerProvider.Immediate original) {
        Map<RenderLayer, BufferBuilder> layerBuffers = ((VertexConsumerProviderImmediateAccessor) original).getLayerBuffers();
        Map<RenderLayer, BufferBuilder> remapped = new Object2ObjectLinkedOpenHashMap<>();
        for (Map.Entry<RenderLayer, BufferBuilder> e : layerBuffers.entrySet()) {
            remapped.put(TextureRenderLayer.remap(e.getKey()), new BufferBuilder(e.getKey().getExpectedBufferSize()));
        }
        return new HologramVertexConsumerProvider(new BufferBuilder(256), remapped);
    }

    @Override
    public VertexFormat getVertexFormat() {
        return new VertexFormat(ImmutableMap.<String, VertexFormatElement>builder().put("Position", POSITION_ELEMENT).put("Texture", TEXTURE_0_ELEMENT).build());
    }

    public static class HologramVertexConsumerProvider extends VertexConsumerProvider.Immediate {

        protected HologramVertexConsumerProvider(BufferBuilder fallback, Map<RenderLayer, BufferBuilder> layerBuffers) {
            super(fallback, layerBuffers);
        }

        @Override
        public VertexConsumer getBuffer(RenderLayer type) {

            type = TextureRenderLayer.remap(type);

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

