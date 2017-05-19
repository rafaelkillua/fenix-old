package br.com.rafaelst.fenix.controllers.util;

import br.com.rafaelst.fenix.models.Ano;
import br.com.rafaelst.fenix.models.Entrada;
import br.com.rafaelst.fenix.models.Mes;
import br.com.rafaelst.fenix.models.Saida;
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

    private static ObservableList<Entrada> dadosEntrada = FXCollections.observableArrayList();
    private static ObservableList<Saida> dadosSaida = FXCollections.observableArrayList();
    private static ObservableList<Mes> dadosMeses = FXCollections.observableArrayList();
    private static ObservableList<Ano> dadosAnos = FXCollections.observableArrayList();

    public static void iniciarBanco() {
        try {
            criarTabelaEntradas();
            criarTabelaSaidas();
            criarTabelaMeses();
            criarTabelaAnos();

            popularEntradas();
            popularSaidas();
            popularMeses();
            popularAnos();
        } catch (SQLException e) {
            CriadorAlerta.criarAlertaExcecao("Código BD1", e.getClass() + " - " + e.getMessage()).showAndWait();
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            CriadorAlerta.criarAlertaExcecao("Código BD2", e.getClass() + " - " + e.getMessage()).showAndWait();
        }
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

    public static void salvarEntrada(Entrada entrada) throws SQLException, ClassNotFoundException {
        conectarBanco();
        atualizar("INSERT INTO entradas (data, historico, valor) "
                + "VALUES ('" + entrada.getData() + "', '"
                + entrada.getHistorico() + "', '"
                + entrada.getValor() + "')");
        consultar("SELECT MAX(id) FROM entradas");
        resultados.next();
        entrada.setId(resultados.getInt(1));
        fecharConsulta();
        fecharConexao();
        dadosEntrada.add(entrada);
    }

    public static void salvarSaida(Saida saida) throws SQLException, ClassNotFoundException {
        conectarBanco();
        atualizar("INSERT INTO saidas (data, historico, saidaContas, saidaDepositos) "
                + "VALUES ('" + saida.getData() + "', '"
                + saida.getHistorico() + "', '"
                + saida.getContas() + "', '"
                + saida.getDepositos() + "')");
        consultar("SELECT MAX(id) FROM saidas");
        resultados.next();
        saida.setId(resultados.getInt(1));
        fecharConsulta();
        fecharConexao();
        dadosSaida.add(saida);
    }

    public static void salvarMes(Mes mes) throws SQLException, ClassNotFoundException {
        conectarBanco();
        atualizar("INSERT INTO meses (mes, totalEntradas, totalSaidas) "
                + "VALUES ('" + mes.getMes() + "', '"
                + mes.getEntradas() + "', '"
                + mes.getSaidas() + "')");
        consultar("SELECT MAX(id) FROM meses");
        resultados.next();
        mes.setId(resultados.getInt(1));
        fecharConsulta();
        fecharConexao();
        dadosMeses.add(mes);
        apagarEntradasSaidas();
    }

    public static void salvarAno(Ano ano) throws SQLException, ClassNotFoundException {
        conectarBanco();
        atualizar("INSERT INTO anos (ano, saldo) "
                + "VALUES ('" + ano.getAno() + "', '"
                + ano.getSaldo() + "')");
        consultar("SELECT MAX(id) FROM anos");
        resultados.next();
        ano.setId(resultados.getInt(1));
        fecharConsulta();
        fecharConexao();
        dadosAnos.add(ano);
        apagarMeses();
    }

    public static void deletarEntrada(Entrada entrada) throws SQLException, ClassNotFoundException {
        conectarBanco();
        atualizar("DELETE FROM entradas where id='" + entrada.getId() + "'");
        fecharConexao();
        dadosEntrada.remove(entrada);
    }

    public static void deletarSaida(Saida saida) throws SQLException, ClassNotFoundException {
        conectarBanco();
        atualizar("DELETE FROM saidas where id='" + saida.getId() + "'");
        fecharConexao();
        dadosSaida.remove(saida);
    }

    public static void deletarMes(Mes mes) throws SQLException, ClassNotFoundException {
        conectarBanco();
        atualizar("DELETE FROM meses where id='" + mes.getId() + "'");
        fecharConexao();
        dadosMeses.remove(mes);
    }

    public static void deletarAno(Ano ano) throws SQLException, ClassNotFoundException {
        conectarBanco();
        atualizar("DELETE FROM anos where id='" + ano.getId() + "'");
        fecharConexao();
        dadosAnos.remove(ano);
    }

    public static ObservableList<Entrada> getDadosEntrada() {
        return dadosEntrada;
    }

    public static ObservableList<Saida> getDadosSaida() {
        return dadosSaida;
    }

    public static ObservableList<Mes> getDadosMeses() {
        return dadosMeses;
    }

    public static ObservableList<Ano> getDadosAnos() {
        return dadosAnos;
    }

    private static void popularEntradas() throws SQLException, ClassNotFoundException {
        conectarBanco();
        consultar("SELECT * FROM 'entradas'");
        while (resultados.next()) {
            dadosEntrada.add(new Entrada(resultados.getInt("id"),
                    resultados.getString("data"),
                    resultados.getString("historico"),
                    resultados.getDouble("valor")));
        }
        fecharConsulta();
        fecharConexao();
    }

    private static void popularSaidas() throws SQLException, ClassNotFoundException {
        conectarBanco();
        consultar("SELECT * FROM 'saidas'");
        while (resultados.next()) {
            dadosSaida.add(new Saida(resultados.getInt("id"),
                    resultados.getString("data"),
                    resultados.getString("historico"),
                    resultados.getDouble("saidaContas"),
                    resultados.getDouble("saidaDepositos")));
        }
        fecharConsulta();
        fecharConexao();
    }

    private static void popularMeses() throws SQLException, ClassNotFoundException {
        conectarBanco();
        consultar("SELECT * FROM 'meses'");
        while (resultados.next()) {
            dadosMeses.add(new Mes(resultados.getInt("id"),
                    resultados.getString("mes"),
                    resultados.getDouble("totalEntradas"),
                    resultados.getDouble("totalSaidas")));
        }
        fecharConsulta();
        fecharConexao();
    }

    private static void popularAnos() throws SQLException, ClassNotFoundException {
        conectarBanco();
        consultar("SELECT * FROM 'anos'");
        while (resultados.next()) {
            dadosAnos.add(new Ano(resultados.getInt("id"),
                    resultados.getInt("ano"),
                    resultados.getDouble("saldo")));
        }
        fecharConsulta();
        fecharConexao();
    }

    private static void apagarEntradasSaidas() throws SQLException, ClassNotFoundException {
        conectarBanco();
        atualizar("DELETE FROM entradas");
        atualizar("DELETE FROM saidas");
        fecharConexao();
        dadosEntrada.clear();
        dadosSaida.clear();
    }

    private static void apagarMeses() throws SQLException, ClassNotFoundException {
        conectarBanco();
        atualizar("DELETE FROM meses");
        fecharConexao();
        dadosMeses.clear();
    }

    private static void criarTabelaEntradas() throws SQLException, ClassNotFoundException {
        conectarBanco();
        afirmacao.executeUpdate("CREATE TABLE IF NOT EXISTS entradas (id INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL UNIQUE DEFAULT (0), " +
                "data TEXT NOT NULL, " +
                "historico TEXT NOT NULL, " +
                "valor DOUBLE NOT NULL);");
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
    }

    private static void criarTabelaMeses() throws SQLException, ClassNotFoundException {
        conectarBanco();
        afirmacao.executeUpdate("CREATE TABLE IF NOT EXISTS meses (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE DEFAULT (0), " +
                "mes TEXT NOT NULL, " +
                "totalEntradas DOUBLE NOT NULL, " +
                "totalSaidas DOUBLE NOT NULL);");
        fecharConexao();
    }

    private static void criarTabelaAnos() throws SQLException, ClassNotFoundException {
        conectarBanco();
        afirmacao.executeUpdate("CREATE TABLE IF NOT EXISTS anos (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE DEFAULT (0), " +
                "ano INTEGER NOT NULL, " +
                "saldo DOUBLE NOT NULL);");
        fecharConexao();
    }
}
