package com.mraof.minestuck.client.gui;

import com.mraof.minestuck.blockentity.machine.PunchDesignixBlockEntity;
import com.mraof.minestuck.network.block.TriggerPunchDesignixPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;
import net.neoforged.neoforge.network.PacketDistributor;

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
		this.captchaTextField.setFilter((text) -> Pattern.matches("^[0-9a-zA-Z!#?]+$", text) || text.isEmpty()); //"^[0-9a-zA-Z!#?]+$" indicates that it will only accept alphanumeric characters or the characters "!"/"#"/"?"
		captchaTextField.setResponder(s -> doneButton.active = s.length() == 8);
		addRenderableWidget(captchaTextField);
		setInitialFocus(captchaTextField);
		
		addRenderableWidget(doneButton = new ExtendedButton(this.width / 2 - 20, yOffset + 50, 40, 20, Component.translatable(PUNCH_MESSAGE), button -> finish()));
		doneButton.active = captchaTextField.getValue().length() == 8;
	}
	
	@Override
	public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks)
	{
		super.renderBackground(graphics, mouseX, mouseY, partialTicks);
		
		int yOffset = (this.height / 2) - (guiHeight / 2);
		int xOffset = (this.width / 2) - (guiWidth / 2);
		graphics.blit(guiBackground, xOffset, yOffset, 0, 0, guiWidth, guiHeight);
	}
	
	private void finish()
	{
		if(this.captchaTextField.getValue().length() == 8)
		{
			String captcha = captchaTextField.getValue();
			be.setCaptcha(captcha);
			PacketDistributor.SERVER.noArg().send(new TriggerPunchDesignixPacket(captcha, be.getBlockPos()));
			this.minecraft.setScreen(null);
		}
	}
}
