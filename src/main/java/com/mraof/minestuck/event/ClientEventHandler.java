package com.mraof.minestuck.event;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.FlowingModFluidBlock;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.client.gui.ColorSelectorScreen;
import com.mraof.minestuck.data.MinestuckFluidTagsProvider;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.fluid.MSFluids;
import com.mraof.minestuck.inventory.ConsortMerchantContainer;
import com.mraof.minestuck.util.MSTags;
import com.mraof.minestuck.world.storage.ClientPlayerData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
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
			if(ClientPlayerData.displaySelectionGui && Minecraft.getInstance().currentScreen == null)
			{
				ClientPlayerData.displaySelectionGui = false;
				if(MinestuckConfig.loginColorSelector.get())
					Minecraft.getInstance().displayGuiScreen(new ColorSelectorScreen(true));
			}
			
		}
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public static void addCustomTooltip(ItemTooltipEvent event)
	{
		//Add config check
		{
			ItemStack stack = event.getItemStack();
			if(event.getPlayer() != null && event.getPlayer().openContainer instanceof ConsortMerchantContainer
					&& event.getPlayer().openContainer.getInventory().contains(stack))
			{
				String unlocalized = stack.getTranslationKey();
				
				EnumConsort type = ((ConsortMerchantContainer)event.getPlayer().openContainer).getConsortType();
				String arg1 = I18n.format(type.getConsortType().getTranslationKey());
				
				String name = "store."+unlocalized;
				String tooltip = "store."+unlocalized+".tooltip";
				event.getToolTip().clear();
				if(I18n.hasKey(name))
					event.getToolTip().add(new TranslationTextComponent(name, arg1));
				else event.getToolTip().add(stack.getDisplayName());
				if(I18n.hasKey(tooltip))
					event.getToolTip().add(new TranslationTextComponent(tooltip, arg1).setStyle(new Style().setColor(TextFormatting.GRAY)));
			} else if(stack.getItem().getRegistryName().getNamespace().equals(Minestuck.MOD_ID))
			{
				String name = stack.getTranslationKey() + ".tooltip";
				if(I18n.hasKey(name))
					event.getToolTip().add(1, new TranslationTextComponent(name).setStyle(new Style().setColor(TextFormatting.GRAY)));
			}
		}
	}
	
	@SubscribeEvent
	public static void onFogRender(EntityViewRenderEvent.FogDensity event)
	{
		if (event.getInfo().getFluidState().getFluid() == MSFluids.ENDER.get() || event.getInfo().getFluidState().getFluid() == MSFluids.FLOWING_ENDER.get())
		{
			event.setCanceled(true);
			event.setDensity(Float.MAX_VALUE);
			GlStateManager.fogMode(GlStateManager.FogMode.EXP);
		}
	}
	@SubscribeEvent
	public static void addFogColor(EntityViewRenderEvent.FogColors event)
	{
		BlockState state = event.getInfo().getFluidState().getBlockState();
		IWorldReader world = event.getInfo().getRenderViewEntity().world;
		BlockPos pos = event.getInfo().getBlockPos();
		Entity entity = event.getInfo().getRenderViewEntity();
		Vec3d originalColor = new Vec3d(event.getRed(), event.getGreen(), event.getBlue());
		float partialTick = (float) (event.getRenderPartialTicks());
		
		Vec3d fogColor = event.getInfo().getFluidState().getBlockState().getBlock().getFogColor(state, world, pos, entity, originalColor, partialTick);
		
		if(event.getInfo().getFluidState().getBlockState().getBlock().getFogColor(state, world, pos, entity, originalColor, partialTick) != null)
		{
			event.setRed((float) fogColor.getX());
			event.setGreen((float) fogColor.getY());
			event.setBlue((float) fogColor.getZ());
		}
	}
}
