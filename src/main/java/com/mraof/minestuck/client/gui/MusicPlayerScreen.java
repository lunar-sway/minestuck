package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mraof.minestuck.inventory.MusicPlayerContainer;
import com.mraof.minestuck.network.GoButtonPacket;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.tileentity.machine.MachineProcessTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.client.gui.widget.ExtendedButton;
import org.lwjgl.glfw.GLFW;

public class MusicPlayerScreen extends AbstractContainerScreen<MusicPlayerContainer>
{
	private final ResourceLocation BACKGROUND = new ResourceLocation("minestuck:textures/gui/music_player.png");
	
	public MusicPlayerScreen(MusicPlayerContainer container, Inventory pPlayerInventory, Component pTitle)
	{
		super(container, pPlayerInventory, pTitle);
	}
	
	@Override
	protected void renderBg(PoseStack matrixStack, float par1, int par2, int par3)
	{
		int x = (width - imageWidth) / 2;
		int y = (height - imageHeight) / 2;
		
		//draw background
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.setShaderTexture(0, BACKGROUND);
		this.blit(matrixStack, x, y, 0, 0, imageWidth, imageHeight);
	}
}
