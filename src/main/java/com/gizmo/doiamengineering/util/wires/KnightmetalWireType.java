package com.gizmo.doiamengineering.util.wires;

import blusunrize.immersiveengineering.api.tool.IElectricEquipment;
import blusunrize.immersiveengineering.api.wires.Connection;
import blusunrize.immersiveengineering.api.wires.WireType;
import blusunrize.immersiveengineering.api.wires.localhandlers.EnergyTransferHandler;
import blusunrize.immersiveengineering.api.wires.localhandlers.WireDamageHandler;
import com.gizmo.doiamengineering.ModRegistry;
import com.google.common.collect.ImmutableList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class KnightmetalWireType extends WireType implements WireDamageHandler.IShockingWire, EnergyTransferHandler.IEnergyWire {
	@Override
	public String getUniqueName() {
		return "KNIGHTMETAL";
	}

	@Override
	public int getColour(Connection connection) {
		return 0xC4D6AE;
	}

	@Override
	public double getSlack() {
		return 1.005D;
	}

	@Override
	public int getMaxLength() {
		return 16;
	}

	@Override
	public ItemStack getWireCoil(Connection con) {
		return new ItemStack(ModRegistry.KNIGHTMETAL_WIRE_COIL.get());
	}

	@Override
	public double getRenderDiameter() {
		return 0.0625D;
	}

	@NotNull
	@Override
	public String getCategory() {
		return MV_CATEGORY;
	}

	@Override
	public double getDamageRadius() {
		return 0.15D;
	}

	@Override
	public float getDamageAmount(Entity entity, Connection connection, int energy) {
		if (entity instanceof LivingEntity living) {
			living.getArmorSlots().forEach(stack -> {
				if (!stack.isEmpty() && (stack.getItem() instanceof ArmorItem || stack.getEquipmentSlot() != null))
					stack.hurtAndBreak(1, living, entity1 -> entity1.broadcastBreakEvent(stack.getItem() instanceof ArmorItem armor ? armor.getSlot() : stack.getEquipmentSlot()));
			});
			return 2.0F + (living.getArmorCoverPercentage() * 4.0F);
		}
		return WireDamageHandler.IShockingWire.super.getDamageAmount(entity, connection, energy);
	}

	@Override
	public IElectricEquipment.ElectricSource getElectricSource() {
		return new IElectricEquipment.ElectricSource(2.0F);
	}

	@Override
	public int getTransferRate() {
		return 8192;
	}

	@Override
	public double getBasicLossRate(Connection connection) {
		return 0.05D * connection.getLength() / this.getMaxLength();
	}

	@Override
	public double getLossRate(Connection c, int transferred) {
		return 0;
	}

	@Override
	public Collection<ResourceLocation> getRequestedHandlers() {
		return ImmutableList.of(TFWireDamageHandler.ID);
	}
}
