package dev.johnwatts.plugins.certificates.strategies;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import dev.johnwatts.plugins.certificates.shared.Result;

public class FindFromSelection implements CertificateFindingStrategy<Editor> {
    @java.lang.Override
    public Result find(Editor source) {
        Result result = new Result();

        SelectionModel selectionModel = source.getSelectionModel();
        if(selectionModel.hasSelection()) {
            result.setCertificate(Base64X509Decoder.decode(selectionModel.getSelectedText()));
        }
        return result;
    }
}
