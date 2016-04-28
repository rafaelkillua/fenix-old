package br.com.rafaelst.fenix.controllers;

import br.com.rafaelst.fenix.controllers.util.CriadorAlerta;
import br.com.rafaelst.fenix.models.Ano;
import br.com.rafaelst.fenix.models.Mes;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URL;
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
    @FXML private TableColumn colunaAno;
    @FXML private TableColumn colunaSaldo;

    @FXML private TableView<Mes> tabelaAnoAtual;
    @FXML private TableColumn colunaMes;
    @FXML private TableColumn colunaEntradas;
    @FXML private TableColumn colunaSaidas;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ativaMovimentoJanela();
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
        Alert alert = CriadorAlerta.criarAlertaErro("Erro de entrada", "Ano tem que ser número");
        Alert alertVazio = CriadorAlerta.criarAlertaErro("Erro de entrada", "Ano não pode ser vazio");

        dialog.showAndWait().ifPresent(name -> {
            if (!name.trim().equals("")) {
                try {
                    System.out.println(Integer.parseInt(name));
                } catch (Exception e) {
                    alert.showAndWait();
                }
            } else {
                alertVazio.showAndWait();
            }
        });
    }

    @FXML
    protected void fecharJanela() {
        Stage stage = (Stage) anosBotaoFechar.getScene().getWindow();
        stage.close();
    }
}
