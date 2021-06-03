package dev.johnwatts.plugins.certificates.strategies;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.vfs.VirtualFile;
import dev.johnwatts.plugins.certificates.shared.NoSourceException;

public abstract class FromFileFindingStrategy extends FindingStrategy {
    @Override
    protected VirtualFile getSource(AnActionEvent event) throws NoSourceException {
        VirtualFile file = event.getData(CommonDataKeys.VIRTUAL_FILE);
        if ( file == null ) {
            throw new NoSourceException("No virtual file to be found in event");
        }
        FileDocumentManager.getInstance().saveAllDocuments();
        return file;
    }
}
