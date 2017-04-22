package com.github.jjYBdx4IL.graphics.examples;

import com.github.jjYBdx4IL.utils.awt.AWTUtils;
import javax.swing.JFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jjYBdx4IL
 */
public class FilterEvalMain {

    private static final Logger LOG = LoggerFactory.getLogger(FilterEvalMain.class);
    
    public static void main(String[] args) {
        new FilterEvalMain().run();
    }

    private void run() {
        ControlFrame controlFrame = new ControlFrame();
        
        controlFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        controlFrame.pack();
        AWTUtils.centerOnScreen(AWTUtils.getMousePointerScreenDeviceIndex(), controlFrame);
        controlFrame.setVisible(true);
    }
}
