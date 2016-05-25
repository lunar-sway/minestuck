package com.mraof.minestuck.nei;

import static codechicken.lib.gui.GuiDraw.changeTexture;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;
import java.util.Map;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.guihook.GuiContainerManager;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.client.gui.GuiHandler;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.util.GristAmount;
import com.mraof.minestuck.util.GristRegistry;
import com.mraof.minestuck.util.GristSet;
import com.mraof.minestuck.util.MinestuckPlayerData;
import com.mraof.minestuck.util.GristType;

public class AlchemiterHandler extends TemplateRecipeHandler
{

	class CachedAlchemiterRecipe extends CachedRecipe
	{

		private PositionedStack result;
		
		public CachedAlchemiterRecipe(ItemStack input)
		{
			result = new PositionedStack(input, 130, 9);
		}
		
		public CachedAlchemiterRecipe(Object item, int damage)
		{
			result = new PositionedStack(AlchemyRecipeHandler.getItems(item, damage), 130, 9);
		}
		
		@Override
		public PositionedStack getResult()
		{
			randomRenderPermutation(result, cycleticks / 20);
			return result;
		}
		
		
	}

	@Override
	public String getRecipeName() {
		return "Alchemiter";
	}

	@Override
	public String getGuiTexture() {
		return "minestuck:textures/gui/alchemiter.png";
	}
	
	@Override
	public void loadCraftingRecipes(String outputId, Object... results)
	{
		if(outputId.equals("item") && results[0] instanceof ItemStack)
			loadCraftingRecipes((ItemStack)results[0]);
		else if(outputId.equals("allAlc"))
			for(Map.Entry<List<Object>, GristSet> entry : GristRegistry.getAllConversions().entrySet())
			{
				List<Object> itemData = entry.getKey();
				Object item = itemData.get(0);
				int meta = (Integer)itemData.get(1);
				if(!AlchemyRecipeHandler.getItems(item, meta).isEmpty())
					arecipes.add(new CachedAlchemiterRecipe(item, meta));
			}
		else if(outputId.startsWith("grist:"))
		{
			String gristName = outputId.substring(6);
			GristType gristType = GristType.getTypeFromString(gristName);
			if(gristType != null)
				for(Map.Entry<List<Object>, GristSet> entry : GristRegistry.getAllConversions().entrySet())
				{
					List<Object> itemData = entry.getKey();
					Object item = itemData.get(0);
					int meta = (Integer)itemData.get(1);
					if(!AlchemyRecipeHandler.getItems(item, meta).isEmpty() && entry.getValue().getGrist(gristType) != 0)
						arecipes.add(new CachedAlchemiterRecipe(item, meta));
				}
		}
	}
	
	@Override
	public void loadCraftingRecipes(ItemStack result)
	{
		result = result.copy();
		result.stackSize = 1;
		if(result.getItem().isDamageable())
			result.setItemDamage(0);
		result.setTagCompound(null);
		if (GristRegistry.getGristConversion(result) != null)
		{
			arecipes.add(new CachedAlchemiterRecipe(result));
		}
	}
	
	@Override
	public void drawExtras(int recipe)
	{
		
		//render progress bar
		changeTexture("minestuck:textures/gui/progress/alchemiter.png");
		drawProgressBar(49, 12, 0, 0, 71, 10, 50, 0);
		
		//render carved dowel
		changeTexture("minestuck:textures/items/cruxite_carved.png");
		drawTexturedModalRect(22, 9, 0, 0, 16, 16,16,16);
		
		//Render grist requirements
		ItemStack result = arecipes.get(recipe).getResult().item;
		GristSet set = GristRegistry.getGristConversion(result);
		
		drawGristBoard(set);
		
	}
	
