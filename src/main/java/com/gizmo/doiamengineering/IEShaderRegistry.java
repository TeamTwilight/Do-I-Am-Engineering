package com.gizmo.doiamengineering;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.crafting.IngredientWithSize;
import blusunrize.immersiveengineering.api.shader.DynamicShaderLayer;
import blusunrize.immersiveengineering.api.shader.ShaderLayer;
import blusunrize.immersiveengineering.api.shader.ShaderRegistry;
import blusunrize.immersiveengineering.api.shader.impl.*;
import com.gizmo.doiamengineering.client.*;
import com.gizmo.doiamengineering.util.TFShaderCaseChemthrower;
import com.gizmo.doiamengineering.util.TFShaderCaseDrill;
import com.gizmo.doiamengineering.util.TFShaderCaseRailgun;
import com.gizmo.doiamengineering.util.TFShaderCaseShield;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.Ingredient;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;

import java.util.List;
import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.IntConsumer;

//TODO shader descriptions for the manual (in lang file)
public class IEShaderRegistry {
	// Layer Constants
	private static final ShaderLayer NULL_LAYER = new ShaderLayer(null, 0xFFFFFFFF);
	private static final ShaderLayer UNCOLORED_REVOLVER_LAYER = new ShaderLayer(new ResourceLocation(Lib.MODID, "revolvers/shaders/revolver_uncoloured"), 0xFFFFFFFF);
	private static final ShaderLayer UNCOLORED_CHEMTHROW_LAYER = new ShaderLayer(new ResourceLocation(Lib.MODID, "item/shaders/chemthrower_uncoloured"), 0xFFFFFFFF);
	private static final ShaderLayer UNCOLORED_DRILL_LAYER = new ShaderLayer(new ResourceLocation(Lib.MODID, "item/shaders/drill_diesel_uncoloured"), 0xFFFFFFFF);
	private static final ShaderLayer UNCOLORED_RAILGUN_LAYER = new ShaderLayer(new ResourceLocation(Lib.MODID, "item/shaders/railgun_uncoloured"), 0xFFFFFFFF);
	private static final ShaderLayer UNCOLORED_SHIELD_LAYER = new ShaderLayer(new ResourceLocation(Lib.MODID, "item/shaders/shield_uncoloured"), 0xFFFFFFFF);
	private static final ShaderLayer UNCOLORED_MINECART_LAYER = new ShaderLayer(new ResourceLocation(Lib.MODID, "textures/models/shaders/minecart_uncoloured.png"), 0xFFFFFFFF);
	private static final ShaderLayer UNCOLORED_BALLOON_LAYER = new ShaderLayer(new ResourceLocation(Lib.MODID, "block/shaders/balloon_uncoloured"), 0xFFFFFFFF);
	private static final ShaderLayer UNCOLORED_BANNER_LAYER = new ShaderLayer(new ResourceLocation(Lib.MODID, "block/shaders/banner_uncoloured"), 0xFFFFFFFF);

	//FIXME placeholders to prevent log error spam. Once the event in TFClientEvents is working again, get rid of these and use the bottom ones
	//https://github.com/TeamTwilight/twilightforest/blob/1.18.x/src/main/java/twilightforest/client/TFClientEvents.java#L164
	//https://github.com/TeamTwilight/twilightforest/blob/1.18.x/src/main/disabled/client/texture/GradientMappedTexture.java
	public static final ResourceLocation PROCESSED_REVOLVER_GRIP_LAYER = new ResourceLocation(Lib.MODID, "revolvers/shaders/revolver_grip");
	public static final ResourceLocation PROCESSED_REVOLVER_LAYER = new ResourceLocation(Lib.MODID, "revolvers/shaders/revolver_0");
	public static final ResourceLocation PROCESSED_CHEMTHROW_LAYER = new ResourceLocation(Lib.MODID, "item/shaders/chemthrower_0");
	public static final ResourceLocation PROCESSED_DRILL_LAYER = new ResourceLocation(Lib.MODID, "item/shaders/drill_diesel_0");
	public static final ResourceLocation PROCESSED_RAILGUN_LAYER = new ResourceLocation(Lib.MODID, "item/shaders/railgun_0");
	public static final ResourceLocation PROCESSED_SHIELD_LAYER = new ResourceLocation(Lib.MODID, "item/shaders/shield_0");
	//	public static final ResourceLocation PROCESSED_MINECART_LAYER = new ResourceLocation(Lib.MODID, "textures/models/shaders/minecart_0");
	public static final ResourceLocation PROCESSED_BALLOON_LAYER = new ResourceLocation(Lib.MODID, "block/shaders/balloon_0");

//	public static final ResourceLocation PROCESSED_REVOLVER_GRIP_LAYER = new ResourceLocation(TwilightForestMod.ID, "items/immersiveengineering/revolver_grip_processed");
//	public static final ResourceLocation PROCESSED_REVOLVER_LAYER = new ResourceLocation(TwilightForestMod.ID, "items/immersiveengineering/revolver_processed");
//	public static final ResourceLocation PROCESSED_CHEMTHROW_LAYER = new ResourceLocation(TwilightForestMod.ID, "items/immersiveengineering/chemthrower_processed");
//	public static final ResourceLocation PROCESSED_DRILL_LAYER = new ResourceLocation(TwilightForestMod.ID, "items/immersiveengineering/drill_processed");
//	public static final ResourceLocation PROCESSED_RAILGUN_LAYER = new ResourceLocation(TwilightForestMod.ID, "items/immersiveengineering/railgun_processed");
//	public static final ResourceLocation PROCESSED_SHIELD_LAYER = new ResourceLocation(TwilightForestMod.ID, "items/immersiveengineering/shield_processed");
//	//public static final ResourceLocation PROCESSED_MINECART_LAYER = new ResourceLocation(TwilightForestMod.ID, "textures/items/immersiveengineering/minecart_processed.png");
//	public static final ResourceLocation PROCESSED_BALLOON_LAYER = new ResourceLocation(TwilightForestMod.ID, "blocks/immersiveengineering/balloon_processed");

