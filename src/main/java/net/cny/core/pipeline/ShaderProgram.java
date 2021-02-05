package net.cny.core.pipeline;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL31.*;

import java.util.HashMap;
import java.util.Map;

import net.cny.core.math.Matrix4f;
import net.cny.core.util.BufferUtil;
import net.cny.core.scenegraph.Node;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL20;

public class ShaderProgram 
{

    private final int program;
    private final Map<String, Integer> uniforms;

    public ShaderProgram() 
    {
        program = glCreateProgram();
        uniforms = new HashMap<>();

        if(program == 0) 
        {
            throw new IllegalStateException("Shader creation failed");
        }
    }

    public void Bind() 
    {
        glUseProgram(program);
    }

    public void AddUniform(String uniform) 
    {
        int uniformLocation = glGetUniformLocation(program, uniform);

        if (uniformLocation == 0xFFFFFFFF) {
            System.err.println(this.getClass().getName() + " Error: Could not find uniform: " + uniform);
            new Exception().printStackTrace();
            System.exit(1);
        }

        uniforms.put(uniform, uniformLocation);
    }

    public void AddUniformBlock(String uniform) 
    {
        int uniformLocation =  glGetUniformBlockIndex(program, uniform);
        if (uniformLocation == 0xFFFFFFFF) 
        {
            System.err.println(this.getClass().getName() + " Error: Could not find uniform: " + uniform);
            new Exception().printStackTrace();
            System.exit(1);
        }

        uniforms.put(uniform, uniformLocation);
    }

    public void AddVertexShader(String source)
    {
        AddProgram(source, GL_VERTEX_SHADER);
    }

    public void AddFragmentShader(String source)
    {
        AddProgram(source, GL_FRAGMENT_SHADER);
    }

    public void CompileShader() 
    {
        glLinkProgram(program);

        if(glGetProgrami(program, GL_LINK_STATUS) == 0) 
        {
            System.out.println(this.getClass().getName() + " " + glGetProgramInfoLog(program, 1024));
            System.exit(1);
        }

        glValidateProgram(program);

        if(glGetProgrami(program, GL_VALIDATE_STATUS) == 0) 
        {
            System.err.println(this.getClass().getName() +  " " + glGetProgramInfoLog(program, 1024));
            System.exit(1);
        }
    }

    private void AddProgram(String text, int type) 
    {
        int shader = glCreateShader(type);

        if (shader == 0) 
        {
            System.err.println(this.getClass().getName() + " Shader creation failed");
            System.exit(1);
        }

        glShaderSource(shader, text);
        glCompileShader(shader);

        if(glGetShaderi(shader, GL_COMPILE_STATUS) == 0) 
        {
            System.err.println(this.getClass().getName() + " " + glGetShaderInfoLog(shader, 1024));
            System.exit(1);
        }

        glAttachShader(program, shader);
    }
    
    public void Delete()
    {
    	glUseProgram(0);
    	
    	if (program != 0)
    	{
    		glDeleteProgram(program);
    	}
    }

    public void SetUniformi(String uniformName, int value) 
    {
        glUniform1i(uniforms.get(uniformName), value);
    }

    public void SetUniformf(String uniformName, float value) 
    {
        glUniform1f(uniforms.get(uniformName), value);
    }

    public void SetUniform(String uniformName, Vector2f value) {
    	
        glUniform2f(uniforms.get(uniformName), value.x, value.y);
    }

    public void SetUniform(String uniformName, Vector3f value)
    {
        glUniform3f(uniforms.get(uniformName), value.x, value.y, value.z);
    }

    public void SetUniform(String uniformName, Vector4f value)
    {
        glUniform4f(uniforms.get(uniformName), value.x, value.y, value.z, value.w);
    }

    public void SetUniform(String uniformName, Matrix4f value)
    {
        GL20.glUniformMatrix4fv(uniforms.get(uniformName), true, BufferUtil.CreateFlippedBuffer(value));
    }

    public void BindUniformBlock(String uniformBlockName, int uniformBlockBinding ) {
        glUniformBlockBinding(program, uniforms.get(uniformBlockName), uniformBlockBinding);
    }

    public void BindFragDataLocation(String name, int index){
        glBindFragDataLocation(program, index, name);
    }

    public void UpdateUniforms(Node object) {}
    
    public int GetProgram() {
        return this.program;
    }
}