	@Override
	public List<String> handleTooltip(GuiRecipe gui, List<String> currenttip, int recipe)
	{
		currenttip = super.handleTooltip(gui, currenttip, recipe);
		
		if (GuiContainerManager.shouldShowTooltip(gui) && currenttip.size() == 0)
		{
			Point offset = gui.getRecipePosition(recipe);
			Point mouse = GuiDraw.getMousePosition();
			int posX = (int) mouse.x - offset.x - (gui.width - 176)/2, posY = mouse.y - offset.y - (gui.height - 166)/2;
			if(posX >= 4 && posY >= 34 && posX < 162 && posY < 58)
			{
				ItemStack result = arecipes.get(recipe).getResult().item;
				GristSet set = GristRegistry.getGristConversion(result);
				GristSet playerGrist = MinestuckPlayerData.getClientGrist();
				
				if(set != null && !set.isEmpty())
					if(!MinestuckConfig.alchemyIcons)
					{
						int place = 0;
						for(GristAmount entry : set.getArray())
						{
							int row = place % 3;
							int col = place / 3;
							
							if(posY >= 34 + 8*row && posY < 42 + 8*row)
							{
								int need = entry.getAmount();
								String needStr = GuiHandler.addSuffix(need);
								
								if(!needStr.equals(String.valueOf(need)) && posX >= 4 + 79*col && posX < 4 + 79*col + GuiDraw.getStringWidth(needStr))
								{
									currenttip.add(String.valueOf(need));
									break;
								}
								
								int width = GuiDraw.getStringWidth(needStr + " " + entry.getType().getDisplayName() + " (");
								int have = playerGrist.getGrist(entry.getType());
								String haveStr = GuiHandler.addSuffix(have);
								
								if(!haveStr.equals(String.valueOf(have)) && posX >= 4 + 79*col + width && posX < 4 + 79*col + width + GuiDraw.getStringWidth(haveStr))
								{
									currenttip.add(String.valueOf(have));
									break;
								}
							}
							
							place++;
						}
					} else
					{
						int index = 0;
						for(GristAmount entry : set.getArray())
						{
							GristType type = entry.getType();
							int need = entry.getAmount();
							int have = playerGrist.getGrist(type);
							int row = index/158;
							
							String needStr = GuiHandler.addSuffix(need), haveStr = GuiHandler.addSuffix(have);
							int needStrWidth = GuiDraw.getStringWidth(needStr);
							int haveStrWidth = GuiDraw.getStringWidth('('+haveStr+')');
							
							if(index + needStrWidth + 10 + haveStrWidth > (row + 1)*158)
							{
								row++;
								index = row*158;
							}
							
							if(posY >= 34 + 8*row && posY < 42 + 8*row)
							{
								if(!needStr.equals(String.valueOf(need)) && posX >= 4 + index%158 && posX < 4 + index%158 + needStrWidth)
									currenttip.add(String.valueOf(need));
								else if(posX >= needStrWidth + 5 + index%158 && posX < needStrWidth + 13 + index%158)
									currenttip.add(type.getDisplayName());
								else if(!haveStr.equals(String.valueOf(have)) && posX >= needStrWidth + 14 + index%158 + GuiDraw.getStringWidth("(") && posX < needStrWidth + 14 + index%158 + GuiDraw.getStringWidth("("+haveStr))
									currenttip.add(String.valueOf(have));
								
								if(!currenttip.isEmpty())
									break;
							}
							
							index += needStrWidth + 10 + haveStrWidth;
							index = Math.min(index + 6, (row + 1)*158);
						}
					}
			}
		}
		
		return currenttip;
	}
	