	private static final ResourceLocation TEXTURE_STARS = new ResourceLocation("textures/entity/end_portal.png");

	private static final BiConsumer<IntConsumer, Boolean> TWILIGHT_TRICONSUMER = (shaderCallback, pre) -> {
		if (pre) {
			ShaderManager.useShader(ShaderManager.twilightSkyShader, shaderCallback);
			ARBShaderObjects.glCreateShaderObjectARB(ARBShaderObjects.GL_INT_VEC2_ARB);
			RenderSystem._setShaderTexture(0, TEXTURE_STARS);
		} else {
			ShaderManager.releaseShader();
		}
		ARBShaderObjects.glCreateShaderObjectARB(ARBShaderObjects.GL_OBJECT_SHADER_SOURCE_LENGTH_ARB);
		RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
	};

	// TODO There's got to be a better way!
	private static final BiConsumer<IntConsumer, Boolean> FIREFLY_TRICONSUMER = (shaderCallback, pre) -> {
		if (pre) ShaderManager.useShader(ShaderManager.fireflyShader, shaderCallback);
		else ShaderManager.releaseShader();
	};

	private static final BiConsumer<IntConsumer, Boolean> CARMINITE_TRICONSUMER = (shaderCallback, pre) -> {
		if (pre) ShaderManager.useShader(ShaderManager.carminiteShader, shaderCallback);
		else ShaderManager.releaseShader();
	};

	private static final BiConsumer<IntConsumer, Boolean> DEVICE_RED_ENERGY_TRICONSUMER = (shaderCallback, pre) -> {
		if (pre) ShaderManager.useShader(ShaderManager.towerDeviceShader, shaderCallback);
		else ShaderManager.releaseShader();
	};

	private static final BiConsumer<IntConsumer, Boolean> DEVICE_YELLOW_ENERGY_TRICONSUMER = (shaderCallback, pre) -> {
		if (pre) {
			GlStateManager._getTexLevelParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			ShaderManager.useShader(ShaderManager.yellowCircuitShader, shaderCallback);
		} else {
			ShaderManager.releaseShader();
			GlStateManager._getTexLevelParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		}
	};

	private static final BiConsumer<IntConsumer, Boolean> AURORA_TRICONSUMER = (shaderCallback, pre) -> {
		if (pre) ShaderManager.useShader(ShaderManager.auroraShader, shaderCallback);
		else ShaderManager.releaseShader();
	};

	private static final BiConsumer<IntConsumer, Boolean> RAM_TRICONSUMER = (shaderCallback, pre) -> {
		if (pre) Minecraft.getInstance().gameRenderer.lightTexture().turnOffLightLayer();
		else Minecraft.getInstance().gameRenderer.lightTexture().turnOnLightLayer();
	};

	//private static final BiConsumer<IntConsumer, Boolean> OUTLINE_TRICONSUMER = (shaderCallback, pre) -> {
	//    if (pre) {
	//        GlStateManager.pushMatrix();
	//    } else {
	//        GlStateManager.popMatrix();
	//    }
	//    //if (pre) {
	//    //    //GlStateManager.pushMatrix();
	//    //    //GlStateManager.scalef(1.05f, 1.05f, 1.05f);
	//    //    GlStateManager.enableCull();
	//    //    //GL11.glFrontFace(GL11.GL_CW);
	//    //    GlStateManager.cullFace(GlStateManager.CullFace.FRONT);
	//    //    ShaderHelper.useShader(ShaderHelper.outlineShader, shaderCallback);
	//    //} else {
	//    //    ShaderHelper.releaseShader();
	//    //    GlStateManager.cullFace(GlStateManager.CullFace.BACK);
	//    //    //GL11.glFrontFace(GL11.GL_CCW);
	//    //    GlStateManager.disableCull();
	//    //    //GlStateManager.scalef(0.8333f, 0.8333f, 0.8333f);
	//    //    //GlStateManager.popMatrix();
	//    //}
	//};

	// m Mod
	// t CaseType
	// s Suffix
	// c Color
	private static final ShaderLayerFactory<?> LAYER_PROVIDER = (m, t, s, c) -> new ShaderLayer(m.provideTex(t, s), c);

	// Registering
	private static List<ShaderRegistry.ShaderRegistryEntry> SHADERS;
	private static List<ShaderRegistry.ShaderRegistryEntry> NONBOSSES;

	private static final Rarity RARITY = TwilightForestMod.getRarity();

