package com.mraof.minestuck.command;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.command.CommandSource;

public class CommandSburbServer
{
	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
	
	}
	
	/*@Override
	public String getName()
	{
		return "sburbServer";
	}
	
	@Override
	public String getUsage(ICommandSender sender)
	{
		return "commands.sburbServer.usage";
	}
	
	@Override
	public int getRequiredPermissionLevel()
	{
		return 2;
	}
	
	@Override
	public void execute(MinecraftServer mcServer, ICommandSender sender, String[] args) throws CommandException
	{
		if(args.length != 2)
			throw new WrongUsageException("commands.sburbServer.usage");
		
		PlayerIdentifier client = IdentifierHandler.getForCommand(mcServer, sender, args[0]);
		PlayerIdentifier server = IdentifierHandler.getForCommand(mcServer, sender, args[1]);
		
		SessionHandler.connectByCommand(sender, this, client, server);
	}
	
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
	{
		return args.length == 1 || args.length == 2 ? IdentifierHandler.getCommandAutocomplete(server, args)
				: Collections.<String>emptyList();
	}*/
}