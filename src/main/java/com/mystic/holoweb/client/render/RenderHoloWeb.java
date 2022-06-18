package com.mystic.holoweb.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mystic.holoweb.HoloWeb;
import com.mystic.holoweb.client.HoloScreenRoot;
import com.mystic.holoweb.client.setup.ClientSetup;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.glDisable;

public class RenderHoloWeb extends AbstractRenderWeb {
    public static final Identifier ID = new Identifier(HoloWeb.MOD_ID, "web");

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider.Immediate immediate, float tickDelta, int light, int overlay, BlockEntity be) {

        matrices.push();
        matrices.scale(0.1f, -0.1f, 0.1f);
        matrices.translate(5, -20, 5);

        PlayerEntity player = MinecraftClient.getInstance().player;
        double x = player.getX() - be.getPos().getX() - 0.5;
        double z = player.getZ() - be.getPos().getZ() - 0.5;
        float rot = (float) MathHelper.atan2(z, x);

        matrices.multiply(Vec3f.POSITIVE_Y.getRadialQuaternion(-rot));
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90));

        matrices.translate(-7.5, 0, 0);


        Identifier identifierTexture = new Identifier(HoloWeb.MOD_ID, "yeet.png");
        TextureRenderLayer textureRenderLayer = new TextureRenderLayer(RenderLayer.getText(identifierTexture));
        VertexFormat vertexFormat = textureRenderLayer.getVertexFormat();
        BufferBuilder bufferBuilder = new BufferBuilder(5);
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, vertexFormat);
        DrawQuad(0.0f, 0.0f, 16.0f, 16.0f, matrices, bufferBuilder);
        bufferBuilder.end();
        RenderSystem.enableDepthTest();
        BufferRenderer.draw(bufferBuilder);
        matrices.pop();
    }

    @Override
    public Identifier getTypeId() {
        return ID;
    }

    public void DrawQuad(float offX, float offY, float width, float height, MatrixStack stack, BufferBuilder buffer) {
        HoloScreenRoot.Screen scr = new HoloScreenRoot.Screen().getScreen();
        scr.load();

        if (scr.doTurnOnAnim) {
            long lt = System.currentTimeMillis() - scr.turnOnTime;
            float ft = ((float) lt) / 100.0f;

            if (ft >= 1.0f) {
                ft = 1.0f;
                scr.doTurnOnAnim = false;
            }
        }

        float sw = ((float) scr.size.x) * 0.5f - 2.f / 16.f;
        float sh = ((float) scr.size.y) * 0.5f - 2.f / 16.f;

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, scr.browser.getTextureID());
        Matrix4f matrix = stack.peek().getPositionMatrix();
        float x2 = offX + width, y2 = offY + height;
        buffer.vertex(matrix, offX, offY, 1.0f).texture(0.0f, 0.0f).next();
        buffer.vertex(matrix, offX, y2, 1.0f).texture(0.0f, 1.0f).next();
        buffer.vertex(matrix, x2, y2, 1.0f).texture(1.0f, 1.0f).next();
        buffer.vertex(matrix, x2, offY, 1.0f).texture(1.0f, 0.0f).next();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        glDisable(GL11.GL_CULL_FACE);
        glDisable(GL11.GL_BLEND);

        GL11.glEnable(GL11.GL_CULL_FACE);
    }
}
