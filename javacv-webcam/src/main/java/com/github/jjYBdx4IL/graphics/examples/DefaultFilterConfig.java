package com.github.jjYBdx4IL.graphics.examples;

import com.github.jjYBdx4IL.graphics.examples.filterdefs.CannyFilter;
import com.github.jjYBdx4IL.graphics.examples.filterdefs.ConvertToGrayFilter;

/**
 *
 * @author jjYBdx4IL
 */
public class DefaultFilterConfig extends FilterConfig {

    public DefaultFilterConfig() {
        getFilters().add(new ConvertToGrayFilter());
        
        CannyFilter filter = new CannyFilter();
        filter.setParam(0, new Double(90));
        filter.setParam(1, new Double(90));
        getFilters().add(filter);
    }
}
