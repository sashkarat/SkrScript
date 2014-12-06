package org.skr.SkrScript;

/**
 * Created by rat on 06.12.14.
 */
public abstract class EngineExtension {
    protected abstract boolean opArithmetic( byte opCode, RunContext rc );
    protected abstract boolean getProperty( Value obj, int propCode, Value result, RunContext rc );
    protected abstract boolean setProperty( RunContext.PropertyRef pr, Value v, RunContext rc );
    protected abstract String getDtsString( byte dts );
    protected abstract boolean buildInFunc( int address, RegisterPool args, int numOfArgs, Value result, RunContext rc );
}
