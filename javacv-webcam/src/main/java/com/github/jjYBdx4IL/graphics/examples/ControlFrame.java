package com.github.jjYBdx4IL.graphics.examples;

import com.github.jjYBdx4IL.graphics.examples.filterdefs.AbstractFilter;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import io.github.lukehutch.fastclasspathscanner.matchprocessor.FileMatchProcessor;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
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
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter.ToIplImage;
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

    private FilterConfig filterConfig = new DefaultFilterConfig();

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

        c.gridx = 0;
        c.gridy++;
        c.weightx = 1.0;
        c.weighty = 1.0;
        container.add(originalImage, c);

        c.gridx = 0;
        c.gridy++;
        c.weightx = 1.0;
        c.weighty = 1.0;
        container.add(finalImage, c);

        processFilters();
    }

    public void apply(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        processFilters();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        LOG.info(e.toString());
        if (ActionEvent.ACTION_PERFORMED == e.getID() && e.getActionCommand().equals(ACMD_PIC_SELECTION_CHANGED)) {
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
        image = scale(image, originalImage.getPreferredSize().width, originalImage.getPreferredSize().width);
        originalImage.setIcon(new ImageIcon(image));

        IplImage iplImage = toIplImage(image);
        for (AbstractFilter filter : filterConfig.getFilters()) {
            iplImage = filter.process(iplImage);
        }
        
        image = toBufferedImage(iplImage);
        finalImage.setIcon(new ImageIcon(image));
    }

    public static BufferedImage scale(BufferedImage sbi, int dWidth, int dHeight) {
        BufferedImage dbi = null;
        if (sbi != null) {
            dbi = new BufferedImage(dWidth, dHeight, sbi.getType());
            Graphics2D g = dbi.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            AffineTransform at = AffineTransform.getScaleInstance(1d * dWidth / sbi.getWidth(), 1d * dHeight / sbi.getHeight());
            g.drawRenderedImage(sbi, at);
        }
        return dbi;
    }

    public static IplImage toIplImage(BufferedImage bufImage) {
        ToIplImage iplConverter = new OpenCVFrameConverter.ToIplImage();
        Java2DFrameConverter java2dConverter = new Java2DFrameConverter();
        IplImage iplImage = iplConverter.convert(java2dConverter.convert(bufImage));
        return iplImage;
    }
    
    
    public static BufferedImage toBufferedImage(IplImage iplImage) {
        Java2DFrameConverter converter = new Java2DFrameConverter();
        OpenCVFrameConverter.ToMat cvt = new OpenCVFrameConverter.ToMat();
        Frame frame = cvt.convert(iplImage);
        return converter.getBufferedImage(frame, converter.getBufferedImageType(frame) ==
                BufferedImage.TYPE_CUSTOM ? 1.0 : 1.0, false, null);
    }
}
