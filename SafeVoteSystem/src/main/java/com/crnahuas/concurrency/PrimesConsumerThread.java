package com.crnahuas.concurrency;

import com.crnahuas.core.PrimesList;
import java.util.concurrent.BlockingQueue;

// Hilo que consume números desde una cola (Queue).
// Verifica si son primos y los agrega a la lista PrimesList.
public class PrimesConsumerThread implements Runnable {

    private final BlockingQueue<Integer> queue; // Cola de números a procesar
    private final PrimesList primesList;        // Lista donde se almacenan los primos

    public PrimesConsumerThread(BlockingQueue<Integer> queue, PrimesList primesList) {
        this.queue = queue;
        this.primesList = primesList;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Toma un número desde la cola.
                Integer numero = queue.poll();

                // Si no hay más números, el hilo finaliza.
                if (numero == null) {
                    System.out.println("Cola vacía, finalización de hilo.");
                    break;
                }

                // Verifica si el número es primo
                if (primesList.isPrime(numero)) {
                    // Acceso sincronizado a la lista para evitar conflictos.
                    synchronized (primesList) {
                        primesList.add(numero);
                    }
                } else {
                    System.out.println("No es primo: " + numero);
                }

                Thread.sleep(10); // Pausa.
            }
        } catch (InterruptedException e) {
            System.out.println("El hilo fue interrumpido.");
            Thread.currentThread().interrupt(); // Marca el hilo como interrumpido.
        }
    }
}