package com.crnahuas.main;

import com.crnahuas.concurrency.PrimeMonitor;
import com.crnahuas.concurrency.PrimesConsumerThread;
import com.crnahuas.concurrency.TopicConsumerThread;
import com.crnahuas.core.PrimesList;
import com.crnahuas.core.PrimesThread;
import com.crnahuas.files.FileHandler;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

// SafeVoteSystem - Clase principal que coordina los hilos y estructuras.
public class SafeVoteSystem {

    public static void main(String[] args) {

        System.out.println("\n================ INICIO DE SafeVoteSystem ================\n");

        // Uso de PrimesThread
        System.out.println("\n----- Ejecutando concurrencia básica -----\n");
        PrimesList listaPaso1 = new PrimesList();
        Thread hiloPaso1 = new Thread(new PrimesThread(listaPaso1, 10));
        hiloPaso1.start();

        try {
            hiloPaso1.join();
            System.out.println("\nPrimos encontrados: " + listaPaso1.getPrimesCount() + "\n");
        } catch (InterruptedException e) {
            System.out.println("\nNo se pudo completar la espera del hilo que genera los números.\n");
            Thread.currentThread().interrupt();
        }

        // Cargar números primos desde CSV.
        System.out.println("\n----- Cargando números desde archivo CSV -----\n");
        List<Integer> numerosDesdeArchivo = FileHandler.cargarPrimosDesdeCSV("src/main/java/com/crnahuas/data/primos.csv");

        // Lista de primos compartida.
        PrimesList listaPrimos = new PrimesList();

        // Distribuir los números.
        BlockingQueue<Integer> colaPrimos = new LinkedBlockingQueue<>(numerosDesdeArchivo);

        // Iniciar hilos.
        System.out.println("\n----- Iniciando hilos -----\n");
        int cantidadConsumidores = 3;
        Thread[] consumidores = new Thread[cantidadConsumidores];

        for (int i = 0; i < cantidadConsumidores; i++) {
            consumidores[i] = new Thread(new PrimesConsumerThread(colaPrimos, listaPrimos));
            consumidores[i].start();
        }

        // Esperar que terminen.
        for (int i = 0; i < cantidadConsumidores; i++) {
            try {
                consumidores[i].join();
            } catch (InterruptedException e) {
                System.out.println("\nNo se pudo completar la espera del hilo consumidor " + i + "\n");
                Thread.currentThread().interrupt();
            }
        }

        // Cantidad total de primos encontrados.
        System.out.println("\nTotal de números primos encontrados: " + listaPrimos.getPrimesCount() + "\n");

        // Mensajes simulados.
        System.out.println("\n----- Procesamiento de mensajes -----\n");
        BlockingQueue<String> topic = new LinkedBlockingQueue<>();
        topic.add("Voto cifrado: Usuario A");
        topic.add("Voto cifrado: Usuario B");
        topic.add("Voto cifrado: Usuario C");
        topic.add("Voto cifrado: Usuario D");
        topic.add("Voto cifrado: Usuario E");
        topic.add("Voto cifrado: Usuario F");
        topic.add("Voto cifrado: Usuario G");
        topic.add("Voto cifrado: Usuario H");

        // Gestionar índice de códigos primos.
        AtomicInteger indicePrimos = new AtomicInteger(0);

        // Iniciar hilos del Topic.
        int cantidadTopic = 2;
        Thread[] topicThreads = new Thread[cantidadTopic];

        for (int i = 0; i < cantidadTopic; i++) {
            topicThreads[i] = new Thread(new TopicConsumerThread(topic, listaPrimos, indicePrimos));
            topicThreads[i].start();
        }

        // Esperar que terminen los hilos.
        for (int i = 0; i < cantidadTopic; i++) {
            try {
                topicThreads[i].join();
            } catch (InterruptedException e) {
                System.out.println("\nNo se pudo completar la espera del hilo de mensajes " + i + "\n");
                Thread.currentThread().interrupt();
            }
        }

        // Wait/Notify
        System.out.println("\n----- Ejecutando módulo con wait/notify -----\n");
        PrimeMonitor monitor = new PrimeMonitor();
        PrimesList listaSync = new PrimesList();

        Thread productor = new Thread(new PrimesThread(monitor, 20));
        productor.start();

        int hilosConsumidoresSync = 2;
        Thread[] consumidoresSync = new Thread[hilosConsumidoresSync];

        for (int i = 0; i < hilosConsumidoresSync; i++) {
            consumidoresSync[i] = new Thread(new PrimesConsumerThread(monitor, listaSync));
            consumidoresSync[i].start();
        }

        try {
            productor.join();
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println("\nNo se pudo completar la espera del módulo de sincronización.\n");
            Thread.currentThread().interrupt();
        }

        for (Thread t : consumidoresSync) {
            t.interrupt();
        }

        System.out.println("\nTotal de primos encontrados (wait/notify): " + listaSync.getPrimesCount() + "\n");

        // Mensaje final.
        System.out.println("\n================ FIN DE SafeVoteSystem ================\n");
        System.out.println("\nSistema completado. Resultados guardados en resultados.txt\n");
    }
}




