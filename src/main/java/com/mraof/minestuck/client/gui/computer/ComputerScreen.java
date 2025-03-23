package com.mraof.minestuck.client.gui.computer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.computer.ProgramType;
import com.mraof.minestuck.computer.ProgramTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * The screen that is shown whenever the computer is first opened. Equivalent to the desktop.
 * Upon closing any program, the player will be taken here.
 */
@ParametersAreNonnullByDefault
public class ComputerScreen extends ThemedScreen
{
	//TODO continually check that player is in reach of the computer
	public static final String TITLE = "minestuck.computer";
	
	private final List<ComputerIcon> icons;
	@Nullable
	private TypedProgramGui<?> program;
	
	public ComputerScreen(Minecraft mc, ComputerBlockEntity computer)
	{
		super(computer, Component.translatable(TITLE));
		
		this.minecraft = mc;
		this.font = minecraft.font;
		this.icons = new ArrayList<>();
		
		computer.setGuiCallback(this::updateGui);
	}
	
	@Override
	public void init()
	{
		genIcons();
		super.init();
		if(program != null)
			program.gui.onInit(this);
		updateGui();
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		
		boolean bsod = computer.isBroken();
		
		if(!bsod)
		{
			//program and widgets
			if(program != null)
				program.gui.render(guiGraphics, this);
		}
	}
	
	public void updateGui()
	{
		if(program != null)
			program.updateGui(this);
		
		boolean shouldShowIcons = this.program == null && !this.computer.isBroken();
		this.icons.forEach(icon -> icon.visible = shouldShowIcons);
	}
	
	protected void setProgram(ProgramType<?> programType)
	{
		if(computer.isBroken())
			return;
		
		program = new TypedProgramGui<>(programType);
		program.gui.onInit(this);
		
		updateGui();
	}
	
	protected void exitProgram()
	{
		program = null;
		
		clearWidgets();
		icons.forEach(this::addRenderableWidget);
		addRenderableWidget(powerButton);
		
		updateGui();
	}
	
	private void genIcons()
	{
		var xOffset = (width - GUI_WIDTH) / 2;
		var yOffset = (height - GUI_HEIGHT) / 2;
		
		icons.clear();
		
		int programCount = 0;
		for(ProgramType<?> programType : computer.installedPrograms().sorted(ProgramTypes.DISPLAY_ORDER_SORTER).toList())
		{
			icons.add(addRenderableWidget(new ComputerIcon(
					xOffset + 15 + Math.floorDiv(programCount, 5) * 20,
					yOffset + 44 + programCount % 5 * 20, programType)
			));
			programCount++;
		}
	}
	
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
	
	private static final class TypedProgramGui<D extends ProgramType.Data>
	{
		private final ProgramType<D> type;
		private final ProgramGui<D> gui;
		
		TypedProgramGui(ProgramType<D> type)
		{
			this.type = type;
			this.gui = ProgramGui.Registry.createGuiInstance(type);
		}
		
		void updateGui(ComputerScreen screen)
		{
			Optional<D> programData = screen.computer.getProgramData(this.type);
			if(programData.isPresent())
				this.gui.onUpdate(screen, programData.get());
			else
				screen.exitProgram();
		}
	}
	
	private class ComputerIcon extends ExtendedButton
	{
		private final ResourceLocation icon;
		private static final int WIDTH = 16, HEIGHT = 16;
		
		public ComputerIcon(int xPos, int yPos, ProgramType<?> programType)
		{
			super(xPos, yPos, WIDTH, HEIGHT, Component.empty(), button -> setProgram(programType));
			
			ResourceLocation programKey = Objects.requireNonNullElse(ProgramTypes.REGISTRY.getKey(programType), Minestuck.id("invalid"));
			this.icon = programKey.withPath(name -> "textures/gui/desktop_icon/" + name + ".png");
			
			setTooltip(Tooltip.create(programType.name()));
		}
		
		@Override
		public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
		{
			if(!visible)
				return;
			
			RenderSystem.setShaderColor(1, 1, 1, 1);
			
			guiGraphics.blit(this.icon, getX(), getY(), WIDTH, HEIGHT, 0, 0, WIDTH, HEIGHT, WIDTH, HEIGHT);
		}
	}
}
