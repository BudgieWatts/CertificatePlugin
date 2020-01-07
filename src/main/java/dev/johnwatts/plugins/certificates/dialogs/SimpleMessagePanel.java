package dev.johnwatts.plugins.certificates.dialogs;

import dev.johnwatts.plugins.certificates.shared.Result;

import javax.swing.*;
import java.awt.*;
import java.util.function.Supplier;

class SimpleMessagePanel implements Supplier<JComponent> {

    private final Result result;

    public SimpleMessagePanel(Result result) {
        this.result = result;
    }

    @Override
    public JComponent get() {
        JPanel panel = new JPanel(new GridLayout());
        panel.add(new JLabel(result.getMessage()));
        return panel;
    }
}
