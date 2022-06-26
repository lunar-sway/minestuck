package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.block.Block;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ChangePageButton;
import net.minecraft.resources.IResource;
import net.minecraft.util.*;
import net.minecraft.util.text.*;
import org.apache.commons.lang3.mutable.MutableInt;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Code is modified components of, or inspired by, WinGameScreen/ReadBookScreen/StoneTabletScreen
 */
public class ReadableSburbCodeScreen extends Screen
{
	public static final ResourceLocation BOOK_TEXTURES = new ResourceLocation("textures/gui/book.png");
	private final List<Block> blockList;
	private final boolean paradoxCode;

	private ChangePageButton forwardButton;
	private ChangePageButton backButton;
	
	public List<String> textList = null;
	public int linesPerBlock = 1;
	public int hieroglyphCount;
	public int totalPages = 8;
	public int currentPage = 0;
	
	//GUI sizes
	public static final int GUI_WIDTH = 192;
	public static final int GUI_HEIGHT = 192;
	public static final int TEXT_WIDTH = 288; //114
	public static final int TEXT_OFFSET_X = 36, TEXT_OFFSET_Y = 32;
	
	public static final int CUSTOM_LINE_HEIGHT = 3;
	public static final int LINES_PER_PAGE = 40; //how many lines can be fit neatly on a page, at 48 character per line
	
	//public final static String EMPTY_SPACE = "                                                                            "; //76 characters
	public final static String EMPTY_SPACE = "                                                            "; //60 characters
	
	public ReadableSburbCodeScreen(List<Block> blockList, boolean paradoxCode)
	{
		super(NarratorChatListener.NO_TITLE);
		this.blockList = blockList;
		this.paradoxCode = paradoxCode;
		
		this.hieroglyphCount = MSTags.Blocks.GREEN_HIEROGLYPHS.getValues().size() + (paradoxCode ? 1 : 0); //adds paradox code to total count
	}
	
	@Override
	protected void init()
	{
		super.init();
		this.createMenuControls();
		this.createPageControlButtons();
		
		try
		{
			IResource iResource = this.minecraft.getResourceManager().getResource(new ResourceLocation(Minestuck.MOD_ID, "texts/rana_temporaria_sec22b.txt")); //The text is broken into lines 60 characters long to neatly fit within a block of text that avoids empty spaces
			InputStream inputStream = iResource.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
			textList = bufferedReader.lines().collect(Collectors.toList());
			
			linesPerBlock = textList.size() / hieroglyphCount;
			totalPages = Math.round((float) (textList.size() + 1) / (float) LINES_PER_PAGE);
		} catch(Exception ignored)
		{
		}
	}
	
	protected void createMenuControls()
	{
		if(this.minecraft != null)
		{
			this.addButton(new Button(this.width / 2 - 100, 196, 200, 20, DialogTexts.GUI_DONE, (p_214161_1_) -> {
				this.minecraft.setScreen(null);
			}));
		}
	}
	
	protected void createPageControlButtons()
	{
		int i = (this.width - 192) / 2;
		this.forwardButton = this.addButton(new ChangePageButton(i + 116, 159, true, (p_214159_1_) -> {
			this.pageForward();
		}, true));
		this.backButton = this.addButton(new ChangePageButton(i + 43, 159, false, (p_214158_1_) -> {
			this.pageBack();
		}, true));
		this.updateButtonVisibility();
	}
	
	/**
	 * Moves the display back one page
	 */
	protected void pageBack()
	{
		if(this.currentPage > 0)
		{
			--this.currentPage;
		}
		
		this.updateButtonVisibility();
	}
	
	/**
	 * Moves the display forward one page
	 */
	protected void pageForward()
	{
		if(this.currentPage < totalPages)
		{
			++this.currentPage;
		}
		
		this.updateButtonVisibility();
	}
	
	private void updateButtonVisibility()
	{
		this.forwardButton.visible = this.currentPage < totalPages;
		this.backButton.visible = this.currentPage > 0;
	}
	
