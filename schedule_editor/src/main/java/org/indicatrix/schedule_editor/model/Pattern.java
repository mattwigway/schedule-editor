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

package org.indicatrix.schedule_editor.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.onebusaway.gtfs.model.Stop;
import org.onebusaway.gtfs.model.StopTime;
import org.onebusaway.gtfs.model.Trip;
import org.onebusaway.gtfs.services.GtfsRelationalDao;

import lombok.Getter;

/**
 * Represents a trip pattern, a unique sequence of stops.
 * This is not in the GTFS spec but is useful for editing.
 * @author mattwigway
 */
public class Pattern {
    @Getter private List<Stop> stops;
    @Getter private List<Trip> trips;
    
    // Cannot instantiate directly
    private Pattern () {}
    
    /**
     * Build patterns for a bunch of trips.
     */
    public static List<Pattern> buildPatternsForTrips(GtfsRelationalDao dao, Collection<Trip> trips) {
        List<Pattern> patterns = new ArrayList<Pattern>();
        
        TRIPS: for (Trip trip : trips) {
            List<Stop> stops = new ArrayList<Stop>();
            
            for (StopTime st : dao.getStopTimesForTrip(trip)) {
                stops.add(st.getStop());
            }
            
            // see if it matches any pattern thus far defined
            for (Pattern pattern : patterns) {
                if (stops.equals(pattern.stops)) {
                    pattern.trips.add(trip);
                    break TRIPS;
                }
            }
            
            // if we get this far, it matches no pattern
            Pattern pattern = new Pattern();
            pattern.stops = stops;
            pattern.trips = new ArrayList<Trip>();
            pattern.trips.add(trip);
            patterns.add(pattern);
        }
        
        return patterns;
    }
}
