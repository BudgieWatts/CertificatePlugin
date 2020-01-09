package dev.johnwatts.plugins.certificates.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import dev.johnwatts.plugins.certificates.strategies.FindByBeginAndEnd;
import dev.johnwatts.plugins.certificates.strategies.FindFromDer;
import dev.johnwatts.plugins.certificates.strategies.FindFromPem;
import dev.johnwatts.plugins.certificates.strategies.FindFromSelection;
import dev.johnwatts.plugins.certificates.shared.Result;
import dev.johnwatts.plugins.certificates.dialogs.CertificateDialog;
import dev.johnwatts.plugins.certificates.strategies.FindingStrategy;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DecodeBase64X509CertificateAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {

        Result result;

            final List<FindingStrategy> strategies = List.of(new FindFromPem(), new FindFromDer(), new FindFromSelection(), new FindByBeginAndEnd());
            try {
                result = strategies.stream().map(strategy -> strategy.find(event))
                        .filter(Result::isSuccessful)
                        .findFirst()
                        .orElse(Result.noCertificateFound());
            } catch (IllegalArgumentException e) {
                result = Result.exceptionThrown(e);
            }
        CertificateDialog dialog = new CertificateDialog(null, result);
        dialog.show();
    }

    @Override
    public void update(AnActionEvent e) {
        Project project = e.getProject();
        e.getPresentation().setEnabledAndVisible(project != null);
    }
}
