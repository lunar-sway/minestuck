package com.mraof.minestuck.block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.alchemy.AlchemyRecipes;
//import com.mraof.minestuck.block.BlockJumperBlock.BlockJumperBlock2;
import com.mraof.minestuck.block.BlockAlchemiterUpgrades.EnumParts;
//import com.mraof.minestuck.block.BlockJumperBlock.BlockJumperBlock2;
//import com.mraof.minestuck.block.BlockJumperBlock.BlockJumperBlock3;
import com.mraof.minestuck.client.gui.GuiHandler;
import com.mraof.minestuck.tileentity.TileEntityAlchemiter;
import com.mraof.minestuck.tileentity.TileEntityAlchemiterUpgrade;
import com.mraof.minestuck.tileentity.TileEntityItemStack;
import com.mraof.minestuck.tileentity.TileEntityJumperBlock;
import com.mraof.minestuck.tileentity.TileEntityUpgradedAlchemiter;
import com.mraof.minestuck.util.AlchemiterUpgrades;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiHopper;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;

public abstract class BlockAlchemiterUpgrades extends BlockLargeMachine {
	public static final PropertyEnum<EnumParts> PART1 = PropertyEnum.create("part", EnumParts.class, EnumParts.BASE_CORNER_LEFT, EnumParts.BASE_SIDE, EnumParts.BASE_CORNER_RIGHT, EnumParts.BASE);
	public static final PropertyEnum<EnumParts> PART2 = PropertyEnum.create("part", EnumParts.class, EnumParts.CHEST, EnumParts.HOPPER, EnumParts.CRAFTING, EnumParts.LIBRARY);
	public static final PropertyEnum<EnumParts> PART3 = PropertyEnum.create("part", EnumParts.class, EnumParts.GRISTWIDGET, EnumParts.CAPTCHA_CARD, EnumParts.DROPPER, EnumParts.BOONDOLLAR);
	public static final PropertyEnum<EnumParts> PART4 = PropertyEnum.create("part", EnumParts.class, EnumParts.UPGRADED_PAD, EnumParts.BLENDER, EnumParts.CRUXTRUDER, EnumParts.HOLOPAD);
	public static final PropertyEnum<EnumParts> BASE = PropertyEnum.create("part", EnumParts.class, EnumParts.BLANK, EnumParts.NONE, EnumParts.PLACEHOLDER_1, EnumParts.PLACEHOLDER_2);
	
	public final PropertyEnum<EnumParts> PART;
	public static final PropertyDirection DIRECTION = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
	private int index;
	
	public BlockAlchemiterUpgrades()
	{
		this(0, PART1);
	}
	
