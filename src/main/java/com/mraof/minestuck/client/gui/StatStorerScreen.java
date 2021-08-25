package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.computer.ComputerProgram;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.StatStorerPacket;
import com.mraof.minestuck.network.WirelessRedstoneTransmitterPacket;
import com.mraof.minestuck.tileentity.StatStorerTileEntity;
import com.mraof.minestuck.tileentity.WirelessRedstoneTransmitterTileEntity;
import com.mraof.minestuck.util.Debug;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

public class StatStorerScreen extends Screen
{
	private static final ResourceLocation guiBackground = new ResourceLocation("minestuck", "textures/gui/transportalizer.png");
	
	private static final int guiWidth = 150;
	private static final int guiHeight = 98;
	
	StatStorerTileEntity te;
	private static StatStorerTileEntity.ActiveType activeType;
	
	public Button typeButton;
	private Button doneButton;
	
	
	StatStorerScreen(StatStorerTileEntity te)
	{
		super(new StringTextComponent("Stat Storer"));
		
		this.te = te;
	}
	
	@Override
	public void init()
	{
		activeType = te.getActiveType();
		int yOffset = (this.height / 2) - (guiHeight / 2);
		
		addButton(typeButton = new ExtendedButton(this.width / 2 - 60, (height - guiHeight) / 2 + 10, 90, 20, /*"Active Stat: " + */activeType.name()/* + " (press again to change)"*/, button -> changeActiveType()));
		
		addButton(doneButton = new ExtendedButton(this.width / 2 - 40, yOffset + 50, 40, 20, I18n.format("gui.done"), button -> finish()));
		doneButton.active = true;
		
		updateGui();
	}
	
	public void updateGui()
	{
		typeButton.setMessage(I18n.format(activeType.name()));
	}
	
	/**
	 * Gets the ordinal of the current active type, and either cycles it back to the beginning at one if it is the last ordinal in the set or just sets it to the next ordinal in line
	 */
	private void changeActiveType()
	{
		Debug.debugf("activeType = %s, current active ordinal = %s, ordinal length = %s", activeType, te.getActiveType().ordinal(), StatStorerTileEntity.ActiveType.values().length);
		activeType = StatStorerTileEntity.ActiveType.fromInt(te.getActiveType().ordinal() < StatStorerTileEntity.ActiveType.values().length - 1 ? te.getActiveType().ordinal() + 1 : 0);
		Debug.debugf("new active type = %s", activeType);
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground();
		RenderSystem.color4f(1F, 1F, 1F, 1F);
		this.minecraft.getTextureManager().bindTexture(guiBackground);
		int yOffset = (this.height / 2) - (guiHeight / 2);
		this.blit((this.width / 2) - (guiWidth / 2), yOffset, 0, 0, guiWidth, guiHeight);
		super.render(mouseX, mouseY, partialTicks);
	}
	
	private void finish()
	{
		if(activeType != null)
		{
			StatStorerPacket packet = new StatStorerPacket(activeType, te.getPos());
			MSPacketHandler.sendToServer(packet);
			this.minecraft.displayGuiScreen(null);
		}
	}
}

