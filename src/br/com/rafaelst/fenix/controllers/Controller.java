package br.com.rafaelst.fenix.controllers;

import br.com.rafaelst.fenix.controllers.util.BancoDeDados;
import br.com.rafaelst.fenix.controllers.util.CriadorAlerta;
import br.com.rafaelst.fenix.models.Entrada;
import br.com.rafaelst.fenix.models.Mes;
import br.com.rafaelst.fenix.models.Saida;
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

    @FXML private TableView<Entrada> tabelaEntrada;
    @FXML private TableColumn<Entrada, String> colunaDataEntrada;
    @FXML private TableColumn<Entrada, Double> colunaValor;

    @FXML private TableView<Saida> tabelaSaida;
    @FXML private TableColumn<Saida, String> colunaDataSaida;
    @FXML private TableColumn<Saida, Double> colunaContas;
    @FXML private TableColumn<Saida, Double> colunaDepositos;

    @FXML private Label labelTotalEntradas;
    @FXML private Label labelTotalContas;
    @FXML private Label labelTotalDepositos;
    @FXML private Label labelTotalSaidas;
    @FXML private Label labelSaldo;

    @FXML private TextField campoData;
    @FXML private TextField campoHistorico;
    @FXML private TextField campoEntradaValor;
    @FXML private TextField campoSaidaConta;
    @FXML private TextField campoSaidaDeposito;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ativaMovimentoJanela();

        tabelaEntrada.setItems(BancoDeDados.getDadosEntrada());
        tabelaSaida.setItems(BancoDeDados.getDadosSaida());

        inicializarEnterListeners();

        atualizarTodosOsTotais();

        resetarCamposEntrada();

        inicializarPopupTabelas();

        Platform.runLater(() -> campoHistorico.requestFocus());

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
    protected void fecharMes() {
        TextInputDialog dialog = CriadorAlerta.criarTextInputDialog("Fechar Mês", "Digite o nome do mês a ser fechado", "Mês:");
        Alert confirm = CriadorAlerta.criarAlertaConfirmacao("Isso irá apagar todos os dados de entrada e saída.");

        dialog.setResult(getMesHoje());

        dialog.showAndWait().ifPresent(name -> {
            if (!name.trim().equals("")) {
                confirm.showAndWait().ifPresent(answer -> {
                    if (answer == ButtonType.OK) {
                        try {
                            BancoDeDados.salvarMes(new Mes(-1, name, getTotalEntradas(), getTotalSaidas()));
                            atualizarTodosOsTotais();
                            abrirVisualizacaoPorAno();
                        } catch (SQLException | ClassNotFoundException e) {
                            CriadorAlerta.criarAlertaErro("Código C01", e.getClass() + " - " + e.getMessage());
                        }
                    }
                });
            } else {
                CriadorAlerta.criarAlertaErro("Erro de entrada", "Mês não pode ser vazio").showAndWait();
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
        String textoHistorico = campoHistorico.getText().trim().isEmpty() ? "<Sem histórico definido>" : campoHistorico.getText();
        if (!campoEntradaValor.getText().isEmpty()) {
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
                BancoDeDados.salvarSaida(new Saida(0, campoData.getText(), textoHistorico, Double.parseDouble(campoSaidaConta.getText()), Double.parseDouble(campoSaidaDeposito.getText())));
            } catch (Exception e) {
                CriadorAlerta.criarAlertaExcecao("Código C05", e.getClass() + e.getMessage()).showAndWait();
            }
        } else if (!campoSaidaConta.getText().isEmpty()) {
            try {
                BancoDeDados.salvarSaida(new Saida(0, campoData.getText(), textoHistorico, Double.parseDouble(campoSaidaConta.getText()), 0));
            } catch (Exception e) {
                CriadorAlerta.criarAlertaExcecao("Código C06", e.getClass() + e.getMessage()).showAndWait();
            }
        } else if (!campoSaidaDeposito.getText().isEmpty()) {
            try {
                BancoDeDados.salvarSaida(new Saida(0, campoData.getText(), textoHistorico, 0, Double.parseDouble(campoSaidaDeposito.getText())));
            } catch (Exception e) {
                CriadorAlerta.criarAlertaExcecao("Código C07", e.getClass() + e.getMessage()).showAndWait();
            }
        }
        atualizarTodosOsTotais();
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
                try {
                    apagarEntrada(row.getItem());
                } catch (SQLException|ClassNotFoundException e) {
                    CriadorAlerta.criarAlertaExcecao("Código C08", e.getClass() + " - " + e.getMessage());
                }
            });
            editarMenuItem.setOnAction(event -> {
                Entrada temp = row.getItem();
                campoData.setText(temp.getData());
                campoHistorico.setText(temp.getHistorico());
                campoEntradaValor.setText(String.valueOf(temp.getValor()));
                try {
                    apagarEntrada(temp);
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
        tabelaSaida.setRowFactory(tableView -> {
            final TableRow<Saida> row = new TableRow<>();
            final ContextMenu contextMenu = new ContextMenu();
            final MenuItem editarMenuItem = new MenuItem("Editar");
            final MenuItem removerMenuItem = new MenuItem("Remover");
            removerMenuItem.setOnAction(event -> {
                try {
                    apagarSaida(row.getItem());
                } catch (SQLException|ClassNotFoundException e) {
                    CriadorAlerta.criarAlertaExcecao("Código C11", e.getClass() + " - " + e.getMessage());
                }
            });
            editarMenuItem.setOnAction(event -> {
                Saida temp = row.getItem();
                campoData.setText(temp.getData());
                campoHistorico.setText(temp.getHistorico());
                campoSaidaConta.setText(String.valueOf(temp.getContas()));
                campoSaidaDeposito.setText(String.valueOf(temp.getDepositos()));
                try {
                    apagarSaida(row.getItem());
                } catch (SQLException|ClassNotFoundException e) {
                    CriadorAlerta.criarAlertaExcecao("Código C12", e.getClass() + " - " + e.getMessage());
                }
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

    private void alinharFormatarColunas() {
        colunaDataEntrada.setStyle("-fx-alignment: CENTER;");
        alinharFormatarColuna(colunaValor);
        colunaDataSaida.setStyle("-fx-alignment: CENTER;");
        alinharFormatarColuna(colunaContas);
        alinharFormatarColuna(colunaDepositos);
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

    private void apagarEntrada(Entrada entrada) throws SQLException, ClassNotFoundException {
        BancoDeDados.deletarEntrada(entrada);
        atualizarTodosOsTotais();
    }

    private void apagarSaida (Saida saida) throws SQLException, ClassNotFoundException {
        BancoDeDados.deletarSaida(saida);
        atualizarTodosOsTotais();
    }

    private void atualizarTotalEntradas() {
        labelTotalEntradas.setText("R$ " + formatadorDeDouble.format(getTotalEntradas()));
    }

    private double getTotalEntradas() {
        double total = 0;
        for (Entrada entrada : BancoDeDados.getDadosEntrada()) {
            total += entrada.getValor();
        }
        return total;
    }

    private void atualizarTotalContas() {
        labelTotalContas.setText("R$ " + formatadorDeDouble.format(getTotalContas()));
    }

    private double getTotalContas() {
        double total = 0;
        for (Saida saida : BancoDeDados.getDadosSaida()) {
            total += saida.getContas();
        }
        return total;
    }

    private void atualizarTotalDepositos() {
        labelTotalDepositos.setText("R$ " + formatadorDeDouble.format(getTotalDepositos()));
    }

    private double getTotalDepositos() {
        double total = 0;
        for (Saida saida : BancoDeDados.getDadosSaida()) {
            total += saida.getDepositos();
        }
        return total;
    }

    private void atualizarTotalSaidas() {
        labelTotalSaidas.setText("R$ " + formatadorDeDouble.format(getTotalSaidas()));
    }

    private double getTotalSaidas() {
        return getTotalContas() + getTotalDepositos();
    }

    private void atualizarSaldo() {
        labelSaldo.setText("R$ " + formatadorDeDouble.format(getSaldo()));
    }

    private double getSaldo() {
        return getTotalEntradas() - getTotalSaidas();
    }

    private void atualizarTodosOsTotais() {
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
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM");
        return dateFormat.format(Calendar.getInstance().getTime());
    }

    private String getMesHoje(){
        return Calendar.getInstance().getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
    }
}