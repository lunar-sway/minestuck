package com.mraof.minestuck.nei;

import static codechicken.lib.gui.GuiDraw.changeTexture;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.util.CombinationRegistry;

public class DesignixHandler extends TemplateRecipeHandler {

	class CachedDesignixRecipe extends CachedRecipe {

		private ArrayList<PositionedStack> stacks;
		private boolean mode;
		private ItemStack output;
		
		public CachedDesignixRecipe(Object input1, int damage1, Object input2, int damage2, boolean mode, ItemStack output)
		{
			this.mode = mode;
			this.output = output;
			stacks = new ArrayList<PositionedStack>();
			stacks.add(new PositionedStack(AlchemyRecipeHandler.getItems(input1, damage1), 57, 15, true));
			stacks.add(new PositionedStack(AlchemyRecipeHandler.getItems(input2, damage2), 57, 39, true));
		}
		
		@Override
		public PositionedStack getResult() {
			return new PositionedStack(output,129,26);
		}
		
		@Override
		public List<PositionedStack> getIngredients()
		{
			return getCycledIngredients(cycleticks / 20, stacks);
		}
		
	}

	@Override
	public String getRecipeName() {
		return "Punch Designix";
	}

	@Override
	public String getGuiTexture() {
		return "minestuck:textures/gui/designix.png";
	}
	
	@Override
	public void loadCraftingRecipes(String outputId, Object... results)
	{
		if(outputId.equals("item") && results[0] instanceof ItemStack)
			loadCraftingRecipes((ItemStack)results[0]);
		else if (outputId.equals("allDesignix"))
		{
			for (Map.Entry<List<Object>, ItemStack> entry : CombinationRegistry.getAllConversions().entrySet())
			{
				List<Object> itemData = (List<Object>)entry.getKey();
				Object id1 = itemData.get(0);
				int meta1 = (Integer)itemData.get(1);
				Object id2 = itemData.get(2);
				int meta2 = (Integer)itemData.get(3);
				boolean mode = (Boolean)itemData.get(4);
				arecipes.add(new CachedDesignixRecipe(id1, meta1, id2, meta2, mode, (ItemStack)entry.getValue()));
			}
		}
	}
	
	@Override
	public void loadCraftingRecipes(ItemStack result){
		for (Map.Entry<List<Object>, ItemStack> entry : CombinationRegistry.getAllConversions().entrySet()) {
			List<Object> itemData = entry.getKey();
			Object id1 = itemData.get(0);
			int meta1 = (Integer)itemData.get(1);
			Object id2 = itemData.get(2);
			int meta2 = (Integer)itemData.get(3);
			boolean mode = (Boolean)itemData.get(4);
			if (result.getItem() == (entry.getValue()).getItem() && result.getItemDamage() == (entry.getValue()).getItemDamage())
			{
				arecipes.add(new CachedDesignixRecipe(id1, meta1 , id2, meta2, mode, (ItemStack)entry.getValue()));
			}
		}
	}
	
	@Override
	public void loadUsageRecipes(String inputId, Object... ingredients)
	{
		if (ingredients.length == 0 || !(ingredients[0] instanceof ItemStack))
		{return;}
		
		ItemStack search = (ItemStack)ingredients[0];
		if(search.getItem() == Minestuck.captchaCard && search.hasTagCompound() && search.getTagCompound().hasKey("contentID"))
			search = AlchemyRecipeHandler.getDecodedItem(search, false);
		
		recipeLoop: for (Map.Entry<List<Object>, ItemStack> entry : CombinationRegistry.getAllConversions().entrySet())
		{
			List<Object> itemData = entry.getKey();
			Object id1 = itemData.get(0);
			int meta1 = (Integer)itemData.get(1);
			Object id2 = itemData.get(2);
			int meta2 = (Integer)itemData.get(3);
			boolean mode = (Boolean)itemData.get(4);
			int[] ids = OreDictionary.getOreIDs(search);
			if(id1 instanceof Item && search.getItem() == id1 && (meta1 == OreDictionary.WILDCARD_VALUE || meta1 == search.getItemDamage()))
			{
				arecipes.add(new CachedDesignixRecipe(search.getItem(), search.getItemDamage(), id2, meta2, mode, entry.getValue()));
				continue recipeLoop;
			}
			else if(id1 instanceof String && (meta1 == OreDictionary.WILDCARD_VALUE || meta1 == search.getItemDamage()))
			{
				int id = OreDictionary.getOreID((String)id1);
				for(int i : ids)
					if(i == id)
					{
						arecipes.add(new CachedDesignixRecipe(search.getItem(), search.getItemDamage(), id2, meta2, mode, entry.getValue()));
						continue recipeLoop;
					}
			}
			
			if(id2 instanceof Item && search.getItem() == id2 && (meta2 == OreDictionary.WILDCARD_VALUE || meta2 == search.getItemDamage()))
				arecipes.add(new CachedDesignixRecipe(id1, meta1, search.getItem(), search.getItemDamage(), mode, entry.getValue()));
			else if(id2 instanceof String && (meta2 == OreDictionary.WILDCARD_VALUE || meta2 == search.getItemDamage()))
			{
				int id = OreDictionary.getOreID((String)id2);
				for(int i : ids)
					if(i == id)
					{
						arecipes.add(new CachedDesignixRecipe(id1, meta1, search.getItem(), search.getItemDamage(), mode, entry.getValue()));
						break;
					}
			}
		}
	}
	
	@Override
	public void drawExtras(int recipe)
	{
		CachedDesignixRecipe crecipe = (CachedDesignixRecipe) arecipes.get(recipe);
		
		//render progress bar
		changeTexture("minestuck:textures/gui/progress/designix.png");
		drawProgressBar(77, 27, 0, 0, 42, 17, 50, 0);
		
		//render blank card
		changeTexture("minestuck:textures/items/CardBlank.png");
		drawTexturedModalRect(21, 39, 0, 0, 16, 16,16,16);

		//render combo mode
		GuiDraw.drawString(crecipe.mode ? "&&" : "||", 22,18, 0x8B8B8B);
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
		transferRects.add(new RecipeTransferRect(new Rectangle(77, 27, 42, 17),"allDesignix"));
	}

}
