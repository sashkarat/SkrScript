package org.skr.SkrScript;


/**
 * Created by rat on 29.11.14.
 */
public class BuildInFunctions {

    private static final RegisterPool args = new RegisterPool( 5 );

    static {

        FunctionPool.setAdapter( Def.F_MSG, new FunctionPool.Adapter() {
            @Override
            public boolean act(RegisterPool args, int numOfArgs, Value res, RunContext rc) {
                return msg( rc );
            }
        } );

        FunctionPool.setAdapter( Def.F_ERR, new FunctionPool.Adapter() {
            @Override
            public boolean act(RegisterPool args, int numOfArgs, Value res, RunContext rc) {
                return err(rc);
            }
        } );

        FunctionPool.setAdapter( Def.F_SET_SLOT_ENABLED, new FunctionPool.Adapter() {
            @Override
            public boolean act(RegisterPool args, int numOfArgs, Value res, RunContext rc) {
                return setSlotEnabled(rc);
            }
        } );

        FunctionPool.setAdapter( Def.F_TYPE_TO_STR, new FunctionPool.Adapter() {
            @Override
            public boolean act(RegisterPool args, int numOfArgs, Value res, RunContext rc) {
                return typeToStr(rc);
            }
        } );

        FunctionPool.setAdapter( Def.F_SIN, new FunctionPool.Adapter() {
            @Override
            public boolean act(RegisterPool args, int numOfArgs, Value res, RunContext rc) {
                return sin(rc);
            }
        } );

        FunctionPool.setAdapter( Def.F_COS, new FunctionPool.Adapter() {
            @Override
            public boolean act(RegisterPool args, int numOfArgs, Value res, RunContext rc) {
                return cos(rc);
            }
        } );

        FunctionPool.setAdapter( Def.F_TAN, new FunctionPool.Adapter() {
            @Override
            public boolean act(RegisterPool args, int numOfArgs, Value res, RunContext rc) {
                return tan(rc);
            }
        } );

        FunctionPool.setAdapter( Def.F_ACOS, new FunctionPool.Adapter() {
            @Override
            public boolean act(RegisterPool args, int numOfArgs, Value res, RunContext rc) {
                return acos(rc);
            }
        } );

        FunctionPool.setAdapter( Def.F_ASIN, new FunctionPool.Adapter() {
            @Override
            public boolean act(RegisterPool args, int numOfArgs, Value res, RunContext rc) {
                return asin(rc);
            }
        } );

        FunctionPool.setAdapter( Def.F_ATAN, new FunctionPool.Adapter() {
            @Override
            public boolean act(RegisterPool args, int numOfArgs, Value res, RunContext rc) {
                return atan(rc);
            }
        } );

        FunctionPool.setAdapter( Def.F_CBRT, new FunctionPool.Adapter() {
            @Override
            public boolean act(RegisterPool args, int numOfArgs, Value res, RunContext rc) {
                return cbrt(rc);
            }
        } );

        FunctionPool.setAdapter( Def.F_SQRT, new FunctionPool.Adapter() {
            @Override
            public boolean act(RegisterPool args, int numOfArgs, Value res, RunContext rc) {
                return sqrt(rc);
            }
        } );

        FunctionPool.setAdapter( Def.F_EXP, new FunctionPool.Adapter() {
            @Override
            public boolean act(RegisterPool args, int numOfArgs, Value res, RunContext rc) {
                return exp(rc);
            }
        } );

        FunctionPool.setAdapter( Def.F_HYPOT, new FunctionPool.Adapter() {
            @Override
            public boolean act(RegisterPool args, int numOfArgs, Value res, RunContext rc) {
                return hypot(rc);
            }
        } );

        FunctionPool.setAdapter( Def.F_POW, new FunctionPool.Adapter() {
            @Override
            public boolean act(RegisterPool args, int numOfArgs, Value res, RunContext rc) {
                return pow(rc);
            }
        } );

        FunctionPool.setAdapter( Def.F_LOG, new FunctionPool.Adapter() {
            @Override
            public boolean act(RegisterPool args, int numOfArgs, Value res, RunContext rc) {
                return log(rc);
            }
        } );

        FunctionPool.setAdapter( Def.F_LOG10, new FunctionPool.Adapter() {
            @Override
            public boolean act(RegisterPool args, int numOfArgs, Value res, RunContext rc) {
                return log10(rc);
            }
        } );

        FunctionPool.setAdapter( Def.F_MAX, new FunctionPool.Adapter() {
            @Override
            public boolean act(RegisterPool args, int numOfArgs, Value res, RunContext rc) {
                return max(rc);
            }
        } );

        FunctionPool.setAdapter( Def.F_MIN, new FunctionPool.Adapter() {
            @Override
            public boolean act(RegisterPool args, int numOfArgs, Value res, RunContext rc) {
                return min(rc);
            }
        } );

        FunctionPool.setAdapter( Def.F_RANDOM, new FunctionPool.Adapter() {
            @Override
            public boolean act(RegisterPool args, int numOfArgs, Value res, RunContext rc) {
                return random(rc);
            }
        } );

        FunctionPool.setAdapter( Def.F_TO_DEGREES, new FunctionPool.Adapter() {
            @Override
            public boolean act(RegisterPool args, int numOfArgs, Value res, RunContext rc) {
                return toDeg(rc);
            }
        } );

        FunctionPool.setAdapter( Def.F_TO_RADIANS, new FunctionPool.Adapter() {
            @Override
            public boolean act(RegisterPool args, int numOfArgs, Value res, RunContext rc) {
                return toRad(rc);
            }
        } );

        FunctionPool.setAdapter( Def.F_ABS, new FunctionPool.Adapter() {
            @Override
            public boolean act(RegisterPool args, int numOfArgs, Value res, RunContext rc) {
                return abs(rc);
            }
        } );

        FunctionPool.setAdapter( Def.F_FLOOR, new FunctionPool.Adapter() {
            @Override
            public boolean act(RegisterPool args, int numOfArgs, Value res, RunContext rc) {
                return floor(rc);
            }
        } );

        FunctionPool.setAdapter( Def.F_CEIL, new FunctionPool.Adapter() {
            @Override
            public boolean act(RegisterPool args, int numOfArgs, Value res, RunContext rc) {
                return ceil(rc);
            }
        } );

        FunctionPool.setAdapter( Def.F_ROUND, new FunctionPool.Adapter() {
            @Override
            public boolean act(RegisterPool args, int numOfArgs, Value res, RunContext rc) {
                return round(rc);
            }
        } );

        FunctionPool.setAdapter(Def.F_GET_DUMP_ENV_STR, new FunctionPool.Adapter() {
            @Override
            public boolean act(RegisterPool args, int numOfArgs, Value res, RunContext rc) {
                if ( numOfArgs != 0 )
                    return Engine.peArgNumError("scriptDumpEnv", rc );
                res.setAsString( "Script dumpEnv. vars.offset " + rc.vars.offset + "; varNum: " + rc.varNum +
                        "; retCode: " + rc.retCode );
                return true;
            }
        });
    }

