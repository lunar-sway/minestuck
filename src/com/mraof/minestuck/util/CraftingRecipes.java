package com.mraof.minestuck.util;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;


/**
 * Contains classes for custom recipe types, and for smelting, oredict and similar registering
 */
public class CraftingRecipes
{
	private static int nextAvailableId;
	//public static final RegistryNamespaced<ResourceLocation, IRecipe> REGISTRY = net.minecraftforge.registries.GameData.getWrapper(IRecipe.class);
	
	public static void registerSmelting()
	{
		/*if(MinestuckConfig.cruxtruderIntake)
			GameRegistry.addSmelting(cruxiteDowel, new ItemStack(MinestuckItems.rawCruxite), 0.0F);
		
		TileEntityUraniumCooker.setRadiation(Items.BEEF, new ItemStack(irradiatedSteak));
		TileEntityUraniumCooker.setRadiation(beefSword, new ItemStack(irradiatedSteakSword));
		TileEntityUraniumCooker.setRadiation(Items.STICK, new ItemStack(upStick));
		TileEntityUraniumCooker.setRadiation(Items.MUSHROOM_STEW, new ItemStack(Items.SLIME_BALL));
		Item ectoSlime = Item.REGISTRY.getObject(new ResourceLocation("minestuckarsenal", "blue_ecto_slime"));
		if(ectoSlime != null)
		{
			TileEntityUraniumCooker.setRadiation(ectoSlime, new ItemStack(Items.SLIME_BALL));
		}
		*/
		
	}

    
  //Forge: Made private use GameData/Registry events!
    private static void register(String name, IRecipe recipe)
    {
    	
        register(new ResourceLocation(name), recipe);
    }

    //Forge: Made private use GameData/Registry events!
    private static void register(ResourceLocation name, IRecipe recipe)
    {
       /* if (REGISTRY.containsKey(name))
        {
            throw new IllegalStateException("Duplicate recipe ignored with ID " + name);
        }
        else
        {
            REGISTRY.register(nextAvailableId++, name, recipe);
        }*/
    }


