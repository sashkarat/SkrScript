package org.skr.SkrScript;


import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.bind.v2.util.TypeCast;

/**
 * Created by rat on 29.11.14.
 */
public class BuildInFunctions {

    private static final ValuePool args = new ValuePool( 5 );

    static {

        FunctionPool.setAdapter( Def.F_MSG, new FunctionPool.Adapter() {
            @Override
            public boolean act(ValuePool args, int numOfArgs, Value res, RunContext rc) {
                return msg( rc );
            }
        } );

        FunctionPool.setAdapter( Def.F_ERR, new FunctionPool.Adapter() {
            @Override
            public boolean act(ValuePool args, int numOfArgs, Value res, RunContext rc) {
                return err( rc );
            }
        } );

        FunctionPool.setAdapter( Def.F_SET_SLOT_ENABLED, new FunctionPool.Adapter() {
            @Override
            public boolean act(ValuePool args, int numOfArgs, Value res, RunContext rc) {
                return setSlotEnabled(rc);
            }
        } );

        FunctionPool.setAdapter( Def.F_TYPE_TO_STR, new FunctionPool.Adapter() {
            @Override
            public boolean act(ValuePool args, int numOfArgs, Value res, RunContext rc) {
                return typeToStr(rc);
            }
        } );

        FunctionPool.setAdapter( Def.F_SIN, new FunctionPool.Adapter() {
            @Override
            public boolean act(ValuePool args, int numOfArgs, Value res, RunContext rc) {
                return sin(rc);
            }
        } );

        FunctionPool.setAdapter( Def.F_COS, new FunctionPool.Adapter() {
            @Override
            public boolean act(ValuePool args, int numOfArgs, Value res, RunContext rc) {
                return cos(rc);
            }
        } );

        FunctionPool.setAdapter( Def.F_TAN, new FunctionPool.Adapter() {
            @Override
            public boolean act(ValuePool args, int numOfArgs, Value res, RunContext rc) {
                return tan(rc);
            }
        } );

        FunctionPool.setAdapter( Def.F_ACOS, new FunctionPool.Adapter() {
            @Override
            public boolean act(ValuePool args, int numOfArgs, Value res, RunContext rc) {
                return acos(rc);
            }
        } );

        FunctionPool.setAdapter( Def.F_ASIN, new FunctionPool.Adapter() {
            @Override
            public boolean act(ValuePool args, int numOfArgs, Value res, RunContext rc) {
                return asin(rc);
            }
        } );

        FunctionPool.setAdapter( Def.F_ATAN, new FunctionPool.Adapter() {
            @Override
            public boolean act(ValuePool args, int numOfArgs, Value res, RunContext rc) {
                return atan(rc);
            }
        } );

        FunctionPool.setAdapter( Def.F_CBRT, new FunctionPool.Adapter() {
            @Override
            public boolean act(ValuePool args, int numOfArgs, Value res, RunContext rc) {
                return cbrt(rc);
            }
        } );

        FunctionPool.setAdapter( Def.F_SQRT, new FunctionPool.Adapter() {
            @Override
            public boolean act(ValuePool args, int numOfArgs, Value res, RunContext rc) {
                return sqrt(rc);
            }
        } );

        FunctionPool.setAdapter( Def.F_EXP, new FunctionPool.Adapter() {
            @Override
            public boolean act(ValuePool args, int numOfArgs, Value res, RunContext rc) {
                return exp(rc);
            }
        } );

        FunctionPool.setAdapter( Def.F_HYPOT, new FunctionPool.Adapter() {
            @Override
            public boolean act(ValuePool args, int numOfArgs, Value res, RunContext rc) {
                return hypot(rc);
            }
        } );

        FunctionPool.setAdapter( Def.F_POW, new FunctionPool.Adapter() {
            @Override
            public boolean act(ValuePool args, int numOfArgs, Value res, RunContext rc) {
                return pow(rc);
            }
        } );

        FunctionPool.setAdapter( Def.F_LOG, new FunctionPool.Adapter() {
            @Override
            public boolean act(ValuePool args, int numOfArgs, Value res, RunContext rc) {
                return log(rc);
            }
        } );

        FunctionPool.setAdapter( Def.F_LOG10, new FunctionPool.Adapter() {
            @Override
            public boolean act(ValuePool args, int numOfArgs, Value res, RunContext rc) {
                return log10(rc);
            }
        } );

        FunctionPool.setAdapter( Def.F_MAX, new FunctionPool.Adapter() {
            @Override
            public boolean act(ValuePool args, int numOfArgs, Value res, RunContext rc) {
                return max(rc);
            }
        } );

        FunctionPool.setAdapter( Def.F_MIN, new FunctionPool.Adapter() {
            @Override
            public boolean act(ValuePool args, int numOfArgs, Value res, RunContext rc) {
                return min(rc);
            }
        } );

        FunctionPool.setAdapter( Def.F_RANDOM, new FunctionPool.Adapter() {
            @Override
            public boolean act(ValuePool args, int numOfArgs, Value res, RunContext rc) {
                return random(rc);
            }
        } );

        FunctionPool.setAdapter( Def.F_TO_DEGREES, new FunctionPool.Adapter() {
            @Override
            public boolean act(ValuePool args, int numOfArgs, Value res, RunContext rc) {
                return toDeg(rc);
            }
        } );

        FunctionPool.setAdapter( Def.F_TO_RADIANS, new FunctionPool.Adapter() {
            @Override
            public boolean act(ValuePool args, int numOfArgs, Value res, RunContext rc) {
                return toRad(rc);
            }
        } );

        FunctionPool.setAdapter( Def.F_ABS, new FunctionPool.Adapter() {
            @Override
            public boolean act(ValuePool args, int numOfArgs, Value res, RunContext rc) {
                return abs(rc);
            }
        } );

        FunctionPool.setAdapter( Def.F_FLOOR, new FunctionPool.Adapter() {
            @Override
            public boolean act(ValuePool args, int numOfArgs, Value res, RunContext rc) {
                return floor(rc);
            }
        } );

        FunctionPool.setAdapter( Def.F_CEIL, new FunctionPool.Adapter() {
            @Override
            public boolean act(ValuePool args, int numOfArgs, Value res, RunContext rc) {
                return ceil(rc);
            }
        } );

        FunctionPool.setAdapter( Def.F_ROUND, new FunctionPool.Adapter() {
            @Override
            public boolean act(ValuePool args, int numOfArgs, Value res, RunContext rc) {
                return round(rc);
            }
        } );

        FunctionPool.setAdapter(Def.F_GET_DUMP_ENV_STR, new FunctionPool.Adapter() {
            @Override
            public boolean act(ValuePool args, int numOfArgs, Value res, RunContext rc) {
                res.setAsString("Script dumpEnv. vars.offset " + rc.vars.offset + "; varNum: " + rc.varNum +
                        "; retCode: " + rc.retCode);
                return true;
            }
        });
    }

