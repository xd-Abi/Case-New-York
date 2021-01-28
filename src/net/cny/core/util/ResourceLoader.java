package net.cny.core.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ResourceLoader 
{

    public static String LoadShader(String fileName) 
    {
        StringBuilder shaderSource = new StringBuilder();
        BufferedReader shaderReader = null;

        try 
        {
            shaderReader = new BufferedReader(new InputStreamReader(GetResource(fileName)));
            String line;
            while((line = shaderReader.readLine()) != null)
            {
                shaderSource.append(line).append("\n");
            }

            shaderReader.close();
        }
        catch(Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        return shaderSource.toString();
    }
	
    public static InputStream GetResource(String resource) 
	{
		try 
		{
			return new FileInputStream("resources/" + resource);
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

/*	public static InputStream GetResource(String resource) 
	{
		return Class.class.getResourceAsStream("resources/" + resource);
	}
*/
}