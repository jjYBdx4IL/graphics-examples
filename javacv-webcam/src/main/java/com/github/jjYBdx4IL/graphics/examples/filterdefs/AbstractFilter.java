package com.github.jjYBdx4IL.graphics.examples.filterdefs;

import java.util.ArrayList;
import java.util.List;
import org.bytedeco.javacpp.opencv_core.IplImage;

/**
 *
 * @author jjYBdx4IL
 */
public abstract class AbstractFilter {

    private final List<ParamDef> paramDefs = new ArrayList<>();
    private final List<Object> paramValues;
    private final int numParams;

    public AbstractFilter(int numParams) {
        this.numParams = numParams;
        paramValues = new ArrayList<>(numParams);
    }

    public List<ParamDef> getParamDefs() {
        return paramDefs;
    }

    public abstract IplImage process(IplImage img);

    public boolean requiresGrayImage() {
        return false;
    }

    public void setParam(int paramIndex, Object value) {
        for (int i = paramValues.size(); i <= paramIndex && i < numParams; i++) {
            paramValues.add(null);
        }
        paramValues.set(paramIndex, value);
    }

    public Object getParam(int paramIndex) {
        return paramValues.get(paramIndex);
    }

    public void setParamByText(int paramIndex, String text) {
        ParamDef paramDef = paramDefs.get(paramIndex);
        if (paramDef instanceof NumberParamDef) {
            
        }
    }
}
