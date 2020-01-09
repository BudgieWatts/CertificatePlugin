package dev.johnwatts.plugins.certificates.strategies;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.util.UserDataHolder;
import dev.johnwatts.plugins.certificates.shared.NoSourceException;
import dev.johnwatts.plugins.certificates.shared.Result;

public abstract class FindingStrategy {
    protected abstract UserDataHolder getSource(AnActionEvent event) throws NoSourceException;
    public abstract Result find(AnActionEvent source);
}
