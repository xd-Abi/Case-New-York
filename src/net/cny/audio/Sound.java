package net.cny.audio;

import net.cny.util.SoundUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.nio.IntBuffer;

public class Sound
{

    private final IntBuffer buffer;
    private final int source;

    public Sound(String soundPath)
    {

        buffer = BufferUtils.createIntBuffer(1);
        AL10.alGenBuffers(buffer);

        try
        {
            SoundUtils.createBufferData(buffer.get(0), soundPath);
        }
        catch (UnsupportedAudioFileException | IOException e)
        {
            e.printStackTrace();
        }

        source = AL10.alGenSources();
        AL10.alSourcei(source, AL10.AL_BUFFER, buffer.get(0));
        AL10.alSource3f(source, AL10.AL_POSITION, 0, 0, 0f);
        AL10.alSource3f(source, AL10.AL_VELOCITY, 0f, 0f, 0f);

        //fun stuff
        AL10.alSourcef(source, AL10.AL_PITCH, 1f);
        AL10.alSourcef(source, AL10.AL_GAIN, 1f);
        AL10.alSourcei(source, AL10.AL_LOOPING, AL10.AL_FALSE);

        SoundManager.AddSound(this);
    }

    public void Play()
    {
        AL10.alSourcePlay(source);
    }

    public void Stop()
    {
        AL10.alSourceStop(source);
    }

    public void Delete()
    {
        Stop();
        AL10.alDeleteBuffers(buffer);
        AL10.alDeleteSources(source);
    }

    public void SetPosition(float x, float y)
    {
        AL10.alSource3f(source, AL10.AL_POSITION, x, y, 0f);
    }

    public void SetPitch(float p)
    {
        AL10.alSourcef(source, AL10.AL_PITCH, p);
    }
}
