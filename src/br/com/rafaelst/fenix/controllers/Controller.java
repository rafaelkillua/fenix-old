package br.com.rafaelst.fenix.controllers;

import br.com.rafaelst.fenix.controllers.util.CriadorAlerta;
import br.com.rafaelst.fenix.models.Entrada;
import br.com.rafaelst.fenix.models.Saida;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.converter.DoubleStringConverter;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private double xOffset = 0;
    private double yOffset = 0;

    //private final DecimalFormat formatadorDeDouble = new DecimalFormat("0.00");

    @FXML private GridPane gridPane;
    @FXML private Button botaoFechar;
    @FXML private Button botaoMinimizar;

    @FXML private TableView<Entrada> tabelaEntrada;
    @FXML private TableColumn colunaDataEntrada;
    @FXML private TableColumn colunaHistoricoEntrada;
    @FXML private TableColumn colunaValor;

    @FXML private TextField campoTotalEntradas;

    @FXML private TableView<Saida> tabelaSaida;
    @FXML private TableColumn colunaDataSaida;
    @FXML private TableColumn colunaHistoricoSaida;
    @FXML private TableColumn colunaDepositos;
    @FXML private TableColumn colunaContas;

    @FXML private TextField campoTotalContas;
    @FXML private TextField campoTotalDepositos;
    @FXML private TextField campoTotalSaidas;

    @FXML private TextField campoSaldo;

    private ObservableList<Entrada> dadosEntrada = FXCollections.observableArrayList();
    private ObservableList<Saida> dadosSaida = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ativaMovimentoJanela();

        for (int i = 0; i < 5; i++) {
            dadosEntrada.add(new Entrada(i, String.valueOf(i*30), "oi", i*50));
            dadosSaida.add(new Saida(i, String.valueOf(i*50), "io", i*10, i*10));
        }
        tabelaEntrada.setItems(dadosEntrada);
        tabelaSaida.setItems(dadosSaida);

        ativaEdicaoTabelaEntrada();
        ativaEdicaoTabelaSaida();

        atualizarTotalEntradas();

        atualizarTotalContas();
        atualizarTotalDepositos();
        atualizarTotalSaidas();

        atualizarSaldo();
    }

    private void ativaMovimentoJanela() {
        gridPane.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        gridPane.setOnMouseDragged(event -> {
            gridPane.getScene().getWindow().setX(event.getScreenX() - xOffset);
            gridPane.getScene().getWindow().setY(event.getScreenY() - yOffset);
        });
    }

    private void ativaEdicaoTabelaEntrada() {
        colunaDataEntrada.setCellFactory(TextFieldTableCell.<Entrada>forTableColumn());
        colunaDataEntrada.setOnEditCommit(
                new EventHandler<CellEditEvent<Entrada, String>>() {
                    @Override
                    public void handle(CellEditEvent<Entrada, String> t) {
                        (t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setData(t.getNewValue());
                    }
                }
        );

        colunaHistoricoEntrada.setCellFactory(TextFieldTableCell.<Entrada>forTableColumn());
        colunaHistoricoEntrada.setOnEditCommit(
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
    }

    private void ativaEdicaoTabelaSaida() {
        colunaDataSaida.setCellFactory(TextFieldTableCell.<Saida>forTableColumn());
        colunaDataSaida.setOnEditCommit(
                new EventHandler<CellEditEvent<Saida, String>>() {
                    @Override
                    public void handle(CellEditEvent<Saida, String> t) {
                        (t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setData(t.getNewValue());
                    }
                }
        );

        colunaHistoricoSaida.setCellFactory(TextFieldTableCell.<Saida>forTableColumn());
        colunaHistoricoSaida.setOnEditCommit(
                new EventHandler<CellEditEvent<Saida, String>>() {
                    @Override
                    public void handle(CellEditEvent<Saida, String> t) {
                        (t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setHistorico(t.getNewValue());
                    }
                }
        );

        colunaContas.setCellFactory(TextFieldTableCell.<Saida, Double>forTableColumn(new DoubleStringConverter()));
        colunaContas.setOnEditCommit(
                new EventHandler<CellEditEvent<Saida, Double>>() {
                    @Override
                    public void handle(CellEditEvent<Saida, Double> t) {
                        (t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setContas(t.getNewValue());
                        atualizarTotalEntradas();
                    }
                }
        );

        colunaDepositos.setCellFactory(TextFieldTableCell.<Saida, Double>forTableColumn(new DoubleStringConverter()));
        colunaDepositos.setOnEditCommit(
                new EventHandler<CellEditEvent<Saida, Double>>() {
                    @Override
                    public void handle(CellEditEvent<Saida, Double> t) {
                        (t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setDepositos(t.getNewValue());
                        atualizarTotalEntradas();
                    }
                }
        );
    }

    @FXML
    protected void fecharMes() {
        TextInputDialog dialog = CriadorAlerta.criarTextInputDialog("Fechar Mês", "Digite o nome do mês a ser fechado", "Mês:");
        Alert alert = CriadorAlerta.criarAlertaErro("Erro de entrada", "Mês não pode ser vazio");

        dialog.showAndWait().ifPresent(name -> {
            if (!name.trim().equals("")) {
                System.out.println("Digitou: " + name);
            } else {
                alert.showAndWait();
            }
        });
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
    protected void fecharJanela() {
        Stage stage = (Stage) botaoFechar.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void minimizarJanela() {
        Stage stage = (Stage) botaoMinimizar.getScene().getWindow();
        stage.setIconified(true);
    }

    public void atualizarTotalEntradas() {
        campoTotalEntradas.setText(String.valueOf(getTotalEntradas()));
    }

    public double getTotalEntradas() {
        double total = 0;
        for (Entrada entrada : tabelaEntrada.getItems()) {
            total += entrada.getValor();
        }
        return total;
    }

    public void atualizarTotalContas() {
        campoTotalContas.setText(String.valueOf(getTotalContas()));
    }

    public double getTotalContas() {
        double total = 0;
        for (Saida saida : tabelaSaida.getItems()) {
            total += saida.getContas();
        }
        return total;
    }

    public void atualizarTotalDepositos() {
        campoTotalDepositos.setText(String.valueOf(getTotalDepositos()));
    }

    public double getTotalDepositos() {
        double total = 0;
        for (Saida saida : tabelaSaida.getItems()) {
            total += saida.getDepositos();
        }
        return total;
    }

    public void atualizarTotalSaidas() {
        campoTotalSaidas.setText(String.valueOf(getTotalSaidas()));
    }

    public double getTotalSaidas() {
        return getTotalContas() + getTotalDepositos();
    }

    public void atualizarSaldo() {
        campoSaldo.setText(String.valueOf(getSaldo()));
    }

    public double getSaldo() {
        return getTotalEntradas() - getTotalSaidas();
    }
}