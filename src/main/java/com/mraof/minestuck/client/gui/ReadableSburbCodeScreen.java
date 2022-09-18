package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.level.block.Block;
import org.apache.commons.lang3.mutable.MutableInt;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Code is modified components of, or inspired by, WinGameScreen/ReadBookScreen/StoneTabletScreen.
 * It reads the nucleotide sequence stored in rana_temporaria_sec22b.txt and associates a number of lines of the text to each hieroglyph from the block tag with one extra pseudo-hieroglyph in the form of the paradox code
 */
public class ReadableSburbCodeScreen extends Screen
{
	public static final ResourceLocation BOOK_TEXTURES = new ResourceLocation(Minestuck.MOD_ID, "textures/gui/sburb_book.png");
	public final int validHieroglyphCount;
	public boolean[] hieroglyphValidityArray; //same size as MAX_HIEROGLYPH_COUNT
	public List<List<String>> listOfPages = new ArrayList<>(); //each element of the outermost list is a different page, and each page is a collection of lines
	
	private PageButton forwardButton;
	private PageButton backButton;
	
	public List<String> textList = null;
	public int linesPerBlock = 27; //With 298 lines of text the default is 27 per block
	public int totalPages = 8;
	public int currentPage = 0;
	
	//GUI sizes
	public static final int GUI_WIDTH = 192;
	public static final int GUI_HEIGHT = 192;
	public static final int TEXT_WIDTH = 288; //114
	public static final int TEXT_OFFSET_X = 36, TEXT_OFFSET_Y = 32;
	
	public static final int CUSTOM_LINE_HEIGHT = 3;
	public static final int LINES_PER_PAGE = 40; //how many lines can be fit neatly on a page, at 48 character per line
	
	public static final List<Block> FULL_HIEROGLYPH_LIST = MSTags.getBlocksFromTag(MSTags.Blocks.GREEN_HIEROGLYPHS);
	public static final int MAX_HIEROGLYPH_COUNT = FULL_HIEROGLYPH_LIST.size() + 1;
	
	public final static String EMPTY_SPACE = "                                                "; //48 characters
	
	public ReadableSburbCodeScreen(List<Block> recordedBlockList, boolean paradoxCode)
	{
		super(NarratorChatListener.NO_TITLE);
		this.hieroglyphValidityArray = checkForValidity(recordedBlockList, paradoxCode);
		
		int numberOfValidBits = 0;
		for(boolean isValidHieroglyph : hieroglyphValidityArray)
		{
			if(isValidHieroglyph)
				numberOfValidBits++;
		}
		this.validHieroglyphCount = numberOfValidBits;
	}
	
	@Override
	protected void init()
	{
		super.init();
		this.createMenuControls();
		this.createPageControlButtons();
		
		try
		{
			Resource resource = this.minecraft.getResourceManager().getResource(new ResourceLocation(Minestuck.MOD_ID, "texts/rana_temporaria_sec22b.txt")); //The text is broken into lines 48 characters long to neatly fit within a block of text that avoids empty spaces
			InputStream inputStream = resource.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
			textList = bufferedReader.lines().collect(Collectors.toList());
			
			linesPerBlock = textList.size() / MAX_HIEROGLYPH_COUNT;
			totalPages = Math.round((float) (textList.size() + 1) / (float) LINES_PER_PAGE);
		} catch(Exception ignored)
		{
		}
		
		if(textList != null)
		{
			makeUnrecordedSectionsBlank();
			sortIntoPages();
		}
	}
	
	/**
	 * Iterates through textList in sections connected to individual hieroglyphs, if that hieroglyph has not been recorded the text is replaced with empty space
	 */
	private void makeUnrecordedSectionsBlank()
	{
		for(int sectionIterate = 0; sectionIterate < hieroglyphValidityArray.length; sectionIterate++)
		{
			int fromSectionIndex = sectionIterate * linesPerBlock;
			int toSectionIndex = Math.min((sectionIterate * linesPerBlock + linesPerBlock - 1), textList.size());
			
			if(!hieroglyphValidityArray[sectionIterate]) //if there was no valid hieroglyph for that element
			{
				if(sectionIterate == hieroglyphValidityArray.length - 1)
					toSectionIndex = textList.size() - 1; //add any overflow to the last section
				
				for(int iterate = fromSectionIndex; iterate < toSectionIndex + 1; iterate++)
				{
					textList.set(iterate, EMPTY_SPACE);
				}
			}
		}
	}
	
