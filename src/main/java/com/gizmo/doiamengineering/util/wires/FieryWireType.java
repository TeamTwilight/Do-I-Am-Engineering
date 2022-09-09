package com.gizmo.doiamengineering.util.wires;

import blusunrize.immersiveengineering.api.ApiUtils;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class FieryWireType extends WireType implements WireDamageHandler.IShockingWire, EnergyTransferHandler.IEnergyWire {
	@Override
	public String getUniqueName() {
		return "FIERY";
	}

	@Override
	public int getColour(Connection connection) {
		return 0xFBAD24;
	}

	@Override
	public double getSlack() {
		return 1.005D;
	}

	@Override
	public int getMaxLength() {
		return 32;
	}

	@Override
	public ItemStack getWireCoil(Connection con) {
		return new ItemStack(ModRegistry.FIERY_WIRE_COIL.get());
	}

	@Override
	public double getRenderDiameter() {
		return 0.0625D;
	}

	@NotNull
	@Override
	public String getCategory() {
		return HV_CATEGORY;
	}

	@Override
	public double getDamageRadius() {
		return 0.3D;
	}

	@Override
	public IElectricEquipment.ElectricSource getElectricSource() {
		return new IElectricEquipment.ElectricSource(3.0F);
	}

	@Override
	public float getDamageAmount(Entity entity, Connection connection, int energy) {
		if (!entity.fireImmune() && entity instanceof LivingEntity living) {
			living.setSecondsOnFire(15);
			Vec3 vec3 = living.getLookAngle();
			ApiUtils.knockbackNoSource(living, 0.5D, vec3.x(), vec3.z());
		}
		return WireDamageHandler.IShockingWire.super.getDamageAmount(entity, connection, energy);
	}

	@Override
	public int getTransferRate() {
		return 65535;
	}

	@Override
	public double getBasicLossRate(Connection c) {
		return 0.075D * c.getLength() / this.getMaxLength();
	}

	@Override
	public double getLossRate(Connection c, int transferred) {
		return 0.005D;
	}

	@Override
	public Collection<ResourceLocation> getRequestedHandlers()
	{
		return ImmutableList.of(TFWireDamageHandler.ID);
	}
}
