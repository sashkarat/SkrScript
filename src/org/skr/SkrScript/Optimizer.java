package org.skr.SkrScript;

import java.nio.ByteBuffer;
import java.util.*;

/**
 * Created by rat on 01.01.15.
 */
public class Optimizer {

    public static class EntryPoints {
        int init;
        int run;

        public EntryPoints(int init, int run) {
            this.init = init;
            this.run = run;
        }
    }

    static class OptimizerContext {
        int initIdx;
        int runIdx;
        Script script;

        public OptimizerContext(Script script) {
            this.script = script;
        }
    }

    public static class OpCode {
        byte code;
        byte [] buffer;
        int pos;
        int refIdx = 0;
        int idx = 0;

        public OpCode(byte code, byte[] buffer, int pos) {
            this.code = code;
            this.buffer = buffer;
            this.pos = pos;

        }

        public boolean isJump() {
            return code== Def.JUMP || code == Def.JUMPF;
        }

        public boolean isFCall() {
            return code == Def.OP_F_CALL;
        }

        public boolean isJumping() {
            return isJump() || isFCall();
        }

        private static final ByteBuffer tmpBb = ByteBuffer.allocateDirect(4);

        protected int getInt() {
            tmpBb.put(0, buffer[0]);
            tmpBb.put(1, buffer[1]);
            tmpBb.put(2, buffer[2]);
            tmpBb.put(3, buffer[3] );
            return tmpBb.getInt(0);
        }

        protected byte getDts() {
            if ( buffer == null )
                return 0;
            return buffer[0];
        }

        static void setInt( int val, ArrayList<Byte> ba, int pos ) {
            tmpBb.putInt(0, val);
            ba.set(pos++, tmpBb.get(0));
            ba.set(pos++, tmpBb.get(1));
            ba.set(pos++, tmpBb.get(2));
            ba.set(pos, tmpBb.get(3));
        }

    }

    static ArrayList<OpCode> opCodes = new ArrayList<OpCode>();

    static byte [] cpyBuffer( byte [] bytes , int posFrom, int len ) {
        byte [] buffer = new byte[len];
        System.arraycopy(bytes, posFrom, buffer, 0, len);
        return buffer;
    }

    static byte [] cpySetValBuffer(byte [] bytes, int pos ) {
        byte dts = bytes[pos];
        int len = 0;
        switch ( dts ) {
            case Def.DTS_NULL:
                break;
            case Def.DTS_BOOL:
            case Def.DTS_TYPE:
                len = 1;
                break;
            case Def.DTS_INT:
            case Def.DTS_FLOAT:
            case Def.DTS_REG:
            case Def.DTS_VAR:
            case Def.DTS_STRING:
            case Def.DTS_PROP_CODE:
                len = 4;
                break;
            default:
                Engine.printError("Optimizer.cpySetValBuffer", "Undefined dts: " + dts, null);
                return null;
        }
        byte[] buffer = new byte[ len + 1 ];
        buffer[0] = dts;
        if ( len == 0 )
            return buffer;
        System.arraycopy(bytes, pos+1, buffer, 1, len );
        return buffer;
    }

    public static int getIndexForPos( int pos ) {
        if ( pos < 0 )
            return pos;
        for (OpCode opCode : opCodes)
            if (opCode.pos == pos)
                return opCode.idx;
        return -1;
    }

