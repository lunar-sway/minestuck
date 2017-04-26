package com.mraof.minestuck.item.weapon;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.util.MinestuckAchievementHandler;
import com.mraof.minestuck.util.Pair;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

//TODO add system for Minestuck weapons that can replace enchantments
public class ItemWeapon extends ItemSword //To allow enchantments such as sharpness
{
	protected double weaponDamage;
	protected double weaponSpeed;
	private final int enchantability;
	private float efficiency;
	private int radius = 0;
	HashSet<Block> farMineBaseAcceptables = new HashSet<Block>();
	HashSet<Block> farMineForbiddenBlocks = new HashSet<Block>();
	HashSet<Block> farMineForceAcceptable = new HashSet<Block>(); 

	@Deprecated
	private ItemWeapon()
	{
		this(0, 0, 0, 0, "weapon");
	}

	public ItemWeapon(int maxUses, double damageVsEntity, double weaponSpeed, int enchantability, String name)
	{
		super(ToolMaterial.IRON);
		this.maxStackSize = 1;
		this.setCreativeTab(Minestuck.tabMinestuck);
		this.setMaxDamage(maxUses);
		this.weaponDamage = damageVsEntity;
		this.enchantability = enchantability;
		this.weaponSpeed = weaponSpeed;
		this.setUnlocalizedName(name);
	}
	
	public ItemWeapon(int maxUses, double damageVsEntity, double weaponSpeed, int enchantability, String name, int radius)
	{
		this(maxUses, damageVsEntity, weaponSpeed, enchantability, name);
		this.radius = radius;
		if(radius != 0)
		{
			reinitializeFarMineLists();
		}
	}

	protected double getAttackDamage(ItemStack stack)
	{
		return weaponDamage;
	}
	
	protected double getAttackSpeed(ItemStack stack)
	{
		return weaponSpeed;
	}

	@Override
	public int getItemEnchantability()
	{
		return this.enchantability;
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack)
	{
		Multimap<String, AttributeModifier> multimap = HashMultimap.create();
		if(slot == EntityEquipmentSlot.MAINHAND)
		{
			multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", getAttackDamage(stack), 0));
			multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", getAttackSpeed(stack), 0));
		}
		return multimap;
	}