	//FIXME some shaders are still broken
	public static void initShaders() {
		NONBOSSES = ImmutableList.of(
				// MAIN COLOR, MINOR COLOR (EDGES), SECONDARY COLOR (GRIP, etc)

				registerShaderCases( "Twilight", ModType.IMMERSIVE_ENGINEERING, "1_4", RARITY,
						0xFF_4C_64_5B, 0xFF_28_25_3F, 0xFF_00_AA_00, 0xFF_FF_FF_FF,
						(m, t, s, c) -> new ShaderGLLayer(m.provideTex(t, s), 0xFFFFFFFF, GLShaders.getCompositeState(GLShaders::getTwilightSky)))
						.setInfo("Twilight Forest", null, "twilightforest")
						.setReplicationCost(() -> new IngredientWithSize(Ingredient.of(TFBlocks.CANOPY_SAPLING.get()), 32)),

				registerShaderCases( "Firefly", ModType.IMMERSIVE_ENGINEERING, "1_6", RARITY,
						0xFF_66_41_40, 0xFF_C0_FF_00, 0xFF_F5_99_2F, 0xFF_C0_FF_00, LAYER_PROVIDER,
						(m, t, s, c) -> new ShaderGLLayer(m.provideTex(t, s), 0xFF_C0_FF_00, GLShaders.emissiveComposite.get()))
						.setInfo("Twilight Forest", null, "firefly")
						.setReplicationCost(() -> new IngredientWithSize(Ingredient.of(TFBlocks.FIREFLY.get()), 32)),

				//TODO add a proper replication ingredient
				registerShaderCases("Pinch Beetle", ModType.IMMERSIVE_ENGINEERING, "1_0", RARITY,
						0xFF_BC_93_27, 0xFF_24_16_09, 0xFF_24_16_09, 0xFF_44_44_44, LAYER_PROVIDER,
						(m, t, s, c) -> new ShaderLayer(m.provideTex(t, "1_6"), c))
						.setInfo("Twilight Forest", null, "pinch_beetle")
						.setReplicationCost(() -> new IngredientWithSize(Ingredient.of(TFItems.ORE_METER.get()))),

				registerShaderCases("Nagastone", ModType.TWILIGHT_FOREST, "streaks", RARITY,
						0xFF_9F_9F_9F, 0xFF_68_68_68, 0xFF_60_60_60, 0xFF_FF_FF_FF, LAYER_PROVIDER,
						(m, t, s, c) -> new ShaderLayer(ModType.TWILIGHT_FOREST.provideTex(t, "scales"), 0xFF_50_50_50),
						(m, t, s, c) -> new ShaderLayer(ModType.IMMERSIVE_ENGINEERING.provideTex(t, "circuit"), 0xFF_58_58_58))
						.setInfo("Twilight Forest", null, "nagastone")
						.setReplicationCost(() -> new IngredientWithSize(Ingredient.of(TFBlocks.NAGASTONE.get(), TFBlocks.NAGASTONE_HEAD.get(),
								TFBlocks.ETCHED_NAGASTONE.get(), TFBlocks.CRACKED_ETCHED_NAGASTONE.get(), TFBlocks.MOSSY_ETCHED_NAGASTONE.get(),
								TFBlocks.NAGASTONE_PILLAR.get(), TFBlocks.CRACKED_NAGASTONE_PILLAR.get(), TFBlocks.MOSSY_NAGASTONE_PILLAR.get()), 10)),

				registerShaderCases("Mazestone", ModType.TWILIGHT_FOREST, "scales", RARITY,
						0xFF_8E_99_8E, 0xFF_50_59_50, 0xFF_70_7B_70, 0xFF_FF_FF_FF, LAYER_PROVIDER)
						.setInfo("Twilight Forest", null, "mazestone")
						.setReplicationCost(() -> new IngredientWithSize(Ingredient.of(TFBlocks.MAZESTONE.get(), TFBlocks.MAZESTONE_BRICK.get(),
								TFBlocks.CRACKED_MAZESTONE.get(), TFBlocks.MOSSY_MAZESTONE.get(), TFBlocks.CUT_MAZESTONE.get(),
								TFBlocks.DECORATIVE_MAZESTONE.get(), TFBlocks.MAZESTONE_MOSAIC.get(), TFBlocks.MAZESTONE_BORDER.get()), 10)),

				registerShaderCases("Underbrick", ModType.TWILIGHT_FOREST, "scales", RARITY,
						0xFF_85_68_45, 0xFF_76_7F_76, 0xFF_61_4D_33, 0xFF_FF_FF_FF, LAYER_PROVIDER)
						.setInfo("Twilight Forest", null, "underbrick")
						.setReplicationCost(() -> new IngredientWithSize(Ingredient.of(TFBlocks.UNDERBRICK.get(), TFBlocks.CRACKED_UNDERBRICK.get(),
								TFBlocks.MOSSY_UNDERBRICK.get(), TFBlocks.UNDERBRICK_FLOOR.get()), 10)),

				registerShaderCases("Towerwood", ModType.IMMERSIVE_ENGINEERING, "1_0", RARITY,
						0xFF_A6_65_3A, 0xFF_F5_DA_93, 0xFF_83_5A_35, 0xFF_FF_FF_FF,
						(m, t, s, c) -> new ShaderGLLayer(ModType.IMMERSIVE_ENGINEERING.provideTex(t, "circuit"), 0xFF_BA_EE_02, GLShaders.emissiveComposite.get()), LAYER_PROVIDER )
						.setInfo("Twilight Forest", null, "towerwood")
						.setReplicationCost(() -> new IngredientWithSize(Ingredient.of(TFBlocks.TOWERWOOD.get(), TFBlocks.CRACKED_TOWERWOOD.get(),
						TFBlocks.MOSSY_TOWERWOOD.get(), TFBlocks.ENCASED_TOWERWOOD.get()), 10)),

				registerShaderCases("Carminite", ModType.TWILIGHT_FOREST, "carminite", RARITY,
						0xFF_72_00_00, 0xFF_FF_00_00, 0xFF_FF_00_00, 0xFF_FF_00_00,
						(modType, t, s, c) -> new ShaderGLLayer(ModType.TWILIGHT_FOREST.provideTex(t, "energy"), 0xFF_FF_00_00, GLShaders.emissiveComposite.get()))
						.setInfo("Twilight Forest", null, "carminite")
						.setReplicationCost(() -> new IngredientWithSize(Ingredient.of(TFItems.CARMINITE.get()), 5)),

				registerShaderCases("Auroralized", ModType.IMMERSIVE_ENGINEERING, "1_5", RARITY,
						0xFF_00_FF_FF, 0xFF_00_FF_00, 0xFF_00_00_FF, 0xFF_FF_FF_FF,
						(m, t, s, c) -> new ShaderGLLayer(ModType.TWILIGHT_FOREST.provideTex(t, "streaks"), 0xFFFFFFFF, GLShaders.getCompositeState(GLShaders::getAurora)))
						.setInfo("Twilight Forest", null, "aurora")
						.setReplicationCost(() -> new IngredientWithSize(Ingredient.of(TFBlocks.AURORA_BLOCK.get(), TFBlocks.AURORA_PILLAR.get(), TFBlocks.AURORA_SLAB.get()), 10))				,

				registerShaderCases("Ironwood", ModType.IMMERSIVE_ENGINEERING, "1_0", RARITY,
						0xFF_6B_61_61, 0xFF_5F_4D_40, 0xFF_5E_57_4B, 0xFF_FF_FF_FF,
						(m, t, s, c) -> new ShaderLayer(ModType.TWILIGHT_FOREST.provideTex(t, "streaks"), 0xFF_79_7C_43), LAYER_PROVIDER)
						.setInfo("Twilight Forest", null, "ironwood")
						.setReplicationCost(() -> new IngredientWithSize(Ingredient.of(TFBlocks.IRONWOOD_BLOCK.get()), 2)),

				registerShaderCases("Steeleaf", ModType.IMMERSIVE_ENGINEERING, "1_0", RARITY,
						0xFF_52_87_3A, 0xFF_1E_32_14, 0xFF_41_62_30, 0xFF_FF_FF_FF,
						(m, t, s, c) -> new ShaderLayer(ModType.IMMERSIVE_ENGINEERING.provideTex(t, "1_4"), 0xFF_41_62_30),
						(m, t, s, c) -> new ShaderLayer(ModType.TWILIGHT_FOREST.provideTex(t, "streaks"), 0xFF_6D_A2_5E), LAYER_PROVIDER)
						.setInfo("Twilight Forest", null, "steeleaf")
						.setReplicationCost(() -> new IngredientWithSize(Ingredient.of(TFBlocks.STEELEAF_BLOCK.get()), 2)),

				registerShaderCases("Knightmetal", ModType.IMMERSIVE_ENGINEERING, "1_0", RARITY,
						0xFF_E7_FC_CD, 0xFF_4D_4C_4B, 0xFF_80_8C_72, 0xFF_FF_FF_FF, LAYER_PROVIDER)
						.setInfo("Twilight Forest", null, "knightmetal")
						.setReplicationCost(() -> new IngredientWithSize(Ingredient.of(TFBlocks.KNIGHTMETAL_BLOCK.get()), 2)),

				registerShaderCases("Fiery", ModType.IMMERSIVE_ENGINEERING, "1_0", RARITY,
						0xFF_19_13_13, 0xFF_FD_D4_5D, 0xFF_77_35_11, 0xFF_FF_FF_FF,
						(m, t, s, c) -> new ShaderGLLayer(m.provideTex(t, s), 0xFF_FD_D4_5D, GLShaders.emissiveComposite.get()))
						.setInfo("Twilight Forest", null, "fiery")
						.setReplicationCost(() -> new IngredientWithSize(Ingredient.of(TFBlocks.FIERY_BLOCK.get()), 2)),

				registerShaderCases("Final Castle", ModType.TWILIGHT_FOREST, "scales", RARITY,
						0xFF_EC_EA_E6, 0xFF_C8_BB_BC, 0xFF_00_FF_FF, 0xFF_00_FF_FF, LAYER_PROVIDER)
						.setInfo("Twilight Forest", null, "final_castle")
						.setReplicationCost(() -> new IngredientWithSize(Ingredient.of(TFBlocks.CASTLE_BRICK.get(), TFBlocks.WORN_CASTLE_BRICK.get(),
								TFBlocks.CRACKED_CASTLE_BRICK.get(), TFBlocks.MOSSY_CASTLE_BRICK.get(),
								TFBlocks.CASTLE_ROOF_TILE.get(), TFBlocks.THICK_CASTLE_BRICK.get(),
								TFBlocks.ENCASED_CASTLE_BRICK_TILE.get(), TFBlocks.ENCASED_CASTLE_BRICK_PILLAR.get(),
								TFBlocks.BOLD_CASTLE_BRICK_TILE.get(), TFBlocks.BOLD_CASTLE_BRICK_PILLAR.get()), 10)),

				//TODO add a proper replication ingredient
				registerShaderCases("Cube of Annihilation", ModType.IMMERSIVE_ENGINEERING, "1_0", RARITY,
						0xFF_00_00_03, 0xFF_14_00_40, 0xFF_00_00_03, 0xFF_14_00_40,
						(m, t, s, c) -> new ShaderGLLayer(m.provideTex(t, s), 0xFF_14_00_40, GLShaders.emissiveComposite.get()))
						.setInfo("Twilight Forest", null, "cube_of_annihilation")
						.setReplicationCost(() -> new IngredientWithSize(Ingredient.of(TFItems.ORE_METER.get())))
		);

		ImmutableList.Builder<ShaderRegistry.ShaderRegistryEntry> listBuilder = ImmutableList.builder();

		listBuilder.addAll(NONBOSSES);

		listBuilder.add(
				registerShaderCases("Quest Ram", ModType.TWILIGHT_FOREST, "streaks", RARITY,
						0xFF_F9_E1_C8, 0xFF_9A_85_69, 0xFF_2F_2B_36, 0xFF_90_D8_EF, LAYER_PROVIDER,
						(m, t, s, c) -> new ShaderGLLayer(ModType.IMMERSIVE_ENGINEERING.provideTex(t, "circuit"), 0x30_90_D8_EF, GLShaders.emissiveComposite.get()))
						.setInfo("Twilight Forest", null, "quest_ram")
						.setReplicationCost(() -> new IngredientWithSize(Ingredient.of(TFItems.CRUMBLE_HORN.get()), 1)),

				registerShaderCases("Naga", ModType.TWILIGHT_FOREST, "scales", RARITY,
						0xFF_32_5D_25, 0xFF_17_29_11, 0xFF_A5_D4_16, 0xFF_FF_FF_FF, LAYER_PROVIDER,
						(m, t, s, c) -> new ShaderLayer(ModType.IMMERSIVE_ENGINEERING.provideTex(t, "shark"), 0xFF_FF_FF_FF))
						.setInfo("Twilight Forest", null, "naga")
						.setReplicationCost(() -> new IngredientWithSize(Ingredient.of(TFItems.NAGA_SCALE.get()), 1)),

				registerShaderCases("Lich", ModType.IMMERSIVE_ENGINEERING, "1_0", RARITY,
						0xFF_DF_D9_CC, 0xFF_C3_9C_00, 0xFF_3A_04_75, 0xFF_FF_FF_FF, LAYER_PROVIDER)
						.setInfo("Twilight Forest", null, "lich")
						.setReplicationCost(() -> new IngredientWithSize(Ingredient.of(TFItems.TWILIGHT_SCEPTER.get(), TFItems.LIFEDRAIN_SCEPTER.get(), TFItems.ZOMBIE_SCEPTER.get(), TFItems.FORTIFICATION_SCEPTER.get()), 1)),

				registerShaderCases("Minoshroom", ModType.IMMERSIVE_ENGINEERING, "1_6", RARITY,
						0xFF_A8_10_12, 0xFF_B3_B3_B3, 0xFF_33_EB_CB, 0xFF_FF_FF_FF, LAYER_PROVIDER)
						.setInfo("Twilight Forest", null, "minoshroom")
						.setReplicationCost(() -> new IngredientWithSize(Ingredient.of(TFItems.MEEF_STROGANOFF.get()), 1)),

				registerShaderCases("Hydra", ModType.TWILIGHT_FOREST, "scales", RARITY,
						0xFF_14_29_40, 0xFF_29_80_6B, 0xFF_F1_0A_92, 0xFF_FF_FF_FF, LAYER_PROVIDER,
						(m, t, s, c) -> new ShaderLayer(ModType.IMMERSIVE_ENGINEERING.provideTex(t, "shark"), 0xFF_FF_FF_FF))
						.setInfo("Twilight Forest", null, "hydra")
						.setReplicationCost(() -> new IngredientWithSize(Ingredient.of(TFItems.HYDRA_CHOP.get(), TFItems.FIERY_BLOOD.get()), 1)),

				registerShaderCases("Knight Phantom", ModType.IMMERSIVE_ENGINEERING, "1_0", RARITY,
						0xCC_40_6D_05, 0xFF_36_35_34, 0xFF_7A_5C_49, 0xFF_FF_FF_FF, LAYER_PROVIDER)
						.setInfo("Twilight Forest", null, "knight_phantom")
						.setReplicationCost(() -> new IngredientWithSize(Ingredient.of(TFItems.PHANTOM_HELMET.get(), TFItems.PHANTOM_CHESTPLATE.get()), 1)),

				registerShaderCases("Ur-Ghast", ModType.IMMERSIVE_ENGINEERING, "1_2", RARITY,
						0xFF_F9_F9_F9, 0xFF_9A_37_37, 0xFF_56_56_56, 0xFF_FF_FF_FF, LAYER_PROVIDER)
						.setInfo("Twilight Forest", null, "ur-ghast")
						.setReplicationCost(() -> new IngredientWithSize(Ingredient.of(TFItems.FIERY_TEARS.get()), 1)),

				registerShaderCases("Alpha Yeti", ModType.IMMERSIVE_ENGINEERING, "1_0", RARITY,
						0xFF_FC_FC_FC, 0xFF_4A_80_CE, 0xFF_25_3F_66, 0xFF_FF_FF_FF, LAYER_PROVIDER)
						.setInfo("Twilight Forest", null, "alpha_yeti")
						.setReplicationCost(() -> new IngredientWithSize(Ingredient.of(TFItems.ALPHA_YETI_FUR.get()), 1)),

				registerShaderCases("Snow Queen", ModType.IMMERSIVE_ENGINEERING, "1_0", RARITY,
						0xFF_DC_FB_FF, 0xFF_C3_9C_00, 0xFF_03_05_89, 0xFF_FF_FF_FF, LAYER_PROVIDER)
						.setInfo("Twilight Forest", null, "snow_queen")
						.setReplicationCost(() -> new IngredientWithSize(Ingredient.of(TFItems.ICE_BOMB.get()), 1))
		);

		SHADERS = listBuilder.build();
	}

