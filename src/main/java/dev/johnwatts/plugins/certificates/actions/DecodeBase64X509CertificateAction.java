package dev.johnwatts.plugins.certificates.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import dev.johnwatts.plugins.certificates.strategies.CertificateFindingStrategy;
import dev.johnwatts.plugins.certificates.strategies.FindByBeginAndEnd;
import dev.johnwatts.plugins.certificates.strategies.FindFromSelection;
import dev.johnwatts.plugins.certificates.shared.Result;
import dev.johnwatts.plugins.certificates.dialogs.CertificateDialog;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DecodeBase64X509CertificateAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {

        Editor editor = event.getData(CommonDataKeys.EDITOR);
        if (editor == null) {
            return;
        }

        final List<CertificateFindingStrategy<Editor>> strategies = List.of(new FindFromSelection(), new FindByBeginAndEnd());
        Result result;

        try {
            result = strategies.stream().map(strategy -> strategy.find(editor))
                    .filter(Result::isSuccessful)
                    .findFirst()
                    .orElse(Result.noCertificateFound());
        } catch (IllegalArgumentException e) {
            result = Result.exceptionThrown(e);
        }
        CertificateDialog dialog = new CertificateDialog(editor.getProject(), result);
        dialog.show();
    }

    @Override
    public void update(AnActionEvent e) {
        Project project = e.getProject();
        e.getPresentation().setEnabledAndVisible(project != null);
    }
}
