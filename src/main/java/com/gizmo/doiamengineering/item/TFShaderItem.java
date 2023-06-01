package com.gizmo.doiamengineering.item;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.client.TextUtils;
import blusunrize.immersiveengineering.api.shader.IShaderItem;
import blusunrize.immersiveengineering.api.shader.ShaderCase;
import blusunrize.immersiveengineering.api.shader.ShaderLayer;
import blusunrize.immersiveengineering.api.shader.ShaderRegistry;
import blusunrize.immersiveengineering.api.shader.impl.ShaderCaseItem;
import blusunrize.immersiveengineering.common.blocks.cloth.ShaderBannerBlockEntity;
import blusunrize.immersiveengineering.common.blocks.cloth.ShaderBannerStandingBlock;
import blusunrize.immersiveengineering.common.blocks.cloth.ShaderBannerWallBlock;
import blusunrize.immersiveengineering.common.register.IEBlocks;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import com.gizmo.doiamengineering.DoIAmEngineering;
import com.gizmo.doiamengineering.IEShaderRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.WallBannerBlock;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

public class TFShaderItem extends Item implements IShaderItem {

	public TFShaderItem(Properties properties) {
		super(properties);
	}

	public static final String TAG_SHADER = "shader_name";

	@Override
	public ShaderCase getShaderCase(ItemStack shader, ResourceLocation shaderType) {
		return ShaderRegistry.getShader(this.getShaderName(shader), shaderType);
	}

	@Override
	public ResourceLocation getShaderName(ItemStack stack) {
		return getShaderType(stack);
	}

	@Override
	public Component getName(ItemStack stack) {
		ResourceLocation rawShaderName = this.getShaderName(stack);
		String unlocalizedShaderName = ("item." + rawShaderName.getNamespace() + ".shader.name." + rawShaderName.getPath().replace(' ', '_').toLowerCase(Locale.ROOT));
		String localizedShaderName = Component.translatable(unlocalizedShaderName).getString();

		if (unlocalizedShaderName.equals(localizedShaderName))
			return Component.translatable(this.getDescriptionId(stack), rawShaderName.getPath()).append(" * TRANSLATION FAILURE * ").withStyle(ChatFormatting.DARK_RED); // Translation failure
		else
			return Component.translatable(this.getDescriptionId(stack), localizedShaderName);
	}

	//Copy of ShaderItem.onItemUse so we can apply to banners
	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		Level world = ctx.getLevel();
		BlockPos pos = ctx.getClickedPos();
		ResourceLocation name = this.getShaderName(ctx.getItemInHand());
		if (ShaderRegistry.shaderRegistry.containsKey(name)) {
			BlockState blockState = world.getBlockState(pos);
			BlockEntity tile = world.getBlockEntity(pos);
			if (tile instanceof BannerBlockEntity) {
				ShaderCase sCase = ShaderRegistry.shaderRegistry.get(name).getCase(new ResourceLocation(Lib.MODID, "banner"));
				if (sCase != null) {
					boolean wall = blockState.getBlock() instanceof WallBannerBlock;

					if (wall)
						world.setBlockAndUpdate(pos, IEBlocks.Cloth.SHADER_BANNER_WALL.defaultBlockState()
								.setValue(ShaderBannerWallBlock.FACING, blockState.getValue(WallBannerBlock.FACING)));
					else
						world.setBlockAndUpdate(pos, IEBlocks.Cloth.SHADER_BANNER.defaultBlockState()
								.setValue(ShaderBannerStandingBlock.ROTATION, blockState.getValue(BannerBlock.ROTATION)));
					tile = world.getBlockEntity(pos);
					if (tile instanceof ShaderBannerBlockEntity sb) {
						sb.shader.setShaderItem(ItemHandlerHelper.copyStackWithSize(ctx.getItemInHand(), 1));
						tile.setChanged();
						return InteractionResult.SUCCESS;
					}
				}
			} else if (tile instanceof ShaderBannerBlockEntity sb) {
				sb.shader.setShaderItem(ItemHandlerHelper.copyStackWithSize(ctx.getItemInHand(), 1));
				sb.setChanged();
				return InteractionResult.SUCCESS;
			}

		}
		return InteractionResult.FAIL;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> list, TooltipFlag flag) {
		list.add(Component.translatable(Lib.DESC_INFO + "shader.level").append(Component.translatable(Lib.DESC_INFO + "shader.rarity." + this.getRarity(stack).name().toLowerCase(Locale.US)).withStyle(this.getRarity(stack).getStyleModifier())));
		if (ShaderRegistry.shaderRegistry.containsKey(this.getShaderName(stack))) {
			if (!Screen.hasShiftDown()) {
				list.add(Component.translatable(Lib.DESC_INFO + "shader.applyTo").append(" ").append(Component.translatable(Lib.DESC_INFO + "holdShift")));
			} else {
				list.add(Component.translatable(Lib.DESC_INFO + "shader.applyTo"));

				for (ShaderCase sCase : ShaderRegistry.shaderRegistry.get(this.getShaderName(stack)).getCases())
					if (!(sCase instanceof ShaderCaseItem))
						list.add(TextUtils.applyFormat(Component.translatable(Lib.DESC_INFO + "shader." + sCase.getShaderType()), ChatFormatting.DARK_GRAY));
			}
		}
	}

	@Override
	public Rarity getRarity(ItemStack stack) {
		return ShaderRegistry.shaderRegistry.containsKey(this.getShaderName(stack)) ? ShaderRegistry.shaderRegistry.get(this.getShaderName(stack)).getRarity() : Rarity.COMMON;
	}

	public static ResourceLocation getShaderType(ItemStack stack) {
		return ItemNBTHelper.hasKey(stack, TAG_SHADER) ? new ResourceLocation(ItemNBTHelper.getString(stack, TAG_SHADER)) : DoIAmEngineering.prefix("twilight");
	}

	public static int getShaderColors(ItemStack stack, int pass) {
		ResourceLocation name = getShaderType(stack);
		if (ShaderRegistry.shaderRegistry.containsKey(name)) {
			ShaderCase sCase = ShaderRegistry.shaderRegistry.get(name).getCase(new ResourceLocation(Lib.MODID, "item"));
			if (sCase != null) {
				ShaderLayer[] layers = sCase.getLayers();
				if (pass < layers.length && layers[pass] != null) {
					return Utils.intFromRGBA(layers[pass].getColor());
				}
				return 0xffffffff;
			}
		}
		return 0xffffffff;
	}

//	@Override
//	public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items) {
//		if (this.allowedIn(tab)) {
//			for (ShaderRegistry.ShaderRegistryEntry entry : IEShaderRegistry.getAllTwilightShaders()) {
//				ItemStack stack = new ItemStack(this);
//				ItemNBTHelper.putString(stack, TAG_SHADER, entry.getName().toString());
//				items.add(stack);
//			}
//		}
//	}
}
