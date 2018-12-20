package com.mraof.minestuck.tileentity;


import com.mraof.minestuck.block.BlockAlchemiter;
import com.mraof.minestuck.block.BlockAlchemiterUpgrades;
import com.mraof.minestuck.block.BlockJumperBlock;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.block.BlockAlchemiter.EnumParts;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.alchemy.AlchemyRecipes;
import com.mraof.minestuck.alchemy.CombinationRegistry;
import com.mraof.minestuck.util.AlchemiterUpgrades;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.AlchemiterUpgrades.EnumType;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import scala.actors.threadpool.Arrays;
import scala.reflect.internal.Trees.Super;

import javax.annotation.Nonnull;



public class TileEntityJumperBlock extends TileEntity
{
	private boolean broken = false;
	private ItemStack upgrade[] = {ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY,ItemStack.EMPTY,ItemStack.EMPTY};
	private int color = -1;
	private int latheUpgradeId = -1;
	
	public int getColor()
	{
		return color;
	}
	
	public void setColor(int Color)
	{
		color = Color;
	}
	
	//constructor
	public TileEntityJumperBlock() {}
	//data checking

	public void setUpgrade(ItemStack stack, int id)
	{
		if((stack.getItem() == MinestuckItems.shunt && stack.hasTagCompound() && stack.getTagCompound().hasKey("contentID")) || stack.isEmpty())
		{
			upgrade[id] = stack;
			if(world != null)
			{
				IBlockState state = world.getBlockState(pos);
				world.notifyBlockUpdate(pos, state, state, 2);
			}
		}
	}
	
	@Nonnull
	public ItemStack getUpgrade(int id)
	{
		return AlchemyRecipes.getDecodedItem(upgrade[id]);
	}
	
	public ItemStack getShunt(int id)
	{
		return upgrade[id];
	}
	
	public boolean isBroken()
	{
		return broken;
	}
	
	public void setBroken()
	{
		BlockPos alchemPos = alchemiterMainPos();
		TileEntity te = world.getTileEntity(alchemPos);
		if(te instanceof TileEntityAlchemiter)
		{
			IBlockState alchemState = world.getBlockState(alchemPos);
			updateUpgradeBlocks(BlockAlchemiter.getFacing(alchemState), alchemPos, true);
			TileEntityAlchemiter alchemTe = (TileEntityAlchemiter) te;
			alchemTe.setUpgraded(false, getPos());
			
		}
		else Debug.warnf("Couldn't find TileEntityAlchemiter at %s, found %s instead.", alchemiterMainPos(), te);
		
		broken = true;
	}
	
	public void unbreakMachine()
	{
		BlockPos alchemPos = alchemiterMainPos();
		TileEntity te = world.getTileEntity(alchemPos);
		if(te instanceof TileEntityAlchemiter)
		{
			IBlockState alchemState = world.getBlockState(alchemPos);
			updateUpgradeBlocks(BlockAlchemiter.getFacing(alchemState), alchemPos, false);
			TileEntityAlchemiter alchemTe = (TileEntityAlchemiter) te;
			alchemTe.setUpgraded(true, getPos());
			
		}
		else Debug.warnf("Couldn't find TileEntityAlchemiter at %s, found %s instead.", alchemiterMainPos(), te);
		
		broken = false;
	}
	
	public BlockPos idToPos(int id)
	{
		EnumFacing facing = getFacing();
		BlockPos mainPos = getPos();
		BlockPos pos;
		
		switch(id)
		{
		default: pos = null; break;
		case 0: pos = mainPos.offset(facing.rotateYCCW(), 2); break;
		case 1: pos = mainPos.offset(facing.rotateYCCW(), 1); break;
		case 2: pos = mainPos.offset(facing.getOpposite(), 1).offset(facing.rotateYCCW(), 2);
		case 3: pos = mainPos.offset(facing.getOpposite(), 2).offset(facing.rotateYCCW(), 2);
		case 4: pos = mainPos.offset(facing.getOpposite(), 3).offset(facing.rotateYCCW(), 2);
		case 5: pos = mainPos.offset(facing.getOpposite(), 1).offset(facing.rotateYCCW(), 1);
		case 6: pos = mainPos.offset(facing.getOpposite(), 2).offset(facing.rotateYCCW(), 1);
		case 7: pos = mainPos.offset(facing.getOpposite(), 3).offset(facing.rotateYCCW(), 1);
		
		}
		
		
		return pos;
	}
	
