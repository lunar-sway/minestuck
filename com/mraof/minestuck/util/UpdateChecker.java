package com.mraof.minestuck.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.mraof.minestuck.Minestuck;

import cpw.mods.fml.common.Mod;

public class UpdateChecker extends Thread
{
	public static boolean outOfDate = false;
	public static String latestVersion = Minestuck.class.getAnnotation(Mod.class).version();
	public static String updateChanges = "";
	
	@Override
	public void run() 
	{
		checkUpdates();
	}
	
	public static void checkUpdates()
	{
		try {
			URL versionURL = new URL("http://minestuck.mraof.com/version.php");
			URLConnection connection = versionURL.openConnection();
			connection.setUseCaches(false);
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
			String version = in.readLine();
			in.close();
			
			if(!version.equals(latestVersion))
			{
				outOfDate = true;
				latestVersion = version;
				URL changesURL = new URL("http://minestuck.mraof.com/changes");
				in = new BufferedReader(new InputStreamReader(changesURL.openStream()));
				updateChanges = in.readLine();
				in.close();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
