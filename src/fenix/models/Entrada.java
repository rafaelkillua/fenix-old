package fenix.models;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Rafael on 28/04/2016.
 */
public class Entrada {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty data;
    private final SimpleStringProperty historico;
    private final SimpleDoubleProperty valor;

    public Entrada(int id, String data, String historico, double valor) {
        this.id = new SimpleIntegerProperty(id);
        this.data = new SimpleStringProperty(data);
        this.historico = new SimpleStringProperty(historico);
        this.valor = new SimpleDoubleProperty(valor);
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

    public double getValor() {
        return valor.get();
    }

    public void setValor(double valor) {
        this.valor.set(valor);
    }
}