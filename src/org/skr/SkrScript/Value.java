package org.skr.SkrScript;

import java.util.Objects;

/**
 * Created by rat on 06.12.14.
 */
public class Value {
    private Object val = null;
    private byte dts = Def.DTS_NULL;

    public void set ( Value v ) {
        val = v.val;
        dts = v.dts;
    }

    public Object val() {
        return val;
    }

    public byte dts() {
        return dts;
    }

    public boolean setVar( Value v, RunContext rc ){
        if ( dts == Def.DTS_REG  )
            rc.regs.set( v, (Integer) val );
        else if ( dts == Def.DTS_VAR )
            rc.vars.set( v, (Integer) val );
        else if ( dts == Def.DTS_PROP_REF ) {
            return PropertyAccess.setProperty((RunContext.PropertyRef) val, v, rc );
        } else {
            set(v);
        }
        return true;
    }

    public boolean isBool() {
        return dts == Def.DTS_BOOL;
    }

    public boolean isNumber() {
        return dts == Def.DTS_FLOAT || dts == Def.DTS_INT;
    }

    public boolean isFloat() {
        return dts == Def.DTS_FLOAT;
    }

    public boolean isInt() {
        return dts == Def.DTS_INT;
    }

    public boolean isString() {
        return dts == Def.DTS_STRING;
    }

    public boolean isNull() {
        return val == null;
    }

    public boolean isVariable() {
        return dts == Def.DTS_REG || dts == Def.DTS_VAR;
    }

    public boolean isType() {
        return dts == Def.DTS_TYPE;
    }

    public boolean isPropertyCode() {
        return dts == Def.DTS_PROP_CODE;
    }

    public boolean isPropertyRef() {
        return dts == Def.DTS_PROP_REF;
    }

    public boolean dtsEquals(byte dts) {
        return this.dts == dts;
    }

    public Value obtain( RunContext rc ) {
        if ( dts == Def.DTS_VAR ) {
            int idx = (Integer) val;
            set(rc.vars.get(idx));
        } else if ( dts == Def.DTS_REG ) {
            int idx = (Integer) val;
            set(rc.regs.get(idx));
        } else if ( dts == Def.DTS_PROP_REF ) {
            RunContext.PropertyRef pr = (RunContext.PropertyRef) val;
            if ( !PropertyAccess.getProperty( pr.obj, pr.prop, this, rc ) )
                return null;
        }
        return this;
    }

    public void setAsNull() {
        val = null;
        dts = Def.DTS_NULL;
    }

    public void setAsInt(Integer i ) {
        val = i;
        dts = Def.DTS_INT;
    }

    public void setAsFloat(Float f ) {
        val = f;
        dts = Def.DTS_FLOAT;
    }

    public void setAsString(String s ) {
        val = s;
        dts = Def.DTS_STRING;
    }

    public void setAsBool(Boolean b ) {
        val = b;
        dts = Def.DTS_BOOL;
    }

    public void setAsPropCode(Integer code ) {
        dts = Def.DTS_PROP_CODE;
        val = code;
    }

    public void setAsReg( Integer index ) {
        dts = Def.DTS_REG;
        val = index;
    }

    public void setAsVar( Integer index ) {
        dts = Def.DTS_VAR;
        val = index;
    }

    public void set(Object v, byte dts ) {
        this.dts = dts;
        this.val = v;
    }

    public Boolean asBool( RunContext rc ) {
        if ( dts == Def.DTS_BOOL )
            return (Boolean) val;
        if ( dts == Def.DTS_INT )
            return (Integer)val != 0;
        if ( dts == Def.DTS_FLOAT )
            return (Float)val != 0.0f;
        if ( val == null )
            return false;
        if ( rc.extension != null )
            return (Boolean) rc.extension.cast( this, Def.DTS_BOOL, rc );
        return true;
    }

    public Float asFloat( RunContext rc) {
        if ( dts == Def.DTS_FLOAT )
            return (Float) val ;
        if ( dts == Def.DTS_INT )
            return  ((Integer) val).floatValue();
        if ( val == null || rc.extension == null)
            return null;
        return (Float) rc.extension.cast( this, Def.DTS_FLOAT, rc );
    }

    public Integer asInt( RunContext rc ) {
        if ( dts == Def.DTS_INT )
            return (Integer) val;
        if ( dts == Def.DTS_FLOAT )
            return ((Float) val).intValue();
        if ( val == null || rc.extension == null)
            return null;
        return (Integer) rc.extension.cast( this, Def.DTS_INT, rc );
    }

    public Object as(byte dts, RunContext rc ) {
        switch ( dts ) {
            case Def.DTS_INT:
                return asInt( rc );
            case Def.DTS_FLOAT:
                return asFloat( rc );
            case Def.DTS_BOOL:
                return asBool( rc );
            case Def.DTS_STRING:
                return asString();
            case Def.DTS_TYPE:
                return asType();
            case Def.DTS_PROP_CODE:
                return asPropertyCode();
            default:
                if ( val == null || rc.extension == null )
                    return null;
                return rc.extension.cast( this, dts, rc);
        }
    }

    public String asString() {
        if ( val == null )
            return "null";
        return val.toString();
    }

    public Byte asType() {
        if ( dts == Def.DTS_TYPE )
            return (Byte)val;
        return null;
    }

    public Integer asPropertyCode() {
        if ( dts == Def.DTS_PROP_CODE)
            return (Integer) val;
        return -1;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if ( dts == Def.DTS_STRING )
            sb.append("\"");
        sb.append( val );
        if ( dts == Def.DTS_STRING )
            sb.append("\"");
        sb.append("(");
        sb.append(Engine.getDtsStr( dts, null ));
        sb.append(")");
        return sb.toString();
    }

    public Value cpy() {
        Value v = new Value();
        v.set( this );
        return v;
    }
}
