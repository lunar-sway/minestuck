package com.mraof.minestuck.nei;

import static codechicken.core.gui.GuiDraw.changeTexture;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import codechicken.core.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;

import com.mraof.minestuck.util.CombinationRegistry;

public class DesignexHandler extends TemplateRecipeHandler {

	class CachedDesignexRecipe extends CachedRecipe {

		private ItemStack input1;
		private ItemStack input2;
		private boolean mode;
		private ItemStack output;
		public CachedDesignexRecipe(ItemStack input1,ItemStack input2,boolean mode,ItemStack output) {
			this.input1 = input1;
			this.input2 = input2;
			this.mode = mode;
			this.output = output;
		}
		
		@Override
		public PositionedStack getResult() {
			return new PositionedStack(output,129,26);
		}
		
		@Override
	       public List<PositionedStack> getIngredients()
	        {
				ArrayList<PositionedStack> stacks = new ArrayList<PositionedStack>();
				stacks.add(new PositionedStack(input1,57,15));
				stacks.add(new PositionedStack(input2,57,39));
				return stacks;
	        }
	}

	private GuiDraw fontRenderer = new GuiDraw();
		
	@Override
	public String getRecipeName() {
		return "Punch Designex";
	}

	@Override
	public String getGuiTexture() {
		return "minestuck:textures/gui/designex.png";
	}
	
	@Override
    public void loadCraftingRecipes(String outputId, Object... results)
    {
        if(outputId.equals("item"))
            loadCraftingRecipes((ItemStack)results[0]);
        else if (outputId.equals("allDesignex")) {
    		for (Object item : CombinationRegistry.getAllConversions().entrySet()) {
    			Map.Entry entry = (Map.Entry)item;
    			List<Object> itemData = (List<Object>)entry.getKey();
    			String id1 = (String)itemData.get(0);
    			int meta1 = (Integer)itemData.get(1);
    			String id2 = (String)itemData.get(2);
    			int meta2 = (Integer)itemData.get(3);
    			boolean mode = (Boolean)itemData.get(4);
    			arecipes.add(new CachedDesignexRecipe(new ItemStack((Item)Item.itemRegistry.getObject(id1),1,meta1),new ItemStack((Item)Item.itemRegistry.getObject(id2),1,meta2),mode,(ItemStack)entry.getValue()));
    		}
        }
        	
        	
    }
	
	@Override
	public void loadCraftingRecipes(ItemStack result){
		for (Object item : CombinationRegistry.getAllConversions().entrySet()) {
			Map.Entry entry = (Map.Entry)item;
			List itemData = (List)entry.getKey();
			String id1 = (String)itemData.get(0);
			int meta1 = (Integer)itemData.get(1);
			String id2 = (String)itemData.get(2);
			int meta2 = (Integer)itemData.get(3);
			boolean mode = (Boolean)itemData.get(4);
			if (result.getItem() == ((ItemStack)entry.getValue()).getItem() && result.getItemDamage() == ((ItemStack)entry.getValue()).getItemDamage()) {
				arecipes.add(new CachedDesignexRecipe(new ItemStack((Item)Item.itemRegistry.getObject(id1),1,meta1),new ItemStack((Item)Item.itemRegistry.getObject(id2),1,meta2),mode,(ItemStack)entry.getValue()));
			}
		}
	}
	
	@Override
	public void loadUsageRecipes(String inputId, Object... ingredients)
    {
		if (ingredients.length == 0) {return;}
		for (Object item : CombinationRegistry.getAllConversions().entrySet()) {
			Map.Entry entry = (Map.Entry)item;
			List itemData = (List)entry.getKey();
			String id1 = (String)itemData.get(0);
			int meta1 = (Integer)itemData.get(1);
			String id2 = (String)itemData.get(2);
			int meta2 = (Integer)itemData.get(3);
			boolean mode = (Boolean)itemData.get(4);
			ItemStack search1 = (ItemStack)ingredients[0];
			//ItemStack search2 = (ItemStack)ingredients[1];
			if ((search1.getItem() == Item.itemRegistry.getObject(id1) && search1.getItemDamage() == meta1) || (search1.getItem() == Item.itemRegistry.getObject(id2) && search1.getItemDamage() == meta2)) {
				arecipes.add(new CachedDesignexRecipe(new ItemStack((Item)Item.itemRegistry.getObject(id1), 1, meta1),new ItemStack((Item)Item.itemRegistry.getObject(id2),1,meta2),mode,(ItemStack)entry.getValue()));
			}
		}
    }
	
    @Override
    public void drawExtras(int recipe)
    {
    	CachedDesignexRecipe crecipe = (CachedDesignexRecipe) arecipes.get(recipe);
    	
    	//render progress bar
    	changeTexture("minestuck:textures/gui/progress/designex.png");
        drawProgressBar(77, 27, 0, 0, 42, 17, 50, 0);
    	
    	//render blank card
    	changeTexture("minestuck:textures/items/CardBlank.png");
    	drawTexturedModalRect(21, 39, 0, 0, 16, 16,16,16);

    	//render combo mode
    	fontRenderer.drawString(crecipe.mode ? "&&" : "||", 22,18, 0);
    }
    
    
    public void drawTexturedModalRect(int par1, int par2, int par3, int par4, int par5, int par6,int w, int h) {
            float f = (float)1/w;
            float f1 = (float)1/h;
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + par6), 0.0F, (double)((float)(par3 + 0) * f), (double)((float)(par4 + par6) * f1));
            tessellator.addVertexWithUV((double)(par1 + par5), (double)(par2 + par6), 0.0F, (double)((float)(par3 + par5) * f), (double)((float)(par4 + par6) * f1));
            tessellator.addVertexWithUV((double)(par1 + par5), (double)(par2 + 0), 0.0F, (double)((float)(par3 + par5) * f), (double)((float)(par4 + 0) * f1));
            tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + 0), 0.0F, (double)((float)(par3 + 0) * f), (double)((float)(par4 + 0) * f1));
            tessellator.draw();
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
    	transferRects.add(new RecipeTransferRect(new Rectangle(77, 27, 42, 17),"allDesignex"));
    }

}
