package ru.gfe.handler;

import java.awt.EventQueue;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import javafx.scene.media.AudioClip;
import ru.gfe.engine.GameFusionEngine;

public final class SoundHandler 
{	
	public static final int DURATION_IN_MILLIS = 0;
	public static final int DURATION_IN_SECONDS = 1;
	public static final int DURATION_IN_MINUTES = 2;
	public static final int DURATION_IN_HOURS = 3;
	
	private static List<AudioClip> audioClips = new ArrayList<AudioClip>();
	
	private SoundHandler() {}
	
	public static double getDurationOfAudio(URL url, int flag)
	{
		if (url != null)
		{	
				try 
				{
					Clip clip = AudioSystem.getClip();
					clip.open(AudioSystem.getAudioInputStream(url));
					
					double duration = clip.getMicrosecondLength();
					
					switch (flag)
					{
						case DURATION_IN_MILLIS:
							duration /= 1000d;
							break;
						case DURATION_IN_SECONDS:
							duration /= 1000000d;
							break;
						case DURATION_IN_MINUTES:
							duration /= (1000000d * 60d);
							break;
						case DURATION_IN_HOURS:
							duration /= (1000000d * 3600d);
							break;
					}
					
					clip = null;
					
					return duration;
				} 
				catch (Exception e) {e.printStackTrace();}
				
				return -1;
		}
		else
			throw new NullPointerException("URL cannot be null");
	}
	
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
		EventQueue.invokeLater(() ->
		{
			try
			{
				if (GameFusionEngine.isStarted() && uri != null)
				{	
					AudioClip audioClip = new AudioClip(uri.toString());
					
					if (audioClip != null && audioClips != null)
					{	
						audioClip.setCycleCount(loop);
						audioClip.play(volume);
						
						audioClips.add(audioClip);
					}
				}
			}
			catch (Exception e) {e.printStackTrace();}
		});
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
		EventQueue.invokeLater(() ->
		{
			if (uri != null)
			{
				try 
				{	
					if (uri != null)
					{
						for (AudioClip audioClip: audioClips)
						{
							if (audioClip != null && audioClip.getSource().equals(uri.toString()))
								audioClip.stop();
						}
					}
				} catch (Exception e) {e.printStackTrace();}
			}
		});
	}
	
	public static void stopAll()
	{
		EventQueue.invokeLater(() ->
		{
			audioClips.forEach(clip -> clip.stop());
			
			audioClips.clear();
		});
	}
}
