package org.skr.SkrScript;

/**
 * Created by rat on 29.11.14.
 */
public class ValuePool {

    Value [] values = null;

    public int offset = 0;

    public ValuePool(int num) {
        allocate( num );
    }

    public ValuePool() {
    }

    public void allocate( int num ) {
        if ( num <= 0 )
            return;
        values = new Value[num];
        for ( int i  = 0; i < num; i++ )
            values[i] = new Value();
    }

    private void realloc( int index ) {
        if ( values == null ) {
            allocate( index + 1 );
            return;
        }
        Value [] oldRegs = values;
        int num = values.length/2;
        if ( num <= index )
            num  = index + 1;
        allocate( num );
        for ( int i = 0; i < oldRegs.length; i++) {
            if ( i >= values.length )
                break;
            values[i].set(oldRegs[i]);
        }
    }

    public boolean isIndexLegal( int index ) {
        if ( values == null )
            return false;
        index+= offset;
        return ( index >= 0 || index < values.length );
    }

    private int updateIndex( int index ) {
        index += offset;
        if ( values == null || index >= values.length )
            realloc( index );
        return index;
    }

    public boolean set( Value v, int index ) {
        index = updateIndex( index );
        values[index].set( v );
        return true;
    }

    public boolean set( Object val, byte dts, int index ) {
        index = updateIndex( index );
        values[index].set( val, dts );
        return true;
    }

    public Value get(int index ) {
        index = updateIndex( index );
        return values[index];
    }

    public int len() {
        if ( values == null )
            return 0;
        return values.length;
    }


}