	public EnumFacing getFacing()
	{
		return EnumFacing.getHorizontal(getBlockMetadata()%4);
	}
	
	public void onRightClick(EntityPlayer player, IBlockState clickedState, int id)
	{
		EnumFacing facing = getFacing();
		BlockPos alchemMainPos = alchemiterMainPos(facing, pos);
		boolean working = isUseable(clickedState);
		TileEntity te = world.getTileEntity(alchemMainPos);
		
		
		ItemStack heldStack = player.getHeldItemMainhand();
		BlockJumperBlock.EnumParts part = BlockJumperBlock.getPart(clickedState);
		if(part == null)
			return;
		//if they have clicked on one of the jbe plugs.
		if(part.isPlug() || part.isShunt())
		{
			
				if(!upgrade[id].isEmpty())
				{
					if(player.getHeldItemMainhand().isEmpty())
						player.setHeldItem(EnumHand.MAIN_HAND, upgrade[id]);
					else if(!player.inventory.addItemStackToInventory(upgrade[id]))
						dropItem(false, getPos(), upgrade[id]);
					else player.inventoryContainer.detectAndSendChanges();
					setUpgrade(ItemStack.EMPTY, id);
				} else if(working && heldStack.getItem() == MinestuckItems.shunt)
				{
					setUpgrade(heldStack.splitStack(1), id);
				} 
				
			}
		if(te instanceof TileEntityAlchemiter)
			updateUpgrades((TileEntityAlchemiter) te, alchemMainPos);
		
		else Debug.warnf("Couldn't find TileEntityAlchemiter at %s, found %s instead.", alchemMainPos, te);
		
		
	}
	
