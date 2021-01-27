package net.fny.core.texturing;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

import net.fny.core.util.ImageLoader;

public class Texture2D {
	
	private int id;
	private int width;
	private int height;
	
	public Texture2D(){}
	
	public Texture2D(String file)
	{
		int[] data = ImageLoader.LoadImage(file);
		id = data[0];
		width = data[1];
		height = data[2];
	
		//NoFilter();
		TrilinearFilter();
	}
	
	public void Bind()
	{
		glBindTexture(GL_TEXTURE_2D, id);
	}
	
	public void Generate()
	{
		id = glGenTextures();
	}
	
	public void Delete()
	{
		glDeleteTextures(id);
	}
	
	public void Unbind()
	{
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public void NoFilter()
	{
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
	}
	
	public void BilinearFilter()
	{
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
	}
	
	public void TrilinearFilter(){
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glGenerateMipmap(GL_TEXTURE_2D);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
	}
	
	public int GetId() 
	{
		return id;
	}

	public int GetWidth() 
	{
		return width;
	}

	public int GetHeight() 
	{
		return height;
	}
}