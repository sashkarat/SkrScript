package org.skr.SkrScript;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by rat on 14.12.14.
 */
public class PropertyPool {

    public static interface Adapter {

        public boolean get( Value obj, Value res, RunContext rc );
        public boolean set( Value obj, Value value, RunContext rc );
    }

    protected static class PropertyList {
        final HashMap<Byte, Adapter> adaptersMap = new HashMap<Byte, Adapter>();
    }

    protected static final HashMap<Integer, PropertyList> propertyMap = new HashMap<Integer, PropertyList>();

    protected static boolean setPropertyAdapter( byte dts, int propertyCode, Adapter adapter ) {
        if ( !propertyMap.containsKey( propertyCode ) )
            propertyMap.put( propertyCode, new PropertyList() );
        PropertyList pl = propertyMap.get( propertyCode );
        pl.adaptersMap.put(dts, adapter );
        return true;
    }

    protected static Adapter getPropertyAdapter( byte dts, int propertyCode ) {
        PropertyList pl = propertyMap.get( propertyCode );
        if ( pl == null ) {
            System.err.println("PropertyPool.getPropertyAdapter. Undefined property code: " + propertyCode);
            return null;
        }
        Adapter adapter =  pl.adaptersMap.get( dts );
        if ( adapter == null )
            System.err.println("PropertyPool.getPropertyAdapter. Invalid dts");
        return adapter;
    }
}
