package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.computer.ComputerProgram;
import com.mraof.minestuck.tileentity.ComputerTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class ComputerScreen extends Screen
{
	
	public static final ResourceLocation guiBackground = new ResourceLocation("minestuck", "textures/gui/sburb.png");
	public static final ResourceLocation guiBsod = new ResourceLocation("minestuck", "textures/gui/bsod_message.png");
	
	public static final int xSize = 176;
	public static final int ySize = 166;
	
	public Button programButton;
	
	public Minecraft mc;
	public ComputerTileEntity te;
	private ComputerProgram program;
	
	ComputerScreen(Minecraft mc, ComputerTileEntity te)
	{
		super(new StringTextComponent("Computer"));
		
		this.mc = mc;
		this.font = mc.font;
		this.te = te;
		te.gui = this;
	}
	
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(matrixStack);
		
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		if(te.hasProgram(-1))
		{
			this.mc.getTextureManager().bind(guiBsod);
			int yOffset = (this.height / 2) - (ySize / 2);
			this.blit(matrixStack, (this.width / 2) - (xSize / 2), yOffset, 0, 0, xSize, ySize);
		} else if(program != null)
			program.paintGui(matrixStack, this, te);
		else
		{
			this.mc.getTextureManager().bind(guiBackground);
			int yOffset = (this.height / 2) - (ySize / 2);
			this.blit(matrixStack, (this.width / 2) - (xSize / 2), yOffset, 0, 0, xSize, ySize);
			font.draw(matrixStack, "Insert disk.", (width - xSize) / 2F + 15, (height - ySize) / 2F + 45, 4210752);
		}
		RenderSystem.disableRescaleNormal();
		RenderHelper.turnOff();
		RenderSystem.disableLighting();
		RenderSystem.disableDepthTest();
		
		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}
	
	@Override
	public boolean isPauseScreen()
	{
		return false;
	}
	
	@Override
	public void init()
	{
		super.init();
		
		if(te.programSelected == -1 && !te.hasProgram(-1))
			for(Entry<Integer, Boolean> entry : te.installedPrograms.entrySet())
				if(entry.getValue() && (te.programSelected == -1 || te.programSelected > entry.getKey()))
					te.programSelected = entry.getKey();
		
		if(te.programSelected != -1 && (program == null || program.getId() != te.programSelected))
			program = ComputerProgram.getProgram(te.programSelected);
		
		programButton = new ExtendedButton((width - xSize) / 2 + 95, (height - ySize) / 2 + 10, 70, 20, StringTextComponent.EMPTY, button -> changeProgram());
		addButton(programButton);
		if(te.programSelected != -1)
			program.onInitGui(this, null);
		
		updateGui();
	}
	
	public void updateGui()
	{
		
		programButton.active = te.installedPrograms.size() > 1;
		
		if(te.hasProgram(-1))
		{
			clearButtons();
			return;
		}
		
		if(program != null)
		{
			program.onUpdateGui(this);
			programButton.setMessage(new TranslationTextComponent(program.getName()));
		}
		
	}
	
	private void changeProgram()
	{
		if(te.hasProgram(-1))
			return;
		
		te.programSelected = getNextProgram();
		ComputerProgram prevProgram = program;
		program = ComputerProgram.getProgram(te.programSelected);
		if(program != null)
			program.onInitGui(this, prevProgram);
		
		updateGui();
	}
	
	//TODO when both server and client programs are installed, disk burner is no longer iterated to
	private int getNextProgram()
	{
		if(te.installedPrograms.size() == 1)
		{
			return te.programSelected;
		}
		Iterator<Entry<Integer, Boolean>> it = te.installedPrograms.entrySet().iterator();
		//int place = 0;
		boolean found = false;
		int lastProgram = te.programSelected;
		while(it.hasNext())
		{
			Map.Entry<Integer, Boolean> pairs = it.next();
			int program = pairs.getKey();
			if(found)
			{
				return program;
			} else if(program == te.programSelected)
			{
				found = true;
			} else
			{
				lastProgram = program;
			}
			//place++;
		}
		return lastProgram;
	}
	
	@Override
	public <T extends Widget> T addButton(T buttonIn)
	{
		return super.addButton(buttonIn);
	}
	
	public void clearButtons()
	{
		children.removeAll(buttons);
		buttons.clear();
	}
}
