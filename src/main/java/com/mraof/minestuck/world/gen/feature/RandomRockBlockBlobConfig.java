package com.mraof.minestuck.world.gen.feature;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.world.gen.feature.IFeatureConfig;

public class RandomRockBlockBlobConfig implements IFeatureConfig
{
   public final int startRadius;

   public RandomRockBlockBlobConfig(int startRadius) {
      this.startRadius = startRadius;
   }

   public <T> Dynamic<T> serialize(DynamicOps<T> ops) {
      return new Dynamic<>(ops, ops.createMap(ImmutableMap.of(ops.createString("start_radius"), ops.createInt(this.startRadius))));
   }

   public static <T> RandomRockBlockBlobConfig deserialize(Dynamic<T> p_214682_0_) {
      int i = p_214682_0_.get("start_radius").asInt(0);
      return new RandomRockBlockBlobConfig(i);
   }
}
