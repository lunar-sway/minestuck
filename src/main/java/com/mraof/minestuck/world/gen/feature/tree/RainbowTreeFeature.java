package com.mraof.minestuck.world.gen.feature.tree;

//public class RainbowTreeFeature extends AbstractTreeFeature<NoFeatureConfig> TODO maybe reimplement? (might not be needed with vanillas changes together with forge patches)
//{
//	private static final BlockState LOG = MSBlocks.RAINBOW_LOG.getDefaultState();
//	private static final BlockState LEAF = MSBlocks.RAINBOW_LEAVES.getDefaultState();
//
//	public RainbowTreeFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactory, boolean doBlockNotify)
//	{
//		super(configFactory);
//	}
//
//	@Override
//	protected boolean place(Set<BlockPos> changedBlocks, IWorldGenerationReader worldIn, Random rand, BlockPos position, MutableBoundingBox boundsIn)
//	{
//		int height = rand.nextInt(3) + 5;
//
//		boolean flag = true;
//
//		if (position.getY() >= 1 && position.getY() + height + 1 <= worldIn.getMaxHeight())
//		{
//			for (int j = position.getY(); j <= position.getY() + 1 + height; ++j)
//			{
//				int k = 1;
//
//				if (j == position.getY())
//				{
//					k = 0;
//				}
//
//				if (j >= position.getY() + 1 + height - 2)
//				{
//					k = 2;
//				}
//
//				BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
//
//				for (int l = position.getX() - k; l <= position.getX() + k && flag; ++l)
//				{
//					for (int i1 = position.getZ() - k; i1 <= position.getZ() + k && flag; ++i1)
//					{
//						if (j >= 0 && j < worldIn.getMaxHeight())
//						{
//							if (!func_214587_a(worldIn, mutableBlockPos.setPos(l, j, i1))) //is replaceable
//							{
//								flag = false;
//							}
//						}
//						else
//						{
//							flag = false;
//						}
//					}
//				}
//			}
//
//			if (!flag)
//			{
//				return false;
//			}
//			else
//			{
//				if (isSoilOrWool(worldIn, position.down(), getSapling()) && position.getY() < worldIn.getMaxHeight() - height - 1)
//				{
//					setDirtAt(worldIn, position.down(), position);
//
//					for (int y = position.getY() - 3 + height; y <= position.getY() + height; ++y)
//					{
//						int yOffset = y - (position.getY() + height);
//						int l2 = 1 - yOffset / 2;
//
//						for (int x = position.getX() - l2; x <= position.getX() + l2; ++x)
//						{
//							int xOffset = x - position.getX();
//
//							for (int z = position.getZ() - l2; z <= position.getZ() + l2; ++z)
//							{
//								int zOffset = z - position.getZ();
//
//								if(Math.abs(xOffset) != l2 || Math.abs(zOffset) != l2 || rand.nextInt(2) != 0 && yOffset != 0)
//								{
//									BlockPos leafPos = new BlockPos(x, y, z);
//
//									if(isAirOrLeaves(worldIn, leafPos))
//									{
//										setLogState(changedBlocks, worldIn, leafPos, LEAF, boundsIn);
//									}
//								}
//							}
//						}
//					}
//
//					for (int yOffset = 0; yOffset < height; ++yOffset)
//					{
//						BlockPos upN = position.up(yOffset);
//						if(isAirOrLeaves(worldIn, upN))
//							setLogState(changedBlocks, worldIn, upN, LOG, boundsIn);
//					}
//
//					return true;
//				}
//				else
//				{
//					return false;
//				}
//			}
//		}
//		else
//		{
//			return false;
//		}
//	}
//
//	@Override
//	protected void func_214584_a(IWorldGenerationReader reader, BlockPos pos)
//	{
//	}
//
//	public static boolean isSoilOrWool(IWorldGenerationBaseReader reader, BlockPos pos, IPlantable sapling)
//	{
//		return isSoil(reader, pos, sapling) || reader.hasBlockState(pos, state -> BlockTags.WOOL.contains(state.getBlock()));
//	}
//}
