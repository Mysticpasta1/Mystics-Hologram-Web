package com.mystic.holoweb.client.render.block;

import com.mystic.holoweb.core.blockentities.HoloScreenBlockEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class HoloWebBlockModel extends AnimatedGeoModel<HoloScreenBlockEntity> {

    //TODO fix the block model, animation, and texture to look better :D

    @Override
    public Identifier getModelLocation(HoloScreenBlockEntity object) {
        return null;
    }

    @Override
    public Identifier getTextureLocation(HoloScreenBlockEntity object) {
        return null;
    }

    @Override
    public Identifier getAnimationFileLocation(HoloScreenBlockEntity animatable) {
        return null;
    }
}
