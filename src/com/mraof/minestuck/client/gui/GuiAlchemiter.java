package com.mraof.minestuck.client.gui;

import java.util.List;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.client.util.GuiUtil;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.network.MinestuckPacketHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.tileentity.TileEntityAlchemiter;
import com.mraof.minestuck.alchemy.AlchemyRecipes;
import com.mraof.minestuck.alchemy.GristSet;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiAlchemiter extends GuiScreen implements GuiButtonImpl.ButtonClickhandler
{
	
	
	private static final ResourceLocation guiBackground = new ResourceLocation("minestuck", "textures/gui/large_alchemiter.png");
	private static final int guiWidth = 159, guiHeight = 102;
	private TileEntityAlchemiter alchemiter;
	private int itemQuantity;
	public GuiAlchemiter(TileEntityAlchemiter te)
	{
		alchemiter = te;
		itemQuantity = 1;
	}
	
	public TileEntityAlchemiter getAlchemiter() {
		return alchemiter;
	}
	
	GuiButtonImpl alchemize;
	
	GuiButtonImpl hundredsUp;
	GuiButtonImpl tensUp;
	GuiButtonImpl onesUp;
	GuiButtonImpl hundredsDown;
	GuiButtonImpl tensDown;
	GuiButtonImpl onesDown;
	
	
	
	@Override
	public void initGui()
	{
		alchemize = new GuiButtonImpl(this, 0, (width-100)/2,(height-guiHeight)/2+110, 100, 20, "ALCHEMIZE");
		
		hundredsUp = new GuiButtonImpl(this, 1,(width-guiWidth)/2+52,(height-guiHeight)/2+10,18,18,"^");
		tensUp = new GuiButtonImpl(this, 2,(width-guiWidth)/2+31,(height-guiHeight)/2+10,18,18,"^");
		onesUp = new GuiButtonImpl(this, 3,(width-guiWidth)/2+10,(height-guiHeight)/2+10,18,18,"^");
		hundredsDown = new GuiButtonImpl(this, 4,(width-guiWidth)/2+52,(height-guiHeight)/2+74,18,18,"v");
		tensDown =new GuiButtonImpl(this, 5,(width-guiWidth)/2+31,(height-guiHeight)/2+74,18,18,"v");
		onesDown = new GuiButtonImpl(this, 6,(width-guiWidth)/2+10,(height-guiHeight)/2+74,18,18,"v");

		buttons.add(alchemize);
		GristSet cost = alchemiter.getGristCost(1);
		//don't add the buttons if the item is free or unalchemizeable
		if(cost != null && !cost.isEmpty())
		{
			buttons.add(onesUp);
			buttons.add(tensUp);
			buttons.add(hundredsUp);
			buttons.add(onesDown);
			buttons.add(tensDown);
			buttons.add(hundredsDown);
		}
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		int xOffset = (width - guiWidth) / 2;
		int yOffset = (height - guiHeight) / 2;
		
		this.drawDefaultBackground();
		
		
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		this.mc.getTextureManager().bindTexture(guiBackground);
		this.drawTexturedModalRect(xOffset, yOffset, 0, 0, guiWidth, guiHeight);
		
		mc.fontRenderer.drawString(Integer.toString(((int) (itemQuantity / Math.pow(10, 2)) % 10)), (width - guiWidth) / 2 + 15, (height - guiHeight) / 2 + 46, 16777215);
		mc.fontRenderer.drawString(Integer.toString(((int) (itemQuantity / Math.pow(10, 1)) % 10)), (width - guiWidth) / 2 + 36, (height - guiHeight) / 2 + 46, 16777215);
		mc.fontRenderer.drawString(Integer.toString(((int) (itemQuantity / Math.pow(10, 0)) % 10)), (width - guiWidth) / 2 + 57, (height - guiHeight) / 2 + 46, 16777215);
		
		//Render grist requirements
		
		
		//Calculate the grist set
		GristSet set;
		set = alchemiter.getGristCost(itemQuantity);
		//draw the grist board
		GuiUtil.drawGristBoard(set, AlchemyRecipes.getDecodedItem(alchemiter.getDowel()).getItem() == MinestuckItems.CAPTCHA_CARD ? GuiUtil.GristboardMode.LARGE_ALCHEMITER_SELECT : GuiUtil.GristboardMode.LARGE_ALCHEMITER, (width - guiWidth) / 2 + 88, (height - guiHeight) / 2 + 13, fontRenderer);
		//draw the grist
		List<String> tooltip = GuiUtil.getGristboardTooltip(set, mouseX, mouseY, 9, 45, fontRenderer);
		if(tooltip != null)
			this.drawHoveringText(tooltip, mouseX, mouseY, fontRenderer);
		super.render(mouseX, mouseY, partialTicks);
	}
		
		
	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}
	
	@Override
	public void actionPerformed(GuiButtonImpl button)
	{
		
		if (button.id == 0)
		{
			
			MinestuckPacket packet = MinestuckPacket.makePacket(Type.ALCHEMITER_PACKET, alchemiter, itemQuantity);
			MinestuckPacketHandler.sendToServer(packet);
			this.mc.displayGuiScreen(null);
			
		} else
		{
			//the amount the button changes the amount
			int change;
			int maxCount = Math.min(999, alchemiter.getOutput().getMaxStackSize() * MinestuckConfig.clientAlchemiterStacks);
			if(button.id <= 3)
			{
				change = (int) Math.pow(10, button.id - 1);
				//custom modulo function
				
				if(itemQuantity + change > maxCount)
				{
					int powTen = (int) Math.pow(10, button.id - 1);
					change = 0 - (maxCount / powTen) * powTen;
					//because it's only a problem about half the time
					if(itemQuantity + change <= 0)
						itemQuantity += powTen;
				}
			} else
			{
				change = 0 - (int) Math.pow(10, button.id - 4);
				//custom modulo function
				if(itemQuantity + change <= 0)
				{
					int powTen = (int) Math.pow(10, button.id - 4);
					change = (maxCount / powTen) * powTen;
					//because it's only a problem about half the time
					if(itemQuantity + change > maxCount)
						itemQuantity -= powTen;
				}
			}
			itemQuantity = (itemQuantity + change);
		}
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
	{
		if(mouseButton == 0 && mc.player.inventory.getItemStack().isEmpty()
				&& alchemiter.getDowel() != null && AlchemyRecipes.getDecodedItem(alchemiter.getDowel()).getItem() == MinestuckItems.CAPTCHA_CARD
				&& mouseX >= (width-guiWidth)/2 +80  && mouseX < (width-guiWidth)/2 + 150 && mouseY >= (height-guiHeight)/2 + 8 && mouseY < (height-guiHeight)/2 + 93)
		{
			mc.currentScreen = new GuiGristSelector(this);
			mc.currentScreen.setWorldAndResolution(mc, width, height);
			return true;
		}
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
}
