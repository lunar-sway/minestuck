package com.mraof.minestuck.util;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.alchemy.AlchemyRecipes;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.tileentity.TileEntityUraniumCooker;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Map;
import java.util.Set;

import static com.mraof.minestuck.block.MinestuckBlocks.*;
import static com.mraof.minestuck.item.MinestuckItems.*;



/**
 * Contains classes for custom recipe types, and for smelting, oredict and similar registering
 */
public class CraftingRecipes
{
	private static int nextAvailableId;
	public static final RegistryNamespaced<ResourceLocation, IRecipe> REGISTRY = net.minecraftforge.registries.GameData.getWrapper(IRecipe.class);
	
	public static void registerSmelting()
	{
		GameRegistry.addSmelting(goldSeeds, new ItemStack(Items.GOLD_NUGGET), 0.1F);
		GameRegistry.addSmelting(ironOreEndStone, new ItemStack(Items.IRON_INGOT), 0.7F);
		GameRegistry.addSmelting(ironOreSandstone, new ItemStack(Items.IRON_INGOT), 0.7F);
		GameRegistry.addSmelting(ironOreSandstoneRed, new ItemStack(Items.IRON_INGOT), 0.7F);
		GameRegistry.addSmelting(goldOreSandstone, new ItemStack(Items.GOLD_INGOT), 1.0F);
		GameRegistry.addSmelting(goldOreSandstoneRed, new ItemStack(Items.GOLD_INGOT), 1.0F);
		GameRegistry.addSmelting(redstoneOreEndStone, new ItemStack(Items.REDSTONE), 0.7F);
		GameRegistry.addSmelting(woodenCactus, new ItemStack(Items.COAL, 1, 1), 0.15F);
		GameRegistry.addSmelting(beefSword, new ItemStack(steakSword), 0.5F);
		if(MinestuckConfig.cruxtruderIntake)
			GameRegistry.addSmelting(cruxiteDowel, new ItemStack(MinestuckItems.rawCruxite), 0.0F);
		
		GameRegistry.addSmelting(log, new ItemStack(Items.COAL, 1, 1), 0.15F);
		GameRegistry.addSmelting(endLog, new ItemStack(Items.COAL, 1, 1), 0.15F);
		
		TileEntityUraniumCooker.setRadiation(Items.BEEF, new ItemStack(irradiatedSteak));
		TileEntityUraniumCooker.setRadiation(beefSword, new ItemStack(irradiatedSteakSword));
		TileEntityUraniumCooker.setRadiation(Items.STICK, new ItemStack(upStick));
		TileEntityUraniumCooker.setRadiation(Items.MUSHROOM_STEW, new ItemStack(Items.SLIME_BALL));
		Item ectoSlime = Item.REGISTRY.getObject(new ResourceLocation("minestuckarsenal", "blue_ecto_slime"));
		if(ectoSlime != null)
		{
			TileEntityUraniumCooker.setRadiation(ectoSlime, new ItemStack(Items.SLIME_BALL));
		}
		
		
	}

    
  //Forge: Made private use GameData/Registry events!
    private static void register(String name, IRecipe recipe)
    {
    	
        register(new ResourceLocation(name), recipe);
    }

    //Forge: Made private use GameData/Registry events!
    private static void register(ResourceLocation name, IRecipe recipe)
    {
        if (REGISTRY.containsKey(name))
        {
            throw new IllegalStateException("Duplicate recipe ignored with ID " + name);
        }
        else
        {
            REGISTRY.register(nextAvailableId++, name, recipe);
        }
    }

    
    
