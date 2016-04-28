package br.com.rafaelst.fenix.controllers.util;

import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

/**
 * Created by Rafael on 28/04/2016.
 */
public class CriadorAlerta {
    public static TextInputDialog criarTextInputDialog(String titulo, String header, String pergunta) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(titulo);
        dialog.setHeaderText(header);
        dialog.setContentText(pergunta);
        return dialog;
    }

    public static Alert criarAlertaErro(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert;
    }
}
