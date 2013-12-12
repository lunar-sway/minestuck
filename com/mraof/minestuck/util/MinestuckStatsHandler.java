package com.mraof.minestuck.util;

import com.mraof.minestuck.Minestuck;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

public class MinestuckStatsHandler {
	
	public static MinestuckStatsHandler instance = new MinestuckStatsHandler();
	
	public static int idOffset;
	
	public static AchievementPage achievementPage;
	
	public static Achievement getHammer;
	public static Achievement mineCruxite;
	public static Achievement setupConnection;
	public static Achievement enterMedium;
	public static Achievement alchemy;
	
	public static void prepareAchievementPage() {
		achievementPage = new AchievementPage("Minestuck");
		AchievementPage.registerAchievementPage(achievementPage);
		getHammer = (new Achievement(idOffset, "getHammer", 0, -2, new ItemStack(Minestuck.clawHammerId+256,1,0), (Achievement)null)).registerAchievement().setIndependent();
		achievementPage.getAchievements().add(getHammer);
		mineCruxite = (new Achievement(idOffset+1, "mineCruxite", -2, 1, new ItemStack(Minestuck.rawCruxiteId+256,1,0), (Achievement)null)).registerAchievement();//.setIndependent();
		achievementPage.getAchievements().add(mineCruxite);
		setupConnection = (new Achievement(idOffset+2, "setupConnection", 0, 0, new ItemStack(Minestuck.diskId+256,1,0), mineCruxite)).registerAchievement();
		achievementPage.getAchievements().add(setupConnection);
		enterMedium = (new Achievement(idOffset+3, "enterMedium", 2, 1, new ItemStack(Minestuck.cruxiteArtifactId+256,1,0), setupConnection)).registerAchievement();
		achievementPage.getAchievements().add(enterMedium);
		alchemy = (new Achievement(idOffset+4, "alchemy", 4, 0, new ItemStack(Minestuck.blockMachineId,1,3), enterMedium)).registerAchievement();
		achievementPage.getAchievements().add(alchemy);
	}
	
	public static void onAlchemizedItem(ItemStack stack, EntityPlayer player) {
		if(!stack.getItem().equals(Minestuck.cruxiteArtifact))
			player.triggerAchievement(alchemy);
		if(stack.getItem().equals(Minestuck.clawHammer))
			player.triggerAchievement(getHammer);
	}
	
	@ForgeSubscribe
	public void onItemPickupEvent(EntityItemPickupEvent event) {
		Item item = event.item.getEntityItem().getItem();
		Debug.print(item.equals(Minestuck.rawCruxite)+" pickup event, item:"+item.getUnlocalizedName());
		if(item.equals(Minestuck.rawCruxite))
			event.entityPlayer.triggerAchievement(mineCruxite);
	}
	
}
