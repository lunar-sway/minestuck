package com.mraof.minestuck.skaianet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ComputerData{
		int x, y, z;
		int dimension;
		String owner;
		
		ComputerData(String owner,int x,int y,int z,int dimension){
			this.owner = owner;
			this.x = x;
			this.y = y;
			this.z = z;
			this.dimension = dimension;
		}
		
		private ComputerData() {}
		
		void save(DataOutputStream stream) throws IOException{
			stream.writeInt(x);
			stream.writeInt(y);
			stream.writeInt(z);
			stream.writeInt(dimension);
			stream.write((owner+"\n").getBytes());
		}
		
		static ComputerData load(DataInputStream stream) throws IOException{
			ComputerData data = new ComputerData();
			data.x = stream.readInt();
			data.y = stream.readInt();
			data.z = stream.readInt();
			data.dimension = stream.readInt();
			data.owner = stream.readLine(); //How should I read the string here without a deprecated method?
			return data;
		}
		
		public String getOwner(){return owner;}
	}