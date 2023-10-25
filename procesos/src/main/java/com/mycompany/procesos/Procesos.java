/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.procesos;


import java.io.*;
import java.util.concurrent.*;


/**
 *
 * @author davidperezelrincon
 */

public class Procesos {

    /**
     * Ejecuta un comando y muestra su salida.
     * @param comando Los componentes del comando a ejecutar, incluyendo el nombre del comando y sus argumentos.
     */
    
    public void ejecutaComando(String... comando) {
        ProcessBuilder pb = new ProcessBuilder();
        pb.command(comando);
        
        try {
            Process proceso = pb.start();
            imprimirResultado(proceso.getInputStream());

            // Esperar a que el proceso finalice y comprobar si se ha ejecutado correctamente.
            int exitVal = proceso.waitFor();
            if (exitVal != 0) {
                System.out.println("Error ejecutando el comando.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    /*
     * Ejecuta un comando y verifica periódicamente si ha finalizado, mostrando un mensaje mientras espera.
     * @param comando El comando a ejecutar.
     * @param intervalo Intervalo de tiempo (en segundos) entre cada comprobación del estado del proceso.
     */
    public void ejecutaComandoyComprueba(String comando, int intervalo) {
        ProcessBuilder pb = new ProcessBuilder();
        pb.command(comando.split(" "));
        
        try {
            Process proceso = pb.start();
            while (proceso.isAlive()) {
                System.out.println("Esperando...");
                Thread.sleep(intervalo * 1000);
            }
            imprimirResultado(proceso.getInputStream());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    
    /**
     * Ejecuta un comando y espera un máximo de 5 segundos antes de forzar la finalización.
     * @param comando Los componentes del comando a ejecutar.
     */
    public void ejecutaComandoyEspera(String... comando) {
        ProcessBuilder pb = new ProcessBuilder();
        pb.command(comando);
        
        try {
            Process proceso = pb.start();
            boolean termino = proceso.waitFor(5, TimeUnit.SECONDS);
            if (!termino) {
                proceso.destroyForcibly();
                System.out.println("Proceso terminado después de esperar 5 segundos.");
            } else {
                imprimirResultado(proceso.getInputStream());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ejecuta un comando en un directorio específico.
     * @param directorio La ruta al directorio en el que se ejecutará el comando.
     * @param comando Los componentes del comando a ejecutar.
     */
    public void ejecutaComandoDirectorio(String directorio, String... comando) {
        ProcessBuilder pb = new ProcessBuilder();
        pb.command(comando);
        pb.directory(new File(directorio));
        
        try {
            Process proceso = pb.start();
            imprimirResultado(proceso.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Busca una cadena de texto dentro de un archivo y guarda la salida en otro archivo.
     * @param texto El texto a buscar dentro del archivo.
     * @param archivoEntrada El archivo en el que buscar.
     * @param archivoSalida El archivo donde se guardará la salida.
     */
    public void buscayGuarda(String texto, String archivoEntrada, String archivoSalida) {
        String comando = "grep";  // o "findstr" en Windows
        ProcessBuilder pb = new ProcessBuilder();
        pb.command(comando, texto, archivoEntrada);

        try (FileWriter writer = new FileWriter(archivoSalida)) {
            Process proceso = pb.start();
            // Buffer para leer la salida del proceso.
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(proceso.getInputStream()))) {
                String linea;
                // Leer cada línea de la salida y escribirla en el archivo de salida.
                while ((linea = reader.readLine()) != null) {
                    writer.write(linea + System.lineSeparator());
                }
            }
            proceso.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void imprimirResultado(InputStream input) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                System.out.println(linea);
            }
        }
    }

    public static void main(String[] args) {
        Procesos proc = new Procesos();

        // Prueba de los métodos
        proc.ejecutaComando("ls", "-l");
        proc.ejecutaComandoyComprueba("sleep 3", 1);
        proc.ejecutaComandoyEspera("sleep", "7");
        proc.ejecutaComandoDirectorio("/tmp", "ls");
        proc.buscayGuarda("textoBuscar", "archivoEntrada.txt", "archivoSalida.txt");
    }
}
/*
public class Procesos {

    public static void main(String[] args) {
        System.out.println("Hello World!");
    }
    
    // 1. Método para ejecutar un comando
    public void ejecutaComando(String comando) {
        try {
            Process proceso = Runtime.getRuntime().exec(comando);
            int exitVal = proceso.waitFor();
            if (exitVal != 0) {
                System.out.println("Error ejecutando el comando.");
            } else {
                imprimirResultado(proceso.getInputStream());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ejecutaComandoyComprueba(String comando, int intervalo) throws InterruptedException {
        try {
            Process proceso = Runtime.getRuntime().exec(comando);
            while (proceso.isAlive()) {
                System.out.println("Esperando...");
                Thread.sleep(intervalo * 1000);
            }
            imprimirResultado(proceso.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ejecutaComandoyEspera(String comando) {
        try {
            Process proceso = Runtime.getRuntime().exec(comando);
            if (!proceso.waitFor(5, TimeUnit.SECONDS)) {
                proceso.destroy();
                System.out.println("Proceso terminado después de esperar 5 segundos.");
            } else {
                imprimirResultado(proceso.getInputStream());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ejecutaComandoDirectorio(String comando, String directorio) {
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command("sh", "-c", comando);
            builder.directory(new File(directorio));
            Process proceso = builder.start();
            imprimirResultado(proceso.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void buscayGuarda(String texto, String archivoEntrada, String archivoSalida) {
        try (FileWriter writer = new FileWriter(archivoSalida);
             BufferedReader reader = new BufferedReader(new FileReader(archivoEntrada))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.contains(texto)) {
                    writer.write(linea + System.lineSeparator());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void imprimirResultado(InputStream input) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                System.out.println(linea);
            }
        }
    }
}
*/