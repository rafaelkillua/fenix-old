package fenix.controllers;

import fenix.models.Entrada;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    @FXML private TableView<Entrada> tabelaEntrada;

    private ObservableList<Entrada> data = FXCollections.observableArrayList(
            new Entrada(0, "antes de ontem", "venda", 10),
            new Entrada(0, "ontem", "venda", 20),
            new Entrada(0, "hoje", "venda", 30),
            new Entrada(0, "amanha", "venda", 40),
            new Entrada(0, "depois de amanha", "venda", 50)
    );

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for (int i = 0; i < 50; i++) {
            data.add(new Entrada(i, String.valueOf(i*30), "oi", i*50));
        }
        tabelaEntrada.setItems(data);
    }

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

    @FXML private Button botaoFechar;

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
                criarAlertaErro("Erro de execução", e.getMessage()).show();
            }
        } else {
            criarAlertaErro("Erro de execução", "Não é possível abrir esse link nesse computador.").show();
        }
    }
}