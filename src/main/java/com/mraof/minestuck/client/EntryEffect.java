package com.mraof.minestuck.client;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entry.EntryBlockIterator;
import com.mraof.minestuck.network.EntryEffectPackets;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.event.TickEvent;
import org.joml.Vector3f;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, value = Dist.CLIENT)
public final class EntryEffect
{
	public static final DustParticleOptions PARTICLE = new DustParticleOptions(new Vector3f(0.5f, 0.5f, 1.0f), 1.0f);
	public static final int VISIBLE_DISTANCE = 16;
	@Nullable
	private static EffectLocation location;
	
	public record EffectLocation(ResourceKey<Level> level, BlockPos center, int range)
	{}
	
	public static void handlePacket(EntryEffectPackets.Effect packet)
	{
		location = new EffectLocation(packet.level(), packet.center(), packet.range());
	}
	
	public static void reset()
	{
		location = null;
	}
	
	@SubscribeEvent
	public static void onLogout(ClientPlayerNetworkEvent.LoggingOut event)
	{
		reset();
	}
	
	@SubscribeEvent
	public static void clientTick(TickEvent.ClientTickEvent event)
	{
		if(event.phase != TickEvent.Phase.START)
			return;
		
		Player player = Minecraft.getInstance().player;
		if(player == null || location == null || player.level().dimension() != location.level())
			return;
		
		Level level = player.level();
		RandomSource rand = level.random;
		
		for(BlockPos pos : EntryBlockIterator.get(location.center.getX(), location.center.getY(), location.center.getZ(), location.range))
		{
			if(player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= VISIBLE_DISTANCE * VISIBLE_DISTANCE
					&& level.getBlockState(pos).isAir())
				particleAtPos(level, rand, pos);
		}
	}
	
	private static void particleAtPos(Level level, RandomSource rand, BlockPos pos)
	{
		if(rand.nextInt(200) == 0)
			level.addParticle(PARTICLE, pos.getX() + rand.nextDouble(), pos.getY() + rand.nextDouble(), pos.getZ() + rand.nextDouble(),
				rand.nextDouble()*0.1, rand.nextDouble()*0.1, rand.nextDouble()*0.1);
	}
}
