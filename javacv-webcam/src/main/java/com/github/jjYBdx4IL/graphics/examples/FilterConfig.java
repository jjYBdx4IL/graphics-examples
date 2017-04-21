package com.github.jjYBdx4IL.graphics.examples;

import com.github.jjYBdx4IL.graphics.examples.filterdefs.AbstractFilter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jjYBdx4IL
 */
public class FilterConfig {

    private List<AbstractFilter> filterDefs = new ArrayList<>();
    
    public FilterConfig() {
        
    }
    
    protected List<AbstractFilter> getFilters() {
        return filterDefs;
    }
}
