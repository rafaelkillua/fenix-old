package br.com.rafaelst.fenix.controllers;

import br.com.rafaelst.fenix.models.Entrada;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.converter.DoubleStringConverter;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private double xOffset = 0;
    private double yOffset = 0;

    private final DecimalFormat formatadorDeDouble = new DecimalFormat("0.00");

    @FXML private GridPane gridPane;
    @FXML private Button botaoMinimizar;
    @FXML private Button botaoFechar;

    @FXML private TextField campoTotalEntradas;
    @FXML private TableView<Entrada> tabelaEntrada;
    @FXML private TableColumn colunaData;
    @FXML private TableColumn colunaValor;
    @FXML private TableColumn colunaHistorico;

    private ObservableList<Entrada> data = FXCollections.observableArrayList(
            new Entrada(0, "antes de ontem", "venda", 10),
            new Entrada(0, "ontem", "venda", 20),
            new Entrada(0, "hoje", "venda", 30),
            new Entrada(0, "amanha", "venda", 40),
            new Entrada(0, "depois de amanha", "venda", 50)
    );

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            for (int i = 0; i < 50; i++) {
                data.add(new Entrada(i, String.valueOf(i*30), "oi", i*50));
            }
            tabelaEntrada.setItems(data);
        }catch (Exception e) {
            System.out.println("Erro ao setar itens da tabela - " + e.getClass() + " - " + e.getMessage());
        }
        try {
            gridPane.setOnMousePressed(event -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });
            gridPane.setOnMouseDragged(event -> {
                gridPane.getScene().getWindow().setX(event.getScreenX() - xOffset);
                gridPane.getScene().getWindow().setY(event.getScreenY() - yOffset);
            });
        }catch (Exception e) {
            System.out.println("Erro do GridPane - " + e.getClass() + " - " + e.getMessage());
        }

        try {
            colunaData.setCellFactory(TextFieldTableCell.forTableColumn());
            colunaData.setOnEditCommit(
                    new EventHandler<CellEditEvent<Entrada, String>>() {
                        @Override
                        public void handle(CellEditEvent<Entrada, String> t) {
                            (t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())
                            ).setData(t.getNewValue());
                        }
                    }
            );

            colunaHistorico.setCellFactory(TextFieldTableCell.forTableColumn());
            colunaHistorico.setOnEditCommit(
                    new EventHandler<CellEditEvent<Entrada, String>>() {
                        @Override
                        public void handle(CellEditEvent<Entrada, String> t) {
                            (t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())
                            ).setHistorico(t.getNewValue());
                        }
                    }
            );

            colunaValor.setCellFactory(TextFieldTableCell.<Entrada, Double>forTableColumn(new DoubleStringConverter()));
            colunaValor.setOnEditCommit(
                    new EventHandler<CellEditEvent<Entrada, Double>>() {
                        @Override
                        public void handle(CellEditEvent<Entrada, Double> t) {
                            (t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())
                            ).setValor(t.getNewValue());
                            atualizarTotalEntradas();
                        }
                    }
            );
        }catch (Exception e) {
            System.out.println("Erro ao preencher colunas e editá-las - " + e.getClass() + " - " + e.getMessage());
        }
        try {
            atualizarTotalEntradas();
        }catch (Exception e) {
            System.out.println("Erro ao setar total de entradas - " + e.getClass() + " - " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    protected void fecharJanela() {
        Stage stage = (Stage) botaoFechar.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void minimizarJanela() {
        Stage stage = (Stage) botaoMinimizar.getScene().getWindow();
        stage.setIconified(true);
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
            Parent root1 = new FXMLLoader(getClass().getResource("/br/com/rafaelst/fenix/views/anos.fxml")).load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("Fênix Convites - Visualização por Ano");
            stage.setScene(new Scene(root1, 800, 600));
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            System.out.println("Erro ao abrir Ano - " + e.getClass() + " - " + e.getMessage());
        }
    }

    @FXML
    protected void abrirSobre() {
        try {
            Parent root1 = new FXMLLoader(getClass().getResource("/br/com/rafaelst/fenix/views/sobre.fxml")).load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("Fênix Convites - Sobre");
            stage.setScene(new Scene(root1, 400, 300));
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            System.out.println("Erro ao abrir Sobre - " + e.getClass() + " - " + e.getMessage());
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

    public void atualizarTotalEntradas() {
        double total = 0;
        for (Entrada entrada : tabelaEntrada.getItems()) {
            total += entrada.getValor();
        }
        campoTotalEntradas.setText(String.valueOf(total));
    }
}