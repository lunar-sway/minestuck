package com.mraof.minestuck.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.*;

public class CommandGristSend
{

	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
	
	}
	//TODO Grist set argument
	/*
	@Override
	public String getName()
	{
		return "gristSend";
	}
	
	@Override
	public List getAliases()
	{
		ArrayList<String> list = new ArrayList<String>();
		list.add("gristSend");
		list.add("gristGive");
		list.add("sendGrist");
		list.add("giveGrist");
		return list;
	}
	
	@Override
	public String getUsage(ICommandSender sender)
	{
		return (sender instanceof EntityPlayerMP) ? "commands.gristSend.usage" : "commands.playerOnly.redirectGrist";
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if(!(sender instanceof EntityPlayerMP))
			throw new PlayerNotFoundException("commands.playerOnly.redirectGrist");
		EntityPlayerMP player = (EntityPlayerMP) sender;
		
		if(args.length < 3 || args.length % 2 == 0)
			throw new WrongUsageException(this.getUsage(sender));
		
		String receiver = args[0];
		EntityPlayerMP receivingPlayer = server.getPlayerList().getPlayerByUsername(receiver);
		if(receivingPlayer != null)
		{
			if(!isPermittedFor(player, receivingPlayer))
				throw new CommandException("commands.gristSend.notPermitted", receivingPlayer.getName());
			
			GristSet set = new GristSet();
			GristAmount[] parsedAmounts = CommandGrist.parseGrist(args, 1);
			for(GristAmount amount : parsedAmounts)
				set.addGrist(amount);
			
			GristSet playerGrist = MinestuckPlayerData.getGristSet(player);
			GristSet receiverGrist = MinestuckPlayerData.getGristSet(receivingPlayer);
			
			StringBuilder costStr = new StringBuilder();
			boolean first = true;
			for(GristType type : GristType.values())
			{
				int i = Math.max(0, Math.min(playerGrist.getGrist(type), set.getGrist(type)));
				
				if(i > 0)
				{
					playerGrist.addGrist(type, -i);
					receiverGrist.addGrist(type, i);
					
					if(!first)
						costStr.append(", ");
					costStr.append(i + " " + type.getDisplayName());
					first = false;
				}
			}
			
			PlayerIdentifier senderIdentifier;
			PlayerIdentifier receiverIdentifier;
			
			senderIdentifier = IdentifierHandler.encode(player);
			receiverIdentifier = IdentifierHandler.encode(receivingPlayer);
			
			MinestuckPlayerTracker.updateGristCache(senderIdentifier);
			MinestuckPlayerTracker.updateGristCache(receiverIdentifier);

			notifyCommandListener(sender, this, "commands.gristSend.success", receiver, costStr.toString());
			
		} else throw new PlayerNotFoundException("Couldn't find player \"%s\".", receiver);
	}
	
	private static boolean isPermittedFor(EntityPlayerMP player, EntityPlayerMP player2)
	{
		PlayerIdentifier name1 = IdentifierHandler.encode(player), name2 = IdentifierHandler.encode(player2);
		SburbConnection c1 = SkaianetHandler.getMainConnection(name1, true);
		SburbConnection c2 = SkaianetHandler.getMainConnection(name2, true);
		if(c1 == null || c2 == null || !c1.enteredGame() || !c2.enteredGame())
			return false;
		else return SessionHandler.getPlayerSession(name1) == SessionHandler.getPlayerSession(name2);
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
	
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
	{
		if (args.length == 1)
		{
			return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
		}
		else if ((args.length > 1) && ((args.length % 2) == 1))
		{
			return getListOfStringsMatchingLastWord(args, GristType.REGISTRY.getKeys());
		}
		else
		{
			return Collections.emptyList();
		}
	}*/
}