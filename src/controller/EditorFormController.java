package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class EditorFormController {
    public TextArea txtEditor;
    public TextField txtFind;
    public AnchorPane pneFind;
    public TextField txtSearch;
    public AnchorPane pneReplace;
    public TextField txtReplaceText;

    private int findOffSet = -1;
    private List<Index> searchList = new ArrayList<Index>();
    private int searchIndex = 0;

    public void initialize(){
        pneFind.setVisible(false);
        pneReplace.setVisible(false);

        txtFind.textProperty().addListener((observable, oldValue, newValue) -> {
            try{

                Pattern regExp = Pattern.compile(newValue);
                Matcher matcher = regExp.matcher(txtEditor.getText());

                searchList.clear();

                while (matcher.find()){
                    searchList.add(new Index(matcher.start(), matcher.end()));
                }
            }catch (PatternSyntaxException e){

            }
        });

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            try{

                Pattern regExp = Pattern.compile(newValue);
                Matcher matcher = regExp.matcher(txtEditor.getText());

                searchList.clear();

                while (matcher.find()){
                    searchList.add(new Index(matcher.start(), matcher.end()));
                }
            }catch (PatternSyntaxException e){

            }
        });

    }

    public void mnuItemNew_OnAction(ActionEvent actionEvent) {
        txtEditor.clear();
        txtEditor.requestFocus();
    }

    public void mnuItemExit_OnAction(ActionEvent actionEvent) {
    }

    public void mnuItemFind_OnAction(ActionEvent actionEvent) {
        pneReplace.setVisible(false);
        pneFind.setVisible(true);
        txtFind.requestFocus();
    }

    public void mnuItemReplace_OnAction(ActionEvent actionEvent) {
        pneFind.setVisible(false);
        pneReplace.setVisible(true);
        txtSearch.requestFocus();
    }

    public void mnuItemSelectAll_OnAction(ActionEvent actionEvent) {
        txtEditor.selectAll();
    }

    public void btnFindNext_OnAction(ActionEvent actionEvent) {
        if (!searchList.isEmpty()){
            if (findOffSet == -1){
                findOffSet = 0;
            }
            txtEditor.selectRange(searchList.get(findOffSet).startIndex, searchList.get(findOffSet).endIndex);
            findOffSet++;
            if (findOffSet >= searchList.size()){
                findOffSet = 0;
            }
        }
    }

    public void btnFindPrevious_OnAction(ActionEvent actionEvent) {
        if (!searchList.isEmpty()){
            if (findOffSet == -1){
                findOffSet = searchList.size() - 1;
            }
            txtEditor.selectRange(searchList.get(findOffSet).startIndex, searchList.get(findOffSet).endIndex);
            findOffSet--;
            if (findOffSet < 0){
                findOffSet = searchList.size() - 1;
            }
        }
    }

    public void btnReplace_OnAction(ActionEvent actionEvent) {
    }

    public void btnReplaceAll_OnAction(ActionEvent actionEvent) {
    }

    public void Escape_OnAction(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ESCAPE){
            pneReplace.setVisible(false);
            pneFind.setVisible(false);
            txtFind.clear();
            txtSearch.clear();
            txtReplaceText.clear();
        }
    }
}

class Index{
    int startIndex;
    int endIndex;

    public Index(int startIndex, int endIndex) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }
}
