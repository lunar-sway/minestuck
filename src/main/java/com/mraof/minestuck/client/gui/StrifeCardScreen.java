package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.network.StrifePackets;
import com.mraof.minestuck.player.KindAbstratusList;
import com.mraof.minestuck.player.KindAbstratusType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

/**
 * Port of GuiStrifeCard (Minestuck Universe 1.12.2).
 */
public class StrifeCardScreen extends Screen
{
	private static final int GUI_W = 147;
	private static final int GUI_H = 185;
	
	private static final ResourceLocation GUI_TEX = ResourceLocation.fromNamespaceAndPath(Minestuck.MOD_ID, "textures/gui/strife_specibus/strife_selector.png");
	
	private static final int COLUMNS = 2;
	private static final int COL_W = 49;
	
	private static final int LIST_X = 27;
	private static final int LIST_Y = 23;
	private static final int LIST_H = 155;
	
	// Y offset where item rows actually start rendering (below the header area in the texture)
	private static final int LIST_ITEMS_Y = 61;
	private static final int SCROLL_X = 128;
	private static final int SCROLL_Y = 23;
	private static final int SCROLL_H = 155;
	private static final float TEXT_SCALE = 0.65f;
	private final InteractionHand hand;
	private List<KindAbstratusType> types;
	
	private int xOff, yOff;
	
	private int rowH;
	private int visibleRows;
	private int maxScroll;
	
	private int scroll = 0;
	private boolean draggingScrollbar = false;
	
	public StrifeCardScreen(InteractionHand hand)
	{
		super(Component.translatable("gui.strifeCard.title"));
		this.hand = hand;
	}
	
	@Override
	protected void init()
	{
		super.init();
		
		xOff = (width - GUI_W) / 2;
		yOff = (height - GUI_H) / 2;
		
		types = KindAbstratusList.getTypeList();
		
		rowH = minecraft.font.lineHeight;
		int itemAreaH = (LIST_Y + LIST_H) - LIST_ITEMS_Y;
		visibleRows = itemAreaH / rowH;
		
		int totalRows = (types.size() + COLUMNS - 1) / COLUMNS;
		maxScroll = Math.max(0, totalRows - visibleRows);
	}
	
	@Override
	public boolean isPauseScreen()
	{
		return false;
	}
	
	@Override
	public void render(GuiGraphics g, int mx, int my, float pt)
	{
		super.render(g, mx, my, pt);
		
		RenderSystem.setShaderColor(1, 1, 1, 1);
		
		// Black background for the list area
		g.fill(xOff + LIST_X, yOff + LIST_Y, xOff + SCROLL_X, yOff + LIST_Y + LIST_H, 0xFF000000);
		g.enableScissor(xOff + LIST_X, yOff + LIST_Y, xOff + SCROLL_X, yOff + LIST_Y + LIST_H);
		
		int firstItem = scroll * COLUMNS;
		
		for(int row = 0; row < visibleRows; row++)
		{
			for(int col = 0; col < COLUMNS; col++)
			{
				int index = firstItem + row * COLUMNS + col;
				if(index >= types.size()) continue;
				
				KindAbstratusType type = types.get(index);
				
				// Screen-space top-left corner of this cell
				int cellX = xOff + LIST_X + col * COL_W;
				int cellY = yOff + LIST_ITEMS_Y + row * rowH;
				
				boolean hovered = isInRegion(cellX, cellY, COL_W, rowH, mx, my);
				int textColor = hovered ? 0x000000 : 0xFFFFFF;
				
				if(hovered)
				{
					g.fill(cellX, cellY, cellX + COL_W, cellY + rowH, 0xFFAFAFAF);
				}
				
				String displayName = type.getDisplayName().getString();
				float textScreenX = cellX + 4;
				float textScreenY = cellY + (rowH - minecraft.font.lineHeight * TEXT_SCALE) / 2f;
				
				g.pose().pushPose();
				g.pose().scale(TEXT_SCALE, TEXT_SCALE, 1f);
				g.drawString(minecraft.font, displayName, (int) (textScreenX / TEXT_SCALE), (int) (textScreenY / TEXT_SCALE), textColor, false);
				g.pose().popPose();
			}
		}
		
		g.disableScissor();
		
		RenderSystem.setShaderColor(1, 1, 1, 1);
		
		g.blit(GUI_TEX, xOff, yOff, 0, 0, GUI_W, GUI_H);
		
		// Scrollbar thumb
		int thumbY = (maxScroll > 0) ? (int) ((SCROLL_H - 15) * ((float) scroll / Math.max(1, maxScroll))) : 0;
		int thumbU = (maxScroll > 0) ? 232 : 244;
		g.blit(GUI_TEX, xOff + SCROLL_X, yOff + SCROLL_Y + thumbY, thumbU, 0, 12, 15);
		
		String label = Component.translatable("gui.strifeCard.label").getString();
		int labelFontWidth = minecraft.font.width(label);
		
		float availableLength = GUI_H - 12f;
		float labelScale = Math.min(1.2f, availableLength / labelFontWidth);
		
		int centerX = xOff + GUI_W / 2 + 5;
		int centerY = yOff + 128;
		
		g.pose().pushPose();
		g.pose().translate(centerX, centerY, 0);
		g.pose().mulPose(com.mojang.math.Axis.ZP.rotationDegrees(270F));
		g.pose().scale(labelScale, labelScale, 1F);
		
		int lx = -(int) (labelFontWidth / 2f);
		int ly = -(int) ((GUI_W / 2F - 6) / labelScale);
		
		g.drawString(minecraft.font, label, lx, ly, 0xFFFFFF, false);
		g.pose().popPose();
	}
	