	private void updateUpgrades(TileEntityAlchemiter alchemTe, BlockPos AlchemPos)
	{
		
		int blockCount = 0;
		int blockCountCheck = 0;
		boolean[] sideUpgraded = {false, false, false, false};
		BlockPos alchemPos = alchemTe.getPos();
		EnumFacing alchemFacing = alchemTe.getFacing();
		
		alchemTe.setUpgraded(true, pos);
		AlchemiterUpgrades[] alchemUpgrades = alchemTe.getUpgradeList();
		IBlockState[] excludedBlocks = new IBlockState[16];
		IBlockState[] excludedLatheBlocks = new IBlockState[20];
		BlockAlchemiter.EnumParts[] totemParts = {EnumParts.TOTEM_CORNER, EnumParts.TOTEM_PAD, EnumParts.LOWER_ROD, EnumParts.UPPER_ROD};
		int EBCount = 0;
		
		if(!world.isRemote)
		{
			
			boolean hasLatheUpg = false;
			for(AlchemiterUpgrades i : alchemUpgrades)
			{
				if(i == null) continue;
				
				if(i.getUpgradeType().equals(AlchemiterUpgrades.EnumType.TOTEM_PAD))
				{
					hasLatheUpg = true;
					break;
				}
			}
			
			if(!hasLatheUpg) setLatheUpgrade(-1);
			
			for(int i = 0; i < 16; i++)
			{
				EnumFacing offsetFacing = alchemFacing;
				EnumFacing offsetFacing2 = offsetFacing.rotateY();
				int offsetFromAlchemiter = 4;
				int offset = blockCountCheck % 4;
				
				offsetFacing = getOffsetFacing(offsetFacing, blockCountCheck);
				offsetFromAlchemiter = getOffsetFromAlchemiter(offsetFacing, alchemFacing);
				offsetFacing2 = getOffsetFacing2(offsetFacing, alchemFacing);
				
				BlockPos upgPos = alchemPos.offset(offsetFacing, offsetFromAlchemiter).offset(offsetFacing2, offset).down();
				
				IBlockState state = world.getBlockState(upgPos);
				if(state.getBlock().getClass().getSuperclass().equals(BlockAlchemiterUpgrades.class)) 
				state = BlockAlchemiterUpgrades.checkForUpgrade(world, upgPos, state);
				boolean isPlaceholder = false;
				if(state.getBlock() instanceof BlockAlchemiterUpgrades)
				{
					isPlaceholder = (BlockAlchemiterUpgrades.getPartIndex(state) == 0);
					state = state.withProperty(BlockAlchemiterUpgrades.DIRECTION, EnumFacing.NORTH);
				}
				
				if(state.getBlock().getClass().getSuperclass().equals(BlockAlchemiterUpgrades.class) && !isPlaceholder) 
				{
					excludedBlocks[EBCount] = state;
					EBCount++;
					sideUpgraded[(blockCountCheck / 4) % 4] = true;
				}
				
				blockCountCheck++;
			}
			
			EBCount = 0;
			
			for(int i = 0; i < 8; i++)
			{
				BlockPos upgPos = alchemPos.down().up(i);
				IBlockState state = world.getBlockState(upgPos);
				
				if(state.getBlock().getClass().getSuperclass().equals(BlockAlchemiterUpgrades.class)) 
				state = BlockAlchemiterUpgrades.checkForUpgrade(world, upgPos, state);
				boolean isPlaceholder = false;
				if(state.getBlock() instanceof BlockAlchemiterUpgrades)
				{
					isPlaceholder = (BlockAlchemiterUpgrades.getPartIndex(state) == 0);
					state = state.withProperty(BlockAlchemiterUpgrades.DIRECTION, EnumFacing.NORTH);
				}
				
				if(state.getBlock().getClass().getSuperclass().equals(BlockAlchemiterUpgrades.class) && !isPlaceholder) 
				{
					excludedLatheBlocks[EBCount] = state;
					EBCount++;
				}
			}
			
			boolean br8k = false;
			
			for(int i = 0; i < alchemUpgrades.length; i++)
			{
				if(alchemUpgrades[i] == null) continue;
				
				IBlockState[] upgradeBlocks = alchemUpgrades[i].getUpgradeBlocks();
				
				int blockLength = upgradeBlocks.length;
				
				switch(alchemUpgrades[i].getUpgradeType())
				{
				case TOTEM_PAD:
				
					BlockPos pos = alchemPos.down();
					
					for(int j = 0; j < blockLength; j++)
					{
						if((world.getBlockState(pos).getBlockHardness(world, pos) > 1 && !(world.getBlockState(pos).getBlock().equals(MinestuckBlocks.alchemiter[0]))) 
								|| (world.getBlockState(pos).getBlock() instanceof BlockAlchemiterUpgrades))
						{
							/*TODO
							 * JBE feedback message: "there's not enough space for this upgrade"
							 */
								Debug.warn("there's not enough space for that upgrade");
								br8k = true;
								break;
							
						}
						
					}
					
					if(br8k) break;
					
					for(int j = 0; j < blockLength; j++)
					{
						if(BlockAlchemiterUpgrades.getPart(upgradeBlocks[j]) == BlockAlchemiterUpgrades.EnumParts.NONE) 
							continue;
						
						if(getLatheUpgradeId() == -1) setLatheUpgrade(alchemUpgrades[i]);
						
						IBlockState state = upgradeBlocks[j];
						if(state.getBlock() instanceof BlockAlchemiterUpgrades) state = state.withProperty(BlockAlchemiterUpgrades.DIRECTION, alchemFacing);
						if(!Arrays.asList(excludedLatheBlocks).contains(upgradeBlocks[j]) && getLatheUpgradeId() == Arrays.asList(AlchemiterUpgrades.upgradeList).indexOf(alchemUpgrades[i]))
						{
							if(BlockAlchemiterUpgrades.getPart(upgradeBlocks[j]) == BlockAlchemiterUpgrades.EnumParts.BLANK && world.getBlockState(pos.up(j)).getBlock() instanceof BlockAlchemiter)
								state = Blocks.AIR.getDefaultState();
							
							if(state == null) continue;
							
							world.destroyBlock(pos.up(j), true);
							world.setBlockState(pos.up(j), state);
							if(world.getTileEntity(pos.up(j)) instanceof TileEntityAlchemiter)
							{
								((TileEntityAlchemiter)world.getTileEntity(pos.up(j))).setUpgraded(true, getPos());
							}
						}
					}
					
					TileEntity te = world.getTileEntity(alchemPos);
					
					if(te instanceof TileEntityUpgradedAlchemiter) ((TileEntityAlchemiter) te).setUpgraded(true, getPos());
					
				break;
				default:
					
					
					for(int j = 0; j < blockLength; j++)
					{
						if(blockCount > 16)
						{
							/*TODO
							 * JBE feedback message: "there's not enough space for this upgrade"
							*/
							Debug.warn("there's not enough space for that upgrade");
							break;
						}
						
						IBlockState part = alchemUpgrades[i].getUpgradeBlocks()[j];
						
						EnumFacing offsetFacing = alchemFacing;
						EnumFacing offsetFacing2 = offsetFacing.rotateY();
						int offsetFromAlchemiter = 4;
						int offset = blockCount % 4;
						
						offsetFacing = getOffsetFacing(offsetFacing, blockCount);
						offsetFromAlchemiter = getOffsetFromAlchemiter(offsetFacing, alchemFacing);
						offsetFacing2 = getOffsetFacing2(offsetFacing, alchemFacing);
						
						IBlockState checkPart = part;
						if(part.getBlock() instanceof BlockAlchemiterUpgrades)
							{
								part = part.withProperty(BlockAlchemiterUpgrades.DIRECTION, offsetFacing.getOpposite());
								checkPart = part.withProperty(BlockAlchemiterUpgrades.DIRECTION, EnumFacing.NORTH);
							}
						BlockPos upgPos = alchemPos.offset(offsetFacing, offsetFromAlchemiter).offset(offsetFacing2, offset).down();
	
						if(!Arrays.asList(excludedBlocks).contains(checkPart))
						{
								
							if((world.getBlockState(upgPos).getBlockHardness(world, upgPos) > 1 && 
									!world.getBlockState(upgPos).getBlock().equals(MinestuckBlocks.alchemiterUpgrades[0])) || ((offset - j + blockLength-1) % 4) != (offset - j + blockLength-1))
								j--;
							else
							{
								world.destroyBlock(upgPos, true);
								world.setBlockState(upgPos, part);
								sideUpgraded[(blockCount / 4) % 4] = true;
							}
							blockCount++;
						}
						
					}
					break;
				}
			}
			
			blockCount = 0;
			
			for(int i = 0; i <= 16; i++)
			{
				EnumFacing offsetFacing = alchemFacing;
				EnumFacing offsetFacing2 = offsetFacing.rotateY();
				int offsetFromAlchemiter = 4;
				int offset = blockCount % 4;
				
				offsetFacing = getOffsetFacing(offsetFacing, blockCount);
				offsetFromAlchemiter = getOffsetFromAlchemiter(offsetFacing, alchemFacing);
				offsetFacing2 = getOffsetFacing2(offsetFacing, alchemFacing);
				
				BlockPos upgPos = alchemPos.offset(offsetFacing, offsetFromAlchemiter).offset(offsetFacing2, offset).down();
				
				BlockAlchemiterUpgrades.EnumParts part;
				EnumFacing partFacing = offsetFacing;
				
				switch(offset)
				{
				case 0: 
						part = BlockAlchemiterUpgrades.EnumParts.BASE_CORNER_RIGHT;
					break;
				case 3: 
						part = BlockAlchemiterUpgrades.EnumParts.BASE_CORNER_LEFT;
					break;
				default: 		part = BlockAlchemiterUpgrades.EnumParts.BASE_SIDE; break;
				}
				
				IBlockState state = BlockAlchemiterUpgrades.getBlockState(part, partFacing.getOpposite());
				
				if(sideUpgraded[(blockCount / 4) % 4])
				{
					if(world.getBlockState(upgPos).getBlockHardness(world, upgPos) <= 1)
					{
						world.destroyBlock(upgPos, true);
						world.setBlockState(upgPos, state);
					}
				}
				else if(world.getBlockState(upgPos).getBlock() instanceof BlockAlchemiterUpgrades) world.destroyBlock(upgPos, true);
				blockCount++;
			}
			EnumFacing checkFacing = alchemFacing;
			
			if(alchemTe instanceof TileEntityUpgradedAlchemiter)
				checkFacing = alchemFacing.getOpposite();
			
			unbreakMachine();
			
			if(getLatheUpgradeId() == -1)
			{
				for(int i = 0; i < totemParts.length; i++)
				{
					if(world.getBlockState(alchemPos.down().up(i)).getBlockHardness(world, alchemPos.down().up(i)) <= 1)
					world.destroyBlock(alchemPos.down().up(i), true);
					world.setBlockState(alchemPos.down().up(i), BlockAlchemiter.getBlockState(totemParts[i], checkFacing));
				}
			}
		}
		
	}
	
