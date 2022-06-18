package com.mystic.holoweb.client;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class HoloScreen extends CottonClientScreen {
    public HoloScreen(GuiDescription description) {
        super(description);
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(keyCode == GLFW.GLFW_KEY_E) {
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
