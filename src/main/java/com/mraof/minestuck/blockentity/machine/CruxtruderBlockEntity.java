package com.mraof.minestuck.blockentity.machine;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.CruxiteDowelBlock;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.blockentity.ItemStackBlockEntity;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.util.ColorHandler;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

/**
 * Stores raw cruxite and produces uncarved cruxite dowels.
 * Core Editmode deployable
 */
@ParametersAreNonnullByDefault
public class CruxtruderBlockEntity extends BlockEntity
{
	private static final Logger LOGGER = LogManager.getLogger();
	public static final String EMPTY = "block.minestuck.cruxtruder.empty";
	
	private static final int CRUXITE_CAPACITY = 64;
	
	private int color = -1;
	private boolean isBroken = false;
	private int material = 0;
	
	public CruxtruderBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.CRUXTRUDER.get(), pos, state);
	}
	
	public int getColor()
	{
		return color;
	}
	
	public void setColor(int Color)
	{
		color = Color;
	}
	
	public void setBroken()
	{
		this.isBroken = true;
		this.setChanged();
	}
	
	private void checkIfValid()
	{
		if(this.isBroken || this.level == null)
			return;
		
		if(MSBlocks.CRUXTRUDER.isInvalidFromTube(this.level, this.getBlockPos()))
		{
			this.setBroken();
			LOGGER.warn("Failed to notice a block being changed until afterwards at the cruxtruder at {}", getBlockPos());
		}
	}
	
	private void setMaterial(int material)
	{
		this.material = material;
		this.setChanged();
	}
	
	public void dropItems()
	{
		if(this.material > 0)
		{
			BlockPos pos = this.getBlockPos();
			Containers.dropItemStack(Objects.requireNonNull(this.level), pos.getX(), pos.getY(), pos.getZ(),
					new ItemStack(MSItems.RAW_CRUXITE.get(), this.material));
			this.setMaterial(0);
		}
	}

	public void onRightClick(Player player, boolean top)
	{
		this.checkIfValid();
		if(!this.isBroken && level != null)
		{
			BlockPos pos = getBlockPos().above();
			BlockState state = level.getBlockState(pos);
			if(top && MinestuckConfig.SERVER.cruxtruderIntake.get() && state.isAir() && -1 < material && material < CRUXITE_CAPACITY)
			{
				ItemStack stack = player.getMainHandItem();
				if(!stack.is(MSItems.RAW_CRUXITE.get()))
					stack = player.getOffhandItem();
				
				if(stack.is(MSItems.RAW_CRUXITE.get()))
				{
					int count = 1;
					if(player.isShiftKeyDown())	//Doesn't actually work just yet
						count = Math.min(CRUXITE_CAPACITY - material, stack.getCount());
					
					if(!player.isCreative())
						stack.shrink(count);
					
					this.setMaterial(this.material + count);
					
					level.playSound(null, pos, MSSoundEvents.CRUXTRUDER_DOWEL.get(), SoundSource.BLOCKS, 1F, 1F);
				}
			} else if(!top)
			{
				if(state.is(MSBlocks.EMERGING_CRUXITE_DOWEL.get()))
				{
					CruxiteDowelBlock.dropDowel(level, pos);
				} else if(state.isAir())
				{
					if(MinestuckConfig.SERVER.cruxtruderIntake.get() && material == 0)
					{
						level.levelEvent(LevelEvent.SOUND_DISPENSER_FAIL, pos, 0);
						player.sendSystemMessage(Component.translatable(EMPTY));
					} else
					{
						level.setBlockAndUpdate(pos, MSBlocks.EMERGING_CRUXITE_DOWEL.get().defaultBlockState());
						level.playSound(null, pos, MSSoundEvents.CRUXTRUDER_DOWEL.get(), SoundSource.BLOCKS, 1F, 1.75F);
						
						if(level.getBlockEntity(pos) instanceof ItemStackBlockEntity blockEntity)
							ColorHandler.setColor(blockEntity.getStack(), color);
						else
							LOGGER.warn("Did not find block entity for setting cruxite color after placing cruxtruder dowel at {}", pos);
						if(0 < material)
							this.setMaterial(this.material - 1);
					}
				}
			}
		}
	}
	
	@Override
	public void load(CompoundTag nbt)
	{
		super.load(nbt);
		
		if(nbt.contains("color"))
			color = nbt.getInt("color");
		this.isBroken = nbt.getBoolean("broken");
		material = nbt.getInt("material");
	}
	
	@Override
	public void saveAdditional(CompoundTag compound)
	{
		super.saveAdditional(compound);
		compound.putInt("color", color);
		compound.putBoolean("broken", this.isBroken);
		compound.putInt("material", material);
	}
}