package com.mraof.minestuck.network.skaianet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.nbt.NBTTagCompound;

import com.mraof.minestuck.tileentity.TileEntityComputer;

public class ComputerData{
		int x, y, z;
		int dimension;
		String owner;
		
		public static ComputerData createData(TileEntityComputer te){
			return new ComputerData(te.owner, te.xCoord, te.yCoord, te.zCoord, te.getWorldObj().provider.dimensionId );
		}
		
		public ComputerData(String owner,int x,int y,int z,int dimension){
			this.owner = owner;
			this.x = x;
			this.y = y;
			this.z = z;
			this.dimension = dimension;
		}
		
	
	ComputerData() {}
	
		void save(DataOutputStream stream) throws IOException{
			stream.writeInt(x);
			stream.writeInt(y);
			stream.writeInt(z);
			stream.writeInt(dimension);
			stream.write(owner.getBytes());
		}
		
		static ComputerData load(DataInputStream stream) throws IOException{
			ComputerData data = new ComputerData();
			data.x = stream.readInt();
			data.y = stream.readInt();
			data.z = stream.readInt();
			data.dimension = stream.readInt();
			byte[] ownerBytes = new byte[stream.available()];
			stream.read(ownerBytes);
			char[] ownerChars = new char[ownerBytes.length];
			for(int i = 0; i < ownerChars.length; i++)
				ownerChars[i] = (char) ownerBytes[i];
			System.out.println(String.valueOf(ownerChars));
			data.owner = String.valueOf(ownerChars); //This will break for non ASCII owners, hopefully none exist
			return data;
		}
	
	ComputerData read(NBTTagCompound nbt){
		owner = nbt.getString("name");
		x = nbt.getInteger("x");
		y = nbt.getInteger("y");
		z = nbt.getInteger("z");
		dimension = nbt.getInteger("dim");
		return this;
	}
	
	NBTTagCompound write(){
		NBTTagCompound c = new NBTTagCompound();
		c.setString("name", owner);
		c.setInteger("x", x);
		c.setInteger("y", y);
		c.setInteger("z", z);
		c.setInteger("dim", dimension);
		return c;
	}
	
		public String getOwner(){return owner;}
		public int getX(){return x;}
		public int getY(){return y;}
		public int getZ() {return z;}
		public int getDimension() {return dimension;}
	}
