package com.aydsii.demo.utils;

public class GeneradorCodigoPedido {

    private static GeneradorCodigoPedido instancia;
    private int contador = 1000;

    private GeneradorCodigoPedido() {
    }

    public static synchronized GeneradorCodigoPedido getInstance() {
        if (instancia == null) {
            instancia = new GeneradorCodigoPedido();
        }
        return instancia;
    }

    public synchronized String generarCodigo() {
        contador++;
        return "VG-" + contador;
    }
}