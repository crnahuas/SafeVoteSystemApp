package com.crnahuas.files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Clase para manejar lectura y escritura de archivos en SafeVoteSystem.
public class FileHandler {

    // Lee un archivo CSV que contiene números (separados por comas o líneas).
    public static List<Integer> cargarPrimosDesdeCSV(String rutaArchivo) {
        List<Integer> primos = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] valores = linea.split(",");
                for (String valor : valores) {
                    try {
                        int numero = Integer.parseInt(valor.trim());
                        primos.add(numero);
                    } catch (NumberFormatException e) {
                        System.out.println("Se encontró un valor no válido y fue omitido: " + valor);
                    }
                }
            }
            System.out.println("Lectura de archivo exitosa: " + rutaArchivo);
        } catch (IOException e) {
            System.out.println("Ocurrió un problema al leer el archivo: " + rutaArchivo);
            e.printStackTrace();
        }

        return primos;
    }

    // Guarda un mensaje junto con su código primo en un archivo de texto.
    public static void guardarMensajeConCodigo(String rutaArchivo, String mensaje, int codigoPrimo) {
        try (FileWriter fw = new FileWriter(rutaArchivo, true);
             BufferedWriter bw = new BufferedWriter(fw)) {

            bw.write("Mensaje: " + mensaje + " | Código Primo: " + codigoPrimo);
            bw.newLine();
            System.out.println("Se guardó el mensaje correctamente: " + mensaje + " (" + codigoPrimo + ")");

        } catch (IOException e) {
            System.out.println("No se pudo escribir en el archivo: " + rutaArchivo);
            e.printStackTrace();
        }
    }
}