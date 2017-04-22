package com.github.jjYBdx4IL.graphics.examples;

import com.github.jjYBdx4IL.graphics.examples.filterdefs.AbstractFilter;
import com.github.jjYBdx4IL.graphics.examples.filterdefs.ParamDef;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jjYBdx4IL
 */
public class FilterConfigPanel extends JPanel {

    private static final Logger LOG = LoggerFactory.getLogger(FilterConfigPanel.class);

    private FilterConfig filterConfig = null;
    // filter -> filter param
    private List<List<JTextField>> paramValues = null;
    private final List<ActionListener> listenerList = new ArrayList<>();

    public FilterConfigPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        TitledBorder border = new TitledBorder("Filter Chain");
        setBorder(border);
    }

    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        rebuild();
    }

    private void rebuild() {
        paramValues = new ArrayList<>();

        for (final AbstractFilter filter : filterConfig.getFilters()) {
            List<JTextField> filterParamValues = new ArrayList<>();
            paramValues.add(filterParamValues);

            TitledBorder border = new TitledBorder(filter.getClass().getSimpleName());
            JPanel filterParamsPanel = new JPanel();
            add(filterParamsPanel);
            filterParamsPanel.setBorder(border);
            filterParamsPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            c.gridy = 0;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 0;
            c.weightx = 1.0;

            for (ParamDef paramDef : filter.getParamDefs()) {
                filterParamsPanel.add(new JLabel(paramDef.getParamName()), c);
                c.gridx++;
            }

            c.gridx = 0;
            c.gridy++;

            List<ParamDef> paramDefs = filter.getParamDefs();
            for (int i = 0; i < paramDefs.size(); i++) {
                final ParamDef paramDef = paramDefs.get(i);
                JTextField paramValue = new JTextField();
                final int j = i;
                paramValue.setAction(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Object value = paramDef.parseString(paramValue.getText());
                        if (value != null && paramDef.isValid(value)) {
                            filter.setParam(j, value);
                            FilterConfigPanel.this.informListeners();
                        }
                    }
                });
                paramValue.setText(paramDef.toString(filter.getParam(i)));
                filterParamValues.add(paramValue);
                filterParamsPanel.add(paramValue, c);
                c.gridx++;
            }
        }
    }

    public void addActionListener(ActionListener l) {
        listenerList.add(l);
    }

    private void informListeners() {
        for (ActionListener listener : listenerList) {
            listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "?!?"));
        }
    }
}
