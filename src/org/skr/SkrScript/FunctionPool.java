package org.skr.SkrScript;

import java.util.ArrayList;

/**
 * Created by rat on 14.12.14.
 */
public class FunctionPool {
    public interface Adapter {
        public boolean act(RegisterPool args, int numOfArgs, Value res, RunContext rc );
    }

    protected static ArrayList<Adapter> adapters = new ArrayList<Adapter>();

    protected static boolean setAdapter( int address, Adapter adapter ) {
        int index  = -address - 1;
        if ( index < 0 ) {
            System.err.println("FunctionPool.setBuildInFunction. Invalid address: " + address);
            return false;
        }
        while ( index >= adapters.size() )
            adapters.add( null );
        adapters.set( index,  adapter );
        return true;
    }

    protected static int getFirstFreeAddress() {
        return - ( adapters.size() + 1 );
    }

    protected static int getNumberOfAdapters() {
        return adapters.size();
    }

    protected static Adapter getAdapter( int address ) {
        try {
            return adapters.get( -address - 1);
        } catch ( IndexOutOfBoundsException e ) {
            System.err.println("FunctionPool.getAdapter. Invalid address: " + address);
            return null;
        }
    }
}
