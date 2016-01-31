package com.mraof.minestuck.command;

import com.mraof.minestuck.tileentity.TileEntityTransportalizer;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.Location;
import com.mraof.minestuck.util.Teleport;

import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

public class CommandTransportalizer extends CommandBase
{
	
	@Override
	public String getCommandName()
	{
		return "tpz";
	}
	
	@Override
	public String getCommandUsage(ICommandSender sender)
	{
		return "commands.tpz.usage";
	}
	
	@Override
	public int getRequiredPermissionLevel()
	{
		return 2;	//Same as /tp
	}
	
	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException
	{
		if(args.length < 1 || args.length > 2)
			throw new WrongUsageException(this.getCommandUsage(sender));
		
		String code;
		EntityPlayerMP player;	//TODO make it possible to apply this command to more than just players
		if(args.length == 1)
		{
			code = args[0];
			player = getCommandSenderAsPlayer(sender);
		} else
		{
			code = args[1];
			player = getPlayer(sender, args[0]);
		}
		code = code.toUpperCase();
		
		if(player == null)
			throw new PlayerNotFoundException();
		
		Location location = TileEntityTransportalizer.transportalizers.get(code);
		if(location == null || !DimensionManager.isDimensionRegistered(location.dim))
			throw new CommandException("commands.tpz.notFound", code);
		
		WorldServer world = MinecraftServer.getServer().worldServerForDimension(location.dim);
		
		TileEntity te = world.getTileEntity(location.pos);
		if(te == null || !(te instanceof TileEntityTransportalizer))
		{
			Debug.print("Invalid transportalizer in map: " + code + " at " + location);
			TileEntityTransportalizer.transportalizers.remove(code);
			throw new CommandException("commands.tpz.notFound", code);
		}
		
		IBlockState block0 = world.getBlockState(location.pos.up());
		IBlockState block1 = world.getBlockState(location.pos.up(2));
		if(block0.getBlock().getMaterial().blocksMovement() || block1.getBlock().getMaterial().blocksMovement())
			throw new CommandException("message.transportalizer.destinationBlocked");
		
		player.timeUntilPortal = 60;
		
		if(location.dim != player.dimension)
			Teleport.teleportEntity(player, location.dim, (TileEntityTransportalizer) te, false);
		else
			TileEntityTransportalizer.teleportTo(player, location);
		
		notifyOperators(sender, this, "commands.tpz.success", player.getCommandSenderName(), code);
	}
}