package com.crnahuas.core;

import java.util.Random;

//Hilo que genera y agrega números primos a una lista compartida.
public class PrimesThread implements Runnable {

    private final PrimesList primesList; // Lista compartida entre hilos.
    private final int intentos;          // Cantidad de intentos para generar primos.
    private final Random random;

    public PrimesThread(PrimesList primesList, int intentos) {
        this.primesList = primesList;
        this.intentos = intentos;
        this.random = new Random();
    }

    @Override
    public void run() {
        for (int i = 0; i < intentos; i++) {
            int numero = random.nextInt(10000); // Genera número aleatorio.

            // Verifica si es primo usando PrimesList.
            if (primesList.isPrime(numero)) {
                synchronized (primesList) {
                    try {
                        primesList.add(numero);
                    } catch (IllegalArgumentException ex) {
                        System.out.println("Error " + ex.getMessage());
                    }
                }
            } else {
                System.out.println("Número descartado no es primo: " + numero);
            }

            // Espera breve.
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                System.out.println("Error el hilo fue interrumpido.");
                Thread.currentThread().interrupt();
            }
        }
    }
}
