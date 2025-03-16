package com.mraof.minestuck.blockentity;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.ColorHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
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
	public static final ResourceLocation ITEM_DYNAMIC = ResourceLocation.fromNamespaceAndPath(Minestuck.MOD_ID, "item");
	
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
	protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider pRegistries)
	{
		super.loadAdditional(nbt, pRegistries);
		stack = ItemStack.parseOptional(pRegistries, nbt.getCompound("stack"));
	}
	
	@Override
	public void saveAdditional(CompoundTag compound, HolderLookup.Provider provider)
	{
		super.saveAdditional(compound, provider);
		compound.put("stack", stack.saveOptional(provider));
	}
	
	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider provider)
	{
		CompoundTag nbt = super.getUpdateTag(provider);
		nbt.put("stack", stack.saveOptional(provider));
		return nbt;
	}
	
	@Override
	public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider)
	{
		stack = ItemStack.parseOptional(lookupProvider, tag.getCompound("stack"));
	}
	
	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket()
	{
		return ClientboundBlockEntityDataPacket.create(this);
	}
	
	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider provider)
	{
		handleUpdateTag(pkt.getTag(), provider);
		if(level != null)
		{
			BlockState state = level.getBlockState(worldPosition);
			level.sendBlockUpdated(worldPosition, state, state, 2);
		}
	}
}
