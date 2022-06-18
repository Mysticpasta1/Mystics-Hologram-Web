package com.mystic.holoweb.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

@Config(name = "holoconfig")
public class HoloConfig implements ConfigData {
    private ArrayList<URL> blacklist = new ArrayList<>();
    public int maxResX = 1920;
    public int maxResY = 1080;

    public ArrayList<URL> getUrls() throws MalformedURLException {
        blacklist.add(new URL("https://www.rule34.xxx"));
        return blacklist;
    }

    public static HoloConfig get() {
        return AutoConfig.getConfigHolder (HoloConfig.class).getConfig();
    }
}
