package com.mraof.minestuck.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.TransportalizerPacket;
import com.mraof.minestuck.tileentity.TileEntityTransportalizer;

public class GuiTransportalizer extends GuiScreen
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
		this.fontRendererObj = mc.fontRenderer;
		this.te = te;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui()
	{
		int yOffset = (this.height / 2) - (guiHeight / 2);
		this.destinationTextField = new GuiTextField(this.fontRendererObj, this.width / 2 - 20, yOffset + 25, 40, 20);
		this.destinationTextField.setMaxStringLength(4);
		this.destinationTextField.setFocused(true);
		this.destinationTextField.setText(this.te.getDestId());
		
		this.doneButton = new GuiButton(0, this.width / 2 - 20, yOffset + 50, 40, 20, I18n.format("gui.done", new Object[0]));
		this.buttonList.add(doneButton);
	}
	@Override
	public void drawScreen(int x, int y, float f1)
	{
		this.drawDefaultBackground();
		GL11.glColor4f(1F, 1F, 1F, 1F);
		this.mc.getTextureManager().bindTexture(guiBackground);
		int yOffset = (this.height / 2) - (guiHeight / 2);
		this.drawTexturedModalRect((this.width / 2) - (guiWidth / 2), yOffset, 0, 0, guiWidth, guiHeight);
		fontRendererObj.drawString(te.getId(), (this.width / 2) - fontRendererObj.getStringWidth(te.getId()) / 2, yOffset + 10, 0x404040);
		this.destinationTextField.drawTextBox();
		super.drawScreen(x, y, f1);
	}
	/**
	 * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
	 */
	@Override
	protected void keyTyped(char character, int key)
	{
		super.keyTyped(character, key);
		this.destinationTextField.textboxKeyTyped(character, key);
		//this.doneBtn.enabled = this.commandTextField.getText().trim().length() > 0;

		//this.actionPerformed(this.doneBtn);
	}

	/**
	 * Called when the mouse is clicked.
	 */
	@Override
	protected void mouseClicked(int x, int y, int button)
	{
		super.mouseClicked(x, y, button);
		this.destinationTextField.mouseClicked(x, y, button);
	}

	@Override
	protected void actionPerformed(GuiButton button)
	{
		if(button.id == 0 && this.destinationTextField.getText().length() == 4)
		{
			//Debug.print("Sending transportalizer packet with destination of " + this.destinationTextField.getText());
			MinestuckPacket packet = new TransportalizerPacket();
			packet.generatePacket(te.xCoord, te.yCoord, te.zCoord, this.destinationTextField.getText().toUpperCase());
			MinestuckChannelHandler.sendToServer(packet);
			this.mc.displayGuiScreen((GuiScreen)null);
		}
	}

}