	public BlockAlchemiterUpgrades(int index, PropertyEnum<EnumParts> part)
	{
		this.index = index;
		PART = part;
		setUnlocalizedName("jumper_block_extension");
		
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state,IBlockAccess source,BlockPos pos)
	{
		EnumParts parts = state.getValue(PART);
		EnumFacing facing = state.getValue(DIRECTION);
		
		return parts.BOUNDING_BOX[facing.getHorizontalIndex()];
	}
	
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
     /*   if (worldIn.isRemote)
        {
            return true;
        }
        else
        	*/
        {
        	TileEntity te = worldIn.getTileEntity(getAlchemiterPos(worldIn, pos));
        	if(!(te instanceof TileEntityAlchemiter)) return false;
        	
        	TileEntityAlchemiter alchemiter = (TileEntityAlchemiter) te;
        	if(alchemiter.isBroken()) return false;
        	
        	switch(index)
        	{
        	case 1:
        		switch(getPart(state))
        		{
        		case CHEST:
        		{
    	            ILockableContainer ilockablecontainer = this.getLockableContainer(worldIn, pos);
    	
    		            if (ilockablecontainer != null)
    		            {
    		                playerIn.displayGUIChest(ilockablecontainer);
    		            }
    	        	}
        		break;
        		case CRAFTING: /*TODO*/ break;
        		default: defaultRightClick(alchemiter, worldIn, playerIn, state); break;
        		}
        	break;
        	default: defaultRightClick(alchemiter, worldIn, playerIn, state); break;
        	}
        	
            return true;
        	
        }
        
    }
	
	protected void defaultRightClick(TileEntityAlchemiter alchemiter, World worldIn, EntityPlayer playerIn, IBlockState state)
	{
		if(alchemiter instanceof TileEntityUpgradedAlchemiter)
			((TileEntityUpgradedAlchemiter)alchemiter).onRightClick(worldIn, playerIn, state);
	}
	
	@Nullable
    public ILockableContainer getLockableContainer(World worldIn, BlockPos pos)
    {
        return this.getContainer(worldIn, pos, false);
    }

	 @Nullable
	    public ILockableContainer getContainer(World worldIn, BlockPos pos, boolean allowBlocking)
	    {
	        TileEntity tileentity = worldIn.getTileEntity(pos);

	        if (!(tileentity instanceof TileEntityChest))
	        {
	            return null;
	        }
	        else
	        {
	            ILockableContainer ilockablecontainer = (TileEntityChest)tileentity;

	            {
	                for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
	                {
	                    BlockPos blockpos = pos.offset(enumfacing);
	                    Block block = worldIn.getBlockState(blockpos).getBlock();

	                    if (block == this)
	                    {
	                        
	                        TileEntity tileentity1 = worldIn.getTileEntity(blockpos);

	                        if (tileentity1 instanceof TileEntityChest)
	                        {
	                            if (enumfacing != EnumFacing.WEST && enumfacing != EnumFacing.NORTH)
	                            {
	                                ilockablecontainer = new InventoryLargeChest("container.chestDouble", ilockablecontainer, (TileEntityChest)tileentity1);
	                            }
	                            else
	                            {
	                                ilockablecontainer = new InventoryLargeChest("container.chestDouble", (TileEntityChest)tileentity1, ilockablecontainer);
	                            }
	                        }
	                    }
	                }

	                return ilockablecontainer;
	            }
	        }
	    }
	
	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return state.getValue(PART).hasTileEntity();
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		IBlockState state = getStateFromMeta(meta);
		EnumParts part = state.getValue(PART);
		
		AlchemiterUpgrades upg = AlchemiterUpgrades.getUpgradeFromBlock(part);
		
			if(hasTileEntity(state))
			{
				if(part.isUpgradedAlchemiter()) 
					return new TileEntityUpgradedAlchemiter();
				else return new TileEntityAlchemiterUpgrade(upg);
			}
			
			return null;
	}
	
	
	
	public static BlockPos getAlchemiterPos(World worldIn, BlockPos pos)
	{
		IBlockState state = worldIn.getBlockState(pos);
		if(state.getBlock() instanceof BlockAlchemiter)
			pos = ((BlockAlchemiter) state.getBlock()).getMainPos(state, pos, worldIn);
		else if(state.getBlock() instanceof BlockAlchemiterUpgrades)
			if(AlchemiterUpgrades.getUpgradeFromBlock(getPart(state)).getUpgradeType() == AlchemiterUpgrades.EnumType.TOTEM_PAD)
			{
				IBlockState[] blocks = AlchemiterUpgrades.getUpgradeFromBlock(getPart(state)).getUpgradeBlocks();
				
				pos = pos.down(Arrays.asList(blocks).indexOf(state.withProperty(DIRECTION, EnumFacing.NORTH)) - 1);
			}
				
				
		return pos;
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{

		TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof IInventory)
        {
            InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tileentity);
        }
        
		super.breakBlock(worldIn, pos, state);
	}
	
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		TileEntity te = worldIn.getTileEntity(pos);
		if(te instanceof TileEntityJumperBlock)
			((TileEntityJumperBlock) te).checkStates();
	}
	
	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
	{
		player.addStat(StatList.getBlockStats(this));
		player.addExhaustion(0.005F);
		
		if(te instanceof TileEntityItemStack)
		{
			int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
			List<ItemStack> items = new ArrayList<>();
			items.add(((TileEntityItemStack) te).getStack());
			net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, fortune, 1.0f, false, harvesters.get());
			
			for (ItemStack item : items)
			{
				spawnAsEntity(worldIn, pos, item);
			}
		} else if(te instanceof TileEntityJumperBlock)
		{
			int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
			List<ItemStack> items = new ArrayList<>();
			items.add(((TileEntityJumperBlock) te).getUpgrade(0));
			net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, fortune, 1.0f, false, harvesters.get());
			
			for (ItemStack item : items)
			{
				spawnAsEntity(worldIn, pos, item);
			}
		}
	}
	
	public static IBlockState checkForUpgrade(World worldIn, BlockPos pos, IBlockState state)
	{
		return checkForUpgrade(worldIn, pos, state, false);
	}
	
	public static IBlockState checkForUpgrade(World worldIn, BlockPos pos, IBlockState state, boolean jbeBroken)
	{
		if(jbeBroken)
		{
			worldIn.destroyBlock(pos, true);
			return Blocks.AIR.getDefaultState();
		}
		
		EnumParts part = BlockAlchemiterUpgrades.getPart(state);
		EnumFacing facing = BlockAlchemiterUpgrades.getFacing(state);
		
		BlockPos offset = (AlchemiterUpgrades.getUpgradeFromBlock(part) == null) ? pos.offset(facing) : (AlchemiterUpgrades.getUpgradeFromBlock(part).getUpgradeType() == AlchemiterUpgrades.EnumType.TOTEM_PAD) ? pos : pos.offset(facing);
		BlockPos mainPos =  getAlchemiterPos(worldIn, offset);
		
		System.out.println("mainPos: " + mainPos);
		
		TileEntity te = worldIn.getTileEntity(mainPos);
		System.out.println("te: " + te);
		
		if(te instanceof TileEntityAlchemiter)
		{
			TileEntityAlchemiter alchem = (TileEntityAlchemiter) te;
			alchem.unbreakMachine();
			alchem.checkStates();
			if(!alchem.isBroken() && AlchemiterUpgrades.hasUpgrade(alchem.getUpgradeList(), AlchemiterUpgrades.getUpgradeFromBlock(part)))
			{
				return state;
			}
		}
		worldIn.destroyBlock(pos, true);
		return Blocks.AIR.getDefaultState();
	}
	
	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) 
	{
		boolean isUpgraded = false;
		boolean isPadUpg = AlchemiterUpgrades.getUpgradeFromBlock(getPart(world.getBlockState(pos))) != null;
		if(isPadUpg) isPadUpg = world.getBlockState(pos).getBlock() instanceof BlockAlchemiterUpgrades ? (AlchemiterUpgrades.getUpgradeFromBlock(getPart(world.getBlockState(pos))).getUpgradeType() == AlchemiterUpgrades.EnumType.TOTEM_PAD) : false;
		
		BlockPos offset = isPadUpg ? pos : pos.offset(world.getBlockState(pos).getValue(DIRECTION));
		TileEntity te = world.getTileEntity(getAlchemiterPos((World)world, offset));
		
		if(te instanceof TileEntityAlchemiter) isUpgraded = ((TileEntityAlchemiter)te).isUpgraded();
		
		checkForUpgrade((World) world, pos, world.getBlockState(pos), !isUpgraded);
		super.onNeighborChange(world, pos, neighbor);
	}
	
	//Block state handling
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, PART1, DIRECTION);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		IBlockState defaultState = getDefaultState();
		EnumParts part = EnumParts.values()[ index * 4  + meta / 4];
		EnumFacing facing = EnumFacing.getHorizontal(meta % 4);
		
		return defaultState.withProperty(PART, part).withProperty(DIRECTION, facing);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		
		EnumParts part = state.getValue(PART);
		
		EnumFacing facing = state.getValue(DIRECTION);
		return (part.ordinal() % 4) * 4 + facing.getHorizontalIndex();
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		EnumParts part = state.getValue(PART);
		EnumFacing facing = state.getValue(DIRECTION);
		
		state = state.withProperty(PART, part);
		return state;
		} 
	
	public static EnumParts getPart(IBlockState state)
	{
		if(state.getBlock() instanceof BlockAlchemiterUpgrades)
			return state.getValue(((BlockAlchemiterUpgrades) state.getBlock()).PART);
		else return null;
	}
	
	public static EnumFacing getFacing(IBlockState state)
	{
		if(state.getBlock() instanceof BlockAlchemiterUpgrades)
			return state.getValue(((BlockAlchemiterUpgrades) state.getBlock()).DIRECTION);
		else return null;
	}
	
	public static int getPartIndex(EnumParts parts)
	{
		return PART1.getAllowedValues().contains(parts) ? 0 : PART2.getAllowedValues().contains(parts) ? 1 : PART3.getAllowedValues().contains(parts) ? 2 : 3;
	}
	
	public static int getPartIndex(IBlockState state)
	{
		return getPartIndex(getPart(state));
	}
	
	public static IBlockState getBlockState(EnumParts parts, EnumFacing direction)
	{
		BlockAlchemiterUpgrades block = MinestuckBlocks.alchemiterUpgrades[getPartIndex(parts)];
		IBlockState state = block.getDefaultState();
		return state.withProperty(block.PART, parts).withProperty(DIRECTION, direction);
	}
	
	public static IBlockState getBlockState(EnumParts parts)
	{
		BlockAlchemiterUpgrades block = MinestuckBlocks.alchemiterUpgrades[PART1.getAllowedValues().contains(parts) ? 0 : PART2.getAllowedValues().contains(parts) ? 1 : PART3.getAllowedValues().contains(parts) ? 2 : PART4.getAllowedValues().contains(parts) ? 3 : BASE.getAllowedValues().contains(parts) ? 4 : 5];IBlockState state = block.getDefaultState();
		return state.withProperty(block.PART, parts);
	}
	
	public static IBlockState getState(EnumParts parts, EnumFacing facing)
    {
        BlockAlchemiterUpgrades block = MinestuckBlocks.alchemiterUpgrades[PART1.getAllowedValues().contains(parts) ? 0 : PART2.getAllowedValues().contains(parts) ? 1 : PART3.getAllowedValues().contains(parts) ? 2 : PART4.getAllowedValues().contains(parts) ? 3 : BASE.getAllowedValues().contains(parts) ? 4 : 5];
        return block.getDefaultState().withProperty(block.PART, parts).withProperty(DIRECTION, facing);
    }
	
	@Override
	public Item getItemFromMachine() 
	{
		return new ItemStack(MinestuckBlocks.alchemiter[0]).getItem();
	}
	
	
	 /**
     *returns the block position of the "Main" block
     *aka the block with the TileEntity for the machine
     */
	
	
	public enum EnumParts implements IStringSerializable
	{
		BASE_CORNER_LEFT(	new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		BASE_SIDE(	new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		BASE_CORNER_RIGHT(		new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		BASE(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		
		CHEST(	new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		HOPPER(	new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		CRAFTING(		new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		LIBRARY(		new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		
		CAPTCHA_CARD(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		GRISTWIDGET(	new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		DROPPER(		new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		BOONDOLLAR(		new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		
		UPGRADED_PAD(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		BLENDER(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		CRUXTRUDER(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		HOLOPAD(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		
		BLANK(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		NONE(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		PLACEHOLDER_1(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		PLACEHOLDER_2(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		
		;
		private final AxisAlignedBB[] BOUNDING_BOX;
		
		EnumParts(AxisAlignedBB bb)
		{
			BOUNDING_BOX = new AxisAlignedBB[4];
			BOUNDING_BOX[0] = bb;
			BOUNDING_BOX[1] = new AxisAlignedBB(1 - bb.maxZ, bb.minY, bb.minX, 1 - bb.minZ, bb.maxY, bb.maxX);
			BOUNDING_BOX[2] = new AxisAlignedBB(1 - bb.maxX, bb.minY, 1- bb.maxZ, 1 - bb.minX, bb.maxY, 1 - bb.minZ);
			BOUNDING_BOX[3] = new AxisAlignedBB(bb.minZ, bb.minY, 1 - bb.maxX, bb.maxZ, bb.maxY, 1 - bb.minX);
			
		}
		
		@Override
		public String toString()
		{
			return getName();
		}
		
		@Override
		public String getName()
		{
			return name().toLowerCase();
		}
		
		public boolean hasTileEntity()
		{
			return 
				this == CHEST
				|| this == HOPPER
				|| this == DROPPER
				|| this == CRAFTING
				|| this == BLENDER
				;
		}
		
		public boolean isUpgradedAlchemiter()
		{
			return
				this == BLENDER
				;
		}
		
	}
	
	public static BlockAlchemiterUpgrades[] createBlocks()
	{
		return new BlockAlchemiterUpgrades[] {new BlockAlchemiterUpgrades1(), new BlockAlchemiterUpgrades2(), new BlockAlchemiterUpgrades3(), new BlockAlchemiterUpgrades4(), new BlockAlchemiterUpgradesSpecial()};
	}
	
	private static class BlockAlchemiterUpgrades1 extends BlockAlchemiterUpgrades
	{
		public BlockAlchemiterUpgrades1()
		{
			super(0, PART1);
		}
		
		@Override
		protected BlockStateContainer createBlockState()
		{
			return new BlockStateContainer(this, PART1, DIRECTION);
		}
	}
	
	private static class BlockAlchemiterUpgrades2 extends BlockAlchemiterUpgrades
	{
		public BlockAlchemiterUpgrades2()
		{
			super(1, PART2);
		}
		
		@Override
		protected BlockStateContainer createBlockState()
		{
			return new BlockStateContainer(this, PART2, DIRECTION);
		}
	}
	
	private static class BlockAlchemiterUpgrades3 extends BlockAlchemiterUpgrades
	{
		public BlockAlchemiterUpgrades3()
		{
			super(2, PART3);
		}
		
		
		@Override
		protected BlockStateContainer createBlockState()
		{
			return new BlockStateContainer(this, PART3, DIRECTION);
		}
	}
	
	private static class BlockAlchemiterUpgrades4 extends BlockAlchemiterUpgrades
	{
		public BlockAlchemiterUpgrades4()
		{
			super(3, PART4);
		}
		
		
		@Override
		protected BlockStateContainer createBlockState()
		{
			return new BlockStateContainer(this, PART4, DIRECTION);
		}
	}
	
	private static class BlockAlchemiterUpgradesSpecial extends BlockAlchemiterUpgrades
	{
		public BlockAlchemiterUpgradesSpecial()
		{
			super(3, BASE);
		}
		
		
		@Override
		protected BlockStateContainer createBlockState()
		{
			return new BlockStateContainer(this, BASE, DIRECTION);
		}
	}
	

}
