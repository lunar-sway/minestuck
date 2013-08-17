package com.mraof.minestuck.world.gen;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.SpawnListEntry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.NoiseGeneratorOctaves;

import com.mraof.minestuck.entity.consort.EntityNakagator;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.util.Title;
import com.mraof.minestuck.world.gen.lands.LandAspect;
import com.mraof.minestuck.world.gen.lands.LandAspectFrost;
import com.mraof.minestuck.world.gen.lands.LandHelper;

public class ChunkProviderLands implements IChunkProvider 
{
	List consortList;
	World landWorld;
	Random random;
	private NoiseGeneratorOctaves noiseGens[] = new NoiseGeneratorOctaves[2];
	public LandAspect aspect1;
	public LandAspect aspect2;
	public int dimId;
	public LandHelper helper;

	public ChunkProviderLands(World worldObj, long seed, boolean b) 
	{
		helper = new LandHelper(seed);
		
		NBTBase landDataTag = worldObj.getWorldInfo().getAdditionalProperty("LandData");

		this.landWorld = worldObj;

		if (landDataTag == null) {
			this.aspect1 = helper.getLandAspect(new Title(0,0));
			this.aspect2 = helper.getLandAspect(new Title(0,0),aspect1);
			Map<String, NBTBase> dataTag = new Hashtable<String,NBTBase>();
			dataTag.put("LandData",LandHelper.toNBT(aspect1,aspect2));
			worldObj.getWorldInfo().setAdditionalProperties(dataTag);

//			// this packet code is wrong-sided, needs fixed, I don't even know if we need it anymore
//			Packet250CustomPayload packet = new Packet250CustomPayload();
//			packet.channel = "Minestuck";
//			packet.data = MinestuckPacket.makePacket(Type.NEWLAND,aspect1.getPrimaryName(),aspect2.getPrimaryName(),3);
//			packet.length = packet.data.length;
//			Minecraft.getMinecraft().getNetHandler().addToSendQueue(packet);
		} else {
			aspect1 = helper.fromName(((NBTTagCompound) landDataTag).getString("aspect1"));
			aspect2 = helper.fromName(((NBTTagCompound) landDataTag).getString("aspect2"));
		}
		
		this.random = new Random(seed);
		this.consortList = new ArrayList();
		this.consortList.add(new SpawnListEntry(EntityNakagator.class, 2, 1, 10));
		this.noiseGens[0] = new NoiseGeneratorOctaves(this.random, 7);
        this.noiseGens[1] = new NoiseGeneratorOctaves(this.random, 1);
	}

	@Override
	public boolean chunkExists(int i, int j) 
	{
		return true;
	}

	@Override
	public Chunk provideChunk(int chunkX, int chunkZ) 
	{
		

		short[] chunkIds = new short[65536];
		byte[] chunkMetadata = new byte[65536];
		double[] generated0 = new double[256];
		double[] generated1 = new double[65536];
		int[] topBlock = new int[256];
		
		generated0 = this.noiseGens[0].generateNoiseOctaves(generated0, chunkX*16, 10, chunkZ*16, 16, 1, 16, .1, 0, .1);
		generated1 = this.noiseGens[1].generateNoiseOctaves(generated1, chunkX*16, 2, chunkZ*16, 16, 256, 16, .12, .11, .12);
		
		for(int i = 0; i < 256; i++)
		{
			int y = (int)(64 + generated0[i]);
			topBlock[i] = (y&511)<=255  ? y&255 : 255 - y&255;
		}
		
		for(int x = 0; x < 16; x++)
			for(int z = 0; z < 16; z++)
			{
				chunkIds[x + z * 16] = (short) Block.bedrock.blockID;
				int y;
				int currentBlockOffset;
				for(y = 1; y < topBlock[x * 16 + z] - 1; y++)
				{
					currentBlockOffset = (int) Math.abs(generated1[x + z * 256 + y * 16]) % aspect1.getSurfaceBlocks()[0].length;
					chunkIds[x + z * 16 + y * 256] = (short) aspect1.getUpperBlocks()[0][currentBlockOffset];
					chunkMetadata[x + z * 16 + y * 256] = (byte) aspect1.getUpperBlocks()[1][currentBlockOffset];
				}
				currentBlockOffset = (int) Math.abs(generated1[x + z * 256 + y * 16]) % aspect1.getSurfaceBlocks()[0].length;
				chunkIds[x + z * 16 + y * 256] = (short) aspect1.getSurfaceBlocks()[0][currentBlockOffset];
				chunkMetadata[x + z * 16 + y * 256] = (byte) aspect1.getSurfaceBlocks()[1][currentBlockOffset];
//					(short) (generated1[x + z * 256 + y * 16] < 0 ? Block.blockEmerald.blockID : Block.blockDiamond.blockID);
				
			}
		Chunk chunk = new Chunk(this.landWorld, chunkIds, chunkMetadata, chunkX, chunkZ);
		return chunk;
	}

	@Override
	public Chunk loadChunk(int chunkX, int chunkZ) 
	{
		return this.provideChunk(chunkX, chunkZ);
	}

	@Override
	public void populate(IChunkProvider ichunkprovider, int i, int j) {
	}

	@Override
	public boolean saveChunks(boolean flag, IProgressUpdate iprogressupdate) {
		return true;
	}

	@Override
	public boolean unloadQueuedChunks() {
		return false;
	}

	@Override
	public boolean canSave() {
		return true;
	}

	@Override
	public String makeString() {
		return "LandRandomLevelSource";
	}

	@Override
	public List getPossibleCreatures(EnumCreatureType enumcreaturetype, int i,
			int j, int k) {
		return enumcreaturetype == EnumCreatureType.creature ? this.consortList : null;
	}

	@Override
	public ChunkPosition findClosestStructure(World world, String s, int i,
			int j, int k) {
		return null;
	}

	@Override
	public int getLoadedChunkCount() {
		return 0;
	}

	@Override
	public void recreateStructures(int i, int j) {
	}

	@Override
	public void func_104112_b() {
	}

}
