package br.com.rafaelst.fenix.controllers;

import br.com.rafaelst.fenix.controllers.util.CriadorAlerta;
import br.com.rafaelst.fenix.models.Transacao;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
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
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private double xOffset = 0;
    private double yOffset = 0;

    private final DecimalFormat formatadorDeDouble = new DecimalFormat("0.00");

    @FXML private GridPane gridPane;
    @FXML private Button botaoFechar;
    @FXML private Button botaoMinimizar;

    @FXML private TableView<Transacao> tabelaTransacoes;
    @FXML private TableColumn<Transacao, Double> colunaValor;

    @FXML private Label labelTotalEntradas;
    @FXML private Label labelTotalContas;
    @FXML private Label labelTotalDepositos;
    @FXML private Label labelTotalSaidas;
    @FXML private Label labelSaldo;

    @FXML private TextField campoDescricao;
    @FXML private TextField campoValor;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ativaMovimentoJanela();

        //tabelaTransacoes.setItems(BancoDeDados.getDadosEntrada()); TODO

        inicializarEnterListeners();

        atualizarTodosOsTotais();

        resetarCamposEntrada();

        inicializarPopupTabelas();

        Platform.runLater(() -> campoDescricao.requestFocus());

        alinharFormatarColunas();
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
            CriadorAlerta.criarAlertaExcecao("Código C02", e.getClass() + " - " + e.getMessage()).showAndWait();
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
            CriadorAlerta.criarAlertaExcecao("Código C03", e.getClass() + " - " + e.getMessage()).showAndWait();
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
        String textoHistorico = campoDescricao.getText().trim().isEmpty() ? "<Sem histórico definido>" : campoDescricao.getText();
        /*if (!campoEntradaValor.getText().isEmpty()) {
            try {
                BancoDeDados.salvarEntrada(new Entrada(-1, campoData.getText(), textoHistorico, Double.parseDouble(campoEntradaValor.getText().replaceAll(",", "."))));
            } catch (SQLException|ClassNotFoundException e) {
                CriadorAlerta.criarAlertaExcecao("Código C04", e.getClass() + e.getMessage()).showAndWait();
            } catch (NumberFormatException e) {
                CriadorAlerta.criarAlertaErro("Dados inválidos", "Há algum campo com dados inválidos").showAndWait();
            }
        }
        if (!campoSaidaConta.getText().isEmpty() && !campoSaidaDeposito.getText().isEmpty()) {
            try {
                //BancoDeDados.salvarSaida(new Transacao(0, campoData.getText(), textoHistorico, Double.parseDouble(campoSaidaConta.getText()), Double.parseDouble(campoSaidaDeposito.getText())));
            } catch (Exception e) {
                CriadorAlerta.criarAlertaExcecao("Código C05", e.getClass() + e.getMessage()).showAndWait();
            }
        } else if (!campoSaidaConta.getText().isEmpty()) {
            try {
                //BancoDeDados.salvarSaida(new Transacao(0, campoData.getText(), textoHistorico, Double.parseDouble(campoSaidaConta.getText()), 0));
            } catch (Exception e) {
                CriadorAlerta.criarAlertaExcecao("Código C06", e.getClass() + e.getMessage()).showAndWait();
            }
        } else if (!campoSaidaDeposito.getText().isEmpty()) {
            try {
                //BancoDeDados.salvarSaida(new Transacao(0, campoData.getText(), textoHistorico, 0, Double.parseDouble(campoSaidaDeposito.getText())));
            } catch (Exception e) {
                CriadorAlerta.criarAlertaExcecao("Código C07", e.getClass() + e.getMessage()).showAndWait();
            }
        }TODO*/
        atualizarTodosOsTotais();
        resetarCamposEntrada();
        puxarFocoHistorico();
    }

    private void inicializarEnterListeners(){
        campoDescricao.setOnKeyPressed((event) -> { if(event.getCode() == KeyCode.ENTER) { adicionar(); } });
        campoValor.setOnKeyPressed((event) -> { if(event.getCode() == KeyCode.ENTER) { adicionar(); } });
    }

    private void inicializarPopupTabelas() {
        tabelaTransacoes.setRowFactory(tableView -> {
            final TableRow<Transacao> row = new TableRow<>();
            final ContextMenu contextMenu = new ContextMenu();
            final MenuItem editarMenuItem = new MenuItem("Editar");
            final MenuItem removerMenuItem = new MenuItem("Remover");
            removerMenuItem.setOnAction(event -> {
                try {
                    apagarTransacao(row.getItem());
                } catch (SQLException|ClassNotFoundException e) {
                    CriadorAlerta.criarAlertaExcecao("Código C08", e.getClass() + " - " + e.getMessage());
                }
            });
            editarMenuItem.setOnAction(event -> {
                Transacao temp = row.getItem();
                campoDescricao.setText(temp.getDescricao());
                campoValor.setText(String.valueOf(temp.getValor()));
                try {
                    apagarTransacao(temp);
                } catch (SQLException|ClassNotFoundException e) {
                    CriadorAlerta.criarAlertaExcecao("Código C09", e.getClass() + " - " + e.getMessage());
                }
            });
            contextMenu.getItems().addAll(editarMenuItem, removerMenuItem);
            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then((ContextMenu) null)
                            .otherwise(contextMenu)
            );
            return row ;
        });
    }

    private void alinharFormatarColunas() {
        alinharFormatarColuna(colunaValor);
        /*colunaDataEntrada.setStyle("-fx-alignment: CENTER;");
        colunaDataSaida.setStyle("-fx-alignment: CENTER;");
        alinharFormatarColuna(colunaContas);
        alinharFormatarColuna(colunaDepositos);*/
    }

    private void alinharFormatarColuna(TableColumn coluna) {
        coluna.setCellFactory(column -> new TableCell<Object, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setStyle("-fx-alignment: CENTER;");
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(formatadorDeDouble.format(item));
                }
            }
        });
    }

    private void apagarTransacao(Transacao transacao) throws SQLException, ClassNotFoundException {
        //BancoDeDados.deletarTransacao(transacao);
        atualizarTodosOsTotais();
    }

    private void atualizarTotalEntradas() {
        labelTotalEntradas.setText("R$ " + formatadorDeDouble.format(getTotalEntradas()));
    }

    private double getTotalEntradas() {
        double total = 0;
        /*for (Transacao entrada : BancoDeDados.getDia()) {
            total += entrada.getValor();
        }*/
        return total;
    }

    private void atualizarSaldo() {
        labelSaldo.setText("R$ " + formatadorDeDouble.format(getSaldo()));
    }

    private double getSaldo() {
        return getTotalEntradas();
    }

    private void atualizarTodosOsTotais() {
        atualizarTotalEntradas();
        atualizarSaldo();
    }

    private void resetarCamposEntrada() {
        campoDescricao.setText("");
        campoValor.setText("");
    }

    private void puxarFocoHistorico() {
        campoDescricao.requestFocus();
    }

    private String getDataHoje(){
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM");
        return dateFormat.format(Calendar.getInstance().getTime());
    }

    private String getMesHoje(){
        return Calendar.getInstance().getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
    }
}