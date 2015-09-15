package com.mraof.minestuck.nei;

import static codechicken.lib.gui.GuiDraw.changeTexture;

import java.awt.Rectangle;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;

import com.mraof.minestuck.util.AlchemyRecipeHandler;
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
		else if (outputId.equals("allAlc"))
		{
			for (Map.Entry<List<Object>, GristSet> entry : GristRegistry.getAllConversions().entrySet())
			{
				List<Object> itemData = entry.getKey();
				Object item = itemData.get(0);
				int meta = (Integer)itemData.get(1);
				if(!AlchemyRecipeHandler.getItems(item, meta).isEmpty())
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
		changeTexture("minestuck:textures/items/CruxiteCarved.png");
		drawTexturedModalRect(22, 9, 0, 0, 16, 16,16,16);
		
		//Render grist requirements
		ItemStack result = arecipes.get(recipe).getResult().item;
		GristSet set = GristRegistry.getGristConversion(result);
		
		if (set == null) 
		{
			GuiDraw.drawString(StatCollector.translateToLocal("gui.notAlchemizable"), 4,34, 16711680);
			return;
		}
		
		Hashtable<Integer, Integer> reqs = set.getHashtable();
		if (reqs.size() == 0) {
			GuiDraw.drawString(StatCollector.translateToLocal("gui.free"), 4,34, 65280);
			return;
		}
		Iterator<Entry<Integer, Integer>> it = reqs.entrySet().iterator();
		int place = 0;
		while (it.hasNext())
		{
			Map.Entry<Integer, Integer> pairs = it.next();
			int type = (Integer) pairs.getKey();
			int need = (Integer) pairs.getValue();
			int have = MinestuckPlayerData.getClientGrist().getGrist(GristType.values()[type]);
			
			int row = place % 3;
			int col = place / 3;
			
			int color = need <= have ? 65280 : 16711680; //Green if we have enough grist, red if not
			
			GuiDraw.drawString(need + " " + GristType.values()[type].getDisplayName() + " (" + have + ")", 4 + (80 * col),34 + (8 * (row)), color);
			
			place++;
			
		}
	}
	
	
	public void drawTexturedModalRect(int par1, int par2, int par3, int par4, int par5, int par6,int w, int h) {
			float f = (float)1/w;
			float f1 = (float)1/h;
			WorldRenderer render = Tessellator.getInstance().getWorldRenderer();
			render.startDrawingQuads();
			render.addVertexWithUV((double)(par1 + 0), (double)(par2 + par6), 0.0F, (double)((float)(par3 + 0) * f), (double)((float)(par4 + par6) * f1));
			render.addVertexWithUV((double)(par1 + par5), (double)(par2 + par6), 0.0F, (double)((float)(par3 + par5) * f), (double)((float)(par4 + par6) * f1));
			render.addVertexWithUV((double)(par1 + par5), (double)(par2 + 0), 0.0F, (double)((float)(par3 + par5) * f), (double)((float)(par4 + 0) * f1));
			render.addVertexWithUV((double)(par1 + 0), (double)(par2 + 0), 0.0F, (double)((float)(par3 + 0) * f), (double)((float)(par4 + 0) * f1));
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
