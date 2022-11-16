package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.TitleSelectPacket;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.EnumClass;
import com.mraof.minestuck.player.Title;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.widget.ExtendedButton;

public class TitleSelectorScreen extends Screen
{
	public static final String TITLE = "minestuck.title_selector";
	public static final String SELECT_TITLE = "minestuck.select_title";
	public static final String USED_TITLE = "minestuck.select_title.used";
	public static final String SELECT = "minestuck.select_title.select";
	public static final String RANDOMIZE = "minestuck.select_title.randomize";
	
	private static final ResourceLocation guiBackground = new ResourceLocation("minestuck", "textures/gui/title_selector.png");
	private static final int guiWidth = 186, guiHeight = 157;
	
	private EnumClass currentClass;
	private EnumAspect currentAspect;
	private final Button[] classButtons = new Button[12], aspectButtons = new Button[12];
	private Button selectButton;
	
	private final Title previous;
	
	TitleSelectorScreen(Title title)
	{
		super(new TranslatableComponent(TITLE));
		previous = title;
	}
	
	@Override
	public void init()
	{
		int leftX = (width - guiWidth) / 2, topY = (height - guiHeight) / 2;
		for(EnumClass c : EnumClass.values())
		{
			int i = EnumClass.getIntFromClass(c);
			if(i < 12)
			{
				Component className = c.asTextComponent();
				Button button = new ExtendedButton(leftX + 4 + (i % 2) * 40, topY + 24 + (i / 2) * 16, 40, 16, className, button1 -> pickClass(c));
				addRenderableWidget(button);
				classButtons[i] = button;
			}
		}
		for(EnumAspect a : EnumAspect.values())
		{
			int i = EnumAspect.getIntFromAspect(a);
			if(i < 12)
			{
				Component aspectName = a.asTextComponent();
				Button button = new ExtendedButton(leftX + 102 + (i % 2) * 40, topY + 24 + (i / 2) * 16, 40, 16, aspectName, button1 -> pickAspect(a));
				addRenderableWidget(button);
				aspectButtons[i] = button;
			}
		}
		
		addRenderableWidget(selectButton = new ExtendedButton(leftX + 24, topY + 128, 60, 20, new TranslatableComponent(SELECT), button -> select()));
		addRenderableWidget(new ExtendedButton(leftX + 102, topY + 128, 60, 20, new TranslatableComponent(RANDOMIZE), button -> random()));
	}
	
	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks)
	{
		selectButton.active = currentClass != null && currentAspect != null;
		
		int xOffset = (width - guiWidth)/2;
		int yOffset = (height - guiHeight)/2;
		
		this.renderBackground(poseStack);
		
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.setShaderTexture(0, guiBackground);
		this.blit(poseStack, xOffset, yOffset, 0, 0, guiWidth, guiHeight);
		
		String message = previous == null ? I18n.get(SELECT_TITLE) : I18n.get(USED_TITLE, previous.asTextComponent().getString());
		font.draw(poseStack, message, (this.width / 2F) - font.width(message) / 2F, yOffset + 10, 0x404040);
		
		message = I18n.get(Title.FORMAT, "", "");
		font.draw(poseStack, message, (this.width / 2F) - font.width(message) / 2F, yOffset + 72 - font.lineHeight/2F, 0x404040);
		
		super.render(poseStack, mouseX, mouseY, partialTicks);
		
	}
	
	private void pickClass(EnumClass c)
	{
		if(currentClass != null)
			classButtons[EnumClass.getIntFromClass(currentClass)].active = true;
		currentClass = c;
		classButtons[EnumClass.getIntFromClass(currentClass)].active = false;
	}
	
	private void pickAspect(EnumAspect a)
	{
		if(currentAspect != null)
			aspectButtons[EnumAspect.getIntFromAspect(currentAspect)].active = true;
		currentAspect = a;
		aspectButtons[EnumAspect.getIntFromAspect(currentAspect)].active = false;
	}
	
	private void select()
	{
		MSPacketHandler.sendToServer(new TitleSelectPacket(new Title(currentClass, currentAspect)));
		onClose();
	}
	
	private void random()
	{
		MSPacketHandler.sendToServer(new TitleSelectPacket());
		onClose();
	}
	
	@Override
	public boolean shouldCloseOnEsc()
	{
		return false;
	}
}