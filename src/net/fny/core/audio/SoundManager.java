package net.fny.core.audio;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

public class SoundManager
{

    private static ArrayList<Sound> sounds;

    public static void Initialize() {
        sounds = new ArrayList<>();

        long device = ALC10.alcOpenDevice((ByteBuffer) null);

        ALCCapabilities deviceCapabilities = ALC.createCapabilities(device);

        long newContext = ALC10.alcCreateContext(device, CreateContextAttribList());

        if (!ALC10.alcMakeContextCurrent(newContext)) {
            throw new IllegalStateException("Error: Failed to make context current");
        }

        AL.createCapabilities(deviceCapabilities);
    }

    private static IntBuffer CreateContextAttribList()
    {
        IntBuffer contextAttribList = BufferUtils.createIntBuffer(16);
        contextAttribList.put(ALC10.ALC_REFRESH);
        contextAttribList.put(60);
        contextAttribList.put(ALC10.ALC_SYNC);
        contextAttribList.put(ALC10.ALC_FALSE);
        contextAttribList.put(EXTEfx.ALC_MAX_AUXILIARY_SENDS);
        contextAttribList.put(2);
        contextAttribList.put(0);

        return contextAttribList.flip();
    }

    public static void CleanUp()
    {
        for (Sound sound : sounds)
            sound.Delete();
    }

    public static void AddSound(Sound sound)
    {
        sounds.add(sound);
    }
}
