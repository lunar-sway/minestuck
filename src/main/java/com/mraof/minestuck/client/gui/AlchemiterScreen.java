package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.client.util.GuiUtil;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.crafting.alchemy.AlchemyRecipes;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.network.AlchemiterPacket;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.tileentity.AlchemiterTileEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.config.GuiButtonExt;

import java.util.List;

public class AlchemiterScreen extends Screen implements Positioned
{
	public static final String TITLE = "minestuck.alchemiter";
	
	private static final ResourceLocation guiBackground = new ResourceLocation("minestuck", "textures/gui/large_alchemiter.png");
	private static final int guiWidth = 159, guiHeight = 102;
	private AlchemiterTileEntity alchemiter;
	private int itemQuantity;
	
	AlchemiterScreen(AlchemiterTileEntity te)
	{
		super(new TranslationTextComponent(TITLE));
		alchemiter = te;
		itemQuantity = 1;
	}
	
	public AlchemiterTileEntity getAlchemiter() {
		return alchemiter;
	}
	
	@Override
	public BlockPos getPosition()
	{
		return getAlchemiter().getPos();
	}
	
	@Override
	protected void init()
	{
		Button alchemize = new GuiButtonExt((width - 100) / 2, (height - guiHeight) / 2 + 110, 100, 20, "ALCHEMIZE", button -> alchemize());
		
		Button hundredsUp = new GuiButtonExt((width - guiWidth) / 2 + 10, (height - guiHeight) / 2 + 10, 18, 18, "^", button -> changeAmount(100));
		Button tensUp = new GuiButtonExt((width - guiWidth) / 2 + 31, (height - guiHeight) / 2 + 10, 18, 18, "^", button -> changeAmount(10));
		Button onesUp = new GuiButtonExt((width - guiWidth) / 2 + 52, (height - guiHeight) / 2 + 10, 18, 18, "^", button -> changeAmount(1));
		Button hundredsDown = new GuiButtonExt((width - guiWidth) / 2 + 10, (height - guiHeight) / 2 + 74, 18, 18, "v", button -> changeAmount(-100));
		Button tensDown = new GuiButtonExt((width - guiWidth) / 2 + 31, (height - guiHeight) / 2 + 74, 18, 18, "v", button -> changeAmount(-10));
		Button onesDown = new GuiButtonExt((width - guiWidth) / 2 + 52, (height - guiHeight) / 2 + 74, 18, 18, "v", button -> changeAmount(-1));
		
		addButton(alchemize);
		GristSet cost = alchemiter.getGristCost(1);
		//don't add the buttons if the item is free or unalchemizeable
		if(cost != null && !cost.isEmpty())
		{
			addButton(onesUp);
			addButton(tensUp);
			addButton(hundredsUp);
			addButton(onesDown);
			addButton(tensDown);
			addButton(hundredsDown);
		}
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		int xOffset = (width - guiWidth) / 2;
		int yOffset = (height - guiHeight) / 2;
		
		this.renderBackground();
		
		
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		this.minecraft.getTextureManager().bindTexture(guiBackground);
		this.blit(xOffset, yOffset, 0, 0, guiWidth, guiHeight);
		
		font.drawString(Integer.toString(((int) (itemQuantity / Math.pow(10, 2)) % 10)), (width - guiWidth) / 2F + 15, (height - guiHeight) / 2F + 46, 16777215);
		font.drawString(Integer.toString(((int) (itemQuantity / Math.pow(10, 1)) % 10)), (width - guiWidth) / 2F + 36, (height - guiHeight) / 2F + 46, 16777215);
		font.drawString(Integer.toString(((int) (itemQuantity / Math.pow(10, 0)) % 10)), (width - guiWidth) / 2F + 57, (height - guiHeight) / 2F + 46, 16777215);
		
		//Render grist requirements
		
		
		//Calculate the grist set
		GristSet set;
		set = alchemiter.getGristCost(itemQuantity);
		//draw the grist board	//TODO Handle select mode correctly
		GuiUtil.drawGristBoard(set, AlchemyRecipes.getDecodedItem(alchemiter.getDowel()).getItem() == MSItems.CAPTCHA_CARD ? GuiUtil.GristboardMode.LARGE_ALCHEMITER_SELECT : GuiUtil.GristboardMode.LARGE_ALCHEMITER, (width - guiWidth) / 2 + 88, (height - guiHeight) / 2 + 13, font);
		//draw the grist
		List<String> tooltip = GuiUtil.getGristboardTooltip(set, GuiUtil.GristboardMode.LARGE_ALCHEMITER, mouseX, mouseY, 9, 45, font);
		if(!tooltip.isEmpty())
			this.renderTooltip(tooltip, mouseX, mouseY, font);
		super.render(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public boolean isPauseScreen()
	{
		return false;
	}
	
	private void alchemize()
	{
		AlchemiterPacket packet = new AlchemiterPacket(alchemiter.getPos(), itemQuantity);
		MSPacketHandler.sendToServer(packet);
		this.minecraft.displayGuiScreen(null);
	}
	
	private void changeAmount(int change)
	{
		//the amount the button changes the amount
		int result = itemQuantity + change;
		int maxCount = Math.min(999, alchemiter.getOutput().getMaxStackSize() * MinestuckConfig.alchemiterMaxStacks.get());
		
		itemQuantity = MathHelper.clamp(result, 1, maxCount);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
	{
		if(mouseButton == 0 && minecraft.player.inventory.getItemStack().isEmpty()
				&& alchemiter.getDowel() != null && AlchemyRecipes.getDecodedItem(alchemiter.getDowel()).getItem() == MSItems.CAPTCHA_CARD
				&& mouseX >= (width-guiWidth)/2F +80  && mouseX < (width-guiWidth)/2F + 150 && mouseY >= (height-guiHeight)/2F + 8 && mouseY < (height-guiHeight)/2F + 93)
		{
			minecraft.currentScreen = new GristSelectorScreen(this);
			minecraft.currentScreen.init(minecraft, width, height);
			return true;
		}
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
}
