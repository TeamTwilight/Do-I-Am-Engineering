package com.gizmo.doiamengineering.item;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.shader.ShaderRegistry;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import com.gizmo.doiamengineering.IEShaderRegistry;
import com.gizmo.doiamengineering.ModRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Locale;

public class TFShaderBagItem extends Item {

	@Nonnull
	private final Rarity rarity;

	public TFShaderBagItem(Rarity rarity, Properties properties) {
		super(properties);
		this.rarity = rarity;
	}

	@Override
	public Rarity getRarity(ItemStack stack) {
		return rarity;
	}

	@Override
	public Component getName(ItemStack stack) {
		return (Component.translatable("item.doiamengineering.shader_bag", Component.translatable(Lib.DESC_INFO + "shader.rarity." + rarity.name().toLowerCase(Locale.US))));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (!level.isClientSide()) {
			if (this.rarity == TwilightForestMod.getRarity()) {
				List<ShaderRegistry.ShaderRegistryEntry> list = IEShaderRegistry.getAllNonbossShaders();
				return randomShader(list.get(player.getRandom().nextInt(list.size())).name, stack, player);
			} else {
				return randomShader(ShaderRegistry.getRandomShader(player.getUUID(), player.getRandom(), this.rarity, true), stack, player);
			}
		}
		return new InteractionResultHolder<>(InteractionResult.PASS, stack);
	}

	private static InteractionResultHolder<ItemStack> randomShader(@Nullable ResourceLocation shader, ItemStack stack, Player player) {
		if (shader == null || shader.getPath().isEmpty())
			return new InteractionResultHolder<>(InteractionResult.FAIL, stack);

		ItemStack shaderItem = new ItemStack(ModRegistry.SHADER.get());
		ItemNBTHelper.putString(shaderItem, TFShaderItem.TAG_SHADER, shader.toString());

		stack.shrink(1);

		if (stack.getCount() <= 0)
			return new InteractionResultHolder<>(InteractionResult.SUCCESS, shaderItem);

		ItemHandlerHelper.giveItemToPlayer(player, shaderItem);

		return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
	}
}
