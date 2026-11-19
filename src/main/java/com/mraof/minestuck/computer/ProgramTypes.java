package com.mraof.minestuck.computer;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.computer.editmode.EditmodeLocations;
import com.mraof.minestuck.skaianet.ComputerInteractions;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.Supplier;

public final class ProgramTypes
{
	public static final ResourceKey<Registry<ProgramType<?>>> REGISTRY_KEY = ResourceKey.createRegistryKey(Minestuck.id("program_type"));
	
	public static final DeferredRegister<ProgramType<?>> REGISTER = DeferredRegister.create(REGISTRY_KEY, Minestuck.MOD_ID);
	
	public static final Registry<ProgramType<?>> REGISTRY = REGISTER.makeRegistry(builder -> builder.sync(true));
	
	public static final Supplier<ProgramType<ProgramType.EmptyData>> SETTINGS = REGISTER.register("settings",
			() -> new ProgramType<>(Handlers.EMPTY, ignored -> ProgramType.EmptyData.INSTANCE));
	public static final Supplier<ProgramType<DiskBurnerData>> DISK_BURNER = REGISTER.register("disk_burner",
			() -> new ProgramType<>(Handlers.EMPTY, DiskBurnerData::new));
	public static final DeferredHolder<ProgramType<?>, ProgramType<SburbServerData>> SBURB_SERVER = REGISTER.register("sburb_server",
			() -> new ProgramType<>(Handlers.SERVER, SburbServerData::new));
	public static final DeferredHolder<ProgramType<?>, ProgramType<SburbClientData>> SBURB_CLIENT = REGISTER.register("sburb_client",
			() -> new ProgramType<>(Handlers.CLIENT, SburbClientData::new));
	public static final DeferredHolder<ProgramType<?>, ProgramType<ProgramType.EmptyData>> GRIST_TORRENT = REGISTER.register("grist_torrent",
			() -> new ProgramType<>(Handlers.EMPTY, ignored -> ProgramType.EmptyData.INSTANCE));
	
	public static final Comparator<ProgramType<?>> DISPLAY_ORDER_SORTER = Comparator.comparing(REGISTRY::getId);
	
	private static final class Handlers
	{
		static final ProgramType.EventHandler EMPTY = new ProgramType.EventHandler()
		{
		};
		
		static final ProgramType.EventHandler CLIENT = new ProgramType.EventHandler()
		{
			@Override
			public void onDiskInserted(ComputerBlockEntity computer)
			{
				EditmodeLocations.addBlockSourceIfValid(computer);
			}
			
			@Override
			public void onLoad(ComputerBlockEntity computer)
			{
				EditmodeLocations.addBlockSourceIfValid(computer);
			}
			
			@Override
			public void onClosed(ComputerBlockEntity computer)
			{
				if(computer.getLevel() instanceof ServerLevel level && computer.getOwner() != null)
				{
					ComputerInteractions.get(level.getServer()).closeClientConnection(computer);    //Can safely be done even if this computer isn't in a connection
					
					EditmodeLocations.removeBlockSource(level.getServer(), computer.getOwner(), level.dimension(), computer.getBlockPos());
				}
			}
		};
		
		static final ProgramType.EventHandler SERVER = new ProgramType.EventHandler()
		{
			@Override
			public void onClosed(ComputerBlockEntity computer)
			{
				Objects.requireNonNull(computer.getLevel());
				MinecraftServer mcServer = Objects.requireNonNull(computer.getLevel().getServer());
				ComputerInteractions.get(mcServer).closeServerConnection(computer);
			}
		};
	}
}