    static boolean decompile( OptimizerContext oc ) {
        opCodes.clear();
        int pos = -1;
        byte [] bytes = oc.script.bytes;

        while ( ++pos < bytes.length ) {
            byte opCode = bytes[ pos ];

            switch ( opCode ) {
                case Def.OP_F_CALL:
                    opCodes.add( new OpCode(opCode, cpyBuffer(bytes, pos+1, 5), pos) ); // address: 4 bytes; numOfArg: 1 byte.
                    pos+=5;
                    continue;
                case Def.SETLV:
                case Def.SETRV:
                    byte [] buffer = cpySetValBuffer(bytes, pos+1 );
                    if ( buffer == null ) {
                        Engine.printError("Optimizer.decompile", "cpySetValBuffer has returned null", null);
                        return false;
                    }
                    opCodes.add( new OpCode(opCode, buffer, pos) );
                    pos += buffer.length;
                    continue;
                case Def.DECVARNUM:
                case Def.JUMP:
                case Def.JUMPF:
                    opCodes.add( new OpCode(opCode, cpyBuffer(bytes, pos+1, 4), pos) );
                    pos+= 4;
                    continue;
                default:
                    opCodes.add( new OpCode(opCode, null, pos) );
            }
        }

        for (int i = 0; i < opCodes.size(); i++ ) {
            OpCode opCode = opCodes.get(i);
            opCode.idx = i;
            if ( opCode.pos == oc.script.initPoint )
                oc.initIdx = opCode.idx;
            if ( opCode.pos == oc.script.runPoint )
                oc.runIdx = opCode.idx;
//            Engine.printMsg("Optimizer.decompile", ";[" + Dumper.getOpCodeStr( opCode.code) + "] ", null);
        }

        for (OpCode opCode : opCodes) {
            if ( !opCode.isJumping() )
                continue;
            opCode.refIdx = getIndexForPos( opCode.getInt() );
        }


        return true;
    }

    static int getPosByIdx(int idx ) {
        if ( idx < 0 )
            return idx;
        int i = -1;
        while ( ++i <= idx ) {
            OpCode opCode = opCodes.get(i);
            if ( idx <= opCode.idx )
                return opCode.pos;
        }
        return -1;
    }


    static boolean assemble( OptimizerContext oc ) {
        ArrayList<Byte> buffer = new ArrayList<Byte>();
        for( OpCode opCode : opCodes ) {
            opCode.pos = buffer.size();
            buffer.add( opCode.code );
            if ( opCode.buffer != null ) {
                for(byte b : opCode.buffer )
                    buffer.add(b);
            }
        }
        for (OpCode opCode : opCodes) {
            if (!opCode.isJumping())
                continue;
            int oldJumpPos = opCode.getInt();
            int jumpPos = getPosByIdx(opCode.refIdx);

//            Engine.printMsg("assemble", " code: " + Dumper.getOpCodeStr( opCode.code) +
//            " oldJumpPos: " + oldJumpPos + " newJumpPos: " + jumpPos + " refIdx: " + opCode.refIdx, null );

            OpCode.setInt( jumpPos, buffer, opCode.pos+1 );
        }

        byte [] bytes = new byte[ buffer.size() ];
        for ( int i = 0; i < bytes.length; i++)
            bytes[i] = buffer.get(i);

        oc.script.bytes = bytes;
        oc.script.initPoint = getPosByIdx( oc.initIdx );
        oc.script.runPoint = getPosByIdx( oc.runIdx );
        return true;
    }


    public static boolean optimize(Script script) {

        OptimizerContext oc = new OptimizerContext(script);
        return optimize0(oc) && optimize1(oc) && optimize2(oc);

    }


