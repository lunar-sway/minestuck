package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.event.UnderlingSpawnListEvent;
import com.mraof.minestuck.item.crafting.alchemy.GristHelper;
import com.mraof.minestuck.item.crafting.alchemy.GristType;
import com.mraof.minestuck.world.lands.GristLayerInfo;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.List;

/**
 * A facade for overlooking features surrounding underlings that does or might make use of properties connected to the land or session that the underling is spawned in.
 * Currently generates grist types for underlings and determines the underling spawn weights based on the distance from spawn.
 * Will probably take on generation of prototyping properties once we have that.
 */
public final class UnderlingController
{
	public static GristType getUnderlingType(UnderlingEntity entity)
	{
		return GristLayerInfo.get(entity.level.dimension())
				.map(info -> info.randomTypeFor(entity))
				.orElseGet(() -> GristHelper.getPrimaryGrist(entity.getRandom()));
	}
	
	private static final List<MobSpawnInfo.Spawners>[] difficultyList = new List[31];
	
	public static List<MobSpawnInfo.Spawners> getUnderlingList(BlockPos pos)
	{
		
		BlockPos spawn = new BlockPos(0, 0, 0);//world.getSpawnPoint(); TODO
		
		int difficulty = (int) Math.round(Math.sqrt(new Vector3i(pos.getX() >> 4, 0, pos.getZ() >> 4).distSqr(new Vector3i(spawn.getX() >> 4, 0, spawn.getZ() >> 4))));
		
		difficulty = Math.min(30, difficulty/3);
		
		if(difficultyList[difficulty] != null)
			return difficultyList[difficulty];
		
		ArrayList<MobSpawnInfo.Spawners> list = new ArrayList<>();
		
		int impWeight, ogreWeight = 0, basiliskWeight = 0, lichWeight = 0, giclopsWeight = 0;
		
		if(difficulty < 8)
			impWeight = difficulty + 1;
		else
		{
			impWeight = 8 - (difficulty - 8)/3;
			if(difficulty < 20)
				ogreWeight = (difficulty - 5)/3;
			else ogreWeight = 5 - (difficulty - 20)/3;
			
			if(difficulty >= 16)
			{
				if(difficulty < 26)
					basiliskWeight = (difficulty - 14)/2;
				else basiliskWeight = 6;
				if(difficulty < 28)
					lichWeight = (difficulty - 12)/3;
				else lichWeight = 6;
				if(difficulty >= 20)
					if(difficulty < 30)
						giclopsWeight = (difficulty - 17)/3;
					else giclopsWeight = 5;
			}
		}
		
		if(impWeight > 0 && MinestuckConfig.SERVER.naturalImpSpawn.get())
			list.add(new MobSpawnInfo.Spawners(MSEntityTypes.IMP, impWeight, Math.max(1, (int)(impWeight/2.5)), Math.max(3, impWeight)));
		if(ogreWeight > 0 && MinestuckConfig.SERVER.naturalOgreSpawn.get())
			list.add(new MobSpawnInfo.Spawners(MSEntityTypes.OGRE, ogreWeight, ogreWeight >= 5 ? 2 : 1, Math.max(1, ogreWeight/2)));
		if(basiliskWeight > 0 && MinestuckConfig.SERVER.naturalBasiliskSpawn.get())
			list.add(new MobSpawnInfo.Spawners(MSEntityTypes.BASILISK, basiliskWeight, 1, Math.max(1, basiliskWeight/2)));
		if(lichWeight > 0 && MinestuckConfig.SERVER.naturalLichSpawn.get())
			list.add(new MobSpawnInfo.Spawners(MSEntityTypes.LICH, lichWeight, 1, Math.max(1, lichWeight/2)));
		if(giclopsWeight > 0 && !MinestuckConfig.SERVER.disableGiclops.get())
			list.add(new MobSpawnInfo.Spawners(MSEntityTypes.GICLOPS, giclopsWeight, 1, Math.max(1, giclopsWeight/2)));
		
		MinecraftForge.EVENT_BUS.post(new UnderlingSpawnListEvent(difficulty, list));
		
		difficultyList[difficulty] = list;
		
		return list;
	}
}