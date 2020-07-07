package com.mraof.minestuck.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;

public class CommandTransportalizer
{
	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
	
	}
	
	/*@Override
	public String getName()
	{
		return "tpz";
	}
	
	@Override
	public String getUsage(ICommandSender sender)
	{
		return "commands.tpz.usage";
	}
	
	@Override
	public int getRequiredPermissionLevel()
	{
		return 2;	//Same as /tp
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if(args.length < 1 || args.length > 2)
			throw new WrongUsageException(this.getUsage(sender));
		
		String code;
		EntityPlayerMP player;	//TODO make it possible to apply this command to more than just players
		if(args.length == 1)
		{
			code = args[0];
			player = getCommandSenderAsPlayer(sender);
		} else
		{
			code = args[1];
			player = getPlayer(server, sender, args[0]);
		}
		code = code.toUpperCase();
		
		if(player == null)
			throw new PlayerNotFoundException("commands.generic.player.unspecified");
		
		Location location = TileEntityTransportalizer.transportalizers.get(code);
		if(location == null || !DimensionManager.isDimensionRegistered(location.dim))
			throw new CommandException("commands.tpz.notFound", code);
		
		WorldServer world = server.getWorld(location.dim);
		
		TileEntity te = world.getTileEntity(location.pos);
		if(te == null || !(te instanceof TileEntityTransportalizer))
		{
			Debug.warn("Invalid transportalizer in map: " + code + " at " + location);
			TileEntityTransportalizer.transportalizers.remove(code);
			throw new CommandException("commands.tpz.notFound", code);
		}
		
		IBlockState block0 = world.getBlockState(location.pos.up());
		IBlockState block1 = world.getBlockState(location.pos.up(2));
		if(block0.getMaterial().blocksMovement() || block1.getMaterial().blocksMovement())
			throw new CommandException("message.transportalizer.destinationBlocked");
		
		boolean success = Teleport.teleportEntity(player, location.dim, null, te.getPos().getX() + 0.5, te.getPos().getY() + 0.6, te.getPos().getZ() + 0.5);
		
		if(success)
		{
			player.timeUntilPortal = 60;
			
			notifyCommandListener(sender, this, "commands.tpz.success", player.getName(), code);
		} else if(sender.sendCommandFeedback())
			throw new CommandException("commands.tpz.failed");
	}
	
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
	{
		return args.length == 1 ? IdentifierHandler.getCommandAutocomplete(server, args) : Collections.<String>emptyList();
	}*/
}