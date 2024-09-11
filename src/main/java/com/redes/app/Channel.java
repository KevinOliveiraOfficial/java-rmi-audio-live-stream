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

    public void start()
    {
        for ( Track track : this.tracks )
        {
            this.playedTime = 0;
            try (FileInputStream fis = new FileInputStream(track.file))
            {
                System.out.println("broadcasting " + track.name);

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
