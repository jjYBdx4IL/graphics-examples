package com.github.jjYBdx4IL.graphics.examples;

import com.github.jjYBdx4IL.utils.env.Maven;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.junit.Test;

import org.apache.batik.transcoder.TranscodingHints;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.util.SVGConstants;
import org.apache.commons.io.FileUtils;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jjYBdx4IL
 */
public class BatikSVGTest {

    private static final Logger LOG = LoggerFactory.getLogger(BatikSVGTest.class);

    @Test
    public void test1() throws FileNotFoundException, TranscoderException, IOException, URISyntaxException {
        // Create a JPEG transcoder
        JPEGTranscoder t = new JPEGTranscoder();

        // Set the transcoding hints.
        t.addTranscodingHint(JPEGTranscoder.KEY_QUALITY,
                new Float(.8));

        // Create the transcoder input.
        TranscoderInput input = new TranscoderInput(getClass().getResourceAsStream("example.svg"));

        // Create the transcoder output.
        File outputFile = new File(Maven.getMavenTargetDir(), getClass().getName() + "1.jpg");
        OutputStream ostream = new FileOutputStream(outputFile);
        TranscoderOutput output = new TranscoderOutput(ostream);

        // Save the image.
        t.transcode(input, output);

        // Flush and close the stream.
        ostream.flush();
        ostream.close();
    }

    @Test
    public void test2() throws IOException, URISyntaxException, TranscoderException {
        File svgFile = new File(getClass().getResource("example.svg").toURI());

        String css = "svg {"
                + "shape-rendering: geometricPrecision;"
                + "text-rendering:  geometricPrecision;"
                + "color-rendering: optimizeQuality;"
                + "image-rendering: optimizeQuality;"
                + "}";

        final List<BufferedImage> result = new ArrayList<>();

        File cssFile = File.createTempFile("batik-default-override-", ".css");
        try {
            FileUtils.write(cssFile, css, "ASCII");

            TranscoderInput input = new TranscoderInput(new FileInputStream(svgFile));

            TranscodingHints transcoderHints = new TranscodingHints();
            transcoderHints.put(ImageTranscoder.KEY_XML_PARSER_VALIDATING, Boolean.FALSE);
            transcoderHints.put(ImageTranscoder.KEY_DOM_IMPLEMENTATION,
                    SVGDOMImplementation.getDOMImplementation());
            transcoderHints.put(ImageTranscoder.KEY_DOCUMENT_ELEMENT_NAMESPACE_URI,
                    SVGConstants.SVG_NAMESPACE_URI);
            transcoderHints.put(ImageTranscoder.KEY_DOCUMENT_ELEMENT, "svg");
            transcoderHints.put(ImageTranscoder.KEY_BACKGROUND_COLOR, Color.white);
            transcoderHints.put(ImageTranscoder.KEY_USER_STYLESHEET_URI, cssFile.toURI().toString());

            ImageTranscoder t = new ImageTranscoder() {

                @Override
                public BufferedImage createImage(int w, int h) {
                    LOG.info("ImageTranscoder->createImage " + w + " " + h);
                    return new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                }

                @Override
                public void writeImage(BufferedImage image, TranscoderOutput out)
                        throws TranscoderException {
                    LOG.info("ImageTranscoder->writeImage " + image.toString());
                    BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
                    Graphics2D g = (Graphics2D) img.getGraphics();
                    g.drawImage(image, 0, 0, null);
                    g.dispose();
                    result.add(img);
//                    result.add(image);
                }
            };
            t.setTranscodingHints(transcoderHints);
            t.transcode(input, null);
        } finally {
            cssFile.delete();
        }

        assertEquals(1, result.size());
        assertNotNull(result.get(0));

        File outputFile = new File(Maven.getMavenTargetDir(), getClass().getName() + "2.jpg");
        if (outputFile.exists()) {
            outputFile.delete();
        }
        LOG.info("writing to " + outputFile.getAbsolutePath());
        ImageIO.write(result.get(0), "jpg", outputFile);
    }
}
