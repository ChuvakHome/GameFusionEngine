package ru.gfe.handler;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.media.AudioClip;
import ru.gfe.engine.GameFusionEngine;

public class SoundHandler 
{	
	private static List<AudioClip> audioClips = new ArrayList<AudioClip>();
	
	public static void play(URL url)
	{
		try 
		{
			play(url.toURI());
		} catch (Exception e) {e.printStackTrace();}
	}
	
	public static void play(URI uri)
	{
		play(uri, 1.0, 1);
	}
	
	public static void play(URL url, double volume)
	{
		play(url, volume, 1);
	}
	
	public static void play(URL url, double volume, int loop)
	{
		try
		{
			play(url.toURI(), volume, loop);
		} catch (Exception e) {e.printStackTrace();}
	}
	
	public static void play(URL url, int loop)
	{
		try
		{
			play(url.toURI(), loop);
		} catch (Exception e) {e.printStackTrace();}
	}
	
	public static void play(URI uri, int loop)
	{
		play(uri, 1.0, loop);
	}
	
	public static void play(URI uri, double volume, int loop)
	{
		GameFusionEngine.addToQueue(new Thread(() ->
		{
			try
			{
				if (GameFusionEngine.isStarted() && uri != null)
				{	
					AudioClip audioClip = new AudioClip(uri.toString());
					
					if (audioClip != null && audioClips != null)
					{
						audioClips.add(audioClip);
							
						audioClip.setCycleCount(loop);
						audioClip.play(volume);
					}
				}
			}
			catch (Exception e) {e.printStackTrace();}
		}));
	}
	
	public static void playAndLoop(URL url)
	{
		try
		{
			playAndLoop(url.toURI());
		} catch (Exception e) {e.printStackTrace();}
	}
	
	public static void playAndLoop(URL url, double volume)
	{
		try
		{
			playAndLoop(url.toURI(), volume);
		} catch (Exception e) {e.printStackTrace();}
	}
	
	public static void playAndLoop(URI uri)
	{
		play(uri, 1.0, -1);
	}
	
	public static void playAndLoop(URI uri, double volume)
	{
		play(uri, volume, -1);
	}
	
	public static void stop(URL url)
	{
		try 
		{
			stop(url.toURI());
		} catch (Exception e) {e.printStackTrace();}
	}
	
	public static void stop(URI uri)
	{
		GameFusionEngine.addToQueue(() ->
		{
			if (uri != null)
			{
				try 
				{	
					AudioClip temp = new AudioClip(uri.toString());
				
					if (temp != null)
					{
						for (AudioClip audioClip: audioClips)
						{
							if (audioClip != null && audioClip.equals(temp))
								audioClip.stop();
						}
					}
				} catch (Exception e) {e.printStackTrace();}
			}
		});
	}
	
	public static void stopAll()
	{
		GameFusionEngine.addToQueue(() ->
		{
			audioClips.forEach(clip -> clip.stop());
			
			audioClips.clear();
		});
	}
}
