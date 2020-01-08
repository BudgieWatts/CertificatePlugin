package dev.johnwatts.plugins.certificates.dialogs;

import javax.swing.*;
import java.awt.*;

class PanelBuilder {
    private JPanel panel;
    private GridBagConstraints constraints;

    public PanelBuilder() {
        this.constraints = new GridBagConstraints();
        this.constraints.insets = new Insets(5,0, 5, 5);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.NORTH;
        this.panel = new JPanel(new GridBagLayout());
    }

    public void addLabel(String labelText) {
        int anchor = constraints.anchor;
        constraints.gridx = 0;
        constraints.anchor = GridBagConstraints.EAST + GridBagConstraints.NORTH;
        panel.add(new JLabel(labelText, SwingConstants.RIGHT), constraints);
        constraints.anchor = anchor;
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

    public void setFill(int gridBagConstraint) {
        this.constraints.fill = gridBagConstraint;
    }

    public void setAnchor(int gridBagConstraint) {
        this.constraints.anchor = gridBagConstraint;
    }

    public void setInsets(Insets insets) {
        this.constraints.insets = insets;
    }

    public JPanel getPanel() {
        return this.panel;
    }
}
