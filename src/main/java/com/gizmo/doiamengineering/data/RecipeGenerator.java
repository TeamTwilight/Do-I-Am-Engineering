package com.gizmo.doiamengineering.data;

import blusunrize.immersiveengineering.api.crafting.builders.ArcFurnaceRecipeBuilder;
import blusunrize.immersiveengineering.api.crafting.builders.CrusherRecipeBuilder;
import blusunrize.immersiveengineering.api.crafting.builders.MetalPressRecipeBuilder;
import blusunrize.immersiveengineering.api.crafting.builders.ThermoelectricSourceBuilder;
import blusunrize.immersiveengineering.common.register.IEItems;
import com.gizmo.doiamengineering.DoIAmEngineering;
import com.gizmo.doiamengineering.ModRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.crafting.Ingredient;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;

import java.util.function.Consumer;

public class RecipeGenerator extends RecipeProvider {
	public RecipeGenerator(DataGenerator generator) {
		super(generator);
	}

	@Override
	protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {

		ThermoelectricSourceBuilder.builder(TFBlocks.FIERY_BLOCK.get())
				.kelvin(2500)
				.build(consumer, DoIAmEngineering.prefix("thermoelectric/fiery_block"));

		ShapelessRecipeBuilder.shapeless(TFItems.FIERY_INGOT.get())
				.requires(Ingredient.of(ModRegistry.FIERY_NUGGET.get()), 9)
				.unlockedBy("has_nugget", has(ModRegistry.FIERY_NUGGET.get()))
				.save(consumer, DoIAmEngineering.prefix("fiery_ingot_from_nuggets"));

		ShapelessRecipeBuilder.shapeless(ModRegistry.FIERY_NUGGET.get(), 9)
				.requires(Ingredient.of(TFItems.FIERY_INGOT.get()))
				.unlockedBy("has_ingot", has(TFItems.FIERY_INGOT.get()))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(TFItems.IRONWOOD_INGOT.get())
				.requires(Ingredient.of(ModRegistry.IRONWOOD_NUGGET.get()), 9)
				.unlockedBy("has_nugget", has(ModRegistry.IRONWOOD_NUGGET.get()))
				.save(consumer, DoIAmEngineering.prefix("ironwood_ingot_from_nuggets"));

		ShapelessRecipeBuilder.shapeless(ModRegistry.IRONWOOD_NUGGET.get(), 9)
				.requires(Ingredient.of(TFItems.IRONWOOD_INGOT.get()))
				.unlockedBy("has_ingot", has(TFItems.IRONWOOD_INGOT.get()))
				.save(consumer);

		ArcFurnaceRecipeBuilder.builder(TFItems.IRONWOOD_INGOT.get())
				.addSecondary(twilightforest.data.tags.ItemTagGenerator.IRONWOOD_INGOTS, 0.5F)
				.addIngredient("input", TFItems.RAW_IRONWOOD.get())
				.setTime(200)
				.setEnergy(102400)
				.build(consumer, DoIAmEngineering.prefix("arcfurnace/ore_ironwood"));

		ArcFurnaceRecipeBuilder.builder(TFItems.KNIGHTMETAL_INGOT.get())
				.addSecondary(twilightforest.data.tags.ItemTagGenerator.KNIGHTMETAL_INGOTS, 0.5F)
				.addIngredient("input", TFItems.ARMOR_SHARD_CLUSTER.get())
				.setTime(200)
				.setEnergy(102400)
				.build(consumer, DoIAmEngineering.prefix("arcfurnace/ore_knightmetal"));

		CrusherRecipeBuilder.builder(ItemTagGenerator.FIERY_DUSTS, 1)
				.addInput(TFItems.FIERY_INGOT.get())
				.setEnergy(3000)
				.build(consumer, DoIAmEngineering.prefix("crusher/ingot_fiery"));

		ArcFurnaceRecipeBuilder.builder(TFItems.FIERY_INGOT.get())
				.addIngredient("input", ModRegistry.FIERY_DUST.get())
				.setTime(100)
				.setEnergy(51200)
				.build(consumer, DoIAmEngineering.prefix("arcfurnace/dust_fiery"));

		MetalPressRecipeBuilder.builder(IEItems.Molds.MOLD_PLATE, ItemTagGenerator.FIERY_PLATES, 1)
				.addInput(TFItems.FIERY_INGOT.get())
				.setEnergy(2400)
				.build(consumer, DoIAmEngineering.prefix("metalpress/plate_fiery"));

		MetalPressRecipeBuilder.builder(IEItems.Molds.MOLD_ROD, ItemTagGenerator.FIERY_RODS, 2)
				.addInput(TFItems.FIERY_INGOT.get())
				.setEnergy(2400)
				.build(consumer, DoIAmEngineering.prefix("metalpress/rods_fiery"));

		MetalPressRecipeBuilder.builder(IEItems.Molds.MOLD_WIRE, ItemTagGenerator.FIERY_WIRES, 2)
				.addInput(TFItems.FIERY_INGOT.get())
				.setEnergy(2400)
				.build(consumer, DoIAmEngineering.prefix("metalpress/wires_fiery"));

		ShapelessRecipeBuilder.shapeless(ModRegistry.FIERY_WIRE.get())
				.requires(ItemTagGenerator.FIERY_PLATES)
				.requires(IEItems.Tools.WIRECUTTER)
				.unlockedBy("has_fiery_ingot", has(TFItems.FIERY_INGOT.get()))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(ModRegistry.FIERY_PLATE.get())
				.requires(TFItems.FIERY_INGOT.get())
				.requires(IEItems.Tools.HAMMER)
				.unlockedBy("has_fiery_ingot", has(TFItems.FIERY_INGOT.get()))
				.save(consumer, DoIAmEngineering.prefix("plate_fiery_hammering"));

		CrusherRecipeBuilder.builder(ItemTagGenerator.KNIGHTMETAL_DUSTS, 1)
				.addInput(TFItems.KNIGHTMETAL_INGOT.get())
				.setEnergy(3000)
				.build(consumer, DoIAmEngineering.prefix("crusher/ingot_knightmetal"));

		ArcFurnaceRecipeBuilder.builder(TFItems.KNIGHTMETAL_INGOT.get())
				.addIngredient("input", ModRegistry.KNIGHTMETAL_DUST.get())
				.setTime(100)
				.setEnergy(51200)
				.build(consumer, DoIAmEngineering.prefix("arcfurnace/dust_knightmetal"));

		MetalPressRecipeBuilder.builder(IEItems.Molds.MOLD_PLATE, ItemTagGenerator.KNIGHTMETAL_PLATES, 1)
				.addInput(TFItems.KNIGHTMETAL_INGOT.get())
				.setEnergy(2400)
				.build(consumer, DoIAmEngineering.prefix("metalpress/plate_knightmetal"));

		MetalPressRecipeBuilder.builder(IEItems.Molds.MOLD_ROD, ItemTagGenerator.KNIGHTMETAL_RODS, 2)
				.addInput(TFItems.KNIGHTMETAL_INGOT.get())
				.setEnergy(2400)
				.build(consumer, DoIAmEngineering.prefix("metalpress/rods_knightmetal"));

		MetalPressRecipeBuilder.builder(IEItems.Molds.MOLD_WIRE, ItemTagGenerator.KNIGHTMETAL_WIRES, 2)
				.addInput(TFItems.KNIGHTMETAL_INGOT.get())
				.setEnergy(2400)
				.build(consumer, DoIAmEngineering.prefix("metalpress/wires_knightmetal"));

		ShapelessRecipeBuilder.shapeless(ModRegistry.KNIGHTMETAL_WIRE.get())
				.requires(ItemTagGenerator.KNIGHTMETAL_PLATES)
				.requires(IEItems.Tools.WIRECUTTER)
				.unlockedBy("has_knightmetal_ingot", has(TFItems.KNIGHTMETAL_INGOT.get()))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(ModRegistry.KNIGHTMETAL_PLATE.get())
				.requires(TFItems.KNIGHTMETAL_INGOT.get())
				.requires(IEItems.Tools.HAMMER)
				.unlockedBy("has_knightmetal_ingot", has(TFItems.KNIGHTMETAL_INGOT.get()))
				.save(consumer, DoIAmEngineering.prefix("plate_knightmetal_hammering"));

		CrusherRecipeBuilder.builder(ItemTagGenerator.IRONWOOD_DUSTS, 1)
				.addInput(TFItems.IRONWOOD_INGOT.get())
				.setEnergy(3000)
				.build(consumer, DoIAmEngineering.prefix("crusher/ingot_ironwood"));

		ArcFurnaceRecipeBuilder.builder(TFItems.IRONWOOD_INGOT.get())
				.addIngredient("input", ModRegistry.IRONWOOD_DUST.get())
				.setTime(100)
				.setEnergy(51200)
				.build(consumer, DoIAmEngineering.prefix("arcfurnace/dust_ironwood"));

		MetalPressRecipeBuilder.builder(IEItems.Molds.MOLD_PLATE, ItemTagGenerator.IRONWOOD_PLATES, 1)
				.addInput(TFItems.IRONWOOD_INGOT.get())
				.setEnergy(2400)
				.build(consumer, DoIAmEngineering.prefix("metalpress/plate_ironwood"));

		MetalPressRecipeBuilder.builder(IEItems.Molds.MOLD_ROD, ItemTagGenerator.IRONWOOD_RODS, 2)
				.addInput(TFItems.IRONWOOD_INGOT.get())
				.setEnergy(2400)
				.build(consumer, DoIAmEngineering.prefix("metalpress/rods_ironwood"));

		MetalPressRecipeBuilder.builder(IEItems.Molds.MOLD_WIRE, ItemTagGenerator.IRONWOOD_WIRES, 2)
				.addInput(TFItems.IRONWOOD_INGOT.get())
				.setEnergy(2400)
				.build(consumer, DoIAmEngineering.prefix("metalpress/wires_ironwood"));

		ShapelessRecipeBuilder.shapeless(ModRegistry.IRONWOOD_WIRE.get())
				.requires(ItemTagGenerator.IRONWOOD_PLATES)
				.requires(IEItems.Tools.WIRECUTTER)
				.unlockedBy("has_ironwood_ingot", has(TFItems.IRONWOOD_INGOT.get()))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(ModRegistry.IRONWOOD_PLATE.get())
				.requires(TFItems.IRONWOOD_INGOT.get())
				.requires(IEItems.Tools.HAMMER)
				.unlockedBy("has_ironwood_ingot", has(TFItems.IRONWOOD_INGOT.get()))
				.save(consumer, DoIAmEngineering.prefix("plate_ironwood_hammering"));


		SimpleCookingRecipeBuilder.smelting(Ingredient.of(
						TFItems.FIERY_HELMET.get(), TFItems.FIERY_CHESTPLATE.get(), TFItems.FIERY_LEGGINGS.get(), TFItems.FIERY_BOOTS.get(),
						TFItems.FIERY_SWORD.get(), TFItems.FIERY_PICKAXE.get()), ModRegistry.FIERY_NUGGET.get(), 0.1F, 200)
				.unlockedBy("has_helmet", has(TFItems.FIERY_HELMET.get())).unlockedBy("has_chestplate", has(TFItems.FIERY_CHESTPLATE.get()))
				.unlockedBy("has_leggings", has(TFItems.FIERY_LEGGINGS.get())).unlockedBy("has_boots", has(TFItems.FIERY_BOOTS.get()))
				.unlockedBy("has_sword", has(TFItems.FIERY_SWORD.get())).unlockedBy("has_pickaxe", has(TFItems.FIERY_PICKAXE.get()))
				.save(consumer, DoIAmEngineering.prefix("fiery_nugget_from_smelting"));

		SimpleCookingRecipeBuilder.blasting(Ingredient.of(
						TFItems.FIERY_HELMET.get(), TFItems.FIERY_CHESTPLATE.get(), TFItems.FIERY_LEGGINGS.get(), TFItems.FIERY_BOOTS.get(),
						TFItems.FIERY_SWORD.get(), TFItems.FIERY_PICKAXE.get()), ModRegistry.FIERY_NUGGET.get(), 0.1F, 100)
				.unlockedBy("has_helmet", has(TFItems.FIERY_HELMET.get())).unlockedBy("has_chestplate", has(TFItems.FIERY_CHESTPLATE.get()))
				.unlockedBy("has_leggings", has(TFItems.FIERY_LEGGINGS.get())).unlockedBy("has_boots", has(TFItems.FIERY_BOOTS.get()))
				.unlockedBy("has_sword", has(TFItems.FIERY_SWORD.get())).unlockedBy("has_pickaxe", has(TFItems.FIERY_PICKAXE.get()))
				.save(consumer, DoIAmEngineering.prefix("fiery_nugget_from_blasting"));

		SimpleCookingRecipeBuilder.smelting(Ingredient.of(
								TFItems.IRONWOOD_HELMET.get(), TFItems.IRONWOOD_CHESTPLATE.get(), TFItems.IRONWOOD_LEGGINGS.get(), TFItems.IRONWOOD_BOOTS.get(),
								TFItems.IRONWOOD_SWORD.get(), TFItems.IRONWOOD_PICKAXE.get(), TFItems.IRONWOOD_AXE.get(), TFItems.IRONWOOD_SHOVEL.get(), TFItems.IRONWOOD_HOE.get()),
						ModRegistry.IRONWOOD_NUGGET.get(), 0.1F, 200)
				.unlockedBy("has_helmet", has(TFItems.IRONWOOD_HELMET.get())).unlockedBy("has_chestplate", has(TFItems.IRONWOOD_CHESTPLATE.get()))
				.unlockedBy("has_leggings", has(TFItems.IRONWOOD_LEGGINGS.get())).unlockedBy("has_boots", has(TFItems.IRONWOOD_BOOTS.get()))
				.unlockedBy("has_sword", has(TFItems.IRONWOOD_SWORD.get())).unlockedBy("has_pickaxe", has(TFItems.IRONWOOD_PICKAXE.get()))
				.unlockedBy("has_axe", has(TFItems.IRONWOOD_AXE.get())).unlockedBy("has_shovel", has(TFItems.IRONWOOD_SHOVEL.get()))
				.unlockedBy("has_hoe", has(TFItems.IRONWOOD_HOE.get()))
				.save(consumer, DoIAmEngineering.prefix("ironwood_nugget_from_smelting"));

		SimpleCookingRecipeBuilder.blasting(Ingredient.of(
						TFItems.IRONWOOD_HELMET.get(), TFItems.IRONWOOD_CHESTPLATE.get(), TFItems.IRONWOOD_LEGGINGS.get(), TFItems.IRONWOOD_BOOTS.get(),
						TFItems.IRONWOOD_SWORD.get(), TFItems.IRONWOOD_PICKAXE.get(), TFItems.IRONWOOD_AXE.get(), TFItems.IRONWOOD_SHOVEL.get(), TFItems.IRONWOOD_HOE.get()),
						ModRegistry.IRONWOOD_NUGGET.get(), 0.1F, 100)
				.unlockedBy("has_helmet", has(TFItems.IRONWOOD_HELMET.get())).unlockedBy("has_chestplate", has(TFItems.IRONWOOD_CHESTPLATE.get()))
				.unlockedBy("has_leggings", has(TFItems.IRONWOOD_LEGGINGS.get())).unlockedBy("has_boots", has(TFItems.IRONWOOD_BOOTS.get()))
				.unlockedBy("has_sword", has(TFItems.IRONWOOD_SWORD.get())).unlockedBy("has_pickaxe", has(TFItems.IRONWOOD_PICKAXE.get()))
				.unlockedBy("has_axe", has(TFItems.IRONWOOD_AXE.get())).unlockedBy("has_shovel", has(TFItems.IRONWOOD_SHOVEL.get()))
				.unlockedBy("has_hoe", has(TFItems.IRONWOOD_HOE.get()))
				.save(consumer, DoIAmEngineering.prefix("ironwood_nugget_from_blasting"));
	}
}
