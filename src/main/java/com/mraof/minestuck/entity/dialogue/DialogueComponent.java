package com.mraof.minestuck.entity.dialogue;

import com.mojang.datafixers.util.Pair;
import com.mraof.minestuck.network.DialoguePackets;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.util.MSCapabilities;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.*;

public final class DialogueComponent
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private final LivingEntity entity;
	@Nullable
	private ResourceLocation startingDialogue;
	private final Map<UUID, ResourceLocation> dialogueEntrypoint = new HashMap<>();
	private boolean keepOnReset;
	private boolean hasGeneratedOnce = false;
	private final Set<String> flags = new HashSet<>();
	private final Map<UUID, Set<String>> playerSpecificFlags = new HashMap<>();
	private final Map<UUID, Item> matchedItem = new HashMap<>();
	private final Map<UUID, Dialogue.NodeReference> currentNodeForPlayer = new HashMap<>();
	
	public DialogueComponent(LivingEntity entity)
	{
		this.entity = entity;
	}
	
	public void read(CompoundTag tag)
	{
		if(tag.contains("dialogue_id", CompoundTag.TAG_STRING))
		{
			this.startingDialogue = ResourceLocation.tryParse(tag.getString("dialogue_id"));
			this.keepOnReset = tag.getBoolean("keep_on_reset");
			this.hasGeneratedOnce = true;
			
			this.flags.clear();
			tag.getList("flags", Tag.TAG_STRING).stream().map(StringTag.class::cast)
					.map(StringTag::getAsString).forEach(this.flags::add);
			
			this.playerSpecificFlags.clear();
			tag.getList("player_flags", Tag.TAG_COMPOUND).stream().map(CompoundTag.class::cast).forEach(entryTag -> {
				
				UUID player = entryTag.getUUID("player");
				Set<String> playerFlags = this.playerFlags(player);
				entryTag.getList("flags", Tag.TAG_STRING).stream().map(StringTag.class::cast)
						.map(StringTag::getAsString).forEach(playerFlags::add);
			});
			
			this.matchedItem.clear();
			tag.getList("matched_item", Tag.TAG_COMPOUND).stream().map(CompoundTag.class::cast).forEach(entryTag -> {
				UUID player = entryTag.getUUID("player");
				ForgeRegistries.ITEMS.getCodec().parse(NbtOps.INSTANCE, entryTag.get("item"))
						.resultOrPartial(LOGGER::error)
						.ifPresent(item -> this.matchedItem.put(player, item));
			});
		}
		else
			this.hasGeneratedOnce = tag.getBoolean("has_generated");
	}
	
	public CompoundTag write()
	{
		CompoundTag tag = new CompoundTag();
		if(this.startingDialogue != null)
		{
			tag.putString("dialogue_id", this.startingDialogue.toString());
			tag.putBoolean("keep_on_reset", this.keepOnReset);
			
			ListTag flagsTag = new ListTag();
			this.flags.stream().map(StringTag::valueOf).forEach(flagsTag::add);
			tag.put("flags", flagsTag);
			
			ListTag playerFlagsMapTag = new ListTag();
			this.playerSpecificFlags.forEach((player, playerFlags) -> {
				CompoundTag entryTag = new CompoundTag();
				entryTag.putUUID("player", player);
				ListTag playerFlagsTag = new ListTag();
				playerFlags.stream().map(StringTag::valueOf).forEach(playerFlagsTag::add);
				entryTag.put("flags", playerFlagsTag);
				playerFlagsMapTag.add(entryTag);
			});
			tag.put("player_flags", playerFlagsMapTag);
			
			ListTag matchedItemTag = new ListTag();
			this.matchedItem.forEach((player, item) -> {
				CompoundTag entryTag = new CompoundTag();
				entryTag.putUUID("player", player);
				ForgeRegistries.ITEMS.getCodec().encodeStart(NbtOps.INSTANCE, item)
						.resultOrPartial(LOGGER::error)
						.ifPresent(itemTag -> {
							entryTag.put("item", itemTag);
							matchedItemTag.add(entryTag);
						});
			});
			tag.put("matched_item", matchedItemTag);
		}
		
		tag.putBoolean("has_generated", this.hasGeneratedOnce);
		
		return tag;
	}
	
	public LivingEntity entity()
	{
		return this.entity;
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
		this.startingDialogue = dialogueId;
		this.keepOnReset = keepOnReset;
	}
	
	public Optional<ResourceLocation> getStartingDialogue()
	{
		return Optional.ofNullable(this.startingDialogue);
	}
	
	public void setDialogueForPlayer(ServerPlayer player, ResourceLocation dialogueId)
	{
		this.dialogueEntrypoint.put(player.getUUID(), dialogueId);
	}
	
	public Optional<ResourceLocation> getDialogueForPlayer(ServerPlayer player)
	{
		return Optional.ofNullable(this.dialogueEntrypoint.getOrDefault(player.getUUID(), this.startingDialogue));
	}
	
	public boolean hasActiveDialogue()
	{
		return this.startingDialogue != null;
	}
	
	public Set<String> flags()
	{
		return this.flags;
	}
	
	public Set<String> playerFlags(ServerPlayer player)
	{
		return this.playerFlags(player.getUUID());
	}
	
	private Set<String> playerFlags(UUID player)
	{
		return this.playerSpecificFlags.computeIfAbsent(player, _player -> new HashSet<>());
	}
	
	public Optional<Item> getMatchedItem(ServerPlayer player)
	{
		return Optional.ofNullable(this.matchedItem.get(player.getUUID()));
	}
	
	public void setMatchedItem(Item item, ServerPlayer player)
	{
		this.matchedItem.put(player.getUUID(), item);
	}
	
	public void clearMatchedItem(ServerPlayer player)
	{
		this.matchedItem.remove(player.getUUID());
	}
	
	public void resetDialogue()
	{
		if(!this.keepOnReset)
			this.startingDialogue = null;
		this.dialogueEntrypoint.clear();
		this.flags.clear();
		this.playerSpecificFlags.clear();
		this.matchedItem.clear();
		closeAllCurrentDialogue();
	}
	
	public void closeAllCurrentDialogue()
	{
		this.currentNodeForPlayer.keySet().forEach(this::closeCurrentDialogue);
		this.currentNodeForPlayer.clear();
	}
	
	private void closeCurrentDialogue(UUID playerId)
	{
		ServerPlayer player = Objects.requireNonNull(this.entity.getServer()).getPlayerList().getPlayer(playerId);
		if(player == null)
			return;
		
		if(player.getCapability(MSCapabilities.CURRENT_DIALOGUE).orElseThrow(IllegalStateException::new).lastTalkedTo(this.entity))
			MSPacketHandler.sendToPlayer(new DialoguePackets.CloseScreen(), player);
	}
	
	public void tryStartDialogue(ServerPlayer player)
	{
		Optional<ResourceLocation> dialogueId = this.getDialogueForPlayer(player);
		if(dialogueId.isEmpty())
			return;
		
		Dialogue.NodeSelector dialogue = DialogueNodes.getInstance().getDialogue(dialogueId.get());
		if(dialogue == null)
		{
			LOGGER.warn("Unable to find dialogue with id {}", dialogueId.get());
			this.resetDialogue();
			return;
		}
		
		this.openScreenForDialogue(player, dialogueId.get(), dialogue);
	}
	
	public void tryOpenScreenForDialogue(ServerPlayer serverPlayer, ResourceLocation dialogueId)
	{
		Dialogue.NodeSelector dialogue = DialogueNodes.getInstance().getDialogue(dialogueId);
		if(dialogue != null)
			this.openScreenForDialogue(serverPlayer, dialogueId, dialogue);
	}
	
	public void openScreenForDialogue(ServerPlayer player, ResourceLocation dialogueId, Dialogue.NodeSelector dialogue)
	{
		Pair<Dialogue.Node, Integer> node = dialogue.pickNode(this.entity, player);
		Dialogue.DialogueData data = node.getFirst().evaluateData(this.entity, player);
		Dialogue.NodeReference nodeReference = new Dialogue.NodeReference(dialogueId, node.getSecond());
		
		this.currentNodeForPlayer.put(player.getUUID(), nodeReference);
		CurrentDialogue dialogueData = player.getCapability(MSCapabilities.CURRENT_DIALOGUE)
				.orElseThrow(IllegalStateException::new);
		DialoguePackets.OpenScreen packet = new DialoguePackets.OpenScreen(dialogueData.newDialogue(this.entity), data);
		MSPacketHandler.sendToPlayer(packet, player);
	}
	
	public Optional<Dialogue.Node> validateAndGetCurrentNode(ServerPlayer player)
	{
		return Optional.ofNullable(this.currentNodeForPlayer.get(player.getUUID()))
				.flatMap(reference ->
						Optional.ofNullable(DialogueNodes.getInstance().getDialogue(reference.dialoguePath()))
								.flatMap(nodeSelector -> nodeSelector.getNodeIfValid(reference.nodeIndex(), this.entity, player))
				);
	}
	
	public void clearCurrentNode(ServerPlayer player)
	{
		this.currentNodeForPlayer.remove(player.getUUID());
	}
	
	public static final class CurrentDialogue
	{
		/**
		 * A counter for identifying the currently displayed dialogue gui. The counter increases by one with each new gui.
		 * This fills the same purpose for dialogue as {@link ServerPlayer#containerCounter} does for container menus:
		 * To identify which gui that the client and server is sending packets about,
		 * to avoid packets meant for a previous gui instead getting used for a new gui.
		 */
		private int dialogueCounter = 0;
		@Nullable
		private Integer entityId = null;
		
		public boolean lastTalkedTo(Entity entity)
		{
			return this.entityId != null && this.entityId == entity.getId();
		}
		
		private int newDialogue(LivingEntity entity)
		{
			this.entityId = entity.getId();
			return ++dialogueCounter;
		}
		
		public Optional<DialogueComponent> validateAndGetActiveComponent(ServerPlayer player, int dialogueId)
		{
			CurrentDialogue dialogueData = player.getCapability(MSCapabilities.CURRENT_DIALOGUE).orElseThrow(IllegalStateException::new);
			if(dialogueId != dialogueData.dialogueCounter || dialogueData.entityId == null)
				return Optional.empty();
			
			Entity entity = player.level().getEntity(dialogueData.entityId);
			if(!(entity instanceof DialogueEntity dialogueEntity))
				return Optional.empty();
			
			return Optional.of(dialogueEntity.getDialogueComponent());
		}
	}
}
