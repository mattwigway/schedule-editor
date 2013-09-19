/*
    Schedule Editor: edit General Transit Feed Specification feeds.
    Copyright (C) 2013 Matthew Wigginton Conway.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

 */

package org.indicatrix.schedule_editor.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import lombok.Getter;

import org.indicatrix.schedule_editor.model.DataManager;
import org.indicatrix.schedule_editor.model.Pattern;

/**
 * Provides a class that allows GUI editing of a pattern 
 * @author matthewc
 */
public class PatternEditor extends JPanel {
    @Getter
    private Pattern pattern;
    private DataManager dataManager;
    private PatternTableModel tableModel;
    private AgencyAndIdSelector idSelector;
    private JTextField blockIdField;
    private DirectionIdSelector directionIdSelector;
    
    public PatternEditor (Pattern pattern, DataManager dataManager) {
        super(new BorderLayout());
        
        this.pattern = pattern;
        this.dataManager = dataManager;
        
        // set up the table
        tableModel = new PatternTableModel(pattern, dataManager.getDao());
        JTable table = new JTable(tableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        JPanel tripArea = new JPanel(new GridLayout(0, 2));
        
        // put the gridlayout inside a flowlayout to prevent expansion
        // http://stackoverflow.com/questions/4699892
        JPanel tripAreaSuper = new JPanel();
        tripAreaSuper.add(tripArea);
        
        // build the trip information area
        tripArea.add(new JLabel("Agency and ID"));
        idSelector = new AgencyAndIdSelector(dataManager);
        tripArea.add(idSelector);
        
        tripArea.add(new JLabel("Block ID"));
        blockIdField = new JTextField("", 10);
        tripArea.add(blockIdField);
        
        tripArea.add(new JLabel("Direction ID"));
        directionIdSelector = new DirectionIdSelector();
        tripArea.add(directionIdSelector);
        
        // TODO: Service and shape IDs
        
        // build the layout
        // create the content frames
        JScrollPane tableArea = new JScrollPane(table);
        JScrollPane tripAreaScr = new JScrollPane(tripAreaSuper);
        JScrollPane stopTimeArea = new JScrollPane();
        
        JSplitPane attrArea = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tripAreaScr, stopTimeArea);
        JSplitPane vertSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tableArea, attrArea);
        
        this.add(vertSplit, BorderLayout.CENTER);
    }
}
