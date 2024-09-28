package com.mraof.minestuck.client.gui;

import com.mraof.minestuck.block.redstone.AreaEffectBlock;
import com.mraof.minestuck.blockentity.redstone.AreaEffectBlockEntity;
import com.mraof.minestuck.network.block.AreaEffectSettingsPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class AreaEffectScreen extends Screen
{
	public static final String TITLE = "minestuck.area_effect";
	public static final String MIN_POS_MESSAGE = "minestuck.area_effect.min_pose_message";
	public static final String MAX_POS_MESSAGE = "minestuck.area_effect.max_pose_message";
	public static final String X_MIN_MESSAGE = "minestuck.area_effect.x_min_message";
	public static final String Y_MIN_MESSAGE = "minestuck.area_effect.Y_min_message";
	public static final String Z_MIN_MESSAGE = "minestuck.area_effect.z_min_message";
	public static final String X_MAX_MESSAGE = "minestuck.area_effect.x_max_message";
	public static final String Y_MAX_MESSAGE = "minestuck.area_effect.y_max_message";
	public static final String Z_MAX_MESSAGE = "minestuck.area_effect.z_max_message";
	public static final String CURRENT_EFFECT_MESSAGE = "minestuck.area_effect.current_effect";
	public static final String CURRENT_EFFECT_AMPLIFIER_MESSAGE = "minestuck.area_effect.current_effect_amplifier";
	public static final String DONE_MESSAGE = "minestuck.area_effect.done";
	public static final String ALL_MOBS_MESSAGE = "minestuck.area_effect.all_mobs";
	public static final String JUST_PLAYERS_MESSAGE = "minestuck.area_effect.just_players";
	private static final ResourceLocation GUI_BACKGROUND = new ResourceLocation("minestuck", "textures/gui/generic_large.png");
	
	private static final int GUI_WIDTH = 150;
	private static final int GUI_HEIGHT = 132;
	
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
		super(Component.translatable(TITLE));
		
		this.be = be;
		this.isAllMobs = be.getBlockState().getValue(AreaEffectBlock.ALL_MOBS);
	}
	
	@Override
	public void init()
	{
		int yOffset = (this.height / 2) - (GUI_HEIGHT / 2);
		
		this.minPosDestinationTextFieldX = new EditBox(this.font, this.width / 2 - 60, yOffset + 15, 40, 20, Component.translatable(X_MIN_MESSAGE));
		this.minPosDestinationTextFieldX.setValue(String.valueOf(be.getMinAreaOffset().getX()));
		addRenderableWidget(minPosDestinationTextFieldX);
		
		this.minPosDestinationTextFieldY = new EditBox(this.font, this.width / 2 - 20, yOffset + 15, 40, 20, Component.translatable(Y_MIN_MESSAGE));
		this.minPosDestinationTextFieldY.setValue(String.valueOf(be.getMinAreaOffset().getY()));
		addRenderableWidget(minPosDestinationTextFieldY);
		
		this.minPosDestinationTextFieldZ = new EditBox(this.font, this.width / 2 + 20, yOffset + 15, 40, 20, Component.translatable(Z_MIN_MESSAGE));
		this.minPosDestinationTextFieldZ.setValue(String.valueOf(be.getMinAreaOffset().getZ()));
		addRenderableWidget(minPosDestinationTextFieldZ);
		
		this.maxPosDestinationTextFieldX = new EditBox(this.font, this.width / 2 - 60, yOffset + 50, 40, 20, Component.translatable(X_MAX_MESSAGE));
		this.maxPosDestinationTextFieldX.setValue(String.valueOf(be.getMaxAreaOffset().getX()));
		addRenderableWidget(maxPosDestinationTextFieldX);
		
		this.maxPosDestinationTextFieldY = new EditBox(this.font, this.width / 2 - 20, yOffset + 50, 40, 20, Component.translatable(Y_MAX_MESSAGE));
		this.maxPosDestinationTextFieldY.setValue(String.valueOf(be.getMaxAreaOffset().getY()));
		addRenderableWidget(maxPosDestinationTextFieldY);
		
		this.maxPosDestinationTextFieldZ = new EditBox(this.font, this.width / 2 + 20, yOffset + 50, 40, 20, Component.translatable(Z_MAX_MESSAGE));
		this.maxPosDestinationTextFieldZ.setValue(String.valueOf(be.getMaxAreaOffset().getZ()));
		addRenderableWidget(maxPosDestinationTextFieldZ);
		
		
		this.effectTextField = new EditBox(this.font, this.width / 2 - 65, yOffset + 79, 105, 18, Component.translatable(CURRENT_EFFECT_MESSAGE));
		this.effectTextField.setValue(String.valueOf(BuiltInRegistries.MOB_EFFECT.getKey(be.getEffect())));
		addRenderableWidget(effectTextField);
		
		this.effectAmplifierTextField = new EditBox(this.font, this.width / 2 + 45, yOffset + 79, 20, 18, Component.translatable(CURRENT_EFFECT_AMPLIFIER_MESSAGE));
		this.effectAmplifierTextField.setValue(String.valueOf(be.getEffectAmplifier()));
		addRenderableWidget(effectAmplifierTextField);
		
		addRenderableWidget(allMobsButton = new ExtendedButton(this.width / 2 - 65, yOffset + 105, 85, 20, getAllMobsButtonMessage(), button -> cycleIsAllMobs()));
		
		addRenderableWidget(new ExtendedButton(this.width / 2 + 25, yOffset + 105, 40, 20, Component.translatable(DONE_MESSAGE), button -> finish()));
	}
	
	private Component getAllMobsButtonMessage()
	{
		return this.isAllMobs ? Component.translatable(ALL_MOBS_MESSAGE) : Component.translatable(JUST_PLAYERS_MESSAGE);
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
		return BuiltInRegistries.MOB_EFFECT.get(ResourceLocation.tryParse(stringInput));
	}
	
	@Override
	public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		super.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
		
		int yOffset = (this.height / 2) - (GUI_HEIGHT / 2);
		guiGraphics.blit(GUI_BACKGROUND, (this.width / 2) - (GUI_WIDTH / 2), yOffset, 0, 0, GUI_WIDTH, GUI_HEIGHT);
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		
		int yOffset = (this.height / 2) - (GUI_HEIGHT / 2);
		guiGraphics.drawString(font, Component.translatable(MIN_POS_MESSAGE), (width / 2) - font.width(Component.translatable(MIN_POS_MESSAGE)) / 2, yOffset + 5, 0x404040, false);
		guiGraphics.drawString(font, Component.translatable(MAX_POS_MESSAGE), (width / 2) - font.width(Component.translatable(MAX_POS_MESSAGE)) / 2, yOffset + 40, 0x404040, false);
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
			PacketDistributor.SERVER.noArg().send(new AreaEffectSettingsPacket(getEffect(effectTextField.getValue()), Mth.clamp(parseInt(effectAmplifierTextField), 0, 255), isAllMobs, minOffsetPos, maxOffsetPos, be.getBlockPos()));
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
