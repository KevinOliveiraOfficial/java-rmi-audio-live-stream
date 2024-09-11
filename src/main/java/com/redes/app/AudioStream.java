package com.redes.app;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface AudioStream extends Remote {
    List<String> getChannels() throws RemoteException;
    byte[] getAudioChunk( String channel ) throws RemoteException;
}