	public static List<ShaderRegistry.ShaderRegistryEntry> getAllTwilightShaders() {
		return SHADERS;
	}

	public static List<ShaderRegistry.ShaderRegistryEntry> getAllNonbossShaders() {
		return NONBOSSES;
	}

	// Shaderizing!
	@Deprecated
	private static class ShaderConsumerLayer extends DynamicShaderLayer {

		private final BiConsumer<IntConsumer, Boolean> render;
		private final IntConsumer shaderCallback;

		ShaderConsumerLayer(ResourceLocation texture, int colour, BiConsumer<IntConsumer, Boolean> render, ShaderUniform[] shaderParams) {
			super(texture, colour);
			this.render = render;

			shaderCallback = shader -> {
				for (ShaderUniform param : shaderParams) {
					param.assignUniform(shader);
				}
			};
		}

		@Override
		public RenderType getRenderType(RenderType baseType) {
			if (this.render == null) {
				return baseType;
			} else {
				return new RenderType(
						"shader_" + baseType + render,
						DefaultVertexFormat.BLOCK,
						VertexFormat.Mode.QUADS,
						256,
						false,
						true,
						() -> {
							baseType.setupRenderState();
							render.accept(shaderCallback, true);
						},
						() -> {
							render.accept(shaderCallback, false);
							baseType.clearRenderState();
						}
				) {

				};
			}
		}
	}

