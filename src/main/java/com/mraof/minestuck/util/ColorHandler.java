package com.mraof.minestuck.util;

import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.skaianet.SburbHandler;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores the array with colors that the player picks from, and provides utility function to handle colors.
 */
public class ColorHandler
{
	public static final int DEFAULT_COLOR = 0xA0DCFF;
	private static final List<Pair<Integer, String>> colors;
	
	static
	{
		colors = new ArrayList<>();
		
		colors.add(Pair.of(0x0715cd, "blue"));
		colors.add(Pair.of(0xb536da, "orchid"));
		colors.add(Pair.of(0xe00707, "red"));
		colors.add(Pair.of(0x4ac925, "green"));
		
		colors.add(Pair.of(0x00d5f2, "cyan"));
		colors.add(Pair.of(0xff6ff2, "pink"));
		colors.add(Pair.of(0xf2a400, "orange"));
		colors.add(Pair.of(0x1f9400, "emerald"));
		
		colors.add(Pair.of(0xa10000, "rust"));
		colors.add(Pair.of(0xa15000, "bronze"));
		colors.add(Pair.of(0xa1a100, "gold"));
		colors.add(Pair.of(0x626262, "iron"));
		colors.add(Pair.of(0x416600, "olive"));
		colors.add(Pair.of(0x008141, "jade"));
		colors.add(Pair.of(0x008282, "teal"));
		colors.add(Pair.of(0x005682, "cobalt"));
		colors.add(Pair.of(0x000056, "indigo"));
		colors.add(Pair.of(0x2b0057, "purple"));
		colors.add(Pair.of(0x6a006a, "violet"));
		colors.add(Pair.of(0x77003c, "fuchsia"));
	}
	
	public static int getColor(int index)
	{
		if(index < 0 || index >= colors.size())
			return DEFAULT_COLOR;
		return colors.get(index).getLeft();
	}
	
	public static ITextComponent getName(int index)
	{
		if(index < 0 || index >= colors.size())
			return new StringTextComponent("INVALID");
		return new TranslationTextComponent("minestuck.color." + colors.get(index).getRight());
	}
	
	public static int getColorSize()
	{
		return colors.size();
	}
	
	public static ItemStack setDefaultColor(ItemStack stack)
	{
		return setColor(stack, DEFAULT_COLOR);
	}
	
	public static ItemStack setColor(ItemStack stack, int color)
	{
		stack.getOrCreateTag().putInt("color", color);
		return stack;
	}
	
	public static int getColorFromStack(ItemStack stack)
	{
		if(stack.hasTag() && stack.getTag().contains("color", Constants.NBT.TAG_ANY_NUMERIC))
			return stack.getTag().getInt("color");
		else return DEFAULT_COLOR;
	}
	
	public static int getColorForDimension(ServerWorld world)
	{
		SburbConnection c = SburbHandler.getConnectionForDimension(world);
		return c == null ? ColorHandler.DEFAULT_COLOR : PlayerSavedData.getData(c.getClientIdentifier(), world).getColor();
	}
	
}