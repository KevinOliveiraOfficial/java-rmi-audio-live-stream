package com.redes.app;

public class Main {

    
    public static void main(String[] args) {
        try {

            // Inicializa o servidor e registra o serviço RMI
            AudioStreamServer server = new AudioStreamServer();
            java.rmi.Naming.rebind("rmi://localhost:1099/AudioStreamService", server);
            System.out.println("Servidor de Áudio RMI iniciado...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
