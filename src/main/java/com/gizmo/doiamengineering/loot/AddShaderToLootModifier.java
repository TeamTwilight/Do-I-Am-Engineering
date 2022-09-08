package com.gizmo.doiamengineering.loot;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import com.gizmo.doiamengineering.ModRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import twilightforest.TwilightForestMod;

public class AddShaderToLootModifier extends LootModifier {

	public static final Codec<AddShaderToLootModifier> CODEC = RecordCodecBuilder.create(inst -> LootModifier.codecStart(inst).and(
			inst.group(
					ResourceLocation.CODEC.fieldOf("shader_name").forGetter(m -> m.shader_name),
					Codec.BOOL.fieldOf("addShaderBag").orElse(false).forGetter(m -> m.addShaderBagToo))
	).apply(inst, AddShaderToLootModifier::new));

	private final ResourceLocation shader_name;
	private final boolean addShaderBagToo;

	public AddShaderToLootModifier(LootItemCondition[] conditions, ResourceLocation name, boolean addShaderBagToo) {
		super(conditions);
		this.shader_name = name;
		this.addShaderBagToo = addShaderBagToo;
	}

	@NotNull
	@Override
	protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
		ItemStack shader = new ItemStack(ModRegistry.SHADER.get());
		ItemNBTHelper.putString(shader, "shader_name", this.shader_name.toString());
		generatedLoot.add(shader);

		if (this.addShaderBagToo)
			generatedLoot.add(new ItemStack(ModRegistry.SHADER_BAGS.get(TwilightForestMod.getRarity()).get()));

		return generatedLoot;
	}

	@Override
	public Codec<? extends IGlobalLootModifier> codec() {
		return CODEC;
	}
}
