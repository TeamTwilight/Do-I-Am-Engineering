package com.gizmo.doiamengineering.data;

import com.gizmo.doiamengineering.DoIAmEngineering;
import com.gizmo.doiamengineering.ModRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.loaders.ItemLayersModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import static com.gizmo.doiamengineering.DoIAmEngineering.prefix;

public class ItemModelGenerator extends ItemModelProvider {
	public ItemModelGenerator(DataGenerator generator, ExistingFileHelper helper) {
		super(generator, DoIAmEngineering.MODID, helper);
	}

	@Override
	protected void registerModels() {
		singleTex(ModRegistry.FIERY_DUST, true);
		singleTex(ModRegistry.FIERY_NUGGET, true);
		singleTex(ModRegistry.FIERY_PLATE, true);
		singleTex(ModRegistry.FIERY_ROD, true);
		singleTex(ModRegistry.FIERY_WIRE, true);

		singleTex(ModRegistry.IRONWOOD_DUST, false);
		singleTex(ModRegistry.IRONWOOD_NUGGET, false);
		singleTex(ModRegistry.IRONWOOD_PLATE, false);
		singleTex(ModRegistry.IRONWOOD_ROD, false);
		singleTex(ModRegistry.IRONWOOD_WIRE, false);

		singleTex(ModRegistry.KNIGHTMETAL_DUST, false);
		singleTex(ModRegistry.KNIGHTMETAL_PLATE, false);
		singleTex(ModRegistry.KNIGHTMETAL_ROD, false);
		singleTex(ModRegistry.KNIGHTMETAL_WIRE, false);

		withExistingParent("shader", prefix("item/lunchcase"))
				.texture("missing", prefix("block/lunchbox_face"))
				.texture("face", prefix("block/lunchbox_face"))
				.texture("side", prefix("block/lunchbox_side"));

		withExistingParent("shader_bag_common", prefix("item/shader"));
		withExistingParent("shader_bag_uncommon", prefix("item/shader"));
		withExistingParent("shader_bag_rare", prefix("item/shader"));
		withExistingParent("shader_bag_epic", prefix("item/shader"));
		withExistingParent("shader_bag_ie_masterwork", prefix("item/shader"));
		withExistingParent("shader_bag_twilight", prefix("item/shader"));

	}

	private ItemModelBuilder singleTex(RegistryObject<Item> item, boolean fullbright) {
		return generated(item.getId().getPath(), fullbright, prefix("items/" + item.getId().getPath()));
	}

	private ItemModelBuilder generated(String name, boolean fullbright, ResourceLocation... layers) {
		return buildItem(name, "item/generated", fullbright, layers);
	}

	private ItemModelBuilder buildItem(String name, String parent, boolean fullbright, ResourceLocation... layers) {
		ItemModelBuilder builder = withExistingParent(name, parent);
		for (int i = 0; i < layers.length; i++) {
			builder = builder.texture("layer" + i, layers[i]);
		}
		if (fullbright) builder = builder.customLoader(ItemLayersModelBuilder::begin).emissive(0).end();
		return builder;
	}
}
