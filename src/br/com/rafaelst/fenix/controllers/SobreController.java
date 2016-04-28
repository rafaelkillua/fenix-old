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
        try {
            sobreGridPane.setOnMousePressed(event -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });
            sobreGridPane.setOnMouseDragged(event -> {
                sobreGridPane.getScene().getWindow().setX(event.getScreenX() - xOffset);
                sobreGridPane.getScene().getWindow().setY(event.getScreenY() - yOffset);
            });
        }catch (Exception e) {
            System.out.println("Erro do GridPane - " + e.getClass() + " - " + e.getMessage());
        }
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
            open(link);
        } catch (URISyntaxException e) {
            CriadorAlerta.criarAlertaErro("Erro de entrada", e.getMessage()).show();
        }
    }

    private void open(URI uri) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(uri);
            } catch (IOException e) {
                CriadorAlerta.criarAlertaErro("Erro de execução", e.getMessage()).show();
            }
        } else {
            CriadorAlerta.criarAlertaErro("Erro de execução", "Não é possível abrir esse link nesse computador.").show();
        }
    }
}
