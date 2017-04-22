package com.github.jjYBdx4IL.graphics.examples;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import io.github.lukehutch.fastclasspathscanner.matchprocessor.FileMatchProcessor;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jjYBdx4IL
 */
public class ControlFrame extends JFrame implements ActionListener {

    private static final Logger LOG = LoggerFactory.getLogger(ControlFrame.class);

    private static final String ACMD_PIC_SELECTION_CHANGED = "PICTURE_SELECTION_CHANGED";

    private final List<String> imageResourcePaths = new ArrayList<>();

    private final JLabel originalImage = new JLabel();
    private final JLabel finalImage = new JLabel();
    private final JComboBox pictureSelectionBox;

    private final FilterConfigPanel filterConfigPanel = new FilterConfigPanel();
    
    private final FilterProcessor processor = new FilterProcessor();
    private FilterConfig filterConfig = null;

    public ControlFrame() {
        // search classpath for test images
        FileMatchProcessor fileMatchProcessor = new FileMatchProcessor() {
            @Override
            public void processMatch(String relativePath, InputStream inputStream, long lengthBytes) throws IOException {
                LOG.debug(relativePath);
                imageResourcePaths.add(relativePath);
            }
        };
        FastClasspathScanner scanner = new FastClasspathScanner();
        scanner.matchFilenamePattern("^org/openimaj/image/.*\\.(png|jpg|jpeg|JPG)", fileMatchProcessor);
        scanner.scan();

        pictureSelectionBox = new JComboBox(imageResourcePaths.toArray());
        pictureSelectionBox.addActionListener(this);
        pictureSelectionBox.setActionCommand(ACMD_PIC_SELECTION_CHANGED);

        filterConfigPanel.addActionListener(this);
        
        originalImage.setPreferredSize(new Dimension(400, 300));
        finalImage.setPreferredSize(new Dimension(400, 300));

        Container container = getContentPane();
        container.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 0.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        container.add(pictureSelectionBox, c);

        c.gridy++;
        c.weighty = 1.0;
        container.add(originalImage, c);

        c.gridy++;
        container.add(filterConfigPanel, c);
        
        c.gridy++;
        container.add(finalImage, c);

        apply(new DefaultFilterConfig());
    }

    public void apply(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        filterConfigPanel.setFilterConfig(filterConfig);
        processFilters();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        LOG.info(e.toString());
        if (ActionEvent.ACTION_PERFORMED == e.getID() && e.getSource() == filterConfigPanel) {
            processFilters();
        }
        else if (ActionEvent.ACTION_PERFORMED == e.getID() && e.getActionCommand().equals(ACMD_PIC_SELECTION_CHANGED)) {
            processFilters();
        }
    }

    private void processFilters() {
        int resourceIndex = pictureSelectionBox.getSelectedIndex();
        BufferedImage image = null;
        try (InputStream is = getClass().getResourceAsStream("/" + imageResourcePaths.get(resourceIndex))) {
            image = ImageIO.read(is);
        } catch (IOException ex) {
            LOG.error("", ex);
        }
        image = ImageUtils.scale(image, originalImage.getPreferredSize().width, originalImage.getPreferredSize().width);
        originalImage.setIcon(new ImageIcon(image));

        image = processor.run(image, filterConfig);
        
        finalImage.setIcon(new ImageIcon(image));
    }
}
