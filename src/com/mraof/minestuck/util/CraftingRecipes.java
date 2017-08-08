package com.mraof.minestuck.util;

public class CraftingRecipes
{
	
	/*public static class NonMirroredRecipe extends ShapedRecipes
	{
		
		public NonMirroredRecipe(int width, int height, ItemStack[] input, ItemStack result)
		{
			super(width, height, input, result);
		}
		
		@Override
		public boolean matches(InventoryCrafting inv, World world)
		{
			for (int i = 0; i <= 3 - this.recipeWidth; ++i)
				for (int j = 0; j <= 3 - this.recipeHeight; ++j)
					if (this.checkMatch(inv, i, j))
						return true;
			
			return false;
		}
		
		protected boolean checkMatch(InventoryCrafting inv, int x, int y)
		{
			for (int invX = 0; invX < 3; invX++)
			{
				for (int invY = 0; invY < 3; invY++)
				{
					int posX = invX - x;
					int posY = invY - y;
					ItemStack itemstack = ItemStack.EMPTY;
					
					if (posX >= 0 && posY >= 0 && posX < this.recipeWidth && posY < this.recipeHeight)
					{
						itemstack = this.recipeItems[posX + posY * this.recipeWidth];
					}
					
					ItemStack itemstack1 = inv.getStackInRowAndColumn(invX, invY);
					
					if (!itemstack1.isEmpty() || !itemstack.isEmpty())
					{
						if (itemstack1.isEmpty() && !itemstack.isEmpty() || !itemstack1.isEmpty() && itemstack.isEmpty())
						{
							return false;
						}
						
						if (itemstack.getItem() != itemstack1.getItem())
						{
							return false;
						}
						
						if (itemstack.getItemDamage() != 32767 && itemstack.getItemDamage() != itemstack1.getItemDamage())
						{
							return false;
						}
					}
				}
			}
			
			return true;
		}
	}
	
	public static class EmptyCardRecipe extends NonMirroredRecipe
	{
		
		public EmptyCardRecipe(int width, int height, ItemStack[] ingredients, ItemStack result)
		{
			super(width, height, ingredients, result);
		}
		
		@Override
		public boolean matches(InventoryCrafting crafting, World world)
		{
			for(int i = 0; i < crafting.getSizeInventory(); i++)
			{
				ItemStack stack = crafting.getStackInSlot(i);
				if(stack.getItem() == MinestuckItems.captchaCard && stack.hasTagCompound() && stack.getTagCompound().hasKey("contentID"))
					return false;
			}
			return super.matches(crafting, world);
		}
	}*/
}