	public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers)
	{
		if(super.keyPressed(pKeyCode, pScanCode, pModifiers))
		{
			return true;
		} else
		{
			switch(pKeyCode)
			{
				case 266:
					this.backButton.onPress();
					return true;
				case 267:
					this.forwardButton.onPress();
					return true;
				default:
					return false;
			}
		}
	}
	
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(matrixStack);
		this.setFocused(null);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bind(BOOK_TEXTURES);
		
		//TODO rendering error where seemingly bits of vanilla text are rendered onto the screen, such as the last displayed splash text
		/*ITextComponent pageMsg = new TranslationTextComponent("book.pageIndicator", this.currentPage + 1, Math.max(totalPages, 1));
		int pageMsgPos = (this.width - 192) / 2;
		int pageMsgWidth = this.font.width(pageMsg);
		this.font.draw(matrixStack, pageMsg, (float) (pageMsgPos - pageMsgWidth + 192 - 44), 18.0F, 0);*/
		
		int topX = (this.width - GUI_WIDTH) / 2;
		int topY = 2;
		this.blit(matrixStack, topX, topY, 0, 0, GUI_WIDTH, GUI_HEIGHT);
		{
			if(textList != null)
			{
				//push matrix function, followed by the scaled function and ending with the pop matrix function, allow the scale changes to only be applied to the text
				RenderSystem.pushMatrix();
				float subtractScale = 0.4F;
				float scale = (1 / subtractScale);
				RenderSystem.scaled(subtractScale, subtractScale, subtractScale);
				
				List<Block> fullBlockList = MSTags.Blocks.GREEN_HIEROGLYPHS.getValues();
				MutableInt lineY = new MutableInt();
				boolean isPresent = false;
				
				for(int lineIterate = 0; lineIterate < hieroglyphCount; lineIterate++)
				{
					//allows the rendered code to be made in chunks proportional to the size of the full sequence
					//TODO after the second page, the starting line of the page(set through fromIndex) shows both the last line of the previous page and the line before the last one
					int fromIndex = Math.min(((lineIterate * linesPerBlock + lineIterate + (currentPage == 0 ? 0 : 1)) + (LINES_PER_PAGE * currentPage)), textList.size());
					int toIndex = Math.min(((lineIterate * linesPerBlock + linesPerBlock) + (LINES_PER_PAGE * currentPage)), textList.size());
					List<String> blockTextList = textList.subList(fromIndex, toIndex);
					
					//TODO fix to no longer be representative of just one page, fix to account for paradox code
					if((isValidBlock(lineIterate) &&
							fullBlockList.contains(blockList.get(lineIterate)))
							|| (lineIterate == hieroglyphCount - 1 && paradoxCode))
					{
						isPresent = true;
					}
					
					for(String text : blockTextList)
					{
						if(isPresent)
						{
							font.getSplitter().splitLines(text, TEXT_WIDTH, Style.EMPTY, true, (style, start, end) -> {
								//limiting the length of the page via this if statement
								if(stillValidLine(lineY.intValue()))
								{
									ITextComponent line = new StringTextComponent(text.substring(start, end)).setStyle(style);
									font.draw(matrixStack, line, ((this.width - GUI_WIDTH) / 2F + TEXT_OFFSET_X) * scale, (lineY.intValue() + TEXT_OFFSET_Y) * scale, 0x000000);
									lineY.add(CUSTOM_LINE_HEIGHT);
								}
							});
						} else
						{
							font.getSplitter().splitLines(EMPTY_SPACE, TEXT_WIDTH, Style.EMPTY, true, (style, start, end) -> {
								if(stillValidLine(lineY.intValue()))
								{
									ITextComponent line = new StringTextComponent(EMPTY_SPACE.substring(start, end)).setStyle(style);
									font.draw(matrixStack, line, ((this.width - GUI_WIDTH) / 2F + TEXT_OFFSET_X) * scale, (lineY.intValue() + TEXT_OFFSET_Y) * scale, 0x000000);
									lineY.add(CUSTOM_LINE_HEIGHT);
								}
							});
						}
					}
					
					isPresent = false;
				}
			}
			
		}
		RenderSystem.popMatrix();
		
		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}

	public boolean isValidBlock(int lineIterate)
	{
		return !blockList.isEmpty() && blockList.size() > lineIterate && blockList.get(lineIterate) != null;
	}
	
	public boolean stillValidLine(int lineY)
	{
		return lineY < LINES_PER_PAGE * CUSTOM_LINE_HEIGHT;
	}
}
