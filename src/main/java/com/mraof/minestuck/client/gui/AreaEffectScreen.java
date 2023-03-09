package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mraof.minestuck.block.redstone.AreaEffectBlock;
import com.mraof.minestuck.network.AreaEffectPacket;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.blockentity.redstone.AreaEffectBlockEntity;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.client.gui.widget.ExtendedButton;
import net.minecraftforge.registries.ForgeRegistries;

public class AreaEffectScreen extends Screen
{
	private static final ResourceLocation GUI_BACKGROUND = new ResourceLocation("minestuck", "textures/gui/generic_large.png");
	
	private static final int GUI_WIDTH = 150;
	private static final int GUI_HEIGHT = 132;
	
	private static final String MIN_POS_MESSAGE = "Min Pos Facing Offset"; //TODO make translatable (lang file + translation key)
	private static final String MAX_POS_MESSAGE = "Max Pos Facing Offset";
	
	private final AreaEffectBlockEntity be;
	private EditBox minPosDestinationTextFieldX;
	private EditBox minPosDestinationTextFieldY;
	private EditBox minPosDestinationTextFieldZ;
	private EditBox maxPosDestinationTextFieldX;
	private EditBox maxPosDestinationTextFieldY;
	private EditBox maxPosDestinationTextFieldZ;
	private EditBox effectTextField;
	private EditBox effectAmplifierTextField;
	private boolean isAllMobs;
	private boolean validInput = true;
	
	private Button allMobsButton;
	
	
	AreaEffectScreen(AreaEffectBlockEntity be)
	{
		super(Component.literal("Area Effect Block"));
		
		this.be = be;
		this.isAllMobs = be.getBlockState().getValue(AreaEffectBlock.ALL_MOBS);
	}
	
	@Override
	public void init()
	{
		int yOffset = (this.height / 2) - (GUI_HEIGHT / 2);
		
		this.minPosDestinationTextFieldX = new EditBox(this.font, this.width / 2 - 60, yOffset + 15, 40, 20, Component.literal("X value of min effect pos")); //TODO make these translatable
		this.minPosDestinationTextFieldX.setValue(String.valueOf(be.getMinAreaOffset().getX()));
		addRenderableWidget(minPosDestinationTextFieldX);
		
		this.minPosDestinationTextFieldY = new EditBox(this.font, this.width / 2 - 20, yOffset + 15, 40, 20, Component.literal("Y value of min effect pos"));
		this.minPosDestinationTextFieldY.setValue(String.valueOf(be.getMinAreaOffset().getY()));
		addRenderableWidget(minPosDestinationTextFieldY);
		
		this.minPosDestinationTextFieldZ = new EditBox(this.font, this.width / 2 + 20, yOffset + 15, 40, 20, Component.literal("Z value of min effect pos"));
		this.minPosDestinationTextFieldZ.setValue(String.valueOf(be.getMinAreaOffset().getZ()));
		addRenderableWidget(minPosDestinationTextFieldZ);
		
		this.maxPosDestinationTextFieldX = new EditBox(this.font, this.width / 2 - 60, yOffset + 50, 40, 20, Component.literal("X value of max effect pos"));
		this.maxPosDestinationTextFieldX.setValue(String.valueOf(be.getMaxAreaOffset().getX()));
		addRenderableWidget(maxPosDestinationTextFieldX);
		
		this.maxPosDestinationTextFieldY = new EditBox(this.font, this.width / 2 - 20, yOffset + 50, 40, 20, Component.literal("Y value of max effect pos"));
		this.maxPosDestinationTextFieldY.setValue(String.valueOf(be.getMaxAreaOffset().getY()));
		addRenderableWidget(maxPosDestinationTextFieldY);
		
		this.maxPosDestinationTextFieldZ = new EditBox(this.font, this.width / 2 + 20, yOffset + 50, 40, 20, Component.literal("Z value of max effect pos"));
		this.maxPosDestinationTextFieldZ.setValue(String.valueOf(be.getMaxAreaOffset().getZ()));
		addRenderableWidget(maxPosDestinationTextFieldZ);
		
		
		this.effectTextField = new EditBox(this.font, this.width / 2 - 65, yOffset + 79, 105, 18, Component.literal("Current Effect"));
		this.effectTextField.setValue(String.valueOf(ForgeRegistries.MOB_EFFECTS.getKey(be.getEffect())));
		addRenderableWidget(effectTextField);
		
		this.effectAmplifierTextField = new EditBox(this.font, this.width / 2 + 45, yOffset + 79, 20, 18, Component.literal("Current Effect Amplifier"));
		this.effectAmplifierTextField.setValue(String.valueOf(be.getEffectAmplifier()));
		addRenderableWidget(effectAmplifierTextField);
		
		addRenderableWidget(allMobsButton = new ExtendedButton(this.width / 2 - 65, yOffset + 105, 85, 20, getAllMobsButtonMessage(), button -> cycleIsAllMobs()));
		
		addRenderableWidget(new ExtendedButton(this.width / 2 + 25, yOffset + 105, 40, 20, Component.literal("DONE"), button -> finish()));
	}
	
	private Component getAllMobsButtonMessage()
	{
		return this.isAllMobs ? Component.literal("ALL MOBS") : Component.literal("JUST PLAYERS");
	}
	
	/**
	 * Cycles between the block affecting all mobs or just players
	 */
	private void cycleIsAllMobs()
	{
		isAllMobs = !isAllMobs;
		allMobsButton.setMessage(getAllMobsButtonMessage());
	}
	
	/**
	 * Returns the current effect type
	 */
	private MobEffect getEffect(String stringInput)
	{
		return ForgeRegistries.MOB_EFFECTS.getValue(ResourceLocation.tryParse(stringInput));
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
		
		font.draw(poseStack, MIN_POS_MESSAGE, (width / 2) - font.width(MIN_POS_MESSAGE) / 2, yOffset + 5, 0x404040);
		font.draw(poseStack, MAX_POS_MESSAGE, (width / 2) - font.width(MAX_POS_MESSAGE) / 2, yOffset + 40, 0x404040);
		super.render(poseStack, mouseX, mouseY, partialTicks);
	}
	
	private void finish()
	{
		int minX = parseInt(minPosDestinationTextFieldX);
		int minY = parseInt(minPosDestinationTextFieldY);
		int minZ = parseInt(minPosDestinationTextFieldZ);
		int maxX = parseInt(maxPosDestinationTextFieldX);
		int maxY = parseInt(maxPosDestinationTextFieldY);
		int maxZ = parseInt(maxPosDestinationTextFieldZ);
		
		BlockPos minOffsetPos = new BlockPos(minX, minY, minZ);
		BlockPos maxOffsetPos = new BlockPos(maxX, maxY, maxZ);
		
		if(validInput)
		{
			MSPacketHandler.sendToServer(new AreaEffectPacket(getEffect(effectTextField.getValue()), Mth.clamp(parseInt(effectAmplifierTextField), 0, 255), isAllMobs, minOffsetPos, maxOffsetPos, be.getBlockPos()));
			onClose();
		}
		
		validInput = true; //allows players to try again
	}
	
	private int parseInt(EditBox widget)
	{
		int parsedValue = 0; //arbitrary starting number that will not be used in the packet as is
		
		try
		{
			parsedValue = Integer.parseInt(widget.getValue());
			widget.setTextColor(0XFFFFFF); //refreshes text color to white in case it was invalid before but is now acceptable
		} catch(NumberFormatException ignored)
		{
			validInput = false;
			widget.setTextColor(0XFF0000); //changes text to red to indicate that it is an invalid type
		}
		
		return parsedValue;
	}
}