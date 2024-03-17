package com.mraof.minestuck.entity.dialogue;

import com.mojang.datafixers.util.Pair;
import com.mraof.minestuck.network.DialogueScreenPacket;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.*;

public final class DialogueComponent
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private final LivingEntity entity;
	@Nullable
	private ResourceLocation activeDialogue;
	private boolean keepOnReset;
	private boolean hasGeneratedOnce = false;
	private final Map<PlayerIdentifier, Set<String>> playerSpecificFlags = new HashMap<>();
	
	public DialogueComponent(LivingEntity entity)
	{
		this.entity = entity;
	}
	
	public void read(CompoundTag tag)
	{
		if(tag.contains("dialogue_id", CompoundTag.TAG_STRING))
		{
			this.activeDialogue = ResourceLocation.tryParse(tag.getString("dialogue_id"));
			this.keepOnReset = tag.getBoolean("keep_on_reset");
			this.hasGeneratedOnce = true;
			this.playerSpecificFlags.clear();
			tag.getList("player_flags", Tag.TAG_COMPOUND).stream().map(CompoundTag.class::cast).forEach(entryTag -> {
				
				PlayerIdentifier player = IdentifierHandler.load(entryTag, "player");
				Set<String> flags = this.playerFlags(player);
				entryTag.getList("flags", Tag.TAG_STRING).stream().map(StringTag.class::cast)
						.map(StringTag::getAsString).forEach(flags::add);
			});
		}
		else
			this.hasGeneratedOnce = tag.getBoolean("has_generated");
	}
	
	public CompoundTag write()
	{
		CompoundTag tag = new CompoundTag();
		if(this.activeDialogue != null)
		{
			tag.putString("dialogue_id", this.activeDialogue.toString());
			tag.putBoolean("keep_on_reset", this.keepOnReset);
			ListTag playerFlagsTag = new ListTag();
			this.playerSpecificFlags.forEach((player, flags) -> {
				CompoundTag entryTag = new CompoundTag();
				player.saveToNBT(entryTag, "player");
				ListTag flagsTag = new ListTag();
				flags.stream().map(StringTag::valueOf).forEach(flagsTag::add);
				entryTag.put("flags", flagsTag);
				playerFlagsTag.add(entryTag);
			});
			tag.put("player_flags", playerFlagsTag);
		}
		
		tag.putBoolean("has_generated", this.hasGeneratedOnce);
		
		return tag;
	}
	
	public boolean hasGeneratedOnce()
	{
		return hasGeneratedOnce;
	}
	
	public void setDialogue(Dialogue.SelectableDialogue selectable)
	{
		this.setDialogue(selectable.dialogueId(), selectable.keepOnReset());
	}
	
	public void setDialogue(ResourceLocation dialogueId, boolean keepOnReset)
	{
		this.hasGeneratedOnce = true;
		this.activeDialogue = dialogueId;
		this.keepOnReset = keepOnReset;
	}
	
	public Optional<ResourceLocation> getActiveDialogue()
	{
		return Optional.ofNullable(this.activeDialogue);
	}
	
	public boolean hasActiveDialogue()
	{
		return this.activeDialogue != null;
	}
	
	public Set<String> playerFlags(PlayerIdentifier player)
	{
		return this.playerSpecificFlags.computeIfAbsent(player, _player -> new HashSet<>());
	}
	
	public void resetDialogue()
	{
		if(!this.keepOnReset)
			this.activeDialogue = null;
		this.playerSpecificFlags.clear();
	}
	
	public void tryStartDialogue(ServerPlayer serverPlayer)
	{
		if(this.activeDialogue == null)
			return;
		
		Dialogue.NodeSelector dialogue = DialogueManager.getInstance().getDialogue(this.activeDialogue);
		if(dialogue == null)
		{
			LOGGER.warn("Unable to find dialogue with id {}", this.activeDialogue);
			this.activeDialogue = null;
			return;
		}
		
		this.openScreenForDialogue(serverPlayer, this.activeDialogue, dialogue);
	}
	
	public void tryOpenScreenForDialogue(ServerPlayer serverPlayer, ResourceLocation dialogueId)
	{
		Dialogue.NodeSelector dialogue = DialogueManager.getInstance().getDialogue(dialogueId);
		if(dialogue != null)
			this.openScreenForDialogue(serverPlayer, dialogueId, dialogue);
	}
	
	public void openScreenForDialogue(ServerPlayer serverPlayer, ResourceLocation dialogueId, Dialogue.NodeSelector dialogue)
	{
		Pair<Dialogue.Node, Integer> node = dialogue.pickNode(this.entity, serverPlayer);
		Dialogue.DialogueData data = node.getFirst().evaluateData(this.entity, serverPlayer);
		Dialogue.NodeReference nodeReference = new Dialogue.NodeReference(dialogueId, node.getSecond());
		
		DialogueScreenPacket packet = new DialogueScreenPacket(this.entity.getId(), nodeReference, data);
		MSPacketHandler.sendToPlayer(packet, serverPlayer);
	}
}
