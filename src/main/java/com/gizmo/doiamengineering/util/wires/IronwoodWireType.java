package com.gizmo.doiamengineering.util.wires;

import blusunrize.immersiveengineering.api.wires.Connection;
import blusunrize.immersiveengineering.api.wires.WireType;
import blusunrize.immersiveengineering.api.wires.localhandlers.EnergyTransferHandler;
import com.gizmo.doiamengineering.ModRegistry;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class IronwoodWireType extends WireType implements EnergyTransferHandler.IEnergyWire {
	@Override
	public String getUniqueName() {
		return "IRONWOOD";
	}

	@Override
	public int getColour(Connection connection) {
		return 0x544339;
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
		return new ItemStack(ModRegistry.IRONWOOD_WIRE_COIL.get());
	}

	@Override
	public double getRenderDiameter() {
		return 0.045D;
	}

	@NotNull
	@Override
	public String getCategory() {
		return LV_CATEGORY;
	}

	@Override
	public int getTransferRate() {
		return 2048;
	}

	@Override
	public double getBasicLossRate(Connection c) {
		return 0.075D * c.getLength() / this.getMaxLength();
	}

	@Override
	public double getLossRate(Connection c, int transferred) {
		return 0;
	}
}
