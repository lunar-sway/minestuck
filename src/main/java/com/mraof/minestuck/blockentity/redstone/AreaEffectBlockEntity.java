package com.mraof.minestuck.blockentity.redstone;

import com.mraof.minestuck.block.redstone.AreaEffectBlock;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.effects.CreativeShockEffect;
import com.mraof.minestuck.effects.MSEffects;
import com.mraof.minestuck.network.block.AreaEffectSettingsPacket;
import com.mraof.minestuck.util.MSRotationUtil;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AreaEffectBlockEntity extends BlockEntity
{
	@Nonnull
	private MobEffect effect = MSEffects.CREATIVE_SHOCK.get();
	private int effectAmplifier;
	@Nonnull
	private BlockPos minAreaOffset = new BlockPos(-16, -16, -16);
	@Nonnull
	private BlockPos maxAreaOffset = new BlockPos(16, 16, 16);
	
	public AreaEffectBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.AREA_EFFECT.get(), pos, state);
	}
	
	public static void serverTick(Level level, BlockPos pos, BlockState state, AreaEffectBlockEntity blockEntity)
	{
		
		if(!level.isAreaLoaded(pos, 0))
			return;
		
		if(level.getGameTime() % 80 == 0 && !level.isClientSide && state.getValue(AreaEffectBlock.POWERED) && !state.getValue(AreaEffectBlock.SHUT_DOWN))
		{
			blockEntity.giveEntitiesEffect();
		}
	}
	
	public void giveEntitiesEffect()
	{
		Objects.requireNonNull(this.level);
		BlockPos bePos = getBlockPos();
		Direction beFacing = getBlockState().getValue(AreaEffectBlock.FACING);
		
		
		BlockPos minAreaPos = bePos.offset(minAreaOffset.rotate(MSRotationUtil.rotationBetween(Direction.EAST, beFacing)));
		BlockPos maxAreaPos = bePos.offset(maxAreaOffset.rotate(MSRotationUtil.rotationBetween(Direction.EAST, beFacing)));
		
		if(getBlockState().getValue(AreaEffectBlock.ALL_MOBS))
		{
			for(LivingEntity livingEntity : level.getEntitiesOfClass(LivingEntity.class,
					AABB.encapsulatingFullBlocks(minAreaPos, maxAreaPos)))
			{
				iterateThroughEntities(livingEntity);
			}
		} else
		{
			for(Player playerEntity : level.getEntitiesOfClass(Player.class,
					AABB.encapsulatingFullBlocks(minAreaPos, maxAreaPos)))
			{
				iterateThroughEntities(playerEntity);
			}
		}
	}
	
	private void iterateThroughEntities(LivingEntity entityIterate)
	{
		if(effect instanceof CreativeShockEffect) //skips later creative/harmful specific checks as the effect should always be given
		{
			entityIterate.addEffect(new MobEffectInstance(effect, 120, effectAmplifier, false, false));
		} else
		{
			boolean ignoreEntity = entityIterate instanceof Player player && player.isCreative() && !effect.isBeneficial(); //if not a player, or it is a player but they are in creative and the effect is not beneficial, ignore
			
			if(!ignoreEntity)
				entityIterate.addEffect(new MobEffectInstance(effect, effect.isInstantenous() ? 1 : 120, effectAmplifier, false, false));
		}
	}
	
	public void setEffect(MobEffect effectIn, int effectAmplifierIn)
	{
		this.effect = Objects.requireNonNull(effectIn);
		this.effectAmplifier = effectAmplifierIn;
		setChanged();
		if(getLevel() instanceof ServerLevel serverLevel)
			serverLevel.getChunkSource().blockChanged(getBlockPos());
	}
	
	public MobEffect getEffect()
	{
		return this.effect;
	}
	
	public int getEffectAmplifier()
	{
		return this.effectAmplifier;
	}
	
	public BlockPos getMinAreaOffset()
	{
		return this.minAreaOffset;
	}
	
	public BlockPos getMaxAreaOffset()
	{
		return this.maxAreaOffset;
	}
	
	public void handleSettingsPacket(AreaEffectSettingsPacket packet)
	{
		Objects.requireNonNull(this.level);
		
		this.minAreaOffset = clampMinPos(packet.minEffectPos().getX(), packet.minEffectPos().getY(), packet.minEffectPos().getZ());
		this.maxAreaOffset = clampMaxPos(packet.maxEffectPos().getX(), packet.maxEffectPos().getY(), packet.maxEffectPos().getZ());
		setChanged();
		
		this.setEffect(packet.effect(), packet.effectAmp());
		
		this.level.setBlock(this.worldPosition, getBlockState().setValue(AreaEffectBlock.ALL_MOBS, packet.isAllMobs()), Block.UPDATE_ALL);
	}
	
	/**
	 * Checks to make sure that the minimum effect pos is within legal bounds, defaults to the intended boundary if it is too far away from the block
	 */
	public static BlockPos clampMinPos(int x, int y, int z)
	{
		return new BlockPos(Math.max(x, -64), Math.max(y, -64), Math.max(z, -64));
	}
	
	/**
	 * Checks to make sure that the maximum effect pos is within legal bounds, defaults to the intended boundary if it is too far away from the block
	 */
	public static BlockPos clampMaxPos(int x, int y, int z)
	{
		return new BlockPos(Math.min(x, 64), Math.min(y, 64), Math.min(z, 64));
	}
	
	@Override
	public void load(CompoundTag compound)
	{
		super.load(compound);
		
		MobEffect effectRead = BuiltInRegistries.MOB_EFFECT.get(new ResourceLocation(compound.getString("effect")));
		if(effectRead != null)
			effect = effectRead;
		
		effectAmplifier = compound.getInt("effectAmplifier");
		
		int minAreaOffsetX = compound.getInt("minAreaOffsetX");
		int minAreaOffsetY = compound.getInt("minAreaOffsetY");
		int minAreaOffsetZ = compound.getInt("minAreaOffsetZ");
		this.minAreaOffset = clampMinPos(minAreaOffsetX, minAreaOffsetY, minAreaOffsetZ);
		
		int maxAreaOffsetX = compound.getInt("maxAreaOffsetX");
		int maxAreaOffsetY = compound.getInt("maxAreaOffsetY");
		int maxAreaOffsetZ = compound.getInt("maxAreaOffsetZ");
		this.maxAreaOffset = clampMaxPos(maxAreaOffsetX, maxAreaOffsetY, maxAreaOffsetZ);
	}
	
	@Override
	public void saveAdditional(CompoundTag compound)
	{
		super.saveAdditional(compound);
		
		compound.putString("effect", String.valueOf(BuiltInRegistries.MOB_EFFECT.getKey(this.effect)));
		compound.putInt("effectAmplifier", effectAmplifier);
		
		compound.putInt("minAreaOffsetX", minAreaOffset.getX());
		compound.putInt("minAreaOffsetY", minAreaOffset.getY());
		compound.putInt("minAreaOffsetZ", minAreaOffset.getZ());
		
		compound.putInt("maxAreaOffsetX", maxAreaOffset.getX());
		compound.putInt("maxAreaOffsetY", maxAreaOffset.getY());
		compound.putInt("maxAreaOffsetZ", maxAreaOffset.getZ());
	}
	
	@Override
	public CompoundTag getUpdateTag()
	{
		return this.saveWithoutMetadata();
	}
	
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket()
	{
		return ClientboundBlockEntityDataPacket.create(this);
	}
}