	@SafeVarargs
	@SuppressWarnings({"rawtypes", "varargs"})
	private static ShaderRegistry.ShaderRegistryEntry registerShaderCasesTopped(String name, ModType mod, String overlayType, Rarity rarity, int bodyColor, int colorSecondary, int gripColor, int colorBlade, ShaderLayerFactory<? extends ShaderLayer>[] providers, ShaderLayerFactory<? extends ShaderLayer>... extraProviders) {
		ResourceLocation modName = new ResourceLocation(DoIAmEngineering.MODID, name.toLowerCase(Locale.ROOT).replace(" ", "_"));
		ShaderRegistry.registerShader_Item(modName, rarity, gripColor, bodyColor, colorSecondary);

		registerShaderCaseRevolver(modName, gripColor, bodyColor, colorBlade, rarity, ShaderLayerFactory.provideFromFactories(mod, CaseType.REVOLVER, overlayType, colorSecondary, providers), ShaderLayerFactory.provideFromFactories(mod, CaseType.REVOLVER, overlayType, colorSecondary, extraProviders));
		registerShaderCaseChemthrower(modName, gripColor, bodyColor, rarity, ShaderLayerFactory.provideFromFactories(mod, CaseType.CHEMICAL_THROWER, overlayType, colorSecondary, providers), ShaderLayerFactory.provideFromFactories(mod, CaseType.CHEMICAL_THROWER, overlayType, colorSecondary, extraProviders));
		registerShaderCaseDrill(modName, gripColor, bodyColor, colorBlade, rarity, ShaderLayerFactory.provideFromFactories(mod, CaseType.DRILL, overlayType, colorSecondary, providers), ShaderLayerFactory.provideFromFactories(mod, CaseType.DRILL, overlayType, colorSecondary, extraProviders));
		registerShaderCaseRailgun(modName, gripColor, bodyColor, rarity, ShaderLayerFactory.provideFromFactories(mod, CaseType.RAILGUN, overlayType, colorSecondary, providers), ShaderLayerFactory.provideFromFactories(mod, CaseType.RAILGUN, overlayType, colorSecondary, extraProviders));
		registerShaderCaseShield(modName, gripColor, bodyColor, rarity, ShaderLayerFactory.provideFromFactories(mod, CaseType.SHIELD, overlayType, colorSecondary, providers), ShaderLayerFactory.provideFromFactories(mod, CaseType.SHIELD, overlayType, colorSecondary, extraProviders));
		registerShaderCaseMinecart(modName, gripColor, bodyColor, rarity, ShaderLayerFactory.provideFromFactories(mod, CaseType.MINECART, overlayType, colorSecondary, providers), ShaderLayerFactory.provideFromFactories(mod, CaseType.MINECART, overlayType + ".png", colorSecondary, extraProviders));
		registerShaderCaseBalloon(modName, gripColor, bodyColor, rarity, ShaderLayerFactory.provideFromFactories(mod, CaseType.BALLOON, overlayType, colorSecondary, providers), ShaderLayerFactory.provideFromFactories(mod, CaseType.BALLOON, overlayType, colorSecondary, extraProviders));
		registerShaderCaseBanner(modName, gripColor, bodyColor, rarity, ShaderLayerFactory.provideFromFactories(mod, CaseType.BALLOON, overlayType, colorSecondary, providers), ShaderLayerFactory.provideFromFactories(mod, CaseType.BANNER, overlayType, colorSecondary, extraProviders));

		// Since shaders won't occur in a way we'd like them to, we should register any additional variants ourselves if we know of any
		for (ShaderRegistry.IShaderRegistryMethod method : ShaderRegistry.shaderRegistrationMethods) {
			method.apply(modName, overlayType, rarity, gripColor, bodyColor, colorSecondary, colorBlade, null, 0);
		}

		return ShaderRegistry.shaderRegistry.get(modName).setCrateLoot(false).setBagLoot(false).setInLowerBags(false);
	}

