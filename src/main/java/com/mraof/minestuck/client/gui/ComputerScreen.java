package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.computer.ComputerProgram;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.widget.ExtendedButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class ComputerScreen extends Screen
{
	
	public static final String TITLE = "minestuck.computer";
	public static final ResourceLocation guiBackground = new ResourceLocation(Minestuck.MOD_ID, "textures/gui/sburb.png");
	public static final ResourceLocation guiBsod = new ResourceLocation(Minestuck.MOD_ID, "textures/gui/bsod_message.png");
	
	public static final int xSize = 176;
	public static final int ySize = 166;
	
	private final List<ComputerIcon> icons;
	private XButton xButton;
	public final ComputerBlockEntity be;
	public ComputerProgram program;
	
	ComputerScreen(Minecraft mc, ComputerBlockEntity be)
	{
		super(Component.translatable(TITLE));
		
		this.minecraft = mc;
		this.font = minecraft.font;
		this.be = be;
		this.icons = new ArrayList<>();
		be.gui = this;
	}
	
	@Override
	public void init()
	{
		super.init();
		genIcons();
		xButton = addRenderableWidget(new XButton());
		setProgram(be.programSelected);
	}
	
	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(poseStack);
		boolean bsod = be.isBroken();
		int xOffset = (this.width / 2) - (xSize / 2);
		int yOffset = (this.height / 2) - (ySize / 2);
		
		//outside gui bits
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.setShaderTexture(0, bsod ? guiBsod : guiBackground);
		blit(poseStack, xOffset, yOffset, 0, 0, xSize, ySize);
		// Sburb logo
		blit(poseStack, xOffset+7, yOffset+6, 0, ySize, 77, 26);
		
		if(!bsod)
		{
			//desktop background
			RenderSystem.setShaderTexture(0, this.be.theme.getTexture());
			blit(poseStack, xOffset+9, yOffset+38, 0, 0, 158, 120);
			
			if(program != null) program.paintGui(poseStack, this, be);
			
			super.render(poseStack, mouseX, mouseY, partialTicks);
		}
	}
	
	private void xButton(Button b)
	{
		if(program == null)
			minecraft.setScreen(null);
		else
			exitProgram();
	}
	
	public void updateGui()
	{
		if(program!=null) program.onUpdateGui(this);
	}
	
	protected void genIcons()
	{
		var xOffset = (width-xSize)/2;
		var yOffset = (height-ySize)/2;
		
		// TODO WRAP ONCE LONG ENOUGH
		for(Entry<Integer, Boolean> entry : be.installedPrograms.entrySet())
			if(entry.getValue())
				icons.add( addRenderableWidget(new ComputerIcon(xOffset+15, yOffset+44 + entry.getKey()*20, entry.getKey())) );
	}
	
	protected void setProgram(int id)
	{
		if(be.isBroken() || id == -1 || id == -2)
			return;
		
		program = ComputerProgram.getProgram(id);
		if(program==null) return;
		
		be.programSelected = id;
		program.onInitGui(this);
		
		for(ComputerIcon icon : icons)
			icon.visible = false;
		
		updateGui();
	}
	
	protected void exitProgram()
	{
		program = null;
		be.programSelected = -1;
		
		//TODO find way to make this only clear program widgets so we don't regen icons every time
		clearWidgets();
		icons.clear();
		
		genIcons();
		addRenderableWidget(xButton);
		
		updateGui();
	}
	
	@Override
	public boolean shouldCloseOnEsc() { return false; }
	
	@Override
	public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers)
	{
		if(pKeyCode == 256) // close programs on esc
		{
			if(program != null) exitProgram();
			else onClose();
			
			return true;
		}
		
		return super.keyPressed(pKeyCode, pScanCode, pModifiers);
	}
	
	// make this method public so that programs can add widgets
	@Override
	public <T extends GuiEventListener & Widget & NarratableEntry> T addRenderableWidget(T button) {return super.addRenderableWidget(button);}
	
	@Override
	public boolean isPauseScreen()
	{
		return false;
	}
	
	private class ComputerIcon extends ExtendedButton
	{
		private final ComputerProgram program;
		private static final int WIDTH = 16, HEIGHT = 16;
		
		public ComputerIcon(int xPos, int yPos, int id)
		{
			super(xPos, yPos, WIDTH, HEIGHT, Component.empty(), button -> setProgram(id));
			
			this.program = ComputerProgram.getProgram(id);
		}
		
		@Override
		public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTick)
		{
			if(!visible) return;
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.setShaderColor(1, 1, 1, 1);
			RenderSystem.setShaderTexture(0, this.program.getIcon());
			
			blit(poseStack, x, y, WIDTH, HEIGHT, 0, 0, WIDTH, HEIGHT, WIDTH, HEIGHT);
		}
	}
	
	private class XButton extends ExtendedButton
	{
		
		public XButton()
		{
			super((ComputerScreen.this.width-xSize)/2 +153, (ComputerScreen.this.height-ySize)/2 +7, 16, 16, Component.empty(), ComputerScreen.this::xButton);
		}
		
		@Override
		public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTick)
		{
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.setShaderColor(1, 1, 1, 1);
			RenderSystem.setShaderTexture(0, be.theme.getTexture());
			blit(poseStack, x, y, 158, 40, 20, 20);
		}
	}
}