package com.mraof.minestuck.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.mraof.minestuck.Minestuck;

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
	
	public static void prepareAchievementPage()
	{
		achievementPage = new AchievementPage("Minestuck");
		AchievementPage.registerAchievementPage(achievementPage);
		getHammer = (Achievement) (new Achievement("achievement.getHammer", "getHammer", 0, -2, Minestuck.clawHammer, AchievementList.buildWorkBench)).registerStat();
		achievementPage.getAchievements().add(getHammer);
		mineCruxite = (Achievement) (new Achievement("achievement.mineCruxite", "mineCruxite", -2, 1, Minestuck.rawCruxite, AchievementList.buildPickaxe)).registerStat();
		achievementPage.getAchievements().add(mineCruxite);
		setupConnection = (Achievement) (new Achievement("achievement.setupConnection", "setupConnection", 0, 0, Minestuck.disk, mineCruxite)).registerStat();
		achievementPage.getAchievements().add(setupConnection);
		enterMedium = (Achievement) (new Achievement("achievement.enterMedium", "enterMedium", 2, 1, Minestuck.cruxiteApple, setupConnection)).registerStat();
		achievementPage.getAchievements().add(enterMedium);
		alchemy = (Achievement) (new Achievement("achievement.alchemy", "alchemy", 4, 0, new ItemStack(Minestuck.blockMachine,1,3), enterMedium)).registerStat();
		achievementPage.getAchievements().add(alchemy);
		goldSeeds = (Achievement) new Achievement("achievement.goldSeeds", "goldSeeds", -2, -2, Minestuck.goldSeeds, AchievementList.buildHoe).registerStat();
		achievementPage.getAchievements().add(goldSeeds);
		treeModus = (Achievement) new Achievement("achievement.treeModus", "treeModus", 2, -2, new ItemStack(Minestuck.modusCard, 1, 3), (Achievement)null).registerStat();
		achievementPage.getAchievements().add(treeModus);
		killOgre = (Achievement) new Achievement("achievement.killOgre", "killOgre", 1, 3, Minestuck.pogoHammer, enterMedium).registerStat();
		achievementPage.getAchievements().add(killOgre);
		killGiclops = (Achievement) new Achievement("achievement.killGiclops", "killGiclops", -1, 3, Minestuck.royalDeringer, killOgre).registerStat();
		achievementPage.getAchievements().add(killGiclops);
		broBlade = (Achievement) new Achievement("achievement.broBlade", "broBlade", 5, -2, Minestuck.katana, alchemy).registerStat();
		achievementPage.getAchievements().add(broBlade);
	}
	
	public static void onAlchemizedItem(ItemStack stack, EntityPlayer player)
	{
		if(!stack.getItem().equals(Minestuck.cruxiteApple))
			player.triggerAchievement(alchemy);
		if(stack.getItem().equals(Minestuck.clawHammer))
			player.triggerAchievement(getHammer);
		if(stack.getItem().equals(Minestuck.katana))
			player.triggerAchievement(broBlade);
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public void onItemPickupEvent(EntityItemPickupEvent event) {
		Item item = event.item.getEntityItem().getItem();
		if(item.equals(Minestuck.rawCruxite))
			event.entityPlayer.triggerAchievement(mineCruxite);
	}
	
}