	public static void addOredictionary()
	{
		//Register ore dictionary entries
		OreDictionary.registerOre("oreCoal", coalOreNetherrack);
		OreDictionary.registerOre("oreCoal", coalOrePinkStone);
		OreDictionary.registerOre("oreIron", ironOreEndStone);
		OreDictionary.registerOre("oreIron", ironOreSandstone);
		OreDictionary.registerOre("oreIron", ironOreSandstoneRed);
		OreDictionary.registerOre("oreGold", goldOreSandstone);
		OreDictionary.registerOre("oreGold", goldOreSandstoneRed);
		OreDictionary.registerOre("oreGold", goldOrePinkStone);
		OreDictionary.registerOre("oreRedstone", redstoneOreEndStone);
		OreDictionary.registerOre("oreLapis", lapisOrePinkStone);
		OreDictionary.registerOre("oreDiamond", diamondOrePinkStone);
		OreDictionary.registerOre("oreCruxite", oreCruxite);
		OreDictionary.registerOre("oreUranium", oreUranium);
		
		OreDictionary.registerOre("dirt", new ItemStack(coloredDirt, 1, OreDictionary.WILDCARD_VALUE));
		
		OreDictionary.registerOre("stone", pinkStoneSmooth);
		OreDictionary.registerOre("chalk", chalk);
		OreDictionary.registerOre("stoneChalk", chalk);
		OreDictionary.registerOre("blockChalk", chalk);
		
		OreDictionary.registerOre("plankWood", glowingPlanks);
		OreDictionary.registerOre("plankWood", endPlanks);
		OreDictionary.registerOre("plankWood", deadPlanks);
		OreDictionary.registerOre("plankWood", treatedPlanks);
		OreDictionary.registerOre("plankWood",	new ItemStack(planks, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("logWood", glowingLog);
		OreDictionary.registerOre("logWood", endLog);
		OreDictionary.registerOre("logWood", deadLog);
		OreDictionary.registerOre("logWood",	new ItemStack(log, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("treeSapling",endSapling);
		OreDictionary.registerOre("treeSapling",rainbowSapling);
		OreDictionary.registerOre("treeLeaves",	endLeaves);
		OreDictionary.registerOre("treeLeaves",	new ItemStack(leaves1, 1, OreDictionary.WILDCARD_VALUE));
		
		OreDictionary.registerOre("cropStrawberry", strawberry);
		OreDictionary.registerOre("blockCactus", bloomingCactus);
	}
	
	/**
	 * Regular recipes can typically be made independent on if the ingredients are put to the left or the right, as long as the shape remains.
	 * With this class, that possible mirror is not possible, and the recipe will instead only follow the patten exactly.
	 */
	public static class NonMirroredRecipe extends ShapedRecipes
	{
		
		public NonMirroredRecipe(String group, int width, int height, NonNullList<Ingredient> input, ItemStack result)
		{
			super(group, width, height, input, result);
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
					Ingredient ingredient = Ingredient.EMPTY;
					
					if (posX >= 0 && posY >= 0 && posX < this.recipeWidth && posY < this.recipeHeight)
					{
						ingredient = this.recipeItems.get(posX + posY * this.recipeWidth);
					}
					
					if (!ingredient.apply(inv.getStackInRowAndColumn(invX, invY)))
					{
						return false;
					}
				}
			}
			
			return true;
		}
		
		public static class Factory extends ShapedFactory
		{
			@Override
			public IRecipe initRecipe(String group, int width, int height, NonNullList<Ingredient> ingredients, ItemStack result)
			{
				return new NonMirroredRecipe(group, width, height, ingredients, result);
			}
		}
	}
	
	/**
	 * Any recipes made out of this instance will not accept captchalogue cards as ingredients, unless said cards are empty and blank.
	 * Beware that this class extends NoMirroredRecipe.
	 */
	public static class EmptyCardRecipe extends NonMirroredRecipe
	{
		
		public EmptyCardRecipe(String group, int width, int height, NonNullList<Ingredient> ingredients, ItemStack result)
		{
			super(group, width, height, ingredients, result);
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
		
		public static class Factory extends ShapedFactory
		{
			@Override
			public IRecipe initRecipe(String group, int width, int height, NonNullList<Ingredient> ingredients, ItemStack result)
			{
				return new EmptyCardRecipe(group, width, height, ingredients, result);
			}
		}
	}
	
	public static class AddEncodeRecipe extends NonMirroredRecipe
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
	}
}
