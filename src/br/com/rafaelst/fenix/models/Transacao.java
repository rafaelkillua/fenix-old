package br.com.rafaelst.fenix.models;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Rafael on 28/04/2016.
 */
public class Transacao {

    private final SimpleIntegerProperty id;
    private final SimpleStringProperty descricao;
    private final SimpleDoubleProperty valor;

    public Transacao(int id, String descricao, double valor) {
        this.id = new SimpleIntegerProperty(id);
        this.descricao = new SimpleStringProperty(descricao);
        this.valor = new SimpleDoubleProperty(valor);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getDescricao() {
        return descricao.get();
    }

    public void setDescricao(String descricao) {
        this.descricao.set(descricao);
    }

    public double getValor() {
        return valor.get();
    }

    public void setValor(double valor) {
        this.valor.set(valor);
    }
}
