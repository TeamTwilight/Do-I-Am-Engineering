package com.gizmo.doiamengineering.data;

import blusunrize.immersiveengineering.api.IETags;
import com.gizmo.doiamengineering.DoIAmEngineering;
import com.gizmo.doiamengineering.ModRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFEntities;

import java.util.concurrent.CompletableFuture;

public class EntityTagGenerator extends EntityTypeTagsProvider {
	public EntityTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> future, @Nullable ExistingFileHelper helper) {
		super(output, future, DoIAmEngineering.MODID, helper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		tag(EntityTypeTags.IMPACT_PROJECTILES).add(ModRegistry.CICADA_SHOT.get());

		tag(IETags.shaderbagBlacklist).add(
				TFEntities.NAGA.get(), TFEntities.LICH.get(),
				TFEntities.MINOSHROOM.get(), TFEntities.HYDRA.get(),
				TFEntities.KNIGHT_PHANTOM.get(), TFEntities.UR_GHAST.get(),
				TFEntities.ALPHA_YETI.get(), TFEntities.SNOW_QUEEN.get(),
				TFEntities.QUEST_RAM.get()
		);

		tag(twilightforest.data.tags.EntityTagGenerator.DONT_KILL_BUGS).add(ModRegistry.CICADA_SHOT.get());
	}
}
