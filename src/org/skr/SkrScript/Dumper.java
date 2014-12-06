package org.skr.SkrScript;

/**
 * Created by rat on 28.11.14.
 */
public class Dumper {

    private static final RunContext rc = new RunContext( 0, 0 );

    public static void dumpBytes( Script script, String delim ) {
        dumpBytes( script.bytes, delim );
    }

    public static void dumpBytes( byte [] bytes, String delim ) {
        for( byte b : bytes ) {
            System.out.print( b );
            System.out.print( delim );
        }
        System.out.println();
    }

    public static void dump( Script script ) {

        rc.script = script;
        rc.bytes = script.bytes;
        rc.pos = 0;

        while ( rc.hasMoreBytes() ) {
            byte opCode = rc.nextByte();
            int idx = rc.pos - 1;
            out("(" + idx + ")");

            if ( idx < 10 )
                out("    ");
            else if ( idx < 100 )
                out("   ");
            else if ( idx < 1000 )
                out("  ");
            else
                out(" ");

            out(getOpCodeStr(opCode));

            switch ( opCode ) {
                case Def.OP_F_CALL:
                    out(" [" + rc.readInt() + "] ");
                    out(" AN<" + rc.nextByte() + ">");
                    break;
                case Def.SETRV:
                    dumpValue( rc );
                    break;
                case Def.SETLV:
                    dumpValue(rc);
                    break;
                case Def.JUMP:
                    out(" [" + rc.readInt() + "]");
                    break;
                case Def.JUMPF:
                    out(" [" + rc.readInt() + "]");
                    break;
            }

            out(";");
            ln();

        }
    }

    static String getOpCodeStr(byte opCode) {
        switch ( opCode ) {
            case Def.EOE:
                return "EOE";
            case Def.OP_NOT:
                return "OP !";
            case Def.OP_U_ADD:
                return "OP u +";
            case Def.OP_U_SUB:
                return "OP u -";
            case Def.OP_TYPEOF:
                return "OP typeof";
            case Def.OP_F_CALL:
                return "FCALL";
            case Def.OP_DIV:
                return "OP /";
            case Def.OP_MUL:
                return "OP *";
            case Def.OP_ADD:
                return "OP +";
            case Def.OP_SUB:
                return "OP -";
            case Def.OP_LESS:
                return "OP <";
            case Def.OP_GRT:
                return "OP >";
            case Def.OP_LOEQ:
                return "OP <=";
            case Def.OP_GOEQ:
                return "OP >=";
            case Def.OP_NOT_EQ:
                return "OP !=";
            case Def.OP_EQ:
                return "OP ==";
            case Def.OP_AND:
                return "OP &";
            case Def.OP_OR:
                return "OP |";
           case Def.OP_ASSIGN:
                return "OP =";
            case Def.OP_COMMA:
                return "OP ,";
            case Def.OP_GET_PROP:
                return "OP .";
            case Def.OP_GET_PROP_REF:
                return "OP .\'";
            case Def.SETRV:
                return "SETRV";
            case Def.SETLV:
                return "SETLV";
            case Def.POPRV:
                return "POPRV";
            case Def.POPLV:
                return "POPLV";
            case Def.PUSHLV:
                return "PUSHLV";
            case Def.PUSHRV:
                return "PUSHRV";
            case Def.JUMP:
                return "JUMP";
            case Def.JUMPF:
                return "JUMPF";
            case Def.RET:
                return "RET";
            case Def.INCVARNUM:
                return "INCVARNUM";
            case Def.OBTAINLV:
                return "OBTAINLV";
            case Def.OBTAINRV:
                return "OBTAINRV";
            case Def.LVTORV:
                return "LVTORV";
            case Def.RVTOLV:
                return "RVTOLV";
            default:
                return "<" + opCode + ">";
        }
    }

    static String getPropStr( int prop ) {
        for ( String propName: Builder.properties.keySet() ) {
            if ( prop == Builder.properties.get( propName) )
                return propName;
        }
        return "UNKNOWN PROPERTY";
    }

    static void dumpDTS( byte dts) {
        out(" <"+Builder.getDtsString(dts)+">   ");
    }

    static void dumpValue(RunContext rc ) {
        byte dts = rc.nextByte();

        dumpDTS( dts );

        switch ( dts ) {
            case Def.DTS_BOOL:
                byte v = rc.nextByte();
                out("" + v);
                break;
            case Def.DTS_NULL:
                break;
            case Def.DTS_NUMBER:
                float f = rc.readFloat();
                out("" + f);
                break;
            case Def.DTS_REG:
                int ri = rc.readInt();
                out("" + ri);
                break;
            case Def.DTS_VAR:
                int vi = rc.readInt();
                out("" + vi);
                break;
            case Def.DTS_STRING:
                int si = rc.readInt();
                out("\"" + rc.script.strings[ si ] + "\"");
                break;
            case Def.DTS_TYPE:
                byte typeDTS = rc.nextByte();
                dumpDTS( typeDTS );
                break;
            case Def.DTS_PROP_CODE:
                int prop = rc.readInt();
                out( getPropStr( prop ) );
                break;
            default:
                out(" <ERROR DTS>");
        }
    }

    static void out(String text ) {
        System.out.print( text );
    }

    static void ln() {
        System.out.println();
    }
}
