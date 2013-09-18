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

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

public class MainWindow {    
    private static void showWindow() {
        JFrame window = new JFrame("Schedule Editor");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Set up the layout
        
        JPanel treeArea = new JPanel();
        JPanel contentArea = new JPanel();
        
        JScrollPane treeAreaScr = new JScrollPane(treeArea);
        JScrollPane contentAreaScr = new JScrollPane(contentArea);
        
        // Set up the left menu/tree and the main content area
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeAreaScr,
                contentAreaScr);
        
        window.getContentPane().add(splitPane);
        
        // now that layout is set, add the tree
        MutableTreeNode agencyNode = new DefaultMutableTreeNode("Agency");
        MutableTreeNode routesNode = new DefaultMutableTreeNode("Routes");
        MutableTreeNode stopsNode = new DefaultMutableTreeNode("Stops");
        MutableTreeNode calendarNode = new DefaultMutableTreeNode("Calendar");
        MutableTreeNode feedInfoNode = new DefaultMutableTreeNode("Feed Info");
        
        JTree tree = new JTree(new TreeNode[] {
            agencyNode,
            routesNode,
            stopsNode,
            calendarNode,
            feedInfoNode
        });
        treeArea.add(tree);
        
        window.pack();
        window.setVisible(true);
    }
    
    public static void main (String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run () {
                showWindow();
            }
        });
    }
}
