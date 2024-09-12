package com.redes.app;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class AudioStreamClient {
    public AudioStream audioStream;
    private String currentChannel = "SERTANEJO"; // Canal padrão
    private boolean playing = false; // Controle de reprodução
    private SourceDataLine line; // Para controlar a linha de áudio

    public AudioStreamClient(AudioStream audioStream) {
        this.audioStream = audioStream;
    }

    private void showChannels() {
        try {
            System.out.println("Channel list:");
            for (String channel : this.audioStream.getChannels()) {
                System.out.println(channel);
            }
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }
    }

    private void playChannel() {
        try {
            System.out.println("Playing channel: " + currentChannel);
            AudioFormat format = new AudioFormat(44100, 16, 2, true, false); // Formato de áudio
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();

            byte[] audioChunk;
            while (playing && (audioChunk = this.audioStream.getAudioChunk(currentChannel)) != null) {
                line.write(audioChunk, 0, audioChunk.length);
                // Parou de imprimir o tamanho dos chunks
            }

            line.drain();
            line.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changeChannel(String newChannel) {
        currentChannel = newChannel;
        System.out.println("Canal trocado para: " + newChannel);
        stopPlaying();
        startPlaying(); // Inicia a reprodução automaticamente ao trocar de canal
    }

    public void stopPlaying() {
        playing = false;
        if (line != null && line.isOpen()) {
            line.stop();
            line.close();
        }
    }

    public void startPlaying() {
        playing = true;
        new Thread(this::playChannel).start(); // Inicia a reprodução em uma nova thread
    }

    public void init()
    {
        try
        {
            Scanner scanner = new Scanner(System.in);
            boolean exit = false;

            while (!exit) {
                System.out.println("\nMenu:");
                System.out.println("1. Mostrar canais disponíveis");
                System.out.println("2. Escolher canal");
                System.out.println("3. Trocar para o próximo canal");
                System.out.println("4. Parar de tocar");
                System.out.println("5. Sair");
                System.out.print("Escolha uma opção: ");
                int choice = scanner.nextInt();
                scanner.nextLine();  // Consome a nova linha

                switch (choice) {
                    case 1:
                        this.showChannels();
                        break;
                    case 2:
                        this.stopPlaying(); // Para a música antes de escolher o novo canal
                        System.out.print("Digite o nome do canal: ");
                        String channel = scanner.nextLine();
                        this.changeChannel(channel); // Muda o canal e já começa a tocar
                        break;
                    case 3:
                        this.stopPlaying(); // Para a música antes de trocar de canal
                        List<String> channels = this.audioStream.getChannels();
                        int currentIndex = channels.indexOf(this.currentChannel);
                        if (currentIndex != -1) {
                            // Se for o último canal, volta para o primeiro
                            if (currentIndex == channels.size() - 1) {
                                this.changeChannel(channels.get(0));
                            } else {
                                this.changeChannel(channels.get(currentIndex + 1));
                            }
                        }
                        break;
                    case 4:
                        this.stopPlaying(); // Para a reprodução
                        System.out.println("Reprodução parada.");
                        break;
                    case 5:
                        this.stopPlaying(); // Certifica-se de parar a reprodução antes de sair
                        exit = true;
                        System.out.println("Saindo...");
                        break;
                    default:
                        System.out.println("Opção inválida.");
                        break;
                }
            }
            scanner.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        
    }
}
