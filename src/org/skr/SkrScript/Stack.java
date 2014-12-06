package org.skr.SkrScript;


/**
 * Created by rat on 29.11.14.
 */
public class Stack<T> {
    Object [] items;
    int size = 0;

    public Stack(int size) {
        if ( size < 1 )
            return;
        items = new Object[ size ];
    }

    public void allocate( int size ) {
        Object [] ni = new Object[ size ];

        if ( items == null ) {
            items = ni;
            return;
        }

        for ( int i = 0; i < this.size; i++ ) {
            if ( i >= ni.length )
                break;
            ni[i] = items[i];
        }
        items = ni;
    }

    public void push( T v ) {
        if ( size == items.length ) {
            allocate( size + size / 2 );
        }
        items[ size++ ] = v;
    }

    public T pop() {
        if ( size == 0 )
            return null;
        return (T) items[ --size];
    }

    public T peek() {
        if ( size == 0 )
            return null;
        return (T) items[ size - 1 ];
    }

    public T get( int i ) {
        if ( size == 0 )
            return null;
        return (T) items[ i ];
    }

    public boolean isEmpty() {
        return size < 1;
    }

    public void reset() {
        size = 0;
    }
}
