package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.RemoteObserverPacket;
import com.mraof.minestuck.tileentity.redstone.RemoteObserverTileEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

import java.util.Optional;

public class RemoteObserverScreen extends Screen
{
	private static final ResourceLocation guiBackground = new ResourceLocation("minestuck", "textures/gui/generic_medium.png");
	
	private static final int guiWidth = 150;
	private static final int guiHeight = 98;
	
	private final RemoteObserverTileEntity te;
	private RemoteObserverTileEntity.ActiveType activeType;
	private int observingRange;
	
	public Button typeButton;
	public Button incrementButton;
	public Button decrementButton;
	
	private TextFieldWidget entityTypeTextField;
	
	
	RemoteObserverScreen(RemoteObserverTileEntity te)
	{
		super(new StringTextComponent("Stat Storer"));
		
		this.te = te;
		this.activeType = te.getActiveType();
		this.observingRange = te.getObservingRange() > 0 ? te.getObservingRange() : 16; //if its defaulted on creation to 0, set it to the intended default of 16
	}
	
	@Override
	public void init()
	{
		int yOffset = (this.height / 2) - (guiHeight / 2);
		
		addButton(typeButton = new ExtendedButton(this.width / 2 - 67, (height - guiHeight) / 2 + 5, 135, 20, new StringTextComponent(activeType.getNameNoSpaces()), button -> changeActiveType()));
		
		this.entityTypeTextField = new TextFieldWidget(this.font, this.width / 2 - 53, yOffset + 29, 105, 18, new StringTextComponent("Current Entity Type"));	//TODO Use translation instead, and maybe look at other text fields for what the text should be
		this.entityTypeTextField.setValue(EntityType.getKey(te.getCurrentEntityType()).toString()); //TODO somewhere along the line, if the active type is not current entity present and the gui is exited, returning to current entity present active type has pig as the entity type
		addButton(entityTypeTextField);
		entityTypeTextField.setVisible(activeType == RemoteObserverTileEntity.ActiveType.CURRENT_ENTITY_PRESENT);
		
		addButton(incrementButton = new ExtendedButton(this.width / 2 + 20, (height - guiHeight) / 2 + 51, 20, 20, new StringTextComponent("+"), button -> changeRange(1)));
		addButton(decrementButton = new ExtendedButton(this.width / 2 - 40, (height - guiHeight) / 2 + 51, 20, 20, new StringTextComponent("-"), button -> changeRange(-1)));
		
		addButton(new ExtendedButton(this.width / 2 - 20, yOffset + 73, 40, 20, new StringTextComponent("DONE"), button -> finish()));
	}
	
	/**
	 * Gets the ordinal of the current active type, and either cycles it back to the beginning at one if it is the last ordinal in the set or just sets it to the next ordinal in line, also refreshes the display for Current Entity Present
	 */
	private void changeActiveType()
	{
		activeType = RemoteObserverTileEntity.ActiveType.fromInt(activeType.ordinal() < RemoteObserverTileEntity.ActiveType.values().length - 1 ? activeType.ordinal() + 1 : 0);
		typeButton.setMessage(new StringTextComponent(activeType.getNameNoSpaces()));
		
		entityTypeTextField.setVisible(activeType == RemoteObserverTileEntity.ActiveType.CURRENT_ENTITY_PRESENT);
	}
	
	/**
	 * Attempts to increase or decrease the range at which the observer attempts to detect entities
	 */
	private void changeRange(int change)
	{
		observingRange = MathHelper.clamp(observingRange + change, 1, 64);
	}
	
	/**
	 * Returns the current entity type if the text field string matches it and the active type is correct, returns players as a generic entity type if not valid but active type is correct, or returns null if the correct active type isnt selected
	 */
	private EntityType<?> getEntityType(String stringInput)
	{
		if(activeType == RemoteObserverTileEntity.ActiveType.CURRENT_ENTITY_PRESENT)
		{
			Optional<EntityType<?>> attemptedEntityType = EntityType.byString(stringInput);
			return attemptedEntityType.orElse(EntityType.PLAYER);
		} else
			return null;
	}
	
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(matrixStack);
		RenderSystem.color4f(1F, 1F, 1F, 1F);
		this.minecraft.getTextureManager().bind(guiBackground);
		int yOffset = (this.height / 2) - (guiHeight / 2);
		
		this.blit(matrixStack, (this.width / 2) - (guiWidth / 2), yOffset, 0, 0, guiWidth, guiHeight);
		font.draw(matrixStack, Integer.toString(observingRange), (width / 2) - 5, (height - guiHeight) / 2 + 55, 0x404040);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}
	
	private void finish()
	{
		MSPacketHandler.sendToServer(new RemoteObserverPacket(activeType, observingRange, te.getBlockPos(), getEntityType(entityTypeTextField.getValue())));
		onClose();
	}
}