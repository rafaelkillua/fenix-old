package br.com.rafaelst.fenix.controllers;

import br.com.rafaelst.fenix.controllers.util.CriadorAlerta;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Rafael on 28/04/2016.
 */
public class SobreController implements Initializable {

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML private GridPane sobreGridPane;
    @FXML private Button sobreBotaoFechar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ativaMovimentoJanela();
    }

    private void ativaMovimentoJanela() {
        sobreGridPane.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        sobreGridPane.setOnMouseDragged(event -> {
            sobreGridPane.getScene().getWindow().setX(event.getScreenX() - xOffset);
            sobreGridPane.getScene().getWindow().setY(event.getScreenY() - yOffset);
        });
    }

    @FXML
    protected void fecharJanela() {
        Stage stage = (Stage) sobreBotaoFechar.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void abrirSite() {
        try {
            final URI link = new URI("http://www.rafaelst.com.br");
            abrirLink(link);
        } catch (URISyntaxException e) {
            CriadorAlerta.criarAlertaErro("Exceção SC01", e.getClass() + " - " + e.getMessage()).showAndWait();
        }
    }

    private void abrirLink(URI uri) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(uri);
            } catch (IOException e) {
                CriadorAlerta.criarAlertaErro("Erro de execução SC02", e.getClass() + " - " + e.getMessage()).showAndWait();
            }
        } else {
            CriadorAlerta.criarAlertaErro("Erro de execução SC03", "Não é possível abrir esse link nesse computador.").showAndWait();
        }
    }
}
