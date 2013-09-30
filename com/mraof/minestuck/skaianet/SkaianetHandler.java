package com.mraof.minestuck.skaianet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class SkaianetHandler {
	
	public static void saveData(File file){
		try{
			DataOutputStream stream = new DataOutputStream(new FileOutputStream(file));
			stream.writeByte(Session.sessions.size());
			for(Session s : Session.sessions){
				
			}
			
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static void loadData(File file){
		try{
			DataInputStream stream =  new DataInputStream(new FileInputStream(file));
			byte size = stream.readByte();
			Session.sessions.clear();
			for(int i = 0; i < size; i++)
				Session.sessions.add(Session.load(stream));
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	
}
