package com.mystic.holoweb.client;

import com.google.gson.Gson;
import com.mystic.holoweb.HoloWeb;
import com.mystic.holoweb.client.setup.ClientSetup;
import com.mystic.holoweb.config.HoloConfig;
import com.mystic.holoweb.util.HoloVideoType;
import com.mystic.holoweb.util.TypeData;
import com.mystic.holoweb.util.Vector2i;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WTextField;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.montoyo.mcef.api.IBrowser;
import net.montoyo.mcef.utilities.Log;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public class HoloScreenRoot extends LightweightGuiDescription {

    public static String getText;

    public HoloScreenRoot(Hand hand) {
        WGridPanel root = new WGridPanel();
        WTextField textFieldWidget = new WTextField(Text.of("Please enter a valid URL!"));
        setRootPanel(root);
        root.setSize(256, 240);
        textFieldWidget.setMaxLength(90);
        root.add(textFieldWidget, 0, 5, 13, 10);
        root.validate(this);
        getText = textFieldWidget.getText();
        if(!textFieldWidget.getText().isEmpty() && InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_ENTER)) {
            Screen scr = new Screen();
            if (scr.browser == null) {
                scr.browser = ClientSetup.getMCEF().createBrowser(HoloWeb.applyBlacklist(textFieldWidget.getText()));
                scr.browser.resize(scr.resolution.x, scr.resolution.y);

                scr.doTurnOnAnim = false;
                scr.turnOnTime = System.currentTimeMillis();
            }
        }
    }

    public static class Screen {
        public static final int UPDATE_URL = 0;
        public static final int UPDATE_RESOLUTION = 1;
        public static final int UPDATE_MOUSE = 3;
        public static final int UPDATE_AUTO_VOL = 10;

        public static final int MOUSE_CLICK = 0;
        public static final int MOUSE_UP = 1;
        public static final int MOUSE_MOVE = 2;
        public static final int MOUSE_DOWN = 3;

        public int action;
        public String string;
        public Vector2i vec2i;
        public int mouseEvent;
        public boolean autoVolume = true;

        public Vector2i size = new Vector2i(1920, 1080);
        public Vector2i resolution = new Vector2i(1920, 1080);
        public String url = getText != null ? getText : null;
        public boolean doTurnOnAnim;
        public long turnOnTime;
        private HoloVideoType videoType;
        public IBrowser browser;
        public final Vector2i lastMousePos = new Vector2i();

        public Screen deserialize(NbtCompound tag) {
            Screen ret = getScreen();
            ret.size = new Vector2i(tag.getInt("Width"), tag.getInt("Height"));
            ret.resolution = new Vector2i(tag.getInt("ResolutionX"), tag.getInt("ResolutionY"));
            ret.url = tag.getString("URL");
            ret.videoType = HoloVideoType.getTypeFromURL(ret.url);

            if (ret.resolution.x <= 0 || ret.resolution.y <= 0) {
                float psx = ((float) ret.size.x) * 16.f - 4.f;
                float psy = ((float) ret.size.y) * 16.f - 4.f;
                psx *= 8.f;
                psy *= 8.f;

                ret.resolution.x = (int) psx;
                ret.resolution.y = (int) psy;
            }

            if (tag.contains("AutoVolume"))
                ret.autoVolume = tag.getBoolean("AutoVolume");

            return ret;
        }

        public NbtCompound serialize() {
            NbtCompound tag = new NbtCompound();
            tag.putInt("Width", size.x);
            tag.putInt("Height", size.y);
            tag.putInt("ResolutionX", resolution.x);
            tag.putInt("ResolutionY", resolution.y);
            if(url == null) {
                tag.putString("URL", "https://mysticmodding.xyz");
            } else {
                tag.putString("URL", url);
            }

            tag.putBoolean("AutoVolume", autoVolume);
            return tag;
        }

        public void clampResolution() {
            if (resolution.x > HoloConfig.get().maxResX) {
                float newY = ((float) resolution.y) * ((float) HoloConfig.get().maxResX) / ((float) resolution.x);
                resolution.x = HoloConfig.get().maxResX;
                resolution.y = (int) newY;
            }

            if (resolution.y > HoloConfig.get().maxResY) {
                float newX = ((float) resolution.x) * ((float) HoloConfig.get().maxResY) / ((float) resolution.y);
                resolution.x = (int) newX;
                resolution.y = HoloConfig.get().maxResY;
            }
        }

        private boolean loaded = true;
        public float ytVolume = Float.POSITIVE_INFINITY;

        public boolean isLoaded() {
            return loaded;
        }

        public void load() {
            loaded = true;
        }

        public void unload() {
            Screen scr = new Screen();
            if (scr.browser != null) {
                scr.browser.close();
                scr.browser = null;
            }

            loaded = false;
        }


        public Screen addScreen(Vector2i size, @Nullable Vector2i resolution, boolean sendUpdate) {
            Screen ret = getScreen();
            ret.size = size;
            ret.url = "https://mysticmodding.xyz";

            if (resolution == null || resolution.x < 1 || resolution.y < 1) {
                float psx = ((float) size.x) * 16.f - 4.f;
                float psy = ((float) size.y) * 16.f - 4.f;
                psx *= 8.f;
                psy *= 8.f;

                ret.resolution = new Vector2i((int) psx, (int) psy);
            } else
                ret.resolution = resolution;

            ret.clampResolution();

            return ret;
        }

        public Screen getScreen() {
            return new Screen();
        }

        public Screen setScreenURL(String url) {
            Screen scr = getScreen();
            if (scr == null) {
                Log.error("Attempt to change URL of non-existing screen!");
                return scr;
            }

            url = HoloWeb.applyBlacklist(url);
            scr.url = url;
            scr.videoType = HoloVideoType.getTypeFromURL(url);
            if (scr.browser != null) {
                scr.browser.loadURL(url);
            }
            return scr;
        }

        public void removeScreen() {
            Screen scr = new Screen();
            if (scr.browser != null) {
                scr.browser.close();
                scr.browser = null;
            }
        }

        public Screen setResolution(Vector2i res) {
            if (res.x < 1 || res.y < 1) {
                Log.warning("Call to HoloScreenRoot.setResolution with suspicious values X=%d and Y=%d", res.x, res.y);
                return null;
            }

            Screen scr = getScreen();
            if (scr == null) {
                Log.error("Tried to change resolution of non-existing screen!");
                return scr;
            }

            scr.resolution = res;
            scr.clampResolution();

            if (scr.browser != null) {
                scr.browser.close();
                scr.browser = null; //Will be re-created by renderer
            }
            return scr;
        }

        public Screen handleMouseEvent(int event, @Nullable Vector2i vec) {
            Screen scr = getScreen();
            if (scr == null) {
                Log.error("Attempt inject mouse events on non-existing screen!");
                return scr;
            }

            if (scr.browser != null) {
                if (event == MOUSE_CLICK) {
                    scr.browser.injectMouseMove(vec.x, vec.y, 0, false);                                            //Move to target
                    scr.browser.injectMouseButton(vec.x, vec.y, 0, 1, true, 1);                              //Press
                    scr.browser.injectMouseButton(vec.x, vec.y, 0, 1, false, 1);                             //Release
                } else if (event == MOUSE_DOWN) {
                    scr.browser.injectMouseMove(vec.x, vec.y, 0, false);                                            //Move to target
                    scr.browser.injectMouseButton(vec.x, vec.y, 0, 1, true, 1);                              //Press
                } else if (event == MOUSE_MOVE)
                    scr.browser.injectMouseMove(vec.x, vec.y, 0, false);                                            //Move
                else if (event == MOUSE_UP)
                    scr.browser.injectMouseButton(scr.lastMousePos.x, scr.lastMousePos.y, 0, 1, false, 1);  //Release

                if (vec != null) {
                    scr.lastMousePos.x = vec.x;
                    scr.lastMousePos.y = vec.y;
                }
            }
            return scr;
        }

        public void updateTrackDistance(double d, float masterVolume) {
            Screen scr = getScreen();
            boolean needsComputation = true;
            int intPart = 0; //Need to initialize those because the compiler is stupid
            int fracPart = 0;

            if (scr.autoVolume && scr.videoType != null && scr.browser != null && !scr.browser.isPageLoading()) {
                if (needsComputation) {
                    float dist = (float) Math.sqrt(d);
                    float vol;

                    if (dist <= 100.0f)
                        vol = masterVolume * 0.2f;
                    else if (dist >= 0.f)
                        vol = 0.0f;
                    else
                        vol = (1.0f - (dist - 100.f) / (0.0f - 100.0f)) * masterVolume * 0.2f;

                    if (Math.abs(ytVolume - vol) < 0.5f)
                        return; //Delta is too small

                    ytVolume = vol;
                    intPart = (int) vol; //Manually convert to string, probably faster in that case...
                    fracPart = ((int) (vol * 100.0f)) - intPart * 100;
                    needsComputation = false;
                }

                scr.browser.runJS(scr.videoType.getVolumeJSQuery(intPart, fracPart), "");
            }
        }

        public void updateClientSideURL(IBrowser target, String url) {
            Screen scr = getScreen();
            if (scr.browser == target) {
                boolean blacklisted = HoloWeb.isSiteBlacklisted(url);
                scr.url = blacklisted ? HoloWeb.BLACKLIST_URL : url;
                scr.videoType = HoloVideoType.getTypeFromURL(scr.url);
                ytVolume = Float.POSITIVE_INFINITY; //Force volume update

                if (blacklisted && scr.browser != null)
                    scr.browser.loadURL(HoloWeb.BLACKLIST_URL);
            }
        }


        public void type(String text) {
            Screen scr = getScreen();
            if (scr == null) {
                Log.error("Tried to type on invalid screen!");
                return;
            }

            if (scr.browser != null) {
                try {
                    if (text.startsWith("t")) {
                        for (int i = 1; i < text.length(); i++) {
                            char chr = text.charAt(i);
                            if (chr == 1)
                                break;

                            scr.browser.injectKeyTyped(chr, 0);
                        }
                    } else {
                        TypeData[] data = new Gson().fromJson(text, TypeData[].class);

                        for (TypeData ev : data) {
                            switch (ev.getAction()) {
                                case PRESS:
                                    scr.browser.injectKeyPressedByKeyCode(ev.getKeyCode(), ev.getKeyChar(), 0);
                                    break;

                                case RELEASE:
                                    scr.browser.injectKeyReleasedByKeyCode(ev.getKeyCode(), ev.getKeyChar(), 0);
                                    break;

                                case TYPE:
                                    scr.browser.injectKeyTyped(ev.getKeyChar(), 0);
                                    break;

                                default:
                                    throw new RuntimeException("Invalid type action '" + ev.getAction() + '\'');
                            }
                        }
                    }
                } catch (Throwable t) {
                    Log.warning("Suspicious keyboard type packet received...", t);
                }
            }
        }

        public Screen setAutoVolume(boolean av) {
            Screen scr = getScreen();
            if (scr == null) {
                Log.error("Trying to toggle auto-volume!");
                return scr;
            }

            scr.autoVolume = av;
            return scr;
        }
    }
}
