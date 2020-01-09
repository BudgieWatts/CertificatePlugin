package dev.johnwatts.plugins.certificates.strategies;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import dev.johnwatts.plugins.certificates.shared.NoSourceException;
import dev.johnwatts.plugins.certificates.shared.Result;

public class FindFromSelection extends FromEditorFindingStrategy {
    @Override
    public Result find(AnActionEvent source) {
        Result result = new Result();

        SelectionModel selectionModel;
        try {
            selectionModel = this.getSource(source).getSelectionModel();
            if(selectionModel.hasSelection()) {
                result.setCertificate(Base64X509Decoder.decode(selectionModel.getSelectedText()));
            }
        } catch (NoSourceException e) {
            result = Result.exceptionThrown(e);
        }
        return result;
    }
}
