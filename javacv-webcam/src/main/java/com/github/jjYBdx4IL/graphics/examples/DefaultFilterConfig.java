package com.github.jjYBdx4IL.graphics.examples;

import com.github.jjYBdx4IL.graphics.examples.filterdefs.ConvertToGrayFilter;

/**
 *
 * @author jjYBdx4IL
 */
public class DefaultFilterConfig extends FilterConfig {

    public DefaultFilterConfig() {
        getFilters().add(new ConvertToGrayFilter());
    }
}