    public static boolean call( int addr, RunContext rc, byte numOfArg ) {
//        printMsg("call", "addr: " + addr + " numOfArg: " + numOfArg, rc );
        for (int i = numOfArg - 1; i >= 0; i--)
            rc.popArg(args, i);
        try {
            FunctionPool.Adapter adapter = FunctionPool.getAdapter(addr);
            return adapter != null && adapter.act(args, numOfArg, rc.l, rc);
        } catch ( Exception e ) {
            e.printStackTrace();
            StringBuilder sb = new StringBuilder("Arguments< " );
            for ( int i = 0; i < numOfArg; i++ ) {
                sb.append( args.get(i) );
                sb.append(" ");
            }
            sb.append(" > ");
            Engine.printError("Build-In function call", sb.toString() , rc );
            return false;
        }
    }

    private static boolean round(RunContext rc) {
        rc.l.setAsFloat((float) Math.round(args.get(0).asFloat(rc)));
        return true;
    }

    private static boolean ceil(RunContext rc) {
        rc.l.setAsFloat((float) Math.ceil(args.get(0).asFloat(rc)));
        return true;
    }

    private static boolean floor(RunContext rc) {
        rc.l.setAsFloat((float) Math.floor(args.get(0).asFloat(rc)));
        return true;
    }

