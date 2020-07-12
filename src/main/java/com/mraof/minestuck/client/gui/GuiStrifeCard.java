package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mraof.minestuck.inventory.specibus.StrifePortfolioHandler;
import com.mraof.minestuck.inventory.specibus.StrifeSpecibus;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.player.KindAbstratusList;
import com.mraof.minestuck.player.KindAbstratusType;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class GuiStrifeCard extends MinestuckScreen
{
	private static final int guiWidth = 147;
	private static final int guiHeight = 185;
	private int xOffset, yOffset;
	private static final ResourceLocation guiStrifeSelector = new ResourceLocation("minestuck", "textures/gui/strife_specibus/strife_selector.png");
	private float scale = 1;
	private static final int columnWidth = 50,columns = 2;
	private final PlayerEntity player;
	//private static final FontRenderer font = Minestuck.fontSpecibus;
	
	private final ArrayList<KindAbstratusType> abstrataList;
	private static int size = 26;
	
	private final int maxScroll;
	private float scrollPos = 0F;
	private boolean isClicking = false;
	private int extraLines = 0;
	
	public GuiStrifeCard(PlayerEntity player)
	{
		super(new TranslationTextComponent("minestuck.strife_card"));
		this.player = player;
		ArrayList<KindAbstratusType> list = new ArrayList<>();
		for(KindAbstratusType i : KindAbstratusList.getTypeList())
			if(i.getSelectable()) list.add(i);
		abstrataList = list;
		maxScroll = Math.max(0, (abstrataList.size() - size)/2);
	}
	
	@Override
	public void init()
	{
		xOffset = (width-guiWidth)/2;
		yOffset = (height-guiHeight)/2;
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		scale = 1;
		super.render(mouseX, mouseY, partialTicks);
		
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		fill(xOffset+27, yOffset+23, xOffset+127, yOffset+178, 0xFF000000);
		
		int listOffsetX = xOffset + 16;
		int listOffsetY = yOffset + 59 - extraLines*font.FONT_HEIGHT;
		
		
		int itemMin = (int) (scrollPos * maxScroll * 2) - extraLines*2;
		if(itemMin%2 == 1) itemMin--;
		
		int i = 0, count = 0;
		for(KindAbstratusType type : abstrataList)
		{
			count++;
			if(count-1 < itemMin) continue;
			i++;
			if(i > size) break;
			
			String typeName = type.getDisplayName().getFormattedText();
			
			//txPos += 78 (0.1625*width)
			//tyPos += 36 (9540 / height)
			
			int color = 0xFFFFFF;
			int listX = (columnWidth*((i-1) % columns));
			int listY = (font.FONT_HEIGHT*((i-1) / columns));
			int xPos = listOffsetX + listX + 11;
			int yPos = listOffsetY + listY + 1;
			int sxPos = (int)((listOffsetX + listX)/scale);
			int syPos = (int)((listOffsetY + listY)/scale);
			int txPos = (sxPos + columnWidth - font.getStringWidth(typeName))+ (int)(10/scale);
			int tyPos = syPos + (int)(3/scale);
			
			if(isPointInRegion(xPos, yPos, columnWidth, font.FONT_HEIGHT, mouseX, mouseY))
			{
				fill(xPos, yPos, xPos+columnWidth, yPos+font.FONT_HEIGHT, 0xFFAFAFAF);
				color = 0x000000;
			}
			font.drawString(typeName, txPos, tyPos, color);
		}
		
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		int scroll = (int) (140*scrollPos);
		this.minecraft.getTextureManager().bindTexture(guiStrifeSelector);
		this.blit(xOffset, yOffset, 0, 0, guiWidth, guiHeight);
		this.blit(xOffset+128, yOffset+23+scroll, (maxScroll > 0) ? 232 : 244, 0, 12, 15);
		
		//96*265/width
		
		setScale(1.8F);
		String label = I18n.format("specibus.card.label");
		int xLabel = (int)(-(((height+guiHeight)/2)-8)/scale);
		int yLabel = (int)((((width+guiWidth)/2)-135)/scale);
		//178
		
		GL11.glRotatef(270, 0, 0, 1);
		font.drawString(label, xLabel, yLabel, 0xFFFFFF);
		
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
	{
		if(mouseButton == GLFW.GLFW_MOUSE_BUTTON_1)
		{
			int listOffsetX = xOffset + 16;
			int listOffsetY = yOffset + 59 - extraLines * font.FONT_HEIGHT;
			
			int itemMin = (int) (scrollPos * maxScroll * 2) - extraLines * 2;
			if(itemMin % 2 == 1) itemMin--;
			
			int i = 0, count = 0;
			for(KindAbstratusType type : abstrataList)
			{
				count++;
				if(count - 1 < itemMin) continue;
				i++;
				if(i > size) break;
				
				int listX = (columnWidth * ((i - 1) % columns));
				int listY = (font.FONT_HEIGHT * ((i - 1) / columns));
				int xPos = listOffsetX + listX + 11;
				int yPos = listOffsetY + listY + 1;
				
				if(isPointInRegion(xPos, yPos, columnWidth, font.FONT_HEIGHT, (int) mouseX, (int) mouseY))
				{
					Hand hand = (player.getHeldItemMainhand().isItemEqual(new ItemStack(MSItems.strifeCard)) ? Hand.MAIN_HAND : Hand.OFF_HAND);
					ItemStack card = player.getHeldItem(hand);
					
					if(card.isItemEqual(new ItemStack(MSItems.strifeCard)))
					{
						StrifeSpecibus specibus = new StrifeSpecibus(i);
						
						StrifePortfolioHandler.addSpecibus(player, specibus);
						this.minecraft.displayGuiScreen(null);
						card.shrink(1);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int mouseButton, double dx, double dy)
	{
		if(maxScroll > 0 && isPointInRegion(xOffset+128, yOffset+23, 12, 155, (int) mouseX, (int) mouseY))
		{
			float sp = (float) ((mouseY-23-yOffset)/140.0F);
			scrollPos = Math.min(1, Math.max(0, sp));
			extraLines = (int) Math.min(4, scrollPos*maxScroll);
			size = 26 + extraLines*2;
			return true;
		}
		return false;
	}
	
	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double scroll)
	{
		if(maxScroll > 0 && scroll != 0)
		{
			float sp = (float) (scrollPos + 1.0F/maxScroll * -Math.signum(scroll));
			scrollPos = Math.min(1, Math.max(0, sp));
			extraLines = (int) Math.min(4, scrollPos*maxScroll);
			size = 26 + extraLines*2;
			return true;
		}
		return false;
	}
	
	public void setScale(float percentage)
	{
		float s = percentage/scale;
		GL11.glScalef(s,s,s);
		scale = percentage;
	}
	
	public void updateScalePos()
	{
		
	}
}
