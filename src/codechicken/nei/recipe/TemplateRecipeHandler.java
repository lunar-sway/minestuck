package codechicken.nei.recipe;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.item.ItemStack;
import codechicken.nei.PositionedStack;

public class TemplateRecipeHandler {
	
    public ArrayList<CachedRecipe> arecipes = new ArrayList<CachedRecipe>();
    public LinkedList<RecipeTransferRect> transferRects = new LinkedList<RecipeTransferRect>();

	
	public abstract class CachedRecipe
	{

		public PositionedStack getResult() {
			return null;
		}

		public List<PositionedStack> getIngredients() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

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

	public void loadUsageRecipes(String inputId, Object... ingredients) {
		// TODO Auto-generated method stub
		
	}

	public void drawExtras(int recipe) {
		// TODO Auto-generated method stub
		
	}

	public void drawProgressBar(int x, int y, int tx, int ty, int w, int h,
			float completion, int direction) {
		// TODO Auto-generated method stub
		
	}

	public void loadTransferRects() {
		// TODO Auto-generated method stub
		
	}
	
	public static class RecipeTransferRect
    {
        public RecipeTransferRect(Rectangle rectangle, String outputId, Object... results)
        {
        }
    }
}
