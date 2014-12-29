package org.skr.SkrScript;

/**
 * Created by rat on 28.12.14.
 */
public class ValueStack extends ValuePool {
    int head = -1;

    public ValueStack(int num) {
        super(num);
    }

    public void reset() {
        head = -1;
    }

    public void push( Value v ) {
        set( v, ++head );
    }

    public void pop( Value v ) {
        if ( head < 0 )
            return;
        v.set( get( head-- ) );
    }
}
