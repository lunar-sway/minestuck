package com.mraof.minestuck.player;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.advancements.MSCriteriaTriggers;
import com.mraof.minestuck.computer.editmode.EditData;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.network.EcheladderDataPacket;
import com.mraof.minestuck.skaianet.SburbPlayerData;
import com.mraof.minestuck.util.MSAttachments;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.EnumSet;
import java.util.UUID;

@MethodsReturnNonnullByDefault
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class Echeladder implements INBTSerializable<CompoundTag>
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static final String NEW_RUNG = "echeladder.new_rung";
	
	public static final int RUNG_COUNT = 50;
	
	private static final UUID echeladderHealthBoostModifierUUID = UUID.fromString("5b49a45b-ff22-4720-8f10-dfd745c3abb8");    //TODO Might be so that only one is needed, as we only add one modifier for each attribute.
	private static final UUID echeladderDamageBoostModifierUUID = UUID.fromString("a74176fd-bf4e-4153-bb68-197dbe4109b2");
	private static final int[] BOONDOLLARS = new int[]{0, 50, 75, 105, 140, 170, 200, 250, 320, 425, 575, 790, 1140, 1630, 2230, 2980, 3850, 4800, 6000, 7500, 9500, 11900, 15200, 19300, 24400, 45000, 68000, 95500, 124000, 180000, 260000, 425000, 632000, 880000, 1000000};
	
	private static final long[] GRIST_CAPACITY =
			{60, 75, 93, 116, 145, 181, 226, 282, 352, 440, 550, 687, 858, 1072, 1340, 1675, 2093, 2616, 3270, 4087, 
			5108, 6385, 7981, 9976, 12470, 15587, 19483, 24353, 30441, 38051, 47563, 59453, 74316, 92895, 116118, 
			145147, 181433, 226791, 283488, 354360, 442950, 553687, 692108, 865135, 1081418, 1351772, 1689715, 2112143, 2640178, 3300222};
			//each value is achieved by multiplying the previous by 1.25 and then rounding the result down to get an integer number
	
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
	
	private int getRungProgressReq()
	{
		return 15 * rung + 10;
	}
	
	public void increaseProgress(int exp)
	{
		//for each rung, the experience is divided and approaches 0(at infinity). That means there is a certain rung for each experience amount where it becomes less than one and no longer capable of contributing
		exp = (int) ((exp / (rung + 1) * 2) + .5D);
		boolean hasEntered = SburbPlayerData.get(identifier, mcServer).hasEntered();
		int topRung = hasEntered ? RUNG_COUNT - 1 : MinestuckConfig.SERVER.preEntryRungLimit.get();
		int expReq = getRungProgressReq();
		
		if(rung >= topRung)
			return;
		
		int prevRung = rung;
		int prevExp = exp;
		LOGGER.debug("Adding {} exp(modified) to player {}'s echeladder (previously at rung {} progress {}/{})", exp, identifier.getUsername(), rung, progress, expReq);
		long boondollarsGained = 0;
		
		increment:
		{
			while(progress + exp >= expReq)
			{
				rung++;
				boondollarsGained += BOONDOLLARS[Math.min(rung, BOONDOLLARS.length - 1)];
				exp -= (expReq - progress);
				progress = 0;
				expReq = getRungProgressReq();
				if(rung >= topRung)
					break increment;
				if(rung > prevRung + 1)
					exp = (int) (exp / 1.5);
				LOGGER.debug("Increased rung to {}, remaining exp is {}", rung, exp);
			}
			if(exp >= 1)
			{
				progress += exp;
				LOGGER.debug("Added remainder exp to progress, which is now at {}", progress);
			} else
				LOGGER.debug("Remaining exp {} is below 1, and will therefore be ignored", exp);
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
		return ((float) progress) / getRungProgressReq();
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
		int healthBonus = healthBoost(rung);
		double damageBonus = attackBonus(rung);
		
		updateAttribute(player.getAttribute(Attributes.MAX_HEALTH), new AttributeModifier(echeladderHealthBoostModifierUUID, "Echeladder Health Boost", healthBonus, AttributeModifier.Operation.ADDITION));    //If this isn't saved, your health goes to 10 hearts (if it was higher before) when loading the save file.
		updateAttribute(player.getAttribute(Attributes.ATTACK_DAMAGE), new AttributeModifier(echeladderDamageBoostModifierUUID, "Echeladder Damage Boost", damageBonus, AttributeModifier.Operation.MULTIPLY_BASE));
	}
	
	public void updateAttribute(AttributeInstance attribute, AttributeModifier modifier)
	{
		if(attribute.hasModifier(modifier))
			attribute.removeModifier(modifier.getId());
		attribute.addPermanentModifier(modifier);
	}
	
	@Override
	public CompoundTag serializeNBT()
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
	public void deserializeNBT(CompoundTag nbt)
	{
		rung = nbt.getInt("rung");
		progress = nbt.getInt("rungProgress");
		
		for(Tag tag : nbt.getList("rungBonuses", Tag.TAG_STRING))
			usedBonuses.add(EcheladderBonusType.fromString(tag.getAsString()));
	}
	
	public static double attackBonus(int rung)
	{
		return Math.pow(1.015, rung) - 1;
	}
	
	public static int healthBoost(int rung)
	{
		return (int) (40 * (rung / (float) (Echeladder.RUNG_COUNT - 1)));    //At max rung, the player will have three rows of hearts
	}
	
	public static double getUnderlingDamageModifier(int rung)
	{
		return 1 + rung * 0.04D;
	}
	
	public static double getUnderlingProtectionModifier(int rung)
	{
		return 1 / (rung * 0.06D + 1);
	}
	
	public static long getGristCapacity(int rung)
	{
		return GRIST_CAPACITY[rung];
	}
	
	public long getGristCapacity()
	{
		return getGristCapacity(this.rung);
	}
	
	public void setByCommand(int rung, double progress)
	{
		int prevRung = this.rung;
		int prevProgress = this.progress;
		
		this.rung = Mth.clamp(rung, 0, RUNG_COUNT - 1);
		
		if(rung != RUNG_COUNT - 1)
		{
			this.progress = (int) (getRungProgressReq() * progress);
			if(this.progress >= getRungProgressReq())
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
		PacketDistributor.PLAYER.with(player).send(packet);
	}
	
	public void sendDataPacket(ServerPlayer player, boolean sendMessage)
	{
		EcheladderDataPacket packet = EcheladderDataPacket.create(getRung(), MinestuckConfig.SERVER.echeladderProgress.get() ? getProgress() : 0F, sendMessage);
		PacketDistributor.PLAYER.with(player).send(packet);
	}
	
	public static String translationKey(int rung)
	{
		return "echeladder.rung." + rung;
	}
}
