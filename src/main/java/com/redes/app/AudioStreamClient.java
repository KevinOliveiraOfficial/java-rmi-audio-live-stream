package com.redes.app;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class AudioStreamClient
{
    public AudioStream audioStream;

    public AudioStreamClient( AudioStream audioStream ) {
        this.audioStream = audioStream;
    }

    private void showChannels()
    {
        try
        {
            System.out.println("Channel list:");
            for ( String channel : this.audioStream.getChannels() )
            {
                System.out.println(channel);
            }
        }
        catch ( RemoteException e ) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args)
    {
        try
        {
            AudioStreamClient audioStreamClient = new AudioStreamClient((AudioStream) Naming.lookup("rmi://localhost:1099/AudioStreamService"));
            audioStreamClient.showChannels();
            

            System.out.println("Play");
            AudioFormat format = new AudioFormat(44100, 16, 2, true, false); // Formato de áudio
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();

            byte[] audioChunk;
            while ((audioChunk = audioStreamClient.audioStream.getAudioChunk("POP")) != null)
            {
                line.write(audioChunk, 0, audioChunk.length);
                System.out.println(audioChunk.length);
            }

            line.drain();
            line.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
