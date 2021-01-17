package com.mraof.minestuck.client.gui;

import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.TitleSelectPacket;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.EnumClass;
import com.mraof.minestuck.player.Title;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

public class TitleSelectorScreen extends Screen
{
	public static final String TITLE = "minestuck.title_selector";
	public static final String SELECT_TITLE = "minestuck.select_title";
	public static final String USED_TITLE = "minestuck.select_title.used";
	public static final String SELECT = "minestuck.select_title.select";
	public static final String RANDOMIZE = "minestuck.select_title.randomize";
	
	private static final ResourceLocation guiBackground = new ResourceLocation("minestuck", "textures/gui/title_selector.png");
	private static final int guiWidth = 186, guiHeight = 157;
	
	private EnumClass currentClass;
	private EnumAspect currentAspect;
	private final Button[] classButtons = new Button[12], aspectButtons = new Button[12];
	private Button selectButton;
	
	private final Title previous;
	
	TitleSelectorScreen(Title title)
	{
		super(new TranslationTextComponent(TITLE));
		previous = title;
	}
	
	@Override
	public void init()
	{
		int leftX = (width - guiWidth) / 2, topY = (height - guiHeight) / 2;
		for(EnumClass c : EnumClass.values())
		{
			int i = EnumClass.getIntFromClass(c);
			if(i < 12)
			{
				ITextComponent className = c.asTextComponent();
				Button button = new ExtendedButton(leftX + 4 + (i % 2) * 40, topY + 24 + (i / 2) * 16, 40, 16, className.getFormattedText(), button1 -> pickClass(c));
				addButton(button);
				classButtons[i] = button;
			}
		}
		for(EnumAspect a : EnumAspect.values())
		{
			int i = EnumAspect.getIntFromAspect(a);
			if(i < 12)
			{
				ITextComponent aspectName = a.asTextComponent();
				Button button = new ExtendedButton(leftX + 102 + (i % 2) * 40, topY + 24 + (i / 2) * 16, 40, 16, aspectName.getFormattedText(), button1 -> pickAspect(a));
				addButton(button);
				aspectButtons[i] = button;
			}
		}
		
		addButton(selectButton = new ExtendedButton(leftX + 24, topY + 128, 60, 20, I18n.format(SELECT), button -> select()));
		addButton(new ExtendedButton(leftX + 102, topY + 128, 60, 20, I18n.format(RANDOMIZE), button -> random()));
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		selectButton.active = currentClass != null && currentAspect != null;
		
		int xOffset = (width - guiWidth)/2;
		int yOffset = (height - guiHeight)/2;
		
		this.renderBackground();
		
		this.minecraft.getTextureManager().bindTexture(guiBackground);
		this.blit(xOffset, yOffset, 0, 0, guiWidth, guiHeight);
		
		String message = previous == null ? I18n.format(SELECT_TITLE) : I18n.format(USED_TITLE, previous.asTextComponent().getFormattedText());
		font.drawString(message, (this.width / 2F) - font.getStringWidth(message) / 2F, yOffset + 10, 0x404040);
		
		message = I18n.format(Title.FORMAT, "", "");
		font.drawString(message, (this.width / 2F) - font.getStringWidth(message) / 2F, yOffset + 72 - font.FONT_HEIGHT/2F, 0x404040);
		
		super.render(mouseX, mouseY, partialTicks);
		
	}
	
	private void pickClass(EnumClass c)
	{
		if(currentClass != null)
			classButtons[EnumClass.getIntFromClass(currentClass)].active = true;
		currentClass = c;
		classButtons[EnumClass.getIntFromClass(currentClass)].active = false;
	}
	
	private void pickAspect(EnumAspect a)
	{
		if(currentAspect != null)
			aspectButtons[EnumAspect.getIntFromAspect(currentAspect)].active = true;
		currentAspect = a;
		aspectButtons[EnumAspect.getIntFromAspect(currentAspect)].active = false;
	}
	
	private void select()
	{
		MSPacketHandler.sendToServer(new TitleSelectPacket(new Title(currentClass, currentAspect)));
		onClose();
	}
	
	private void random()
	{
		MSPacketHandler.sendToServer(new TitleSelectPacket());
		onClose();
	}
	
	@Override
	public boolean shouldCloseOnEsc()
	{
		return false;
	}
}