package com.gizmo.doiamengineering.client;

import blusunrize.immersiveengineering.api.shader.DynamicShaderLayer;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class ShaderGLLayer extends DynamicShaderLayer {
    private final String prefix;
    private final RenderType.CompositeState compositeState;
    private final Function<RenderType, RenderType> renderTypeFactory;

    public ShaderGLLayer(ResourceLocation texture, int color, RenderType.CompositeState compositeState) {
        super(texture, color);
        // TODO Find out if this prefixing is better eliminated
        this.prefix = "diae_shader_" + texture.toShortLanguageKey().replace('.', '_').replace('/', '_') + "_";
        this.compositeState = compositeState;
        this.renderTypeFactory = Util.memoize(this::createRenderType);
    }

    @Override
    public RenderType getRenderType(RenderType baseType) {
        return this.renderTypeFactory.apply(baseType);
    }

    private RenderType createRenderType(RenderType baseType) {
        return new RenderType(
                this.prefix + baseType,
                DefaultVertexFormat.NEW_ENTITY,
                VertexFormat.Mode.QUADS,
                256,
                false,
                true,
                () -> {
                    baseType.setupRenderState();
                    this.compositeState.states.forEach(RenderStateShard::setupRenderState);
                }, () -> {
                    this.compositeState.states.forEach(RenderStateShard::clearRenderState);
                    baseType.clearRenderState();
                }
        ) { };
    }
}
