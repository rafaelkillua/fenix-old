package br.com.rafaelst.fenix.models;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Rafael on 28/04/2016.
 */
public class Ano {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty ano;
    private final SimpleDoubleProperty saldo;

    private Ano(int id, String ano, double saldo){
        this.id = new SimpleIntegerProperty(id);
        this.ano = new SimpleStringProperty(ano);
        this.saldo = new SimpleDoubleProperty(saldo);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getAno() {
        return ano.get();
    }

    public void setAno(String ano) {
        this.ano.set(ano);
    }

    public double getSaldo() {
        return saldo.get();
    }

    public void setSaldo(double saldo) {
        this.saldo.set(saldo);
    }

}
