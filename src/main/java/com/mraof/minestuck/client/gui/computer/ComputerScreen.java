package com.mraof.minestuck.client.gui.computer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.computer.ProgramType;
import com.mraof.minestuck.computer.theme.ComputerTheme;
import com.mraof.minestuck.computer.theme.ComputerThemes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;
import org.lwjgl.glfw.GLFW;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//TODO continually check that player is in reach of the computer
@ParametersAreNonnullByDefault
public class ComputerScreen extends Screen
{
	public static final String TITLE = "minestuck.computer";
	public static final ResourceLocation guiMain = ResourceLocation.fromNamespaceAndPath(Minestuck.MOD_ID, "textures/gui/sburb.png");
	public static final ResourceLocation guiBsod = ResourceLocation.fromNamespaceAndPath(Minestuck.MOD_ID, "textures/gui/bsod_message.png");
	
	public static final int xSize = 176;
	public static final int ySize = 166;
	
	public final ComputerBlockEntity be;
	private final List<ComputerIcon> icons;
	private PowerButton powerButton;
	private ProgramGui program;
	private ComputerTheme cachedTheme;
	
	public ComputerScreen(Minecraft mc, ComputerBlockEntity be)
	{
		super(Component.translatable(TITLE));
		
		this.minecraft = mc;
		this.font = minecraft.font;
		this.be = be;
		this.icons = new ArrayList<>();
		this.cachedTheme = ComputerThemes.instance().lookup(be.getTheme());
		be.setGuiCallback(this::updateGui);
	}
	
	@Override
	public void init()
	{
		genIcons();
		powerButton = addRenderableWidget(new PowerButton());
		if(be.programSelected != null)
			setProgram(be.programSelected);
	}
	
	@Override
	public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		super.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
		
		boolean bsod = be.isBroken();
		int xOffset = (this.width / 2) - (xSize / 2);
		int yOffset = (this.height / 2) - (ySize / 2);
		
		//outside gui bits
		RenderSystem.setShaderColor(1, 1, 1, 1);
		guiGraphics.blit(guiMain, xOffset, yOffset, 0, 0, xSize, ySize);
		
		if(bsod)
			guiGraphics.blit(guiBsod, xOffset+9, yOffset+38, 0, 0, 158, 120);
		else
			guiGraphics.blit(this.getTheme().data().texturePath(), xOffset + 9, yOffset + 38, 0, 0, 158, 120);
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		
		boolean bsod = be.isBroken();
		int xOffset = (this.width / 2) - (xSize / 2);
		int yOffset = (this.height / 2) - (ySize / 2);
		
		if(!bsod)
		{
			//program and widgets
			if(program != null)
				program.render(guiGraphics, this);
		}
		
		//corner bits (goes on top of computer screen slightly)
		guiGraphics.blit(guiMain, xOffset + 9, yOffset + 38, xSize, 0, 3, 3);
		guiGraphics.blit(guiMain, xOffset + 164, yOffset + 38, xSize + 3, 0, 3, 3);
	}
	
	public ComputerTheme getTheme()
	{
		return cachedTheme;
	}
	
	public void updateGui()
	{
		if(!this.cachedTheme.id().equals(be.getTheme()))
			this.cachedTheme = ComputerThemes.instance().lookup(be.getTheme());
		if(program!=null) program.onUpdate(this);
	}
	
	protected void setProgram(ProgramType programType)
	{
		if(be.isBroken())
			return;
		
		program = ProgramGui.Registry.createGuiInstance(programType);
		if(program==null) return;
		
		be.programSelected = programType;
		program.onInit(this);
		
		for(ComputerIcon icon : icons)
			icon.visible = false;
		
		updateGui();
	}
	
	protected void exitProgram()
	{
		program = null;
		be.programSelected = null;
		
		clearWidgets();
		icons.forEach(this::addRenderableWidget);
		icons.forEach(icon -> icon.visible = true);
		addRenderableWidget(powerButton);
		
		updateGui();
	}
	
	protected void genIcons()
	{
		var xOffset = (width-xSize)/2;
		var yOffset = (height-ySize)/2;
		
		icons.clear();
		
		int programCount = 0;
		for(ProgramType programType : be.installedPrograms().sorted(ProgramType.DISPLAY_ORDER_SORTER).toList())
		{
			icons.add(addRenderableWidget(new ComputerIcon(
					xOffset + 15 + Math.floorDiv(programCount, 5) * 20,
					yOffset + 44 + programCount % 5 * 20, programType)
			));
			programCount++;
		}
	}
	
	@Override
	public boolean shouldCloseOnEsc() { return false; }
	
	@Override
	public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers)
	{
		if(pKeyCode == GLFW.GLFW_KEY_ESCAPE)
		{
			if(program == null)
				onClose();
			else
				exitProgram();
			
			return true;
		}
		
		return super.keyPressed(pKeyCode, pScanCode, pModifiers);
	}
	
	// make this method public so that programs can add widgets
	@Override
	public <T extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidget(T button) {return super.addRenderableWidget(button);}
	
	@Override
	public boolean isPauseScreen()
	{
		return false;
	}
	
	private class ComputerIcon extends ExtendedButton
	{
		private final ResourceLocation icon;
		private static final int WIDTH = 16, HEIGHT = 16;
		
		public ComputerIcon(int xPos, int yPos, ProgramType programType)
		{
			super(xPos, yPos, WIDTH, HEIGHT, Component.empty(), button -> setProgram(programType));
			
			String programName = Objects.requireNonNullElse(ProgramType.REGISTRY.inverse().get(programType), "invalid");
			this.icon = Minestuck.id("textures/gui/desktop_icon/" + programName + ".png");
		}
		
		@Override
		public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
		{
			if(!visible) return;
			RenderSystem.setShaderColor(1, 1, 1, 1);
			
			guiGraphics.blit(this.icon, getX(), getY(), WIDTH, HEIGHT, 0, 0, WIDTH, HEIGHT, WIDTH, HEIGHT);
		}
	}
	
	//TODO make this button have its own texture instead of just using screen  main
	private class PowerButton extends Button
	{
		public PowerButton()
		{
			super(builder(Component.empty(), b -> minecraft.setScreen(null))
					.pos((ComputerScreen.this.width-xSize)/2+143, (ComputerScreen.this.height-ySize)/2+3)
					.size(29, 29));
		}
		
		@Override
		public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float pt) { /* invisible */ }
	}
}
