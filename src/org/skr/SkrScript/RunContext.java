package org.skr.SkrScript;

import java.nio.ByteBuffer;

/**
 * Created by rat on 22.11.14.
 */

public class RunContext {

    public static class PropertyRef {

        public final Value obj = new Value();
        public int prop;

        @Override
        public String toString() {
            return obj.toString() + " @ " + prop;
        }
    }

    EngineExtension extension = null;

    Stack<Integer> varOffsetStack;
    Stack<Integer> varNumStack;
    Stack<Integer> retCodeStack;

    Stack<Object> dataStack;
    Stack<Byte> dtsStack;

    Slot slot;
    Script script;
    byte [] bytes;

    int pos = 0;
    int opPos = 0;
    int varNum = 0;
    int retCode = -1;

    RegisterPool vars = new RegisterPool( 128 );
    RegisterPool regs;

    public final Value l = new Value();
    public final Value r = new Value();
    public final PropertyRef pr = new PropertyRef();

    public RunContext(int stackSize, int dataStackSize) {
        if ( stackSize > 0 ) {
            varNumStack = new Stack<Integer>( stackSize );
            varOffsetStack = new Stack<Integer>( stackSize );
            retCodeStack = new Stack<Integer>( stackSize );
        }
        if ( dataStackSize > 0 ) {
            dataStack = new Stack<Object>( dataStackSize );
            dtsStack = new Stack<Byte>( dataStackSize );
        }
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
        this.script = slot.script;
        this.bytes = script.bytes;
        this.regs = slot.registers;
    }

    public Slot getSlot() {
        return slot;
    }

    public void reset() {
        pos = 0;
        vars.offset =  0 ;
        varNum = 0;
        retCode = -1;
        varOffsetStack.reset();
        varNumStack.reset();
        retCodeStack.reset();
        dataStack.reset();
        dtsStack.reset();
        extension = null;
    }

    public void setExtension( EngineExtension extension ) {
        this.extension = extension;
    }

    protected boolean hasMoreBytes() {
        return ( pos < bytes.length );
    }

    protected byte nextByte() {
        return bytes[pos++];
    }

    private static final ByteBuffer tmpBb = ByteBuffer.allocateDirect(4);

    protected int readInt() {
        tmpBb.put(0, bytes[ pos++ ] );
        tmpBb.put(1, bytes[ pos++ ] );
        tmpBb.put(2, bytes[ pos++ ] );
        tmpBb.put(3, bytes[ pos++ ] );
        return tmpBb.getInt(0);
    }

    protected float readFloat() {
        tmpBb.put(0, bytes[ pos++ ] );
        tmpBb.put(1, bytes[ pos++ ] );
        tmpBb.put(2, bytes[ pos++ ] );
        tmpBb.put(3, bytes[ pos++ ] );
        return tmpBb.getFloat(0);
    }

    protected void pushEnv() {
        varOffsetStack.push( vars.offset );
        varNumStack.push( varNum );
        retCodeStack.push( retCode );
    }

    protected void popEnv() {
        vars.offset = varOffsetStack.pop();
        varNum = varNumStack.pop();
        retCode = retCodeStack.pop();
    }

    protected void pushLv() {
        dataStack.push( l.val );
        dtsStack.push( l.dts );
//        Engine.printMsg("pushLv", lval.toString(), this );
    }

    protected void pushRv() {
        dataStack.push( r.val );
        dtsStack.push( r.dts );
//        Engine.printMsg("pushRv", rval.toString(), this );
    }
    protected void popLv() {
        l.val = dataStack.pop();
        l.dts = dtsStack.pop();
//        Engine.printMsg("popLv", lval.toString(), this );
    }

    protected void popRv() {
        r.val = dataStack.pop();
        r.dts = dtsStack.pop();
//        Engine.printMsg("popRv", rval.toString(), this );
    }

    protected void popArg( RegisterPool args, int index ) {
        args.set( dataStack.pop(), dtsStack.pop(), index);

    }

    protected void popVal( Value v) {
        v.val = dataStack.pop();
        v.dts = dtsStack.pop();
//        Engine.printMsg("popRv", rval.toString(), this );
    }

    protected void fcall( int addr, byte argNumber ) {

        if ( addr >= bytes.length )
            return;

        pushEnv();

        retCode = this.pos;
        vars.offset += varNum;
        varNum = argNumber;
        pos = addr;

//        Engine.printMsg("fcall", "addr: " + pos + " retCode: " + retCode, this );
    }

    protected void ret( ) {
//        Engine.printMsg("ret.", "lval: " + lval.toString(), this);
        pos = retCode;
        popEnv();
    }

    protected void jumpF( int addr ) {
        if ( l.dts != Def.DTS_BOOL )
            return;
        if ( ! (Boolean) l.val )
            pos = addr;
    }

