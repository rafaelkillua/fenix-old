package br.com.rafaelst.fenix.models;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Rafael on 28/04/2016.
 */
public class Saida {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty data;
    private final SimpleStringProperty historico;
    private final SimpleDoubleProperty contas;
    private final SimpleDoubleProperty depositos;

    private Saida(int id, String data, String historico, double contas, double depositos) {
        this.id = new SimpleIntegerProperty(id);
        this.data = new SimpleStringProperty(data);
        this.historico = new SimpleStringProperty(historico);
        this.contas = new SimpleDoubleProperty(contas);
        this.depositos = new SimpleDoubleProperty(depositos);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getData() {
        return data.get();
    }

    public void setData(String data) {
        this.data.set(data);
    }

    public String getHistorico() {
        return historico.get();
    }

    public void setHistorico(String historico) {
        this.historico.set(historico);
    }

    public double getContas() {
        return contas.get();
    }

    public void setContas(double valor) {
        this.contas.set(valor);
    }

    public double getDepositos() {
        return depositos.get();
    }

    public void setDepositos(double depositos) {
        this.depositos.set(depositos);
    }
}
