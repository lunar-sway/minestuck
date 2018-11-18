package com.mraof.minestuck.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import scala.util.Random;

public class ItemSurpriseEmbryo extends ItemFood {

	public ItemSurpriseEmbryo(int amount, float saturation, boolean isWolfFood) {
		super(amount, saturation, isWolfFood);
		this.setCreativeTab(TabMinestuck.instance);
		this.setUnlocalizedName("surprise_embryo");
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
    {
        if (entityLiving instanceof EntityPlayer)
        {
            EntityPlayer entityplayer = (EntityPlayer)entityLiving;
            entityplayer.getFoodStats().addStats(this, stack);
            worldIn.playSound((EntityPlayer)null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
            this.onFoodEaten(stack, worldIn, entityplayer);
            entityplayer.addStat(StatList.getObjectUseStats(this));
            Random ran = new Random();
            int coinflip = ran.nextInt(2);
            int num = ran.nextInt(3);
            if(coinflip == 0) {
            	Item[]  items = new Item[] {Items.MELON, 
                        Items.STICK, 
                        Items.EGG};
            	String[]  itemsString = new String[] {"Melon", 
                        "Stick", 
                        "Egg"};
            	if(!entityplayer.world.isRemote) {
            		entityplayer.inventory.addItemStackToInventory(new ItemStack(items[num], 1));
            		ITextComponent message = new TextComponentTranslation("WOW! NO WAY! You found a " + itemsString[num] +" inside your Surprise Embryo!");       
            		message.getStyle().setColor(TextFormatting.GOLD);
            		entityplayer.sendMessage(message);
            	}
            } else {
            	Block[]  blocks = new Block[] {Blocks.DIRT,
                    Blocks.PUMPKIN,
                    Blocks.COBBLESTONE};
            	if(!entityplayer.world.isRemote) {
            		entityplayer.inventory.addItemStackToInventory(new ItemStack(blocks[num], 1));
            		ITextComponent message = new TextComponentTranslation("WOW! NO WAY! You found a " + blocks[num].getLocalizedName() + " inside your Surprise Embryo!");      
            		message.getStyle().setColor(TextFormatting.GOLD);
            		entityplayer.sendMessage(message);
            	}
            }
            if (entityplayer instanceof EntityPlayerMP)
            {
                CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP)entityplayer, stack);
            }
        }

        stack.shrink(1);
        return stack;
    }
}
