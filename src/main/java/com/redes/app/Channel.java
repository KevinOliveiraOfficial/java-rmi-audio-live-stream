package com.redes.app;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Channel
{
    public String name;
    public List<Track> tracks = new ArrayList<Track>();
    public Track playing;
    public long playedTime;
    public byte[] availableBuffer;

    public Channel( String name ) {
        this.name = name;
    }

    public byte[] getAudioChunk()
    {
        byte[] array1 = this.playing.header;
        byte[] array2 = this.availableBuffer;

        // Cria um novo array com o tamanho dos dois arrays
        byte[] result = new byte[array1.length + array2.length];

        // Copia o conteúdo do primeiro array para o novo array
        System.arraycopy(array1, 0, result, 0, array1.length);

        // Copia o conteúdo do segundo array para o novo array após o primeiro array
        System.arraycopy(array2, 0, result, array1.length, array2.length);

        return result;
    }

    public void start()
    {
        for ( Track track : this.tracks )
        {
            this.playedTime = 0;
            try (FileInputStream fis = new FileInputStream(track.file))
            {
                System.out.println("broadcasting " + track.name);
                
                // Read header
                byte[] header = new byte[track.headerSize]; // WAV header size
                fis.read(header); // Read the WAV header
                track.header = header;

                // Set current playing track
                this.playing = track;

                // Broadcast track
                byte[] buffer = new byte[track.calcBps()];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1)
                {
                    this.availableBuffer = buffer;
                    this.playedTime += 1;
                    System.out.println(this.name + " broadcast " + this.playedTime + " seconds");
                    Thread.sleep(1000);
                }
                
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                System.out.println("Interrupted " + e.getMessage());
            } 
        }
    }
}
