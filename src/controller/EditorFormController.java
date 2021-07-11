package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.print.PrinterJob;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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

    public void initialize() {
        setEditorFont(Font.getDefault());
        //setEditorFont(Font.font("Ubuntu"));
        pneFind.setVisible(false);
        pneReplace.setVisible(false);
        this.printerJob = PrinterJob.createPrinterJob();

        ChangeListener textListener = (ChangeListener<String>) (observable, oldValue, newValue) -> {
            searchMatches(newValue);
        };

        txtSearch.textProperty().addListener(textListener);
        txtFind.textProperty().addListener(textListener);

       // System.out.println(txtEditor.getFont().getName());
        //txtEditor.setFont(new Font(12));
        txtEditor.setFont(editorFont);
        String changedFontName = FormatFontFormController.getChangedFontName();
        System.out.println(changedFontName);

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
        /*if (!searchList.isEmpty()) {
            if (findOffSet == -1) {
                findOffSet = 0;
            }
            txtEditor.selectRange(searchList.get(findOffSet).getStartIndex(), searchList.get(findOffSet).getEndIndex());
            selectedText = txtEditor.getSelectedText();
            txtEditor.setStyle("-fx-highlight-fill: #fff500;");
            startPoint = searchList.get(findOffSet).getStartIndex();
            endPoint = searchList.get(findOffSet).getEndIndex();

            findOffSet++;
            if (findOffSet >= searchList.size()) {
                findOffSet = 0;
            }
        }*/

        if (!searchList.isEmpty()) {
            findOffSet++;
            if (findOffSet >= searchList.size()) {
                findOffSet = 0;
            }
            txtEditor.selectRange(searchList.get(findOffSet).getStartIndex(), searchList.get(findOffSet).getEndIndex());
        }
    }

    public void btnFindPrevious_OnAction(ActionEvent actionEvent) {
        /*if (!searchList.isEmpty()) {
            if (findOffSet == -1) {
                findOffSet = searchList.size() - 1;
            }
            txtEditor.selectRange(searchList.get(findOffSet).getStartIndex(), searchList.get(findOffSet).getEndIndex());
            findOffSet--;
            if (findOffSet < 0) {
                findOffSet = searchList.size() - 1;
            }
        }*/

        if (!searchList.isEmpty()) {
            findOffSet--;
            if (findOffSet < 0) {
                findOffSet = searchList.size() - 1;
            }
            txtEditor.selectRange(searchList.get(findOffSet).getStartIndex(), searchList.get(findOffSet).getEndIndex());
        }
    }

    public void btnReplace_OnAction(ActionEvent actionEvent) {
        /*if (!selectedText.isEmpty()) {
            selectedText = txtReplaceText.getText();
            txtEditor.replaceText(startPoint, endPoint, txtReplaceText.getText());
        }*/
        if (findOffSet == -1) return;
        txtEditor.replaceText(searchList.get(findOffSet).getStartIndex(), searchList.get(findOffSet).getEndIndex(), txtReplaceText.getText());
        searchMatches(txtFind.getText());
    }

    public void btnReplaceAll_OnAction(ActionEvent actionEvent) {
        /*for (Index index : searchList) {
            txtEditor.selectRange(index.getStartIndex(), index.getEndIndex());
            selectedText = txtEditor.getSelectedText();

            if (!selectedText.isEmpty()) {
                txtEditor.setStyle("-fx-highlight-fill: #fff500;");
                selectedText = txtReplaceText.getText();
                txtEditor.replaceText(index.getStartIndex(), index.getEndIndex(), txtReplaceText.getText());
            }
        }*/


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
        System.out.println(absolutePath);
        try (FileReader fileReader = new FileReader(file);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                txtEditor.appendText(line + '\n');
            }
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
                //System.out.println(txtEditor.getText());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
    }

    public void mnuPrint_OnAction(ActionEvent actionEvent) {
        boolean printDialog = printerJob.showPrintDialog(txtEditor.getScene().getWindow());
        if (printDialog){
            printerJob.printPage(txtEditor.lookup("Text"));
        }
    }

    public void mnuPageSetup_OnAction(ActionEvent actionEvent) {
        //Window window = txtEditor.getScene().getWindow();
        //System.out.println(window);
        printerJob.showPageSetupDialog(txtEditor.getScene().getWindow());
    }

    public void mnuCut_OnAction(ActionEvent actionEvent) {
        txtEditor.cut();
    }

    public void mnuCopy_OnAction(ActionEvent actionEvent) {
        txtEditor.copy();
    }

    public void mnuPaste_OnAction(ActionEvent actionEvent) {
        txtEditor.paste();
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
