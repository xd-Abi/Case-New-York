package net.fny.audio;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALC11;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;
import org.lwjgl.openal.ALUtil;
import org.lwjgl.openal.EXTEfx;

import net.fny.core.util.ResourceLoader;

public class SoundManager 
{

    public static void Initalize() throws Exception {
        //Start by acquiring the default device
        long device = ALC10.alcOpenDevice((ByteBuffer)null);

        //Create a handle for the device capabilities, as well.
        ALCCapabilities deviceCaps = ALC.createCapabilities(device);
        // Create context (often already present, but here, necessary)
        IntBuffer contextAttribList = BufferUtils.createIntBuffer(16);

        // Note the manner in which parameters are provided to OpenAL...
        contextAttribList.put(ALC10.ALC_REFRESH);
        contextAttribList.put(60);

        contextAttribList.put(ALC10.ALC_SYNC);
        contextAttribList.put(ALC10.ALC_FALSE);

        // Don't worry about this for now; deals with effects count
        contextAttribList.put(EXTEfx.ALC_MAX_AUXILIARY_SENDS);
        contextAttribList.put(2);

        contextAttribList.put(0);
        contextAttribList.flip();
        
        //create the context with the provided attributes
        long newContext = ALC10.alcCreateContext(device, contextAttribList);
        
        if(!ALC10.alcMakeContextCurrent(newContext)) {
            throw new Exception("Failed to make context current");
        }
        
        AL.createCapabilities(deviceCaps);
        
        
        //define listener
        AL10.alListener3f(AL10.AL_VELOCITY, 0f, 0f, 0f);
        AL10.alListener3f(AL10.AL_ORIENTATION, 0f, 0f, -1f);
        
        
        IntBuffer buffer = BufferUtils.createIntBuffer(1);
        AL10.alGenBuffers(buffer);
        
        //We'll get to this next!
        long time = createBufferData(buffer.get(0));
        
        //Define a source
        int source = AL10.alGenSources();
        AL10.alSourcei(source, AL10.AL_BUFFER, buffer.get(0));
        AL10.alSource3f(source, AL10.AL_POSITION, 0f, 0f, 0f);
        AL10.alSource3f(source, AL10.AL_VELOCITY, 0f, 0f, 0f);
        
        //fun stuff
        AL10.alSourcef(source, AL10.AL_PITCH, 1);
        AL10.alSourcef(source, AL10.AL_GAIN, 1f);
        AL10.alSourcei(source, AL10.AL_LOOPING, AL10.AL_FALSE);
        
        //Trigger the source to play its sound
        AL10.alSourcePlay(source);
        
        try {
            Thread.sleep(time); //Wait for the sound to finish
        } catch(InterruptedException ex) {}
        
        AL10.alSourceStop(source); //Demand that the sound stop
        
        //and finally, clean up
        AL10.alDeleteSources(source);
        

    }
	
    private static long createBufferData(int p) throws UnsupportedAudioFileException, IOException {
        //shortcut finals:
        final int MONO = 1, STEREO = 2;
        
        AudioInputStream stream = null;
        
        InputStream audioSrc = ResourceLoader.GetResource("bounce.wav");
      //add buffer for mark/reset support
        InputStream bufferedIn = new BufferedInputStream(audioSrc);
        stream = AudioSystem.getAudioInputStream(bufferedIn);
      
  
        AudioFormat format = stream.getFormat();
        if(format.isBigEndian()) throw new UnsupportedAudioFileException("Can't handle Big Endian formats yet");
        
        //load stream into byte buffer
        int openALFormat = -1;
        switch(format.getChannels()) {
            case MONO:
                switch(format.getSampleSizeInBits()) {
                    case 8:
                        openALFormat = AL10.AL_FORMAT_MONO8;
                        break;
                    case 16:
                        openALFormat = AL10.AL_FORMAT_MONO16;
                        break;
                }
                break;
            case STEREO:
                switch(format.getSampleSizeInBits()) {
                    case 8:
                        openALFormat = AL10.AL_FORMAT_STEREO8;
                        break;
                    case 16:
                        openALFormat = AL10.AL_FORMAT_STEREO16;
                        break;
                }
                break;
        }
        
        //load data into a byte buffer
        //I've elected to use IOUtils from Apache Commons here, but the core
        //notion is to load the entire stream into the byte array--you can
        //do this however you would like.
        
        byte[] b = org.apache.commons.io.IOUtils.toByteArray(stream);
        
        ByteBuffer data = BufferUtils.createByteBuffer(b.length).put(b);
        data.flip();
        
        //load audio data into appropriate system space....
        AL10.alBufferData(p, openALFormat, data, (int)format.getSampleRate());
        
        //and return the rough notion of length for the audio stream!
        return (long)(1000f * stream.getFrameLength() / format.getFrameRate());
    }
}
