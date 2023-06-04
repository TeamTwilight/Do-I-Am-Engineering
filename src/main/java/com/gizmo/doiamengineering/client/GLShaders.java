package com.gizmo.doiamengineering.client;

import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.blockentity.TheEndPortalRenderer;
import net.minecraft.world.inventory.InventoryMenu;
import org.jetbrains.annotations.NotNull;

public class GLShaders extends RenderStateShard {
    static ShaderInstance twilightSky;

    public GLShaders() {
        super(null, null, null);
        throw new UnsupportedOperationException("Do not use this constructor!");
    }

    public static ShaderInstance getTwilightSky() {
        return twilightSky;
    }

    @NotNull
    public static RenderType.CompositeState getCompositeState() {
        return RenderType.CompositeState.builder()
                .setTextureState(MultiTextureStateShard.builder().add(InventoryMenu.BLOCK_ATLAS, false, false).add(TheEndPortalRenderer.END_PORTAL_LOCATION, false, false).build())
                .setShaderState(new ShaderStateShard(GLShaders::getTwilightSky))
                .setTransparencyState(NO_TRANSPARENCY)
                .setCullState(NO_CULL)
                .setLightmapState(NO_LIGHTMAP)
                .setOverlayState(NO_OVERLAY)
                .createCompositeState(false);
    }
}
