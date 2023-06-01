package com.gizmo.doiamengineering.data;

import com.gizmo.doiamengineering.DoIAmEngineering;
import com.gizmo.doiamengineering.loot.AddShaderToLootModifier;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;
import twilightforest.TwilightForestMod;
import twilightforest.loot.TFLootTables;

public class LootModifierGenerator extends GlobalLootModifierProvider {
	public LootModifierGenerator(PackOutput output) {
		super(output, DoIAmEngineering.MODID);
	}

	@Override
	protected void start() {
		add("naga_shader", new AddShaderToLootModifier(new LootItemCondition[]{LootTableIdCondition.builder(TwilightForestMod.prefix("entities/naga")).build()}, DoIAmEngineering.prefix("naga"), true));
		add("lich_shader", new AddShaderToLootModifier(new LootItemCondition[]{LootTableIdCondition.builder(TwilightForestMod.prefix("entities/lich")).build()}, DoIAmEngineering.prefix("lich"), true));
		add("minoshroom_shader", new AddShaderToLootModifier(new LootItemCondition[]{LootTableIdCondition.builder(TwilightForestMod.prefix("entities/minoshroom")).build()}, DoIAmEngineering.prefix("minoshroom"), true));
		add("hydra_shader", new AddShaderToLootModifier(new LootItemCondition[]{LootTableIdCondition.builder(TwilightForestMod.prefix("entities/hydra")).build()}, DoIAmEngineering.prefix("hydra"), true));
		add("knight_phantom_shader", new AddShaderToLootModifier(new LootItemCondition[]{LootTableIdCondition.builder(TFLootTables.STRONGHOLD_BOSS.lootTable).build()}, DoIAmEngineering.prefix("knight_phantom"), true));
		add("ur_ghast_shader", new AddShaderToLootModifier(new LootItemCondition[]{LootTableIdCondition.builder(TwilightForestMod.prefix("entities/ur_ghast")).build()}, DoIAmEngineering.prefix("ur-ghast"), true));
		add("alpha_yeti_shader", new AddShaderToLootModifier(new LootItemCondition[]{LootTableIdCondition.builder(TwilightForestMod.prefix("entities/alpha_yeti")).build()}, DoIAmEngineering.prefix("alpha_yeti"), true));
		add("snow_queen_shader", new AddShaderToLootModifier(new LootItemCondition[]{LootTableIdCondition.builder(TwilightForestMod.prefix("entities/snow_queen")).build()}, DoIAmEngineering.prefix("snow_queen"), true));
		add("quest_ram_shader", new AddShaderToLootModifier(new LootItemCondition[]{LootTableIdCondition.builder(TFLootTables.QUESTING_RAM_REWARDS).build()}, DoIAmEngineering.prefix("quest_ram"), true));
	}
}
