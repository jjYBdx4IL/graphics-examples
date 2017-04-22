package com.github.jjYBdx4IL.graphics.examples;

import com.github.jjYBdx4IL.graphics.examples.filterdefs.AbstractFilter;
import java.awt.image.BufferedImage;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jjYBdx4IL
 */
public class FilterProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(FilterProcessor.class);

    private final OpenCVFrameConverter.ToIplImage iplConverter = new OpenCVFrameConverter.ToIplImage();
    private final Java2DFrameConverter java2dConverter = new Java2DFrameConverter();
    private final OpenCVFrameConverter.ToMat matConverter = new OpenCVFrameConverter.ToMat();

    public BufferedImage run(BufferedImage inputImage, FilterConfig filterConfig) {
        opencv_core.IplImage iplImage = toIplImage(inputImage);
        
        for (AbstractFilter filter : filterConfig.getFilters()) {
            iplImage = filter.process(iplImage);
        }

        return toBufferedImage(iplImage);
    }

    public opencv_core.IplImage toIplImage(BufferedImage bufImage) {
        opencv_core.IplImage iplImage = iplConverter.convert(java2dConverter.convert(bufImage));
        return iplImage;
    }

    public BufferedImage toBufferedImage(opencv_core.IplImage iplImage) {
        Frame frame = matConverter.convert(iplImage);
        return java2dConverter.getBufferedImage(frame, java2dConverter.getBufferedImageType(frame)
                == BufferedImage.TYPE_CUSTOM ? 1.0 : 1.0, false, null);
    }
}
