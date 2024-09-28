package com.mraof.minestuck.client.gui;

import com.mraof.minestuck.block.redstone.SummonerBlock;
import com.mraof.minestuck.blockentity.redstone.SummonerBlockEntity;
import com.mraof.minestuck.network.block.SummonerSettingsPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
public class SummonerScreen extends Screen
{
	public static final String TITLE = "minestuck.summoner";
	public static final String CURRENT_ENTITY_TYPE_MESSAGE = "minestuck.current_entity_type";
	public static final String DONE_MESSAGE = "minestuck.summoner.done";
	public static final String UNTRIGGERABLE_MESSAGE = "minestuck.summoner.untriggerable";
	public static final String TRIGGERABLE_MESSAGE = "minestuck.summoner.triggerable";
	private static final ResourceLocation GUI_BACKGROUND = new ResourceLocation("minestuck", "textures/gui/generic_medium.png");
	
	private static final int GUI_WIDTH = 150;
	private static final int GUI_HEIGHT = 98;
	
	private final SummonerBlockEntity be;
	private boolean isUntriggerable;
	private int summonRange;
	
	private Button incrementButton;
	private Button decrementButton;
	private Button largeIncrementButton;
	private Button largeDecrementButton;
	private Button unTriggerableButton;
	
	private EditBox entityTypeTextField;
	
	SummonerScreen(SummonerBlockEntity be)
	{
		super(Component.translatable(TITLE));
		
		this.be = be;
		this.summonRange = be.getSummonRange();
		this.isUntriggerable = be.getBlockState().getValue(SummonerBlock.UNTRIGGERABLE);
	}
	
	@Override
	public void init()
	{
		int yOffset = (this.height / 2) - (GUI_HEIGHT / 2);
		
		addRenderableWidget(incrementButton = new ExtendedButton(this.width / 2 + 20, (height - GUI_HEIGHT) / 2 + 12, 20, 20, Component.literal("+"), button -> changeRange(1)));
		addRenderableWidget(decrementButton = new ExtendedButton(this.width / 2 - 40, (height - GUI_HEIGHT) / 2 + 12, 20, 20, Component.literal("-"), button -> changeRange(-1)));
		addRenderableWidget(largeIncrementButton = new ExtendedButton(this.width / 2 + 45, (height - GUI_HEIGHT) / 2 + 12, 20, 20, Component.literal("++"), button -> changeRange(10)));
		addRenderableWidget(largeDecrementButton = new ExtendedButton(this.width / 2 - 65, (height - GUI_HEIGHT) / 2 + 12, 20, 20, Component.literal("--"), button -> changeRange(-10)));
		
		this.entityTypeTextField = new EditBox(this.font, this.width / 2 - 60, yOffset + 40, 120, 18, Component.translatable(CURRENT_ENTITY_TYPE_MESSAGE));    //TODO Maybe look at other text fields for what the text should be
		this.entityTypeTextField.setValue(EntityType.getKey(be.getSummonedEntity()).toString());
		addRenderableWidget(entityTypeTextField);
		
		addRenderableWidget(unTriggerableButton = new ExtendedButton(this.width / 2 - 65, yOffset + 70, 85, 20, getTriggerableButtonMessage(), button -> cycleUntriggerable()));
		addRenderableWidget(new ExtendedButton(this.width / 2 + 25, yOffset + 70, 40, 20, Component.translatable(DONE_MESSAGE), button -> finish()));
	}
	
	/**
	 * Attempts to increase or decrease the range at which the entity can be summoned if that value is between the min and max
	 */
	private void changeRange(int change)
	{
		summonRange = Mth.clamp(summonRange + change, 1, 64);
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
	
	private Component getTriggerableButtonMessage()
	{
		return this.isUntriggerable ? Component.translatable(UNTRIGGERABLE_MESSAGE) : Component.translatable(TRIGGERABLE_MESSAGE);
	}
	
	@Override
	public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		super.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
		
		guiGraphics.blit(GUI_BACKGROUND, (this.width - GUI_WIDTH / 2), (this.height - GUI_HEIGHT) / 2, 0, 0, GUI_WIDTH, GUI_HEIGHT);
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		
		guiGraphics.drawString(font, Integer.toString(summonRange), (width / 2) - 5, (this.height - GUI_HEIGHT) / 2 + 16, 0x404040, false);
	}
	
	private void finish()
	{
		Optional<EntityType<?>> attemptedEntityType = EntityType.byString(entityTypeTextField.getValue());
		if(attemptedEntityType.isPresent())
		{
			PacketDistributor.SERVER.noArg().send(new SummonerSettingsPacket(isUntriggerable, summonRange, attemptedEntityType.get(), be.getBlockPos()));
			onClose();
		} else
		{
			entityTypeTextField.setTextColor(0XFF0000); //changes text to red to indicate that it is an invalid type
		}
	}
}
