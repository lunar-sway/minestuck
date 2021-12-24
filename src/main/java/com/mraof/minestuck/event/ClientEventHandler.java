package com.mraof.minestuck.event;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.client.gui.ColorSelectorScreen;
import com.mraof.minestuck.effects.CreativeShockEffect;
import com.mraof.minestuck.effects.MSEffects;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.fluid.IMSFog;
import com.mraof.minestuck.inventory.ConsortMerchantContainer;
import com.mraof.minestuck.world.storage.ClientPlayerData;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Used to track mixed client sided events.
 */
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEventHandler
{
	@SubscribeEvent
	public static void onClientTick(TickEvent.ClientTickEvent event)
	{
		if(event.phase == TickEvent.Phase.END)
		{
			if(ClientPlayerData.shouDisplayColorSelection() && Minecraft.getInstance().screen == null)
			{
				ClientPlayerData.clearDisplayColorSelection();
				if(MinestuckConfig.CLIENT.loginColorSelector.get())
					Minecraft.getInstance().setScreen(new ColorSelectorScreen(true));
			}
			
		}
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public static void addCustomTooltip(ItemTooltipEvent event)
	{
		//Add config check
		{
			ItemStack stack = event.getItemStack();
			if(event.getPlayer() != null && event.getPlayer().containerMenu instanceof ConsortMerchantContainer
					&& event.getPlayer().containerMenu.getItems().contains(stack))
			{
				String unlocalized = stack.getDescriptionId();
				
				EnumConsort type = ((ConsortMerchantContainer)event.getPlayer().containerMenu).getConsortType();
				String arg1 = I18n.get(type.getConsortType().getDescriptionId());
				
				String name = "store."+unlocalized;
				String tooltip = "store."+unlocalized+".tooltip";
				event.getToolTip().clear();
				if(I18n.exists(name))
					event.getToolTip().add(new TranslationTextComponent(name, arg1));
				else event.getToolTip().add(stack.getHoverName());
				if(I18n.exists(tooltip))
					event.getToolTip().add(new TranslationTextComponent(tooltip, arg1).withStyle(TextFormatting.GRAY));
			} else if(stack.getItem().getRegistryName().getNamespace().equals(Minestuck.MOD_ID))
			{
				String name = stack.getDescriptionId() + ".tooltip";
				if(I18n.exists(name))
					event.getToolTip().add(1, new TranslationTextComponent(name).withStyle(TextFormatting.GRAY));
			}
		}
	}
	
	/**
	 *Used to change the density on of the Fog overlay
	 */
	@SubscribeEvent
	public static void onFogRender(EntityViewRenderEvent.FogDensity event)
	{
		if (event.getInfo().getFluidInCamera().createLegacyBlock().getBlock() instanceof IMSFog)
		{
			IMSFog fog = (IMSFog)event.getInfo().getFluidInCamera().createLegacyBlock().getBlock();
			float fogDensity = fog.getMSFogDensity();
			
			event.setCanceled(true);
			event.setDensity(fogDensity);
			RenderSystem.fogMode(GlStateManager.FogMode.EXP);
		}
	}
	
	/**
	 * used to changes colors of the fog overlay
	 */
	@SubscribeEvent
	public static void addFogColor(EntityViewRenderEvent.FogColors event)
	{
		BlockState state = event.getInfo().getFluidInCamera().createLegacyBlock();
		IWorldReader world = event.getInfo().getEntity().level;
		BlockPos pos = event.getInfo().getBlockPosition();
		Entity entity = event.getInfo().getEntity();
		Vector3d originalColor = new Vector3d(event.getRed(), event.getGreen(), event.getBlue());
		float partialTick = (float) (event.getRenderPartialTicks());
		
		if(state.getBlock() instanceof IMSFog)
		{
			IMSFog fog = (IMSFog) (state.getBlock());
			Vector3d fogColor = fog.getMSFogColor(state, world, pos, entity, originalColor, partialTick);
			
			event.setRed((float) fogColor.x());
			event.setGreen((float) fogColor.y());
			event.setBlue((float) fogColor.z());
		}
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public static void onLeftClickBlockEvent(PlayerInteractEvent.LeftClickBlock event)
	{
		if(event.getEntity() instanceof PlayerEntity)
		{
			PlayerEntity playerEntity = (PlayerEntity) event.getEntity();
			if(CreativeShockEffect.doesCreativeShockLimit(playerEntity, 0, 3))
				event.setCanceled(true); //It is necessary to do this here in order to prevent creative mode players from breaking blocks
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOW)
	public static void onPlayerTickEvent(TickEvent.PlayerTickEvent event)
	{
		if(event.player.hasEffect(MSEffects.CREATIVE_SHOCK.get()))
		{
			if(CreativeShockEffect.doesCreativeShockLimit(event.player, 2, 5))
				event.player.stopFallFlying(); //Stopping elytra moment on client side prevent visual disruptions
		}
	}
}
