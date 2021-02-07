package net.cny.core.config;

import static org.lwjgl.opengl.GL11.*;

public class RenderConfig
{

    public void InitOpenGL()
    {
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glClearColor(0,0,0,0);

        System.out.println("OpenGL Version: " + glGetString(GL_VERSION));
        // TODO:
        //  System.out.println("OpenAL Version: " + alGetString(AL_VERSION));
    }

    public void ClearScreen()
    {
        glClear(GL_COLOR_BUFFER_BIT);
    }
}
