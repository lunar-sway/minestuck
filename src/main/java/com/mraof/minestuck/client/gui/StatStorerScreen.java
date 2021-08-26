package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.StatStorerPacket;
import com.mraof.minestuck.tileentity.redstone.StatStorerTileEntity;
import com.mraof.minestuck.util.Debug;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

public class StatStorerScreen extends Screen
{
	private static final ResourceLocation guiBackground = new ResourceLocation("minestuck", "textures/gui/transportalizer.png");
	
	private static final int guiWidth = 150;
	private static final int guiHeight = 98;
	
	private static final String divideValueMessage = "Divide power output by:";
	
	StatStorerTileEntity te;
	private static StatStorerTileEntity.ActiveType activeType;
	private static int divideValueBy;
	
	public Button typeButton;
	
	//public Button divideButton;
	private TextFieldWidget divideTextField;
	
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
		
		addButton(typeButton = new ExtendedButton(this.width / 2 - 68, (height - guiHeight) / 2 + 12, 110, 20, activeType.getNameNoSpaces(), button -> changeActiveType()));
		int yOffset = (this.height / 2) - (guiHeight / 2);
		this.divideTextField = new TextFieldWidget(this.font, this.width / 2 - 30, yOffset + 50, 40, 18, "Divide comparator output strength");	//TODO Use translation instead, and maybe look at other text fields for what the text should be
		//this.divideTextField.setMaxStringLength(4);
		this.divideTextField.setText(String.valueOf(te.getDivideValueBy()));
		this.divideTextField.setFocused2(true);
		//destinationTextField.setResponder(s -> doneButton.active = s.length() == 4);
		addButton(divideTextField);
		setFocusedDefault(divideTextField);
		
		addButton(doneButton = new ExtendedButton(this.width / 2 - 30, yOffset + 70, 40, 20, I18n.format("gui.done"), button -> finish()));
		doneButton.active = true;
		
		//updateGui();
	}
	
	/**
	 * Gets the ordinal of the current active type, and either cycles it back to the beginning at one if it is the last ordinal in the set or just sets it to the next ordinal in line
	 */
	private void changeActiveType()
	{
		Debug.debugf("activeType = %s, current active ordinal = %s, ordinal length = %s", activeType, te.getActiveType().ordinal(), StatStorerTileEntity.ActiveType.values().length);
		activeType = StatStorerTileEntity.ActiveType.fromInt(te.getActiveType().ordinal() < StatStorerTileEntity.ActiveType.values().length - 1 ? te.getActiveType().ordinal() + 1 : 0);
		typeButton.setMessage(I18n.format(activeType.getNameNoSpaces()));
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
		font.drawString(divideValueMessage, (width / 2) - font.getStringWidth(divideValueMessage) / 2, yOffset + 40, 0x404040);
		super.render(mouseX, mouseY, partialTicks);
	}
	
	private void finish()
	{
		if(activeType != null)
		{
			StatStorerPacket packet = new StatStorerPacket(activeType, te.getPos(), textToInt());
			MSPacketHandler.sendToServer(packet);
			this.minecraft.displayGuiScreen(null);
		}
	}
	
	private int textToInt()
	{
		try
		{
			divideValueBy = Integer.parseInt(divideTextField.getText());
		} catch(NumberFormatException ignored)
		{
		}
		
		return divideValueBy;
	}
}

