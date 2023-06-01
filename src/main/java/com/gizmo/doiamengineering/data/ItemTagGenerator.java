package com.gizmo.doiamengineering.data;

import blusunrize.immersiveengineering.api.IETags;
import com.gizmo.doiamengineering.DoIAmEngineering;
import com.gizmo.doiamengineering.ModRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFItems;

import java.util.concurrent.CompletableFuture;

public class ItemTagGenerator extends ItemTagsProvider {

	public static final TagKey<Item> FIERY_DUSTS = ItemTags.create(new ResourceLocation("forge", "dusts/fiery"));
	public static final TagKey<Item> IRONWOOD_DUSTS = ItemTags.create(new ResourceLocation("forge", "dusts/ironwood"));
	public static final TagKey<Item> KNIGHTMETAL_DUSTS = ItemTags.create(new ResourceLocation("forge", "dusts/knightmetal"));

	public static final TagKey<Item> FIERY_NUGGETS = ItemTags.create(new ResourceLocation("forge", "nuggets/fiery"));
	public static final TagKey<Item> IRONWOOD_NUGGETS = ItemTags.create(new ResourceLocation("forge", "nuggets/ironwood"));
	public static final TagKey<Item> KNIGHTMETAL_NUGGETS = ItemTags.create(new ResourceLocation("forge", "nuggets/knightmetal"));

	public static final TagKey<Item> FIERY_PLATES = ItemTags.create(new ResourceLocation("forge", "plates/fiery"));
	public static final TagKey<Item> IRONWOOD_PLATES = ItemTags.create(new ResourceLocation("forge", "plates/ironwood"));
	public static final TagKey<Item> KNIGHTMETAL_PLATES = ItemTags.create(new ResourceLocation("forge", "plates/knightmetal"));

	public static final TagKey<Item> FIERY_RODS = ItemTags.create(new ResourceLocation("forge", "rods/fiery"));
	public static final TagKey<Item> IRONWOOD_RODS = ItemTags.create(new ResourceLocation("forge", "rods/ironwood"));
	public static final TagKey<Item> KNIGHTMETAL_RODS = ItemTags.create(new ResourceLocation("forge", "rods/knightmetal"));

	public static final TagKey<Item> FIERY_WIRES = ItemTags.create(new ResourceLocation("forge", "wires/fiery"));
	public static final TagKey<Item> IRONWOOD_WIRES = ItemTags.create(new ResourceLocation("forge", "wires/ironwood"));
	public static final TagKey<Item> KNIGHTMETAL_WIRES = ItemTags.create(new ResourceLocation("forge", "wires/knightmetal"));

	public ItemTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> future, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper helper) {
		super(output, future, blockTags, DoIAmEngineering.MODID, helper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		tag(FIERY_DUSTS).add(ModRegistry.FIERY_DUST.get());
		tag(IRONWOOD_DUSTS).add(ModRegistry.IRONWOOD_DUST.get());
		tag(KNIGHTMETAL_DUSTS).add(ModRegistry.KNIGHTMETAL_DUST.get());
		tag(Tags.Items.DUSTS).addTags(FIERY_DUSTS, IRONWOOD_DUSTS, KNIGHTMETAL_DUSTS);

		tag(FIERY_NUGGETS).add(ModRegistry.FIERY_NUGGET.get());
		tag(IRONWOOD_NUGGETS).add(ModRegistry.IRONWOOD_NUGGET.get());
		tag(KNIGHTMETAL_NUGGETS).add(TFItems.ARMOR_SHARD.get());
		tag(Tags.Items.NUGGETS).addTags(FIERY_NUGGETS, IRONWOOD_NUGGETS, KNIGHTMETAL_NUGGETS);

		tag(FIERY_RODS).add(ModRegistry.FIERY_ROD.get());
		tag(IRONWOOD_RODS).add(ModRegistry.IRONWOOD_ROD.get());
		tag(KNIGHTMETAL_RODS).add(ModRegistry.KNIGHTMETAL_ROD.get());
		tag(IETags.metalRods).addTags(FIERY_RODS, IRONWOOD_RODS, KNIGHTMETAL_RODS);

		tag(FIERY_PLATES).add(ModRegistry.FIERY_PLATE.get());
		tag(IRONWOOD_PLATES).add(ModRegistry.IRONWOOD_PLATE.get());
		tag(KNIGHTMETAL_PLATES).add(ModRegistry.KNIGHTMETAL_PLATE.get());
		tag(IETags.plates).addTags(FIERY_PLATES, IRONWOOD_PLATES, KNIGHTMETAL_PLATES);

		tag(FIERY_WIRES).add(ModRegistry.FIERY_WIRE.get());
		tag(IRONWOOD_WIRES).add(ModRegistry.IRONWOOD_WIRE.get());
		tag(KNIGHTMETAL_WIRES).add(ModRegistry.KNIGHTMETAL_WIRE.get());
		tag(IETags.allWires).addTags(FIERY_WIRES, IRONWOOD_WIRES, KNIGHTMETAL_WIRES);
	}
}
