package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mraof.minestuck.blockentity.machine.PunchDesignixBlockEntity;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.PunchDesignixPacket;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.widget.ExtendedButton;

import java.util.regex.Pattern;

public class PunchDesignixScreen extends Screen
{
	public static final String TITLE = "minestuck.punch_designix";
	public static final String ENTER_CAPTCHA_MESSAGE = "minestuck.punch_designix.enter_captcha";
	public static final String PUNCH_MESSAGE = "minestuck.punch_designix.punch";
	private static final ResourceLocation guiBackground = new ResourceLocation("minestuck", "textures/gui/generic_small.png");
	
	private static final int guiWidth = 126;
	private static final int guiHeight = 98;
	
	PunchDesignixBlockEntity be;
	private EditBox captchaTextField;
	private Button doneButton;
	
	
	PunchDesignixScreen(PunchDesignixBlockEntity be)
	{
		super(Component.translatable(TITLE));
		
		this.be = be;
	}
	
	@Override
	public void init()
	{
		int yOffset = (this.height / 2) - (guiHeight / 2);
		this.captchaTextField = new EditBox(this.font, this.width / 2 - 40, yOffset + 25, 80, 20, Component.translatable(ENTER_CAPTCHA_MESSAGE));
		this.captchaTextField.setCanLoseFocus(false);
		
		String storedCaptcha = be.getCaptcha();
		if(storedCaptcha != null)
			this.captchaTextField.setValue(storedCaptcha);
		
		this.captchaTextField.setMaxLength(8);
		this.captchaTextField.setFilter((text) -> Pattern.matches("^[0-9a-zA-Z!#?]+$", text) || text.isEmpty()); //"^[0-9a-zA-Z!#?]+$" indicates that it will only accept characters used to represent hex or the characters "!"/"#"/"?"
		captchaTextField.setResponder(s -> doneButton.active = s.length() == 8);
		addRenderableWidget(captchaTextField);
		setInitialFocus(captchaTextField);
		
		addRenderableWidget(doneButton = new ExtendedButton(this.width / 2 - 20, yOffset + 50, 40, 20, Component.translatable(PUNCH_MESSAGE), button -> finish()));
		doneButton.active = captchaTextField.getValue().length() == 8;
	}
	
	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(poseStack);
		
		int yOffset = (this.height / 2) - (guiHeight / 2);
		
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.setShaderTexture(0, guiBackground);
		this.blit(poseStack, (this.width / 2) - (guiWidth / 2), yOffset, 0, 0, guiWidth, guiHeight);
		
		super.render(poseStack, mouseX, mouseY, partialTicks);
	}
	
	private void finish()
	{
		if(this.captchaTextField.getValue().length() == 8)
		{
			PunchDesignixPacket packet = new PunchDesignixPacket(be.getBlockPos(), captchaTextField.getValue());
			MSPacketHandler.sendToServer(packet);
			this.minecraft.setScreen(null);
		}
	}
}