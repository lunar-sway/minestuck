package com.mraof.minestuck.util;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.event.AlchemyEvent;
import com.mraof.minestuck.item.AlchemizedColored;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.skaianet.SburbHandler;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores the array with colors that the player picks from, and provides utility function to handle colors.
 */
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
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
	
	@SubscribeEvent
	public static void onAlchemy(AlchemyEvent event)
	{
		ItemStack stack = event.getItemResult();
		if(stack.getItem() instanceof AlchemizedColored)
		{
			int color = getColorForPlayer(event.getPlayer(), event.getWorld());
			event.setItemResult(((AlchemizedColored) stack.getItem()).setColor(stack, color));
		}
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
		return c == null ? ColorHandler.DEFAULT_COLOR : getColorForPlayer(c.getClientIdentifier(), world);
	}
	
	public static int getColorForPlayer(ServerPlayerEntity player)
	{
		return getColorForPlayer(IdentifierHandler.encode(player), player.level);
	}
	
	public static int getColorForPlayer(PlayerIdentifier identifier, World world)
	{
		return PlayerSavedData.getData(identifier, world).getColor();
	}
}