package org.skr.SkrScript;


/**
 * Created by rat on 23.11.14.
 */
public class Operators {

//    private static final Value rv = new Value();
//    private static final Value lv = new Value();

    public static boolean execOp(byte opCode, RunContext rc) {

        switch (opCode) {
            case Def.OP_NOT:
                return opNot( rc );
            case Def.OP_TYPEOF:
                return opTypeOf( rc );
            case Def.OP_U_PLUS:
            case Def.OP_U_MINUS:
                return unaryOpArithmetic( opCode, rc );
            case Def.OP_DIV:
            case Def.OP_MUL:
            case Def.OP_MOD:
            case Def.OP_ADD:
            case Def.OP_SUB:
            case Def.OP_LESS:
            case Def.OP_GRT:
            case Def.OP_LOEQ:
            case Def.OP_GOEQ:
                return opArithmetic( opCode, rc );
            case Def.OP_NOT_EQ:
                return opNotEqual( rc );
            case Def.OP_EQ:
                return opEqual( rc );
            case Def.OP_AND:
            case Def.OP_OR:
                return opAndOr( opCode, rc );
            case Def.OP_ASSIGN:
                return opAssign( rc );
            case Def.OP_GET_PROP:
                return PropertyAccess.getProperty(rc);
            case Def.OP_GET_PROP_REF:
                return opGetPropRef( rc );
        }
        Engine.printError("execOp", "Unknown op. opCode: " + opCode, rc);
        return false;
    }

    private static boolean opGetPropRef(RunContext rc) {
        rc.obtainLv();
        rc.obtainRv();
        if ( ! rc.r.isPropertyCode() ) {
            return Engine.printError("opGetPropRef", "rvalue is not a property", rc);
        }
        rc.pr.obj.set( rc.l );
        rc.pr.prop = rc.r.asPropertyCode();
        rc.l.set( rc.pr, Def.DTS_PROP_REF);
        return true;
    }

    public static boolean opNot( RunContext rc ) {
        rc.obtainRv();
        rc.l.setAsBool( ! rc.r.asBool( rc ) );
        return true;
    }

    public static boolean opTypeOf( RunContext rc ) {
        rc.obtainRv();
        rc.l.set( rc.r.dts(), Def.DTS_TYPE );
        return true;
    }

    protected static boolean unaryOpArithmetic( byte opCode, RunContext rc ) {
        rc.obtainRv();
        if (opCode == Def.OP_U_MINUS) {
            if (rc.r.isFloat()) {
                rc.l.setAsFloat(-rc.r.asFloat(rc));
                return true;
            } else if (rc.r.isInt()) {
                rc.l.setAsInt(-rc.r.asInt(rc));
                return true;
            }
        }
        if (opCode == Def.OP_U_PLUS) {
            if (rc.r.isFloat()) {
                rc.l.setAsFloat( rc.r.asFloat(rc));
                return true;
            } else if (rc.r.isInt()) {
                rc.l.setAsInt( rc.r.asInt(rc));
                return true;
            }
        }
        return rc.extension != null && rc.extension.unaryOpArithmetic(opCode, rc.r, rc.l, rc);
    }

    public static boolean opArithmetic( byte opCode, RunContext rc ) {


        rc.obtainLv();
        rc.obtainRv();

        if ( rc.l.isNumber() )
            return opNumberArithmetic(opCode, rc);
        if ( rc.l.isString() && opCode == Def.OP_ADD ) {
            rc.l.setAsString( rc.l.asString() + rc.r.asString() );
            return true;
        }

        if ( rc.extension != null )
            return rc.extension.opArithmetic( opCode, rc.l, rc.r, rc.l, rc );
        return Engine.printError("opArithmetic", "opCode: " + opCode + ". illegal lvalue type. dts: " + rc.l.dts(), rc);
    }

