/*
 * Copyright (c) 2021  Dinusha Jayawardena. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

package controller;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.ArrayList;
import java.util.List;

public class FormatFontFormController {
    private static String changedFontName = Font.getDefault().getName();
    private final ObservableList<String> availableFonts = FXCollections.observableArrayList();
    public ListView lstFont;
    public TextField txtSelectedFont;
    public TextArea txtSampleText;
    public ListView lstStyle;
    public TextField txtSelectedStyle;
    public ListView lstFontSizes;
    public TextField txtSelectedFontSize;
    public ColorPicker clrpckrFont;
    public ColorPicker clrpckrBG;
    private List<String> fontNames = Font.getFontNames();
    private final ObservableList<Integer> sizes = FXCollections.observableArrayList();

    public static long parseUnsignedHex(String text) {
        if (text.length() == 16) {
            return (parseUnsignedHex(text.substring(0, 1)) << 60)
                    | parseUnsignedHex(text.substring(1));
        }
        return Long.parseLong(text, 16);
    }

    public static String getChangedFontName() {
        return changedFontName;
    }

    public void setChangedFontName(String changedFontName) {
        FormatFontFormController.changedFontName = changedFontName;
    }

    public void initialize() {

        Platform.runLater(() -> {
            txtSelectedFont.requestFocus();

        });

        for (String fontName : fontNames) {
            availableFonts.add(fontName);
        }
        lstFont.setItems(availableFonts);

        sizes.addAll(8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 30, 32, 64);
        lstFontSizes.setItems(sizes);

        /*txtSelectedFont.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                availableFonts.clear();
                if (!newValue.isEmpty()) {
                    fontNames = searchFonts(newValue);
                    for (String fontName : fontNames) {
                        availableFonts.add(fontName);
                    }
                }
            }
        });*/

        lstFont.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (!newValue.equals("")) {

                    txtSelectedFont.setText(newValue.toString());
                    txtSampleText.setFont(Font.font(String.valueOf(newValue)));
                    //double size = Font.font(String.valueOf(newValue)).getSize();
                    //System.out.println();
                }
            }
        });

        lstFontSizes.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                try {
                    Double d = Double.parseDouble(newValue.toString());
                    txtSelectedFontSize.setText(d + "");
                    txtSampleText.setFont(Font.font(d));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        clrpckrFont.valueProperty().addListener(new ChangeListener<Color>() {
            @Override
            public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
                String colorHash = newValue.toString();

                Long redLong = parseUnsignedHex(colorHash.substring(2, 4));
                double redDouble = Double.longBitsToDouble(redLong);

                Long blueLong = parseUnsignedHex(colorHash.substring(4, 6));
                double blueDouble = Double.longBitsToDouble(blueLong);

                Long greenLong = parseUnsignedHex(colorHash.substring(6, 8));
                double greenDouble = Double.longBitsToDouble(greenLong);

            }
        });

//        Stage stage = (Stage) txtSampleText.getScene().getWindow();

    }

    private List<String> searchFonts(String query) {
        System.out.flush();
        List<String> allFonts = Font.getFamilies();
        List<String> filteredFonts = new ArrayList<>();
        for (String filteredFont : allFonts) {
            if (filteredFont.contains(query)) {
                //System.out.println(filteredFont);
                filteredFonts.add(filteredFont);
            }
        }
        return filteredFonts;
        //System.out.println(fontNames);

    }

    public void txtSelectedFont_KeyReleased(KeyEvent keyEvent) {
        /*txtSelectedFont.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                availableFonts.clear();
                fontNames = searchFonts(newValue);
                for (String fontName : fontNames) {
                    availableFonts.add(fontName);
                }
            }
        });*/
        availableFonts.clear();
        fontNames = searchFonts(txtSelectedFont.getText());
        for (String fontName : fontNames) {
            availableFonts.add(fontName);
        }
    }

    public void btnApply_OnAction(ActionEvent actionEvent) {
        Font font = Font.font(txtSelectedFont.getText(), FontPosture.REGULAR, 14);
        setChangedFontName(txtSelectedFont.getText());
        // System.out.println("Selected Font: " + font);
        // System.out.println(getChangedFontName());
        //editorFormController.txtEditor.setFont(Font.font("Verdana", FontWeight.BOLD, 70));
        //System.out.println();
        //  editorFormController.setEditorFont(Font.font("ubuntu"));
        Window window = txtSelectedFont.getScene().getWindow();
        Stage secondary = (Stage) window;
        secondary.getOwner().setUserData(txtSelectedFont.toString());
    }

    public void btnCancel_OnAction(ActionEvent actionEvent) {
    }
}
