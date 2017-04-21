package com.github.jjYBdx4IL.graphics.examples.filterdefs;

import org.bytedeco.javacpp.opencv_core;
import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_imgproc;
import static org.bytedeco.javacpp.opencv_imgproc.CV_RGB2GRAY;

/**
 *
 * @author jjYBdx4IL
 */
public class ConvertToGrayFilter extends AbstractFilter {

    public IplImage process(IplImage img) {
        IplImage grayImg = opencv_core.cvCreateImage(opencv_core.cvSize(img.width(), img.height()), IPL_DEPTH_8U, 1);
        opencv_imgproc.cvCvtColor(img, grayImg, CV_RGB2GRAY);
        return grayImg;
    }
}
