package com.mraof.minestuck.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.item.ItemCruxiteArtifact;
import com.mraof.minestuck.item.MinestuckItems;

public class MinestuckAchievementHandler {
	
	public static MinestuckAchievementHandler instance = new MinestuckAchievementHandler();
	
	public static int idOffset;
	
	public static AchievementPage achievementPage;
	
	public static Achievement getHammer;
	public static Achievement mineCruxite;
	public static Achievement setupConnection;
	public static Achievement enterMedium;
	public static Achievement alchemy;
	public static Achievement goldSeeds;
	public static Achievement treeModus;
	public static Achievement killOgre;
	public static Achievement killGiclops;
	public static Achievement broBlade;
	public static Achievement returnNode;
	
	public static void prepareAchievementPage()
	{
		achievementPage = new AchievementPage("Minestuck");
		AchievementPage.registerAchievementPage(achievementPage);
		getHammer = (Achievement) (new Achievement("achievement.getHammer", "getHammer", 0, -2, MinestuckItems.clawHammer, AchievementList.buildWorkBench)).registerStat();
		achievementPage.getAchievements().add(getHammer);
		mineCruxite = (Achievement) (new Achievement("achievement.mineCruxite", "mineCruxite", -2, 1, MinestuckItems.rawCruxite, AchievementList.buildPickaxe)).registerStat();
		achievementPage.getAchievements().add(mineCruxite);
		setupConnection = (Achievement) (new Achievement("achievement.setupConnection", "setupConnection", 0, 0, MinestuckItems.disk, mineCruxite)).registerStat();
		achievementPage.getAchievements().add(setupConnection);
		enterMedium = (Achievement) (new Achievement("achievement.enterMedium", "enterMedium", 2, 1, MinestuckItems.cruxiteApple, setupConnection)).registerStat();
		achievementPage.getAchievements().add(enterMedium);
		alchemy = (Achievement) (new Achievement("achievement.alchemy", "alchemy", 4, 0, new ItemStack(MinestuckBlocks.sburbMachine, 1, 3), enterMedium)).registerStat();
		achievementPage.getAchievements().add(alchemy);
		goldSeeds = (Achievement) new Achievement("achievement.goldSeeds", "goldSeeds", -2, -2, MinestuckItems.goldSeeds, AchievementList.buildHoe).registerStat();
		achievementPage.getAchievements().add(goldSeeds);
		treeModus = (Achievement) new Achievement("achievement.treeModus", "treeModus", 2, -2, new ItemStack(MinestuckItems.modusCard, 1, 3), (Achievement)null).registerStat();
		achievementPage.getAchievements().add(treeModus);
		killOgre = (Achievement) new Achievement("achievement.killOgre", "killOgre", 1, 3, MinestuckItems.pogoHammer, enterMedium).registerStat();
		achievementPage.getAchievements().add(killOgre);
		killGiclops = (Achievement) new Achievement("achievement.killGiclops", "killGiclops", -1, 3, MinestuckItems.royalDeringer, killOgre).registerStat();
		achievementPage.getAchievements().add(killGiclops);
		broBlade = (Achievement) new Achievement("achievement.broBlade", "broBlade", 5, -2, MinestuckItems.katana, alchemy).registerStat();
		achievementPage.getAchievements().add(broBlade);
		returnNode = (Achievement) new Achievement("achievement.returnNode", "returnNode", 4, 2, Items.bed, enterMedium).registerStat();
		achievementPage.getAchievements().add(returnNode);
	}
	
	public static void onAlchemizedItem(ItemStack stack, EntityPlayer player)
	{
		if(!(stack.getItem() instanceof ItemCruxiteArtifact))
		{
			player.addStat(alchemy);
			Echeladder e = MinestuckPlayerData.getData(player).echeladder;
			e.checkBonus(Echeladder.ALCHEMY_BONUS_OFFSET);
		}
		if(stack.getItem().equals(MinestuckItems.clawHammer))
			player.addStat(getHammer);
		if(stack.getItem().equals(MinestuckItems.katana))
			player.addStat(broBlade);
		GristSet set = GristRegistry.getGristConversion(stack);
		if(set != null) //The only time the grist set should be null here is if it was a captchalouge card that was alchemized
		{
			double value = 0;
			for(GristType type : GristType.values())
			{
				int v = set.getGrist(type);
				float f = type == GristType.Build || type == GristType.Artifact ? 0.5F : type == GristType.Zillium ? 20 : type.getPower();
				if(v > 0)
					value += f*v/2;
			}
			
			Echeladder e = MinestuckPlayerData.getData(player).echeladder;
			if(value >= 50)
				e.checkBonus((byte) (Echeladder.ALCHEMY_BONUS_OFFSET + 1));
			if(value >= 500)
				e.checkBonus((byte) (Echeladder.ALCHEMY_BONUS_OFFSET + 2));
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public void onItemPickupEvent(EntityItemPickupEvent event)
	{
		Item item = event.getItem().getEntityItem().getItem();
		if(item.equals(MinestuckItems.rawCruxite))
			event.getEntityPlayer().addStat(mineCruxite);
	}
	
}
