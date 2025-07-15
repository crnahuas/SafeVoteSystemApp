package com.crnahuas.concurrency;

// Sincronizar productores y consumidores de números primos.
public class PrimeMonitor {
    private int numero;
    private boolean disponible = true;

    // Publica un número
    public synchronized void publicarNumero(int numero) throws InterruptedException {
        while (!disponible) {
            wait(); // Espera hasta que se consuma el anterior.
        }
        this.numero = numero;
        disponible = false;
        notifyAll(); // avisa a los consumidores
    }

    // Obtiene un número
    public synchronized int obtenerNumero() throws InterruptedException {
        while (disponible) {
            wait(); // Espera hasta que haya un nuevo número.
        }
        disponible = true;
        notifyAll(); // Avisa que ya se consumió.
        return numero;
    }
}
