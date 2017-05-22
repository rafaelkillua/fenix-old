package br.com.rafaelst.fenix.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.ObservableList;

/**
 * Created by Rafael on 28/04/2016.
 */
public class Dia {
    private final SimpleIntegerProperty id;
    private final SimpleLongProperty data;
    private final SimpleListProperty listaTransacoes;


    public Dia(int id, long data, ObservableList<Transacao> transacoes){
        this.id = new SimpleIntegerProperty(id);
        this.data = new SimpleLongProperty(data);
        this.listaTransacoes = new SimpleListProperty(transacoes);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public long getData() {
        return data.get();
    }

    public void setData(long data) {
        this.data.set(data);
    }

    public ObservableList<Transacao> getListaTransacoes() {
        return listaTransacoes.get();
    }

    public void setListaTransacoes(ObservableList<Transacao> listaTransacoes) {
        this.listaTransacoes.set(listaTransacoes);
    }
}
