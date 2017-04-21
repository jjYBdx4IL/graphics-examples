package com.github.jjYBdx4IL.graphics.examples.filterdefs;

import org.bytedeco.javacpp.opencv_core;

/**
 *
 * @author jjYBdx4IL
 */
public class CannyFilter extends AbstractFilter {

    public double threshold1;
    public double threshold2;
    
    public static final boolean REQUIRES_GRAYSCALE_INPUT = true;

    @Override
    public opencv_core.IplImage process(opencv_core.IplImage img) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
