package com.redes.app;

import java.rmi.Naming;

public class Main {

    
    public static void main(String[] args)
    {
        try
        {
            if (args.length > 0)
            {
                if ( args[0].equals("client"))
                {
                    // Conecta ao serviço de áudio via RMI
                    AudioStreamClient audioStreamClient = new AudioStreamClient((AudioStream) Naming.lookup("rmi://localhost:1099/AudioStreamService"));
                    audioStreamClient.init();
                }
                else if ( args[0].equals("server") )
                {
                    // Inicializa o servidor e registra o serviço RMI
                    AudioStreamServer server = new AudioStreamServer();
                    java.rmi.Naming.rebind("rmi://localhost:1099/AudioStreamService", server);
                    System.out.println("Servidor de Áudio RMI iniciado...");
                }
                else {
                    System.out.println("Nenhum argumento válido foi passado.");

                }
            }
            else
            {
                System.out.println("Nenhum argumento foi passado.");
            }

            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
