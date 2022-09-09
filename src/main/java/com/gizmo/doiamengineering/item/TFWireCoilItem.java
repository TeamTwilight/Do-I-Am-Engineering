package com.gizmo.doiamengineering.item;

import blusunrize.immersiveengineering.api.wires.WireType;
import blusunrize.immersiveengineering.common.items.WireCoilItem;
import com.gizmo.doiamengineering.ModRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFItems;

import java.util.List;

public class TFWireCoilItem extends WireCoilItem {
	public TFWireCoilItem(@NotNull WireType type) {
		super(type);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> list, TooltipFlag flag) {
		if(stack.is(ModRegistry.FIERY_WIRE_COIL.get())) {
			list.add(Component.translatable("item.doiamengineering.fiery_wire_coil.desc").withStyle(ChatFormatting.GRAY));
			list.add(Component.translatable("item.doiamengineering.fiery_wire_coil.desc1").withStyle(ChatFormatting.GRAY));
		} else if(stack.is(ModRegistry.IRONWOOD_WIRE_COIL.get())) {
			list.add(Component.translatable("item.doiamengineering.ironwood_wire_coil.desc").withStyle(ChatFormatting.GRAY));
			list.add(Component.translatable("item.doiamengineering.ironwood_wire_coil.desc1").withStyle(ChatFormatting.GRAY));
		} else if(stack.is(ModRegistry.KNIGHTMETAL_WIRE_COIL.get())) {
			list.add(Component.translatable("item.doiamengineering.knightmetal_wire_coil.desc").withStyle(ChatFormatting.GRAY));
			list.add(Component.translatable("item.doiamengineering.knightmetal_wire_coil.desc1").withStyle(ChatFormatting.GRAY));
		}

		super.appendHoverText(stack, world, list, flag);
	}

	@Override
	public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items) {
		if (tab == TFItems.creativeTab || this.allowedIn(tab)) {
			items.add(new ItemStack(this));
		}
	}
}
