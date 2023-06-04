package com.gizmo.doiamengineering;

import blusunrize.immersiveengineering.api.shader.ShaderLayer;

@FunctionalInterface
public interface ShaderLayerFactory<T extends ShaderLayer> {
    T get(IEShaderRegistry.ModType mod, IEShaderRegistry.CaseType type, String suffix, int color);

    static ShaderLayer[] provideFromFactories(IEShaderRegistry.ModType mod, IEShaderRegistry.CaseType type, String suffix, int color, ShaderLayerFactory<? extends ShaderLayer>[] layerFactories) {
        ShaderLayer[] array = new ShaderLayer[layerFactories.length];

        for (int i = 0; i < layerFactories.length; i++)
            array[i] = layerFactories[i].get(mod, type, suffix, color);

        return array;
    }
}
