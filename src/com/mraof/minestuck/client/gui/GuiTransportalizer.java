package com.mraof.minestuck.client.gui;

import com.mraof.minestuck.network.MinestuckPacketHandler;
import com.mraof.minestuck.network.TransportalizerPacket;
import com.mraof.minestuck.tileentity.TileEntityTransportalizer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiTransportalizer extends GuiScreen implements GuiButtonImpl.ButtonClickhandler
{
	private static final ResourceLocation guiBackground = new ResourceLocation("minestuck", "textures/gui/transportalizer.png");

	private static final int guiWidth = 126;
	private static final int guiHeight = 98;

	private Minecraft mc;
	TileEntityTransportalizer te;
	private GuiTextField destinationTextField;
	private GuiButton doneButton;

	public GuiTransportalizer(Minecraft mc, TileEntityTransportalizer te)
	{
		super();

		this.mc = mc;
		this.fontRenderer = mc.fontRenderer;
		this.te = te;
	}

	@Override
	public void initGui()
	{
		int yOffset = (this.height / 2) - (guiHeight / 2);
		this.destinationTextField = new GuiTextField(0, this.fontRenderer, this.width / 2 - 20, yOffset + 25, 40, 20);
		this.destinationTextField.setMaxStringLength(4);
		this.destinationTextField.setFocused(true);
		this.destinationTextField.setText(this.te.getDestId());
		children.add(destinationTextField);
		
		this.doneButton = addButton(new GuiButtonImpl(this, 0, this.width / 2 - 20, yOffset + 50, 40, 20, I18n.format("gui.done")));
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		this.drawDefaultBackground();
		GlStateManager.color4f(1F, 1F, 1F, 1F);
		this.mc.getTextureManager().bindTexture(guiBackground);
		int yOffset = (this.height / 2) - (guiHeight / 2);
		this.drawTexturedModalRect((this.width / 2) - (guiWidth / 2), yOffset, 0, 0, guiWidth, guiHeight);
		fontRenderer.drawString(te.getId(), (this.width / 2) - fontRenderer.getStringWidth(te.getId()) / 2, yOffset + 10, te.getActive() ? 0x404040 : 0xFF0000);
		this.destinationTextField.drawTextField(mouseX, mouseY, partialTicks);
		super.render(mouseX, mouseY, partialTicks);
	}

	@Override
	public void actionPerformed(GuiButtonImpl button)
	{
		if(button.id == 0 && this.destinationTextField.getText().length() == 4)
		{
			//Debug.print("Sending transportalizer packet with destination of " + this.destinationTextField.getText());
			TransportalizerPacket packet = new TransportalizerPacket(te.getPos(), destinationTextField.getText().toUpperCase());
			MinestuckPacketHandler.sendToServer(packet);
			this.mc.displayGuiScreen(null);
		}
	}

}

