package com.crnahuas.main;

import com.crnahuas.core.PrimesList;
import com.crnahuas.core.PrimesThread;

public class SafeVoteSystem {

    public static void main(String[] args) {
        System.out.println("Iniciando SafeVoteSystem...");

        // Lista compartida para almacenar los números primos.
        PrimesList listaPrimos = new PrimesList();

        // Número de hilos y cantidad de intentos por hilo.
        int cantidadHilos = 4;
        int intentosPorHilo = 50;

        // Arreglo de hilos.
        Thread[] hilos = new Thread[cantidadHilos];

        // Crea e inicia cada hilo.
        for (int i = 0; i < cantidadHilos; i++) {
            hilos[i] = new Thread(new PrimesThread(listaPrimos, intentosPorHilo));
            hilos[i].start();
        }

        // Espera a que todos los hilos terminen su ejecución.
        for (int i = 0; i < cantidadHilos; i++) {
            try {
                hilos[i].join();
            } catch (InterruptedException e) {
                System.out.println("Error al esperar el hilo " + i);
                Thread.currentThread().interrupt();
            }
        }

        // Muestra resultado final.
        System.out.println("\nProceso completado.");
        System.out.println("Total de números primos encontrados: " + listaPrimos.getPrimesCount());
    }
}
