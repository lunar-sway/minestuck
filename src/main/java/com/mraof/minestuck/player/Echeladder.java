package com.mraof.minestuck.player;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.data.EcheladderDataPacket;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.EnumSet;
import java.util.Optional;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Echeladder
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static final String NEW_RUNG = "echeladder.new_rung";
	
	public static final int RUNG_COUNT = 50;
	
	private static final UUID echeladderHealthBoostModifierUUID = UUID.fromString("5b49a45b-ff22-4720-8f10-dfd745c3abb8");    //TODO Might be so that only one is needed, as we only add one modifier for each attribute.
	private static final UUID echeladderDamageBoostModifierUUID = UUID.fromString("a74176fd-bf4e-4153-bb68-197dbe4109b2");
	private static final int[] BOONDOLLARS = new int[]{0, 50, 75, 105, 140, 170, 200, 250, 320, 425, 575, 790, 1140, 1630, 2230, 2980, 3850, 4800, 6000, 7500, 9500, 11900, 15200, 19300, 24400, 45000, 68000, 95500, 124000, 180000, 260000, 425000, 632000, 880000, 1000000};
	
	public static void increaseProgress(PlayerIdentifier player, Level level, int progress)
	{
		PlayerSavedData.getData(player, level).getEcheladder().increaseProgress(progress);
	}
	
	public static void increaseProgress(ServerPlayer player, int progress)
	{
		PlayerSavedData.getData(player).getEcheladder().increaseProgress(progress);
	}
	
	@SubscribeEvent
	public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event)
	{
		Echeladder echeladder = PlayerSavedData.getData((ServerPlayer) event.getEntity()).getEcheladder();
		echeladder.updateEcheladderBonuses((ServerPlayer) event.getEntity());
		if(MinestuckConfig.SERVER.rungHealthOnRespawn.get())
			event.getEntity().heal(event.getEntity().getMaxHealth());
	}
	
	private final PlayerSavedData savedData;
	private final PlayerIdentifier identifier;
	private int rung;
	private int progress;
	EnumSet<EcheladderBonusType> usedBonuses = EnumSet.noneOf(EcheladderBonusType.class);
	
	public Echeladder(PlayerSavedData savedData, PlayerIdentifier identifier)
	{
		this.savedData = savedData;
		this.identifier = identifier;
	}
	
	private int getRungProgressReq()
	{
		return 15 * rung + 10;
	}
	
	public void increaseProgress(int exp)
	{
		//for each rung, the experience is divided and approaches 0(at infinity). That means there is a certain rung for each experience amount where it becomes less than one and no longer capable of contributing
		exp = (int) ((exp / (rung + 1) * 2) + .5D);
		Optional<SburbConnection> c = SkaianetHandler.get(savedData.mcServer).getPrimaryConnection(identifier, true);
		int topRung = c.map(SburbConnection::hasEntered).orElse(false) ? RUNG_COUNT - 1 : MinestuckConfig.SERVER.preEntryRungLimit.get();
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
				savedData.setDirty();
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
				savedData.setDirty();
				LOGGER.debug("Added remainder exp to progress, which is now at {}", progress);
			} else
				LOGGER.debug("Remaining exp {} is below 1, and will therefore be ignored", exp);
		}
		
		savedData.getData(identifier).addBoondollars(boondollarsGained);
		
		LOGGER.debug("Finished echeladder climbing for {} at {} with progress {}", identifier.getUsername(), rung, progress);
		ServerPlayer player = identifier.getPlayer(savedData.mcServer);
		if(player != null)
		{
			sendDataPacket(player, true);
			if(rung != prevRung)
			{
				updateEcheladderBonuses(player);
				player.level.playSound(null, player.getX(), player.getY(), player.getZ(), MSSoundEvents.EVENT_ECHELADDER_INCREASE.get(), SoundSource.AMBIENT, 1F, 1F);
			}
		}
	}
	
	public void checkBonus(EcheladderBonusType type)
	{
		if(!usedBonuses.contains(type))
		{
			usedBonuses.add(type);
			savedData.setDirty();
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
			attribute.removeModifier(attribute.getModifier(modifier.getId()));
		attribute.addPermanentModifier(modifier);
	}
	
	public void saveEcheladder(CompoundTag nbt)
	{
		nbt.putInt("rung", rung);
		nbt.putInt("rungProgress", progress);
		
		ListTag bonuses = new ListTag();
		
		for(EcheladderBonusType bonus : usedBonuses)
		{
			bonuses.add(StringTag.valueOf(bonus.toString()));
		}
		
		nbt.put("rungBonuses", bonuses);
	}
	
	public void loadEcheladder(CompoundTag nbt)
	{
		rung = nbt.getInt("rung");
		progress = nbt.getInt("rungProgress");
		
		for(Tag tag : nbt.getList("rungBonuses", Tag.TAG_STRING))
		{
			usedBonuses.add(EcheladderBonusType.fromString(tag.getAsString()));
		}
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
			savedData.setDirty();
			ServerPlayer player = identifier.getPlayer(savedData.mcServer);
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
		MSPacketHandler.sendToPlayer(packet, player);
	}
	
	public void sendDataPacket(ServerPlayer player, boolean sendMessage)
	{
		EcheladderDataPacket packet = EcheladderDataPacket.create(getRung(), MinestuckConfig.SERVER.echeladderProgress.get() ? getProgress() : 0F, sendMessage);
		MSPacketHandler.sendToPlayer(packet, player);
	}
	
	public static String translationKey(int rung)
	{
		return "echeladder.rung." + rung;
	}
}