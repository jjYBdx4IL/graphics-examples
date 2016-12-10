package com.github.jjYBdx4IL.graphics.examples;

import com.github.jjYBdx4IL.utils.env.CI;
import org.junit.Test;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import org.freehep.postscript.PSInputFile;
import org.freehep.postscript.PSPanel;
import org.freehep.postscript.Processor;
import static org.junit.Assert.assertFalse;
import org.junit.Assume;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jjYBdx4IL
 */
public class PostscriptTest {
    
    private static final Logger LOG = LoggerFactory.getLogger(PostscriptTest.class);
    
    private final AtomicBoolean windowClosed = new AtomicBoolean(false);
    private volatile boolean edtError = false;
    
    @Test
    public void test() throws IOException, URISyntaxException, InterruptedException, InvocationTargetException {
        Assume.assumeFalse(CI.isCI());
        
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                try {
                    File exampleEPS = new File(PostscriptTest.class.getResource("example.eps").toURI());

                    // Create Panels
                    JPanel panel = new JPanel();
                    panel.setLayout(new GridLayout(1, 2));
                    PSPanel ps1 = new PSPanel();
                    panel.add(ps1);
                    PSPanel ps2 = new PSPanel();
                    panel.add(ps2);

                    // Show Panel
                    JFrame frame = new JFrame("Embedded PSViewer");
                    frame.getContentPane().add(panel);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setSize(1024, 800);
                    frame.setVisible(true);

                    // Create processors and associate to panels and input files
                    Processor processor1 = new Processor(ps1);
                    processor1.setData(new PSInputFile(exampleEPS.getAbsolutePath()));
                    Processor processor2 = new Processor(ps2);
                    processor2.setData(new PSInputFile(exampleEPS.getAbsolutePath()));

                    // Process
                    processor1.process();
                    processor2.process();
                    
                    Timer timer = new Timer(3000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                        }
                    });
                    timer.setRepeats(false);
                    
                    frame.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            timer.stop();
                            synchronized (windowClosed) {
                                windowClosed.set(true);
                                windowClosed.notify();
                            }
                        }
                    });
                    
                    timer.start();
                } catch (Exception ex) {
                    edtError = true;
                    throw new RuntimeException(ex);
                }
            }
        });
        waitForWindowClosing();
        assertFalse(edtError);
    }
    
    protected void waitForWindowClosing() throws InterruptedException {
        synchronized (windowClosed) {
            while (!windowClosed.get()) {
                LOG.debug("waitForWindowClosing(): wait for windowClosed");
                windowClosed.wait(1000l);
            }
        }
    }
}
