package com.crnahuas.concurrency;

import com.crnahuas.core.PrimesList;
import com.crnahuas.files.FileHandler;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

// Hilo que consume mensajes cifrados desde un Topic (BlockingQueue),
// asigna a cada uno un código primo de la lista y guarda el resultado.
public class TopicConsumerThread implements Runnable {

    private final BlockingQueue<String> topic;       // Cola de mensajes a procesar.
    private final PrimesList primesList;             // Lista de códigos primos disponibles.
    private final AtomicInteger primeIndex;          // Índice seguro para recorrer la lista de primos.

    public TopicConsumerThread(BlockingQueue<String> topic, PrimesList primesList, AtomicInteger primeIndex) {
        this.topic = topic;
        this.primesList = primesList;
        this.primeIndex = primeIndex;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Toma un mensaje desde la cola.
                String mensaje = topic.poll();

                // Si no hay más mensajes, el hilo termina.
                if (mensaje == null) {
                    System.out.println("Topic vacío, hilo finaliza.");
                    break;
                }

                // Obtiene un índice único para la lista de primos.
                int index;
                synchronized (primeIndex) {
                    index = primeIndex.getAndIncrement();
                }

                // Verifica que aún haya códigos primos disponibles.
                if (index < primesList.size()) {
                    int codigoPrimo = primesList.get(index);

                    // Guarda el mensaje con su código primo en el archivo.
                    FileHandler.guardarMensajeConCodigo(
                            "src/main/java/com/crnahuas/data/resultados.txt",
                            mensaje,
                            codigoPrimo
                    );
                } else {
                    System.out.println("No hay código primo disponible para: " + mensaje);
                }

                Thread.sleep(10); // Pausa.
            }

        } catch (InterruptedException e) {
            System.out.println("⛔ Hilo de Topic interrumpido.");
            Thread.currentThread().interrupt();
        }
    }
}
