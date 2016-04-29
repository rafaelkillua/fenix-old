package br.com.rafaelst.fenix.controllers;

import br.com.rafaelst.fenix.controllers.util.CriadorAlerta;
import br.com.rafaelst.fenix.models.Entrada;
import br.com.rafaelst.fenix.models.Saida;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private double xOffset = 0;
    private double yOffset = 0;

    //private final DecimalFormat formatadorDeDouble = new DecimalFormat("0.00");

    @FXML private GridPane gridPane;
    @FXML private Button botaoFechar;
    @FXML private Button botaoMinimizar;

    @FXML private TableView<Entrada> tabelaEntrada;
    @FXML private TableView<Saida> tabelaSaida;

    @FXML private TextField campoTotalEntradas;

    @FXML private TextField campoTotalContas;
    @FXML private TextField campoTotalDepositos;
    @FXML private TextField campoTotalSaidas;

    @FXML private TextField campoSaldo;

    @FXML private TextField campoData;
    @FXML private TextField campoHistorico;
    @FXML private TextField campoEntradaValor;
    @FXML private TextField campoSaidaConta;
    @FXML private TextField campoSaidaDeposito;

    private ObservableList<Entrada> dadosEntrada = FXCollections.observableArrayList();
    private ObservableList<Saida> dadosSaida = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ativaMovimentoJanela();

        tabelaEntrada.setItems(dadosEntrada);
        tabelaSaida.setItems(dadosSaida);

        inicializarEnterListeners();

        atualizarTodosOsCampos();

        resetarCamposEntrada();

        inicializarPopupTabelas();

        Platform.runLater(() -> campoHistorico.requestFocus());
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

    @FXML
    protected void adicionar() {
        if (!campoEntradaValor.getText().isEmpty()) {
            try {
                dadosEntrada.add(new Entrada(0, campoData.getText(), campoHistorico.getText(), Double.parseDouble(campoEntradaValor.getText())));
            } catch (Exception e) {
                CriadorAlerta.criarAlertaErro("Exceção", e.getClass() + e.getMessage());
            }
        }
        if (!campoSaidaConta.getText().isEmpty() && !campoSaidaDeposito.getText().isEmpty()) {
            try {
                dadosSaida.add(new Saida(0, campoData.getText(), campoHistorico.getText(), Double.parseDouble(campoSaidaConta.getText()), Double.parseDouble(campoSaidaDeposito.getText())));
            } catch (Exception e) {
                CriadorAlerta.criarAlertaErro("Exceção", e.getClass() + e.getMessage());
            }
        } else if (!campoSaidaConta.getText().isEmpty()) {
            try {
                dadosSaida.add(new Saida(0, campoData.getText(), campoHistorico.getText(), Double.parseDouble(campoSaidaConta.getText()), 0));
            } catch (Exception e) {
                CriadorAlerta.criarAlertaErro("Exceção", e.getClass() + e.getMessage());
            }
        } else if (!campoSaidaDeposito.getText().isEmpty()) {
            try {
                dadosSaida.add(new Saida(0, campoData.getText(), campoHistorico.getText(), 0, Double.parseDouble(campoSaidaDeposito.getText())));
            } catch (Exception e) {
                CriadorAlerta.criarAlertaErro("Exceção", e.getClass() + e.getMessage());
            }
        }
        atualizarTodosOsCampos();
        resetarCamposEntrada();
        puxarFocoHistorico();
    }

    private void inicializarEnterListeners(){
        campoData.setOnKeyPressed((event) -> { if(event.getCode() == KeyCode.ENTER) { adicionar(); } });
        campoHistorico.setOnKeyPressed((event) -> { if(event.getCode() == KeyCode.ENTER) { adicionar(); } });
        campoEntradaValor.setOnKeyPressed((event) -> { if(event.getCode() == KeyCode.ENTER) { adicionar(); } });
        campoSaidaConta.setOnKeyPressed((event) -> { if(event.getCode() == KeyCode.ENTER) { adicionar(); } });
        campoSaidaDeposito.setOnKeyPressed((event) -> { if(event.getCode() == KeyCode.ENTER) { adicionar(); } });
    }

    private void inicializarPopupTabelas() {
        tabelaEntrada.setRowFactory(tableView -> {
            final TableRow<Entrada> row = new TableRow<>();
            final ContextMenu contextMenu = new ContextMenu();
            final MenuItem editarMenuItem = new MenuItem("Editar");
            final MenuItem removerMenuItem = new MenuItem("Remover");
            removerMenuItem.setOnAction(event -> {
                Entrada temp = row.getItem();
                tabelaEntrada.getItems().remove(temp);
                apagarEntrada(temp);
            });
            editarMenuItem.setOnAction(event -> {
                Entrada temp = row.getItem();
                tabelaEntrada.getItems().remove(temp);
                campoData.setText(temp.getData());
                campoHistorico.setText(temp.getHistorico());
                campoEntradaValor.setText(String.valueOf(temp.getValor()));
                apagarEntrada(temp);
            });
            contextMenu.getItems().addAll(editarMenuItem, removerMenuItem);
            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then((ContextMenu)null)
                            .otherwise(contextMenu)
            );
            return row ;
        });
        tabelaSaida.setRowFactory(tableView -> {
            final TableRow<Saida> row = new TableRow<>();
            final ContextMenu contextMenu = new ContextMenu();
            final MenuItem editarMenuItem = new MenuItem("Editar");
            final MenuItem removerMenuItem = new MenuItem("Remover");
            removerMenuItem.setOnAction(event -> {
                Saida temp = row.getItem();
                tabelaSaida.getItems().remove(temp);
                apagarSaida(temp);
            });
            editarMenuItem.setOnAction(event -> {
                Saida temp = row.getItem();
                tabelaSaida.getItems().remove(temp);
                campoData.setText(temp.getData());
                campoHistorico.setText(temp.getHistorico());
                campoSaidaConta.setText(String.valueOf(temp.getContas()));
                campoSaidaDeposito.setText(String.valueOf(temp.getDepositos()));
                apagarSaida(temp);
            });
            contextMenu.getItems().addAll(editarMenuItem, removerMenuItem);
            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then((ContextMenu)null)
                            .otherwise(contextMenu)
            );
            return row ;
        });
    }

    private void apagarEntrada(Entrada entrada) {
        dadosEntrada.remove(entrada);
    }

    private void apagarSaida (Saida saida) {
        dadosSaida.remove(saida);
    }

    private void atualizarTotalEntradas() {
        campoTotalEntradas.setText(String.valueOf(getTotalEntradas()));
    }

    private double getTotalEntradas() {
        double total = 0;
        for (Entrada entrada : tabelaEntrada.getItems()) {
            total += entrada.getValor();
        }
        return total;
    }

    private void atualizarTotalContas() {
        campoTotalContas.setText(String.valueOf(getTotalContas()));
    }

    private double getTotalContas() {
        double total = 0;
        for (Saida saida : tabelaSaida.getItems()) {
            total += saida.getContas();
        }
        return total;
    }

    private void atualizarTotalDepositos() {
        campoTotalDepositos.setText(String.valueOf(getTotalDepositos()));
    }

    private double getTotalDepositos() {
        double total = 0;
        for (Saida saida : tabelaSaida.getItems()) {
            total += saida.getDepositos();
        }
        return total;
    }

    private void atualizarTotalSaidas() {
        campoTotalSaidas.setText(String.valueOf(getTotalSaidas()));
    }

    private double getTotalSaidas() {
        return getTotalContas() + getTotalDepositos();
    }

    private void atualizarSaldo() {
        campoSaldo.setText(String.valueOf(getSaldo()));
    }

    private double getSaldo() {
        return getTotalEntradas() - getTotalSaidas();
    }

    private void atualizarTodosOsCampos() {
        atualizarTotalEntradas();
        atualizarTotalContas();
        atualizarTotalDepositos();
        atualizarTotalSaidas();
        atualizarSaldo();
    }

    private void resetarCamposEntrada() {
        campoData.setText(getDataHoje());
        campoHistorico.setText("");
        campoEntradaValor.setText("");
        campoSaidaConta.setText("");
        campoSaidaDeposito.setText("");
    }

    private void puxarFocoHistorico() {
        campoHistorico.requestFocus();
    }

    private String getDataHoje(){
        final Calendar calendar = Calendar.getInstance();
        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        String mes = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        return dia + "/" + mes;
    }
}