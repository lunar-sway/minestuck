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
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

import java.util.Optional;

public class SummonerScreen extends Screen
{
	private static final ResourceLocation guiBackground = new ResourceLocation("minestuck", "textures/gui/generic_medium.png");
	
	private static final int guiWidth = 150;
	private static final int guiHeight = 98;
	
	private final SummonerTileEntity te;
	private boolean isUntriggerable;
	private int summonRange;
	
	public Button incrementButton;
	public Button decrementButton;
	private Button unTriggerableButton;
	
	private TextFieldWidget entityTypeTextField;
	
	
	SummonerScreen(SummonerTileEntity te)
	{
		super(new StringTextComponent("Summoner"));
		
		this.te = te;
		this.summonRange = te.getSummonRange() > 1 ? te.getSummonRange() : 8; //if its defaulted on creation to 0, set it to the intended default of 8
		this.isUntriggerable = te.getBlockState().getValue(SummonerBlock.UNTRIGGERABLE);
	}
	
	@Override
	public void init()
	{
		int yOffset = (this.height / 2) - (guiHeight / 2);
		
		addButton(incrementButton = new ExtendedButton(this.width / 2 + 20, (height - guiHeight) / 2 + 12, 20, 20, new StringTextComponent("+"), button -> changeRange(1)));
		addButton(decrementButton = new ExtendedButton(this.width / 2 - 40, (height - guiHeight) / 2 + 12, 20, 20, new StringTextComponent("-"), button -> changeRange(-1)));
		
		this.entityTypeTextField = new TextFieldWidget(this.font, this.width / 2 - 60, yOffset + 40, 120, 18, new StringTextComponent("Current Entity Type"));	//TODO Use translation instead, and maybe look at other text fields for what the text should be
		this.entityTypeTextField.setValue(EntityType.getKey(te.getSummonedEntity()).toString());
		addButton(entityTypeTextField);
		
		if(isUntriggerable)
			addButton(unTriggerableButton = new ExtendedButton(this.width / 2 - 65, yOffset + 70, 85, 20, new StringTextComponent("UNTRIGGERABLE"), button -> cycleUntriggerable()));
		else
			addButton(unTriggerableButton = new ExtendedButton(this.width / 2 - 65, yOffset + 70, 85, 20, new StringTextComponent("TRIGGERABLE"), button -> cycleUntriggerable()));
		addButton(new ExtendedButton(this.width / 2 + 25, yOffset + 70, 40, 20, new StringTextComponent("DONE"), button -> finish()));
	}
	
	/**
	 * Attempts to increase or decrease the range at which the entity can be summoned if that value is between the min and max
	 */
	private void changeRange(int change)
	{
		summonRange = MathHelper.clamp(summonRange + change, 1, 64);
	}
	
	/**
	 * Cycles between the block being triggerable and untriggerable
	 */
	private void cycleUntriggerable()
	{
		isUntriggerable = !isUntriggerable;
		if(isUntriggerable)
			unTriggerableButton.setMessage(new StringTextComponent("UNTRIGGERABLE"));
		else
			unTriggerableButton.setMessage(new StringTextComponent("TRIGGERABLE"));
	}
	
	/**
	 * Returns the current entity type, with imps as the generic entity type
	 */
	private EntityType<?> getEntityType(String stringInput)
	{
		Optional<EntityType<?>> attemptedEntityType = EntityType.byString(stringInput);
		return attemptedEntityType.orElse(MSEntityTypes.IMP);
	}
	
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(matrixStack);
		RenderSystem.color4f(1F, 1F, 1F, 1F);
		this.minecraft.getTextureManager().bind(guiBackground);
		int yOffset = (this.height / 2) - (guiHeight / 2);
		
		this.blit(matrixStack, (this.width / 2) - (guiWidth / 2), yOffset, 0, 0, guiWidth, guiHeight);
		font.draw(matrixStack, Integer.toString(summonRange), (width / 2) - 5, (height - guiHeight) / 2 + 16, 16777215); //0x404040
		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}
	
	private void finish()
	{
		MSPacketHandler.sendToServer(new SummonerPacket(isUntriggerable, summonRange, te.getBlockPos(), getEntityType(entityTypeTextField.getValue())));
		onClose();
	}
}