package com.mraof.minestuck.event;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.ModBlockEntities;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventClientBusEvents {
	@SubscribeEvent
	public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
	}
	
	@SubscribeEvent
	public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
		
		event.registerBlockEntityRenderer(ModBlockEntities.MOD_SIGN.get(), SignRenderer::new);
		event.registerBlockEntityRenderer(ModBlockEntities.MOD_HANGING_SIGN.get(), HangingSignRenderer::new);
	}
	
	
}