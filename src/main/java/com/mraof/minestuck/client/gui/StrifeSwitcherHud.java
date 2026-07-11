package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.components.MSItemComponents;
import com.mraof.minestuck.network.StrifePackets;
import com.mraof.minestuck.player.KindAbstratusType;
import com.mraof.minestuck.player.StrifePortfolioData;
import com.mraof.minestuck.player.StrifeSpecibus;
import com.mraof.minestuck.util.MSAttachments;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.LinkedList;

/**
 * Client-side hud for quick weapon / specibus switching.
 * Port of GuiStrifeSwitcher (Minestuck Universe 1.12.2).
 * <p>
 * Keys:
 * - Press V with a NON-assigned weapon in hand -> assign weapon to portfolio (no hud shown)
 * - Press V with empty hand or already-assigned weapon -> show hud
 * - Scroll -> cycle weapons in current specibus
 * - Sneak + scroll -> cycle specibus slots (if switcher unlocked)
 * - Release -> arm selected weapon (RetrieveWeaponPacket)
 * - Press B with empty/assigned hand -> offhand swap mode
 */
@EventBusSubscriber(modid = Minestuck.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public final class StrifeSwitcherHud
{
	
	private static final String ICONS = "textures/gui/strife_specibus/icons/";
	
	public static boolean showSwitcher = false;
	public static boolean offhandMode = false;
	
	private static boolean prevStrifeDown = false;
	private static boolean prevSwapDown = false;
	
	public static int selSpecibus = -1;
	public static int selWeapon = 0;
	
	public static void beginSwitch(boolean offhand)
	{
		Minecraft mc = Minecraft.getInstance();
		
		if(mc.player == null || mc.screen != null)
			return;
		
		offhandMode = offhand;
		
		ItemStack main = mc.player.getMainHandItem();
		boolean armed = !main.isEmpty() && main.has(MSItemComponents.STRIFE_ASSIGNED.get());
		boolean hasItem = !main.isEmpty() && !armed;
		
		StrifePortfolioData data = mc.player.getData(MSAttachments.STRIFE_PORTFOLIO.get());
		
		if(!offhandMode && hasItem && !data.isPortfolioEmpty())
		{
			PacketDistributor.sendToServer(
					new StrifePackets.AssignStrifePacket(InteractionHand.MAIN_HAND));
			showSwitcher = false;
			return;
		}
		
		showSwitcher = true;
		
		selSpecibus = data.getSelectedSpecibusIndex();
		selWeapon = data.getSelectedWeaponIndex();
		
		if(selSpecibus < 0)
		{
			StrifeSpecibus[] nonEmpty = data.getNonEmptyPortfolio();
			
			if(nonEmpty.length > 0)
				selSpecibus = data.getSpecibusIndex(nonEmpty[0]);
		}
	}
	
	public static void finishSwitch()
	{
		if(!showSwitcher)
			return;
		
		showSwitcher = false;
		commitSelection(Minecraft.getInstance());
	}
	
	private static void commitSelection(Minecraft mc)
	{
		if(selSpecibus < 0) return;
		
		if(offhandMode)
		{
			PacketDistributor.sendToServer(new StrifePackets.SwapOffhandStrifePacket(selSpecibus, selWeapon));
		} else
		{
			boolean sneaking = mc.player != null && mc.player.isCrouching();
			StrifePortfolioData data = mc.player.getData(MSAttachments.STRIFE_PORTFOLIO.get());
			
			if(sneaking && data.abstrataSwitcherUnlocked())
			{
				// Specibus already changed server-side via SetActiveStrifePacket during scroll
				// Soo no further action needed =]
			} else
			{
				// Arm/disarm selected weapon
				PacketDistributor.sendToServer(new StrifePackets.RetrieveWeaponPacket(selWeapon, InteractionHand.MAIN_HAND));
			}
		}
	}
	
	@SubscribeEvent
	public static void onMouseScroll(net.neoforged.neoforge.client.event.InputEvent.MouseScrollingEvent event)
	{
		if(!showSwitcher || Minecraft.getInstance().player == null) return;
		scroll(Minecraft.getInstance(), (int) -Math.signum(event.getScrollDeltaY()));
		event.setCanceled(true);
	}
	
	public static void scroll(Minecraft mc, int dir)
	{
		if(mc.player == null) return;
		StrifePortfolioData data = mc.player.getData(MSAttachments.STRIFE_PORTFOLIO.get());
		boolean sneaking = mc.player.isCrouching();
		
		if(sneaking && data.abstrataSwitcherUnlocked())
		{
			StrifeSpecibus[] ne = data.getNonEmptyPortfolio();
			if(ne.length == 0) return;
			
			int curPos = 0;
			for(int j = 0; j < ne.length; j++)
				if(data.getSpecibusIndex(ne[j]) == selSpecibus)
				{
					curPos = j;
					break;
				}
			
			curPos = Math.floorMod(curPos + dir, ne.length);
			selSpecibus = data.getSpecibusIndex(ne[curPos]);
			selWeapon = 0;
			PacketDistributor.sendToServer(new StrifePackets.SetActiveStrifePacket(selSpecibus));
		} else
		{
			if(selSpecibus < 0 || selSpecibus >= data.getPortfolio().length) return;
			StrifeSpecibus sp = data.getPortfolio()[selSpecibus];
			if(sp == null || sp.getContents().isEmpty()) return;
			int deckSize = sp.getContents().size();
			selWeapon = Math.floorMod(selWeapon + dir, deckSize);
		}
	}
	
	@SubscribeEvent
	public static void onRenderHud(RenderGuiEvent.Post event)
	{
		Minecraft mc = Minecraft.getInstance();
		if(!showSwitcher || mc.player == null) return;
		
		GuiGraphics g = event.getGuiGraphics();
		int sw = mc.getWindow().getGuiScaledWidth();
		int sh = mc.getWindow().getGuiScaledHeight();
		int cx = sw / 2;
		int baseY = sh * 3 / 4;
		
		StrifePortfolioData data = mc.player.getData(MSAttachments.STRIFE_PORTFOLIO.get());
		boolean sneaking = mc.player.isCrouching();
		
		if(sneaking && data.abstrataSwitcherUnlocked()) renderSpecibusPicker(g, data, cx, baseY);
		else renderWeaponPicker(g, data, cx, baseY, mc);
	}
	
	private static void renderSpecibusPicker(GuiGraphics g, StrifePortfolioData data, int cx, int baseY)
	{
		StrifeSpecibus[] ne = data.getNonEmptyPortfolio();
		if(ne.length == 0) return;
		
		int curPos = 0;
		for(int j = 0; j < ne.length; j++)
			if(data.getSpecibusIndex(ne[j]) == selSpecibus)
			{
				curPos = j;
				break;
			}
		
		int toShow = (int) Math.min(5, Math.ceil((ne.length - 1) / 2f) * 2);
		for(int offset = -(toShow / 2); offset <= (toShow / 2); offset++)
		{
			int i = Math.floorMod(curPos + offset, ne.length);
			StrifeSpecibus sp = ne[i];
			if(sp == null || sp.getKindAbstratus() == null) continue;
			
			int x = cx - 8 + offset * 20;
			
			if(offset == 0)
			{
				drawWidgetBox(g, x - 3, baseY - 3, false);
				String name = sp.getDisplayName().getString();
				Minecraft mc = Minecraft.getInstance();
				g.drawString(mc.font, name, cx - mc.font.width(name) / 2, baseY - 14, 0x00AB54, true);
			}
			
			KindAbstratusType t = sp.getKindAbstratus();
			ResourceLocation icon = ResourceLocation.fromNamespaceAndPath(Minestuck.MOD_ID, ICONS + iconSuffix(t) + ".png");
			RenderSystem.setShaderColor(1, 1, 1, 1);
			g.blit(icon, x, baseY, 0, 0, 16, 16, 16, 16);
		}
	}
	
	private static void renderWeaponPicker(GuiGraphics g, StrifePortfolioData data, int cx, int baseY, Minecraft mc)
	{
		if(selSpecibus < 0 || selSpecibus >= data.getPortfolio().length) return;
		StrifeSpecibus sp = data.getPortfolio()[selSpecibus];
		if(sp == null) return;
		
		LinkedList<ItemStack> deck = sp.getContents();
		if(deck.isEmpty()) return;
		
		int toShow = (int) Math.min(5, Math.ceil((deck.size() - 1) / 2f) * 2);
		
		for(int offset = -(toShow / 2); offset <= (toShow / 2); offset++)
		{
			int wIdx = Math.floorMod(selWeapon + offset, deck.size());
			ItemStack stack = deck.get(wIdx);
			if(stack == null || stack.isEmpty()) continue;
			
			int x = cx - 8 + offset * 20;
			
			if(offset == 0)
			{
				drawWidgetBox(g, x - 3, baseY - 3, offhandMode);
				String name = stack.getHoverName().getString();
				g.drawString(mc.font, name, cx - mc.font.width(name) / 2, baseY - 14, 0x00AB54, true);
			}
			
			// highlight if this is the currently armed weapon in offhand mode
			if(data.isArmed() && offhandMode && wIdx == data.getSelectedWeaponIndex())
				drawWidgetBox(g, x - 3, baseY - 3, true);
			
			g.renderItem(stack, x, baseY);
			g.renderItemDecorations(mc.font, stack, x, baseY);
		}
	}
	
	private static void drawWidgetBox(GuiGraphics g, int x, int y, boolean offhand)
	{
		ResourceLocation icons = ResourceLocation.fromNamespaceAndPath(Minestuck.MOD_ID, "textures/gui/icons.png");
		RenderSystem.setShaderColor(1, 1, 1, 1);
		g.blit(icons, x, y, offhand ? 134 : 112, 0, 22, 22, 256, 256);
	}
	
	private static String iconSuffix(KindAbstratusType type)
	{
		String n = type.getUnlocalizedName();
		int dot = n.lastIndexOf('.');
		return dot >= 0 ? n.substring(dot + 1) : n;
	}
}