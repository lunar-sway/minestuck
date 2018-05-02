package com.mraof.minestuck.client.gui;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.client.util.GuiUtil;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.tileentity.TileEntityAlchemiter;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.util.GristAmount;
import com.mraof.minestuck.util.GristRegistry;
import com.mraof.minestuck.util.GristSet;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class GuiAlchemiter extends GuiScreen
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
	
	@Override
	public void initGui()
	{
		GuiButton alchemize = new GuiButton(0, (width-100)/2,(height-guiHeight)/2+110, 100, 20, "ALCHEMIZE");
		
		GuiButton hundredsUp = new GuiButton(1,(width-guiWidth)/2+52,(height-guiHeight)/2+10,18,18,"^");
		GuiButton tensUp = new GuiButton(2,(width-guiWidth)/2+31,(height-guiHeight)/2+10,18,18,"^");
		GuiButton onesUp = new GuiButton(3,(width-guiWidth)/2+10,(height-guiHeight)/2+10,18,18,"^");
		GuiButton hundredsDown = new GuiButton(4,(width-guiWidth)/2+52,(height-guiHeight)/2+74,18,18,"v");
		GuiButton tensDown =new GuiButton(5,(width-guiWidth)/2+31,(height-guiHeight)/2+74,18,18,"v");
		GuiButton onesDown = new GuiButton(6,(width-guiWidth)/2+10,(height-guiHeight)/2+74,18,18,"v");

		buttonList.add(alchemize);
		//dont add the buttons if the item is free
		if(!getGristCost().isEmpty()) {
			buttonList.add(onesUp);
			buttonList.add(tensUp);
			buttonList.add(hundredsUp);
			buttonList.add(onesDown);
			buttonList.add(tensDown);
			buttonList.add(hundredsDown);
		}
	}
	
		@Override
		public void drawScreen(int mouseX, int mouseY, float partialTicks)
		{
			int xOffset = (width - guiWidth)/2;
			int yOffset = (height - guiHeight)/2;
			
			this.drawDefaultBackground();	
			
			
			
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

			this.mc.getTextureManager().bindTexture(guiBackground);
			this.drawTexturedModalRect(xOffset, yOffset, 0, 0, guiWidth, guiHeight);
			
			mc.fontRenderer.drawString(Integer.toString(((int)(itemQuantity/Math.pow(10,2))%10)), (width-guiWidth)/2+15,(height-guiHeight)/2+46, 16777215);
			mc.fontRenderer.drawString(Integer.toString(((int)(itemQuantity/Math.pow(10,1))%10)), (width-guiWidth)/2+36,(height-guiHeight)/2+46, 16777215);
			mc.fontRenderer.drawString(Integer.toString(((int)(itemQuantity/Math.pow(10,0))%10)), (width-guiWidth)/2+57,(height-guiHeight)/2+46, 16777215);
			
			//Render grist requirements
				
			
			//Calculate the grist set
			GristSet set;
			set=getGristCost();
			//disable number buttons if cost is free
			if(set.isEmpty()) {
				
				///////////////////////////////////////////////
				//do something
				////////////////////////////////////////////////
			}
			
			GuiUtil.drawGristBoard(set, AlchemyRecipeHandler.getDecodedItem(alchemiter.getDowel()).getItem() == MinestuckItems.captchaCard ? GuiUtil.GristboardMode.LARGE_ALCHEMITER_SELECT : GuiUtil.GristboardMode.LARGE_ALCHEMITER, (width-guiWidth)/2+88,(height-guiHeight)/2+13, fontRenderer);
			
			List<String> tooltip = GuiUtil.getGristboardTooltip(set, mouseX , mouseY , 9, 45, fontRenderer);
			if (tooltip != null)
				this.drawHoveringText(tooltip, mouseX , mouseY , fontRenderer);
			super.drawScreen(mouseX, mouseY, partialTicks);
		}
		
		
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	@Override
	protected void actionPerformed(GuiButton button)
	{
		
		if (button.id == 0)
		{
			
			MinestuckPacket packet = MinestuckPacket.makePacket(Type.ALCHEMITER_PACKET, alchemiter, itemQuantity);
			MinestuckChannelHandler.sendToServer(packet);
			this.mc.displayGuiScreen(null);
			
		} else if (button.id <= 3)
		{
			if ((int) (itemQuantity / Math.pow(10, button.id - 1)) % 10 != 9)
			{
				itemQuantity += Math.pow(10, button.id - 1);
			}
		} else
		{
			if ((int) (itemQuantity / Math.pow(10, button.id - 4)) % 10 != 0)
			{
				itemQuantity -= Math.pow(10, button.id - 4);
			}
		}
	}
	private GristSet getGristCost() {
		GristSet set;
		ItemStack stack;
		boolean useSelectedType;

		//get the item in the dowel
		stack = AlchemyRecipeHandler.getDecodedItem(alchemiter.getDowel());
		
		//set the item as a generic object if there is nothing in the dowel
		if( !(alchemiter.getDowel().hasTagCompound() && alchemiter.getDowel().getTagCompound().hasKey("contentID")))
			stack = new ItemStack(MinestuckBlocks.genericObject);
		
		//get the grist cost of stack
		set = GristRegistry.getGristConversion(stack);

		//if the item is a captcha card do other stuff
		useSelectedType = stack.getItem() == MinestuckItems.captchaCard;
		if (useSelectedType)
			set = new GristSet(alchemiter.getSelectedGrist(), MinestuckConfig.clientCardCost);
		
		//remove damage from the item
		if (set != null && stack.isItemDamaged())
		{
			float multiplier = 1 - stack.getItem().getDamage(stack) / ((float) stack.getMaxDamage());
			for (GristAmount amount : set.getArray())
			{
				set.setGrist(amount.getType(), (int)( Math.ceil(amount.getAmount() * multiplier)));
			}
			
		}
		
		//multiply cost by quantity
		for (GristAmount amount : set.getArray())
		{
			set.setGrist(amount.getType(), amount.getAmount()*itemQuantity);
		}
		
		return set;
	}
}
