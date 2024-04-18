package com.mraof.minestuck.entity;

import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

public class DecoyEntity extends Mob implements IEntityWithComplexSpawn
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public GameType gameType;
	public String username;
	private UUID playerId;
	private FoodData foodStats;
	private CompoundTag foodStatsNBT;
	public CompoundTag capabilities = new CompoundTag();
	
	public boolean markedForDespawn;
	private double originX, originY, originZ;
	private DecoyPlayer player;
	
	public Inventory inventory;
	
	public DecoyEntity(EntityType<? extends DecoyEntity> type, Level level)
	{
		super(type, level);
		inventory = new Inventory(null);
		if(!level.isClientSide)    //If not spawned the way it should
			markedForDespawn = true;
	}
	
	public DecoyEntity(ServerLevel level, ServerPlayer player)
	{
		super(MSEntityTypes.PLAYER_DECOY.get(), level);
		this.setBoundingBox(player.getBoundingBox());
		this.player = new DecoyPlayer(level, this, player);
		for(String key : player.getPersistentData().getAllKeys())
			this.player.getPersistentData().put(key, player.getPersistentData().get(key).copy());
		this.setPos(player.getX(), player.getY(), player.getZ());
		originX = this.getX();
		originY = this.getY();
		originZ = this.getZ();
		this.setXRot(player.getXRot());
		this.setYRot(player.getYRot());
		this.yHeadRot = player.yHeadRot;
		this.yBodyRot = player.yBodyRot;
		this.gameType = player.gameMode.getGameModeForPlayer();
		initInventory(player);
		this.getAttribute(Attributes.MAX_HEALTH).replaceFrom(player.getAttribute(Attributes.MAX_HEALTH));
		this.player.getAttribute(Attributes.MAX_HEALTH).replaceFrom(player.getAttribute(Attributes.MAX_HEALTH));
		this.setHealth(player.getHealth());
		username = player.getGameProfile().getName();
		playerId = player.getUUID();
		setNoGravity(player.getAbilities().flying);
		player.getAbilities().addSaveData(this.capabilities);
		foodStatsNBT = new CompoundTag();
		player.getFoodData().addAdditionalSaveData(foodStatsNBT);
		initFoodStats(player);
	}
	
	@Override
	public EntityDimensions getDimensions(Pose poseIn)
	{
		return EntityType.PLAYER.getDimensions();
	}
	
	private void initInventory(ServerPlayer player)
	{
		inventory = this.player.getInventory();
		
		inventory.replaceWith(player.getInventory());
	}
	
	private void initFoodStats(ServerPlayer sourcePlayer)
	{
		try
		{
			try
			{
				foodStats = new FoodData();
			} catch(NoSuchMethodError e)
			{
				LOGGER.info("Custom constructor detected for FoodStats. Trying with player as parameter...");
				try
				{
					foodStats = FoodData.class.getConstructor(Player.class).newInstance(player);
				} catch(NoSuchMethodException ex)
				{
					throw new NoSuchMethodException("Found no known constructor for net.minecraft.util.FoodStats.");
				}
			}
			foodStats.readAdditionalSaveData(foodStatsNBT);    //Exact copy of food stack
		} catch(Exception e)
		{
			foodStats = null;
			LOGGER.error("Couldn't initiate food stats for player decoy. Proceeding to not simulate food stats.", e);
			sourcePlayer.sendSystemMessage(Component.literal("An issue came up while creating the decoy. More info in the server logs."));
		}
	}
	
	public CompoundTag getFoodStatsNBT()
	{
		if(foodStats != null)
		{
			CompoundTag nbt = new CompoundTag();
			foodStats.addAdditionalSaveData(nbt);
			return nbt;
		} else return foodStatsNBT;
	}
	
	@Override
	public void writeSpawnData(FriendlyByteBuf buffer)
	{
		buffer.writeUtf(username, 16);
		buffer.writeUUID(playerId);
		buffer.writeFloat(yHeadRot);
	}
	
	@Override
	public void readSpawnData(FriendlyByteBuf additionalData)
	{
		username = additionalData.readUtf(16);
		playerId = additionalData.readUUID();
		yHeadRot = additionalData.readFloat();
		yHeadRotO = yHeadRot;
		this.setYRot(yHeadRot);    //I don't know how much of this that is necessary
		yRotO = getYRot();
		yBodyRot = getYRot();
	}
	
	public UUID getPlayerID()
	{
		return playerId;
	}
	
	@Override
	public void tick()
	{
		if(markedForDespawn)
		{
			this.discard();
			return;
		}
		super.tick();
		
		yHeadRot = yHeadRotO;    //Neutralize the effect of the LookHelper
		setYRot(yRotO);
		setXRot(xRotO);
		
		if(!level().isClientSide)
		{
			if(foodStats != null)
				foodStats.tick(player);
			if(this.locationChanged())
				ServerEditHandler.reset(ServerEditHandler.getData(this));
		}
	}
	
	public boolean locationChanged()
	{
		return originX >= this.getX() + 1 || originX <= this.getX() - 1 ||
				originY >= this.getY() + 1 || originY <= this.getY() - 1 ||
				originZ >= this.getZ() + 1 || originZ <= this.getZ() - 1;
	}
	
	@Override
	public boolean hurt(DamageSource damageSource, float par2)
	{
		if(!level().isClientSide && (!gameType.equals(GameType.CREATIVE) || damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)))
			ServerEditHandler.reset(damageSource, par2, ServerEditHandler.getData(this));
		return true;
	}
	
	@Override
	public boolean shouldShowName()
	{
		return username != null;
	}
	
	@Override
	public Component getName()
	{
		return Component.literal(username != null ? username : "DECOY");
	}
	
	@Override
	public ItemStack getItemBySlot(EquipmentSlot slotIn)
	{
		if(slotIn == EquipmentSlot.MAINHAND)
			return inventory.getSelected();
		else if(slotIn == EquipmentSlot.OFFHAND)
			return inventory.offhand.get(0);
		else return inventory.armor.get(slotIn.getIndex());
	}
	
	@Override
	public void setItemSlot(EquipmentSlot slotIn, ItemStack stack)
	{
		if(slotIn == EquipmentSlot.MAINHAND)
			inventory.setItem(inventory.selected, stack);
		else if(slotIn == EquipmentSlot.OFFHAND)
			inventory.offhand.set(0, stack);
		else inventory.armor.set(slotIn.getIndex(), stack);
	}
	
	@Override
	public void setHealth(float par1)
	{
		if(player != null)
			player.setHealth(par1);
		super.setHealth(par1);
	}
	
	@Override
	public Iterable<ItemStack> getArmorSlots()
	{
		return inventory.armor;
	}
	
	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer)
	{
		return false;
	}
	
	private static class DecoyPlayer extends FakePlayer    //Never spawned into the world. Only used for the InventoryPlayer and FoodStats.
	{
		
		DecoyEntity decoy;
		
		DecoyPlayer(ServerLevel level, DecoyEntity decoy, ServerPlayer player)
		{
			super(level, player.getGameProfile());
			player.getServer().getPlayerList().getPlayerAdvancements(player);
			//Fixes annoying NullPointerException when unlocking advancement, caused by just creating the fake player
			this.decoy = decoy;
			this.setHealth(decoy.getHealth());
		}
		
		@Override
		public void heal(float par1)
		{
			decoy.heal(par1);
		}
	}
}