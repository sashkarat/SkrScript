package org.skr.SkrScript;

/**
 * Created by rat on 29.11.14.
 */
public class BuildInFunctions {

    private static final RegisterPool args = new RegisterPool( 4 );

    public static boolean call( int addr, RunContext rc, byte numOfArg ) {

        for ( int i = numOfArg - 1; i >= 0; i--) {
            rc.popArg(args, i);
        }

        switch ( addr ) {
            case Def.F_MSG:
                return msg(rc);
            case Def.F_ERR:
                return err( rc );
            case Def.F_SET_SLOT_ENABLED:
                return setSlotEnabled(rc);
            case Def.F_TYPE_TO_STR:
                return typeToStr( rc );
            case Def.F_SIN:
                return sin( rc );
            case Def.F_COS:
                return cos( rc );
            case Def.F_TAN:
                return tan( rc );
            case Def.F_ACOS:
                return acos( rc );
            case Def.F_ASIN:
                return asin( rc );
            case Def.F_ATAN:
                return atan( rc );
            case Def.F_CBRT:
                return cbrt( rc );
            case Def.F_SQRT:
                return sqrt( rc );
            case Def.F_EXP:
                return exp( rc );
            case Def.F_HYPOT:
                return hypot( rc );
            case Def.F_POW:
                return pow( rc );
            case Def.F_LOG:
                return log( rc );
            case Def.F_LOG10:
                return log10( rc );
            case Def.F_MAX:
                return max( rc );
            case Def.F_MIN:
                return min( rc );
            case Def.F_RANDOM:
                return random( rc );
            case Def.F_TO_DEGREES:
                return toDeg( rc );
            case Def.F_TO_RADIANS:
                return toRad( rc );
            case Def.F_ABS:
                return abs( rc );
            case Def.F_FLOOR:
                return floor( rc );
            case Def.F_CEIL:
                return ceil( rc );
            case Def.F_ROUND:
                return round( rc );
            default:
                if ( rc.extension != null ) {
                    return rc.extension.buildInFunc( addr, args, numOfArg, rc.l, rc);
                }
        }
        return printError("BuildInFunctions.call", "Unknown address: " + addr, rc);
    }

    private static boolean round(RunContext rc) {
        if ( args.getDts(0) != Def.DTS_NUMBER )
            return printError("round", "arg is not a number", rc );
        rc.l.set(  Math.round((Float) args.getValue(0) ), Def.DTS_NUMBER  );
        return true;
    }

    private static boolean ceil(RunContext rc) {
        if ( args.getDts(0) != Def.DTS_NUMBER )
            return printError("ceil", "arg is not a number", rc );
        rc.l.set(  (float) Math.ceil((Float) args.getValue(0) ) , Def.DTS_NUMBER  );
        return true;
    }

    private static boolean floor(RunContext rc) {
        if ( args.getDts(0) != Def.DTS_NUMBER )
            return printError("floor", "arg is not a number", rc );
        rc.l.set(  (float) Math.floor((Float) args.getValue(0)) , Def.DTS_NUMBER  );
        return true;
    }

    private static boolean abs(RunContext rc) {
        if ( args.getDts(0) != Def.DTS_NUMBER )
            return printError("abs", "arg is not a number", rc );
        rc.l.set( Math.abs((Float) args.getValue(0) ) , Def.DTS_NUMBER  );
        return true;
    }

    private static boolean toRad(RunContext rc) {
        if ( args.getDts(0) != Def.DTS_NUMBER )
            return printError("toRad", "arg is not a number", rc );
        rc.l.set(  (float) Math.toRadians((Float) args.getValue(0)) , Def.DTS_NUMBER  );
        return true;
    }

    private static boolean toDeg(RunContext rc) {
        if ( args.getDts(0) != Def.DTS_NUMBER )
            return printError("toDeg", "arg is not a number", rc );
        rc.l.set(  (float) Math.toDegrees((Float) args.getValue(0)) , Def.DTS_NUMBER  );
        return true;
    }