    public static boolean call( int addr, RunContext rc, byte numOfArg ) {
//        printMsg("call", "addr: " + addr + " numOfArg: " + numOfArg, rc );
        for (int i = numOfArg - 1; i >= 0; i--)
            rc.popArg(args, i);
        FunctionPool.Adapter adapter = FunctionPool.getAdapter(addr);
        return adapter != null && adapter.act(args, numOfArg, rc.l, rc);
    }

    private static boolean round(RunContext rc) {
        if ( args.getDts(0) != Def.DTS_NUMBER )
            return Engine.printError("round", "arg is not a number", rc);
        rc.l.set(  Math.round((Float) args.getValue(0) ), Def.DTS_NUMBER  );
        return true;
    }

    private static boolean ceil(RunContext rc) {
        if ( args.getDts(0) != Def.DTS_NUMBER )
            return Engine.printError("ceil", "arg is not a number", rc);
        rc.l.set(  (float) Math.ceil((Float) args.getValue(0) ) , Def.DTS_NUMBER  );
        return true;
    }

    private static boolean floor(RunContext rc) {
        if ( args.getDts(0) != Def.DTS_NUMBER )
            return Engine.printError("floor", "arg is not a number", rc);
        rc.l.set(  (float) Math.floor((Float) args.getValue(0)) , Def.DTS_NUMBER  );
        return true;
    }

    private static boolean abs(RunContext rc) {
        if ( args.getDts(0) != Def.DTS_NUMBER )
            return Engine.printError("abs", "arg is not a number", rc);
        rc.l.set( Math.abs((Float) args.getValue(0) ) , Def.DTS_NUMBER  );
        return true;
    }

    private static boolean toRad(RunContext rc) {
        if ( args.getDts(0) != Def.DTS_NUMBER )
            return Engine.printError("toRad", "arg is not a number", rc);
        rc.l.set(  (float) Math.toRadians((Float) args.getValue(0)) , Def.DTS_NUMBER  );
        return true;
    }

    private static boolean toDeg(RunContext rc) {
        if ( args.getDts(0) != Def.DTS_NUMBER )
            return Engine.printError("toDeg", "arg is not a number", rc);
        rc.l.set(  (float) Math.toDegrees((Float) args.getValue(0)) , Def.DTS_NUMBER  );
        return true;
    }

    private static boolean random(RunContext rc) {
        rc.l.set(  (float) Math.random() , Def.DTS_NUMBER  );
        return true;
    }

    private static boolean min(RunContext rc) {
        if ( args.getDts(0) != Def.DTS_NUMBER )
            return Engine.printError("min", "arg0 is not a number", rc);
        if ( args.getDts(1) != Def.DTS_NUMBER )
            return Engine.printError("min", "arg1 is not a number", rc);
        rc.l.set( Math.min((Float) args.getValue(0), (Float) args.getValue(1) ) , Def.DTS_NUMBER );
        return true;
    }