	public void setLatheUpgrade(AlchemiterUpgrades upg)
	{
		setLatheUpgrade(Arrays.asList(AlchemiterUpgrades.upgradeList).indexOf(upg));
	}
	
	public void setLatheUpgrade(int i)
	{
		latheUpgradeId = i;
	}
	
	public int getLatheUpgradeId()
	{
		return latheUpgradeId;
	}
	
	public AlchemiterUpgrades getLatheUpgrade()
	{
		return AlchemiterUpgrades.upgradeList[getLatheUpgradeId()];
	}
	
	public void updateUpgradeBlocks(EnumFacing alchemFacing, BlockPos alchemPos)
	{
		updateUpgradeBlocks(alchemFacing, alchemPos, isBroken());
	}
	
	public void updateUpgradeBlocks(EnumFacing alchemFacing, BlockPos alchemPos, boolean isBroken)
	{
		for(int i = 0; i < 16; i++)
		{
			EnumFacing offsetFacing = alchemFacing;
			EnumFacing offsetFacing2 = offsetFacing.rotateY();
			int offsetFromAlchemiter = 4;
			int offset = i % 4;
			
			offsetFacing = getOffsetFacing(offsetFacing, i);
			offsetFromAlchemiter = getOffsetFromAlchemiter(offsetFacing, alchemFacing);
			offsetFacing2 = getOffsetFacing2(offsetFacing, alchemFacing);
			
			BlockPos upgPos = alchemPos.offset(offsetFacing, offsetFromAlchemiter).offset(offsetFacing2, offset).down();
			
			IBlockState state = world.getBlockState(upgPos);
			if(state.getBlock() instanceof BlockAlchemiterUpgrades)
				world.setBlockState(upgPos, BlockAlchemiterUpgrades.checkForUpgrade(world, upgPos, state, isBroken));
			

		}
	}
	
