package com.mraof.minestuck.block;

import com.mraof.minestuck.data.loot_table.MSChestLootTables;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.entity.item.GristEntity;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.crafting.alchemy.GristAmount;
import com.mraof.minestuck.particles.ParticleTypes;
import com.mraof.minestuck.util.CustomVoxelShape;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.storage.loot.MSLootTables;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.*;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class LootBlock extends Block
{
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public final Map<Direction, VoxelShape> shape;

    public LootBlock(Properties properties, CustomVoxelShape shape)
    {
        super(properties);
        this.shape = shape.createRotatedShapes();
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        ItemStack itemstack = player.getHeldItem(handIn);
        if (worldIn.isRemote) {
            return true;
        } else {
            if (itemstack.isEmpty()){
                Direction direction = hit.getFace();
                Direction direction1 = direction.getAxis() == Direction.Axis.Y ? player.getHorizontalFacing().getOpposite() : direction; //gets the direction of the block face you hit
                worldIn.playSound(null, player.posX, player.posY, player.posZ, MSSoundEvents.BLOCK_LOOTBLOCK_OPEN, SoundCategory.BLOCKS, 1F, 1F);
                if (getBlock() == MSBlocks.LOOT_CHEST){
                    worldIn.setBlockState(pos, MSBlocks.EMPTY_LOOT_CHEST.getDefaultState().with(DecorBlock.FACING, direction1), 11);

                    LootContext.Builder contextBuilder = new LootContext.Builder((ServerWorld) player.world).withParameter(LootParameters.POSITION, new BlockPos(pos)).withLuck(player.getLuck()).withParameter(LootParameters.THIS_ENTITY, player);

                    List<ItemStack> lootTable = player.getServer().getLootTableManager().getLootTableFromLocation(MSLootTables.BASIC_MEDIUM_CHEST).generate(contextBuilder.build(LootParameterSets.CHEST));
                    //List<ItemStack> loot = player.getServer().getLootTableManager().getLootTableFromLocation(MSLootTables.BASIC_MEDIUM_CHEST).generate(contextBuilder, loot);


                    //LootTable table = this.world.getLootTableManager().getLootTableFromLocation(MSLootTables.BASIC_MEDIUM_CHEST).generate(contextBuilder, Consumer<ItemStack>); // /assets/minestuck/loot_tables/chests/medium_basic.json?
                    //LootTable table = this.world.getLootTableManager().getLootTableFromLocation(new ResourceLocation("minestuck:chests/medium_chest")).generate(contextBuilder, Consumer<ItemStack>); // /assets/minestuck/loot_tables/chests/medium_basic.json?

                    //LootTable table = this.world.getLootTableManager().getLootTableFromLocation(new ResourceLocation("minestuck:medium_chest")); // /assets/minestuck/loot_tables/chests/medium_basic.json?
                    //LootContext ctx = new LootContext.Builder((ServerWorld) player.world).withLuck().build();
                    //List<ItemStack> stacks = table.generateLootForPools(world.rand, ctx);
                    //table.fillInventory(iinventory, world.rand, ctx);

                    //Empty chest should face direction of original block, not player oriented
                    //Item = new ItemEntity(worldIn, (double)pos.getX() + 0.5D + (double)direction1.getXOffset() * 0.65D, (double)pos.getY() + 0.1D, (double)pos.getZ() + 0.5D + (double)direction1.getZOffset() * 0.65D, new ItemStack(MSItems.BOONDOLLARS, 1));
                }
                if (getBlock() == MSBlocks.WOODEN_LOOT_CHEST){
                    //worldIn.setBlockState(pos, MSBlocks.WOODEN_LOOT_CHEST_EMPTY.getDefaultState().with(DecorBlock.FACING, direction1), 11);
                    ItemEntity itementity = new ItemEntity(worldIn, (double)pos.getX() + 0.5D + (double)direction1.getXOffset() * 0.65D, (double)pos.getY() + 0.1D, (double)pos.getZ() + 0.5D + (double)direction1.getZOffset() * 0.65D, new ItemStack(MSItems.BOONDOLLARS, 1));
                    itementity.setMotion(0.05D * (double)direction1.getXOffset() + worldIn.rand.nextDouble() * 0.02D, 0.05D, 0.05D * (double)direction1.getZOffset() + worldIn.rand.nextDouble() * 0.02D);
                    worldIn.addEntity(itementity);
                    //include empty wooden loot chest instead, what is the nbt for larger boondollars
                }
                if (getBlock() == MSBlocks.CALEDFWLCH_PEDESTAL){
                    worldIn.setBlockState(pos, MSBlocks.EMPTY_CALEDFWLCH_PEDESTAL.getDefaultState(), 11);
                    ItemEntity itementity = new ItemEntity(worldIn, (double)pos.getX() + 0.5D + (double)direction1.getXOffset() * 0.65D, (double)pos.getY() + 0.1D, (double)pos.getZ() + 0.5D + (double)direction1.getZOffset() * 0.65D, new ItemStack(MSItems.CALEDFWLCH, 1));
                    itementity.setMotion(0.05D * (double)direction1.getXOffset() + worldIn.rand.nextDouble() * 0.02D, 0.05D, 0.05D * (double)direction1.getZOffset() + worldIn.rand.nextDouble() * 0.02D);
                    worldIn.addEntity(itementity);

                    player.sendMessage(new TranslationTextComponent("You have claimed the legendary sword Caledfwlch!... more like a legendary piece of shit."));
                }
            }
            //on right clicking a block in a players range(onBlockActivated), an item should enter the players empty
            //hand accompanied by a block change and music, may also try and send a message of what loot they got
            return true;
        }
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }


    @Override
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos)
    {
        return Block.doesSideFillSquare(state.getCollisionShape(worldIn, pos.down()), Direction.UP);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return shape.get(state.get(FACING));
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState rotate(BlockState state, Rotation rotation)
    {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState mirror(BlockState state, Mirror mirrorIn)
    {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }
}