    protected void lvToRv() {
        r.set( l );
    }

    protected void rvToLv() {
        l.set( r );
    }

    protected Value obtainRv() {
        return r.obtain( this );
    }

    protected Value obtainLv() {
        return l.obtain( this );
    }

    protected boolean rvToLvVar() {
        return l.setVar( r, this );
    }

    protected void readRVal() {
        r.dts = nextByte();
        r.val = readValue( r.dts );
//        Engine.printMsg("readRVal", "rval: " + rval, this);
        if ( r.val == null )
            r.dts = Def.DTS_NULL;
    }

    protected void readLVal() {
        l.dts = nextByte();
        l.val = readValue( l.dts );
//        Engine.printMsg("readLVal", "lval: " + lval, this);
        if ( l.val == null )
            l.dts = Def.DTS_NULL;
    }

    protected Object readValue( byte dts ) {
//        Engine.printMsg("readValue", "dts: " + dts, this);
        switch ( dts ) {
            case Def.DTS_NULL:
                return null;
            case Def.DTS_REG:
                return readInt();
            case Def.DTS_VAR:
                return readInt();
            case Def.DTS_NUMBER:
                return readFloat();
            case Def.DTS_BOOL:
                return ( nextByte() != 0);
            case Def.DTS_STRING:
                return script.strings[ readInt() ];
            case Def.DTS_TYPE:
                return nextByte();
            case Def.DTS_PROP_CODE:
                return readInt();
            default:
                Engine.printError("readValue", "Undefined dts: " + dts, this);
                return null;
        }
    }



    protected static boolean run( int entryPoint, RunContext rc ) {

        rc.pos = entryPoint;

//        printMsg("run", "entryPoint: " + entryPoint, rc);


        while ( rc.hasMoreBytes() ) {
            rc.opPos = rc.pos;
            byte opCode = rc.nextByte();
//            printMsg("run", "opCode: " + Builder.getOpName( opCode ), rc);
            switch ( opCode ) {
                case Def.SETRV:
                    rc.readRVal();
                    continue;
                case Def.SETLV:
                    rc.readLVal();
                    continue;
                case Def.POPRV:
                    rc.popRv();
                    continue;
                case Def.PUSHRV:
                    rc.pushRv();
                    continue;
                case Def.POPLV:
                    rc.popLv();
                    continue;
                case Def.PUSHLV:
                    rc.pushLv();
                    continue;
                case Def.JUMP:
                    rc.pos = rc.readInt();
                    continue;
                case Def.JUMPF:
                    rc.jumpF( rc.readInt() );
                    continue;
                case Def.RET:
                    if ( rc.retCode < 0 ) {
//                        printMsg("run", "finalize", rc);
                        return true;
                    }
                    rc.ret( );
                    continue;
                case Def.INCVARNUM:
                    rc.varNum++;
                    continue;
                case Def.DECVARNUM:
                    int count = rc.readInt();
                    rc.varNum -= count;
                    continue;
                case Def.OBTAINLV:
                    rc.obtainLv();
                    continue;
                case Def.OBTAINRV:
                    rc.obtainRv();
                    continue;
                case Def.LVTORV:
                    rc.lvToRv();
                    continue;
                case Def.RVTOLV:
                    rc.rvToLv();
                    continue;
                case Def.OP_F_CALL:
                    int faddr = rc.readInt();
                    byte numOfArg = rc.nextByte();
                    if ( faddr <= Def.FUNCTIONS_START_ADDR ) {
                        if ( ! BuildInFunctions.call(faddr, rc, numOfArg) )
                            return false;
                        continue;
                    }
                    rc.fcall( faddr, numOfArg );
                    continue;
            }

            if ( Def.isOperator(opCode) ) {
//                Engine.printMsg("run", "op: " + ScriptDumper.getOpCodeStr( opCode ), rc);
                if ( ! Operators.execOp(opCode, rc) )
                    return false;
//                Engine.printMsg("run", "op: " + ScriptDumper.getOpCodeStr( opCode ) + " res: lval: " + rc.lval.toString(), rc);
                continue;
            }

            Engine.printError("run", "Undefined opCode: " + opCode, rc);
            break;
        }

        return Engine.printError("run", "Unexpected code finalization", rc);
    }

    public static boolean checkArgNumber(RunContext rc,  int numOfArgs, int noaMin, int noaMax ) {
        return !(numOfArgs < noaMin || numOfArgs > noaMax) || Engine.printError("Invalid arguments count", rc);
    }

    public static boolean checkArgTypes(RunContext rc,  RegisterPool args, byte ... dts ) {
        for ( int i = 0; i < dts.length; i++) {
            if (args.getDts(i) != dts[i])
                return Engine.printError("Argument " + i + " type mismatch", rc);
        }
        return true;
    }
}
