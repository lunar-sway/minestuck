package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.block.Block;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.screen.Screen;
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
	//private ReadableSburbCodeScreen.IBookInfo bookAccess;
	
	private int currentPage;
	/**
	 * Holds a copy of the page text, split into page width lines
	 */
	private List<IReorderingProcessor> cachedPageComponents = Collections.emptyList();
	private int cachedPage = -1;
	private ITextComponent pageMsg = StringTextComponent.EMPTY;
	private ChangePageButton forwardButton;
	private ChangePageButton backButton;
	
	List<String> textList = null;
	
	//GUI sizes
	public static final int GUI_WIDTH = 192;
	public static final int GUI_HEIGHT = 192;
	public static final int TEXT_WIDTH = 288; //114
	public static final int TEXT_OFFSET_X = 36, TEXT_OFFSET_Y = 32;
	
	public static final int CUSTOM_LINE_HEIGHT = 3;
	
	//public final static String EMPTY_SPACE = "                                                                            "; //76 characters
	public final static String EMPTY_SPACE = "                                                            "; //60 characters
	//public final static String EMPTY_SPACE = "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO0000000"; //77 spaces
	
	public ReadableSburbCodeScreen(List<Block> blockList)
	{
		super(NarratorChatListener.NO_TITLE);
		//this.bookAccess = p_214155_1_;
		this.blockList = blockList;
	}
	
	@Override
	protected void init()
	{
		super.init();
		
		try
		{
			IResource iResource = this.minecraft.getResourceManager().getResource(new ResourceLocation(Minestuck.MOD_ID, "texts/rana_temporaria_sec22b.txt")); //The text is broken into lines 60 characters long to neatly fit within a block of text that avoids empty spaces
			//Debug.debugf("iResource = %s", iResource.toString());
			InputStream inputStream = iResource.getInputStream();
			//InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
			//inputStreamReader
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
			textList = bufferedReader.lines().collect(Collectors.toList());
		} catch(Exception ignored)
		{
		}
	}
	
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(matrixStack);
		this.setFocused(null);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bind(BOOK_TEXTURES);
		int topX = (this.width - GUI_WIDTH) / 2;
		int topY = 2;
		
		this.blit(matrixStack, topX, topY, 0, 0, GUI_WIDTH, GUI_HEIGHT);
		{
			//TODO add one last line of code after the rest to simulate the missing piece of code that came with the lotus flower drop
			if(textList != null)
			{
				RenderSystem.pushMatrix();
				float subtractScale = 0.4F; //was too wide with .
				float scale = (1 / subtractScale);
				RenderSystem.scaled(subtractScale, subtractScale, subtractScale);
				
				List<Block> fullBlockList = MSTags.Blocks.GREEN_HIEROGLYPHS.getValues();
				MutableInt lineY = new MutableInt();
				boolean isPresent = false;
				int subStringStart = 0;
				int numberOfLines = textList.size();
				
				//TODO remove its use here, bring it back to a section in which unrecorded code leaves a vacant slot
				for(int lineIterate = 0; lineY.getValue() < 28 * CUSTOM_LINE_HEIGHT || lineIterate < numberOfLines; lineIterate++)
				{
					String text = textList.get(lineIterate);
					font.getSplitter().splitLines(text, TEXT_WIDTH, Style.EMPTY, true, (style, start, end) -> {
						//limiting the length of the page via this if statement
						ITextComponent line = new StringTextComponent(text.substring(start, end)).setStyle(style);
						font.draw(matrixStack, line, ((this.width - GUI_WIDTH) / 2F + TEXT_OFFSET_X) * scale, (lineY.intValue() + TEXT_OFFSET_Y) * scale, 0x000000);
						lineY.add(CUSTOM_LINE_HEIGHT);
					});
				}
				
				/*for(int lineIterate = 0; lineIterate < fullBlockList.size(); lineIterate++)
				{
					for(Block blockListIterate : blockList)
					{
						if(fullBlockList.get(lineIterate) == blockListIterate)
						{
							isPresent = true;
							break;
						}
					}
					
					//textList.subList()
					
					String text = textList.get(lineIterate);
					/*ITextComponent line = new StringTextComponent(isPresent ? text : EMPTY_SPACE).setStyle(Style.EMPTY);
					
					font.getSplitter().splitLines(text, TEXT_WIDTH, Style.EMPTY, true, (style, start, end) -> {
						//limiting the length of the page via this if statement, 25 lines of text
						if(lineY.getValue() < 25 * CUSTOM_LINE_HEIGHT)
						{
							//ITextComponent line = new StringTextComponent(text.substring(start, end)).setStyle(style);
							font.draw(matrixStack, line, ((this.width - GUI_WIDTH) / 2F + TEXT_OFFSET_X) * scale, (lineY.intValue() + TEXT_OFFSET_Y) * scale, 0x000000);
							lineY.add(CUSTOM_LINE_HEIGHT);
						}
					});*/
					
					/*if(isPresent) //TODO stop line splitting unless it reaches the edge of a line, provide the whole gene(instead of chromosome) and divy it up into fractions of content as opposed to new lines dependent on the spacing in the text file itself
					{
						
						//int subStringStart = 0;
						/*for(int subStringStart = 0; subStringStart < text.length(); subStringStart += 39)
						{
							ITextComponent line = new StringTextComponent(text.substring(subStringStart, subStringStart + 38)).setStyle(Style.EMPTY);
							font.getSplitter().splitLines(text, TEXT_WIDTH, Style.EMPTY, true, (style, start, end) -> {
								
								font.draw(matrixStack, line, ((this.width - GUI_WIDTH) / 2F + TEXT_OFFSET_X) * (int) (1 / scale), (lineY.intValue() + TEXT_OFFSET_Y) * (int) (1 / scale), 0x000000);
								lineY.add(font.lineHeight);
							});
						}*/
						
						/*font.getSplitter().splitLines(text, TEXT_WIDTH, Style.EMPTY, true, (style, start, end) -> {
							//limiting the length of the page via this if statement
							if(lineY.getValue() < 25 * CUSTOM_LINE_HEIGHT)
							{
								ITextComponent line = new StringTextComponent(text.substring(start, end)).setStyle(style);
								font.draw(matrixStack, line, ((this.width - GUI_WIDTH) / 2F + TEXT_OFFSET_X) * scale, (lineY.intValue() + TEXT_OFFSET_Y) * scale, 0x000000);
								lineY.add(CUSTOM_LINE_HEIGHT);
							}
						});
					} else
					{
						font.getSplitter().splitLines(EMPTY_SPACE, TEXT_WIDTH, Style.EMPTY, true, (style, start, end) -> {
							if(lineY.getValue() < 25 * CUSTOM_LINE_HEIGHT)
							{
								ITextComponent line = new StringTextComponent(EMPTY_SPACE.substring(start, end)).setStyle(style);
								font.draw(matrixStack, line, ((this.width - GUI_WIDTH) / 2F + TEXT_OFFSET_X) * scale, (lineY.intValue() + TEXT_OFFSET_Y) * scale, 0x000000);
								lineY.add(CUSTOM_LINE_HEIGHT);
							}
						});
					}
					
					isPresent = false;
				}*/
			}
			
		}
		RenderSystem.popMatrix();
		
		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}
}