    private static boolean max(RunContext rc) {
        if ( args.getDts(0) != Def.DTS_NUMBER )
            return Engine.printError("max", "arg0 is not a number", rc);
        if ( args.getDts(1) != Def.DTS_NUMBER )
            return Engine.printError("max", "arg1 is not a number", rc);
        rc.l.set( Math.max((Float) args.getValue(0), (Float) args.getValue(1)) , Def.DTS_NUMBER );
        return true;
    }

    private static boolean log10(RunContext rc) {
        if ( args.getDts(0) != Def.DTS_NUMBER )
            return Engine.printError("log10", "arg is not a number", rc);
        rc.l.set(  (float) Math.log10((Float) args.getValue(0)) , Def.DTS_NUMBER  );
        return true;
    }

    private static boolean log(RunContext rc) {
        if ( args.getDts(0) != Def.DTS_NUMBER )
            return Engine.printError("log", "arg is not a number", rc);
        rc.l.set(  (float) Math.log((Float) args.getValue(0)) , Def.DTS_NUMBER  );
        return true;
    }

    private static boolean pow(RunContext rc) {
        if ( args.getDts(0) != Def.DTS_NUMBER )
            return Engine.printError("pow", "arg0 is not a number", rc);
        if ( args.getDts(1) != Def.DTS_NUMBER )
            return Engine.printError("pow", "arg1 is not a number", rc);
        rc.l.set( (float) Math.pow((Float) args.getValue(0), (Float) args.getValue(1)) , Def.DTS_NUMBER );
        return true;

    }

    private static boolean hypot(RunContext rc) {
        if ( args.getDts(0) != Def.DTS_NUMBER )
            return Engine.printError("hypot", "arg0 is not a number", rc);
        if ( args.getDts(1) != Def.DTS_NUMBER )
            return Engine.printError("hypot", "arg1 is not a number", rc);
        rc.l.set( (float) Math.hypot((Float) args.getValue(0), (Float) args.getValue(1) ) , Def.DTS_NUMBER );
        return true;

    }

    private static boolean exp(RunContext rc) {
        if ( args.getDts(0) != Def.DTS_NUMBER )
            return Engine.printError("exp", "arg is not a number", rc);
        rc.l.set(  (float) Math.exp((Float) args.getValue(0)) , Def.DTS_NUMBER  );
        return true;
    }

    private static boolean sqrt(RunContext rc) {
        if ( args.getDts(0) != Def.DTS_NUMBER )
            return Engine.printError("sqrt", "arg is not a number", rc);
        rc.l.set(  (float) Math.sqrt((Float) args.getValue(0)) , Def.DTS_NUMBER  );
        return true;
    }

    private static boolean cbrt(RunContext rc) {
        if ( args.getDts(0) != Def.DTS_NUMBER )
            return Engine.printError("cbrt", "arg is not a number", rc);
        rc.l.set(  (float) Math.cbrt((Float) args.getValue(0)) , Def.DTS_NUMBER  );
        return true;
    }

    private static boolean atan(RunContext rc) {
        if ( args.getDts(0) != Def.DTS_NUMBER )
            return Engine.printError("atan", "arg is not a number", rc);
        rc.l.set(  (float) Math.atan((Float) args.getValue(0)) , Def.DTS_NUMBER  );
        return true;
    }

    private static boolean asin(RunContext rc) {
        if ( args.getDts(0) != Def.DTS_NUMBER )
            return Engine.printError("asin", "arg is not a number", rc);
        rc.l.set(  (float) Math.asin((Float) args.getValue(0)) , Def.DTS_NUMBER  );
        return true;
    }

    private static boolean acos(RunContext rc) {
        if ( args.getDts(0) != Def.DTS_NUMBER )
            return Engine.printError("acos", "arg is not a number", rc);
        rc.l.set(  (float) Math.acos((Float) args.getValue(0)) , Def.DTS_NUMBER  );
        return true;
    }

    private static boolean tan(RunContext rc) {
        if ( args.getDts(0) != Def.DTS_NUMBER )
            return Engine.printError("tan", "arg is not a number", rc);
        rc.l.set(  (float) Math.tan((Float) args.getValue(0)) , Def.DTS_NUMBER  );
        return true;

    }

    private static boolean cos(RunContext rc) {
        if ( args.getDts(0) != Def.DTS_NUMBER )
            return Engine.printError("cos", "arg is not a number", rc);
        rc.l.set(  (float) Math.cos((Float) args.getValue(0)) , Def.DTS_NUMBER  );
        return true;
    }

    private static boolean sin(RunContext rc) {
        if ( args.getDts(0) != Def.DTS_NUMBER )
            return Engine.printError("sin", "arg is not a number", rc);
        rc.l.set(  (float) Math.sin((Float) args.getValue(0)) , Def.DTS_NUMBER  );
        return true;
    }

    private static boolean typeToStr(RunContext rc ) {
        byte dts = (args.getDts(0) == Def.DTS_TYPE )? (Byte) args.getValue(0) : args.getDts(0);
        rc.l.setAsString( Engine.getDtsStr( dts, rc) );
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
        return Engine.printError(fname, "Argument type mistmatch. Expected: " + rightType + " but taken: " + thisType, rc);
    }

}