	@Override
	public boolean hitEntity(ItemStack itemStack, EntityLivingBase target, EntityLivingBase player)
	{
		itemStack.damageItem(1, player);
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isFull3D()
	{
		return true;
	}

	public ItemWeapon setTool(String toolClass, int harvestLevel, float efficiency) {
		this.efficiency = efficiency;
		this.setHarvestLevel(toolClass, harvestLevel);
		return this;
	}

	@Override
	public float getStrVsBlock(ItemStack stack, IBlockState state)
	{
		for(String tool : getToolClasses(stack))
		{
			if(state.getBlock().isToolEffective(tool, state))
			{
				return efficiency;
			}
		}
		return super.getStrVsBlock(stack, state);
	}


	@Override
	public void onCreated(ItemStack stack, World world, EntityPlayer player)
	{
		if(this == MinestuckItems.clawHammer)
		{
			player.addStat(MinestuckAchievementHandler.getHammer);
		}
	}
	
	public int getRadius()
	{
		return radius;
	}
	
	public ItemWeapon setRadius(int r)
	{
		radius = r;
		if(farMineForbiddenBlocks.isEmpty())
		{
			reinitializeFarMineLists();
		}
		return this;
	}
	
	private void reinitializeFarMineLists()
	{
		farMineForbiddenBlocks.add(Blocks.STONE);		//Stone
		farMineForbiddenBlocks.add(Blocks.GRASS);		//Grass
		farMineForbiddenBlocks.add(Blocks.NETHERRACK);	//Netherrack
		farMineForbiddenBlocks.add(Blocks.END_STONE);	//End Stone
		farMineForbiddenBlocks.add(MinestuckBlocks.coloredDirt);

		farMineBaseAcceptables.add(Blocks.GOLD_ORE);		//Gold Ore
		farMineBaseAcceptables.add(Blocks.IRON_ORE);		//Iron Ore
		farMineBaseAcceptables.add(Blocks.COAL_ORE);		//Coal Ore
		farMineBaseAcceptables.add(Blocks.LAPIS_ORE);		//Lapis Ore
		farMineBaseAcceptables.add(Blocks.DIAMOND_ORE);		//Diamond Ore
		farMineBaseAcceptables.add(Blocks.REDSTONE_ORE);	//Redstone Ore
		farMineBaseAcceptables.add(Blocks.EMERALD_ORE);		//Emerald Ore
		farMineBaseAcceptables.add(Blocks.QUARTZ_ORE);		//Nether Quartz Ore
		farMineBaseAcceptables.add(Blocks.SANDSTONE);		//Sandstone
		farMineBaseAcceptables.add(Blocks.QUARTZ_BLOCK);	//Quartz
		farMineBaseAcceptables.add(Blocks.PRISMARINE);		//Prismarine
		farMineBaseAcceptables.add(Blocks.RED_SANDSTONE);	//Red Sandstone
		farMineBaseAcceptables.add(MinestuckBlocks.oreCruxite);

		farMineForceAcceptable.add(Blocks.LOG);
		farMineForceAcceptable.add(Blocks.LOG2);
	}

	//Thanks to Mraof for supplying the base for this method.
	@Override
	public boolean canHarvestBlock(IBlockState state, ItemStack stack)
	{
		String tool = state.getBlock().getHarvestTool(state);
		if(getToolClasses(stack).contains(tool))
		{
			int blockHarvestLevel = state.getBlock().getHarvestLevel(state);
			int toolHarvestLevel = getHarvestLevel(stack, tool, null, state);
			return toolHarvestLevel >= blockHarvestLevel;
		} else
		{
			return state.getMaterial().isToolNotRequired();
		}
	}

	//Because of the use of IBlockState to determine block equivalence,
	//Blocks that would normally be considered equivalent but are trivially different
	//Will still be considered different blocks. Examples include logs oriented in different directions
	//Or torches mounted on different sides of blocks.
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState blockState, BlockPos pos, EntityLivingBase playerIn)
	{
		Comparator<Pair> comparator = new pairedIntComparator();
		PriorityQueue<Pair> candidates = new PriorityQueue<Pair>(comparator);
		Block block = blockState.getBlock();
		
		//If the tool can't harvest the block, or the player is sneaking,
		//or the tool doesn't farmine, or it's one of those blocks that breaks instantly, don't farmine.
		if(!canHarvestBlock(blockState, stack) || playerIn.isSneaking()
				|| radius == 0 || Math.abs(blockState.getBlockHardness(worldIn, pos))<0.000000001)
		{
			//All this really does is deal 2 damage to the item.
			//Unless the block's hardness is 0, in which case it doesn't.
			return super.onBlockDestroyed(stack, worldIn, blockState, pos, playerIn);
		}
		//If the block is acceptable and there's no tool mismatch, farmine normally
		else if(getToolClasses(stack).contains(blockState.getBlock().getHarvestTool(blockState))
				&& (farMineForceAcceptable.contains(block) || (!farMineForbiddenBlocks.contains(block) && farMineBaseAcceptables.contains(block))))
		{
			candidates.add(new Pair(pos, radius));
		}
		//Otherwise, cap out at a basic 3x3 area.
		else
		{
			candidates.add(new Pair(pos, Math.min(1, radius)));
		}
		
		int damageCount = 1;	//Ensures the weapon takes at least 2 points of damage.
		//This is important because all weapons take 2 damage when they break one block.
		while(!candidates.isEmpty())
		{
			BlockPos curr = (BlockPos)candidates.peek().object1;
			int rad = (Integer)candidates.poll().object2;
			if(worldIn.getBlockState(curr).getBlock()==block)
			{
				block.dropBlockAsItem(worldIn, pos, blockState, 0);
				worldIn.setBlockToAir(curr);
				//worldIn.destroyBlock(curr, true);
				damageCount++;
				if(rad>0)
				{
					//Iterates across all blocks in a 3x3 cube centered on this block.
					for(int i=-1; i<2; i++)
					{
						for(int j=-1; j<2; j++)
						{
							for(int k=-1; k<2; k++)
							{
								BlockPos newBlock = new BlockPos(curr.getX()+i, curr.getY()+j, curr.getZ()+k);
								if(worldIn.getBlockState(newBlock).getBlock()==block)
								{
									candidates.add(new Pair(newBlock, rad-1));
								}
							}
						}
					}
				}
			}
		}
		stack.damageItem(damageCount, playerIn);
		return true;
	}
}

class pairedIntComparator implements Comparator<Pair>
{
	@Override
	public int compare(Pair x, Pair y)
	{
		if(x == null || y ==null || x.object2==null || y.object2==null)
			return 0;
		
		return (Integer)y.object2-(Integer)x.object2;
	}
}