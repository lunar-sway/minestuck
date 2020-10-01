package com.mraof.minestuck.player;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.data.EcheladderDataPacket;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Echeladder
{
	
	public static final String NEW_RUNG = "echeladder.new_rung";
	
	public static final int RUNG_COUNT = 50;
	public static final byte UNDERLING_BONUS_OFFSET = 0;
	public static final byte ALCHEMY_BONUS_OFFSET = 15;
	public static final double MIN_PROGRESS_MODIFIER = 1/100D;
	
	private static final UUID echeladderHealthBoostModifierUUID = UUID.fromString("5b49a45b-ff22-4720-8f10-dfd745c3abb8");	//TODO Might be so that only one is needed, as we only add one modifier for each attribute.
	private static final UUID echeladderDamageBoostModifierUUID = UUID.fromString("a74176fd-bf4e-4153-bb68-197dbe4109b2");
	private static final int[] UNDERLING_BONUSES = new int[] {10, 120, 450, 1200, 2500};	//Bonuses for first time killing an underling
	private static final int[] ALCHEMY_BONUSES = new int[] {30, 400, 3000};
													//	0				4						9							14								19								24									29										34
	private static final int[] BOONDOLLARS = new int[] {0, 50, 75, 105, 140, 170, 200, 250, 320, 425, 575, 790, 1140, 1630, 2230, 2980, 3850, 4800, 6000, 7500, 9500, 11900, 15200, 19300, 24400, 45000, 68000, 95500, 124000, 180000, 260000, 425000, 632000, 880000, 1000000};
	
	public static void increaseProgress(PlayerIdentifier player, World world, int progress)
	{
		PlayerSavedData.getData(player, world).getEcheladder().increaseProgress(progress);
	}
	
	public static void increaseProgress(ServerPlayerEntity player, int progress)
	{
		PlayerSavedData.getData(player).getEcheladder().increaseProgress(progress);
	}
	
	@SubscribeEvent
	public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event)
	{
		Echeladder echeladder = PlayerSavedData.getData((ServerPlayerEntity) event.getPlayer()).getEcheladder();
		echeladder.updateEcheladderBonuses((ServerPlayerEntity) event.getPlayer());
		if(MinestuckConfig.SERVER.rungHealthOnRespawn.get())
			event.getPlayer().heal(event.getPlayer().getMaxHealth());
	}
	
	private final PlayerSavedData savedData;
	private final PlayerIdentifier identifier;
	private int rung;
	private int progress;
	
	private boolean[] underlingBonuses = new boolean[UNDERLING_BONUSES.length];
	private boolean[] alchemyBonuses = new boolean[ALCHEMY_BONUSES.length];
	
	public Echeladder(PlayerSavedData savedData, PlayerIdentifier identifier)
	{
		this.savedData = savedData;
		this.identifier = identifier;
	}
	
	private int getRungProgressReq()
	{
		return (int) (Math.pow(1.4, rung)*9);
	}
	
	public void increaseProgress(int exp)
	{
		SburbConnection c = SkaianetHandler.get(savedData.mcServer).getMainConnection(identifier, true);
		int topRung = c != null && c.hasEntered() ? RUNG_COUNT - 1 : MinestuckConfig.SERVER.preEntryRungLimit.get();
		int expReq = getRungProgressReq();
		if(rung >= topRung || exp < expReq*MIN_PROGRESS_MODIFIER)
			return;
		
		int prevRung = rung;
		int prevExp = exp;
		Debug.debugf("Adding %s exp to player %s's echeladder (previously at rung %s progress %s)", exp, identifier.getUsername(), rung, progress);
		long boondollarsGained = 0;
		
		increment:
		{
			while(progress + exp >= expReq)
			{
				rung++;
				boondollarsGained += BOONDOLLARS[Math.min(rung, BOONDOLLARS.length - 1)];
				exp -= (expReq - progress);
				progress = 0;
				savedData.markDirty();
				expReq = getRungProgressReq();
				if(rung >= topRung)
					break increment;
				if(rung > prevRung + 1)
					exp = (int) (exp/1.5);
				Debug.debugf("Increased rung to %s, remaining exp is %s", rung, exp);
			}
			if(exp >= expReq/50)
			{
				progress += exp;
				savedData.markDirty();
				Debug.debugf("Added remainder exp to progress, which is now at %s", progress);
			} else Debug.debugf("Remaining exp %s is below the threshold of 1/50 out of the exp requirement, which is %s, and will therefore be ignored", exp, expReq/50);
		}
		
		savedData.getData(identifier).addBoondollars(boondollarsGained);
		
		Debug.debugf("Finished echeladder climbing for %s at %s with progress %s", identifier.getUsername(), rung, progress);
		ServerPlayerEntity player = identifier.getPlayer(savedData.mcServer);
		if(player != null)
		{
			sendDataPacket(player, true);
			if(rung != prevRung)
			{
				updateEcheladderBonuses(player);
				player.world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), MSSoundEvents.EVENT_ECHELADDER_INCREASE, SoundCategory.AMBIENT, 1F, 1F);
			}
		}
	}
	
	public void checkBonus(byte type)
	{
		if(type >= UNDERLING_BONUS_OFFSET && type < UNDERLING_BONUS_OFFSET + underlingBonuses.length && !underlingBonuses[type - UNDERLING_BONUS_OFFSET])
		{
			underlingBonuses[type - UNDERLING_BONUS_OFFSET] = true;
			savedData.markDirty();
			increaseProgress(UNDERLING_BONUSES[type - UNDERLING_BONUS_OFFSET]);
		} else if(type >= ALCHEMY_BONUS_OFFSET && type < ALCHEMY_BONUS_OFFSET + alchemyBonuses.length && !alchemyBonuses[type - ALCHEMY_BONUS_OFFSET])
		{
			alchemyBonuses[type - ALCHEMY_BONUS_OFFSET] = true;
			savedData.markDirty();
			increaseProgress(ALCHEMY_BONUSES[type - ALCHEMY_BONUS_OFFSET]);
		}
	}
	
	public int getRung()
	{
		return rung;
	}
	
	public float getProgress()
	{
		return ((float) progress)/getRungProgressReq();
	}
	
	public double getUnderlingDamageModifier()
	{
		return getUnderlingDamageModifier(rung);
	}
	
	public double getUnderlingProtectionModifier()
	{
		return getUnderlingProtectionModifier(rung);
	}
	
	public void updateEcheladderBonuses(ServerPlayerEntity player)
	{
		int healthBonus = healthBoost(rung);
		double damageBonus = attackBonus(rung);
		
		updateAttribute(player.getAttribute(SharedMonsterAttributes.MAX_HEALTH), new AttributeModifier(echeladderHealthBoostModifierUUID, "Echeladder Health Boost", healthBonus, AttributeModifier.Operation.ADDITION));	//If this isn't saved, your health goes to 10 hearts (if it was higher before) when loading the save file.
		updateAttribute(player.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE), new AttributeModifier(echeladderDamageBoostModifierUUID, "Echeladder Damage Boost", damageBonus, AttributeModifier.Operation.MULTIPLY_BASE).setSaved(false));
	}
	
	public void updateAttribute(IAttributeInstance attribute, AttributeModifier modifier)
	{
		if(attribute.hasModifier(modifier))
			attribute.removeModifier(attribute.getModifier(modifier.getID()));
		attribute.applyModifier(modifier);
	}
	
	public void resendAttributes(PlayerEntity player)
	{
		Set<IAttributeInstance> attributesToSend = ((AttributeMap) player.getAttributes()).getDirtyInstances();
		
		attributesToSend.add(player.getAttribute(SharedMonsterAttributes.MAX_HEALTH));
	}
	
	public void saveEcheladder(CompoundNBT nbt)
	{
		nbt.putInt("rung", rung);
		nbt.putInt("rungProgress", progress);
		
		byte[] bonuses = new byte[ALCHEMY_BONUS_OFFSET + alchemyBonuses.length];	//Booleans would be stored as bytes anyways
		for(int i = 0; i < underlingBonuses.length; i++)
			bonuses[i + UNDERLING_BONUS_OFFSET] = (byte) (underlingBonuses[i] ? 1 : 0);
		for(int i = 0; i < alchemyBonuses.length; i++)
			bonuses[i + ALCHEMY_BONUS_OFFSET] = (byte) (alchemyBonuses[i] ? 1 : 0);
		nbt.putByteArray("rungBonuses", bonuses);
	}
	
	public void loadEcheladder(CompoundNBT nbt)
	{
		rung = nbt.getInt("rung");
		progress = nbt.getInt("rungProgress");
		
		byte[] bonuses = nbt.getByteArray("rungBonuses");
		for(int i = 0; i < underlingBonuses.length && i + UNDERLING_BONUS_OFFSET < bonuses.length; i++)
			underlingBonuses[i] = bonuses[i + UNDERLING_BONUS_OFFSET] != 0;
		for(int i = 0; i < alchemyBonuses.length && i + ALCHEMY_BONUS_OFFSET < bonuses.length; i++)
			alchemyBonuses[i] = bonuses[i + ALCHEMY_BONUS_OFFSET] != 0;
	}
	
	public static double attackBonus(int rung)
	{
		return Math.pow(1.035, rung) - 1;	//rung*0.05D;
	}
	
	public static int healthBoost(int rung)
	{
		return (int) (40*(rung/(float) (Echeladder.RUNG_COUNT - 1)));	//At max rung, the player will have three rows of hearts
	}
	
	public static double getUnderlingDamageModifier(int rung)
	{
		return 1 + rung*0.04D;
	}
	
	public static double getUnderlingProtectionModifier(int rung)
	{
		return 1/(rung*0.06D + 1);
	}
	
	public void setByCommand(int rung, double progress)
	{
		int prevRung = this.rung;
		int prevProgress = this.progress;
		
		this.rung = MathHelper.clamp(rung, 0, RUNG_COUNT - 1);
		
		if(rung != RUNG_COUNT - 1)
		{
			this.progress = (int) (getRungProgressReq()*progress);
			if(this.progress >= getRungProgressReq())
				this.progress--;
		} else this.progress = 0;
		
		if(prevProgress != this.progress || prevRung != this.rung)
		{
			savedData.markDirty();
			ServerPlayerEntity player = identifier.getPlayer(savedData.mcServer);
			if(player != null && (MinestuckConfig.SERVER.echeladderProgress.get() || prevRung != this.rung))
			{
				sendDataPacket(player, false);
				if(prevRung != this.rung)
					updateEcheladderBonuses(player);
			}
		}
	}
	
	public void sendInitialPacket(ServerPlayerEntity player)
	{
		EcheladderDataPacket packet = EcheladderDataPacket.init(getRung(), MinestuckConfig.SERVER.echeladderProgress.get() ? getProgress() : 0F, MinestuckConfig.SERVER.preEntryRungLimit.get() > 0);
		MSPacketHandler.sendToPlayer(packet, player);
	}
	
	public void sendDataPacket(ServerPlayerEntity player, boolean sendMessage)
	{
		EcheladderDataPacket packet = EcheladderDataPacket.create(getRung(), MinestuckConfig.SERVER.echeladderProgress.get() ? getProgress() : 0F, sendMessage);
		MSPacketHandler.sendToPlayer(packet, player);
	}
	
	public static String translationKey(int rung)
	{
		return "echeladder.rung." + rung;
	}
}