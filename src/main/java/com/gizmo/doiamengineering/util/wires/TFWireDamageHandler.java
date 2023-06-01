package com.gizmo.doiamengineering.util.wires;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.wires.*;
import blusunrize.immersiveengineering.api.wires.localhandlers.EnergyTransferHandler;
import blusunrize.immersiveengineering.api.wires.localhandlers.WireDamageHandler;
import blusunrize.immersiveengineering.api.wires.utils.IElectricDamageSource;
import com.gizmo.doiamengineering.DoIAmEngineering;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TFWireDamageHandler extends WireDamageHandler {

	public static final ResourceLocation ID = new ResourceLocation(DoIAmEngineering.MODID, "wire_damage");

	public TFWireDamageHandler(LocalWireNetwork net, GlobalWireNetwork global) {
		super(net, global);
	}

	// Copy of WireDamageHandler.onCollided, we just need to handle a special case.
	// I wrote a comment above it.
	@Override
	public void onCollided(LivingEntity entity, BlockPos pos, WireCollisionData.CollisionInfo info) {
		WireType wType = info.connection().type;
		if (!(wType instanceof IShockingWire shockWire))
			return;
		EnergyTransferHandler energyHandler = getEnergyHandler();
		if (energyHandler == null)
			return;
		double extra = shockWire.getDamageRadius();
		AABB eAabb = entity.getBoundingBox();
		AABB includingExtra = eAabb.inflate(extra).move(-pos.getX(), -pos.getY(), -pos.getZ());
		boolean collides = includingExtra.contains(info.intersectA()) || includingExtra.contains(info.intersectB());
		if (!collides && includingExtra.clip(info.intersectA(), info.intersectB()).isEmpty())
			return;
		final ConnectionPoint target = info.connection().getEndA();
		final List<SourceData> available = getAvailableEnergy(energyHandler, target);
		if (available.isEmpty()) {
			//our wires have some special damage handling if they don't have power, so that's what this bit is for
			final float maxPossibleDamage = shockWire.getDamageAmount(entity, info.connection(), 0);
			if (maxPossibleDamage <= 0)
				return;
			IElectricDamageSource dmg = GET_WIRE_DAMAGE.getValue().make(entity.getLevel(), maxPossibleDamage, shockWire.getElectricSource());
			if (!dmg.apply(entity))
				return;
			final float actualDamage = dmg.getDamage();
			Vec3 v = entity.getLookAngle();
			ApiUtils.knockbackNoSource(entity, actualDamage / 10, v.x(), v.z());
		} else {
			int totalAvailable = 0;
			for (SourceData source : available)
				totalAvailable += source.amountAvailable * (1 - source.pathToSource.loss);
			totalAvailable = Math.min(totalAvailable, shockWire.getTransferRate());

			final float maxPossibleDamage = shockWire.getDamageAmount(entity, info.connection(), totalAvailable);
			if (maxPossibleDamage <= 0)
				return;
			IElectricDamageSource dmg = GET_WIRE_DAMAGE.getValue().make(entity.getLevel(), maxPossibleDamage, shockWire.getElectricSource());
			if (!dmg.apply(entity))
				return;
			final float actualDamage = dmg.getDamage();
			Vec3 v = entity.getLookAngle();
			ApiUtils.knockbackNoSource(entity, actualDamage / 10, v.x, v.z);
			//Consume energy
			final double factor = actualDamage / maxPossibleDamage;
			Object2DoubleMap<Connection> transferred = energyHandler.getTransferredNextTick();
			for (SourceData source : available) {
				final double energyFromSource = source.amountAvailable * factor;
				source.source.extractEnergy(Mth.ceil(energyFromSource));
				for (Connection c : source.pathToSource.conns)
					transferred.mergeDouble(c, energyFromSource, Double::sum);
			}
		}
	}

	private record SourceData(int amountAvailable, EnergyTransferHandler.Path pathToSource,
							  EnergyTransferHandler.EnergyConnector source) {
	}

	private List<SourceData> getAvailableEnergy(EnergyTransferHandler energyHandler, ConnectionPoint target) {
		List<SourceData> ret = new ArrayList<>();
		Map<ConnectionPoint, EnergyTransferHandler.Path> paths = null;
		for (Map.Entry<ConnectionPoint, EnergyTransferHandler.EnergyConnector> c : energyHandler.getSources().entrySet()) {
			final int energy = c.getValue().getAvailableEnergy();
			if (energy <= 0)
				continue;
			if (paths == null)
				paths = energyHandler.getPathsFromSource(target);
			final EnergyTransferHandler.Path path = paths.get(c.getKey());
			if (path != null)
				ret.add(new SourceData(energy, path, c.getValue()));
		}
		return ret;
	}

	private EnergyTransferHandler getEnergyHandler() {
		return localNet.getHandler(EnergyTransferHandler.ID, EnergyTransferHandler.class);
	}
}
