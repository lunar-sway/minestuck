package com.mraof.minestuck.network;

import io.netty.buffer.ByteBuf;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.editmode.DeployList;
import com.mraof.minestuck.inventory.ContainerHandler;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.util.UsernameHandler;

public class MinestuckConfigPacket extends MinestuckPacket
{
	
	boolean mode;
	
	int overWorldEditRange;
	int landEditRange;
	int cardCost;
	int windowIdStart;
	byte treeModusSetting;
	
	boolean giveItems;
	boolean easyDesignix;
	boolean cardRecipe;
	boolean disableGristWidget;
	boolean dataChecker;
	boolean preEntryEcheladder;
	boolean[] deployValues;
	
	String lanHost;
	
	@Override
	public MinestuckPacket generatePacket(Object... dat)
	{
		boolean mode = (Boolean) dat[0];
		data.writeBoolean(mode);
		if(mode)	//Values that shouldn't be changed while the game is running, and should because of that only be sent once
		{
			data.writeInt(MinestuckConfig.overworldEditRange);
			data.writeInt(MinestuckConfig.landEditRange);
			data.writeInt(ContainerHandler.windowIdStart);
			data.writeBoolean(MinestuckConfig.giveItems);
			data.writeBoolean(MinestuckConfig.cardRecipe);
			
			for(int i = 0; i < MinestuckConfig.deployConfigurations.length; i++)
				data.writeBoolean(MinestuckConfig.deployConfigurations[i]);
			if(UsernameHandler.host != null)
				writeString(data,UsernameHandler.host);
		} else
		{
			data.writeInt(MinestuckConfig.cardCost);
			data.writeBoolean(MinestuckConfig.disableGristWidget);
			data.writeByte(MinestuckConfig.treeModusSetting);
			data.writeBoolean((Boolean) dat[1]);
			data.writeBoolean(MinestuckConfig.preEntryRungLimit <= 0);
		}
		
		return this;
	}
	
	@Override
	public MinestuckPacket consumePacket(ByteBuf data)
	{
		mode = data.readBoolean();
		
		if(mode)
		{
			overWorldEditRange = data.readInt();
			landEditRange = data.readInt();
			windowIdStart = data.readInt();
			giveItems = data.readBoolean();
			cardRecipe = data.readBoolean();
			
			deployValues = new boolean[MinestuckConfig.deployConfigurations.length];
			for(int i = 0; i < deployValues.length; i++)
				deployValues[i] = data.readBoolean();
			lanHost = readLine(data);
			if(lanHost.isEmpty())
				lanHost = null;
		} else
		{
			cardCost = data.readInt();
			disableGristWidget = data.readBoolean();
			treeModusSetting = data.readByte();
			dataChecker = data.readBoolean();
			preEntryEcheladder = data.readBoolean();
		}
		
		return this;
	}

	@Override
	public void execute(EntityPlayer player)
	{
		if(mode)
		{
			MinestuckConfig.clientOverworldEditRange = overWorldEditRange;
			MinestuckConfig.clientLandEditRange = landEditRange;
			MinestuckConfig.clientGiveItems = giveItems;
			ContainerHandler.clientWindowIdStart = windowIdStart;
			
			if(!Minestuck.isServerRunning)
			{
				UsernameHandler.host = lanHost;
				DeployList.applyConfigValues(deployValues);
				AlchemyRecipeHandler.addOrRemoveRecipes(cardRecipe);
			}
		} else
		{
			MinestuckConfig.clientCardCost = cardCost;
			MinestuckConfig.clientDisableGristWidget = disableGristWidget;
			MinestuckConfig.clientTreeAutobalance = treeModusSetting;
			MinestuckConfig.dataCheckerAccess = dataChecker;
			MinestuckConfig.preEntryEcheladder = preEntryEcheladder;
		}
	}

	@Override
	public EnumSet<Side> getSenderSide() {
		return EnumSet.of(Side.SERVER);
	}

}
