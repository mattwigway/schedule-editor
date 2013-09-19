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

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;

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
    
    public PatternEditor (Pattern pattern, DataManager dataManager) {
        super(new BorderLayout());
        
        this.pattern = pattern;
        this.dataManager = dataManager;
        
        // set up the table
        tableModel = new PatternTableModel(pattern, dataManager.getDao());
        JTable table = new JTable(tableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        // build the layout
        // create the content frames
        JScrollPane tableArea = new JScrollPane(table);
        JScrollPane tripArea = new JScrollPane();
        JScrollPane stopTimeArea = new JScrollPane();
        
        JSplitPane attrArea = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tripArea, stopTimeArea);
        JSplitPane vertSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tableArea, attrArea);
        
        this.add(vertSplit, BorderLayout.CENTER);
    }
}
