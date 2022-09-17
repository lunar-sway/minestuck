package com.mraof.minestuck.event;

import com.mojang.blaze3d.shaders.FogShape;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.client.gui.ColorSelectorScreen;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.fluid.IMSFog;
import com.mraof.minestuck.inventory.ConsortMerchantContainer;
import com.mraof.minestuck.player.ClientPlayerData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
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
					event.getToolTip().add(new TranslatableComponent(name, arg1));
				else event.getToolTip().add(stack.getHoverName());
				if(I18n.exists(tooltip))
					event.getToolTip().add(new TranslatableComponent(tooltip, arg1).withStyle(ChatFormatting.GRAY));
			} else if(stack.getItem().getRegistryName().getNamespace().equals(Minestuck.MOD_ID))
			{
				String name = stack.getDescriptionId() + ".tooltip";
				if(I18n.exists(name))
					event.getToolTip().add(1, new TranslatableComponent(name).withStyle(ChatFormatting.GRAY));
			}
		}
	}
	
	@SubscribeEvent
	public static void onFogRender(EntityViewRenderEvent.RenderFogEvent event)
	{
		// Don't change fog distance for spectators
		if(event.getCamera().getEntity().isSpectator())
			return;
		
		LevelReader level = event.getCamera().getEntity().level;
		BlockPos blockPos = event.getCamera().getBlockPosition();
		Vec3 pos = event.getCamera().getPosition();
		FluidState fluid = level.getFluidState(blockPos);
		
		if (!fluid.isEmpty() && pos.y >= blockPos.getY() + fluid.getHeight(level, blockPos))
			return;
		
		if (fluid.createLegacyBlock().getBlock() instanceof IMSFog fog)
		{
			float fogDensity = fog.getMSFogDensity();
			
			event.setCanceled(true);
			event.setNearPlaneDistance(-8);
			event.setFarPlaneDistance(4.8F/fogDensity);
			event.setFogShape(FogShape.SPHERE);
		}
		
	}
	
	/**
	 * used to changes colors of the fog overlay
	 */
	@SubscribeEvent
	public static void addFogColor(EntityViewRenderEvent.FogColors event)
	{
		LevelReader level = event.getCamera().getEntity().level;
		BlockPos blockPos = event.getCamera().getBlockPosition();
		Vec3 pos = event.getCamera().getPosition();
		FluidState fluid = level.getFluidState(blockPos);
		
		if (!fluid.isEmpty() && pos.y >= blockPos.getY() + fluid.getHeight(level, blockPos))
			return;
		
		if(fluid.createLegacyBlock().getBlock() instanceof IMSFog fog)
		{
			Entity entity = event.getCamera().getEntity();
			Vec3 originalColor = new Vec3(event.getRed(), event.getGreen(), event.getBlue());
			float partialTick = (float) event.getPartialTicks();
			
			Vec3 fogColor = fog.getMSFogColor(level, blockPos, entity, originalColor, partialTick);
			
			event.setRed((float) fogColor.x());
			event.setGreen((float) fogColor.y());
			event.setBlue((float) fogColor.z());
		}
	}
}
