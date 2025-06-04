package com.mraof.minestuck.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;

public class MSItemProperties extends Item.Properties {
    private boolean durability_set;

    @Override
    public Properties durability(int maxDamage) {
        if (!durability_set) {
            super.durability(maxDamage);
            durability_set = true;
        }
        return this;
    }
}
