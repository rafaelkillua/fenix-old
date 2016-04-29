package br.com.rafaelst.fenix.controllers;

import br.com.rafaelst.fenix.controllers.util.BancoDeDados;
import br.com.rafaelst.fenix.controllers.util.CriadorAlerta;
import br.com.rafaelst.fenix.models.Ano;
import br.com.rafaelst.fenix.models.Mes;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.ResourceBundle;

/**
 * Created by Rafael on 28/04/2016.
 */
public class AnosController implements Initializable {

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML private GridPane anosGridPane;
    @FXML private Button anosBotaoFechar;

    @FXML private TableView<Ano> tabelaAnos;
    @FXML private TableView<Mes> tabelaAnoAtual;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ativaMovimentoJanela();

        tabelaAnoAtual.setItems(BancoDeDados.getDadosMeses());
        tabelaAnos.setItems(BancoDeDados.getDadosAnos());

    }

    private void ativaMovimentoJanela() {
        anosGridPane.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        anosGridPane.setOnMouseDragged(event -> {
            anosGridPane.getScene().getWindow().setX(event.getScreenX() - xOffset);
            anosGridPane.getScene().getWindow().setY(event.getScreenY() - yOffset);
        });
    }

    @FXML
    protected void fecharAno() {
        TextInputDialog dialog = CriadorAlerta.criarTextInputDialog("Fechar Ano", "Digite o ano a ser fechado", "Ano:");

        dialog.setResult(getAnoHoje());

        dialog.showAndWait().ifPresent(name -> {
            if (!name.trim().equals("")) {
                try {
                    BancoDeDados.salvarAno(new Ano(-1, Integer.parseInt(name), getSaldoMeses()));
                } catch (NumberFormatException e) {
                    CriadorAlerta.criarAlertaErro("Erro de entrada", "Ano tem que ser número").showAndWait();
                } catch (SQLException|ClassNotFoundException e) {
                    CriadorAlerta.criarAlertaErro("Exceção", e.getClass() + " - " + e.getMessage());
                }
            } else {
                CriadorAlerta.criarAlertaErro("Erro de entrada", "Ano não pode ser vazio").showAndWait();
            }
        });
    }

    @FXML
    protected void fecharJanela() {
        Stage stage = (Stage) anosBotaoFechar.getScene().getWindow();
        stage.close();
    }

    private double getSaldoMeses() {
        double total = 0;
        for (Mes mes : BancoDeDados.getDadosMeses()) {
            total += mes.getEntradas() - mes.getSaidas();
        }
        return total;
    }

    private String getAnoHoje(){
        return String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
    }
}
