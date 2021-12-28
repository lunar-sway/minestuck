package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.world.storage.loot.MSLootTables;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class LootBlockTileEntity extends TileEntity
{
	private ResourceLocation lootTable;
	
	public LootBlockTileEntity()
	{
		super(MSTileEntityTypes.LOOT_BLOCK.get());
	}
	
	@Override
	public void load(BlockState state, CompoundNBT compound)
	{
		super.load(state, compound);
		
		if(compound.contains("lootTable"))
			this.lootTable = new ResourceLocation(compound.getString("lootTable"));
		else
			this.lootTable = MSLootTables.BASIC_MEDIUM_CHEST;
	}
	
	@Override
	public CompoundNBT save(CompoundNBT compound)
	{
		super.save(compound);
		if(this.lootTable != null)
			compound.putString("lootTable", lootTable.toString());
		
		return compound;
	}
	
	public ResourceLocation getLootTable()
	{
		if(this.lootTable == null)
			this.lootTable = MSLootTables.BASIC_MEDIUM_CHEST;
		return this.lootTable;
	}
	
	public void setLootTable(ResourceLocation lootTableIn)
	{
		this.lootTable = lootTableIn;
	}
}
