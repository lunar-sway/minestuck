package com.mraof.minestuck.player;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.advancements.MSCriteriaTriggers;
import com.mraof.minestuck.computer.editmode.EditData;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.data.RungsProvider;
import com.mraof.minestuck.network.EcheladderDataPacket;
import com.mraof.minestuck.skaianet.SburbPlayerData;
import com.mraof.minestuck.util.MSAttachments;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.EnumSet;

@MethodsReturnNonnullByDefault
@EventBusSubscriber(modid = Minestuck.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public final class Echeladder implements INBTSerializable<CompoundTag>
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static final String NEW_RUNG = "echeladder.new_rung";
	
	public static Echeladder get(PlayerIdentifier player, Level level)
	{
		return get(PlayerData.get(player, level));
	}
	
	public static Echeladder get(ServerPlayer player)
	{
		PlayerData playerData = PlayerData.get(player).orElseThrow(() -> new IllegalArgumentException("Cannot get player data for player " + player));
		return get(playerData);
	}
	
	public static Echeladder get(PlayerData playerData)
	{
		return playerData.getData(MSAttachments.ECHELADDER);
	}
	
	@SubscribeEvent
	private static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
	{
		ServerPlayer player = (ServerPlayer) event.getEntity();
		
		Echeladder echeladder = get(player);
		echeladder.updateEcheladderBonuses(player);
		echeladder.sendInitialPacket(player);
	}
	
	@SubscribeEvent
	private static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event)
	{
		ServerPlayer player = (ServerPlayer) event.getEntity();
		Echeladder echeladder = get(player);
		echeladder.updateEcheladderBonuses(player);
		if(MinestuckConfig.SERVER.rungHealthOnRespawn.get())
			event.getEntity().heal(event.getEntity().getMaxHealth());
	}
	
	private final MinecraftServer mcServer;
	private final PlayerIdentifier identifier;
	private int rung;
	private int progress;
	private final EnumSet<EcheladderBonusType> usedBonuses = EnumSet.noneOf(EcheladderBonusType.class);
	
	public Echeladder(PlayerData playerData)
	{
		this.mcServer = playerData.getMinecraftServer();
		this.identifier = playerData.identifier;
	}
	
	public void increaseProgress(double exp)
	{
		//for each rung, the experience is divided and approaches 0. If exp is smaller than 1, there is only a percent chance of contribution
		exp = (exp / (rung + 1) * 2);
		boolean hasEntered = SburbPlayerData.get(identifier, mcServer).hasEntered();
		int topRung = hasEntered ? Rungs.finalRung() : MinestuckConfig.SERVER.preEntryRungLimit.get();
		long expReq = Rungs.getProgressReq(rung);
		
		if(rung >= topRung)
			return;
		
		//TODO try cleaning up structure of the below, also making the logger output more concise
		int prevRung = rung;
		LOGGER.debug("Adding {} exp(modified) to player {}'s echeladder (previously at rung {} progress {}/{})", exp, identifier.getUsername(), rung, progress, expReq);
		long boondollarsGained = 0;
		
		increment:
		{
			while(progress + exp >= expReq)
			{
				rung++;
				boondollarsGained += Rungs.getBoondollarsGained(rung);
				exp -= (expReq - progress);
				progress = 0;
				expReq = Rungs.getProgressReq(rung);
				if(rung >= topRung)
					break increment;
				if(rung > prevRung + 1)
					exp = (int) (exp / 1.5);
				LOGGER.debug("Increased rung to {}, remaining exp is {}", rung, exp);
			}
			if(exp >= 1)
			{
				progress += (int) exp;
				LOGGER.debug("Added remainder exp to progress, which is now at {}", progress);
			} else
			{
				int bound = (int) (2 / exp);
				if(RandomSource.create().nextInt(bound) == 1) //there is a 1 in 2/exp chance that progress will be iterated
				{
					progress++;
					LOGGER.debug("Remaining exp {} is below 1, added 1 exp to progress with 1 in {} chance", exp, bound);
				} else
					LOGGER.debug("Remaining exp {} is below 1, failed to add 1 exp to progress with 1 in {} chance", exp, bound);
			}
		}
		
		PlayerBoondollars.addBoondollars(PlayerData.get(identifier, mcServer), boondollarsGained);
		
		LOGGER.debug("Finished echeladder climbing for {} at {} with progress {}", identifier.getUsername(), rung, progress);
		ServerPlayer player = identifier.getPlayer(mcServer);
		if(player != null)
		{
			sendDataPacket(player, true);
			if(rung != prevRung)
			{
				updateEcheladderBonuses(player);
				MSCriteriaTriggers.ECHELADDER.get().trigger(player, rung);
				player.level().playSound(null, player.getX(), player.getY(), player.getZ(), MSSoundEvents.EVENT_ECHELADDER_INCREASE.get(), SoundSource.AMBIENT, 1F, 1F);
			}
		}
		
		if(rung != prevRung)
		{
			EditData data = ServerEditHandler.getData(this.mcServer, this.identifier);
			if(data != null)
				data.sendCacheLimitToEditor();
		}
	}
	
	/**
	 * Check if the bonus has already been given to the player, give it if it hasn't.
	 *
	 * @param type
	 */
	public void checkBonus(EcheladderBonusType type)
	{
		if(!usedBonuses.contains(type))
		{
			usedBonuses.add(type);
			increaseProgress(type.getBonus());
		}
	}
	
	public int getRung()
	{
		return rung;
	}
	
	public float getProgress()
	{
		return ((float) progress) / Rungs.getProgressReq(rung);
	}
	
	public double getUnderlingDamageModifier()
	{
		return getUnderlingDamageModifier(rung);
	}
	
	public double getUnderlingProtectionModifier()
	{
		return getUnderlingProtectionModifier(rung);
	}
	
	public void updateEcheladderBonuses(ServerPlayer player)
	{
		Rungs.getRelevantAttributes(rung).forEach(attribute -> attribute.updateAttribute(player, rung));
	}
	
	@Override
	public CompoundTag serializeNBT(HolderLookup.Provider provider)
	{
		CompoundTag nbt = new CompoundTag();
		
		nbt.putInt("rung", rung);
		nbt.putInt("rungProgress", progress);
		
		ListTag bonuses = new ListTag();
		for(EcheladderBonusType bonus : usedBonuses)
			bonuses.add(StringTag.valueOf(bonus.toString()));
		nbt.put("rungBonuses", bonuses);
		
		return nbt;
	}
	
	@Override
	public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt)
	{
		rung = nbt.getInt("rung");
		progress = nbt.getInt("rungProgress");
		
		for(Tag tag : nbt.getList("rungBonuses", Tag.TAG_STRING))
			usedBonuses.add(EcheladderBonusType.fromString(tag.getAsString()));
	}
	
	public static double attackBonus(int rung)
	{
		return getAttributeAmount(rung, RungsProvider.DAMAGE_BOOST_ID);
	}
	
	public static double healthBoost(int rung)
	{
		return getAttributeAmount(rung, RungsProvider.HEALTH_BOOST_ID);
	}
	
	public static double getUnderlingDamageModifier(int rung)
	{
		return getAttributeAmount(rung, RungsProvider.UNDERLING_DAMAGE_ID);
	}
	
	public static double getUnderlingProtectionModifier(int rung)
	{
		return getAttributeAmount(rung, RungsProvider.UNDERLING_PROTECTION_ID);
	}
	
	private static double getAttributeAmount(int rung, ResourceLocation id)
	{
		for(Rung.EcheladderAttribute echeladderAttribute : Rungs.getRelevantAttributes(rung))
		{
			if(echeladderAttribute.id().equals(id))
				return echeladderAttribute.getAmount(rung);
		}
		
		return 0;
	}
	
	public long getGristCapacity()
	{
		return Rungs.getGristCapacity(this.rung);
	}
	
	public void setByCommand(int rung, double progress)
	{
		int prevRung = this.rung;
		int prevProgress = this.progress;
		
		this.rung = Mth.clamp(rung, 0, Rungs.finalRung());
		
		if(rung != Rungs.finalRung())
		{
			long progressReq = Rungs.getProgressReq(rung);
			this.progress = (int) (progressReq * progress);
			if(this.progress >= progressReq)
				this.progress--;
		} else this.progress = 0;
		
		if(prevProgress != this.progress || prevRung != this.rung)
		{
			ServerPlayer player = identifier.getPlayer(mcServer);
			if(player != null && (MinestuckConfig.SERVER.echeladderProgress.get() || prevRung != this.rung))
			{
				sendDataPacket(player, false);
				if(prevRung != this.rung)
					updateEcheladderBonuses(player);
			}
		}
	}
	
	public void sendInitialPacket(ServerPlayer player)
	{
		EcheladderDataPacket packet = EcheladderDataPacket.init(getRung(), MinestuckConfig.SERVER.echeladderProgress.get() ? getProgress() : 0F);
		PacketDistributor.sendToPlayer(player, packet);
	}
	
	public void sendDataPacket(ServerPlayer player, boolean sendMessage)
	{
		EcheladderDataPacket packet = EcheladderDataPacket.create(getRung(), MinestuckConfig.SERVER.echeladderProgress.get() ? getProgress() : 0F, sendMessage);
		PacketDistributor.sendToPlayer(player, packet);
	}
	
	public static String translationKey(int rung)
	{
		return "echeladder.rung." + rung;
	}
}
