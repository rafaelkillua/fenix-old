package br.com.rafaelst.fenix.models;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Created by Rafael on 28/04/2016.
 */
public class Ano {
    private final SimpleIntegerProperty id;
    private final SimpleIntegerProperty ano;
    private final SimpleDoubleProperty saldo;

    public Ano(int id, int ano, double saldo){
        this.id = new SimpleIntegerProperty(id);
        this.ano = new SimpleIntegerProperty(ano);
        this.saldo = new SimpleDoubleProperty(saldo);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getAno() {
        return ano.get();
    }

    public void setAno(int ano) {
        this.ano.set(ano);
    }

    public double getSaldo() {
        return saldo.get();
    }

    public void setSaldo(double saldo) {
        this.saldo.set(saldo);
    }

}
