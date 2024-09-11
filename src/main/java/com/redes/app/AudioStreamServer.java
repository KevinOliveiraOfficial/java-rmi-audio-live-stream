package com.redes.app;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.sound.sampled.UnsupportedAudioFileException;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class AudioStreamServer extends UnicastRemoteObject implements AudioStream
{
    //private List<Channel> channels = new ArrayList<Channel>();
    private LinkedHashMap<String, Channel> channels = new LinkedHashMap<String, Channel>();
    public AudioStreamServer() throws RemoteException, IOException, UnsupportedAudioFileException
    {
        super();
            
        // Pop
        Channel pop = new Channel("POP");
        pop.tracks.add(new Track("Martin Garrix - Smile", new File("music/martingarrix_smile.wav"), 78));
        this.channels.put(pop.name, pop);
        
        // Eletro
        Channel eletro = new Channel("ELETRO");
        eletro.tracks.add(new Track("Vintage Culture - Deep Inside", new File("music/vintage_culture_deep_inside.wav"), 262));
        this.channels.put(eletro.name, eletro);

        // Sertanejo
        Channel sertanejo = new Channel("SERTANEJO");
        sertanejo.tracks.add(new Track("Gusttavo Lima - Morar Nesse Motel", new File("music/GUSTTAVO_LIMA_MORAR_NESSE_MOTEL.wav"), 238));
        this.channels.put(sertanejo.name, sertanejo);

        ExecutorService executorService = Executors.newFixedThreadPool(this.channels.size());

        // Create threads for each channel
        for ( String channel : this.channels.keySet() )
        {
            executorService.execute( () ->
            {
                // Start channel
                this.channels.get(channel).start();
            });
        }

        executorService.shutdown();

    }

    public List<String> getChannels()
    {
        return new ArrayList<>(this.channels.keySet());
    }

    @Override
    public byte[] getAudioChunk( String channelName ) throws RemoteException {
        try
        {
            return this.channels.get(channelName).availableBuffer;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Erro ao ler o arquivo de áudio", e);
        }
    }
}