    protected static boolean opNumberArithmetic(byte opCode,  RunContext rc ) {
        if ( rc.l.isFloat() || rc.r.isFloat()) {
            switch (opCode) {
                case Def.OP_ADD:
                    rc.l.setAsFloat( rc.l.asFloat(rc) + rc.r.asFloat(rc) );
                    return true;
                case Def.OP_SUB:
                    rc.l.setAsFloat( rc.l.asFloat(rc) - rc.r.asFloat(rc)  );
                    return true;
                case Def.OP_MUL:
                    rc.l.setAsFloat( rc.l.asFloat(rc) * rc.r.asFloat(rc)  );
                    return true;
                case Def.OP_DIV:
                    rc.l.setAsFloat( rc.l.asFloat(rc) / rc.r.asFloat(rc) );
                case Def.OP_MOD:
                    rc.l.setAsFloat( rc.l.asFloat(rc) % rc.r.asFloat(rc)  );
                    return true;
                case Def.OP_LESS:
                    rc.l.setAsBool( rc.l.asFloat(rc) < rc.r.asFloat(rc) );
                    return true;
                case Def.OP_LOEQ:
                    rc.l.setAsBool( rc.l.asFloat(rc) <= rc.r.asFloat(rc)  );
                    return true;
                case Def.OP_GRT:
                    rc.l.setAsBool( rc.l.asFloat(rc) > rc.r.asFloat(rc) );
                    return true;
                case Def.OP_GOEQ:
                    rc.l.setAsBool( rc.l.asFloat(rc) >= rc.r.asFloat(rc)  );
                    return true;
            }
        } else if ( rc.l.isInt() ) {
            switch (opCode) {
                case Def.OP_ADD:
                    rc.l.setAsInt( rc.l.asInt( rc ) + rc.r.asInt( rc ) );
                    return true;
                case Def.OP_SUB:
                    rc.l.setAsInt( rc.l.asInt( rc ) - rc.r.asInt( rc ) );
                    return true;
                case Def.OP_MUL:
                    rc.l.setAsInt( rc.l.asInt( rc ) * rc.r.asInt( rc ) );
                    return true;
                case Def.OP_DIV:
                    rc.l.setAsInt( rc.l.asInt( rc ) / rc.r.asInt( rc ) );
                    return true;
                case Def.OP_MOD:
                    rc.l.setAsInt( rc.l.asInt( rc ) % rc.r.asInt( rc ) );
                    return true;
                case Def.OP_LESS:
                    rc.l.setAsBool( rc.l.asInt( rc ) < rc.r.asInt( rc )  );
                    return true;
                case Def.OP_LOEQ:
                    rc.l.setAsBool( rc.l.asInt( rc ) <= rc.r.asInt( rc )  );
                    return true;
                case Def.OP_GRT:
                    rc.l.setAsBool( rc.l.asInt( rc ) > rc.r.asInt( rc )  );
                    return true;
                case Def.OP_GOEQ:
                    rc.l.setAsBool( rc.l.asInt( rc ) >= rc.r.asInt( rc )  );
                    return true;
            }
        }

        return Engine.printError("opNumberArithmetic", "Unexpected opCode. opCode: " + opCode, rc);
    }

    public static boolean opEqual( RunContext rc ) {
        rc.obtainRv();
        rc.obtainLv();

        if ( rc.l.isBool() || rc.r.isBool() ) {
            rc.l.setAsBool( rc.l.asBool( rc ) == rc.r.asBool( rc ) );
            return true;
        }
        if ( rc.l.isNull() )
            return rc.r.isNull();

        rc.l.setAsBool(rc.l.val().equals(rc.r.val()));
        return true;
    }

    public static boolean opNotEqual( RunContext rc ) {
        rc.obtainRv();
        rc.obtainLv();

        if ( rc.l.isBool() || rc.r.isBool() ) {
            rc.l.setAsBool( rc.l.asBool(rc) != rc.r.asBool(rc) );
            return true;
        }
        if ( rc.l.isNull() )
            return !rc.r.isNull();

        rc.l.setAsBool( ! rc.l.val().equals( rc.r.val() ) );
        return true;
    }

    public static boolean opAndOr(byte opCode, RunContext rc ) {
        rc.obtainLv();
        rc.obtainRv();

        Boolean a = rc.l.asBool(rc);
        Boolean b = rc.r.asBool(rc);

        if ( opCode == Def.OP_AND )
            rc.l.setAsBool(a && b);
        else if ( opCode == Def.OP_OR )
            rc.l.setAsBool(a || b);
        return true;
    }

    protected static boolean opAssign( RunContext rc ) {
//        printMsg("opAssign", "l: " + rc.l + " r: " + rc.r, rc);
        return ( rc.obtainRv() != null ) && rc.rvToLvVar();
    }
}