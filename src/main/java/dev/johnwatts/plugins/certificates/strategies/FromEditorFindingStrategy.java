package dev.johnwatts.plugins.certificates.strategies;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import dev.johnwatts.plugins.certificates.shared.NoSourceException;

public abstract class FromEditorFindingStrategy extends FindingStrategy {
    @Override
    protected Editor getSource(AnActionEvent event) throws NoSourceException {
        Editor source = event.getData(CommonDataKeys.EDITOR);
        if ( source == null ) {
            throw new NoSourceException("Event was not triggered from editor window");
        }
        FileDocumentManager.getInstance().saveAllDocuments();
        return source;
    }
}
