package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.block.redstone.AreaEffectBlock;
import com.mraof.minestuck.network.AreaEffectPacket;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.tileentity.redstone.AreaEffectTileEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;
import net.minecraftforge.registries.ForgeRegistries;

public class AreaEffectScreen extends Screen
{
	private static final ResourceLocation GUI_BACKGROUND = new ResourceLocation("minestuck", "textures/gui/generic_large.png");
	
	private static final int GUI_WIDTH = 150;
	private static final int GUI_HEIGHT = 132;
	
	private static final String MIN_POS_MESSAGE = "Min Pos Facing Offset"; //TODO make translatable (lang file + translation key)
	private static final String MAX_POS_MESSAGE = "Max Pos Facing Offset";
	
	private final AreaEffectTileEntity te;
	private TextFieldWidget minPosDestinationTextFieldX;
	private TextFieldWidget minPosDestinationTextFieldY;
	private TextFieldWidget minPosDestinationTextFieldZ;
	private TextFieldWidget maxPosDestinationTextFieldX;
	private TextFieldWidget maxPosDestinationTextFieldY;
	private TextFieldWidget maxPosDestinationTextFieldZ;
	private TextFieldWidget effectTextField;
	private TextFieldWidget effectAmplifierTextField;
	private boolean isAllMobs;
	
	private Button allMobsButton;
	
	
	AreaEffectScreen(AreaEffectTileEntity te)
	{
		super(new StringTextComponent("Area Effect Block"));
		
		this.te = te;
		this.isAllMobs = te.getBlockState().getValue(AreaEffectBlock.ALL_MOBS);
	}
	
	@Override
	public void init()
	{
		int yOffset = (this.height / 2) - (GUI_HEIGHT / 2);
		
		this.minPosDestinationTextFieldX = new TextFieldWidget(this.font, this.width / 2 - 60, yOffset + 15, 40, 20, new StringTextComponent("X value of min effect pos")); //TODO make these translatable
		this.minPosDestinationTextFieldX.setValue(String.valueOf(te.getMinAreaOffset().getX()));
		addButton(minPosDestinationTextFieldX);
		
		this.minPosDestinationTextFieldY = new TextFieldWidget(this.font, this.width / 2 - 20, yOffset + 15, 40, 20, new StringTextComponent("Y value of min effect pos"));
		this.minPosDestinationTextFieldY.setValue(String.valueOf(te.getMinAreaOffset().getY()));
		addButton(minPosDestinationTextFieldY);
		
		this.minPosDestinationTextFieldZ = new TextFieldWidget(this.font, this.width / 2 + 20, yOffset + 15, 40, 20, new StringTextComponent("Z value of min effect pos"));
		this.minPosDestinationTextFieldZ.setValue(String.valueOf(te.getMinAreaOffset().getZ()));
		addButton(minPosDestinationTextFieldZ);
		
		this.maxPosDestinationTextFieldX = new TextFieldWidget(this.font, this.width / 2 - 60, yOffset + 50, 40, 20, new StringTextComponent("X value of max effect pos"));
		this.maxPosDestinationTextFieldX.setValue(String.valueOf(te.getMaxAreaOffset().getX()));
		addButton(maxPosDestinationTextFieldX);
		
		this.maxPosDestinationTextFieldY = new TextFieldWidget(this.font, this.width / 2 - 20, yOffset + 50, 40, 20, new StringTextComponent("Y value of max effect pos"));
		this.maxPosDestinationTextFieldY.setValue(String.valueOf(te.getMaxAreaOffset().getY()));
		addButton(maxPosDestinationTextFieldY);
		
		this.maxPosDestinationTextFieldZ = new TextFieldWidget(this.font, this.width / 2 + 20, yOffset + 50, 40, 20, new StringTextComponent("Z value of max effect pos"));
		this.maxPosDestinationTextFieldZ.setValue(String.valueOf(te.getMaxAreaOffset().getZ()));
		addButton(maxPosDestinationTextFieldZ);
		
		
		this.effectTextField = new TextFieldWidget(this.font, this.width / 2 - 65, yOffset + 79, 105, 18, new StringTextComponent("Current Effect"));
		this.effectTextField.setValue(te.getEffect().getRegistryName().toString());
		addButton(effectTextField);
		
		this.effectAmplifierTextField = new TextFieldWidget(this.font, this.width / 2 + 45, yOffset + 79, 20, 18, new StringTextComponent("Current Effect Amplifier"));
		this.effectAmplifierTextField.setValue(String.valueOf(te.getEffectAmplifier()));
		addButton(effectAmplifierTextField);
		
		addButton(allMobsButton = new ExtendedButton(this.width / 2 - 65, yOffset + 105, 85, 20, getAllMobsButtonMessage(), button -> cycleIsAllMobs()));
		
		addButton(new ExtendedButton(this.width / 2 + 25, yOffset + 105, 40, 20, new StringTextComponent("DONE"), button -> finish()));
	}
	
	private ITextComponent getAllMobsButtonMessage()
	{
		return this.isAllMobs ? new StringTextComponent("UNTRIGGERABLE") : new StringTextComponent("TRIGGERABLE");
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
	private Effect getEffect(String stringInput)
	{
		return ForgeRegistries.POTIONS.getValue(ResourceLocation.tryParse(stringInput));
	}
	
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(matrixStack);
		RenderSystem.color4f(1F, 1F, 1F, 1F);
		this.minecraft.getTextureManager().bind(GUI_BACKGROUND);
		int yOffset = (this.height / 2) - (GUI_HEIGHT / 2);
		this.blit(matrixStack, (this.width / 2) - (GUI_WIDTH / 2), yOffset, 0, 0, GUI_WIDTH, GUI_HEIGHT);
		font.draw(matrixStack, MIN_POS_MESSAGE, (width / 2) - font.width(MIN_POS_MESSAGE) / 2, yOffset + 5, 0x404040);
		font.draw(matrixStack, MAX_POS_MESSAGE, (width / 2) - font.width(MAX_POS_MESSAGE) / 2, yOffset + 40, 0x404040);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
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
		
		MSPacketHandler.sendToServer(new AreaEffectPacket(getEffect(effectTextField.getValue()), MathHelper.clamp(parseInt(effectAmplifierTextField), 0, 255), isAllMobs, minOffsetPos, maxOffsetPos, te.getBlockPos()));
		onClose();
	}
	
	private static int parseInt(TextFieldWidget widget)
	{
		try
		{
			return Integer.parseInt(widget.getValue());
		} catch(NumberFormatException ignored)
		{
			return 0;
		}
	}
}