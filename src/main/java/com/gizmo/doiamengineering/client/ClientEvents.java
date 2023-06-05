package com.gizmo.doiamengineering.client;

import blusunrize.immersiveengineering.api.shader.ShaderRegistry;
import com.gizmo.doiamengineering.DoIAmEngineering;
import com.gizmo.doiamengineering.ModRegistry;
import com.gizmo.doiamengineering.item.TFShaderItem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlocks;

import java.io.IOException;
import java.util.Locale;

@Mod.EventBusSubscriber(modid = DoIAmEngineering.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {
	@SubscribeEvent
	public static void registerShaders(RegisterShadersEvent event) throws IOException {
		event.registerShader(new ShaderInstance(event.getResourceProvider(), DoIAmEngineering.prefix("aurora"), DefaultVertexFormat.NEW_ENTITY), shader -> GLShaders.aurora = shader);
		event.registerShader(new ShaderInstance(event.getResourceProvider(), DoIAmEngineering.prefix("emissive"), DefaultVertexFormat.NEW_ENTITY), shader -> GLShaders.emissive = shader);
		event.registerShader(new ShaderInstance(event.getResourceProvider(), DoIAmEngineering.prefix("twilight_sky"), DefaultVertexFormat.NEW_ENTITY), shader -> GLShaders.twilightSky = shader);
	}

	@SubscribeEvent
	public static void registerShaderColors(RegisterColorHandlersEvent.Item colors) {
		colors.register(TFShaderItem::getShaderColors, ModRegistry.SHADER.get());
		for (Rarity rarity : ShaderRegistry.rarityWeightMap.keySet()) {
			colors.register((stack, tintIndex) -> {
				if (rarity.color.getColor() != null) {
					int c = rarity.color.getColor();

					float d = tintIndex + 1;

					return (int) ((c >> 16 & 0xFF) / d) << 16
							| (int) ((c >> 8 & 0xFF) / d) << 8
							| (int) ((c & 0xFF) / d);
				}
				return 0XFFFFFF;
			}, ForgeRegistries.ITEMS.getValue(DoIAmEngineering.prefix("shader_bag_" + rarity)));
		}
	}

	@SubscribeEvent
	public static void registerShaderModels(ModelEvent.ModifyBakingResult event) {
		for (Rarity rarity : ShaderRegistry.rarityWeightMap.keySet()) {
			ResourceLocation itemRL = DoIAmEngineering.prefix("shader_bag_" + rarity.name().toLowerCase(Locale.ROOT).replace(':', '_'));
			ModelResourceLocation mrl = new ModelResourceLocation(itemRL, "inventory");
			event.getModels().put(mrl, new ShaderBagItemModel(event.getModels().get(mrl), new ItemStack(ForgeRegistries.ITEMS.getValue(itemRL))));
		}
	}

	@SubscribeEvent
	public static void registerEntityRenderer(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(ModRegistry.CICADA_SHOT.get(), CicadaShotRenderer::new);
	}

	@Mod.EventBusSubscriber(modid = DoIAmEngineering.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
	public static class ForgeClientEvents {
		@SubscribeEvent
		public static void addCicadaTooltip(ItemTooltipEvent event) {
			if (event.getItemStack().is(TFBlocks.CICADA.get().asItem())) {
				event.getToolTip().add(1, Component.translatable("block.doiamengineering.cicada.desc").withStyle(TwilightForestMod.getRarity().color).withStyle(ChatFormatting.ITALIC));
			}
		}
	}
}
