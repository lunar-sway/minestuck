package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.Minestuck;
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
	
	//GUI sizes
	public static final int GUI_WIDTH = 192;
	public static final int GUI_HEIGHT = 192;
	public static final int TEXT_WIDTH = 114;
	public static final int TEXT_OFFSET_X = 36, TEXT_OFFSET_Y = 32;
	
	public final static String EMPTY_SPACE = "                                                                            "; //76 characters
	//public final static String EMPTY_SPACE = "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO0000000"; //77 spaces
	
	public ReadableSburbCodeScreen(List<Block> blockList)
	{
		super(NarratorChatListener.NO_TITLE);
		//this.bookAccess = p_214155_1_;
		this.blockList = blockList;
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
			List<String> textList = null;
			try
			{
				IResource iResource = this.minecraft.getResourceManager().getResource(new ResourceLocation(Minestuck.MOD_ID, "texts/rana_temporaria_map1s.txt")); //The text is broken into lines 76 characters long to neatly fit within a block of text that avoids empty spaces
				InputStream inputStream = iResource.getInputStream();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
				textList = bufferedReader.lines().collect(Collectors.toList());
			} catch(Exception ignored)
			{
			}
			
			//TODO add one last line of code after the rest to simulate the missing piece of code that came with the lotus flower drop
			if(textList != null)
			{
				List<Block> fullBlockList = MSTags.Blocks.GREEN_HIEROGLYPHS.getValues();
				MutableInt lineY = new MutableInt();
				boolean isPresent = false;
				
				for(int lineIterate = 0; lineIterate < fullBlockList.size(); lineIterate++)
				{
					for(Block blockListIterate : blockList)
					{
						if(fullBlockList.get(lineIterate) == blockListIterate)
						{
							isPresent = true;
							break;
						}
						
					}
					
					if(isPresent) //TODO stop line splitting unless it reaches the edge of a line, provide the whole gene(instead of chromosome) and divy it up into fractions of content as opposed to new lines dependent on the spacing in the text file itself
					{
						String text = textList.get(lineIterate);
						
						font.getSplitter().splitLines(text, TEXT_WIDTH, Style.EMPTY, true, (style, start, end) -> {
							ITextComponent line = new StringTextComponent(text.substring(start, end)).setStyle(style);
							font.draw(matrixStack, line, (this.width - GUI_WIDTH) / 2F + TEXT_OFFSET_X, lineY.intValue() + TEXT_OFFSET_Y, 0x000000);
							lineY.add(font.lineHeight);
						});
						
					} else
					{
						font.getSplitter().splitLines(EMPTY_SPACE, TEXT_WIDTH, Style.EMPTY, true, (style, start, end) -> {
							ITextComponent line = new StringTextComponent(EMPTY_SPACE.substring(start, end)).setStyle(style);
							font.draw(matrixStack, line, (this.width - GUI_WIDTH) / 2F + TEXT_OFFSET_X, lineY.intValue() + TEXT_OFFSET_Y, 0x000000);
							lineY.add(font.lineHeight);
						});
					}
					
					isPresent = false;
				}
			}
			
		}
		
		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}
	
	/*
	
	@Override
	protected void init()
	{
		this.createMenuControls();
		this.createPageControlButtons();
	}
	
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(matrixStack);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bind(BOOK_LOCATION);
		int i = (this.width - 192) / 2;
		int j = 2;
		this.blit(matrixStack, i, 2, 0, 0, 192, 192);
		if(this.cachedPage != this.currentPage)
		{
			ITextProperties itextproperties = this.bookAccess.getPage(this.currentPage);
			this.cachedPageComponents = this.font.split(itextproperties, 114);
			this.pageMsg = new TranslationTextComponent("book.pageIndicator", this.currentPage + 1, Math.max(this.getNumPages(), 1));
		}
		
		this.cachedPage = this.currentPage;
		int i1 = this.font.width(this.pageMsg);
		this.font.draw(matrixStack, this.pageMsg, (float) (i - i1 + 192 - 44), 18.0F, 0);
		int k = Math.min(128 / 9, this.cachedPageComponents.size());
		
		for(int l = 0; l < k; ++l)
		{
			IReorderingProcessor ireorderingprocessor = this.cachedPageComponents.get(l);
			this.font.draw(matrixStack, ireorderingprocessor, (float) (i + 36), (float) (32 + l * 9), 0);
		}
		
		Style style = this.getClickedComponentStyleAt((double) mouseX, (double) mouseY);
		if(style != null)
		{
			this.renderComponentHoverEffect(matrixStack, style, mouseX, mouseY);
		}
		
		super.render(matrixStack, mouseX, mouseY, pPartialTicks);
	}
	
	public ITextProperties getCodeOnPage(int givenPage)
	{
		iresource = this.minecraft.getResourceManager().getResource(new ResourceLocation("texts/end.txt"));
		
		ITextProperties itextproperties = ITextComponent.Serializer.fromJson(s);
		
		if(itextproperties != null)
		{
			return itextproperties;
		}
		return null;
	}
	
	public boolean setPage(int pPageNum)
	{
		int i = MathHelper.clamp(pPageNum, 0, this.bookAccess.getPageCount() - 1);
		if(i != this.currentPage)
		{
			this.currentPage = i;
			this.updateButtonVisibility();
			this.cachedPage = -1;
			return true;
		} else
		{
			return false;
		}
	}
	
	
	protected boolean forcePage(int pPageNum)
	{
		return this.setPage(pPageNum);
	}
	
	protected void createMenuControls()
	{
		this.addButton(new Button(this.width / 2 - 100, 196, 200, 20, DialogTexts.GUI_DONE, (p_214161_1_) -> {
			this.minecraft.setScreen((Screen) null);
		}));
	}
	
	protected void createPageControlButtons()
	{
		int i = (this.width - 192) / 2;
		int j = 2;
		this.forwardButton = this.addButton(new ChangePageButton(i + 116, 159, true, (p_214159_1_) -> {
			this.pageForward();
		}, true));
		this.backButton = this.addButton(new ChangePageButton(i + 43, 159, false, (p_214158_1_) -> {
			this.pageBack();
		}, true));
		this.updateButtonVisibility();
	}
	
	private int getNumPages()
	{
		return this.bookAccess.getPageCount();
	}
	
	/**
	 * Moves the display back one page
	 *//*
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
	 *//*
	protected void pageForward()
	{
		if(this.currentPage < this.getNumPages() - 1)
		{
			++this.currentPage;
		}
		
		this.updateButtonVisibility();
	}
	
	private void updateButtonVisibility()
	{
		this.forwardButton.visible = this.currentPage < this.getNumPages() - 1;
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
	
	@OnlyIn(Dist.CLIENT)
	public interface IBookInfo
	{
		/**
		 * Returns the size of the book
		 *//*
		int getPageCount();
		
		ITextProperties getPageRaw(int p_230456_1_);
		
		default ITextProperties getPage(int p_238806_1_)
		{
			return p_238806_1_ >= 0 && p_238806_1_ < this.getPageCount() ? this.getPageRaw(p_238806_1_) : ITextProperties.EMPTY;
		}
		
		static ReadableSburbCodeScreen.IBookInfo fromItem(ItemStack p_216917_0_)
		{
			//Item item = p_216917_0_.getItem();
			return new ReadableSburbCodeScreen.WrittenBookInfo(p_216917_0_);
		}
	}
	
	@OnlyIn(Dist.CLIENT)
	public static class WrittenBookInfo implements ReadableSburbCodeScreen.IBookInfo
	{
		private final List<String> pages;
		
		public WrittenBookInfo(ItemStack p_i50616_1_)
		{
			this.pages = readPages(p_i50616_1_);
		}
		
		private static List<String> readPages(ItemStack p_216921_0_)
		{
			CompoundNBT compoundnbt = p_216921_0_.getTag();
			return (List<String>) (compoundnbt != null && WrittenBookItem.makeSureTagIsValid(compoundnbt) ? ReadBookScreen.convertPages(compoundnbt) : ImmutableList.of(ITextComponent.Serializer.toJson((new TranslationTextComponent("book.invalid.tag")).withStyle(TextFormatting.DARK_RED))));
		}
		
		/**
		 * Returns the size of the book
		 *//*
		public int getPageCount()
		{
			return this.pages.size();
		}
		
		public ITextProperties getPageRaw(int p_230456_1_)
		{
			String s = this.pages.get(p_230456_1_);
			
			try
			{
				ITextProperties itextproperties = ITextComponent.Serializer.fromJson(s);
				if(itextproperties != null)
				{
					return itextproperties;
				}
			} catch(Exception exception)
			{
			}
			
			return ITextProperties.of(s);
		}
	}*/
}
