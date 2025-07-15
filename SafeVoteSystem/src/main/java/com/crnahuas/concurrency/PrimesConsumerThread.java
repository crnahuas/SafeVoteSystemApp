package com.crnahuas.concurrency;

import com.crnahuas.core.PrimesList;
import java.util.concurrent.BlockingQueue;

public class PrimesConsumerThread implements Runnable {

    private final BlockingQueue<Integer> queue;
    private final PrimeMonitor monitor;
    private final PrimesList lista;
    private final boolean usaMonitor;

    // Constructor para uso con BlockingQueue.
    public PrimesConsumerThread(BlockingQueue<Integer> queue, PrimesList lista) {
        this.queue = queue;
        this.lista = lista;
        this.monitor = null;
        this.usaMonitor = false;
    }

    // Constructor para uso con PrimeMonitor.
    public PrimesConsumerThread(PrimeMonitor monitor, PrimesList lista) {
        this.queue = null;
        this.lista = lista;
        this.monitor = monitor;
        this.usaMonitor = true;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Integer numero;

                if (usaMonitor) {
                    numero = monitor.obtenerNumero();
                } else {
                    numero = queue.poll();
                    if (numero == null) {
                        System.out.println("Cola vacía, finalización de hilo.");
                        break;
                    }
                }

                if (lista.isPrime(numero)) {
                    synchronized (lista) {
                        lista.add(numero);
                        System.out.println("Número primo agregado: " + numero);
                    }
                } else {
                    System.out.println("No es primo: " + numero);
                }

                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Hilo interrumpido.");
        }
    }
}
