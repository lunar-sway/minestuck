package codechicken.nei.recipe;

import java.awt.Rectangle;
import java.util.List;

import codechicken.nei.PositionedStack;
import net.minecraft.item.ItemStack;

public class TemplateRecipeHandler {
public List<CachedRecipe> arecipes;
public List transferRects;
	public String getRecipeName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getGuiTexture() {
		// TODO Auto-generated method stub
		return null;
	}

	public void loadCraftingRecipes(String outputId, Object... results) {
		// TODO Auto-generated method stub
		
	}

	public void loadCraftingRecipes(ItemStack result) {
		// TODO Auto-generated method stub
		
	}

	public void drawExtras(int recipe) {
		// TODO Auto-generated method stub
		
	}

	public List<String> handleTooltip(GuiRecipe gui, List<String> currenttip,
			int recipe) {
		// TODO Auto-generated method stub
		return null;
	}

	public void drawProgressBar(int x, int y, int tx, int ty, int w, int h,
			float completion, int direction) {
		// TODO Auto-generated method stub
		
	}

	public void loadTransferRects() {
		// TODO Auto-generated method stub
		
	}
public class RecipeTransferRect
{

	public RecipeTransferRect(Rectangle rectangle, String string) {
		// TODO Auto-generated constructor stub
	}
	
}
public class CachedRecipe{

	public PositionedStack getResult() {
		// TODO Auto-generated method stub
		return null;
	}
	protected int cycleticks;
	public List<PositionedStack> getIngredients() {
		// TODO Auto-generated method stub
		return null;
	}public List<PositionedStack> getCycledIngredients(int a, List b)
	{
		return null;
	}public void randomRenderPermutation(PositionedStack stack, int a)
	{
		
	}
}
public void loadUsageRecipes(String inputId, Object... ingredients) {
	// TODO Auto-generated method stub
	
}}
