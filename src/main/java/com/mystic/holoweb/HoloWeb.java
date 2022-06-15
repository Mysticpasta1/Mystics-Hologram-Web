package com.mystic.holoweb;

import com.mystic.holoweb.client.HoloScreenHandler;
import com.mystic.holoweb.core.blockentities.HoloScreenBlock;
import com.mystic.holoweb.core.blockentities.HoloScreenBlockEntity;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class HoloWeb implements ModInitializer {

	public static final Block HOLO_BLOCK;
	public static final BlockItem HOLO_BLOCK_ITEM;
	public static final BlockEntityType<HoloScreenBlockEntity> HOLO_BLOCK_ENTITY;

	public static final String MOD_ID = "holoweb";
	public static final Identifier HOLOBLOCK = new Identifier(MOD_ID, "holoblock");

	public static final ScreenHandlerType<HoloScreenHandler> HOLO_SCREEN_HANDLER;

	static {
		HOLO_BLOCK = Registry.register(Registry.BLOCK, HOLOBLOCK, new HoloScreenBlock(FabricBlockSettings.of(Material.STONE)));
		HOLO_BLOCK_ITEM = Registry.register(Registry.ITEM, HOLOBLOCK, new BlockItem(HOLO_BLOCK, new Item.Settings().group(ItemGroup.MISC)));

		// In 1.17 use FabricBlockEntityTypeBuilder instead of BlockEntityType.Builder
		HOLO_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, HOLOBLOCK, FabricBlockEntityTypeBuilder.create(HoloScreenBlockEntity::new, HOLO_BLOCK).build(null));

		HOLO_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(HOLOBLOCK, HoloScreenHandler::new);
	}
	
	@Override
	public void onInitialize() {

	}
}
