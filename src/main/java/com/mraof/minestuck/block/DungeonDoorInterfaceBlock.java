package com.mraof.minestuck.block;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.KeyItem;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.tileentity.CassettePlayerTileEntity;
import com.mraof.minestuck.tileentity.DungeonDoorInterfaceTileEntity;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class DungeonDoorInterfaceBlock extends Block
{
	//private Item keyItem;
	private EnumKeyType keyType;
	public static final EnumProperty<EnumKeyType> KEY = MSProperties.KEY;
	
	public DungeonDoorInterfaceBlock(Properties properties)
	{
		super(properties);
		//this.setDefaultState(getDefaultState().with(KEY, EnumKeyType.tier_1_key)); //EnumKeyType must be exactly the same as the registered item name
		//this.asItem().addPropertyOverride(new ResourceLocation(Minestuck.MOD_ID, "key"), (stack, world, holder) -> getKeyTypeInt(stack));
		//this.addPropertyOverride(new ResourceLocation(Minestuck.MOD_ID, "count"), (stack, world, holder) -> getCount(stack));
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		ItemStack itemstack = player.getHeldItem(handIn);
		
		//keyItem = getKeyItem(state);
		//if(keyItem == null)
		//return ActionResultType.FAIL;
		
		TileEntity tileentity = worldIn.getTileEntity(pos);
		if(tileentity instanceof DungeonDoorInterfaceTileEntity && itemstack.getItem() instanceof KeyItem && !worldIn.isRemote)
		{
			EnumKeyType itemKeyType = ((KeyItem) itemstack.getItem()).keyID;
			EnumKeyType tileEntityKeyType = ((DungeonDoorInterfaceTileEntity) tileentity).getKey();
			
			//Debug.debugf("DoorBlock activated. keyStack = %s, keyStack tag = %s, itemstack = %s, itemstack tag = %s", keyStack, keyStack.getTag(), itemstack, itemstack.getTag());
			Debug.debugf("DoorBlock activated. itemKeyType = %s, tileEntityKeyType = %s", itemKeyType, tileEntityKeyType);
			
			if(itemKeyType == tileEntityKeyType/*ItemStack.areItemsEqual(itemstack, keyStack)*/)
			{
				
				removeDoorBlocks(worldIn, pos);
				if(!player.isCreative())
					itemstack.shrink(1);
				
				return ActionResultType.SUCCESS;
			}
		}
		
		return ActionResultType.FAIL;
	}
	
	/*@Override
	public BlockRenderType getRenderType(BlockState state)
	{
		return super.getRenderType(state);
	}*/
	
	
	public void removeDoorBlocks(World world, BlockPos actionOrigin)
	{
		world.playSound(null, actionOrigin.getX(), actionOrigin.getY(), actionOrigin.getZ(), SoundEvents.BLOCK_GRINDSTONE_USE, SoundCategory.BLOCKS, 1.3F, 0F);
		
		for(BlockPos blockPos : BlockPos.getAllInBoxMutable(actionOrigin.add(10, 10, 10), actionOrigin.add(-10, -10, -10)))
		{
			BlockState blockState = world.getBlockState(blockPos);
			if(blockState.getBlock() == MSBlocks.DUNGEON_DOOR)
			{
				world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
			}
		}
	}
	
	/*public void setKeyItem(Item key)
	{
		keyItem = key;
		//key.getName();
		//this.setDefaultState(getDefaultState().with(KEY, key));
	}*/
	
	/*public static ItemStack setKey(ItemStack stack, BlockState state)
	{
		CompoundNBT nbt = stack.getTag();
		if(nbt == null)
		{
			nbt = new CompoundNBT();
			stack.setTag(nbt);
			Debug.debugf("DoorBlock setKey. stackTag = %s", stack.getTag());
		}
		//nbt.putString("key", state.get(KEY).toString());
		nbt.putInt("key", state.get(KEY).ordinal());
		return stack;
	}*/
	
	/*public EnumKeyType getKeyType(ItemStack itemStack)
	{
		if(!itemStack.hasTag() || !itemStack.getTag().contains("key", Constants.NBT.TAG_ANY_NUMERIC))
			return null;
		else
		{
			//EnumKeyType stackKeyType = EnumKeyType.valueOf(itemStack.getTag().getString("key"));
			return EnumKeyType.values()[itemStack.getTag().getInt("key")];
		}
	}*/
	
	/*public int getKeyTypeInt(ItemStack itemStack)
	{
		if(!itemStack.hasTag() || !itemStack.getTag().contains("key", Constants.NBT.TAG_ANY_NUMERIC))
			return 99;
		else
		{
			return itemStack.getTag().getInt("key");
		}
	}*/
	
	/*public Item getKeyItem(BlockState state)
	{
		keyType = state.get(KEY);
		List<Item> keyList = new ArrayList<>(MSTags.Items.KEYS.getAllElements());
		for(int i = 0; i < MSTags.Items.KEYS.getAllElements().size(); i++)
		{
			Debug.debugf("keyType name = %s, keyList item name = %s", keyType.getName(), keyList.get(i).toString());
			if(keyType.getName().equals(keyList.get(i).toString()))
				return keyList.get(i);
		}
		return null;
	}*/
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		super.fillStateContainer(builder);
		//builder.add(KEY);
	}
	
	/*@Override
	public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
		if(getKeyType(stack) != null)
		{
			keyType = getKeyType(stack);
			tooltip.add(new TranslationTextComponent("item.minestuck." + this + ".key", keyType));
		}
	}*/
	
	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new DungeonDoorInterfaceTileEntity();
	}
}