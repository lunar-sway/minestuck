package com.mraof.minestuck.entity.underling;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.alchemy.GristHelper;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.event.UnderlingSpawnListEvent;
import com.mraof.minestuck.world.lands.GristLayerInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.neoforged.neoforge.common.NeoForge;

import java.util.ArrayList;
import java.util.List;

/**
 * A facade for overlooking features surrounding underlings that does or might make use of properties connected to the land or session that the underling is spawned in.
 * Currently generates grist types for underlings and determines the underling spawn weights based on the distance from spawn.
 * Will probably take on generation of prototyping properties once we have that.
 */
public final class UnderlingSpawnSettings
{
	public static GristType getUnderlingType(UnderlingEntity entity)
	{
		return GristLayerInfo.get((ServerLevel) entity.level())
				.map(info -> info.randomTypeFor(entity))
				.orElseGet(() -> GristHelper.getPrimaryGrist(entity.getRandom()));
	}
	
	private static final WeightedRandomList<MobSpawnSettings.SpawnerData>[] difficultyList = new WeightedRandomList[31];
	
	public static WeightedRandomList<MobSpawnSettings.SpawnerData> getUnderlingList(BlockPos pos)
	{
		
		BlockPos spawn = new BlockPos(0, 0, 0);
		
		int difficulty = (int) Math.round(Math.sqrt(new Vec3i(pos.getX() >> 4, 0, pos.getZ() >> 4).distSqr(new Vec3i(spawn.getX() >> 4, 0, spawn.getZ() >> 4))));
		difficulty = Math.min(30, difficulty / 3);
		
		if (difficultyList[difficulty] != null) {
			return difficultyList[difficulty];
		}
		
		List<MobSpawnSettings.SpawnerData> list = new ArrayList<>();
		
		//for difficulty values: 0,10,20,30
		int impWeight = difficulty < 8 ? difficulty + 1 : 8 - (difficulty - 8) / 3; //1,7,4,0
		int ogreWeight = difficulty < 20 ? (difficulty - 5) / 3 : 5 - (difficulty - 20) / 3; //<0,1,5,1
		int basiliskWeight = (difficulty >= 16 && difficulty < 26) ? (difficulty - 14) / 2 : (difficulty >= 26) ? 6 : 0; //0,0,3,6
		int lichWeight = difficulty >= 28 ? 6 : difficulty >= 16 ? (difficulty - 12) / 3 : 0; //0,0,2,6
		int giclopsWeight = (difficulty >= 20 && difficulty < 30) ? (difficulty - 17) / 3 : difficulty >= 30 ? 4 : 0; //0,0,1,5
		
		if(impWeight > 0 && MinestuckConfig.SERVER.naturalImpSpawn.get())
			list.add(new MobSpawnSettings.SpawnerData(MSEntityTypes.IMP.get(), impWeight, Math.max(1, (int) (impWeight / 2.5)), Math.min(5, Math.max(3, impWeight))));
		if(ogreWeight > 0 && MinestuckConfig.SERVER.naturalOgreSpawn.get())
			list.add(new MobSpawnSettings.SpawnerData(MSEntityTypes.OGRE.get(), ogreWeight, ogreWeight >= 5 ? 2 : 1, Math.min(5, Math.max(1, ogreWeight / 2))));
		if(basiliskWeight > 0 && MinestuckConfig.SERVER.naturalBasiliskSpawn.get())
			list.add(new MobSpawnSettings.SpawnerData(MSEntityTypes.BASILISK.get(), basiliskWeight, 1, Math.min(5, Math.max(1, basiliskWeight / 2))));
		if(lichWeight > 0 && MinestuckConfig.SERVER.naturalLichSpawn.get())
			list.add(new MobSpawnSettings.SpawnerData(MSEntityTypes.LICH.get(), lichWeight, 1, Math.min(5, Math.max(1, lichWeight / 2))));
		if(giclopsWeight > 0 && !MinestuckConfig.SERVER.disableGiclops.get())
			list.add(new MobSpawnSettings.SpawnerData(MSEntityTypes.GICLOPS.get(), giclopsWeight, 1, Math.min(5, Math.max(1, giclopsWeight / 2))));
		
		NeoForge.EVENT_BUS.post(new UnderlingSpawnListEvent(difficulty, list));
		
		return difficultyList[difficulty] = WeightedRandomList.create(list);
	}
}