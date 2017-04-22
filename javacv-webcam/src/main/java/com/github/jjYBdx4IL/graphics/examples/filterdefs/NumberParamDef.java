package com.github.jjYBdx4IL.graphics.examples.filterdefs;

/**
 *
 * @author jjYBdx4IL
 * @param <T>
 */
public abstract class NumberParamDef<T extends Comparable> extends ParamDef {

    private final T minValue;
    private final T maxValue;
    
    public NumberParamDef(String paramName, T minValue, T maxValue) {
        super(paramName);
        if (minValue == null || maxValue == null) {
            throw new IllegalArgumentException();
        }
        this.minValue = minValue;
        this.maxValue = maxValue;
    }
    
    public boolean validate(T value) {
        if (value == null) {
            return false;
        }
        if (value.compareTo(minValue) < 0) {
            return false;
        }
        if (value.compareTo(maxValue) > 0) {
            return false;
        }
        return true;
    }
    
    /**
     * @return the minValue
     */
    public T getMinValue() {
        return minValue;
    }

    /**
     * @return the maxValue
     */
    public T getMaxValue() {
        return maxValue;
    }

}