	@Override
	public boolean mouseScrolled(double mx, double my, double scrollX, double scrollY)
	{
		if(maxScroll <= 0) return false;
		scroll = clamp(scroll - (int) Math.signum(scrollY), 0, maxScroll);
		return true;
	}
	
	@Override
	public boolean mouseClicked(double mx, double my, int button)
	{
		if(button != 0) return super.mouseClicked(mx, my, button);
		
		int firstItem = scroll * COLUMNS;
		
		for(int row = 0; row < visibleRows; row++)
		{
			for(int col = 0; col < COLUMNS; col++)
			{
				int index = firstItem + row * COLUMNS + col;
				if(index >= types.size()) continue;
				
				int cellX = xOff + LIST_X + col * COL_W;
				int cellY = yOff + LIST_ITEMS_Y + row * rowH;
				
				if(isInRegion(cellX, cellY, COL_W, rowH, (int) mx, (int) my))
				{
					PacketDistributor.sendToServer(new StrifePackets.SelectAbstrataForCardPacket(hand, types.get(index).getUnlocalizedName()));
					onClose();
					return true;
				}
			}
		}
		
		if(isInRegion(xOff + SCROLL_X, yOff + SCROLL_Y, 12, SCROLL_H, (int) mx, (int) my))
		{
			draggingScrollbar = true;
			updateScrollFromMouse(my);
			return true;
		}
		
		return super.mouseClicked(mx, my, button);
	}
	
	@Override
	public boolean mouseDragged(double mx, double my, int button, double dragX, double dragY)
	{
		if(button == 0 && draggingScrollbar)
		{
			updateScrollFromMouse(my);
			return true;
		}
		return super.mouseDragged(mx, my, button, dragX, dragY);
	}
	
	@Override
	public boolean mouseReleased(double mx, double my, int button)
	{
		if(button == 0) draggingScrollbar = false;
		return super.mouseReleased(mx, my, button);
	}
	
	private boolean isInRegion(int rx, int ry, int rw, int rh, int px, int py)
	{
		return px >= rx && px < rx + rw && py >= ry && py < ry + rh;
	}
	
	private void updateScrollFromMouse(double my)
	{
		if(maxScroll <= 0) return;
		float rel = (float) (my - (yOff + SCROLL_Y) - 15 / 2F) / (SCROLL_H - 15);
		scroll = clamp(Math.round(rel * maxScroll), 0, maxScroll);
	}
	
	private static int clamp(int value, int min, int max)
	{
		return Math.max(min, Math.min(max, value));
	}
}