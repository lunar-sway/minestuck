package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class PortraitDialogueScreen extends DialogueScreen
{
	private static final ResourceLocation PORTRAIT_BACKGROUND = new ResourceLocation("minestuck", "textures/gui/dialogue_portrait.png");
	private static final ResourceLocation TEST_PORTRAIT = new ResourceLocation("minestuck", "textures/gui/dialogue/salamander.png");
	
	private static final int GUI_WIDTH = 90;
	private static final int GUI_HEIGHT = 90;
	private static final int GUI_SPACING = 10;
	
	private ResourceLocation portrait;
	
	protected PortraitDialogueScreen(List<String> paragraphs, ResourceLocation portrait)
	{
		super(paragraphs);
		this.portrait = portrait;
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		super.render(mouseX, mouseY, partialTicks);
		
		int xOffset = getXOffset() - GUI_WIDTH - GUI_SPACING;
		int yOffset = getYOffset();
		
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		this.minecraft.getTextureManager().bindTexture(PORTRAIT_BACKGROUND);
		this.blit(xOffset, yOffset, 0, 0, GUI_WIDTH, GUI_HEIGHT);
		
		this.minecraft.getTextureManager().bindTexture(portrait);
		this.blit(xOffset, yOffset, 0, 0, GUI_WIDTH, GUI_HEIGHT);
	}
	
	@Override
	protected int getXOffset()
	{
		return super.getXOffset() + (GUI_WIDTH / 2) + GUI_SPACING;
	}
}
