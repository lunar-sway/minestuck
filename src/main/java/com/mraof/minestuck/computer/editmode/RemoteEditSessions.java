package com.mraof.minestuck.computer.editmode;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class RemoteEditSessions
{
	public record PreviewEntry(BlockPos localOffset, BlockState state) {}
	
	public static final class Session
	{
		public boolean dragActive;
		public int toolKind = -1;
		@Nullable public BlockPos dragPos1;
		@Nullable public BlockPos dragPos2;
		
		public boolean selectionActive;
		@Nullable public BlockPos selectionPos1;
		@Nullable public BlockPos selectionPos2;
		
		public List<PreviewEntry> previewBlocks = List.of();
		public int previewSizeX = 1, previewSizeY = 1, previewSizeZ = 1;
		
		public boolean previewActive;
		@Nullable public BlockPos previewAnchor;
		public int previewRotation;
		public boolean previewIsCopy;
	}
	
	private static final Map<UUID, Session> SESSIONS = new HashMap<>();
	
	private static Session getOrCreate(UUID editorId)
	{
		return SESSIONS.computeIfAbsent(editorId, id -> new Session());
	}
	
	public static void updateDragBox(UUID editorId, boolean active, int toolKind, @Nullable BlockPos pos1, @Nullable BlockPos pos2)
	{
		Session s = getOrCreate(editorId);
		s.dragActive = active;
		s.toolKind = toolKind;
		s.dragPos1 = pos1;
		s.dragPos2 = pos2;
	}
	
	public static void updateSelectionBox(UUID editorId, boolean active, @Nullable BlockPos pos1, @Nullable BlockPos pos2)
	{
		Session s = getOrCreate(editorId);
		s.selectionActive = active;
		s.selectionPos1 = pos1;
		s.selectionPos2 = pos2;
	}
	
	public static void setPreviewBlocks(UUID editorId, int sizeX, int sizeY, int sizeZ, List<PreviewEntry> blocks)
	{
		Session s = getOrCreate(editorId);
		s.previewSizeX = sizeX;
		s.previewSizeY = sizeY;
		s.previewSizeZ = sizeZ;
		s.previewBlocks = blocks;
	}
	
	public static void updatePreviewTransform(UUID editorId, boolean active, @Nullable BlockPos anchor, int rotation, boolean isCopy)
	{
		Session s = getOrCreate(editorId);
		s.previewActive = active;
		s.previewAnchor = anchor;
		s.previewRotation = rotation;
		s.previewIsCopy = isCopy;
	}
	
	public static void clearSession(UUID editorId)
	{
		SESSIONS.remove(editorId);
	}
	
	public static void clearAll()
	{
		SESSIONS.clear();
	}
	
	public static Map<UUID, Session> allSessions()
	{
		return SESSIONS;
	}
}
