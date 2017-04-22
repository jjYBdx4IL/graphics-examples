package com.github.jjYBdx4IL.graphics.examples.filterdefs;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_imgproc;

/**
 *
 * @author jjYBdx4IL
 */
public class CannyFilter extends AbstractFilter {

    public CannyFilter() {
        super(2);
        getParamDefs().add(new PositiveThresholdParamDef("Threshold 1"));
        getParamDefs().add(new PositiveThresholdParamDef("Threshold 2"));
    }

    @Override
    public opencv_core.IplImage process(opencv_core.IplImage img) {
        opencv_imgproc.cvCanny(img, img, (Double)getParam(0), (Double)getParam(1));
        return img;
    }
    
    @Override
    public boolean requiresGrayImage() {
        return true;
    }
}
