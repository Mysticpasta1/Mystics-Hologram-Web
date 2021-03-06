package com.mystic.holoweb.mixin;

import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(VertexConsumerProvider.Immediate.class)
public interface VertexConsumerProviderImmediateAccessor {

    @Accessor
    Map<RenderLayer, BufferBuilder> getLayerBuffers();

}
