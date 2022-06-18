package com.mystic.holoweb;

import com.mystic.holoweb.client.HoloScreenRoot;
import com.mystic.holoweb.config.HoloConfig;
import com.mystic.holoweb.core.blockentities.HoloScreenBlock;
import com.mystic.holoweb.core.blockentities.HoloScreenBlockEntity;
import com.mystic.holoweb.util.Utils;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class HoloWeb implements ModInitializer {

	public static final Block HOLO_BLOCK;
	public static final BlockItem HOLO_BLOCK_ITEM;

	public static final String MOD_ID = "holoweb";
	public static final Identifier HOLOBLOCK = new Identifier(MOD_ID, "holoblock");
	public static final String BLACKLIST_URL = "mod://holoweb/blacklisted.html";
	public static final BlockEntityType<HoloScreenBlockEntity> HOLO_BLOCK_ENTITY;


	static {
		HOLO_BLOCK = Registry.register(Registry.BLOCK, HOLOBLOCK, new HoloScreenBlock(FabricBlockSettings.of(Material.STONE)));
		HOLO_BLOCK_ITEM = Registry.register(Registry.ITEM, HOLOBLOCK, new BlockItem(HOLO_BLOCK, new Item.Settings().group(ItemGroup.MISC)));
		HOLO_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "holo_block_entity", BlockEntityType.Builder.create(
				HoloScreenBlockEntity::new, HOLO_BLOCK).build(null));
	}

	public static boolean isSiteBlacklisted(String url) {
		try {
			URL url2 = new URL(Utils.addProtocol(url));
			return Arrays.stream(HoloConfig.get().getUrls().toArray()).anyMatch(str -> str.toString().equalsIgnoreCase(url2.getHost()));
		} catch(MalformedURLException ex) {
			return false;
		}
	}

	public static String applyBlacklist(String url) {
		return isSiteBlacklisted(url) ? BLACKLIST_URL : url;
	}

	@Override
	public void onInitialize() {
		AutoConfig.register(HoloConfig.class, GsonConfigSerializer::new);
	}
}