	protected EnumFacing getOffsetFacing(EnumFacing offsetFacing, int blockCountCheck)
	{
		switch((blockCountCheck / 4) % 4)
		{
		case 3: offsetFacing = offsetFacing.rotateY();
		case 2: offsetFacing = offsetFacing.rotateY();
		case 1: offsetFacing = offsetFacing.rotateY();
		case 0: offsetFacing = offsetFacing.rotateY(); 
		}
		
		return offsetFacing;
	}
	
	protected int getOffsetFromAlchemiter(EnumFacing offsetFacing, EnumFacing alchemFacing)
	{
		if(offsetFacing == alchemFacing || offsetFacing == alchemFacing.rotateY()) 
			return 4;
		else 
			return 1;
		
	}
	
	protected EnumFacing getOffsetFacing2(EnumFacing offsetFacing, EnumFacing alchemFacing)
	{
		if(offsetFacing == alchemFacing || offsetFacing == alchemFacing.rotateYCCW()) 
		 	return offsetFacing.rotateY();
		else 
			return offsetFacing.rotateYCCW();
		
	}
	
	private boolean isUseable(IBlockState state)
	{
		IBlockState currentState = getWorld().getBlockState(getPos());
		if(!isBroken())
		{
			checkStates();
			if(isBroken())
				Debug.warnf("Failed to notice a block being broken or misplaced at the jumper block at %s", getPos());
		}
		
		if(!state.getValue(BlockJumperBlock.DIRECTION).equals(currentState.getValue(BlockJumperBlock.DIRECTION)))
			return false;
		return !isBroken();
	}
	
	public BlockPos alchemiterMainPos()
	{
		return alchemiterMainPos(getFacing(), pos);
	}
	
