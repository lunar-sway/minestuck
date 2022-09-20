package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mraof.minestuck.computer.ComputerProgram;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.widget.ExtendedButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class ComputerScreen extends Screen
{
	
	public static final ResourceLocation guiBackground = new ResourceLocation("minestuck", "textures/gui/sburb.png");
	public static final ResourceLocation guiBsod = new ResourceLocation("minestuck", "textures/gui/bsod_message.png");
	
	public static final int xSize = 176;
	public static final int ySize = 166;
	
	private Button programButton;
	
	public final Minecraft mc;
	public final ComputerBlockEntity be;
	private ComputerProgram program;
	
	ComputerScreen(Minecraft mc, ComputerBlockEntity be)
	{
		super(new TextComponent("Computer"));
		
		this.mc = mc;
		this.font = mc.font;
		this.be = be;
		be.gui = this;
	}
	
	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(poseStack);
		boolean bsod = be.hasProgram(-1);
		int yOffset = (this.height / 2) - (ySize / 2);
		
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.setShaderTexture(0, bsod ? guiBsod : guiBackground);
		this.blit(poseStack, (this.width / 2) - (xSize / 2), yOffset, 0, 0, xSize, ySize);
		
		if(!bsod && program != null)
			program.paintGui(poseStack, this, be);
		else
		{
			font.draw(poseStack, "Insert disk.", (width - xSize) / 2F + 15, (height - ySize) / 2F + 45, 4210752);
		}
		
		super.render(poseStack, mouseX, mouseY, partialTicks);
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
		
		if(be.programSelected == -1 && !be.hasProgram(-1))
			for(Entry<Integer, Boolean> entry : be.installedPrograms.entrySet())
				if(entry.getValue() && (be.programSelected == -1 || be.programSelected > entry.getKey()))
					be.programSelected = entry.getKey();
		
		if(be.programSelected != -1 && (program == null || program.getId() != be.programSelected))
			program = ComputerProgram.getProgram(be.programSelected);
		
		programButton = new ExtendedButton((width - xSize) / 2 + 95, (height - ySize) / 2 + 10, 70, 20, TextComponent.EMPTY, button -> changeProgram());
		addRenderableWidget(programButton);
		if(be.programSelected != -1)
			program.onInitGui(this);
		
		updateGui();
	}
	
	public void updateGui()
	{
		
		programButton.active = be.installedPrograms.size() > 1;
		
		if(be.hasProgram(-1))
		{
			clearWidgets();
			return;
		}
		
		if(program != null)
		{
			program.onUpdateGui(this);
			programButton.setMessage(new TranslatableComponent(program.getName()));
		}
		
	}
	
	private void changeProgram()
	{
		if(be.hasProgram(-1))
			return;
		
		be.programSelected = getNextProgram();
		program = ComputerProgram.getProgram(be.programSelected);
		if(program != null)
		{
			clearWidgets();
			addRenderableWidget(programButton);
			program.onInitGui(this);
		}
		
		updateGui();
	}
	
	//TODO when all three programs exist there is no problem but if only server disk and disk burner exist then there is a crash
	private int getNextProgram()
	{
		/*if(be.installedPrograms.size() == 1)
		{
			return be.programSelected;
		}*/
		
		List<Integer> installedProgramOrdinals = new ArrayList<>();
		int highestNumberedProgram = 0;
		int nextInstalledProgram = be.programSelected;
		int lowestNumberedProgram = 0;
		
		for(Iterator<Integer> iterator = be.installedPrograms.keySet().iterator(); iterator.hasNext(); )
		{
			int key = iterator.next();
			installedProgramOrdinals.add(key);
			
			if(highestNumberedProgram < key)
				highestNumberedProgram = key;
			
			if(key == be.programSelected + 1)
				nextInstalledProgram = key;
			
			if(lowestNumberedProgram > key)
				lowestNumberedProgram = key;
		}
		
		if(installedProgramOrdinals.get(0) > installedProgramOrdinals.get(Math.max(0, installedProgramOrdinals.size() - 1)))
			Collections.reverse(installedProgramOrdinals); //for some reason, programs are added in reverse with the highest numbered program being the first element
		
		if(nextInstalledProgram <= be.programSelected)
		{
			for(int ordinal : installedProgramOrdinals)
			{
				if(ordinal > be.programSelected)
				{
					nextInstalledProgram = ordinal;
					break;
				}
			}
		}
		
		return be.programSelected >= highestNumberedProgram ? lowestNumberedProgram : nextInstalledProgram;
	}
	
	@Override
	public <T extends GuiEventListener & Widget & NarratableEntry> T addRenderableWidget(T button)
	{
		return super.addRenderableWidget(button);
	}
}