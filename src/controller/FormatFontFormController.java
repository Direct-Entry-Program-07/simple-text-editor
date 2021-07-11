package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;

import java.util.List;

public class FormatFontFormController {
    public ListView lstFont;
    public TextField txtSelectedFont;

    public void initialize(){
        List<String> fontNames = Font.getFontNames();
        txtSelectedFont.requestFocus();
        ObservableList<String> availableFonts = FXCollections.observableArrayList();
        for (String fontName : fontNames) {
            availableFonts.add(fontName);
        }
        txtSelectedFont.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            }
        });
        //System.out.println(fontNames);
        lstFont.setItems(availableFonts);

        lstFont.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                txtSelectedFont.setText(newValue.toString());
            }
        });
    }
}
