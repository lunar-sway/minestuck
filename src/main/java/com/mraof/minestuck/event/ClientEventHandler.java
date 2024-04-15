package com.mraof.minestuck.event;

import com.mojang.blaze3d.shaders.FogShape;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.client.gui.ColorSelectorScreen;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.fluid.IMSFog;
import com.mraof.minestuck.inventory.ConsortMerchantMenu;
import com.mraof.minestuck.player.ClientPlayerData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

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
			if(event.getEntity() != null && event.getEntity().containerMenu instanceof ConsortMerchantMenu
					&& event.getEntity().containerMenu.getItems().contains(stack))
			{
				String unlocalized = stack.getDescriptionId();
				
				EnumConsort type = ((ConsortMerchantMenu)event.getEntity().containerMenu).getConsortType();
				String arg1 = I18n.get(type.getConsortType().getDescriptionId());
				
				String name = "store."+unlocalized;
				String tooltip = "store."+unlocalized+".tooltip";
				event.getToolTip().clear();
				if(I18n.exists(name))
					event.getToolTip().add(Component.translatable(name, arg1));
				else event.getToolTip().add(stack.getHoverName());
				if(I18n.exists(tooltip))
					event.getToolTip().add(Component.translatable(tooltip, arg1).withStyle(ChatFormatting.GRAY));
			} else {
				final ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
				if(itemId != null && itemId.getNamespace().equals(Minestuck.MOD_ID))
				{
					String name = stack.getDescriptionId() + ".tooltip";
					if(I18n.exists(name))
						event.getToolTip().add(1, Component.translatable(name).withStyle(ChatFormatting.GRAY));
				}
			}
		}
	}
	
	//TODO these two events can now be replaced using IClientFluidTypeExtensions
	@SubscribeEvent
	public static void onFogRender(ViewportEvent.RenderFog event)
	{
		// Don't change fog distance for spectators
		if(event.getCamera().getEntity().isSpectator())
			return;
		
		LevelReader level = event.getCamera().getEntity().level();
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
	public static void addFogColor(ViewportEvent.ComputeFogColor event)
	{
		LevelReader level = event.getCamera().getEntity().level();
		BlockPos blockPos = event.getCamera().getBlockPosition();
		Vec3 pos = event.getCamera().getPosition();
		FluidState fluid = level.getFluidState(blockPos);
		
		if (!fluid.isEmpty() && pos.y >= blockPos.getY() + fluid.getHeight(level, blockPos))
			return;
		
		if(fluid.createLegacyBlock().getBlock() instanceof IMSFog fog)
		{
			Entity entity = event.getCamera().getEntity();
			Vec3 originalColor = new Vec3(event.getRed(), event.getGreen(), event.getBlue());
			float partialTick = (float) event.getPartialTick();
			
			Vec3 fogColor = fog.getMSFogColor(level, blockPos, entity, originalColor, partialTick);
			
			event.setRed((float) fogColor.x());
			event.setGreen((float) fogColor.y());
			event.setBlue((float) fogColor.z());
		}
	}
}
