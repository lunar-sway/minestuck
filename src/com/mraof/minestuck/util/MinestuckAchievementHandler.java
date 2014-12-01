package com.mraof.minestuck.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
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
	
	public static void prepareAchievementPage()
	{
		achievementPage = new AchievementPage("Minestuck");
		AchievementPage.registerAchievementPage(achievementPage);
		getHammer = (Achievement) (new Achievement("achievement.getHammer", "getHammer", 0, -2, new ItemStack(Minestuck.clawHammer,1,0), (Achievement)null)).registerStat().initIndependentStat();
		achievementPage.getAchievements().add(getHammer);
		mineCruxite = (Achievement) (new Achievement("achievement.mineCruxite", "mineCruxite", -2, 1, new ItemStack(Minestuck.rawCruxite,1,0), (Achievement)null)).registerStat();//.setIndependent();
		achievementPage.getAchievements().add(mineCruxite);
		setupConnection = (Achievement) (new Achievement("achievement.setupConnection", "setupConnection", 0, 0, new ItemStack(Minestuck.disk,1,0), mineCruxite)).registerStat();
		achievementPage.getAchievements().add(setupConnection);
		enterMedium = (Achievement) (new Achievement("achievement.enterMedium", "enterMedium", 2, 1, new ItemStack(Minestuck.cruxiteArtifact,1,0), setupConnection)).registerStat();
		achievementPage.getAchievements().add(enterMedium);
		alchemy = (Achievement) (new Achievement("achievement.alchemy", "alchemy", 4, 0, new ItemStack(Minestuck.blockMachine,1,3), enterMedium)).registerStat();
		achievementPage.getAchievements().add(alchemy);
	}
	
	public static void onAlchemizedItem(ItemStack stack, EntityPlayer player) {
		if(!stack.getItem().equals(Minestuck.cruxiteArtifact))
			player.triggerAchievement(alchemy);
		if(stack.getItem().equals(Minestuck.clawHammer))
			player.triggerAchievement(getHammer);
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public void onItemPickupEvent(EntityItemPickupEvent event) {
		Item item = event.item.getEntityItem().getItem();
		if(item.equals(Minestuck.rawCruxite))
			event.entityPlayer.triggerAchievement(mineCruxite);
	}
	
}
