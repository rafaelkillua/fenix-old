package fenix.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

public class Controller {

    @FXML
    protected void fecharMes(ActionEvent event) {
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
}