    private static boolean random(RunContext rc) {
        rc.l.set(  (float) Math.random() , Def.DTS_NUMBER  );
        return true;
    }

    private static boolean min(RunContext rc) {
        if ( args.getDts(0) != Def.DTS_NUMBER )
            return printError("min", "arg0 is not a number", rc );
        if ( args.getDts(1) != Def.DTS_NUMBER )
            return printError("min", "arg1 is not a number", rc );
        rc.l.set( Math.min((Float) args.getValue(0), (Float) args.getValue(1) ) , Def.DTS_NUMBER );
        return true;
    }

    private static boolean max(RunContext rc) {
        if ( args.getDts(0) != Def.DTS_NUMBER )
            return printError("max", "arg0 is not a number", rc );
        if ( args.getDts(1) != Def.DTS_NUMBER )
            return printError("max", "arg1 is not a number", rc );
        rc.l.set( Math.max((Float) args.getValue(0), (Float) args.getValue(1)) , Def.DTS_NUMBER );
        return true;
    }

    private static boolean log10(RunContext rc) {
        if ( args.getDts(0) != Def.DTS_NUMBER )
            return printError("log10", "arg is not a number", rc );
        rc.l.set(  (float) Math.log10((Float) args.getValue(0)) , Def.DTS_NUMBER  );
        return true;
    }

    private static boolean log(RunContext rc) {
        if ( args.getDts(0) != Def.DTS_NUMBER )
            return printError("log", "arg is not a number", rc );
        rc.l.set(  (float) Math.log((Float) args.getValue(0)) , Def.DTS_NUMBER  );
        return true;
    }

    private static boolean pow(RunContext rc) {
        if ( args.getDts(0) != Def.DTS_NUMBER )
            return printError("pow", "arg0 is not a number", rc );
        if ( args.getDts(1) != Def.DTS_NUMBER )
            return printError("pow", "arg1 is not a number", rc );
        rc.l.set( (float) Math.pow((Float) args.getValue(0), (Float) args.getValue(1)) , Def.DTS_NUMBER );
        return true;

    }

    private static boolean hypot(RunContext rc) {
        if ( args.getDts(0) != Def.DTS_NUMBER )
            return printError("hypot", "arg0 is not a number", rc );
        if ( args.getDts(1) != Def.DTS_NUMBER )
            return printError("hypot", "arg1 is not a number", rc );
        rc.l.set( (float) Math.hypot((Float) args.getValue(0), (Float) args.getValue(1) ) , Def.DTS_NUMBER );
        return true;

    }

    private static boolean exp(RunContext rc) {
        if ( args.getDts(0) != Def.DTS_NUMBER )
            return printError("exp", "arg is not a number", rc );
        rc.l.set(  (float) Math.exp((Float) args.getValue(0)) , Def.DTS_NUMBER  );
        return true;
    }

    private static boolean sqrt(RunContext rc) {
        if ( args.getDts(0) != Def.DTS_NUMBER )
            return printError("sqrt", "arg is not a number", rc );
        rc.l.set(  (float) Math.sqrt((Float) args.getValue(0)) , Def.DTS_NUMBER  );
        return true;
    }

    private static boolean cbrt(RunContext rc) {
        if ( args.getDts(0) != Def.DTS_NUMBER )
            return printError("cbrt", "arg is not a number", rc );
        rc.l.set(  (float) Math.cbrt((Float) args.getValue(0)) , Def.DTS_NUMBER  );
        return true;
    }

    private static boolean atan(RunContext rc) {
        if ( args.getDts(0) != Def.DTS_NUMBER )
            return printError("atan", "arg is not a number", rc );
        rc.l.set(  (float) Math.atan((Float) args.getValue(0)) , Def.DTS_NUMBER  );
        return true;
    }

