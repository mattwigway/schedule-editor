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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import lombok.Getter;

/**
 * A very simple direction ID selector: click it and the direction changes from 0 to 1.
 * @author mattwigway
 */
public class DirectionIdSelector extends JButton {
    @Getter
    int currentDirection;
    
    public DirectionIdSelector() {
        this(0);
    }
    
    public DirectionIdSelector(int currentDirection) {
        super();
        setCurrentDirection(currentDirection);
        
        this.addActionListener(new Clicked());
    }
    
    public void setCurrentDirection(int currentDirection) {
        if (currentDirection != 1 && currentDirection != 0)
            throw new IllegalArgumentException("Direction must be 0 or 1");
        
        this.currentDirection = currentDirection;
        this.setText("" + this.currentDirection);
    }
    
    private class Clicked implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            if (currentDirection == 0) setCurrentDirection(1);
            else setCurrentDirection(0);
        }
    }
}
