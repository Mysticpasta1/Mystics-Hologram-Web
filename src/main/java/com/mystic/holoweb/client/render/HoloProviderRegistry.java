package com.mystic.holoweb.client.render;

import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.function.Supplier;

public class HoloProviderRegistry {

    private static final HashMap<Identifier, Supplier<AbstractRenderWeb>> REGISTRY = new HashMap<>();

    public static void register(Identifier typeId, Supplier<AbstractRenderWeb> factory) {
        if (REGISTRY.containsKey(typeId)) throw new IllegalStateException("Tried to double-register provider with type id" + typeId + "!");
        REGISTRY.put(typeId, factory);
    }

    public static AbstractRenderWeb getProvider(AbstractRenderWeb previousProvider, Identifier typeId) {
        return previousProvider != null && previousProvider.getTypeId().equals(typeId) ? previousProvider : REGISTRY.get(typeId).get();
    }

}
