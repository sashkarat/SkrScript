package org.skr.SkrScript;

/**
 * Created by rat on 15.12.14.
 */
public class TypeCast {
    public static boolean cast(Value value, byte dstDts, RunContext rc) {
        if ( dstDts == value.dts )
            return true;
        if ( dstDts == Def.DTS_STRING ) {
            value.setAsString( (value.val == null) ? "null" : value.val.toString() );
            return true;
        }
        if ( rc.extension != null && rc.extension.typeCast( value, dstDts, rc ) )
            return true;
        System.err.println("TypeCast. Can't cast from: " + value.dts + " to " + dstDts );
        return false;
    }
}
