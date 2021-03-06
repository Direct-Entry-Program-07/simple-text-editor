/*
 * Copyright (c) 2021  Dinusha Jayawardena. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

package controller;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.print.PrinterJob;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import util.FXUtil;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class EditorFormController {
    private final List<Index> searchList = new ArrayList<>();
    private final int searchIndex = 0;
    public TextArea txtEditor;
    public AnchorPane pneWorkingArea;
    public ToolBar tlbrStatus;
    public Label lblStatus;
    public Label lblApplicationStatus;
    private Font editorFont ;
    public TextField txtFind;
    public AnchorPane pneFind;
    public TextField txtSearch;
    public AnchorPane pneReplace;
    public TextField txtReplaceText;
    public MenuItem btnOpen;
    public MenuItem btnSave;
    public MenuItem btnSaveAs;
    private int findOffSet = -1;
    private final String selectedText = "";

    private final int startPoint = 0;
    private final int endPoint = 0;
    private PrinterJob printerJob;
    private String absolutePath = "";
    private String statusText = "";
    private long allCount = 0;
    private long numberCount = 0;

    public void initialize() {
        setEditorFont(Font.getDefault());
        pneFind.setVisible(false);
        pneReplace.setVisible(false);
        this.printerJob = PrinterJob.createPrinterJob();
        lblApplicationStatus.setText("| Sample Text Loaded");

        fillStatusBar((long) txtEditor.getText().length(), getNumbersCountInText());

        ChangeListener textListener = (ChangeListener<String>) (observable, oldValue, newValue) -> {
            searchMatches(newValue);
        };

        txtSearch.textProperty().addListener(textListener);
        txtFind.textProperty().addListener(textListener);
        txtEditor.setFont(editorFont);
        String changedFontName = FormatFontFormController.getChangedFontName();
        //System.out.println(changedFontName);

        txtEditor.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                allCount = txtEditor.getText().length();
                //System.out.println(allCount);
                fillStatusBar(allCount, getNumbersCountInText());
            }
        });


    }

    private Long getNumbersCountInText(){
        String trimmedText = txtEditor.getText().trim();
        Long count = 0L;
        char c;
        for (int i = 0; i < trimmedText.length(); i++) {
            c = trimmedText.charAt(i);
            try{
                Integer.parseInt(String.valueOf(trimmedText.charAt(i)));
                count++;
            }catch (RuntimeException e){

            }
        }

        return count;
    }

    private void fillStatusBar(Long allCount, Long numberCount) {

            lblStatus.setText("Editor contains " + " " + allCount + " Characters with "  + numberCount + " numbers");

    }

    private void searchMatches(String query) {
        FXUtil.highlightOnTextArea(txtEditor, query, Color.web("yellow", 0.8));

        try {

            Pattern regExp = Pattern.compile(query);
            Matcher matcher = regExp.matcher(txtEditor.getText());

            searchList.clear();

            while (matcher.find()) {
                searchList.add(new Index(matcher.start(), matcher.end()));
            }

            if (searchList.isEmpty()) {
                findOffSet = -1;
            }
        } catch (PatternSyntaxException e) {

        }
    }

    public void mnuItemNew_OnAction(ActionEvent actionEvent) {
        txtEditor.clear();
        absolutePath = "";
        txtEditor.requestFocus();
        lblApplicationStatus.setText("loaded new document.");

    }

    public void mnuItemExit_OnAction(ActionEvent actionEvent) {
        ((Stage) txtEditor.getScene().getWindow()).close();
    }

    public void mnuItemFind_OnAction(ActionEvent actionEvent) {
        findOffSet = -1;
        if (pneReplace.isVisible()) {
            pneReplace.setVisible(false);
        }
        pneFind.setVisible(true);
        txtSearch.requestFocus();
    }

    public void mnuItemReplace_OnAction(ActionEvent actionEvent) {
        if (pneFind.isVisible()) {
            pneFind.setVisible(false);
        }
        pneReplace.setVisible(true);
        txtFind.requestFocus();
    }

    public void mnuItemSelectAll_OnAction(ActionEvent actionEvent) {
        txtEditor.selectAll();
    }

    public void btnFindNext_OnAction(ActionEvent actionEvent) {
        if (!searchList.isEmpty()) {
            findOffSet++;
            if (findOffSet >= searchList.size()) {
                findOffSet = 0;
            }
            txtEditor.selectRange(searchList.get(findOffSet).getStartIndex(), searchList.get(findOffSet).getEndIndex());
        }
    }

    public void btnFindPrevious_OnAction(ActionEvent actionEvent) {

        if (!searchList.isEmpty()) {
            findOffSet--;
            if (findOffSet < 0) {
                findOffSet = searchList.size() - 1;
            }
            txtEditor.selectRange(searchList.get(findOffSet).getStartIndex(), searchList.get(findOffSet).getEndIndex());
        }
    }

    public void btnReplace_OnAction(ActionEvent actionEvent) {
        if (findOffSet == -1) return;
        txtEditor.replaceText(searchList.get(findOffSet).getStartIndex(), searchList.get(findOffSet).getEndIndex(), txtReplaceText.getText());
        searchMatches(txtFind.getText());
    }

    public void btnReplaceAll_OnAction(ActionEvent actionEvent) {

        while (!searchList.isEmpty()) {
            txtEditor.replaceText(searchList.get(0).getStartIndex(), searchList.get(0).getEndIndex(), txtReplaceText.getText());
            searchMatches(txtFind.getText());
        }
    }

    public void Escape_OnAction(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ESCAPE) {
            pneReplace.setVisible(false);
            pneFind.setVisible(false);
            txtFind.clear();
            txtSearch.clear();
            txtReplaceText.clear();
        }
    }

    public void btnOpen_OnAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(("Open File"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Text Files", "*.txt", "&.html"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Files", "*"));

        File file = fileChooser.showOpenDialog(txtEditor.getScene().getWindow());

        if (file == null) return;

        txtEditor.clear();
        absolutePath = file.getAbsolutePath();
        //System.out.println(absolutePath);
        openFile(file);
    }

    public void openFile(File file){
        try (FileReader fileReader = new FileReader(file);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                txtEditor.appendText(line + '\n');
            }
            lblApplicationStatus.setText("Successfully opened the file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnSave_OnAction(ActionEvent actionEvent) {
        if (absolutePath.isEmpty()){
            btnSaveAs_OnAction(actionEvent);
        }else {
            try (FileWriter fw = new FileWriter(absolutePath);
                 BufferedWriter bw = new BufferedWriter(fw)) {
                bw.write(txtEditor.getText());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        lblApplicationStatus.setText("File Successfully saved.");
    }

    public void btnSaveAs_OnAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File ");
        File file = fileChooser.showSaveDialog(txtEditor.getScene().getWindow());

        if (file == null) {
            return;
        }

        try (FileWriter fw = new FileWriter(file);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(txtEditor.getText());
            absolutePath = file.getAbsolutePath();
            //System.out.println(txtEditor.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
        lblApplicationStatus.setText("File Successfully saved.");
    }

    public void mnuPrint_OnAction(ActionEvent actionEvent) {
        boolean printDialog = printerJob.showPrintDialog(txtEditor.getScene().getWindow());
        if (printDialog){
            printerJob.printPage(txtEditor.lookup("Text"));
        }
    }

    public void mnuPageSetup_OnAction(ActionEvent actionEvent) {
        printerJob.showPageSetupDialog(txtEditor.getScene().getWindow());
    }

    public void mnuCut_OnAction(ActionEvent actionEvent) {
        txtEditor.cut();
        lblApplicationStatus.setText("| Cut completed");
    }

    public void mnuCopy_OnAction(ActionEvent actionEvent) {
        txtEditor.copy();
        lblApplicationStatus.setText("| Copy completed");
    }

    public void mnuPaste_OnAction(ActionEvent actionEvent) {
        txtEditor.paste();
        lblApplicationStatus.setText("| Paste completed");
    }

    public void mnuAbout_OnAction(ActionEvent actionEvent) throws IOException {

        Stage secondaryStage = new Stage();
        Parent root = FXMLLoader.load(this.getClass().getResource("/view/AboutForm.fxml"));
        Scene secondaryScene = new Scene(root);
        secondaryStage.setScene(secondaryScene);
        secondaryStage.centerOnScreen();
        secondaryStage.initOwner(txtEditor.getScene().getWindow());
        secondaryStage.initModality(Modality.WINDOW_MODAL);
        secondaryStage.setResizable(false);
        secondaryStage.setTitle("About Us");
        secondaryStage.show();

    }

    public void mnuItemFormatFont_OnAction(ActionEvent actionEvent) throws IOException {

        Stage secondaryStage = new Stage();
        AnchorPane root = FXMLLoader.load(this.getClass().getResource("/view/FormatFontForm.fxml"));
        Scene secondaryScene = new Scene(root);
        secondaryStage.setScene(secondaryScene);
        secondaryStage.centerOnScreen();
        secondaryStage.initOwner(txtEditor.getScene().getWindow());
        secondaryStage.initModality(Modality.APPLICATION_MODAL);
        secondaryStage.setResizable(false);
        secondaryStage.setTitle("Format Font");
        secondaryStage.setUserData(txtEditor.getSelectedText());
       // System.out.println(secondaryStage.getUserData());
        secondaryStage.show();
        secondaryStage.setOnCloseRequest(event->{
            System.out.println(FormatFontFormController.getChangedFontName());
            String changedFontName = FormatFontFormController.getChangedFontName();


        });
    }

    public Font getEditorFont() {
        return editorFont;
    }

    public void setEditorFont(Font editorFont) {
       this.editorFont = editorFont;
    }


    public void txtEditor_OnDragOver(DragEvent dragEvent) {
        if (dragEvent.getDragboard().hasFiles()){
            dragEvent.acceptTransferModes(TransferMode.ANY);
        }

    }


    public void txtEditor_OnDragDropped(DragEvent dragEvent) {
        if (dragEvent.getDragboard().hasFiles()){
            File file = dragEvent.getDragboard().getFiles().get(0);
            openFile(file);
            dragEvent.setDropCompleted(true);
        }
    }
}

class Index {
    private int startIndex;
    private int endIndex;

    public Index(int startIndex, int endIndex) {
        this.setStartIndex(startIndex);
        this.setEndIndex(endIndex);
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }
}
