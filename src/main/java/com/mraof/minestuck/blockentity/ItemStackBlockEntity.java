package com.mraof.minestuck.blockentity;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.ColorHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class ItemStackBlockEntity extends BlockEntity implements IColored
{
	public static final ResourceLocation ITEM_DYNAMIC = new ResourceLocation(Minestuck.MOD_ID, "item");
	
	public ItemStackBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.ITEM_STACK.get(), pos, state);
	}
	
	public ItemStackBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
	{
		super(type, pos, state);
	}
	
	private ItemStack stack = ItemStack.EMPTY;
	
	public ItemStack getStack()
	{
		return stack;
	}
	
	public void setStack(ItemStack stack)
	{
		if(stack != null)
		{
			this.stack = stack;
			setChanged();
		}
	}
	
	@Override
	public int getColor()
	{
		return ColorHandler.getColorFromStack(stack);
	}
	
	@Override
	public void load(CompoundTag nbt)
	{
		super.load(nbt);
		stack = ItemStack.of(nbt.getCompound("stack"));
	}
	
	@Override
	public void saveAdditional(CompoundTag compound)
	{
		super.saveAdditional(compound);
		compound.put("stack", stack.save(new CompoundTag()));
	}
	
	@Override
	public CompoundTag getUpdateTag()
	{
		CompoundTag nbt = super.getUpdateTag();
		nbt.put("stack", stack.save(new CompoundTag()));
		return nbt;
	}
	
	
	@Override
	public void handleUpdateTag(CompoundTag tag)
	{
		stack = ItemStack.of(tag.getCompound("stack"));
	}
	
	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket()
	{
		return ClientboundBlockEntityDataPacket.create(this);
	}
	
	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt)
	{
		handleUpdateTag(pkt.getTag());
		if(level != null)
		{
			BlockState state = level.getBlockState(worldPosition);
			level.sendBlockUpdated(worldPosition, state, state, 2);
		}
	}
}