    static boolean optimize0(OptimizerContext oc) {
        if ( !decompile( oc ) )
            return false;

        int i = -1;
        while(++i < opCodes.size() ) {
            OpCode opCode = opCodes.get( i );
            if ( i == 0)
                continue;
            OpCode pOpCode = opCodes.get( i - 1 );

            if ( pOpCode.code == Def.PUSHRV && opCode.code == Def.POPRV ) {
                opCodes.remove( i );
                opCodes.remove( i - 1);
                i-=2;
                continue;
            }

            if ( pOpCode.code == Def.PUSHLV && opCode.code == Def.POPLV ) {
                opCodes.remove( i );
                opCodes.remove( i - 1);
                i-=2;
                continue;
            }

            if ( pOpCode.code == Def.POPRV && opCode.code == Def.PUSHRV ) {
                opCodes.remove( i );
                opCodes.remove( i - 1);
                i-=2;
                continue;
            }

            if ( pOpCode.code == Def.POPLV && opCode.code == Def.PUSHLV ) {
                opCodes.remove( i );
                opCodes.remove( i - 1);
                i-=2;
                continue;
            }

            if ( pOpCode.code == Def.RVTOLV && opCode.code == Def.LVTORV ) {
                opCodes.remove( i );
                opCodes.remove( i - 1);
                i-=2;
                continue;
            }

            if ( pOpCode.code == Def.LVTORV && opCode.code == Def.RVTOLV ) {
                opCodes.remove( i );
                opCodes.remove( i - 1);
                i-=2;
                continue;
            }

            if ( pOpCode.code == Def.PUSHLV && opCode.code == Def.POPRV ) {
                pOpCode.code = Def.LVTORV;
                opCodes.remove( i );
                i--;
                continue;
            }
            if ( pOpCode.code == Def.PUSHRV && opCode.code == Def.POPLV ) {
                pOpCode.code = Def.RVTOLV;
                opCodes.remove( i );
                i--;
                continue;
            }

            if ( pOpCode.code == Def.RVTOLV && opCode.code == Def.PUSHLV ) {
                pOpCode.code = Def.PUSHRV;
                opCodes.remove( i );
                i--;
                continue;
            }

            if ( pOpCode.code == Def.LVTORV && opCode.code == Def.PUSHRV ) {
                pOpCode.code = Def.PUSHLV;
                opCodes.remove( i );
                i--;
                continue;
            }

            if ( pOpCode.code == Def.OBTAINLV && opCode.code != Def.PUSHLV ) {
                opCodes.remove( i - 1 );
                i--;
                continue;
            }

            if ( pOpCode.code == Def.OBTAINRV && opCode.code != Def.PUSHRV ) {
                opCodes.remove( i - 1 );
                i--;
                continue;
            }


        }

        return assemble( oc );
    }

    static boolean optimize1(OptimizerContext oc) {
        if ( !decompile( oc ) )
            return false;

        int i = -1;
        int pushPos = -1;
        while(++i < opCodes.size() ) {
            OpCode opCode = opCodes.get( i );
            if ( i == 0)
                continue;

            if ( opCode.code == Def.PUSHLV ) {
                pushPos = i;
                continue;
            }

            if ( opCode.code == Def.POPLV && pushPos >= 0 ) {
                opCodes.remove(i);
                opCodes.remove( pushPos );
                i = pushPos;
                pushPos = -1;
                continue;
            }

            if ( opCode.code != Def.SETRV && opCode.code != Def.OBTAINRV)
                pushPos = -1;

            OpCode pOpCode = opCodes.get( i - 1 );

            if ( pOpCode.code == Def.PUSHLV && opCode.code == Def.PUSHRV ) {
                pOpCode.code = Def.PUSHLVRV;
                opCodes.remove( i );
                i--;
                continue;
            }

            if ( pOpCode.code == Def.LVTORV && opCode.code == Def.POPLV ) {
                pOpCode.code = Def.LVTORVPOPLV;
                opCodes.remove( i );
                i--;
                continue;
            }

            if ( pOpCode.code == Def.SETLV && opCode.code == Def.OP_ASSIGN ) {
                byte dts = pOpCode.getDts();
                if ( dts != Def.DTS_VAR && dts != Def.DTS_REG )
                    continue;
                pOpCode.code = Def.RVTOVAR;
                opCodes.remove( i );
                i--;
                continue;
            }
        }

        return assemble( oc );
    }


    static boolean optimize2(OptimizerContext oc) {
        if ( !decompile( oc ) )
            return false;

        int i = -1;
        while(++i < opCodes.size() ) {
            OpCode opCode = opCodes.get( i );
            if ( i == 0)
                continue;
            OpCode pOpCode = opCodes.get( i - 1 );

            if ( pOpCode.code == Def.LVTORV && opCode.code == Def.RVTOVAR ) {
                opCode.code = Def.LVTOVAR;
                opCodes.remove( i - 1 );
                i--;
                continue;
            }

        }

        return assemble( oc );
    }
}
