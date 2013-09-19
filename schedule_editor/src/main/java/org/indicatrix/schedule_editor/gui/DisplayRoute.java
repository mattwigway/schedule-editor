package org.indicatrix.schedule_editor.gui;

import org.onebusaway.gtfs.model.Route;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** TreeNode always uses the ToString of the object display the text, so we must wrap route to
 * override its toString
 * @author mattwigway
 */
@AllArgsConstructor
public class DisplayRoute {
    @Getter
    private Route route;
    
    public String toString () {
        return route.getShortName() + " " + route.getLongName()
                + " (" + route.getId().toString() + ")";
    }
}
