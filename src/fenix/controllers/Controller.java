package fenix.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Controller {

    @FXML
    protected void fecharMes() {
        TextInputDialog dialog = criarTextInputDialog("Fechar Mês", "Digite o nome do mês a ser fechado", "Mês:");
        Alert alert = criarAlertaErro("Erro de entrada", "Mês não pode ser vazio");

        dialog.showAndWait().ifPresent(name -> {
            if (!name.trim().equals("")) {
                System.out.println("Digitou: " + name);
            } else {
                alert.showAndWait();
            }
        });
    }

    private TextInputDialog criarTextInputDialog(String titulo, String header, String pergunta) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(titulo);
        dialog.setHeaderText(header);
        dialog.setContentText(pergunta);
        return dialog;
    }

    private Alert criarAlertaErro(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert;
    }

    @FXML
    protected void abrirVisualizacaoPorAno() {
        try {
            Parent root1 = new FXMLLoader(getClass().getResource("/fenix/views/anos.fxml")).load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("Fênix Convites - Visualização por Ano");
            stage.setScene(new Scene(root1));
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            System.out.println("Erro " + e.getMessage());
        }
    }

    @FXML
    protected void abrirSobre() {
        try {
            Parent root1 = new FXMLLoader(getClass().getResource("/fenix/views/sobre.fxml")).load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("Fênix Convites - Sobre");
            stage.setScene(new Scene(root1));
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            System.out.println("Erro " + e.getMessage());
        }
    }

    @FXML
    protected void abrirSite() {
        try {
            final URI link = new URI("http://www.rafaelst.com.br");
            open(link);
        } catch (URISyntaxException e) {
            criarAlertaErro("Erro de entrada", e.getMessage()).show();
        }
    }

    @FXML private javafx.scene.control.Button botaoFechar;

    @FXML
    private void fecharStage(){
        Stage stage = (Stage) botaoFechar.getScene().getWindow();
        stage.close();
    }

    private void open(URI uri) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(uri);
            } catch (IOException e) {
                criarAlertaErro("Erro de entrada", e.getMessage()).show();
            }
        } else {
            criarAlertaErro("Erro de entrada", "Não é possível abrir esse link nesse computador.");
        }
    }
}
