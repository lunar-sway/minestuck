package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.inventory.SendificatorContainer;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.SendificatorPacket;
import com.mraof.minestuck.tileentity.machine.MachineProcessTileEntity;
import com.mraof.minestuck.tileentity.machine.SendificatorTileEntity;
import com.mraof.minestuck.util.Debug;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

public class SendificatorScreen extends MachineScreen<SendificatorContainer>
{
	private static final ResourceLocation BACKGROUND = new ResourceLocation("minestuck:textures/gui/uranium_cooker.png");
	private static final ResourceLocation PROGRESS = new ResourceLocation("minestuck:textures/gui/progress/uranium_cooker.png");
	
	private int progressX;
	private int progressY;
	private int progressWidth;
	private int progressHeight;
	private int goX;
	private int goY;
	private static final int guiWidth = 150;
	private static final int guiHeight = 98;
	
	private final SendificatorTileEntity te;
	private TextFieldWidget destinationTextFieldX;
	private TextFieldWidget destinationTextFieldY;
	private TextFieldWidget destinationTextFieldZ;
	
	
	SendificatorScreen(SendificatorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
	{
		super(MachineProcessTileEntity.RunType.BUTTON, screenContainer, inv, titleIn);
		
		//sets progress bar information
		progressX = 67;
		progressY = 24;
		progressWidth = 35;
		progressHeight = 39;
		goX = 69;
		goY = 69;
		
		SendificatorTileEntity tempTE = null;
		World world = screenContainer.getTEWorld();
		if(world != null/* && world.isClientSide*/)
		{
			TileEntity tileEntity = world.getBlockEntity(screenContainer.machinePos);
			if(tileEntity instanceof SendificatorTileEntity)
				tempTE = ((SendificatorTileEntity) tileEntity);
		}
		te = tempTE;
	}
	
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderTooltip(matrixStack, mouseX, mouseY);
		int yOffset = (this.height / 2) - (guiHeight / 2);
		this.blit(matrixStack, (this.width / 2) - (guiWidth / 2), yOffset, 0, 0, imageWidth, imageHeight);
	}
	
