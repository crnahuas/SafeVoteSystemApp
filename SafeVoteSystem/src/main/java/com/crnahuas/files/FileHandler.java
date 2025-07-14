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

    // Lee un archivo CSV de números primos.
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
                        System.out.println("⚠ Valor inválido omitido: " + valor);
                    }
                }
            }
            System.out.println("Archivo cargado con éxito: " + rutaArchivo);
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + rutaArchivo);
            e.printStackTrace();
        }

        return primos;
    }

    // Guarda mensaje y código primo en txt.
    public static void guardarMensajeConCodigo(String rutaArchivo, String mensaje, int codigoPrimo) {
        try (FileWriter fw = new FileWriter(rutaArchivo, true);
             BufferedWriter bw = new BufferedWriter(fw)) {

            bw.write("Mensaje: " + mensaje + " | Código Primo: " + codigoPrimo);
            bw.newLine();
            System.out.println("Mensaje guardado: " + mensaje + " (" + codigoPrimo + ")");

        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo: " + rutaArchivo);
            e.printStackTrace();
        }
    }
}