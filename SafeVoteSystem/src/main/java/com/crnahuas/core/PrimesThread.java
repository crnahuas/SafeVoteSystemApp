package com.crnahuas.core;

import com.crnahuas.concurrency.PrimeMonitor;
import java.util.Random;

//Hilo que genera y agrega números primos a una lista compartida.
public class PrimesThread implements Runnable {

    private final PrimesList lista;
    private final PrimeMonitor monitor;
    private final int cantidad;
    private final boolean usaMonitor;

    // Constructor para uso con PrimesList
    public PrimesThread(PrimesList lista, int cantidad) {
        this.lista = lista;
        this.monitor = null;
        this.cantidad = cantidad;
        this.usaMonitor = false;
    }

    // Constructor para uso con PrimeMonitor
    public PrimesThread(PrimeMonitor monitor, int cantidad) {
        this.lista = null;
        this.monitor = monitor;
        this.cantidad = cantidad;
        this.usaMonitor = true;
    }

    @Override
    public void run() {
        Random random = new Random();
        int generados = 0;

        while (generados < cantidad && !Thread.currentThread().isInterrupted()) {
            int numero = 2 + random.nextInt(100);

            if (usaMonitor) {
                try {
                    monitor.publicarNumero(numero);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("El hilo productor fue detenido mientras intentaba enviar un número.");
                    break;
                }
            } else {
                if (lista.isPrime(numero)) {
                    synchronized (lista) {
                        lista.add(numero);
                        System.out.println("Número primo agregado: " + numero);
                    }
                } else {
                    System.out.println("No es primo: " + numero);
                }
            }

            generados++;

            try {
                Thread.sleep(80);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("El hilo productor fue detenido mientras esperaba.");
                break;
            }
        }
    }
} 
