package com.mraof.minestuck.client.gui.playerStats;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.network.StrifePackets;
import com.mraof.minestuck.player.ClientPlayerData;
import com.mraof.minestuck.player.KindAbstratusType;
import com.mraof.minestuck.player.StrifePortfolioData;
import com.mraof.minestuck.player.StrifeSpecibus;
import com.mraof.minestuck.util.MSAttachments;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.LinkedList;

/**
 * Port of GuiStrifePortfolio (Minestuck Universe 1.12.2).
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class StrifePortfolioScreen extends PlayerStatsScreen
{
	public static final String TITLE = "minestuck.strife_portfolio";
	
	private static final ResourceLocation BG_TEX = ms("textures/gui/strife_specibus/portfolio_bg.png");
	private static final ResourceLocation TABS_TEX = ms("textures/gui/strife_specibus/portfolio_tabs.png");
	private static final ResourceLocation FRAME_TEX = ms("textures/gui/strife_specibus/strife_portfolio.png");
	private static final ResourceLocation CARD_TEX = ms("textures/gui/strife_specibus/strife_card.png");
	private static final String ICONS = "textures/gui/strife_specibus/icons/";
	private static final ResourceLocation PORTFOLIO_ICONS = ms("textures/gui/icons.png");
	
	private static final float CS = 0.25f;
	
	// Card fan: {offsetX, offsetY, portfolioSlotIndex}
	private static final int[][] FAN = {{159, 69, 2}, {107, 77, 1}, {159, 25, 3}, {56, 80, 4}, {107, 33, 5}, {56, 40, 0}, {107, 7, 8}, {12, 50, 9}, {59, 7, 7}, {11, 9, 6},};
	private final float[] hoverAnim = new float[StrifePortfolioData.PORTFOLIO_SIZE];
	
	private int selectedCard = -1; // slot index the mouse is over (-1 = none)
	private int mouseX, mouseY;
	
	public StrifePortfolioScreen()
	{
		super(Component.translatable(TITLE));
		guiWidth = 226;
		guiHeight = 188;
	}
	
	
	@Override
	public void renderBackground(GuiGraphics g, int mx, int my, float pt)
	{
		super.renderBackground(g, mx, my, pt);
		drawTabs(g);
	}
	
	@Override
	public void render(GuiGraphics g, int mx, int my, float pt)
	{
		super.render(g, mx, my, pt);
		mouseX = mx;
		mouseY = my;
		selectedCard = -1;
		
		if(ClientPlayerData.hasDataCheckerAccess())
		{
			RenderSystem.setShaderColor(1, 1, 1, 1);
			g.blit(PORTFOLIO_ICONS, xOffset + 198, yOffset, 112, 32, 28, 35);
		}
		StrifePortfolioData data = mc.player.getData(MSAttachments.STRIFE_PORTFOLIO.get());
		StrifeSpecibus[] port = data.getPortfolio();
		int active = data.getSelectedSpecibusIndex();
		
		RenderSystem.setShaderColor(1, 1, 1, 1);
		
		g.blit(BG_TEX, xOffset, yOffset, 0, 0, guiWidth, guiHeight);
		
		tab(g, 0, 0, 20, 58, 98, 94);
		cardIf(g, 11, 9, 6, true, port, active);
		tab(g, 2, 6, 4, 44, 132, 120);
		
		cardIf(g, 59, 7, 7, true, port, active);
		cardIf(g, 12, 50, 9, true, port, active);
		tab(g, 21, 4, 0, 18, 152, 134);
		
		cardIf(g, 107, 7, 8, true, port, active);
		cardIf(g, 56, 40, 0, true, port, active);
		tab(g, 45, 10, 0, 0, 164, 152);
		tab(g, 173, 6, 124, 0, 7, 4);
		
		cardIf(g, 107, 33, 5, true, port, active);
		cardIf(g, 56, 80, 4, true, port, active);
		tab(g, 81, 28, 0, 8, 137, 120);
		tab(g, 218, 46, 142, 22, 2, 10);
		
		cardIf(g, 159, 25, 3, true, port, active);
		cardIf(g, 107, 77, 1, true, port, active);
		tab(g, 124, 52, 0, 32, 96, 96);
		
		cardIf(g, 159, 69, 2, true, port, active);
		tab(g, 168, 96, 204, 0, 52, 52);
		
		// active card (separate, not skipped by checkSelected)
		if(active >= 0 && active < port.length) drawCard(g, 10, 85, active, port[active]);
		
		// hover detection for all card positions
		detectHover(g, 10, 85, active, false, port, active);
		for(int[] f : FAN)
			detectHover(g, f[0], f[1], f[2], true, port, active);
		
		float speed = 0.2F;
		
		for(int i = 0; i < hoverAnim.length; i++)
		{
			boolean hovered = i == selectedCard;
			
			if(hovered)
				hoverAnim[i] = Math.min(1F, hoverAnim[i] + speed);
			else
				hoverAnim[i] = Math.max(0F, hoverAnim[i] - speed);
		}
		
		// foreground frame overlay
		RenderSystem.setShaderColor(1, 1, 1, 1);
		g.blit(FRAME_TEX, xOffset, yOffset, 0, 0, guiWidth, guiHeight);
		drawActiveTabAndOther(g, mx, my);
		
		// ── bottom abstrata icon strip
		float s = 0.0625f;
		for(int i = 0; i < StrifePortfolioData.PORTFOLIO_SIZE; i++)
		{
			StrifeSpecibus sp = port[i];
			if(sp == null || !sp.isAssigned()) continue;
			KindAbstratusType t = sp.getKindAbstratus();
			if(t == null) continue;
			
			g.pose().pushPose();
			g.pose().scale(s, s, 1f);
			RenderSystem.setShaderColor(1, 1, 1, 1);
			g.blit(iconLoc(t), (int) ((xOffset + 23 + 20 * i) / s), (int) ((yOffset + 166) / s), 0, 0, 256, 256);
			g.pose().popPose();
		}
	}
	
	private void tab(GuiGraphics g, int rx, int ry, int u, int v, int w, int h)
	{
		RenderSystem.setShaderColor(1, 1, 1, 1);
		g.blit(TABS_TEX, xOffset + rx, yOffset + ry, u, v, w, h);
	}
	
	private void cardIf(GuiGraphics g, int cx, int cy, int idx, boolean checkSelected, StrifeSpecibus[] port, int active)
	{
		if(idx < 0 || idx >= port.length) return;
		if(checkSelected && idx == active) return;
		drawCard(g, cx, cy, idx, port[idx]);
	}
	
	/**
	 * Full card with body, icon, labels and item deck.
	 */
	private void drawCard(GuiGraphics g, int cx, int cy, int idx, StrifeSpecibus sp)
	{
		if(sp == null) return;
		
		// hover pop-out offset
		float anim = hoverAnim[idx];
		
		int ox = -(int)(anim * 3F);
		int oy = -(int)(anim * 3F);
		int x = xOffset + cx + ox;
		int y = yOffset + cy + oy;
		
		// card body
		scaleBlit(g, CS, x, y, CARD_TEX, 28, 0, 200, 256);
		
		// abstrata icon
		KindAbstratusType type = sp.getKindAbstratus();
		if(type != null)
		{
			float s = CS / 2.5f;
			g.pose().pushPose();
			g.pose().scale(s, s, 1f);
			RenderSystem.setShaderColor(1, 1, 1, 1);
			g.blit(iconLoc(type), (int) (x / s) + 57, (int) (y / s) + 148, 0, 0, 256, 256);
			g.pose().popPose();
		}
		
		// "strife specibus" text
		{
			float s = CS * 1.5f;
			g.pose().pushPose();
			g.pose().scale(s, s, 1f);
			g.drawString(font, Component.translatable("gui.strifePortfolio.specibus"), (int) (x / s) + 5, (int) (y / s) + 4, 0xFF00E371, false);
			g.pose().popPose();
		}
		
		// type name
		{
			float s = CS * 2.5f;
			String dn = sp.getDisplayNameForCard();
			g.pose().pushPose();
			g.pose().scale(s, s, 1f);
			g.drawString(font, dn, (int) (x / s) + 70 - font.width(dn), (int) (y / s) + 91, 0xFF00E371, false);
			g.pose().popPose();
		}
		
		// "DECK" label
		{
			g.pose().pushPose();
			g.pose().scale(CS, CS, 1f);
			g.drawString(font, Component.translatable("gui.strifePortfolio.deck"), (int) (x / CS) + 16, (int) (y / CS) + 179, 0xFFFFFFFF, false);
			g.pose().popPose();
		}
		
		// weapon items
		LinkedList<ItemStack> items = sp.getContents();
		int shown = Math.min(items.size(), 5);
		int deckX = (int) (94 - 23 * (shown / 2f));

		for(int n = 0; n < shown; n++)
		{
			ItemStack stack = items.get(n);
			int ix = deckX + n * 23;
			int iy = 193;
			
			// slot frame from icons.png
			g.pose().pushPose();
			g.pose().scale(CS, CS, 1f);
			RenderSystem.setShaderColor(1, 1, 1, 1);
			g.blit(PlayerStatsScreen.icons, (int) (x / CS) + ix, (int) (y / CS) + iy, 0, 122, 21, 26);
			g.pose().popPose();
			
			// item icon at card scale
			g.pose().pushPose();
			g.pose().scale(CS, CS, 1f);
			g.pose().translate((x / CS) + ix + 2, (y / CS) + iy + 4, 0f);
			g.renderItem(stack, 0, 0);
			g.pose().popPose();
			
			g.flush();
		}
	}
	
	/**
	 * Draws {@code tex} at screen position (x, y) scaled by {@code s}.
	 */
	private void scaleBlit(GuiGraphics g, float s, int x, int y, ResourceLocation tex, int u, int v, int w, int h)
	{
		g.pose().pushPose();
		g.pose().scale(s, s, 1f);
		RenderSystem.setShaderColor(1, 1, 1, 1);
		g.blit(tex, (int) (x / s), (int) (y / s), u, v, w, h);
		g.pose().popPose();
	}
	
	private void detectHover(GuiGraphics g, int cx, int cy, int idx, boolean checkSelected, StrifeSpecibus[] port, int active)
	{
		if(idx < 0 || idx >= port.length) return;
		if(checkSelected && idx == active) return;
		if(port[idx] == null) return;
		if(selectedCard >= 0 && selectedCard != idx) return; // another card already hovered
		
		int ox = (idx == selectedCard) ? -5 : 0;
		int oy = (idx == selectedCard) ? -5 : 0;
		int x = xOffset + cx + ox;
		int y = yOffset + cy + oy;
		int w = Math.round((200 + (selectedCard == idx ? 20 : 0)) * CS);
		int h = Math.round((256 + (selectedCard == idx ? 20 : 0)) * CS);
		
		if(isPointInRegion(x, y, w, h, mouseX, mouseY)) selectedCard = idx;
	}
	
	@Override
	public boolean mouseClicked(double mx, double my, int button)
	{
		StrifePortfolioData data = mc.player.getData(MSAttachments.STRIFE_PORTFOLIO.get());
		
		// card fan click
		if(selectedCard >= 0)
		{
			if(button == 0) PacketDistributor.sendToServer(new StrifePackets.SetActiveStrifePacket(selectedCard));
			else if(button == 1) PacketDistributor.sendToServer(new StrifePackets.RetrieveStrifeCardPacket(selectedCard));
			return true;
		}
		
		// bottom icon strip click
		for(int i = 0; i < StrifePortfolioData.PORTFOLIO_SIZE; i++)
		{
			int sx = xOffset + 22 + 20 * i;
			int sy = yOffset + 165;
			if(!isPointInRegion(sx, sy, 18, 18, (int) mx, (int) my)) continue;
			if(button == 0) PacketDistributor.sendToServer(new StrifePackets.SetActiveStrifePacket(i));
			else if(button == 1) PacketDistributor.sendToServer(new StrifePackets.RetrieveStrifeCardPacket(i));
			return true;
		}
		
		// weapon click in active card
		StrifeSpecibus activeSp = data.getSelectedSpecibus();
		if(activeSp != null && button == 0)
		{
			int active = data.getSelectedSpecibusIndex();
			LinkedList<ItemStack> weapons = activeSp.getContents();
			int deckX = (int) (94 - 23 * (Math.min(weapons.size(), 5) / 2f));
			int baseX = xOffset + 10 + ((active == selectedCard) ? -5 : 0);
			int baseY = yOffset + 85 + ((active == selectedCard) ? -5 : 0);
			int n = 0;
			for(int i = 0; i < weapons.size(); i++)
			{
				int ix = (int) (deckX + (n % 5) * 23) - (n / 5);
				int iy = 193 - (n / 5) * 2;
				int wx = baseX + Math.round(ix * CS) + Math.round(2 * CS);
				int wy = baseY + Math.round(iy * CS) + Math.round(4 * CS);
				int sz = Math.round(16 * CS);
				if(isPointInRegion(wx, wy, sz, sz, (int) mx, (int) my))
				{
					PacketDistributor.sendToServer(new StrifePackets.RetrieveWeaponPacket(i, InteractionHand.MAIN_HAND));
					return true;
				}
				n++;
			}
		}
		
		return super.mouseClicked(mx, my, button);
	}
	
	private static ResourceLocation iconLoc(KindAbstratusType type)
	{
		String n = type.getUnlocalizedName(); // e.g. "minestuck.sword"
		int dot = n.lastIndexOf('.');
		return ms(ICONS + (dot >= 0 ? n.substring(dot + 1) : n) + ".png");
	}
	
	private static ResourceLocation ms(String path)
	{
		return ResourceLocation.fromNamespaceAndPath(Minestuck.MOD_ID, path);
	}
}