package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.StatStorerPacket;
import com.mraof.minestuck.tileentity.redstone.StatStorerTileEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

public class StatStorerScreen extends Screen
{
	private static final ResourceLocation guiBackground = new ResourceLocation("minestuck", "textures/gui/generic_medium.png");
	
	private static final int guiWidth = 150;
	private static final int guiHeight = 98;
	
	private static final String divideValueMessage = "Divide power output by:";
	
	private final StatStorerTileEntity te;
	private StatStorerTileEntity.ActiveType activeType;
	private int divideValueBy;
	
	public Button typeButton;
	
	private TextFieldWidget divideTextField;
	
	
	StatStorerScreen(StatStorerTileEntity te)
	{
		super(new StringTextComponent("Stat Storer"));
		
		this.te = te;
		this.activeType = te.getActiveType();
	}
	
	@Override
	public void init()
	{
		addButton(typeButton = new ExtendedButton(this.width / 2 - 67, (height - guiHeight) / 2 + 15, 135, 20, new StringTextComponent(activeType.getNameNoSpaces()), button -> changeActiveType()));
		int yOffset = (this.height / 2) - (guiHeight / 2);
		this.divideTextField = new TextFieldWidget(this.font, this.width / 2 - 18, yOffset + 50, 40, 18, new StringTextComponent("Divide comparator output strength")); //TODO make these translatable
		this.divideTextField.setValue(String.valueOf(te.getDivideValueBy()));
		addButton(divideTextField);
		setInitialFocus(divideTextField);
		
		addButton(new ExtendedButton(this.width / 2 - 18, yOffset + 70, 40, 20, new StringTextComponent("DONE"), button -> finish()));
	}
	
	/**
	 * Gets the ordinal of the current active type, and either cycles it back to the beginning at one if it is the last ordinal in the set or just sets it to the next ordinal in line
	 */
	private void changeActiveType()
	{
		activeType = StatStorerTileEntity.ActiveType.fromInt(activeType.ordinal() < StatStorerTileEntity.ActiveType.values().length - 1 ? activeType.ordinal() + 1 : 0);
		typeButton.setMessage(new StringTextComponent(activeType.getNameNoSpaces()));
	}
	
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(matrixStack);
		RenderSystem.color4f(1F, 1F, 1F, 1F);
		this.minecraft.getTextureManager().bind(guiBackground);
		int yOffset = (this.height / 2) - (guiHeight / 2);
		
		this.blit(matrixStack, (this.width / 2) - (guiWidth / 2), yOffset, 0, 0, guiWidth, guiHeight);
		font.draw(matrixStack, divideValueMessage, (width / 2) - font.width(divideValueMessage) / 2, yOffset + 40, 0x404040);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}
	
	private void finish()
	{
		MSPacketHandler.sendToServer(new StatStorerPacket(activeType, te.getBlockPos(), textToInt()));
		onClose();
	}
	
	private int textToInt()
	{
		try
		{
			return Integer.parseInt(divideTextField.getValue());
		} catch(NumberFormatException ignored)
		{
			return 0;
		}
	}
}
