package controller;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditorFormController {
    public TextArea txtEditor;
    public TextField txtFind;
    public int findOffSet = 0;

    public void mnuItemNew_OnAction(ActionEvent actionEvent) {
    }

    public void mnuItemExit_OnAction(ActionEvent actionEvent) {
    }

    public void mnuItemFind_OnAction(ActionEvent actionEvent) {
    }

    public void mnuItemReplace_OnAction(ActionEvent actionEvent) {
    }

    public void mnuItemSelectAll_OnAction(ActionEvent actionEvent) {
        txtEditor.selectAll();
    }

    public void btnFindNext_OnAction(ActionEvent actionEvent) {
        String pattern = txtFind.getText();
        String text = txtEditor.getText();

        Pattern regExp = Pattern.compile(pattern);
        Matcher matcher = regExp.matcher(text);

        boolean exist = matcher.find(findOffSet);
        if (exist){
            findOffSet = matcher.start() + 1;
            txtEditor.selectRange(matcher.start(), matcher.end());
        }
    }

    public void btnFindPrevious_OnAction(ActionEvent actionEvent) {
    }
}
