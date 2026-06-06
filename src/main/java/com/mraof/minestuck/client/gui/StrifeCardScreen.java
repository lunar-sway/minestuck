package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.network.SelectAbstrataForCardPacket;
import com.mraof.minestuck.player.KindAbstratusList;
import com.mraof.minestuck.player.KindAbstratusType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

/**
 * Port of GuiStrifeCard (Minestuck Universe 1.12.2).
 */
public class StrifeCardScreen extends Screen
{
	private static final int GUI_W = 147;
	private static final int GUI_H = 185;
	
	private static final ResourceLocation GUI_TEX = ResourceLocation.fromNamespaceAndPath(Minestuck.MOD_ID, "textures/gui/strife_specibus/strife_selector.png");
	
	private static final String ICONS = "textures/gui/strife_specibus/icons/";
	
	private static final int COLUMNS = 2;
	private static final int COL_W = 50;
	
	private static final int LIST_X = 27;
	private static final int LIST_Y = 23;
	private static final int LIST_H = 155;
	
	private static final int SCROLL_X = 128;
	private static final int SCROLL_Y = 23;
	private static final int SCROLL_H = 155;
	
	private final InteractionHand hand;
	private List<KindAbstratusType> types;
	
	private int xOff, yOff;
	
	private int rowH;
	private int visibleRows;
	private int maxScroll;
	
	private int scroll = 0;
	
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
		
		types = new ArrayList<>(KindAbstratusList.getTypeList());
		
		rowH = minecraft.font.lineHeight + 1;
		visibleRows = LIST_H / rowH;
		
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
		
		int listLeft = xOff + LIST_X - 11;
		int listTop = yOff + 59;
		
		int firstItem = scroll * COLUMNS;
		
		g.fill(xOff + LIST_X, yOff + LIST_Y, xOff + LIST_X + COL_W * COLUMNS, yOff + LIST_Y + LIST_H, 0xFF000000);
		
		g.enableScissor(xOff + LIST_X, yOff + LIST_Y, xOff + LIST_X + COL_W * COLUMNS, yOff + LIST_Y + LIST_H);
		
		for(int row = 0; row < visibleRows; row++)
		{
			for(int col = 0; col < COLUMNS; col++)
			{
				int index = firstItem + row * COLUMNS + col;
				
				if(index >= types.size()) continue;
				
				KindAbstratusType type = types.get(index);
				
				int ex = listLeft + col * COL_W;
				int ey = listTop + row * rowH;
				
				boolean hovered = isInRegion(ex + 11, ey + 1, COL_W, rowH, mx, my);
				
				boolean iconHovered = isInRegion(ex + 11, ey + 1, 12, 12, mx, my);
				
				int textColor = hovered ? 0x000000 : 0xFFFFFF;
				int iconBg = iconHovered ? 0xFF000000 : 0xFFFFFFFF;
				
				// ICON BACKGROUND
				g.fill(ex + 11, ey + 1, ex + 11 + 12, ey + 1 + 12, iconBg);
				
				// TEXT HOVER BACKGROUND
				if(hovered)
				{
					g.fill(ex + 11, ey + 1, ex + 11 + COL_W, ey + 1 + rowH, 0xFFAFAFAF);
				}
				
				// TEXT SCALE
				float scale = 0.75f;
				
				g.pose().pushPose();
				g.pose().scale(scale, scale, 1f);
				
				g.drawString(minecraft.font, type.getDisplayName(), (int) ((ex + COL_W - minecraft.font.width(type.getDisplayName().getString()) + 10) / scale), (int) ((ey + 3) / scale), textColor, false);
				
				g.pose().popPose();
				
				// ICON
				String suffix = iconSuffix(type);
				ResourceLocation icon = ResourceLocation.fromNamespaceAndPath(Minestuck.MOD_ID, ICONS + suffix + ".png");
				
				float s = 12f / 16f;
				
				g.pose().pushPose();
				g.pose().scale(s, s, 1f);
				
				RenderSystem.setShaderColor(1, 1, 1, 1);
				
				g.blit(icon, (int) ((ex + 11) / s), (int) ((ey + 1) / s), 0, 0, 16, 16, 16, 16);
				
				g.pose().popPose();
			}
		}
		
		g.disableScissor();
		
		RenderSystem.setShaderColor(1, 1, 1, 1);
		
		g.blit(GUI_TEX, xOff, yOff, 0, 0, GUI_W, GUI_H);
		
		int thumbY = (maxScroll > 0) ? (int) ((SCROLL_H - 15) * ((float) scroll / Math.max(1, maxScroll))) : 0;
		
		int thumbU = (maxScroll > 0) ? 232 : 244;
		
		g.blit(GUI_TEX, xOff + SCROLL_X, yOff + SCROLL_Y + thumbY, thumbU, 0, 12, 15);
	}
	
	@Override
	public boolean mouseScrolled(double mx, double my, double scrollX, double scrollY)
	{
		if(maxScroll <= 0) return false;
		
		if(scrollY > 0) scroll = Math.max(0, scroll - 1);
		else scroll = Math.min(maxScroll, scroll + 1);
		
		return true;
	}
	
	@Override
	public boolean mouseClicked(double mx, double my, int button)
	{
		if(button != 0) return super.mouseClicked(mx, my, button);
		
		int listLeft = xOff + LIST_X - 11;
		int listTop = yOff + 59;
		
		int firstItem = scroll * COLUMNS;
		
		for(int row = 0; row < visibleRows; row++)
		{
			for(int col = 0; col < COLUMNS; col++)
			{
				int index = firstItem + row * COLUMNS + col;
				
				if(index >= types.size()) continue;
				
				int ex = listLeft + col * COL_W;
				int ey = listTop + row * rowH;
				
				if(isInRegion(ex + 11, ey + 1, COL_W, rowH, (int) mx, (int) my))
				{
					PacketDistributor.sendToServer(new SelectAbstrataForCardPacket(hand, types.get(index).getUnlocalizedName()));
					
					onClose();
					return true;
				}
			}
		}
		
		return super.mouseClicked(mx, my, button);
	}
	
	private boolean isInRegion(int rx, int ry, int rw, int rh, int px, int py)
	{
		return px >= rx && px < rx + rw && py >= ry && py < ry + rh;
	}
	
	private static String iconSuffix(KindAbstratusType type)
	{
		String n = type.getUnlocalizedName();
		int dot = n.lastIndexOf('.');
		return dot >= 0 ? n.substring(dot + 1) : n;
	}
}