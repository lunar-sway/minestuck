package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.block.DungeonDoorInterfaceBlock;
import com.mraof.minestuck.block.EnumKeyType;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.GateHandler;
import net.minecraft.inventory.IClearable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;

public class DungeonDoorInterfaceTileEntity extends TileEntity
{
	private ItemStack key = ItemStack.EMPTY;
	public EnumKeyType keyType;
	
	public DungeonDoorInterfaceTileEntity()
	{
		super(MSTileEntityTypes.DUNGEON_DOOR_INTERFACE.get());
	}
	
	@Override
	public void read(CompoundNBT compound)
	{
		super.read(compound);
		if(compound.contains("key"))
			this.keyType = fromString(compound.getString("key"));
		
		/*Debug.debugf("DoorTile read = %s", ItemStack.read(compound.getCompound("KeyItem")));
		super.read(compound);
		if(compound.contains("KeyItem", 10))
		{
			this.setKey(ItemStack.read(compound.getCompound("KeyItem")));
		}*/
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		super.write(compound);
		if(this.keyType != null)
			compound.putString("key", keyType.toString());
		return compound;
		
		/*if(!this.getKey().isEmpty())
		{
			compound.put("KeyItem", this.getKey().write(new CompoundNBT()));
		}
		
		return compound;*/
	}
	
	public EnumKeyType getKey()
	{
		//key = this.world.getBlockState(pos).get(DungeonDoorInterfaceBlock.KEY);
		
		return this.keyType;
	}
	
	public void setKey(ItemStack stack)
	{
		this.key = stack;
		this.markDirty();
	}
	
	public static EnumKeyType fromString(String str)
	{
		for(EnumKeyType type : EnumKeyType.values())
		{
			if(type.toString().equals(str))
				return type;
		}
		return null;
	}
}
