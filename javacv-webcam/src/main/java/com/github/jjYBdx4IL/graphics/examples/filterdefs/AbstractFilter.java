package com.github.jjYBdx4IL.graphics.examples.filterdefs;

import org.bytedeco.javacpp.opencv_core.IplImage;

/**
 *
 * @author jjYBdx4IL
 */
public abstract class AbstractFilter {

    public abstract IplImage process(IplImage img);
}
