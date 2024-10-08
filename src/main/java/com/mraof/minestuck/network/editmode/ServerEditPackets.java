package com.mraof.minestuck.network.editmode;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.computer.editmode.ClientDeployList;
import com.mraof.minestuck.computer.editmode.ClientEditmodeData;
import com.mraof.minestuck.network.MSPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public final class ServerEditPackets
{
	public record Activate() implements MSPacket.PlayToClient
	{
		public static final ResourceLocation ID = Minestuck.id("server_edit/activate");
		
		@Override
		public ResourceLocation id()
		{
			return ID;
		}
		
		@Override
		public void write(FriendlyByteBuf ignored)
		{}
		
		public static Activate read(FriendlyByteBuf ignored)
		{
			return new Activate();
		}
		
		@Override
		public void execute()
		{
			ClientEditmodeData.onActivatePacket();
		}
	}
	
	public record UpdateDeployList(CompoundTag data) implements MSPacket.PlayToClient
	{
		public static final ResourceLocation ID = Minestuck.id("server_edit/update_deploy_list");
		
		@Override
		public ResourceLocation id()
		{
			return ID;
		}
		
		@Override
		public void write(FriendlyByteBuf buffer)
		{
			buffer.writeNbt(this.data);
		}
		
		public static UpdateDeployList read(FriendlyByteBuf buffer)
		{
			CompoundTag data = Objects.requireNonNull(buffer.readNbt());
			return new UpdateDeployList(data);
		}
		
		@Override
		public void execute()
		{
			ClientDeployList.load(this);
		}
	}
	
	public record Exit() implements MSPacket.PlayToClient
	{
		public static final ResourceLocation ID = Minestuck.id("server_edit/exit");
		
		@Override
		public ResourceLocation id()
		{
			return ID;
		}
		
		@Override
		public void write(FriendlyByteBuf ignored)
		{}
		
		public static Exit read(FriendlyByteBuf ignored)
		{
			return new Exit();
		}
		
		@Override
		public void execute()
		{
			ClientEditmodeData.onExitPacket(this);
		}
	}
}
