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

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerListModel;

import org.indicatrix.schedule_editor.model.DataManager;
import org.onebusaway.gtfs.model.Agency;
import org.onebusaway.gtfs.model.AgencyAndId;

/**
 * Select an AgencyAndId using agencies in the feed
 * @author mattwigway
 */
public class AgencyAndIdSelector extends JPanel {
    private DataManager dataManager;
    private List<String> agencies;
    private JSpinner agencySelector;
    private JTextField idEntry;

    public AgencyAndIdSelector (DataManager dataManager) {
        super();
        
        this.dataManager = dataManager;
        
        // Agency selector
        updateAgencyList();
        SpinnerListModel slm = new SpinnerListModel(agencies);
        agencySelector = new JSpinner(slm);
        this.add(agencySelector);
        
        idEntry = new JTextField("", 15);
        this.add(idEntry);
    }
    
    /** Update the list of agencies for the spinner */
    private void updateAgencyList () {
        agencies = new ArrayList<String>();
        for (Agency agency : dataManager.getDao().getAllAgencies()) {
            agencies.add(agency.getId());
        }
        
        try {
            ((SpinnerListModel) agencySelector.getModel()).setList(agencies);
        } catch (NullPointerException e) {
            // Spinner not initialized: do nothing
        }
    }
    
    public void setAgencyAndId(AgencyAndId id) {
        updateAgencyList();
        
        try {
            // Note: this works even if the strings are not at the same memory address, because it uses
            // List.indexOf which uses String.equals not ==
            agencySelector.setValue(id.getAgencyId());
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Agency " + id.getAgencyId() +
                " was passed into AgencyAndIdSelector but does not exist in loaded GTFS.");
        }
        idEntry.setText(id.getId());
    }
    
    public AgencyAndId getAgencyAndId() {
        // intern the strings in case lazy programmers use == to compare them
        return new AgencyAndId(((String) agencySelector.getValue()).intern(),
                idEntry.getText().intern());
    }
}
