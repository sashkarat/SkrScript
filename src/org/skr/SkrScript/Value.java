package org.skr.SkrScript;

/**
 * Created by rat on 06.12.14.
 */
public class Value {
    public Object val = null;
    public byte dts = Def.DTS_NULL;

    public void set ( Value v ) {
        val = v.val;
        dts = v.dts;
    }

    public boolean setVar( Value v, RunContext rc ){
        if ( dts == Def.DTS_REG  )
            rc.regs.set( v, (Integer) val );
        else if ( dts == Def.DTS_VAR )
            rc.vars.set( v, (Integer) val );
        else if ( dts == Def.DTS_PROP_REF ) {
            return PropertyAccess.setProperty((RunContext.PropertyRef) val, v, rc );
        } else {
            val = v.val;
            dts = v.dts;
        }
        return true;
    }

    public boolean isBool() {
        return dts == Def.DTS_BOOL;
    }

    public boolean isNumber() {
        return dts == Def.DTS_NUMBER;
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

    public boolean isDts( byte dts ) {
        return this.dts == dts;
    }

    public Value obtain( RunContext rc ) {
        if ( dts == Def.DTS_VAR ) {
            Integer idx = (Integer) val;
            rc.vars.get(this, idx);
        } else if ( dts == Def.DTS_REG ) {
            Integer idx = (Integer) val;
            rc.regs.get( this, idx);
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

    public void setAsNumber(Float v ) {
        val = v;
        dts = Def.DTS_NUMBER;
    }

    public void setAsString(String s ) {
        val = s;
        dts = Def.DTS_STRING;
    }

    public void setAsBool(Boolean b ) {
        val = b;
        dts = Def.DTS_BOOL;
    }

    public void set(Object v, byte dts ) {
        val = v;
        this.dts = dts;
    }

    @Override
    public String toString() {
        return "" + val + "(" + dts + ")";
    }
}
