package com.mraof.minestuck.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Clearable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CassettePlayerBlockEntity extends BlockEntity implements Clearable
{
	private ItemStack cassette = ItemStack.EMPTY;
	
	public CassettePlayerBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.CASSETTE_PLAYER.get(), pos, state);
	}
	
	@Override
	protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider pRegistries)
	{
		super.loadAdditional(nbt, pRegistries);
		if(nbt.contains("CassetteItem", 10))
		{
			this.setCassette(ItemStack.parseOptional(pRegistries, nbt.getCompound("CassetteItem")));
		} else
		{
			// Ensures the cassette disappears when removed
			this.setCassette(ItemStack.EMPTY);
		}
	}
	
	@Override
	public void saveAdditional(CompoundTag compound, HolderLookup.Provider provider)
	{
		super.saveAdditional(compound, provider);
		if(!this.getCassette().isEmpty())
		{
			compound.put("CassetteItem", this.getCassette().save(provider));
		}
	}
	
	public ItemStack getCassette()
	{
		return this.cassette;
	}
	
	public void setCassette(ItemStack stack)
	{
		this.cassette = stack;
		this.setChanged();
	}
	
	@Override
	public void clearContent()
	{
		this.setCassette(ItemStack.EMPTY);
	}
	
	@Override
	public void setChanged() {
		super.setChanged();
		if(level != null)
			level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_IMMEDIATE);
	}
	
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket()
	{
		return ClientboundBlockEntityDataPacket.create(this);
	}
	
	@Override
	public CompoundTag getUpdateTag(Provider registries)
	{
		return saveWithoutMetadata(registries);
	}
	
	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, Provider lookupProvider)
	{
		loadWithComponents(pkt.getTag(), lookupProvider);
	}
	
	@Override
	public void handleUpdateTag(CompoundTag tag, Provider lookupProvider)
	{
		super.handleUpdateTag(tag, lookupProvider);
		
		if(level != null)
			level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_IMMEDIATE);
	}
}