	@Override
	protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY)
	{
		//TODO INameable confirmed to work?
		//draws the name of the TE, which may be customized via anvil
		font.draw(matrixStack, this.te.getDisplayName(), 8, 6, 4210752);
		
		//draws "Inventory" or your regional equivalent
		font.draw(matrixStack, this.inventory.getDisplayName().getString(), 8, imageHeight - 96 + 2, 4210752);
	}
	
	@Override
	protected void renderBg(MatrixStack matrixStack, float par1, int par2, int par3)
	{
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		//draw background
		this.minecraft.getTextureManager().bind(BACKGROUND);
		int x = (width - imageWidth) / 2;
		int y = (height - imageHeight) / 2;
		this.blit(matrixStack, x, y, 0, 0, imageWidth, imageHeight);
		
		//draw progress bar
		this.minecraft.getTextureManager().bind(PROGRESS);
		int width = progressWidth;
		int height = getScaledValue(menu.getFuel(), SendificatorTileEntity.getMaxFuel(), progressHeight);
		blit(matrixStack, x + progressX, y + progressY + progressHeight - height, 0, progressHeight - height, width, height, progressWidth, progressHeight);
	}
	
	@Override
	public void init()
	{
		super.init();
		
		if(te != null) //TODO does not render
		{
			int yOffset = (this.height / 2) - (guiHeight / 2);
			
			this.destinationTextFieldX = new TextFieldWidget(this.font, this.width / 2 - 20, 10, 40, 20, new StringTextComponent("X value of destination block pos")); //TODO make these translatable
			this.destinationTextFieldX.setValue(String.valueOf(te.getDestinationBlockPos().getX()));
			addButton(destinationTextFieldX);
			
			this.destinationTextFieldY = new TextFieldWidget(this.font, this.width / 2 + 20, 10, 40, 20, new StringTextComponent("Y value of destination block pos"));
			this.destinationTextFieldY.setValue(String.valueOf(te.getDestinationBlockPos().getY()));
			addButton(destinationTextFieldY);
			
			this.destinationTextFieldZ = new TextFieldWidget(this.font, this.width / 2 + 60, 10, 40, 20, new StringTextComponent("Z value of destination block pos"));
			this.destinationTextFieldZ.setValue(String.valueOf(te.getDestinationBlockPos().getZ()));
			addButton(destinationTextFieldZ);
			
			addButton(new ExtendedButton((width - imageWidth) / 2 + 90, 40, 60, 20, new StringTextComponent("Update"), button -> updateDestinationPos()));
		}
		
		goButton = new GoButton((width - imageWidth) / 2 + goX, (height - imageHeight) / 2 + goY, 30, 12, new StringTextComponent(menu.overrideStop() ? "STOP" : "GO"));
		addButton(goButton);
	}
	
	private void updateDestinationPos()
	{
		MSPacketHandler.sendToServer(new SendificatorPacket(parseBlockPos(), te.getBlockPos()));
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
	
	private BlockPos parseBlockPos()
	{
		int x = parseInt(destinationTextFieldX);
		int y = parseInt(destinationTextFieldY);
		int z = parseInt(destinationTextFieldZ);
		
		return new BlockPos(x, y, z);
	}
	
	/*private SendificatorTileEntity getTE()
	{
		//if(this.getMinecraft())
		ClientWorld clientWorld = this.getMinecraft().level;
		return clientWorld.te
	}*/
	
	/*@Override
	public void init()
	{
		int yOffset = (this.height / 2) - (guiHeight / 2);
		
		this.destinationTextFieldX = new TextFieldWidget(this.font, this.width / 2 - 60, yOffset + 10, 40, 20, new StringTextComponent("X value of destination block pos")); //TODO make these translatable
		this.destinationTextFieldX.setValue(String.valueOf(te.getDestinationBlockPos().getX()));
		addButton(destinationTextFieldX);
		
		this.destinationTextFieldY = new TextFieldWidget(this.font, this.width / 2 - 20, yOffset + 10, 40, 20, new StringTextComponent("Y value of destination block pos"));
		this.destinationTextFieldY.setValue(String.valueOf(te.getDestinationBlockPos().getY()));
		addButton(destinationTextFieldY);
		
		this.destinationTextFieldZ = new TextFieldWidget(this.font, this.width / 2 + 20, yOffset + 10, 40, 20, new StringTextComponent("Z value of destination block pos"));
		this.destinationTextFieldZ.setValue(String.valueOf(te.getDestinationBlockPos().getZ()));
		addButton(destinationTextFieldZ);
		
		//addButton(new ExtendedButton(this.width / 2 - 45, yOffset + 40, 90, 20, new StringTextComponent("Find Receiver"), button -> findReceiver()));
		
		addButton(new ExtendedButton(this.width / 2 - 20, yOffset + 70, 40, 20, new StringTextComponent("DONE"), button -> finish()));
	}
	
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(matrixStack);
		RenderSystem.color4f(1F, 1F, 1F, 1F);
		this.minecraft.getTextureManager().bind(guiBackground);
		int yOffset = (this.height / 2) - (guiHeight / 2);
		this.blit(matrixStack, (this.width / 2) - (guiWidth / 2), yOffset, 0, 0, guiWidth, guiHeight);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}
	
	private void finish()
	{
		MSPacketHandler.sendToServer(new WirelessRedstoneTransmitterPacket(parseBlockPos(), te.getBlockPos()));
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
	
	private BlockPos parseBlockPos()
	{
		int x = parseInt(destinationTextFieldX);
		int y = parseInt(destinationTextFieldY);
		int z = parseInt(destinationTextFieldZ);
		
		return new BlockPos(x, y, z);
	}*/
}