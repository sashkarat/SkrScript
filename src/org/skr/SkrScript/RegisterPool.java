package org.skr.SkrScript;

/**
 * Created by rat on 29.11.14.
 */
public class RegisterPool {

    Object [] objects;
    byte   [] dataTypes;

    public int    offset = 0;

    public RegisterPool(int num) {
        allocate( num );
    }

    public RegisterPool() {
    }

    public void allocate( int num ) {
        if ( num <= 0 )
            return;
        objects = new Object[ num ];
        dataTypes = new byte[ num ];
        for ( int i  = 0; i < num; i++ )
            dataTypes[i] = Def.DTS_NULL;
    }

    private void realloc( int index ) {
        if ( objects == null ) {
            allocate( index + 1 );
            return;
        }
        Object [] oldObjects = objects;
        byte [] oldDataTypes = dataTypes;
        int num = objects.length/2;
        if ( num <= index )
            num  = index + 1;
        allocate( num );
        for ( int i = 0; i < oldObjects.length; i++) {
            if ( i >= objects.length )
                break;
            objects[i] = oldObjects[i];
            dataTypes[i] = oldDataTypes[i];
        }
    }

    public boolean isIndexLegal( int index ) {
        if ( objects == null )
            return false;
        index+= offset;
        return ( index >= 0 || index < objects.length );
    }

    public boolean set( Value v, int index ) {
        index += offset;
        if ( objects == null || index >= objects.length )
            realloc( index );
        objects[ index ] = v.val;
        dataTypes[ index ] = v.dts;
        return true;
    }

    public boolean set( Object val, byte dts, int index ) {
        index += offset;
        if ( objects == null || index >= objects.length )
            realloc( index );
        objects[ index ] = val;
        dataTypes[ index ] = dts;
        return true;
    }

    public void get( Value v,  int index ) {
        index += offset;
        if ( objects == null || index >= objects.length )
            realloc( index );
        v.val = objects[ index ];
        v.dts = dataTypes[ index ];
    }

    public Object getValue( int index ) {
        index += offset;
        if ( objects == null || index >= objects.length )
            realloc( index );
        return objects[ index ];
    }

    public byte getDts( int index ) {
        index += offset;
        if ( objects == null || index >= objects.length )
            realloc( index );
        return dataTypes[ index ];
    }

    public int len() {
        if ( objects == null )
            return 0;
        return objects.length;
    }
}
