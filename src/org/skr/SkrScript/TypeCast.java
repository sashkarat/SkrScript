package org.skr.SkrScript;

/**
 * Created by rat on 15.12.14.
 */
public class TypeCast {
    public static boolean cast(Value value, byte dstDts, RunContext rc) {
        if (dstDts == value.dts)
            return true;
        if (dstDts == Def.DTS_STRING) {
            value.setAsString((value.val == null) ? "null" : value.val.toString());
            return true;
        }
        return rc.extension != null && rc.extension.typeCast(value, dstDts, rc) ||
                Engine.printError("TypeCast", "Can't cast: " + value.dts + " to " + dstDts, rc);
    }
}
