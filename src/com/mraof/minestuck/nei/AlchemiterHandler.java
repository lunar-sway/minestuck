/*package com.mraof.minestuck.nei;

import static codechicken.lib.gui.GuiDraw.changeTexture;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;
import java.util.Map;

import net.minecraft.client.gui.Gui;
import net.minecraft.item.ItemStack;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.api.stack.PositionedStack;
import codechicken.nei.guihook.GuiContainerManager;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;

import com.mraof.minestuck.client.util.GuiUtil;
import com.mraof.minestuck.client.util.GuiUtil.GristboardMode;
import com.mraof.minestuck.alchemy.AlchemyRecipeHandler;
import com.mraof.minestuck.alchemy.GristRegistry;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.GristType;

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
	public String getRecipeName()
	{
		return "Alchemiter";
	}

	@Override
	public String getGuiTexture()
	{
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
		Gui.drawModalRectWithCustomSizedTexture(22, 9, 0, 0, 16, 16,16,16);
		
		//Render grist requirements
		ItemStack result = arecipes.get(recipe).getResult().item;
		GristSet set = GristRegistry.getGristConversion(result);
		
		GuiUtil.drawGristBoard(set, GristboardMode.ALCHEMITER, 4, 34, GuiDraw.fontRenderer);
		
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
				
				if(set != null && !set.isEmpty())
				{
					List<String> tooltip = GuiUtil.getGristboardTooltip(set, posX, posY, 4, 34, GuiDraw.fontRenderer);
					if(tooltip != null)
						currenttip.addAll(tooltip);
				}
			}
		}
		
		return currenttip;
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
				Gui.drawModalRectWithCustomSizedTexture(x, y, tx, ty, var, h,w,h);
			break;
			case 1://down
				Gui.drawModalRectWithCustomSizedTexture(x, y, tx, ty, w, var,w,h);
			break;
			case 2://left
				Gui.drawModalRectWithCustomSizedTexture(x+w-var, y, tx+w-var, ty, var, h,w,h);
			break;
			case 3://up
				Gui.drawModalRectWithCustomSizedTexture(x, y+h-var, tx, ty+h-var, w, var,w,h);
			break;		
		}
	}
	
	@Override
	public void loadTransferRects()
	{
		transferRects.add(new RecipeTransferRect(new Rectangle(49, 12, 71, 10),"allAlc"));
	}

}
*/