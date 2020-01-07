package dev.johnwatts.plugins.certificates.dialogs;

import javax.swing.*;
import java.awt.*;

class DialogBuilder {
    private JPanel panel;
    private GridBagConstraints constraints;

    public DialogBuilder() {
        this.constraints = new GridBagConstraints();
        this.constraints.insets = new Insets(5,5, 5, 5);
        this.panel = new JPanel(new GridBagLayout());
    }

    public void addLabel(String labelText) {
        constraints.gridx = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JLabel(labelText, SwingConstants.RIGHT), constraints);
    }

    public void addLabelledText(String labelText, String text) {
        JLabel displayedText = new JLabel(text);
        this.addLabelledComponent(labelText, displayedText);
    }

    public void addLabelledComponent(String labelText, Component component) {
        this.addLabel(labelText);

        constraints.gridx = 1;
        panel.add(component, constraints);
    }

    public void addSpanningComponent(Component component) {
        constraints.gridx = 0;
        constraints.gridwidth = 2;
        panel.add(component, constraints);
    }

    public JPanel getPanel() {
        return this.panel;
    }
}