	private void drawGristBoard(GristSet cost)
	{
		if (cost == null)
		{
			GuiDraw.drawString(I18n.translateToLocal("gui.notAlchemizable"), 4,34, 16711680);
			return;
		}
		GristSet playerGrist = MinestuckPlayerData.getClientGrist();
		
		if (cost.isEmpty())
		{
			GuiDraw.drawString(I18n.translateToLocal("gui.free"), 4,34, 65280);
			return;
		}
		
		if(!MinestuckConfig.alchemyIcons)
		{
			int place = 0;
			for(GristAmount entry : cost.getArray())
			{
				GristType type = entry.getType();
				int need = entry.getAmount();
				int have = playerGrist.getGrist(type);
				
				int row = place % 3;
				int col = place / 3;
				
				int color = need <= have ? 0x00FF00 : 0xFF0000; //Green if we have enough grist, red if not
				
				String needStr = GuiHandler.addSuffix(need), haveStr = GuiHandler.addSuffix(have);
				GuiDraw.drawString(needStr + " " + type.getDisplayName() + " (" + haveStr + ")", 4 + 79*col, 34 + 8*row, color);
				
				place++;
			}
		} else
		{
			int index = 0;
			for(GristAmount entry : cost.getArray())
			{
				GristType type = entry.getType();
				int need = entry.getAmount();
				int have = playerGrist.getGrist(type);
				int row = index/158;
				int color = need <= have ? 0x00FF00 : 0xFF0000;
				
				String needStr = GuiHandler.addSuffix(need), haveStr = GuiHandler.addSuffix(have);
				haveStr = '('+haveStr+')';
				int needStrWidth = GuiDraw.getStringWidth(needStr);
				
				if(index + needStrWidth + 10 + GuiDraw.getStringWidth(haveStr) > (row + 1)*158)
				{
					row++;
					index = row*158;
				}
				
				GuiDraw.drawString(needStr, 4 + index%158, 34 + 8*row, color);
				GuiDraw.drawString(haveStr, needStrWidth + 14 + index%158, 34 + (8 * (row)), color);
				
				GlStateManager.color(1, 1, 1);
				changeTexture("minestuck:textures/grist/" + type.getName()+ ".png");
				drawTexturedModalRect(needStrWidth + 5 + index%158, 34 + 8*row, 0, 0, 8, 8, 8, 8);
				
				index += needStrWidth + 10 + GuiDraw.getStringWidth(haveStr);
				index = Math.min(index + 6, (row + 1)*158);
			}
		}
	}
	
	public void drawTexturedModalRect(int par1, int par2, int par3, int par4, int par5, int par6,int w, int h)
	{	//TODO put all instances of this method in the same place
			float f = (float)1/w;
			float f1 = (float)1/h;
			VertexBuffer render = Tessellator.getInstance().getBuffer();
			render.begin(7, DefaultVertexFormats.POSITION_TEX);
			render.pos(par1, par2 + par6, 0D).tex((par3)*f, (par4 + par6)*f1).endVertex();
			render.pos(par1 + par5, par2 + par6, 0D).tex((par3 + par5)*f, (par4 + par6)*f1).endVertex();
			render.pos(par1 + par5, par2, 0D).tex((par3 + par5)*f, (par4)*f1).endVertex();
			render.pos(par1, par2, 0D).tex((par3)*f, (par4)*f1).endVertex();
			Tessellator.getInstance().draw();
	}
	
	@Override
	public void drawProgressBar(int x, int y, int tx, int ty, int w, int h, float completion, int direction)
	{
		if(direction > 3)
		{
			completion = 1-completion;
			direction %= 4;
		}
		int var = (int) (completion * (direction % 2 == 0 ? w : h));
		
		switch(direction)
		{
			case 0://right
				this.drawTexturedModalRect(x, y, tx, ty, var, h,w,h);
			break;
			case 1://down
				this.drawTexturedModalRect(x, y, tx, ty, w, var,w,h);
			break;
			case 2://left
				this.drawTexturedModalRect(x+w-var, y, tx+w-var, ty, var, h,w,h);
			break;
			case 3://up
				this.drawTexturedModalRect(x, y+h-var, tx, ty+h-var, w, var,w,h);
			break;		
		}
	}
	
	@Override
	public void loadTransferRects()
	{
		transferRects.add(new RecipeTransferRect(new Rectangle(49, 12, 71, 10),"allAlc"));
	}

}
