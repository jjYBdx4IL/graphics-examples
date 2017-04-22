package com.github.jjYBdx4IL.graphics.examples;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JFrame;
import javax.swing.JTextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jjYBdx4IL
 */
public class ParamInputFrame extends JFrame implements ActionListener {

    private static final Logger LOG = LoggerFactory.getLogger(ParamInputFrame.class);

    protected final AtomicInteger threshold1Value = new AtomicInteger(Float.floatToIntBits(90f));
    protected final AtomicInteger threshold2Value = new AtomicInteger(Float.floatToIntBits(90f));
    protected final AtomicInteger apertureSizeValue = new AtomicInteger(3);

    protected final JTextField threshold1 = new JTextField(Float.toString(Float.intBitsToFloat(threshold1Value.get())));
    protected final JTextField threshold2 = new JTextField(Float.toString(Float.intBitsToFloat(threshold2Value.get())));
    protected final JTextField apertureSize = new JTextField(Integer.toString(apertureSizeValue.get()));

    public ParamInputFrame() {
        super("JavaCV Webcam Parameters");

        GridBagConstraints c = new GridBagConstraints();
        GridBagLayout layout = new GridBagLayout();
        Container container = getContentPane();
        container.setLayout(layout);

        c.gridx = 0;
        c.weightx = .5;
        c.fill = GridBagConstraints.HORIZONTAL;
        
        c.gridy = 0;
        container.add(threshold1, c);
        c.gridy++;
        container.add(threshold2, c);
        c.gridy++;
        container.add(apertureSize, c);

        threshold1.addActionListener(this);
        threshold2.addActionListener(this);
        apertureSize.addActionListener(this);

        pack();
        setPreferredSize(new Dimension(getWidth()*2, getHeight()));
        pack();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        LOG.info(e.toString());
        try {
            if (e.getSource() == threshold1) {
                threshold1Value.set(Float.floatToIntBits(Float.parseFloat(threshold1.getText())));
            }
            if (e.getSource() == threshold2) {
                threshold2Value.set(Float.floatToIntBits(Float.parseFloat(threshold2.getText())));
            }
            if (e.getSource() == apertureSize) {
                apertureSizeValue.set(Integer.parseInt(apertureSize.getText()));
            }
        } catch (NumberFormatException ex) {
            LOG.error("", ex);
        }
    }

    public float threshold1() {
        return Float.intBitsToFloat(threshold1Value.get());
    }

    public float threshold2() {
        return Float.intBitsToFloat(threshold2Value.get());
    }

    public int apertureSize() {
        return apertureSizeValue.get();
    }
}
