package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.block.redstone.SummonerBlock;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.SummonerPacket;
import com.mraof.minestuck.tileentity.redstone.SummonerTileEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

import java.util.Optional;

public class SummonerScreen extends Screen
{
	private static final ResourceLocation GUI_BACKGROUND = new ResourceLocation("minestuck", "textures/gui/generic_medium.png");
	
	private static final int GUI_WIDTH = 150;
	private static final int GUI_HEIGHT = 98;
	
	private final SummonerTileEntity te;
	private boolean isUntriggerable;
	private int summonRange;
	
	private Button incrementButton;
	private Button decrementButton;
	private Button largeIncrementButton;
	private Button largeDecrementButton;
	private Button unTriggerableButton;
	
	private TextFieldWidget entityTypeTextField;
	private boolean shouldFinish = true;
	
	SummonerScreen(SummonerTileEntity te)
	{
		super(new StringTextComponent("Summoner")); //TODO convert to translatable text string
		
		this.te = te;
		this.summonRange = te.getSummonRange();
		this.isUntriggerable = te.getBlockState().getValue(SummonerBlock.UNTRIGGERABLE);
	}
	
	@Override
	public void init()
	{
		int yOffset = (this.height / 2) - (GUI_HEIGHT / 2);
		
		addButton(incrementButton = new ExtendedButton(this.width / 2 + 20, (height - GUI_HEIGHT) / 2 + 12, 20, 20, new StringTextComponent("+"), button -> changeRange(1)));
		addButton(decrementButton = new ExtendedButton(this.width / 2 - 40, (height - GUI_HEIGHT) / 2 + 12, 20, 20, new StringTextComponent("-"), button -> changeRange(-1)));
		addButton(largeIncrementButton = new ExtendedButton(this.width / 2 + 45, (height - GUI_HEIGHT) / 2 + 12, 20, 20, new StringTextComponent("++"), button -> changeRange(10)));
		addButton(largeDecrementButton = new ExtendedButton(this.width / 2 - 65, (height - GUI_HEIGHT) / 2 + 12, 20, 20, new StringTextComponent("--"), button -> changeRange(-10)));
		
		this.entityTypeTextField = new TextFieldWidget(this.font, this.width / 2 - 60, yOffset + 40, 120, 18, new StringTextComponent("Current Entity Type"));    //TODO Use translation instead, and maybe look at other text fields for what the text should be
		this.entityTypeTextField.setValue(EntityType.getKey(te.getSummonedEntity()).toString());
		addButton(entityTypeTextField);
		
		addButton(unTriggerableButton = new ExtendedButton(this.width / 2 - 65, yOffset + 70, 85, 20, getTriggerableButtonMessage(), button -> cycleUntriggerable()));
		addButton(new ExtendedButton(this.width / 2 + 25, yOffset + 70, 40, 20, new StringTextComponent("DONE"), button -> finish()));
	}
	
	/**
	 * Attempts to increase or decrease the range at which the entity can be summoned if that value is between the min and max
	 */
	private void changeRange(int change)
	{
		summonRange = MathHelper.clamp(summonRange + change, 1, 64);
		incrementButton.active = summonRange < 64;
		decrementButton.active = summonRange > 1;
		largeIncrementButton.active = summonRange < 64;
		largeDecrementButton.active = summonRange > 1;
	}
	
	/**
	 * Cycles between the block being triggerable and untriggerable
	 */
	private void cycleUntriggerable()
	{
		isUntriggerable = !isUntriggerable;
		unTriggerableButton.setMessage(getTriggerableButtonMessage());
	}
	
	private ITextComponent getTriggerableButtonMessage()
	{
		return this.isUntriggerable ? new StringTextComponent("UNTRIGGERABLE") : new StringTextComponent("TRIGGERABLE");
	}
	
	/**
	 * Returns the current entity type, with a flag(shouldFinish) to prevent invalid strings from going through
	 */
	private EntityType<?> getEntityType(String stringInput)
	{
		Optional<EntityType<?>> attemptedEntityType = EntityType.byString(stringInput);
		if(!attemptedEntityType.isPresent())
			shouldFinish = false;
		return attemptedEntityType.orElse(MSEntityTypes.IMP); //despite having an orElse(), the packet changing the TE's value will not go through unless it is valid
	}
	
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(matrixStack);
		RenderSystem.color4f(1F, 1F, 1F, 1F);
		this.minecraft.getTextureManager().bind(GUI_BACKGROUND);
		int yOffset = (height - GUI_HEIGHT) / 2;
		
		this.blit(matrixStack, (this.width / 2) - (GUI_WIDTH / 2), yOffset, 0, 0, GUI_WIDTH, GUI_HEIGHT);
		font.draw(matrixStack, Integer.toString(summonRange), (width / 2) - 5, yOffset + 16, 0x404040);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}
	
	private void finish()
	{
		EntityType<?> entityType = getEntityType(entityTypeTextField.getValue());
		if(shouldFinish)
		{
			MSPacketHandler.sendToServer(new SummonerPacket(isUntriggerable, summonRange, te.getBlockPos(), entityType));
			onClose();
		} else
		{
			entityTypeTextField.setTextColor(0XFF0000); //changes text to red to indicate that it is an invalid type
			shouldFinish = true; //will be set to false again if it fails the check upon retrying
		}
	}
}