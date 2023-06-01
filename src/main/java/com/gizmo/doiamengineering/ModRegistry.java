package com.gizmo.doiamengineering;

import blusunrize.immersiveengineering.common.items.WireCoilItem;
import com.gizmo.doiamengineering.entity.CicadaShot;
import com.gizmo.doiamengineering.item.TFShaderItem;
import com.gizmo.doiamengineering.item.TFWireCoilItem;
import com.gizmo.doiamengineering.loot.AddShaderToLootModifier;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import twilightforest.init.TFItems;

import java.util.HashMap;
import java.util.Map;

//not much to register, why not just do it all in 1 class?
public class ModRegistry {

	public static final Map<Rarity, RegistryObject<Item>> SHADER_BAGS = new HashMap<>();

	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, DoIAmEngineering.MODID);
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DoIAmEngineering.MODID);
	public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, DoIAmEngineering.MODID);
	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, DoIAmEngineering.MODID);

	public static final RegistryObject<EntityType<CicadaShot>> CICADA_SHOT = ENTITIES.register("cicada_shot", () -> EntityType.Builder.<CicadaShot>of(CicadaShot::new, MobCategory.MISC).sized(0.25F, 0.25F).setTrackingRange(150).setUpdateInterval(3).fireImmune().build("cicada_shot"));


	public static final RegistryObject<Item> FIERY_WIRE_COIL = ITEMS.register("fiery_wire_coil", () -> new TFWireCoilItem(DoIAmEngineering.FIERY));
	public static final RegistryObject<Item> IRONWOOD_WIRE_COIL = ITEMS.register("ironwood_wire_coil", () -> new TFWireCoilItem(DoIAmEngineering.IRONWOOD));
	public static final RegistryObject<Item> KNIGHTMETAL_WIRE_COIL = ITEMS.register("knightmetal_wire_coil", () -> new TFWireCoilItem(DoIAmEngineering.KNIGHTMETAL));

	public static final RegistryObject<Item> FIERY_DUST = ITEMS.register("fiery_dust", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> FIERY_NUGGET = ITEMS.register("fiery_nugget", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> FIERY_PLATE = ITEMS.register("fiery_plate", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> FIERY_ROD = ITEMS.register("fiery_rod", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> FIERY_WIRE = ITEMS.register("fiery_wire", () -> new Item(new Item.Properties()));

	public static final RegistryObject<Item> IRONWOOD_DUST = ITEMS.register("ironwood_dust", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> IRONWOOD_NUGGET = ITEMS.register("ironwood_nugget", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> IRONWOOD_PLATE = ITEMS.register("ironwood_plate", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> IRONWOOD_ROD = ITEMS.register("ironwood_rod", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> IRONWOOD_WIRE = ITEMS.register("ironwood_wire", () -> new Item(new Item.Properties()));

	public static final RegistryObject<Item> KNIGHTMETAL_DUST = ITEMS.register("knightmetal_dust", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> KNIGHTMETAL_PLATE = ITEMS.register("knightmetal_plate", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> KNIGHTMETAL_ROD = ITEMS.register("knightmetal_rod", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> KNIGHTMETAL_WIRE = ITEMS.register("knightmetal_wire", () -> new Item(new Item.Properties()));

	public static final RegistryObject<Item> SHADER = ITEMS.register("shader", () -> new TFShaderItem(new Item.Properties().stacksTo(1)));

	public static final RegistryObject<Codec<AddShaderToLootModifier>> ADD_SHADER = LOOT_MODIFIERS.register("add_shader", () -> AddShaderToLootModifier.CODEC);

	public static final RegistryObject<SoundEvent> CICADA_FLYING = SOUNDS.register("entity.doiamengineering.cicada_flying", () -> SoundEvent.createVariableRangeEvent(DoIAmEngineering.prefix("entity.doiamengineering.cicada_flying")));

	public static final ResourceKey<DamageType> CICADA = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(DoIAmEngineering.MODID, "cicada"));

	public static void bootstrap(BootstapContext<DamageType> context) {
		context.register(CICADA, new DamageType("doiamengineering.cicada", 0.0F));
	}

}
