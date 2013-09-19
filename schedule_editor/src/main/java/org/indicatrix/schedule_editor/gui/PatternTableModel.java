package org.indicatrix.schedule_editor.gui;

import javax.swing.table.AbstractTableModel;

import org.indicatrix.schedule_editor.model.Pattern;
import org.onebusaway.gtfs.model.Stop;
import org.onebusaway.gtfs.model.StopTime;
import org.onebusaway.gtfs.model.Trip;
import org.onebusaway.gtfs.services.GtfsMutableRelationalDao;

import lombok.Getter;

/**
 * A table adapter that shows both arrival times and run times from a Pattern.
 * @author mattwigway
 */
public class PatternTableModel extends AbstractTableModel {
    @Getter
    private Pattern pattern;
    private GtfsMutableRelationalDao dao;
    
    public PatternTableModel (Pattern pattern, GtfsMutableRelationalDao dao) {
        this.pattern = pattern;
        this.dao = dao;
    }
    
    public int getColumnCount() {
        return pattern.getStops().size();
    }

    public int getRowCount() {
        // Why + 1? Because we have header columns with the stop names.
        return pattern.getTrips().size() * 2 + 1;
    }

    public Object getValueAt(int row, int col) {
        if (row == 0) {
            // we need to get the heading for this column, i.e. the stop name + ID
            Stop stop = pattern.getStops().get(col);
            return "<html>" + stop.getName() + "<br>(" + stop.getId().toString() + ")</html>";
        }
        
        // Odd row (1, 3, &c.): show times
        else if (row % 2 == 1) {
            Trip trip = pattern.getTrips().get((row - 1) / 2);
            StopTime st = dao.getStopTimesForTrip(trip).get(col);
            int arrTime = st.getArrivalTime();
            int depTime = st.getDepartureTime();
            return formatTime(arrTime, depTime);
        }
        
        // even row: show running times
        else {
            Trip trip = pattern.getTrips().get((row - 2) / 2);
            
            // no running time to first stop
            if (col == 0) return "";
            
            StopTime stFrom = dao.getStopTimesForTrip(trip).get(col - 1);
            StopTime stTo = dao.getStopTimesForTrip(trip).get(col);
            
            int diff = stTo.getArrivalTime() - stFrom.getDepartureTime();
            
            int seconds = diff % 60;
            diff -= seconds;
            return String.format("%d:%02d", diff / 60, seconds);
        }
    }
    
    /**
     * Format arrival and departure times for human consumption
     */
    private String formatTime(int arrTime, int depTime) {
        // only show one time if that's all that's needed
        if (arrTime == depTime)
            return "<html>" + getTimeString(arrTime) + "</html>";
        else
            return "<html>" + getTimeString(arrTime) + "<br>" + getTimeString(depTime) + "</html>";
    }

    /** Format a single time for human consumption */
    private String getTimeString(int time) {
        int seconds = time % 60;
        time -= seconds;
        
        int minutes = time % 3600;
        time -= minutes;
        minutes /= 60;
        
        int hours = time / 3600;
        
        if (hours > 12) {
            hours -= 12;
            if (seconds == 0)
                return String.format("<b>%02d%02d</b>", hours, minutes);
            else
                return String.format("<b>%02d%02d%02d</b>", hours, minutes, seconds);
        }
        else {
            if (seconds == 0)
                return String.format("%02d%02d", hours, minutes);
            else
                return String.format("%02d%02d%02d", hours, minutes, seconds);
        }
            
    }
}
