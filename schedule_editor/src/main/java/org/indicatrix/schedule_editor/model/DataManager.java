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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.onebusaway.gtfs.impl.GtfsRelationalDaoImpl;
import org.onebusaway.gtfs.model.Route;
import org.onebusaway.gtfs.serialization.GtfsReader;
import org.onebusaway.gtfs.services.GtfsMutableRelationalDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;

/**
 * The data manager controls access to data and keeps things in sync.
 * @author mattwigway
 */
public class DataManager {
    private static Logger _log = LoggerFactory.getLogger(DataManager.class);
    
    @Getter
    private GtfsMutableRelationalDao dao;
    private HashMap<Route, List<Pattern>> patterns;    
    
    public DataManager() {
        dao = null;
    }
    
    /** Get the patterns for the given route */
    public List<Pattern> getPatternsForRoute (Route route) {
        return patterns.get(route);
    }
    
    public void loadGtfs (File path) throws IllegalAccessException, IOException {       
        if (dao != null) {
            _log.error("DataManager re-use attempted");
            throw new IllegalAccessException("DataManagers can only be used once");
        }
        
        _log.debug("Reading GTFS");
        dao = new GtfsRelationalDaoImpl();
        GtfsReader reader = new GtfsReader();
        reader.setInputLocation(path);
        reader.setEntityStore(dao);
        reader.run();
        
        _log.debug("Building patterns for routes");
        buildPatternsForAllRoutes();
        _log.debug("GTFS loaded");
    }
    
    /**
     * Build a cache of trip patterns for every loaded route.
     */
    private void buildPatternsForAllRoutes() {
        patterns = new HashMap<Route, List<Pattern>>();
        
        for (Route route : dao.getAllRoutes()) {
            buildPatternsForRoute(route);
        }
    }
    
    /**
     * Build a cache of trip patterns for the given route.
     */
    private void buildPatternsForRoute(Route route) {
        List<Pattern> routePatterns =
                Pattern.buildPatternsForTrips(dao, dao.getTripsForRoute(route));
        patterns.put(route, routePatterns);
    }
}
