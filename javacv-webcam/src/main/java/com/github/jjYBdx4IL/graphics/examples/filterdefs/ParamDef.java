package com.github.jjYBdx4IL.graphics.examples.filterdefs;

/**
 *
 * @author jjYBdx4IL
 */
public abstract class ParamDef {

    private final String paramName;
    
    public ParamDef(String paramName) {
        this.paramName = paramName;
    }
    
    /**
     * @return the paramName
     */
    public String getParamName() {
        return paramName;
    }
    
    public abstract String toString(Object paramValue);
    public abstract Object parseString(String text);
    
    public abstract boolean isValid(Object value);
}
