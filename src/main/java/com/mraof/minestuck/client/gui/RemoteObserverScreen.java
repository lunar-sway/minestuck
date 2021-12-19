package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.RemoteObserverPacket;
import com.mraof.minestuck.tileentity.redstone.RemoteObserverTileEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

public class RemoteObserverScreen extends Screen
{
	private static final ResourceLocation guiBackground = new ResourceLocation("minestuck", "textures/gui/generic_medium.png");
	
	private static final int guiWidth = 150;
	private static final int guiHeight = 98;
	
	RemoteObserverTileEntity te;
	private RemoteObserverTileEntity.ActiveType activeType;
	
	public Button typeButton;
	
	
	RemoteObserverScreen(RemoteObserverTileEntity te)
	{
		super(new StringTextComponent("Stat Storer"));
		
		this.te = te;
		this.activeType = te.getActiveType();
	}
	
	@Override
	public void init()
	{
		addButton(typeButton = new ExtendedButton(this.width / 2 - 65, (height - guiHeight) / 2 + 15, 130, 20, activeType.getNameNoSpaces(), button -> changeActiveType()));
		int yOffset = (this.height / 2) - (guiHeight / 2);
		
		addButton(new ExtendedButton(this.width / 2 - 18, yOffset + 60, 40, 20, I18n.format("gui.done"), button -> finish()));
	}
	
	/**
	 * Gets the ordinal of the current active type, and either cycles it back to the beginning at one if it is the last ordinal in the set or just sets it to the next ordinal in line
	 */
	private void changeActiveType()
	{
		activeType = RemoteObserverTileEntity.ActiveType.fromInt(activeType.ordinal() < RemoteObserverTileEntity.ActiveType.values().length - 1 ? activeType.ordinal() + 1 : 0);
		typeButton.setMessage(activeType.getNameNoSpaces());
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
		RemoteObserverPacket packet = new RemoteObserverPacket(activeType, te.getPos());
		MSPacketHandler.sendToServer(packet);
		onClose();
	}
}