package com.mraof.minestuck.command;

import com.mraof.minestuck.entity.consort.EntityConsort;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec3d;

public class CommandConsortReply extends CommandBase
{
	
	@Override
	public String getName()
	{
		return "consortReply";
	}
	
	@Override
	public String getUsage(ICommandSender sender)
	{
		return "";
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		EntityPlayerMP player = getCommandSenderAsPlayer(sender);
		if(args.length == 2)
		{
			int id = parseInt(args[0]);
			Entity entity = player.world.getEntityByID(id);
			if(entity != null && entity instanceof EntityConsort && new Vec3d(player.posX, player.posY, player.posZ)
					.squareDistanceTo(entity.posX, entity.posY, entity.posZ) < 100)
			{
				EntityConsort consort = (EntityConsort) entity;
				consort.commandReply(player, args[1]);
			}
		}
	}
	
	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender)
	{
		return true;
	}
}