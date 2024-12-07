package com.mraof.minestuck.client.gui;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.blockentity.machine.AlchemiterBlockEntity;
import com.mraof.minestuck.client.util.GuiUtil;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.components.EncodedItemComponent;
import com.mraof.minestuck.network.block.TriggerAlchemiterPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public final class AlchemiterScreen extends Screen
{
	public static final String TITLE = "minestuck.alchemiter";
	
	private static final ResourceLocation guiBackground = ResourceLocation.fromNamespaceAndPath("minestuck", "textures/gui/large_alchemiter.png");
	private static final int guiWidth = 159, guiHeight = 102;
	private final AlchemiterBlockEntity alchemiter;
	private int itemQuantity;
	
	AlchemiterScreen(AlchemiterBlockEntity be)
	{
		super(Component.translatable(TITLE));
		alchemiter = be;
		itemQuantity = 1;
	}
	
	public AlchemiterBlockEntity getAlchemiter()
	{
		return alchemiter;
	}
	
	@Override
	protected void init()
	{
		int xOffset = (width - guiWidth) / 2;
		int yOffset = (height - guiHeight) / 2;
		addRenderableWidget(new ExtendedButton(xOffset + 29, yOffset + 110, 100, 20, Component.literal("ALCHEMIZE"), button -> alchemize()));
		
		GristSet cost = alchemiter.getGristCost(1);
		//don't add the buttons if the item is free or unalchemizeable
		if(cost != null && !cost.isEmpty())
		{
			addRenderableWidget(new ExtendedButton(xOffset + 10, yOffset + 10, 18, 18,
					Component.literal("^"), button -> changeAmount(100)));
			addRenderableWidget(new ExtendedButton(xOffset + 31, yOffset + 10, 18, 18,
					Component.literal("^"), button -> changeAmount(10)));
			addRenderableWidget(new ExtendedButton(xOffset + 52, yOffset + 10, 18, 18,
					Component.literal("^"), button -> changeAmount(1)));
			
			addRenderableWidget(new ExtendedButton(xOffset + 10, yOffset + 74, 18, 18,
					Component.literal("v"), button -> changeAmount(-100)));
			addRenderableWidget(new ExtendedButton(xOffset + 31, yOffset + 74, 18, 18,
					Component.literal("v"), button -> changeAmount(-10)));
			addRenderableWidget(new ExtendedButton(xOffset + 52, yOffset + 74, 18, 18,
					Component.literal("v"), button -> changeAmount(-1)));
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
		//draw the grist board	//FIXME Handle wildcard grist costs instead of hardcoding to captcha card
		GuiUtil.GristboardMode boardMode = EncodedItemComponent.getEncodedOrBlank(alchemiter.getDowel()).is(MSItems.CAPTCHA_CARD)
				? GuiUtil.GristboardMode.LARGE_ALCHEMITER_SELECT : GuiUtil.GristboardMode.LARGE_ALCHEMITER;
		GuiUtil.drawGristBoard(guiGraphics, set, boardMode, (width - guiWidth) / 2 + 88, (height - guiHeight) / 2 + 13, font);
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
		PacketDistributor.sendToServer(new TriggerAlchemiterPacket(itemQuantity, alchemiter.getBlockPos()));
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
		float xOffset = (width - guiWidth) / 2F;
		float yOffset = (height - guiHeight) / 2F;
		if(mouseButton == 0    //FIXME Handle wildcard grist costs instead of hardcoding to captcha card
				&& !alchemiter.getDowel().isEmpty() && EncodedItemComponent.getEncodedOrBlank(alchemiter.getDowel()).is(MSItems.CAPTCHA_CARD)
				&& xOffset + 80 <= mouseX && mouseX < xOffset + 150 && yOffset + 8 <= mouseY && mouseY < yOffset + 93)
		{
			minecraft.pushGuiLayer(new GristSelectorScreen(this.getAlchemiter().getBlockPos()));
			return true;
		}
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}
}
