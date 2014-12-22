package org.skr.SkrScript;

/**
 * Created by rat on 06.12.14.
 */
public abstract class EngineExtension {
    public static boolean setBuildInFunction(int address, FunctionPool.Adapter adapter) {
        if ( address > Def.EXTENDED_BUILD_IN_FUNCTION_ADDRESS ) {
            System.err.println("EngineExtension.setBuildInFunction. " +
                    "Address must be less or equal Def.EXTENDED_BUILD_IN_FUNCTION_ADDRESS");
            return false;
        }
        return FunctionPool.setAdapter( address, adapter );
    }

    public static boolean setProperty( int property, byte dts, PropertyPool.Adapter adapter ) {
        return PropertyPool.setPropertyAdapter( dts, property, adapter );
    }

    protected abstract boolean opArithmetic( byte opCode, Value l, Value r, Value res, RunContext rc );
    protected abstract String getDtsString( byte dts );
    protected abstract Object cast( Value value, byte dstDts, RunContext rc );
    protected abstract void setup();
}
