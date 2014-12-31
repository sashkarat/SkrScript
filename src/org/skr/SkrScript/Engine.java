package org.skr.SkrScript;

/**
 * Created by rat on 22.11.14.
 */
public class Engine {

    RunContext rc = new RunContext( 8, 16);
    EngineExtension extension;

    boolean errEnabled = true;
    boolean outEnabled = true;

    public Engine() {
    }

    public boolean isErrEnabled() {
        return errEnabled;
    }

    public void setErrEnabled(boolean errEnabled) {
        this.errEnabled = errEnabled;
    }

    public boolean isOutEnabled() {
        return outEnabled;
    }

    public void setOutEnabled(boolean outEnabled) {
        this.outEnabled = outEnabled;
    }

    public void setExtension( EngineExtension extension ) {
        this.extension = extension;
        extension.setup();
    }

    public boolean init( Slot slot ) {
        return slot.script == null || exec(slot.script.initPoint, slot);
    }

    public boolean run(Slot slot) {
        return slot.script == null || exec(slot.script.runPoint, slot);
    }

    boolean exec( int startPoint, Slot slot ) {

//        printMsg("run. start from: "  + startPoint );

        if ( ! (slot.enabled && slot.script.enabled )  )
            return true;

        rc.setSlot(slot);
        rc.reset();
        rc.setExtension( extension );
        rc.setErrEnabled( errEnabled );
        rc.setOutEnabled( outEnabled );


        if ( !RunContext.run(startPoint, rc) ) {
            printError("SkrScriptEngine. script execution failed. Script will be disabled.", rc);
            slot.script.enabled = false;
            return false;
        }

//        printMsg("run.done");
        return true;
    }

    public static String getDtsStr(byte dts, RunContext rc) {
        switch ( dts ) {
            case Def.DTS_BOOL:
                return "bool";
            case Def.DTS_NULL:
                return "null";
            case Def.DTS_INT:
                return "int";
            case Def.DTS_FLOAT:
                return "float";
            case Def.DTS_REG:
                return "reg";
            case Def.DTS_VAR:
                return "var";
            case Def.DTS_STRING:
                return "string";
            case Def.DTS_TYPE:
                return "type";
            case Def.DTS_PROP_CODE:
                return "propertyCode";
            default:
                if ( rc!= null && rc.extension != null )
                    return rc.extension.getDtsString( dts );
                return "<" + dts + ">";
        }
    }

    public static boolean printError( String msg, RunContext rc ) {
        System.err.println("Script Run Error "+ "<" + (rc.opPos) + ">: " + " " + msg);
        return false;
    }

    public static boolean printError( String tag, String msg, RunContext rc ) {
        System.err.println("Script Run Error "+ "<" + (rc.opPos) + ">: " + tag + " " + msg);
        return false;
    }

    public static boolean peArgNumError( String tag, RunContext rc ) {
        System.err.println("Script Run Error "+ "<" + (rc.opPos) + ">: " + tag + " argument count mismatch");
        return false;
    }

    public static boolean peArgTypeError( String tag, int idx, RunContext rc ) {
        System.err.println("Script Run Error "+ "<" + (rc.opPos) + ">: " + tag + " argument " + idx + " type mismatch");
        return false;
    }

    public static boolean peCastFailed( String tag, byte srcDts, byte dstDts, RunContext rc ) {
        return printError(tag, " Unable to cast: " + getDtsStr(srcDts, rc)
                + " to " + getDtsStr( dstDts, rc ), rc);
    }

    public static boolean pePropertyIsReadOnly(int propCode, RunContext rc ) {
        return printError("setProperty: " + propCode, " Property is read only ", rc);
    }

    public static boolean peSetPropInvalidType(int propCode, RunContext rc) {
        return printError("setProperty: " + propCode, " Invalid value type", rc );
    }

    protected static void printMsg( String tag, String msg, RunContext rc ) {
        System.out.println("Script Run Msg " + "<" + (rc.opPos) + ">: " + tag + " " + msg);
    }



}
