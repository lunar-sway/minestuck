package com.mraof.minestuck.network;

import io.netty.buffer.ByteBuf;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.UsernameHandler;
//import com.mraof.minestuck.editmode.ServerEditHandler;

import cpw.mods.fml.relauncher.Side;

public class ClientEditPacket extends MinestuckPacket {
	
	String username;
	String target;
	
	public ClientEditPacket() {
		super(Type.CLIENT_EDIT);
	}

	@Override
	public MinestuckPacket generatePacket(Object... dat) {
		if(dat.length > 0)
			writeString(data,dat[0].toString()+"\n"+dat[1].toString());
		return this;
	}

	@Override
	public MinestuckPacket consumePacket(ByteBuf data) {
		if(data.readableBytes() == 0)
			return this;
		username = readLine(data);
		target = readLine(data);
		return this;
	}

	@Override
	public void execute(EntityPlayer player) {
		EntityPlayerMP playerMP = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(UsernameHandler.decode(target));
		if(playerMP != null && (!Minestuck.privateComputers || player.getCommandSenderName().equals(UsernameHandler.decode(username)))) {
			SburbConnection c = SkaianetHandler.getClientConnection(target);
			Debug.print(c == null);
			Debug.print(!c.getServerName().equals(username));
			Debug.print(!(c.isMain() || SkaianetHandler.giveItems(target)));
			if(c == null || !c.getServerName().equals(username) || !(c.isMain() || SkaianetHandler.giveItems(target)))
				return;
			for(int i = 0; i < c.givenItems().length; i++)
				if(i == 4) {
					if(c.enteredGame())
						continue;
					ItemStack card = new ItemStack(Minestuck.punchedCard);
					card.stackTagCompound = new NBTTagCompound();
					card.stackTagCompound.setString("contentID", Item.itemRegistry.getNameForObject(Minestuck.cruxiteArtifact));
					card.stackTagCompound.setInteger("contentMeta", 0);
					if(!playerMP.inventory.hasItemStack(card))
						c.givenItems()[i] = playerMP.inventory.addItemStackToInventory(card) || c.givenItems()[i];
				} else {
					ItemStack machine = new ItemStack(Minestuck.blockMachine, 1, i);
					if(i == 1 && !c.enteredGame())
						continue;
					if(!playerMP.inventory.hasItemStack(machine))
						c.givenItems()[i] = playerMP.inventory.addItemStackToInventory(machine) || c.givenItems()[i];
				}
			MinecraftServer.getServer().getConfigurationManager().syncPlayerInventory(playerMP);
		}
		
//		if(username == null)
//			ServerEditHandler.onPlayerExit(playerMP);
//		if(!Minestuck.privateComputers || playerMP.getCommandSenderName().equals(this.username))
//			ServerEditHandler.newServerEditor(playerMP, username, target);
	}

	@Override
	public EnumSet<Side> getSenderSide() {
		return EnumSet.of(Side.CLIENT);
	}

}
