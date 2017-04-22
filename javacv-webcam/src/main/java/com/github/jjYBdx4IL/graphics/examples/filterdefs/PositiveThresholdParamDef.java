package com.github.jjYBdx4IL.graphics.examples.filterdefs;

/**
 *
 * @author jjYBdx4IL
 */
public class PositiveThresholdParamDef extends DoubleParamDef {

    public PositiveThresholdParamDef(String paramName) {
        super(paramName, new Double(0d), Double.MAX_VALUE);
    }
    
}
