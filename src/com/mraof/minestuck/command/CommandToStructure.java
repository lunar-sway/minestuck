package com.mraof.minestuck.command;

import com.mraof.minestuck.world.WorldProviderLands;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

/**
 * Created by kirderf1 for debugging purposes
 */
public class CommandToStructure extends CommandBase
{
	@Override
	public String getName()
	{
		return "tpStruct";
	}
	
	@Override
	public String getUsage(ICommandSender sender)
	{
		return "commands.tpStruct.usage";
	}
	
	@Override
	public int getRequiredPermissionLevel()
	{
		return 2;
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if(args.length < 1)
			throw new WrongUsageException(this.getUsage(sender));
		
		EntityPlayerMP playerMP = CommandBase.getCommandSenderAsPlayer(sender);
		
		if(!playerMP.getEntityData().hasKey(EntityPlayer.PERSISTED_NBT_TAG))
			playerMP.getEntityData().setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());
		if(!playerMP.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).hasKey("commandVisitedStructures"))
			playerMP.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).setTag("commandVisitedStructures", new NBTTagList());
		
		if(playerMP.world.provider instanceof WorldProviderLands)
		{
			BlockPos location = ((WorldProviderLands) playerMP.world.provider).findAndMarkNextStructure(playerMP, args[0], playerMP.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getTagList("commandVisitedStructures", 4));
			if(location != null)
				playerMP.setPositionAndUpdate(location.getX(), location.getY(), location.getZ());
			else throw new CommandException("A problem occured");
		}
	}
}
