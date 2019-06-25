package com.mraof.minestuck.client.gui;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.config.GuiButtonExt;

@OnlyIn(Dist.CLIENT)
public class GuiButtonImpl extends GuiButtonExt
{
	protected final ButtonClickhandler handler;
	
	public GuiButtonImpl(ButtonClickhandler handler, int id, int xPos, int yPos, String displayString)
	{
		super(id, xPos, yPos, displayString);
		this.handler = handler;
	}
	
	public GuiButtonImpl(ButtonClickhandler handler, int id, int xPos, int yPos, int width, int height, String displayString)
	{
		super(id, xPos, yPos, width, height, displayString);
		this.handler = handler;
	}
	
	@Override
	public void onClick(double mouseX, double mouseY)
	{
		super.onClick(mouseX, mouseY);
		handler.actionPerformed(this);
	}
	
	public interface ButtonClickhandler
	{
		void actionPerformed(GuiButtonImpl button);
	}
}