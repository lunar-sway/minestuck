package com.mraof.minestuck.client.renderer.effects;

import com.mojang.authlib.GameProfile;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.effects.MSEffects;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderArmEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ArtifactEffectRenderer extends PlayerRenderer
{
	private static final ResourceLocation TEXTURE = new ResourceLocation(Minestuck.MOD_ID, "textures/mob_effect/artifacted_skin.png");
	private boolean skipEvent;
	
	public ArtifactEffectRenderer(EntityRendererManager entityRendererManager)
	{
		super(entityRendererManager);
	}
	
	@SubscribeEvent
	public void onRenderPlayer(RenderPlayerEvent event)
	{
		if(!event.getPlayer().hasEffect(MSEffects.ARTIFACTED.get()))
		{
			return;
		}
		
		if(skipEvent)
			return;
		skipEvent = true;
		
		
		event.setCanceled(true);
		this.render((AbstractClientPlayerEntity) event.getPlayer(), event.getPlayer().getYHeadRot(), event.getPartialRenderTick(), event.getMatrixStack(), event.getBuffers(), event.getLight());
		skipEvent = false;
	}
	
	@Override
	public ResourceLocation getTextureLocation(AbstractClientPlayerEntity pEntity)
	{
		return TEXTURE;
	}
	
	@SubscribeEvent
	public void onRenderArm(RenderArmEvent event)
	{
		if(!event.getPlayer().hasEffect(MSEffects.ARTIFACTED.get()))
		{
			return;
		}
		
		if(skipEvent)
			return;
		skipEvent = true;
		
		TexturedClientPlayerEntity player = new TexturedClientPlayerEntity((ClientWorld) event.getPlayer().level, event.getPlayer().getGameProfile());
		event.setCanceled(true);
		this.renderRightHand(event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight(), player);
		skipEvent = false;
	}
	
	private class TexturedClientPlayerEntity extends AbstractClientPlayerEntity
	{
		
		public TexturedClientPlayerEntity(ClientWorld pClientLevel, GameProfile pGameProfile)
		{
			super(pClientLevel, pGameProfile);
		}
		
		@Override
		public ResourceLocation getSkinTextureLocation()
		{
			return TEXTURE;
		}
	}
}
