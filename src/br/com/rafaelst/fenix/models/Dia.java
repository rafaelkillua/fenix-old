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
    private final SimpleLongProperty dia;
    private final SimpleListProperty listaEntradas;
    private final SimpleListProperty listaSaidas;


    public Dia(int id, long dia, ObservableList<Entrada> entradas, ObservableList<Saida> saidas){
        this.id = new SimpleIntegerProperty(id);
        this.dia = new SimpleLongProperty(dia);
        this.listaEntradas = new SimpleListProperty(entradas);
        this.listaSaidas = new SimpleListProperty(saidas);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public long getDia() {
        return dia.get();
    }

    public void setDia(int ano) {
        this.dia.set(ano);
    }

    public ObservableList<Entrada> getListaEntradas() {
        return listaEntradas.get();
    }

    public void setListaEntradas(ObservableList<Entrada> listaEntradas) {
        this.listaEntradas.set(listaEntradas);
    }

    public ObservableList<Saida> getListaSaidas() {
        return listaSaidas.get();
    }

    public void setListaSaidas(ObservableList<Saida> listaSaidas) {
        this.listaSaidas.set(listaSaidas);
    }
}