	@SafeVarargs
	@SuppressWarnings("varargs")
	private static ShaderRegistry.ShaderRegistryEntry registerShaderCases(String name, ModType type, String overlayType, Rarity rarity, int bodyColor, int colorSecondary, int gripColor, int colorBlade, ShaderLayerFactory<? extends ShaderLayer>... providers) {
		return registerShaderCasesTopped(name, type, overlayType, rarity, bodyColor, colorSecondary, gripColor, colorBlade, providers);
	}

	// Shader Case Registration helpers
	@SuppressWarnings("UnusedReturnValue")
	private static ShaderCaseRevolver registerShaderCaseRevolver(ResourceLocation name, int gripColor, int bodyColor, int bladeColor, Rarity rarity, ShaderLayer[] additionalLayers, ShaderLayer... topLayers) {
		ImmutableList.Builder<ShaderLayer> shaderLayerBuilder = ImmutableList.builder();

		return ShaderRegistry.registerShaderCase(name, new ShaderCaseRevolver(
				shaderLayerBuilder.add(
						new ShaderLayer(PROCESSED_REVOLVER_GRIP_LAYER, gripColor),
						new ShaderLayer(PROCESSED_REVOLVER_LAYER, bodyColor),
						new ShaderLayer(PROCESSED_REVOLVER_LAYER, bladeColor)
				).add(additionalLayers).add(UNCOLORED_REVOLVER_LAYER).add(topLayers).build()), rarity);
	}

