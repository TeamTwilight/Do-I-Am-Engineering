package com.gizmo.doiamengineering.client;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ForgeHooksClient;
import org.joml.Matrix4f;
import twilightforest.TFConfig;
import twilightforest.TwilightForestMod;
import twilightforest.client.TFClientEvents;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class ShaderBagItemModel implements BakedModel {

	protected final BakedModel delegate;
	protected final ItemStack item;
	ModelResourceLocation backModelLocation = new ModelResourceLocation(new ResourceLocation(TwilightForestMod.ID, "trophy_minor"), "inventory");


	public ShaderBagItemModel(BakedModel delegate, ItemStack item) {
		this.delegate = delegate;
		this.item = item;
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState pState, @Nullable Direction pSide, RandomSource pRand) {
		return this.delegate.getQuads(pState, pSide, pRand);
	}

	@Override
	public boolean useAmbientOcclusion() {
		return delegate.useAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return delegate.isGui3d();
	}

	@Override
	public boolean usesBlockLight() {
		return delegate.usesBlockLight();
	}

	@Override
	public boolean isCustomRenderer() {
		return delegate.isCustomRenderer();
	}

	@Override
	public TextureAtlasSprite getParticleIcon() {
		return delegate.getParticleIcon();
	}

	@Override
	public ItemOverrides getOverrides() {
		return delegate.getOverrides();
	}

	@Override
	public BakedModel applyTransform(ItemDisplayContext context, PoseStack ms, boolean applyLeftHandTransform) {

		BakedModel modelBack = Minecraft.getInstance().getItemRenderer().getItemModelShaper().getModelManager().getModel(backModelLocation);

		if (context == ItemDisplayContext.GUI) {

			Lighting.setupForFlatItems();
			MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
			ms.pushPose();

			ms.translate(0.0F, 0.0F, -1.5F);
			Minecraft.getInstance().getItemRenderer().render(this.item, ItemDisplayContext.GUI, false, ms, bufferSource, 15728880, OverlayTexture.NO_OVERLAY, ForgeHooksClient.handleCameraTransforms(ms, modelBack, context, false));

			ms.popPose();

			ms.pushPose();

			ms.translate(0.0F, 0.0F, 2.0F);
			// Rotate the lunchbox if we're in the Gui. This is a setup for the next bit of rendering.
			ms.mulPose(Axis.XN.rotationDegrees(TFConfig.CLIENT_CONFIG.rotateTrophyHeadsGui.get() ? Mth.sin(TFClientEvents.rotationTicker * 0.125F) : 30));
			ms.mulPose(Axis.YN.rotationDegrees(TFConfig.CLIENT_CONFIG.rotateTrophyHeadsGui.get() ? TFClientEvents.rotationTicker : 45));
			ms.mulPose(Axis.ZN.rotationDegrees(TFConfig.CLIENT_CONFIG.rotateTrophyHeadsGui.get() ? Mth.sin(TFClientEvents.rotationTicker * 0.125F) : 0));
			ms.translate(0.0F, -0.1F, 0.0F);
			ms.scale(1.25F, 1.25F, 1.25F);

			// Render the lunchbox
			Minecraft.getInstance().getItemRenderer().render(this.item, ItemDisplayContext.GUI, false, ms, bufferSource, 15728880, OverlayTexture.NO_OVERLAY, ForgeHooksClient.handleCameraTransforms(ms, this.delegate, context, false));

			ms.popPose();

			bufferSource.endBatch();
			Lighting.setupFor3DItems();

			drawSquare(ms, this.item.getRarity().color.getColor(), bufferSource.getBuffer(GLShaders.starburstRendering));

			return ForgeHooksClient.handleCameraTransforms(ms, new DummyModel(), context, applyLeftHandTransform);
		} else {
			return ForgeHooksClient.handleCameraTransforms(ms, this.delegate, context, applyLeftHandTransform);
		}
	}

	private static void drawSquare(PoseStack stack, int color, VertexConsumer buffer) {
		float r = FastColor.ARGB32.red(color) / 255.0f;
		float g = FastColor.ARGB32.green(color) / 255.0f;
		float b = FastColor.ARGB32.blue(color) / 255.0f;

		Matrix4f poseMatrix = stack.last().pose();
		buffer.vertex(poseMatrix, -0.5f, 0.5f, 0)
				.uv(-0.5f, 0.5f)
				.color(r, g, b, 1f)
				.endVertex();
		buffer.vertex(poseMatrix, 0.5f, 0.5f, 0)
				.uv(0.5f, 0.5f)
				.color(r, g, b, 1f)
				.endVertex();
		buffer.vertex(poseMatrix, 0.5f, -0.5f, 0)
				.uv(0.5f, -0.5f)
				.color(r, g, b, 1f)
				.endVertex();
		buffer.vertex(poseMatrix, -0.5f, -0.5f, 0)
				.uv(-0.5f, -0.5f)
				.color(r, g, b, 1f)
				.endVertex();
	}

	//dummy class to kill off item rendering in the GUI
	public static class DummyModel implements BakedModel {
		@Override
		public List<BakedQuad> getQuads(@Nullable BlockState pState, @Nullable Direction pSide, RandomSource pRand) { return Collections.emptyList(); }
		@Override
		public boolean useAmbientOcclusion() {return false;}
		@Override
		public boolean isGui3d() {return false;}
		@Override
		public boolean usesBlockLight() {return false;}
		@Override
		public boolean isCustomRenderer() {return false;}
		@Override
		public TextureAtlasSprite getParticleIcon() {return null;}
		@Override
		public ItemOverrides getOverrides() {return null;}
	}
}
