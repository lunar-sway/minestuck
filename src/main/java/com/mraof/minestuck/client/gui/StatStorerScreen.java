package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mraof.minestuck.blockentity.redstone.StatStorerBlockEntity;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.StatStorerPacket;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.widget.ExtendedButton;

public class StatStorerScreen extends Screen
{
	private static final ResourceLocation GUI_BACKGROUND = new ResourceLocation("minestuck", "textures/gui/generic_medium.png");
	
	private static final int GUI_WIDTH = 150;
	private static final int GUI_HEIGHT = 98;
	
	private static final String DIVIDE_VALUE_MESSAGE = "Divide power output by:"; //TODO make this translatable
	
	private final StatStorerBlockEntity be;
	private StatStorerBlockEntity.ActiveType activeType;
	
	private Button typeButton;
	
	private EditBox divideTextField;
	
	
	StatStorerScreen(StatStorerBlockEntity be)
	{
		super(Component.literal("Stat Storer"));
		
		this.be = be;
		this.activeType = be.getActiveType();
	}
	
	@Override
	public void init()
	{
		addRenderableWidget(typeButton = new ExtendedButton(this.width / 2 - 67, (height - GUI_HEIGHT) / 2 + 15, 135, 20, Component.literal(activeType.getNameNoSpaces()), button -> changeActiveType()));
		int yOffset = (this.height / 2) - (GUI_HEIGHT / 2);
		this.divideTextField = new EditBox(this.font, this.width / 2 - 18, yOffset + 50, 40, 18, Component.literal("Divide comparator output strength")); //TODO make these translatable
		this.divideTextField.setValue(String.valueOf(be.getDivideValueBy()));
		addRenderableWidget(divideTextField);
		setInitialFocus(divideTextField);
		
		addRenderableWidget(new ExtendedButton(this.width / 2 - 18, yOffset + 70, 40, 20, Component.literal("DONE"), button -> finish()));
	}
	
	/**
	 * Gets the ordinal of the current active type, and either cycles it back to the beginning at one if it is the last ordinal in the set or just sets it to the next ordinal in line
	 */
	private void changeActiveType()
	{
		activeType = StatStorerBlockEntity.ActiveType.fromInt(activeType.ordinal() < StatStorerBlockEntity.ActiveType.values().length - 1 ? activeType.ordinal() + 1 : 0);
		typeButton.setMessage(Component.literal(activeType.getNameNoSpaces()));
	}
	
	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(poseStack);
		int yOffset = (this.height / 2) - (GUI_HEIGHT / 2);
		
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.setShaderTexture(0, GUI_BACKGROUND);
		this.blit(poseStack, (this.width / 2) - (GUI_WIDTH / 2), yOffset, 0, 0, GUI_WIDTH, GUI_HEIGHT);
		
		font.draw(poseStack, DIVIDE_VALUE_MESSAGE, (width / 2) - font.width(DIVIDE_VALUE_MESSAGE) / 2, yOffset + 40, 0x404040);
		super.render(poseStack, mouseX, mouseY, partialTicks);
	}
	
	private void finish()
	{
		MSPacketHandler.sendToServer(new StatStorerPacket(activeType, be.getBlockPos(), textToInt()));
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