	public static BlockPos staticAlchemiterMainPos(BlockPos pos, World world)
	{
		IBlockState state = world.getBlockState(pos);
		BlockPos alchemPos = pos;
		
		
		
		if(state.getBlock() instanceof BlockAlchemiter)
			alchemPos = ((BlockAlchemiter)world.getBlockState(pos).getBlock()).getMainPos(state, pos, world);
		
		return alchemPos;
	}
	
	public BlockPos alchemiterMainPos(EnumFacing facing, BlockPos pos)
	{
		BlockPos alchemPos = getPos().offset(facing.rotateY()).offset(facing);
		
		
		for(int i = 0; i <= 5; i++)
		{
			if(world.getBlockState(alchemPos).equals(BlockAlchemiter.getBlockState(EnumParts.TOTEM_CORNER, facing)))
			{	
				pos = alchemPos.up();
				break;
			}
			else
			alchemPos = alchemPos.offset(facing.rotateYCCW(), 3);
			if(world.getBlockState(pos.offset(facing.rotateYCCW(), 3)).equals(BlockAlchemiter.getBlockState(EnumParts.TOTEM_CORNER, facing)))
			{
				pos = alchemPos.up();
				break;
			}
			if(world.getBlockState(alchemPos).getBlock() instanceof BlockAlchemiter || world.getBlockState(alchemPos).getBlock() instanceof BlockAlchemiterUpgrades)
				facing = world.getBlockState(alchemPos).getValue(BlockAlchemiter.DIRECTION);
			else
				facing = facing.rotateY();
		}
		
		return pos;
	}
	