	/*public static class AddEncodeRecipe extends NonMirroredRecipe
	{
		
		public AddEncodeRecipe(String group, int width, int height, NonNullList<Ingredient> ingredients, ItemStack result)
		{
			super(group, width, height, ingredients, result);
		}
		
		public ItemStack getCraftingResult(final InventoryCrafting crafting) 
		{
			ItemStack decode = ItemStack.EMPTY;
			final ItemStack output = super.getCraftingResult(crafting);
			ItemStack stack = output;
			
			for(int i = 0; i < crafting.getSizeInventory(); i++)
			{
				stack = crafting.getStackInSlot(i);
				if(stack.getItem() == MinestuckItems.captchaCard && stack.hasTagCompound() && stack.getTagCompound().hasKey("contentID") && stack.getTagCompound().getBoolean("punched"))
				{
					decode = AlchemyRecipes.getDecodedItem(stack);
					return AlchemyRecipes.createEncodedItem(decode, output);
				}
					
			}
			
			return ItemStack.EMPTY;
			
		}
		
		public static class Factory extends ShapedFactory
		{
			@Override
			public IRecipe initRecipe(String group, int width, int height, NonNullList<Ingredient> ingredients, ItemStack result)
			{
				return new AddEncodeRecipe(group, width, height, ingredients, result);
			}
		}
	}
	
	public static class RemoveCardRecipe extends NonMirroredRecipe
	{
		
		public RemoveCardRecipe(String group, int width, int height, NonNullList<Ingredient> ingredients, ItemStack result)
		{
			super(group, width, height, ingredients, result);
		}
		
		
		public ItemStack getCraftingResult(final InventoryCrafting crafting) 
		{
			ItemStack decode = ItemStack.EMPTY;
			final ItemStack output = super.getCraftingResult(crafting);
			ItemStack stack = output;
			
			for(int i = 0; i < crafting.getSizeInventory(); i++)
			{
				stack = crafting.getStackInSlot(i);
				if(stack.getItem() == MinestuckItems.shunt && stack.hasTagCompound() && stack.getTagCompound().hasKey("contentID"))
				{
					decode = AlchemyRecipes.getDecodedItem(stack);
					return AlchemyRecipes.createCard(decode, true);
				}
					
			}
			
			return ItemStack.EMPTY;
		}
		@Override
		public NonNullList<ItemStack> getRemainingItems(InventoryCrafting crafting) {
			final NonNullList<ItemStack> remainingItems = NonNullList.withSize(crafting.getSizeInventory(), ItemStack.EMPTY);
			
			for(int i = 0; i < remainingItems.size(); ++i)
			{
				final ItemStack stack = crafting.getStackInSlot(i);
				
				if(AlchemyRecipes.hasDecodedItem(stack))
					remainingItems.set(i, new ItemStack(stack.getItem()));
				else
					remainingItems.set(i, ForgeHooks.getContainerItem(stack));
				
			}
			
			return remainingItems;
		}
		
		public static class Factory extends ShapedFactory
		{
			@Override
			public IRecipe initRecipe(String group, int width, int height, NonNullList<Ingredient> ingredients, ItemStack result)
			{
				return new RemoveCardRecipe(group, width, height, ingredients, result);
			}
		}
	}
	
	public static abstract class ShapedFactory implements IRecipeFactory
	{
		@Override
		public IRecipe parse(JsonContext context, JsonObject json)
		{
			String group = JsonUtils.getString(json, "group", "");
			//Copy-pasted from the ShapedRecipes parser
			Map<Character, Ingredient> ingMap = Maps.newHashMap();
			for (Map.Entry<String, JsonElement> entry : JsonUtils.getJsonObject(json, "key").entrySet())
			{
				if (entry.getKey().length() != 1)
					throw new JsonSyntaxException("Invalid key entry: '" + entry.getKey() + "' is an invalid symbol (must be 1 character only).");
				if (" ".equals(entry.getKey()))
					throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
				
				ingMap.put(entry.getKey().toCharArray()[0], CraftingHelper.getIngredient(entry.getValue(), context));
			}
			ingMap.put(' ', Ingredient.EMPTY);
			
			JsonArray patternJ = JsonUtils.getJsonArray(json, "pattern");
			
			if (patternJ.size() == 0)
				throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
			if (patternJ.size() > 3)
				throw new JsonSyntaxException("Invalid pattern: too many rows, 3 is maximum");
			
			String[] pattern = new String[patternJ.size()];
			for (int x = 0; x < pattern.length; ++x)
			{
				String line = JsonUtils.getString(patternJ.get(x), "pattern[" + x + "]");
				if (line.length() > 3)
					throw new JsonSyntaxException("Invalid pattern: too many columns, 3 is maximum");
				if (x > 0 && pattern[0].length() != line.length())
					throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
				pattern[x] = line;
			}
			
			NonNullList<Ingredient> input = NonNullList.withSize(pattern[0].length() * pattern.length, Ingredient.EMPTY);
			Set<Character> keys = Sets.newHashSet(ingMap.keySet());
			keys.remove(' ');
			
			int x = 0;
			for (String line : pattern)
			{
				for (char chr : line.toCharArray())
				{
					Ingredient ing = ingMap.get(chr);
					if (ing == null)
						throw new JsonSyntaxException("Pattern references symbol '" + chr + "' but it's not defined in the key");
					input.set(x++, ing);
					keys.remove(chr);
				}
			}
			
			if (!keys.isEmpty())
				throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + keys);
			
			ItemStack result = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "result"), context);
			return initRecipe(group, pattern[0].length(), pattern.length, input, result);
		}
		
		public abstract IRecipe initRecipe(String group, int width, int height, NonNullList<Ingredient> ingredients, ItemStack result);
	}*/
}
