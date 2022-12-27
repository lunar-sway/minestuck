package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mraof.minestuck.block.redstone.SummonerBlock;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.SummonerPacket;
import com.mraof.minestuck.blockentity.redstone.SummonerBlockEntity;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.client.gui.widget.ExtendedButton;

import java.util.Optional;

public class SummonerScreen extends Screen
{
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
		super(Component.literal("Summoner")); //TODO convert to translatable text string
		
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
		
		this.entityTypeTextField = new EditBox(this.font, this.width / 2 - 60, yOffset + 40, 120, 18, Component.literal("Current Entity Type"));    //TODO Use translation instead, and maybe look at other text fields for what the text should be
		this.entityTypeTextField.setValue(EntityType.getKey(be.getSummonedEntity()).toString());
		addRenderableWidget(entityTypeTextField);
		
		addRenderableWidget(unTriggerableButton = new ExtendedButton(this.width / 2 - 65, yOffset + 70, 85, 20, getTriggerableButtonMessage(), button -> cycleUntriggerable()));
		addRenderableWidget(new ExtendedButton(this.width / 2 + 25, yOffset + 70, 40, 20, Component.literal("DONE"), button -> finish()));
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
		return this.isUntriggerable ? Component.literal("UNTRIGGERABLE") : Component.literal("TRIGGERABLE");
	}
	
	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(poseStack);
		int yOffset = (height - GUI_HEIGHT) / 2;
		
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.setShaderTexture(0, GUI_BACKGROUND);
		this.blit(poseStack, (this.width / 2) - (GUI_WIDTH / 2), yOffset, 0, 0, GUI_WIDTH, GUI_HEIGHT);
		
		font.draw(poseStack, Integer.toString(summonRange), (width / 2) - 5, yOffset + 16, 0x404040);
		
		super.render(poseStack, mouseX, mouseY, partialTicks);
	}
	
	private void finish()
	{
		Optional<EntityType<?>> attemptedEntityType = EntityType.byString(entityTypeTextField.getValue());
		if(attemptedEntityType.isPresent())
		{
			MSPacketHandler.sendToServer(new SummonerPacket(isUntriggerable, summonRange, be.getBlockPos(), attemptedEntityType.get()));
			onClose();
		} else
		{
			entityTypeTextField.setTextColor(0XFF0000); //changes text to red to indicate that it is an invalid type
		}
	}
}