	public void checkStates()
	{
		if(isBroken())
			return;
		EnumFacing facing = getFacing();
		BlockPos alchemMainPos = alchemiterMainPos(facing, pos);
		
		if(alchemMainPos == pos)
		{
			setBroken();
			return;
		}
		
		TileEntity alchemTe = world.getTileEntity(alchemMainPos);
		
		
		if(!(alchemTe instanceof TileEntityAlchemiter))
		{
			setBroken();
			return;
		} else
		{
			if(((TileEntityAlchemiter) alchemTe).isBroken())
			{
				setBroken();
				return;
			}
		}
		facing = getFacing();
		if(		!world.getBlockState(getPos()).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.CABLE, facing)) ||
				!world.getBlockState(getPos().offset(facing.getOpposite(),1)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.CENTER, facing)) ||
				!world.getBlockState(getPos().offset(facing.getOpposite(),2)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.CENTER, facing)) ||
				!world.getBlockState(getPos().offset(facing.getOpposite(),3)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.CENTER, facing)) ||
				!world.getBlockState(getPos().offset(facing.getOpposite(),4)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.BORDER_LEFT, facing)) ||				
				
				!(world.getBlockState(getPos().offset(facing.rotateYCCW(),1)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.BOTTOM_CORNER_PLUG, facing)) || world.getBlockState(getPos().offset(facing.rotateYCCW(),1)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.BOTTOM_CORNER_SHUNT, facing))) ||
				!(world.getBlockState(getPos().offset(facing.rotateYCCW(),1).offset(facing.getOpposite(),1)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.BOTTOM_PLUG, facing)) || world.getBlockState(getPos().offset(facing.rotateYCCW(),1)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.BOTTOM_SHUNT, facing))) ||
				!(world.getBlockState(getPos().offset(facing.rotateYCCW(),1).offset(facing.getOpposite(),2)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.BOTTOM_PLUG, facing)) || world.getBlockState(getPos().offset(facing.rotateYCCW(),1)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.BOTTOM_SHUNT, facing))) ||
				!(world.getBlockState(getPos().offset(facing.rotateYCCW(),1).offset(facing.getOpposite(),3)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.BOTTOM_PLUG, facing)) || world.getBlockState(getPos().offset(facing.rotateYCCW(),1)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.BOTTOM_SHUNT, facing))) ||
				!world.getBlockState(getPos().offset(facing.rotateYCCW(),1).offset(facing.getOpposite(),4)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.BORDER_SIDE, facing)) ||
				
				!(world.getBlockState(getPos().offset(facing.rotateYCCW(),2)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.TOP_CORNER_PLUG, facing)) || world.getBlockState(getPos().offset(facing.rotateYCCW(),1)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.TOP_CORNER_SHUNT, facing))) ||
				!(world.getBlockState(getPos().offset(facing.rotateYCCW(),2).offset(facing.getOpposite(),1)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.TOP_PLUG, facing)) || world.getBlockState(getPos().offset(facing.rotateYCCW(),1)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.TOP_SHUNT, facing))) ||
				!(world.getBlockState(getPos().offset(facing.rotateYCCW(),2).offset(facing.getOpposite(),2)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.TOP_PLUG, facing)) || world.getBlockState(getPos().offset(facing.rotateYCCW(),1)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.TOP_SHUNT, facing))) ||
				!(world.getBlockState(getPos().offset(facing.rotateYCCW(),2).offset(facing.getOpposite(),3)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.TOP_PLUG, facing)) || world.getBlockState(getPos().offset(facing.rotateYCCW(),1)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.TOP_SHUNT, facing))) ||
				!world.getBlockState(getPos().offset(facing.rotateYCCW(),2).offset(facing.getOpposite(),4)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.BORDER_RIGHT, facing)) ||
				
				!world.getBlockState(getPos().offset(facing.rotateY(),1)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.BASE_CORNER, facing.rotateYCCW())) ||
				!world.getBlockState(getPos().offset(facing.rotateY(),1).offset(facing.getOpposite(),1)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.BASE_SIDE, facing.rotateYCCW())) ||
				!world.getBlockState(getPos().offset(facing.rotateY(),1).offset(facing.getOpposite(),2)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.BASE_SIDE, facing.rotateYCCW())) ||
				!world.getBlockState(getPos().offset(facing.rotateY(),1).offset(facing.getOpposite(),3)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.BASE_CORNER, facing)) ||
				!world.getBlockState(getPos().offset(facing.rotateY(),1).offset(facing.getOpposite(),4)).equals(BlockJumperBlock.getState(BlockJumperBlock.EnumParts.SMALL_CORNER, facing))  

				)

		{
			setBroken();
		}
		
		if(isBroken())((TileEntityAlchemiter) alchemTe).setUpgraded(false, pos);
		else ((TileEntityAlchemiter) alchemTe).setUpgraded(true, pos);
		
	}
	
	public void dropItem(boolean inBlock, BlockPos pos, ItemStack stack)
	{
		EnumFacing direction = getFacing();
		BlockPos dropPos;
		if(inBlock)
			dropPos = pos;
		else if(!world.getBlockState(pos.offset(direction)).isBlockNormalCube())
			dropPos = pos.offset(direction);
		else dropPos = pos;
		
		InventoryHelper.spawnItemStack(world, dropPos.getX(), dropPos.getY(), dropPos.getZ(), stack);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		broken = tagCompound.getBoolean("broken");
		latheUpgradeId = tagCompound.getInteger("latheUpgradeId");
		
		for(int i = 0; i < upgrade.length; i++)
		{
			setUpgrade(new ItemStack(tagCompound.getCompoundTag("upgrade" + i)), i);
		}
		
		
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		tagCompound.setBoolean("broken",broken);
		tagCompound.setInteger("latheUpgradeId", latheUpgradeId);
		for(int i = 0; i < upgrade.length; i++)
		{
			tagCompound.setTag("upgrade" + i, upgrade[i].writeToNBT(new NBTTagCompound()));
		}
		
		return tagCompound;
	}
	@Override
	public NBTTagCompound getUpdateTag()
	{
		return writeToNBT(new NBTTagCompound());
	}
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		return new SPacketUpdateTileEntity(this.pos, 0, getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		handleUpdateTag(pkt.getNbtCompound());
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
	{
		return oldState.getBlock() != newSate.getBlock() || oldState.getValue(BlockJumperBlock.PART1) != newSate.getValue(BlockJumperBlock.PART1);
	}
	
	
	private void effects(boolean success)
	{
		BlockPos pos = getPos().up().offset(getFacing().rotateYCCW(), 2);
		world.playEvent(success ? 1000 : 1001, pos, 0);
		if (success)
		{
			EnumFacing direction = getFacing();
			int i = direction.getFrontOffsetX() + 1 + (direction.getFrontOffsetZ() + 1) * 3;
			world.playEvent(2000, pos, i);
		}
	}
}