package com.mraof.minestuck.pesterchum;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class IRC implements Runnable
{
	public PrintWriter output;
	public BufferedReader in;
	public String userString = "pcc31";
	public String chumhandle = "minestuckChum";
	public boolean running = false;
	public MessageParser parser;

	public IRC()
	{
		parser = new MessageParser(this);
	}

	@Override
	public void run()
	{
		in = null;
		running = true;
		Socket socket = new Socket();
		try
		{
			socket.connect(new InetSocketAddress("irc.mindfang.org", 6667));
			output = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}
		catch(Exception e)
		{
			try 
			{
				socket.close();
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
			return;
		}

		output.println("USER " + userString + " 0 * :" + "pcc31");
		output.println("NICK " + chumhandle);

		(new Thread(parser)).start();

		try
		{
			while(running)
			{
				String message = in.readLine();
				parser.add(message);
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

		try 
		{
			in.close();
			output.close();
			socket.close();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
