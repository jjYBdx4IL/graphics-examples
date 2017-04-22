package com.github.jjYBdx4IL.graphics.examples.filterdefs;

/**
 *
 * @author jjYBdx4IL
 */
public class DoubleParamDef extends NumberParamDef<Double> {

    public DoubleParamDef(String paramName, Double minValue, Double maxValue) {
        super(paramName, minValue, maxValue);
    }
    
    @Override
    public String toString(Object paramValue) {
        if (paramValue instanceof Double) {
            return ((Double)paramValue).toString();
        }
        throw new IllegalArgumentException(paramValue.toString());
    }

    @Override
    public Object parseString(String text) {
        try {
            return new Double(Double.parseDouble(text));
        } catch (NumberFormatException ex) {}
        return null;
    }

    @Override
    public boolean isValid(Object value) {
        return (value instanceof Double) && validate((Double)value);
    }
}