    private static boolean asin(RunContext rc) {
        if ( args.getDts(0) != Def.DTS_NUMBER )
            return printError("asin", "arg is not a number", rc );
        rc.l.set(  (float) Math.asin((Float) args.getValue(0)) , Def.DTS_NUMBER  );
        return true;
    }

    private static boolean acos(RunContext rc) {
        if ( args.getDts(0) != Def.DTS_NUMBER )
            return printError("acos", "arg is not a number", rc );
        rc.l.set(  (float) Math.acos((Float) args.getValue(0)) , Def.DTS_NUMBER  );
        return true;
    }

    private static boolean tan(RunContext rc) {
        if ( args.getDts(0) != Def.DTS_NUMBER )
            return printError("tan", "arg is not a number", rc );
        rc.l.set(  (float) Math.tan((Float) args.getValue(0)) , Def.DTS_NUMBER  );
        return true;

    }

    private static boolean cos(RunContext rc) {
        if ( args.getDts(0) != Def.DTS_NUMBER )
            return printError("cos", "arg is not a number", rc );
        rc.l.set(  (float) Math.cos((Float) args.getValue(0)) , Def.DTS_NUMBER  );
        return true;
    }

    private static boolean sin(RunContext rc) {
        if ( args.getDts(0) != Def.DTS_NUMBER )
            return printError("sin", "arg is not a number", rc );
        rc.l.set(  (float) Math.sin((Float) args.getValue(0)) , Def.DTS_NUMBER  );
        return true;
    }

    private static boolean typeToStr(RunContext rc ) {

        byte dts = (args.getDts(0) == Def.DTS_TYPE )? (Byte) args.getValue(0) : args.getDts(0);
        rc.l.dts = Def.DTS_STRING;

        switch ( dts ) {
            case Def.DTS_BOOL:
                rc.l.val = "BOOL";
                break;
            case Def.DTS_NULL:
                rc.l.val = "NULL";
                break;
            case Def.DTS_NUMBER:
                rc.l.val = "NUMBER";
                break;
            case Def.DTS_REG:
                rc.l.val = "REG";
                break;
            case Def.DTS_VAR:
                rc.l.val = "VAR";
                break;
            case Def.DTS_STRING:
                rc.l.val = "STR";
                break;
            case Def.DTS_TYPE:
                rc.l.val = "TYPE";
                break;
            default:
                if ( rc.extension != null ) {
                    String s = rc.extension.getDtsString( dts );
                    if ( s != null ) {
                        rc.l.val = s;
                        return true;
                    }
                }
                return printError("typeToStr", "unknown dts: " + dts, rc);
        }
        return true;
    }

    private static boolean setSlotEnabled(RunContext rc) {
        if ( args.getDts(0) != Def.DTS_BOOL )
            return printErrTypeMistmatch("setSlotEnabled", Def.DTS_BOOL, args.getDts(0), rc );
        rc.slot.setEnabled((Boolean) args.getValue(0));
        return true;
    }

    public static boolean msg(RunContext rc) {
        System.out.println("Script msg: " + args.getValue(0).toString());
        return true;
    }

    public static boolean err( RunContext rc) {
        System.out.println("Script error: " + args.getValue(0).toString());
        return true;
    }

    protected static boolean printErrTypeMistmatch( String fname, byte rightType, byte thisType, RunContext rc ) {
        return printError(fname, "Argument type mistmatch. Expected: " + rightType + " but taken: " + thisType, rc );
    }

    protected static boolean printError( String fname, String msg, RunContext rc ) {
        System.err.println("ERROR. ScriptBuildInFunctions"+ "<" + rc.pos + "> " + fname + " " + msg);
        return false;
    }

    protected static void printMsg( String fname, String msg, RunContext rc ) {
        System.out.println("ScriptBuildInFunctions"+ "<" + rc.pos + "> " + fname + " " + msg);
    }
}
