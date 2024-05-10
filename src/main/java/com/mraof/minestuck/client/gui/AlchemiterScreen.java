package com.mraof.minestuck.client.gui;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.alchemy.AlchemyHelper;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.blockentity.machine.AlchemiterBlockEntity;
import com.mraof.minestuck.client.util.GuiUtil;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.network.block.TriggerAlchemiterPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class AlchemiterScreen extends Screen
{
	public static final String TITLE = "minestuck.alchemiter";
	
	private static final ResourceLocation guiBackground = new ResourceLocation("minestuck", "textures/gui/large_alchemiter.png");
	private static final int guiWidth = 159, guiHeight = 102;
	private final AlchemiterBlockEntity alchemiter;
	private int itemQuantity;
	
	AlchemiterScreen(AlchemiterBlockEntity be)
	{
		super(Component.translatable(TITLE));
		alchemiter = be;
		itemQuantity = 1;
	}
	
	public AlchemiterBlockEntity getAlchemiter() {
		return alchemiter;
	}
	
	@Override
	protected void init()
	{
		Button alchemize = new ExtendedButton((width - 100) / 2, (height - guiHeight) / 2 + 110, 100, 20, Component.literal("ALCHEMIZE"), button -> alchemize());
		addRenderableWidget(alchemize);
		
		GristSet cost = alchemiter.getGristCost(1);
		//don't add the buttons if the item is free or unalchemizeable
		if(cost != null && !cost.isEmpty())
		{
			Button hundredsUp = new ExtendedButton((width - guiWidth) / 2 + 10, (height - guiHeight) / 2 + 10, 18, 18, Component.literal("^"), button -> changeAmount(100));
			Button tensUp = new ExtendedButton((width - guiWidth) / 2 + 31, (height - guiHeight) / 2 + 10, 18, 18, Component.literal("^"), button -> changeAmount(10));
			Button onesUp = new ExtendedButton((width - guiWidth) / 2 + 52, (height - guiHeight) / 2 + 10, 18, 18, Component.literal("^"), button -> changeAmount(1));
			Button hundredsDown = new ExtendedButton((width - guiWidth) / 2 + 10, (height - guiHeight) / 2 + 74, 18, 18, Component.literal("v"), button -> changeAmount(-100));
			Button tensDown = new ExtendedButton((width - guiWidth) / 2 + 31, (height - guiHeight) / 2 + 74, 18, 18, Component.literal("v"), button -> changeAmount(-10));
			Button onesDown = new ExtendedButton((width - guiWidth) / 2 + 52, (height - guiHeight) / 2 + 74, 18, 18, Component.literal("v"), button -> changeAmount(-1));
			
			addRenderableWidget(onesUp);
			addRenderableWidget(tensUp);
			addRenderableWidget(hundredsUp);
			addRenderableWidget(onesDown);
			addRenderableWidget(tensDown);
			addRenderableWidget(hundredsDown);
		}
	}
	
	@Override
	public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		super.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
		
		int xOffset = (width - guiWidth) / 2;
		int yOffset = (height - guiHeight) / 2;
		
		guiGraphics.blit(guiBackground, xOffset, yOffset, 0, 0, guiWidth, guiHeight);
		
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		
		guiGraphics.drawString(font, Integer.toString(((int) (itemQuantity / Math.pow(10, 2)) % 10)), (width - guiWidth) / 2F + 15, (height - guiHeight) / 2F + 46, 0xFFFFFF, false);
		guiGraphics.drawString(font, Integer.toString(((int) (itemQuantity / Math.pow(10, 1)) % 10)), (width - guiWidth) / 2F + 36, (height - guiHeight) / 2F + 46, 0xFFFFFF, false);
		guiGraphics.drawString(font, Integer.toString(((int) (itemQuantity / Math.pow(10, 0)) % 10)), (width - guiWidth) / 2F + 57, (height - guiHeight) / 2F + 46, 0xFFFFFF, false);
		
		//Render grist requirements
		
		
		//Calculate the grist set
		GristSet set = alchemiter.getGristCost(itemQuantity);
		//draw the grist board	//TODO Handle select mode correctly
		GuiUtil.drawGristBoard(guiGraphics, set, AlchemyHelper.getDecodedItem(alchemiter.getDowel()).getItem() == MSItems.CAPTCHA_CARD.get() ? GuiUtil.GristboardMode.LARGE_ALCHEMITER_SELECT : GuiUtil.GristboardMode.LARGE_ALCHEMITER, (width - guiWidth) / 2 + 88, (height - guiHeight) / 2 + 13, font);
		//draw the grist
		Component tooltip = GuiUtil.getGristboardTooltip(set, GuiUtil.GristboardMode.LARGE_ALCHEMITER, mouseX, mouseY, 9, 45, font);
		if(tooltip != null)
			guiGraphics.renderTooltip(font, tooltip, mouseX, mouseY);
	}
	
	@Override
	public boolean isPauseScreen()
	{
		return false;
	}
	
	private void alchemize()
	{
		PacketDistributor.SERVER.noArg().send(new TriggerAlchemiterPacket(itemQuantity, alchemiter.getBlockPos()));
		this.minecraft.setScreen(null);
	}
	
	private void changeAmount(int change)
	{
		//the amount the button changes the amount
		int result = itemQuantity + change;
		int maxCount = Math.min(999, alchemiter.getOutput().getMaxStackSize() * MinestuckConfig.SERVER.alchemiterMaxStacks.get());
		
		itemQuantity = Mth.clamp(result, 1, maxCount);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
	{
		if(mouseButton == 0
				&& alchemiter.getDowel() != null && AlchemyHelper.getDecodedItem(alchemiter.getDowel()).getItem() == MSItems.CAPTCHA_CARD.get()
				&& mouseX >= (width-guiWidth)/2F +80  && mouseX < (width-guiWidth)/2F + 150 && mouseY >= (height-guiHeight)/2F + 8 && mouseY < (height-guiHeight)/2F + 93)
		{
			minecraft.pushGuiLayer(new GristSelectorScreen(this.getAlchemiter().getBlockPos()));
			return true;
		}
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
}
