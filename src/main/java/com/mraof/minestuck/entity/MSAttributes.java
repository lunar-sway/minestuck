package com.mraof.minestuck.entity;

import com.mraof.minestuck.Minestuck;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.PercentageAttribute;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = Minestuck.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class MSAttributes
{
	public static final DeferredRegister<Attribute> REGISTER = DeferredRegister.create(Registries.ATTRIBUTE, Minestuck.MOD_ID);
	
	public static final Holder<Attribute> UNDERLING_DAMAGE_MODIFIER = REGISTER.register("player.underling_damage_modifier", () -> new RangedAttribute("attribute.name.player.underling_damage_modifier", 1.0, -2048.0, 2048.0).setSyncable(true).setSentiment(Attribute.Sentiment.POSITIVE));
	public static final Holder<Attribute> UNDERLING_PROTECTION_MODIFIER = REGISTER.register("player.underling_protection_modifier", () -> new PercentageAttribute("attribute.name.player.underling_protection_modifier", 1.0, 0.0, 1.0).setSyncable(true).setSentiment(Attribute.Sentiment.POSITIVE));
	
	@SubscribeEvent
	public static void modifyEntityAttributes(EntityAttributeModificationEvent event)
	{
		event.getTypes().stream().filter(entityType -> entityType == EntityType.PLAYER).forEach(entity ->
				REGISTER.getEntries().forEach(attribute ->
						event.add(entity, attribute))
		);
	}
}
