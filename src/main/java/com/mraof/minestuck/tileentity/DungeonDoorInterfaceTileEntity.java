package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.block.EnumKeyType;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;

public class DungeonDoorInterfaceTileEntity extends TileEntity
{
	private EnumKeyType keyType;
	private boolean alreadyActivated;
	
	public DungeonDoorInterfaceTileEntity()
	{
		super(MSTileEntityTypes.DUNGEON_DOOR_INTERFACE.get());
	}
	
	@Override
	public void load(BlockState state, CompoundNBT compound)
	{
		super.load(state, compound);
		
		if(compound.contains("key"))
			this.keyType = EnumKeyType.fromString(compound.getString("key"));
		else
			this.keyType = EnumKeyType.none;
		
		this.alreadyActivated = compound.getBoolean("alreadyActivated");
	}
	
	@Override
	public CompoundNBT save(CompoundNBT compound)
	{
		super.save(compound);
		if(this.keyType != null)
			compound.putString("key", keyType.toString()); //enum turned into string in order to store
		
		compound.putBoolean("alreadyActivated", alreadyActivated);
		
		return compound;
	}
	
	public EnumKeyType getKey()
	{
		return this.keyType;
	}
	
	public void setKey(EnumKeyType keyTypeIn)
	{
		this.keyType = keyTypeIn;
	}
	
	public boolean getAlreadyActivated()
	{
		return this.alreadyActivated;
	}
	
	public void setAlreadyActivated(boolean alreadyActivatedIn)
	{
		this.alreadyActivated = alreadyActivatedIn;
	}
	
	
}