	/**
	 * Groups the textList lines into pages after textList has been processed by makeUnrecordedSectionsBlank()
	 */
	private void sortIntoPages()
	{
		for(int pageIterate = 0; pageIterate < totalPages + 1; pageIterate++)
		{
			int fromIndex = Math.min(LINES_PER_PAGE * pageIterate, textList.size());
			int toIndex = Math.min(LINES_PER_PAGE * pageIterate + LINES_PER_PAGE, textList.size());
			
			listOfPages.add(textList.subList(fromIndex, toIndex));
		}
	}
	
	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(poseStack);
		this.setFocused(null);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, BOOK_TEXTURES);
		
		int topX = (this.width - GUI_WIDTH) / 2;
		int topY = 2;
		this.blit(poseStack, topX, topY, 0, 0, GUI_WIDTH, GUI_HEIGHT);
		{
			if(textList != null)
			{
				//pushPose function, followed by the scale function and ending with the popPose function, allow the scale changes to only be applied to the text
				poseStack.pushPose();
				float subtractScale = 0.4F;
				float scale = (1 / subtractScale);
				poseStack.scale(subtractScale, subtractScale, subtractScale);
				
				MutableInt lineY = new MutableInt();
				
				//takes the necessary page from the group of pages and then reads out each of the 40 lines stored for that page(with lines being made blank if the associated hieroglyph component isnt recorded)
				for(String text : listOfPages.get(currentPage))
				{
					font.getSplitter().splitLines(text, TEXT_WIDTH, Style.EMPTY, true, (style, start, end) -> {
						//limiting the length of the page via this if statement
						if(stillValidLine(lineY.intValue()))
						{
							Component line = new TextComponent(text.substring(start, end)).setStyle(style);
							font.draw(poseStack, line, ((this.width - GUI_WIDTH) / 2F + TEXT_OFFSET_X) * scale, (lineY.intValue() + TEXT_OFFSET_Y) * scale, 0x00A300); //hex is green
							lineY.add(CUSTOM_LINE_HEIGHT);
						}
					});
				}
			}
		}
		
		poseStack.popPose();
		
		super.render(poseStack, mouseX, mouseY, partialTicks);
	}
	
	/**
	 * Creates an array of booleans with each element representing a hieroglyph, elements are assigned true if the paradox code(first element) is present or a valid block contained in FULL_HIEROGLYPH_LIST is present
	 */
	private boolean[] checkForValidity(List<Block> recordedBlockList, boolean paradoxCode)
	{
		boolean[] booleans = new boolean[MAX_HIEROGLYPH_COUNT];
		
		for(int iterate = 0; iterate < MAX_HIEROGLYPH_COUNT; iterate++)
		{
			if(iterate == 0) //first iteration, checks for paradox code
				booleans[iterate] = paradoxCode;
			else if(!recordedBlockList.isEmpty() && iterate < FULL_HIEROGLYPH_LIST.size() + 1) //all but first iteration, checks for blocks in FULL_HIEROGLYPH_LIST, +1 to offset paradox code
				booleans[iterate] = recordedBlockList.contains(FULL_HIEROGLYPH_LIST.get(iterate - 1));
			else //unrecorded blocks
				booleans[iterate] = false;
		}
		
		return booleans;
	}
	
	private boolean stillValidLine(int lineY)
	{
		return lineY < LINES_PER_PAGE * CUSTOM_LINE_HEIGHT;
	}
	
	protected void createMenuControls()
	{
		if(this.minecraft != null)
		{
			this.addRenderableWidget(new Button(this.width / 2 - 100, 196, 200, 20, new TranslatableComponent("gui.done"), (p_214161_1_) -> {
				this.minecraft.setScreen(null);
			}));
		}
	}
	
	protected void createPageControlButtons()
	{
		int i = (this.width - 192) / 2;
		this.forwardButton = this.addRenderableWidget(new PageButton(i + 116, 159, true, (p_214159_1_) -> {
			this.pageForward();
		}, true));
		this.backButton = this.addRenderableWidget(new PageButton(i + 43, 159, false, (p_214158_1_) -> {
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
}
