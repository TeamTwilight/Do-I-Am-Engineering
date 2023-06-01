package com.gizmo.doiamengineering.data;

import com.gizmo.doiamengineering.DoIAmEngineering;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DoIAmEngineering.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

	@SubscribeEvent
	public static void gatherData(GatherDataEvent evt) {
		DataGenerator generator = evt.getGenerator();

		generator.addProvider(evt.includeServer(), new EntityTagGenerator(generator.getPackOutput(), evt.getLookupProvider(), evt.getExistingFileHelper()));
		generator.addProvider(evt.includeClient(), new ItemModelGenerator(generator.getPackOutput(), evt.getExistingFileHelper()));
		BlockTagGenerator blockTags = new BlockTagGenerator(generator.getPackOutput(), evt.getLookupProvider(), evt.getExistingFileHelper());
		generator.addProvider(evt.includeServer(), blockTags);
		generator.addProvider(evt.includeServer(), new ItemTagGenerator(generator.getPackOutput(), evt.getLookupProvider(), blockTags.contentsGetter(), evt.getExistingFileHelper()));
		generator.addProvider(evt.includeServer(), new LootModifierGenerator(generator.getPackOutput()));
		generator.addProvider(evt.includeServer(), new RecipeGenerator(generator.getPackOutput()));
		generator.addProvider(evt.includeServer(), new RegistryDataGenerator(generator.getPackOutput(), evt.getLookupProvider()));
	}
}
