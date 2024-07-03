package com.mraof.minestuck.skaianet;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public record LandChain(List<ResourceKey<Level>> lands, boolean isLoop)
{
	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeBoolean(this.isLoop);
		buffer.writeCollection(this.lands, FriendlyByteBuf::writeResourceKey);
	}
	
	public static LandChain read(FriendlyByteBuf buffer)
	{
		boolean isLoop = buffer.readBoolean();
		List<ResourceKey<Level>> lands = buffer.readList(buffer1 -> buffer1.readResourceKey(Registries.DIMENSION));
		return new LandChain(lands, isLoop);
	}
	
	/**
	 * Returns all lands in this chain with angles attached.
	 * This angle is the angle between that land and the given "source land" as seen from skaia.
	 * If this chain is a loop, all lands will be evenly spaced out.
	 * If this chain is still open, there will be an empty spot where the chain is open.
	 */
	public Stream<AngledLand> relativeAngledLands(ResourceKey<Level> sourceLand)
	{
		int positions = this.lands().size() + (this.isLoop() ? 0 : 1);
		int sourceIndex = this.lands().indexOf(sourceLand);
		float sourceAngle = Mth.TWO_PI * sourceIndex / (float) positions;
		return IntStream.range(0, this.lands().size())
				.mapToObj(landIndex -> new AngledLand(this.lands().get(landIndex), angleBetween(sourceAngle, Mth.TWO_PI * landIndex / (float) positions)));
	}
	
	public record AngledLand(ResourceKey<Level> landId, float landToLandAngle)
	{
		public boolean isZeroAngle()
		{
			return this.landToLandAngle == 0F;
		}
		
		public boolean isOppositeAngle()
		{
			return this.landToLandAngle == Mth.PI;
		}
		
		/**
		 * Returns the angle between skaia and this land as seen from the land at skaia angle 0.
		 */
		public float skaiaToLandAngle()
		{
			return (Mth.PI - this.landToLandAngle)/2;
		}
	}
	
	private static float angleBetween(float fromAngle, float toAngle)
	{
		float angle = toAngle - fromAngle;
		if(angle < 0)
			return angle + Mth.TWO_PI;
		else
			return angle;
	}
}
