package com.gizmo.doiamengineering;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.shader.ShaderRegistry;
import blusunrize.immersiveengineering.api.tool.RailgunHandler;
import blusunrize.immersiveengineering.api.wires.WireApi;
import blusunrize.immersiveengineering.api.wires.WireType;
import blusunrize.immersiveengineering.api.wires.localhandlers.LocalNetworkHandler;
import blusunrize.immersiveengineering.common.register.IEBlocks;
import com.gizmo.doiamengineering.entity.CicadaShot;
import com.gizmo.doiamengineering.item.TFShaderBagItem;
import com.gizmo.doiamengineering.util.wires.FieryWireType;
import com.gizmo.doiamengineering.util.wires.IronwoodWireType;
import com.gizmo.doiamengineering.util.wires.KnightmetalWireType;
import com.gizmo.doiamengineering.util.wires.TFWireDamageHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.NewRegistryEvent;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;

import java.util.Locale;
import java.util.UUID;

@Mod(DoIAmEngineering.MODID)
public class DoIAmEngineering {
	public static final String MODID = "doiamengineering";

	public DoIAmEngineering() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

		bus.addListener(this::commonSetup);
		bus.addListener(EventPriority.HIGHEST, this::hackyShaderBagRegistry);
		ShaderRegistry.rarityWeightMap.put(TwilightForestMod.getRarity(), 1);

		ModRegistry.ENTITIES.register(bus);
		ModRegistry.ITEMS.register(bus);
		ModRegistry.LOOT_MODIFIERS.register(bus);
		ModRegistry.SOUNDS.register(bus);

		MinecraftForge.EVENT_BUS.register(this);
	}

	// why do we need this, you may wonder?
	// apparently the rarityWeightMap isn't populated when we need it to be during item registry, so we delay registering the items until its filled.
	// I COULD just use each rarity value, but that will register shader bags for other mods that have a rarity. If they dont have IE compat, who knows what that will do.
	private void hackyShaderBagRegistry(NewRegistryEvent event) {
		for (Rarity rarity : ShaderRegistry.rarityWeightMap.keySet()) {
			ModRegistry.SHADER_BAGS.put(rarity, ModRegistry.ITEMS.register("shader_bag_" + rarity.name().toLowerCase(Locale.ROOT).replace(':', '_'), () -> new TFShaderBagItem(rarity, new Item.Properties().tab(TFItems.creativeTab))));
		}
	}

	private void commonSetup(FMLCommonSetupEvent event) {
		// Yeah, it's a thing! https://twitter.com/AtomicBlom/status/1004931868012056583
		RailgunHandler.registerProjectile(() -> Ingredient.of(TFBlocks.CICADA.get().asItem()), new RailgunHandler.IRailgunProjectile() {
			@Override
			public Entity getProjectile(@Nullable Player shooter, ItemStack ammo, Entity projectile) {
				Vec3 look = shooter.getLookAngle();
				//we zoomin
				shooter.getLevel().playSound(null, shooter.blockPosition(), ModRegistry.CICADA_FLYING.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
				//FallingBlockEntity doesnt like cicadas, so custom entity it is
				return new CicadaShot(shooter.getLevel(), shooter, look.x() * 20.0D, look.y() * 20.0D, look.z() * 20.0D);
			}

			@Override
			public boolean isValidForTurret() {
				return false;
			}
		});

		RailgunHandler.registerProjectile(() -> Ingredient.of(ModRegistry.FIERY_ROD.get()), new RailgunHandler.StandardRailgunProjectile(10.0D, 1.25D) {
			@Override
			public void onHitTarget(Level level, HitResult target, @Nullable UUID shooter, Entity projectile) {
				if (target instanceof EntityHitResult result) {
					result.getEntity().setSecondsOnFire(10);
				}
			}
		}.setColorMap(new RailgunHandler.RailgunRenderColors(0x3C2323, 0x3C2323, 0x3C2323, 0x191313, 0x080606, 0x080606)));

		RailgunHandler.registerStandardProjectile(ModRegistry.IRONWOOD_ROD.get().getDefaultInstance(), 16.0D, 1.25D).setColorMap(
				new RailgunHandler.RailgunRenderColors(0x887C7C, 0x8A8E3B, 0x5E574B, 0x83764A, 0x5F4D40, 0x5F4D40)
		);

		RailgunHandler.registerStandardProjectile(ModRegistry.KNIGHTMETAL_ROD.get().getDefaultInstance(), 32.0D, 1.25D).setColorMap(
				new RailgunHandler.RailgunRenderColors(0xE7FCCD, 0xE7FCCD, 0xE7FCCD, 0x6A735E, 0x6A735E, 0x6A735E)
		);
		WireApi.registerFeedthroughForWiretype(FIERY, new ResourceLocation(Lib.MODID, "block/connector/connector_hv"),
				new double[]{0.0D, 4.0D, 8.0D, 12.0D}, 0.75D, IEBlocks.Connectors.getEnergyConnector(WireType.HV_CATEGORY, false).defaultBlockState());

		WireApi.registerFeedthroughForWiretype(IRONWOOD, new ResourceLocation(Lib.MODID, "block/connector/connector_lv"),
				new double[]{0.0D, 4.0D, 8.0D, 12.0D}, 0.5D, IEBlocks.Connectors.getEnergyConnector(WireType.LV_CATEGORY, false).defaultBlockState());

		WireApi.registerFeedthroughForWiretype(KNIGHTMETAL, new ResourceLocation(Lib.MODID, "block/connector/connector_mv"),
				new double[]{0.0D, 4.0D, 8.0D, 12.0D}, 0.5625D, IEBlocks.Connectors.getEnergyConnector(WireType.MV_CATEGORY, false).defaultBlockState());

		LocalNetworkHandler.register(TFWireDamageHandler.ID, TFWireDamageHandler::new);

		IEShaderRegistry.initShaders();
	}

	public static ResourceLocation prefix(String name) {
		return new ResourceLocation(MODID, name.toLowerCase(Locale.ROOT));
	}
}
