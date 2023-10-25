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
            /*
            Process proceso = Runtime.getRuntime().exec(comando);
            int exitVal = proceso.waitFor();
            if (exitVal != 0) {
                System.out.println("Error ejecutando el comando.");
            } else {
                imprimirResultado(proceso.getInputStream());
            }
*/
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