	@SuppressWarnings("UnusedReturnValue")
	private static ShaderCaseChemthrower registerShaderCaseChemthrower(ResourceLocation name, int gripColor, int bodyColor, Rarity rarity, ShaderLayer[] additionalLayers, ShaderLayer... topLayers) {
		ImmutableList.Builder<ShaderLayer> shaderLayerBuilder = ImmutableList.builder();

		return ShaderRegistry.registerShaderCase(name, new TFShaderCaseChemthrower(3 + additionalLayers.length,
				shaderLayerBuilder.add(
						new ShaderLayer(PROCESSED_CHEMTHROW_LAYER, gripColor),
						new ShaderLayer(PROCESSED_CHEMTHROW_LAYER, bodyColor)
				).add(additionalLayers).add(UNCOLORED_CHEMTHROW_LAYER).add(topLayers).build()), rarity);
	}

	@SuppressWarnings("UnusedReturnValue")
	private static ShaderCaseDrill registerShaderCaseDrill(ResourceLocation name, int gripColor, int bodyColor, int bladeColor, Rarity rarity, ShaderLayer[] additionalLayers, ShaderLayer... topLayers) {
		ImmutableList.Builder<ShaderLayer> shaderLayerBuilder = ImmutableList.builder();

		return ShaderRegistry.registerShaderCase(name, new TFShaderCaseDrill(5 + additionalLayers.length,
				shaderLayerBuilder.add(
						new ShaderLayer(PROCESSED_DRILL_LAYER, gripColor),
						new ShaderLayer(PROCESSED_DRILL_LAYER, bodyColor)
				).add(UNCOLORED_DRILL_LAYER).add(additionalLayers).add(UNCOLORED_DRILL_LAYER).add(NULL_LAYER).add(topLayers).build()), rarity);//.addHeadLayers(new ShaderCase (new ResourceLocation(Lib.MODID, "items/drill_iron"), bladeColor));
	}

	@SuppressWarnings("UnusedReturnValue")
	private static ShaderCaseRailgun registerShaderCaseRailgun(ResourceLocation name, int gripColor, int bodyColor, Rarity rarity, ShaderLayer[] additionalLayers, ShaderLayer... topLayers) {
		ImmutableList.Builder<ShaderLayer> shaderLayerBuilder = ImmutableList.builder();

		return ShaderRegistry.registerShaderCase(name, new TFShaderCaseRailgun(3 + additionalLayers.length,
				shaderLayerBuilder.add(
						new ShaderLayer(PROCESSED_RAILGUN_LAYER, gripColor),
						new ShaderLayer(PROCESSED_RAILGUN_LAYER, bodyColor)
				).add(additionalLayers).add(UNCOLORED_RAILGUN_LAYER).add(topLayers).build()), rarity);
	}

	@SuppressWarnings("UnusedReturnValue")
	private static ShaderCaseShield registerShaderCaseShield(ResourceLocation name, int gripColor, int bodyColor, Rarity rarity, ShaderLayer[] additionalLayers, ShaderLayer... topLayers) {
		ImmutableList.Builder<ShaderLayer> shaderLayerBuilder = ImmutableList.builder();

		return ShaderRegistry.registerShaderCase(name, new TFShaderCaseShield(3 + additionalLayers.length,
				shaderLayerBuilder.add(
						new ShaderLayer(PROCESSED_SHIELD_LAYER, gripColor),
						new ShaderLayer(PROCESSED_SHIELD_LAYER, bodyColor)
				).add(additionalLayers).add(UNCOLORED_SHIELD_LAYER).add(topLayers).build()), rarity);
	}

