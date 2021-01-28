package net.cny.util;

public class Time
{

	public static final long SECOND = 1000000000L;

	public static double GetTime()
	{
		return (double)System.nanoTime()/(double)SECOND;
	}
}