package com.gizmo.doiamengineering.client;

import com.google.common.base.Suppliers;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.blockentity.TheEndPortalRenderer;
import net.minecraft.world.inventory.InventoryMenu;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class GLShaders extends RenderStateShard {
    static ShaderInstance aurora;
    static ShaderInstance emissive;
    static ShaderInstance twilightSky;

    public static Supplier<RenderType.CompositeState> emissiveComposite = Suppliers.memoize(() -> getCompositeState(GLShaders::getEmissive));

    public GLShaders() {
        super(null, null, null);
        throw new UnsupportedOperationException("Do not use this constructor!");
    }

    public static ShaderInstance getAurora() {
        return aurora;
    }

    public static ShaderInstance getEmissive() {
        return emissive;
    }

    public static ShaderInstance getTwilightSky() {
        return twilightSky;
    }

    @NotNull
    public static RenderType.CompositeState getCompositeState(Supplier<ShaderInstance> shader) {
        return RenderType.CompositeState.builder()
                .setTextureState(MultiTextureStateShard.builder().add(InventoryMenu.BLOCK_ATLAS, false, false).add(TheEndPortalRenderer.END_PORTAL_LOCATION, false, false).build())
                .setShaderState(new ShaderStateShard(shader))
                .setTransparencyState(NO_TRANSPARENCY)
                .setCullState(NO_CULL)
                .setLightmapState(NO_LIGHTMAP)
                .setOverlayState(NO_OVERLAY)
                .createCompositeState(false);
    }
}
