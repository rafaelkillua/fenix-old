package br.com.rafaelst.fenix.controllers.util;

import br.com.rafaelst.fenix.models.Dia;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

/**
 * Created by Rafael on 29/04/2016.
 */
public class BancoDeDados {

    private static Connection conexao = null;
    private static Statement afirmacao = null;
    private static ResultSet resultados = null;
    private static final String DRIVER = "org.sqlite.JDBC";
    private static final String URL = "jdbc:sqlite:database.db";

    private static ObservableList<Dia> dadosDias = FXCollections.observableArrayList();

    public static void iniciarBanco() {

        /*try {
            criarTabelaDias();
            popularDias();
        } catch (SQLException e) {
            CriadorAlerta.criarAlertaExcecao("Código BD1", e.getClass() + " - " + e.getMessage()).showAndWait();
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            CriadorAlerta.criarAlertaExcecao("Código BD2", e.getClass() + " - " + e.getMessage()).showAndWait();
        }*/
    }

    private static void consultar(String sql) throws SQLException {
        resultados = afirmacao.executeQuery(sql);
    }

    private static void atualizar(String sql) throws SQLException {
        afirmacao.executeUpdate(sql);
    }

    private static void conectarBanco() throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER);
        conexao = DriverManager.getConnection(URL);
        afirmacao = conexao.createStatement();
    }

    private static void fecharConexao() throws SQLException {
        afirmacao.close();
        conexao.close();
    }

    private static void fecharConsulta() throws SQLException {
        resultados.close();
    }

    /*public static void salvarDia(Dia dia) throws SQLException, ClassNotFoundException {
        conectarBanco();
        atualizar("INSERT INTO saidas (data, historico, saidaContas, saidaDepositos) "
                + "VALUES ('" + dia.getData() + "', '"
                + dia.getHistorico() + "', '"
                + dia.getContas() + "', '"
                + dia.getDepositos() + "')");
        consultar("SELECT MAX(id) FROM saidas");
        resultados.next();
        dia.setId(resultados.getInt(1));
        fecharConsulta();
        fecharConexao();
        dadosDias.add(dia);
    }

    public static void deletarDia(Entrada entrada) throws SQLException, ClassNotFoundException {
        conectarBanco();
        atualizar("DELETE FROM entradas where id='" + entrada.getId() + "'");
        fecharConexao();
        dadosEntrada.remove(entrada);
    }

    public static void deletarTransacao(Transacao transacao, Dia dia) {

    } TODO

    public static ObservableList<Dia> getDadosDias() {
        return dadosDias;
    }

    private static void popularDias() throws SQLException, ClassNotFoundException {
        conectarBanco();
        consultar("SELECT * FROM 'entradas'");
        while (resultados.next()) {
            dadosDias.add(new Dia(resultados.getInt("id"),
                    resultados.getString("data"),
                    resultados.getString("historico"),
                    resultados.getDouble("valor")));
        }
        fecharConsulta();
        fecharConexao();
    }

    private static void criarTabelaSaidas() throws SQLException, ClassNotFoundException {
        conectarBanco();
        afirmacao.executeUpdate("CREATE TABLE IF NOT EXISTS saidas (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL DEFAULT (0), " +
                "data TEXT NOT NULL, " +
                "historico TEXT NOT NULL, " +
                "saidaContas DOUBLE NOT NULL, " +
                "saidaDepositos DOUBLE NOT NULL);");
        fecharConexao();
    }*/
}
