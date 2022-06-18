package com.mystic.holoweb.client.updates;

import com.mystic.holoweb.client.HoloScreenRoot;
import com.mystic.holoweb.util.Vector2i;
import net.montoyo.mcef.utilities.Log;

import javax.annotation.Nullable;

import static com.mystic.holoweb.client.HoloScreenRoot.Screen.*;

public class CMessageScreenUpdate implements Runnable {

    public static HoloScreenRoot.Screen setScreenURL(String url) {
        HoloScreenRoot.Screen ret = new HoloScreenRoot.Screen().setScreenURL(url);

        return ret;
    }

    public static HoloScreenRoot.Screen setResolution(Vector2i res) {
        HoloScreenRoot.Screen ret = new HoloScreenRoot.Screen().setResolution(res);
        ret.action = UPDATE_RESOLUTION;
        ret.vec2i = res;

        return ret;
    }

    public static HoloScreenRoot.Screen handleMouseEvent(int mouseEvent, @Nullable Vector2i pos) {
        HoloScreenRoot.Screen ret = new HoloScreenRoot.Screen().handleMouseEvent(mouseEvent, pos);
        ret.action = UPDATE_MOUSE;
        ret.mouseEvent = mouseEvent;
        ret.vec2i = pos;

        return ret;
    }

    public static HoloScreenRoot.Screen setAutoVolume(boolean av) {
        HoloScreenRoot.Screen ret = new HoloScreenRoot.Screen().setAutoVolume(av);
        ret.action = UPDATE_AUTO_VOL;
        ret.setAutoVolume(av);

        return ret;
    }

    @Override
    public void run() {

        HoloScreenRoot.Screen tes = new HoloScreenRoot.Screen();

        if(tes.action == UPDATE_URL)
            setScreenURL(tes.string);
        else if(tes.action == UPDATE_MOUSE)
            handleMouseEvent(tes.mouseEvent, tes.vec2i);
        else if(tes.action == UPDATE_RESOLUTION)
            setResolution(tes.vec2i);

            if(tes.action == UPDATE_AUTO_VOL)
                setAutoVolume(tes.autoVolume);
        else
            Log.warning("Caught invalid HoloScreenRoot.Screen with action ID %d", tes.action);
    }
}