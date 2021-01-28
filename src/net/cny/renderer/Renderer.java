
package net.cny.renderer;

import net.cny.scenegraph.NodeComponent;
import net.cny.image.Image;
import org.lwjgl.opengl.GL13;

import net.cny.pipeline.ShaderProgram;

public class Renderer extends NodeComponent
{
	
	private Image image;
	private ShaderProgram shaderProgram;
	
	@Override
	public void Render() 
	{
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		
		image.Bind();
		shaderProgram.Bind();
		shaderProgram.UpdateUniforms(getParent());
		getParent().GetMesh().Draw();
		image.Unbind();
	}
	
	@Override
	public void CleanUp() 
	{
		shaderProgram.Delete();
		image.Delete();
	}
	
	public void SetImage(Image image)
	{
		this.image = image;
	}
	
	public void SetShaderProgram(ShaderProgram shaderProgram) 
	{
		this.shaderProgram = shaderProgram;
	}
	
	@Override
	public String GetType() {
		return "renderer";
	}
}