	@SuppressWarnings("UnusedReturnValue")
	private static ShaderCaseMinecart registerShaderCaseMinecart(ResourceLocation name, int bodyColor, int secondaryColor, Rarity rarity, ShaderLayer[] additionalLayers, ShaderLayer... topLayers) {
		ImmutableList.Builder<ShaderLayer> shaderLayerBuilder = ImmutableList.builder();

		return ShaderRegistry.registerShaderCase(name, new ShaderCaseMinecart(
				shaderLayerBuilder.add(
						new ShaderLayer(new ResourceLocation(Lib.MODID, "textures/models/shaders/minecart_0.png"), bodyColor),
						new ShaderLayer(new ResourceLocation(Lib.MODID, "textures/models/shaders/minecart_1_0.png"), secondaryColor)
				).add(additionalLayers).add(UNCOLORED_MINECART_LAYER).add(topLayers).build()), rarity);
	}

	@SuppressWarnings("UnusedReturnValue")
	public static ShaderCaseBanner registerShaderCaseBanner(ResourceLocation name, int bodyColor, int secondaryColor, Rarity rarity, ShaderLayer[] additionalLayers, ShaderLayer... topLayers) {
		ImmutableList.Builder<ShaderLayer> shaderLayerBuilder = ImmutableList.builder();

		return ShaderRegistry.registerShaderCase(name, new ShaderCaseBanner(
				shaderLayerBuilder.add(
						new ShaderLayer(new ResourceLocation(Lib.MODID, "block/shaders/banner_0"), bodyColor),
						new ShaderLayer(new ResourceLocation(Lib.MODID, "block/shaders/banner_1_0"), secondaryColor)
				).add(additionalLayers).add(UNCOLORED_BANNER_LAYER).add(topLayers).build()), rarity);
	}

	@SuppressWarnings("UnusedReturnValue")
	private static ShaderCaseBalloon registerShaderCaseBalloon(ResourceLocation name, int gripColor, int bodyColor, Rarity rarity, ShaderLayer[] additionalLayers, ShaderLayer... topLayers) {
		ImmutableList.Builder<ShaderLayer> shaderLayerBuilder = ImmutableList.builder();

		return ShaderRegistry.registerShaderCase(name, new ShaderCaseBalloon(
				shaderLayerBuilder.add(
						new ShaderLayer(PROCESSED_BALLOON_LAYER, gripColor),
						new ShaderLayer(PROCESSED_BALLOON_LAYER, bodyColor)
				).add(additionalLayers).add(UNCOLORED_BALLOON_LAYER).add(topLayers).build()), rarity);
	}

	public enum ModType {
		IMMERSIVE_ENGINEERING(Lib.MODID) {
			@Override
			String getPath(CaseType caseType, String suffix) {
				return switch (caseType) {
					case REVOLVER -> "revolvers/shaders/revolver_" + suffix;
					case CHEMICAL_THROWER -> "item/shaders/chemthrower_" + suffix;
					case DRILL -> "item/shaders/drill_diesel_" + suffix;
					case RAILGUN -> "item/shaders/railgun_" + suffix;
					case SHIELD -> "item/shaders/shield_" + suffix;
					case MINECART -> "textures/models/shaders/minecart_" + suffix + ".png";
					case BALLOON -> "block/shaders/balloon_" + suffix;
					case BANNER -> "block/shaders/banner_" + suffix;
				};
			}
		},
		TWILIGHT_FOREST(DoIAmEngineering.MODID) {
			@Override
			String getPath(CaseType caseType, String suffix) {
				return switch (caseType) {
					case REVOLVER -> "items/shaders/revolver_" + suffix;
					case CHEMICAL_THROWER -> "items/shaders/chemthrower_" + suffix;
					case DRILL -> "items/shaders/drill_" + suffix;
					case RAILGUN -> "items/shaders/railgun_" + suffix;
					case SHIELD -> "items/shaders/shield_" + suffix;
					case MINECART -> "entity/minecart_" + suffix;
					case BALLOON -> "block/balloon_" + suffix;
					case BANNER -> "block/banner_" + suffix;
				};
			}

			@Override
			public ResourceLocation provideTex(CaseType caseType, String suffix) {
				if (caseType == CaseType.MINECART && suffix.startsWith("1_")) {
					return IMMERSIVE_ENGINEERING.provideTex(caseType, suffix);
				}
				return super.provideTex(caseType, suffix);
			}
		};

		private final String namespace;

		ModType(String namespace) {
			this.namespace = namespace;
		}

		abstract String getPath(CaseType caseType, String suffix);

		public ResourceLocation provideTex(CaseType caseType, String suffix) {
			return new ResourceLocation(namespace, getPath(caseType, suffix));
		}
	}

	public enum CaseType {

		REVOLVER,
		CHEMICAL_THROWER,
		DRILL,
		RAILGUN,
		SHIELD,
		MINECART,
		BALLOON,
		BANNER;

		public static CaseType[] everythingButMinecart() {
			return new CaseType[]{REVOLVER, CHEMICAL_THROWER, DRILL, RAILGUN, SHIELD, BALLOON, BANNER};
		}
	}
}
