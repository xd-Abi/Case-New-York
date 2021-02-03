package net.cny.config;

import org.lwjgl.openal.*;
import static org.lwjgl.openal.AL10.*;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class AudioConfig {

    public void InitOpenAL() {
        long device = ALC10.alcOpenDevice((ByteBuffer) null);

        ALCCapabilities deviceCapabilities = ALC.createCapabilities(device);

        long newContext = ALC10.alcCreateContext(device, CreateOpenALContext());

        if (!ALC10.alcMakeContextCurrent(newContext)) {
            throw new IllegalStateException("Error: Failed to create OpenAL context current");
        }

        AL.createCapabilities(deviceCapabilities);

        System.out.println("OpenAL Version: " + alGetString(AL_VERSION));
    }

    private IntBuffer CreateOpenALContext() {
        IntBuffer openALContext = BufferUtils.createIntBuffer(16);

        openALContext.put(ALC10.ALC_REFRESH);
        openALContext.put(60);

        openALContext.put(ALC10.ALC_SYNC);
        openALContext.put(ALC10.ALC_FALSE);

        openALContext.put(EXTEfx.ALC_MAX_AUXILIARY_SENDS);
        openALContext.put(2);

        openALContext.put(0);
        openALContext.flip();

        return openALContext;
    }

    public void DestroyContext()
    {
        long openALContext = ALC10.alcGetCurrentContext();

        ALC10.alcDestroyContext(openALContext);
        ALC10.alcCloseDevice(openALContext);
    }
}
