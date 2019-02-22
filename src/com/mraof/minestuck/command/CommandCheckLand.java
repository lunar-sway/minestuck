package com.mraof.minestuck.command;

import java.util.ArrayList;
import java.util.List;

import com.mojang.brigadier.CommandDispatcher;
import com.mraof.minestuck.world.MinestuckDimensionHandler;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandCheckLand extends CommandBase
{
	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
	
	}
	
	@Override
	public String getName()
	{
		return "checkLand";
	}
	
	@Override
	public List getAliases()
	{
		ArrayList<String> list = new ArrayList<String>();
		list.add("land");
		list.add("getLand");
		list.add("checkLandAspects");
		list.add("getLandAspects");
		return list;
	}
	
	@Override
	public String getUsage(ICommandSender sender)
	{
		return (sender instanceof EntityPlayerMP) ? "commands.checkLand.usage" : "commands.playerOnly";
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if(!(sender instanceof EntityPlayerMP))
			throw new PlayerNotFoundException("commands.playerOnly");
		EntityPlayerMP player = (EntityPlayerMP) sender;
		
		if(MinestuckDimensionHandler.isLandDimension(player.dimension))
		{
			LandAspectRegistry.AspectCombination aspects = MinestuckDimensionHandler.getAspects(player.dimension);
			ChunkProviderLands chunkProvider = (ChunkProviderLands) player.world.provider.createChunkGenerator();
			ITextComponent aspect1 = new TextComponentTranslation("land."+aspects.aspectTerrain.getNames()[chunkProvider.nameIndex1]);
			ITextComponent aspect2 = new TextComponentTranslation("land."+aspects.aspectTitle.getNames()[chunkProvider.nameIndex2]);
			ITextComponent toSend;
			if(chunkProvider.nameOrder)
				toSend = new TextComponentTranslation("land.message.check", aspect1, aspect2);
			else toSend = new TextComponentTranslation("land.message.check", aspect2, aspect1);
			player.sendMessage(toSend);
		}
		else
		{
			player.sendMessage(new TextComponentTranslation("land.message.checkFail"));
		}
	}
	
	@Override
	public int getRequiredPermissionLevel()
	{
		return 0;
	}
	
	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender)
	{
		return true;
	}
	
}
