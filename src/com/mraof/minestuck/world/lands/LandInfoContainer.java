package com.mraof.minestuck.world.lands;

import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.IdentifierHandler;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.dimension.DimensionType;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Container for land dimension information between info being loaded,
 * and the dimensions actually being registered.
 */
public class LandInfoContainer
{
	public final IdentifierHandler.PlayerIdentifier identifier;
	public final LandAspects landAspects;
	public final DimensionType dimensionType;
	private final boolean useReverseOrder;
	private final int terrainNameIndex, titleNameIndex;
	@Nullable
	private BlockPos gatePos = null;
	
	public LandInfoContainer(IdentifierHandler.PlayerIdentifier identifier, LandAspects landAspects, DimensionType dimensionType, Random random)
	{
		this.identifier = identifier;
		this.landAspects = landAspects;
		this.dimensionType = dimensionType;
		useReverseOrder = random.nextBoolean();
		terrainNameIndex = random.nextInt(landAspects.terrain.getNames().length);
		titleNameIndex = random.nextInt(landAspects.title.getNames().length);
	}
	
	private LandInfoContainer(SkaianetHandler handler, IdentifierHandler.PlayerIdentifier identifier, LandAspects landAspects, DimensionType dimensionType, boolean reverseOrder, int terrainNameIndex, int titleNameIndex)
	{
		this.identifier = identifier;
		this.landAspects = landAspects;
		this.dimensionType = dimensionType;
		useReverseOrder = reverseOrder;
		this.terrainNameIndex = terrainNameIndex % landAspects.terrain.getNames().length;
		this.titleNameIndex = titleNameIndex % landAspects.title.getNames().length;
	}
	
	public ITextComponent landAsTextComponent()
	{
		ITextComponent aspect1 = new TranslationTextComponent("land."+landName1());
		ITextComponent aspect2 = new TranslationTextComponent("land."+landName2());
		return new TranslationTextComponent("land.format", aspect1, aspect2);
	}
	
	public String landName1()
	{
		if(!useReverseOrder)
			return landAspects.terrain.getNames()[terrainNameIndex];
		else return landAspects.title.getNames()[titleNameIndex];
	}
	
	public String landName2()
	{
		if(useReverseOrder)
			return landAspects.terrain.getNames()[terrainNameIndex];
		else return landAspects.title.getNames()[titleNameIndex];
	}
	
	@Nullable
	public BlockPos getGatePos()
	{
		return gatePos;
	}
	
	public void setGatePos(BlockPos pos)
	{
		gatePos = pos;
	}
	
	/**
	 * Saves the info container to nbt, except for the identifier
	 */
	public CompoundNBT write(CompoundNBT nbt)
	{
		landAspects.write(nbt);
		nbt.putString("dim_type", dimensionType.getRegistryName().toString());
		nbt.putBoolean("reverse_order", useReverseOrder);
		nbt.putInt("terrain_name_index", terrainNameIndex);
		nbt.putInt("title_name_index", titleNameIndex);
		if(gatePos != null)
		{
			nbt.putInt("gate_x", gatePos.getX());
			nbt.putInt("gate_y", gatePos.getY());
			nbt.putInt("gate_z", gatePos.getZ());
		}
		return nbt;
	}
	
	public static LandInfoContainer read(CompoundNBT nbt, SkaianetHandler handler, IdentifierHandler.PlayerIdentifier identifier)
	{
		LandAspects aspects = LandAspects.read(nbt);
		ResourceLocation dimName = new ResourceLocation(nbt.getString("dim_type"));
		DimensionType type = DimensionType.byName(dimName);
		boolean reverse = nbt.getBoolean("reverse_order");
		int terrainIndex = nbt.getInt("terrain_name_index");
		int titleIndex = nbt.getInt("title_name_index");
		
		LandInfoContainer info = new LandInfoContainer(handler, identifier, aspects, type, reverse, terrainIndex, titleIndex);
		
		if(nbt.contains("gate_x", 99))
		{
			info.gatePos = new BlockPos(nbt.getInt("gate_x"), nbt.getInt("gate_y"), nbt.getInt("gate_z"));
		}
		
		return info;
	}
}