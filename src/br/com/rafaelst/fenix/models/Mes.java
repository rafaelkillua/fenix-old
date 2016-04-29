package br.com.rafaelst.fenix.models;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Rafael on 28/04/2016.
 */
public class Mes {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty mes;
    private final SimpleDoubleProperty entradas;
    private final SimpleDoubleProperty saidas;

    public Mes(int id, String mes, double entradas, double saidas){
        this.id = new SimpleIntegerProperty(id);
        this.mes = new SimpleStringProperty(mes);
        this.entradas = new SimpleDoubleProperty(entradas);
        this.saidas = new SimpleDoubleProperty(saidas);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getMes() {
        return mes.get();
    }

    public void setMes(String mes) {
        this.mes.set(mes);
    }

    public double getEntradas() {
        return entradas.get();
    }

    public void setEntradas(double entradas) {
        this.entradas.set(entradas);
    }

    public double getSaidas() {
        return saidas.get();
    }

    public void setSaidas(double saidas) {
        this.saidas.set(saidas);
    }
}
