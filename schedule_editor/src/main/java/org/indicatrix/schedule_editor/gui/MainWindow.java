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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.indicatrix.schedule_editor.model.DataManager;
import org.indicatrix.schedule_editor.model.Pattern;
import org.onebusaway.gtfs.model.Route;
import org.onebusaway.gtfs.services.GtfsMutableRelationalDao;

public class MainWindow {
    /** The schedule data manager used by all of the manipulations */
    private static DataManager dataManager;
    private static JFrame window;
    private static DefaultTreeModel tree;
    private static JPanel contentArea;
    
    private static MutableTreeNode routesNode;
    
    private static void showWindow() {
        window = new JFrame("Schedule Editor");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Set up the menu
        JMenuBar menuBar = new JMenuBar();
        
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        fileMenu.getAccessibleContext()
            .setAccessibleDescription("Opening, saving, importing and exporting schedule data");
        menuBar.add(fileMenu);
        
        JMenuItem importGtfsMenu = new JMenuItem("Import GTFS", KeyEvent.VK_I);
        importGtfsMenu.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));
        importGtfsMenu.addActionListener(new ImportGtfsAction());
        fileMenu.add(importGtfsMenu);
        
        window.setJMenuBar(menuBar);
        
        JPanel treeArea = new JPanel();
        contentArea = new JPanel();
        
        JScrollPane treeAreaScr = new JScrollPane(treeArea);
        JScrollPane contentAreaScr = new JScrollPane(contentArea);
        
        // Set up the left menu/tree and the main content area
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeAreaScr,
                contentAreaScr);
        
        window.getContentPane().add(splitPane);
        
        // now that layout is set, add the tree
        MutableTreeNode agencyNode = new DefaultMutableTreeNode("Agency");
        routesNode = new DefaultMutableTreeNode("Routes"); 
        MutableTreeNode stopsNode = new DefaultMutableTreeNode("Stops");
        MutableTreeNode calendarNode = new DefaultMutableTreeNode("Calendar");
        MutableTreeNode feedInfoNode = new DefaultMutableTreeNode("Feed Info");
        
        MutableTreeNode rootNode = new DefaultMutableTreeNode("Schedule Editor");
        tree = new DefaultTreeModel(rootNode);
        tree.insertNodeInto(agencyNode, rootNode, 0);
        tree.insertNodeInto(routesNode, rootNode, 1);
        tree.insertNodeInto(stopsNode, rootNode, 2);
        tree.insertNodeInto(calendarNode, rootNode, 3);
        tree.insertNodeInto(feedInfoNode, rootNode, 4);
        
        JTree treeView = new JTree(tree);
        treeView.addTreeSelectionListener(new HandleTreeSelect());
        treeArea.add(treeView);
        
        window.pack();
        window.setVisible(true);
    }
    
    // actions
    /** Handle a GTFS import */
    private static class ImportGtfsAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFileChooser fc = new JFileChooser();
            int ret = fc.showOpenDialog(window);
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                
                // TODO: save changes to existing project?
                // create a new Data Manager for a new GTFS feed
                dataManager = new DataManager();
                
                if (file == null) return;
                
                try {
                    dataManager.loadGtfs(file);
                } catch (IllegalAccessException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                
                // populate the tree
                GtfsMutableRelationalDao dao = dataManager.getDao();
                
                MutableTreeNode parentNode;
                MutableTreeNode childNode;
                
                for (Route route : dao.getAllRoutes()) {
                    parentNode = new DefaultMutableTreeNode(new DisplayRoute(route));
                    tree.insertNodeInto(parentNode, routesNode, routesNode.getChildCount());
                    
                    for (Pattern pattern : dataManager.getPatternsForRoute(route)) {
                        childNode = new DefaultMutableTreeNode(pattern);
                        tree.insertNodeInto(childNode, parentNode, parentNode.getChildCount());
                    }
                }
            }
        }
    }
    
    /** Handle a tree selection */
    private static class HandleTreeSelect implements TreeSelectionListener {
        public void valueChanged(TreeSelectionEvent e) {
            // get the relevant node
            // TODO: multiple selections
            if (e.getPaths().length == 0) return;
            
            TreePath path = e.getPaths()[0];
            Object obj = ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject();
            
            if (obj instanceof Pattern) showPattern((Pattern) obj);
        }
        
        private void showPattern (Pattern pattern) {
            PatternTableModel ptm = new PatternTableModel(pattern, dataManager.getDao());
            JTable table = new JTable(ptm);
            
            contentArea.removeAll();
            contentArea.add(table);
            contentArea.revalidate();
        }
    }
    
    public static void main (String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run () {
                showWindow();
            }
        });
    }
}