    private static boolean abs(RunContext rc) {
        rc.l.setAsFloat(  Math.abs( args.get(0).asFloat(rc) ) );
        return true;
    }

    private static boolean toRad(RunContext rc) {
        rc.l.setAsFloat((float) Math.toRadians(args.get(0).asFloat(rc)));
        return true;
    }

    private static boolean toDeg(RunContext rc) {
        rc.l.setAsFloat((float) Math.toDegrees(args.get(0).asFloat(rc)));
        return true;
    }

    private static boolean random(RunContext rc) {
        rc.l.setAsFloat((float) Math.random());
        return true;
    }

    private static boolean min(RunContext rc) {
        rc.l.setAsFloat(Math.min(args.get(0).asFloat(rc), args.get(1).asFloat(rc)));
        return true;
    }

    private static boolean max(RunContext rc) {
        rc.l.setAsFloat(Math.max(args.get(0).asFloat(rc), args.get(1).asFloat(rc)));
        return true;
    }

    private static boolean log10(RunContext rc) {
        rc.l.setAsFloat((float) Math.log10(args.get(0).asFloat(rc)));
        return true;
    }

    private static boolean log(RunContext rc) {
        rc.l.setAsFloat((float) Math.log(args.get(0).asFloat(rc)));
        return true;
    }

    private static boolean pow(RunContext rc) {
        rc.l.setAsFloat( (float) Math.pow(args.get(0).asFloat(rc), args.get(1).asFloat(rc)));
        return true;

    }

    private static boolean hypot(RunContext rc) {
        rc.l.setAsFloat( (float) Math.hypot(args.get(0).asFloat(rc), args.get(1).asFloat(rc)));
        return true;

    }

    private static boolean exp(RunContext rc) {
        rc.l.setAsFloat((float) Math.exp(args.get(0).asFloat(rc)));
        return true;
    }

    private static boolean sqrt(RunContext rc) {
        rc.l.setAsFloat((float) Math.sqrt(args.get(0).asFloat(rc)));
        return true;
    }

    private static boolean cbrt(RunContext rc) {
        rc.l.setAsFloat((float) Math.cbrt(args.get(0).asFloat(rc)));
        return true;
    }

    private static boolean atan(RunContext rc) {
        rc.l.setAsFloat((float) Math.atan(args.get(0).asFloat(rc)));
        return true;
    }

    private static boolean asin(RunContext rc) {
        rc.l.setAsFloat((float) Math.asin(args.get(0).asFloat(rc)));
        return true;
    }

    private static boolean acos(RunContext rc) {
        rc.l.setAsFloat((float) Math.acos(args.get(0).asFloat(rc)));
        return true;
    }

    private static boolean tan(RunContext rc) {
        rc.l.setAsFloat((float) Math.tan(args.get(0).asFloat(rc)));
        return true;

    }

    private static boolean cos(RunContext rc) {
        rc.l.setAsFloat((float) Math.cos(args.get(0).asFloat(rc)));
        return true;
    }

    private static boolean sin(RunContext rc) {
        rc.l.setAsFloat((float) Math.sin(args.get(0).asFloat(rc)));
        return true;
    }

    private static boolean typeToStr(RunContext rc ) {
        byte dts = (args.get(0).isType() )? (Byte) args.get(0).val() : args.get(0).dts();
        rc.l.setAsString( Engine.getDtsStr( dts, rc) );
        return true;
    }

    private static boolean setSlotEnabled(RunContext rc) {
        rc.slot.setEnabled(args.get(0).asBool(rc));
        return true;
    }

    public static boolean msg( RunContext rc ) {
        if ( rc.isOutEnabled() )
            System.out.println("Script msg: " + args.get(0).asString());
        return true;
    }

    public static boolean err( RunContext rc ) {
        if ( rc.isErrEnabled() )
            System.err.println("Script error: " + args.get(0).asString());
        return true;
    }
}
