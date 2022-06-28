package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.StructureCorePacket;
import com.mraof.minestuck.tileentity.redstone.StructureCoreTileEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

public class StructureCoreScreen extends Screen
{
	private static final ResourceLocation GUI_BACKGROUND = new ResourceLocation("minestuck", "textures/gui/generic_medium.png");
	
	private static final int GUI_WIDTH = 150;
	private static final int GUI_HEIGHT = 98;
	
	private final StructureCoreTileEntity te;
	private StructureCoreTileEntity.ActionType actionType;
	private int shutdownRange;
	
	private Button incrementButton;
	private Button decrementButton;
	private Button largeIncrementButton;
	private Button largeDecrementButton;
	private Button typeButton;
	
	
	StructureCoreScreen(StructureCoreTileEntity te)
	{
		super(new StringTextComponent("Structure Core"));
		
		this.te = te;
		this.actionType = te.getActionType();
		this.shutdownRange = te.getShutdownRange();
	}
	
	@Override
	public void init()
	{
		int yOffset = (this.height / 2) - (GUI_HEIGHT / 2);
		
		addButton(incrementButton = new ExtendedButton(this.width / 2 + 20, (height - GUI_HEIGHT) / 2 + 12, 20, 20, new StringTextComponent("+"), button -> changeRange(1)));
		addButton(decrementButton = new ExtendedButton(this.width / 2 - 40, (height - GUI_HEIGHT) / 2 + 12, 20, 20, new StringTextComponent("-"), button -> changeRange(-1)));
		addButton(largeIncrementButton = new ExtendedButton(this.width / 2 + 45, (height - GUI_HEIGHT) / 2 + 12, 20, 20, new StringTextComponent("++"), button -> changeRange(10)));
		addButton(largeDecrementButton = new ExtendedButton(this.width / 2 - 65, (height - GUI_HEIGHT) / 2 + 12, 20, 20, new StringTextComponent("--"), button -> changeRange(-10)));
		
		addButton(typeButton = new ExtendedButton(this.width / 2 - 67, yOffset + 40, 135, 20, new StringTextComponent(actionType.getNameNoSpaces()), button -> changeActionType()));
		
		addButton(new ExtendedButton(this.width / 2 - 18, yOffset + 70, 40, 20, new StringTextComponent("DONE"), button -> finish()));
	}
	
	/**
	 * Gets the ordinal of the current action type, and either cycles it back to the beginning at one if it is the last ordinal in the set or just sets it to the next ordinal in line
	 */
	private void changeActionType()
	{
		actionType = StructureCoreTileEntity.ActionType.fromInt(actionType.ordinal() < StructureCoreTileEntity.ActionType.values().length - 1 ? actionType.ordinal() + 1 : 0);
		typeButton.setMessage(new StringTextComponent(actionType.getNameNoSpaces()));
	}
	
	/**
	 * Attempts to increase or decrease the range at which the block sends out a shutdown "pulse" if the block is in receiving mode
	 */
	private void changeRange(int change)
	{
		shutdownRange = MathHelper.clamp(shutdownRange + change, 1, 64);
		incrementButton.active = shutdownRange < 64;
		decrementButton.active = shutdownRange > 1;
		largeIncrementButton.active = shutdownRange < 64;
		largeDecrementButton.active = shutdownRange > 1;
	}
	
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(matrixStack);
		RenderSystem.color4f(1F, 1F, 1F, 1F);
		this.minecraft.getTextureManager().bind(GUI_BACKGROUND);
		int yOffset = (this.height / 2) - (GUI_HEIGHT / 2);
		
		this.blit(matrixStack, (this.width / 2) - (GUI_WIDTH / 2), yOffset, 0, 0, GUI_WIDTH, GUI_HEIGHT);
		font.draw(matrixStack, Integer.toString(shutdownRange), (width / 2) - 5, (height - GUI_HEIGHT) / 2 + 16, 0x404040);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}
	
	private void finish()
	{
		MSPacketHandler.sendToServer(new StructureCorePacket(actionType, shutdownRange, te.getBlockPos()));
		onClose();
	}
}