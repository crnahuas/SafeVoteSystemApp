package com.crnahuas.core;

import java.util.ArrayList;

// Lista especializada que solo acepta números primos.
// Restringe la inserción de no primos.
public class PrimesList extends ArrayList<Integer> {

    // Verifica si un número es primo
    public boolean isPrime(int number) {
        if (number < 2) {
            return false; // No hay primos menores que 2
        }
        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) {
                return false; // Tiene divisor → no es primo
            }
        }
        return true;
    }

    // Agrega un número solo si es primo, si no lanza excepción
    @Override
    public boolean add(Integer number) {
        if (isPrime(number)) {
            System.out.println("Número primo agregado: " + number);
            return super.add(number);
        } else {
            throw new IllegalArgumentException("El número " + number + " no es primo.");
        }
    }


}
