package com.mraof.minestuck.network;

import io.netty.buffer.ByteBuf;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.util.UsernameHandler;
import com.mraof.minestuck.editmode.ServerEditHandler;

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
			writeString(data, dat[0].toString() + "\n" + dat[1].toString());
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
		if(!Minestuck.giveItems) {
			if(username == null)
				ServerEditHandler.onPlayerExit(player);
			if(!Minestuck.privateComputers || UsernameHandler.encode(player.getCommandSenderName()).equals(this.username))
				ServerEditHandler.newServerEditor((EntityPlayerMP) player, username, target);
			return;
		}
		
		EntityPlayerMP playerMP = MinecraftServer.getServer().getConfigurationManager().func_152612_a(UsernameHandler.decode(target));
		
		if(playerMP != null && (!Minestuck.privateComputers || player.getCommandSenderName().equals(UsernameHandler.decode(username)))) {
			SburbConnection c = SkaianetHandler.getClientConnection(target);
			if(c == null || !c.getServerName().equals(username) || !(c.isMain() || SkaianetHandler.giveItems(target)))
				return;
			for(int i = 0; i < c.givenItems().length; i++)
				if(i == 4) {
					if(c.enteredGame())
						continue;
					ItemStack card = AlchemyRecipeHandler.createCard(new ItemStack(Minestuck.cruxiteArtifact), true);
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
	}

	@Override
	public EnumSet<Side> getSenderSide() {
		return EnumSet.of(Side.CLIENT);
	}

}
