package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.client.util.GuiUtil;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.alchemy.AlchemyHelper;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.network.AlchemiterPacket;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.blockentity.machine.AlchemiterBlockEntity;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.client.gui.widget.ExtendedButton;

public class AlchemiterScreen extends Screen implements Positioned
{
	public static final String TITLE = "minestuck.alchemiter";
	
	private static final ResourceLocation guiBackground = new ResourceLocation("minestuck", "textures/gui/large_alchemiter.png");
	private static final int guiWidth = 159, guiHeight = 102;
	private final AlchemiterBlockEntity alchemiter;
	private int itemQuantity;
	
	AlchemiterScreen(AlchemiterBlockEntity be)
	{
		super(new TranslatableComponent(TITLE));
		alchemiter = be;
		itemQuantity = 1;
	}
	
	public AlchemiterBlockEntity getAlchemiter() {
		return alchemiter;
	}
	
	@Override
	public BlockPos getPosition()
	{
		return getAlchemiter().getBlockPos();
	}
	
	@Override
	protected void init()
	{
		Button alchemize = new ExtendedButton((width - 100) / 2, (height - guiHeight) / 2 + 110, 100, 20, new TextComponent("ALCHEMIZE"), button -> alchemize());
		addRenderableWidget(alchemize);
		
		GristSet cost = alchemiter.getGristCost(1);
		//don't add the buttons if the item is free or unalchemizeable
		if(cost != null && !cost.isEmpty())
		{
			Button hundredsUp = new ExtendedButton((width - guiWidth) / 2 + 10, (height - guiHeight) / 2 + 10, 18, 18, new TextComponent("^"), button -> changeAmount(100));
			Button tensUp = new ExtendedButton((width - guiWidth) / 2 + 31, (height - guiHeight) / 2 + 10, 18, 18, new TextComponent("^"), button -> changeAmount(10));
			Button onesUp = new ExtendedButton((width - guiWidth) / 2 + 52, (height - guiHeight) / 2 + 10, 18, 18, new TextComponent("^"), button -> changeAmount(1));
			Button hundredsDown = new ExtendedButton((width - guiWidth) / 2 + 10, (height - guiHeight) / 2 + 74, 18, 18, new TextComponent("v"), button -> changeAmount(-100));
			Button tensDown = new ExtendedButton((width - guiWidth) / 2 + 31, (height - guiHeight) / 2 + 74, 18, 18, new TextComponent("v"), button -> changeAmount(-10));
			Button onesDown = new ExtendedButton((width - guiWidth) / 2 + 52, (height - guiHeight) / 2 + 74, 18, 18, new TextComponent("v"), button -> changeAmount(-1));
			
			addRenderableWidget(onesUp);
			addRenderableWidget(tensUp);
			addRenderableWidget(hundredsUp);
			addRenderableWidget(onesDown);
			addRenderableWidget(tensDown);
			addRenderableWidget(hundredsDown);
		}
	}
	
	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks)
	{
		int xOffset = (width - guiWidth) / 2;
		int yOffset = (height - guiHeight) / 2;
		
		this.renderBackground(poseStack);
		
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.setShaderTexture(0, guiBackground);
		this.blit(poseStack, xOffset, yOffset, 0, 0, guiWidth, guiHeight);
		
		font.draw(poseStack, Integer.toString(((int) (itemQuantity / Math.pow(10, 2)) % 10)), (width - guiWidth) / 2F + 15, (height - guiHeight) / 2F + 46, 16777215);
		font.draw(poseStack, Integer.toString(((int) (itemQuantity / Math.pow(10, 1)) % 10)), (width - guiWidth) / 2F + 36, (height - guiHeight) / 2F + 46, 16777215);
		font.draw(poseStack, Integer.toString(((int) (itemQuantity / Math.pow(10, 0)) % 10)), (width - guiWidth) / 2F + 57, (height - guiHeight) / 2F + 46, 16777215);
		
		//Render grist requirements
		
		
		//Calculate the grist set
		GristSet set;
		set = alchemiter.getGristCost(itemQuantity);
		//draw the grist board	//TODO Handle select mode correctly
		GuiUtil.drawGristBoard(poseStack, set, AlchemyHelper.getDecodedItem(alchemiter.getDowel()).getItem() == MSItems.CAPTCHA_CARD.get() ? GuiUtil.GristboardMode.LARGE_ALCHEMITER_SELECT : GuiUtil.GristboardMode.LARGE_ALCHEMITER, (width - guiWidth) / 2 + 88, (height - guiHeight) / 2 + 13, font);
		//draw the grist
		Component tooltip = GuiUtil.getGristboardTooltip(set, GuiUtil.GristboardMode.LARGE_ALCHEMITER, mouseX, mouseY, 9, 45, font);
		if(tooltip != null)
			this.renderTooltip(poseStack, tooltip, mouseX, mouseY);
		super.render(poseStack, mouseX, mouseY, partialTicks);
	}
	
	@Override
	public boolean isPauseScreen()
	{
		return false;
	}
	
	private void alchemize()
	{
		AlchemiterPacket packet = new AlchemiterPacket(alchemiter.getBlockPos(), itemQuantity);
		MSPacketHandler.sendToServer(packet);
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
			minecraft.screen = new GristSelectorScreen<>(this);
			minecraft.screen.init(minecraft, width, height);
			return true;
		}
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
}
