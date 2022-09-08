package com.gizmo.doiamengineering.data;

import com.gizmo.doiamengineering.DoIAmEngineering;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DoIAmEngineering.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

	@SubscribeEvent
	public static void gatherData(GatherDataEvent evt) {
		DataGenerator generator = evt.getGenerator();

		generator.addProvider(true, new EntityTagGenerator(generator, evt.getExistingFileHelper()));
		generator.addProvider(true, new ItemModelGenerator(generator, evt.getExistingFileHelper()));
		generator.addProvider(true, new ItemTagGenerator(generator, new BlockTagsProvider(generator, DoIAmEngineering.MODID, evt.getExistingFileHelper()), evt.getExistingFileHelper()));
		generator.addProvider(true, new LootModifierGenerator(generator));
		generator.addProvider(true, new RecipeGenerator(generator));
	}
}
