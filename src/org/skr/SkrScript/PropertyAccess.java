package org.skr.SkrScript;

/**
 * Created by rat on 03.12.14.
 */
public class PropertyAccess {

    // lval must contains object. rval must contains prop code
    public static boolean getProperty( RunContext rc ) {
        rc.obtainLv();
        rc.obtainRv();
        if ( !rc.r.isPropertyCode() )
            return printError("getProperty", "rvalue is not a property", rc);
        return getProperty( rc.l, (Integer)rc.r.val, rc.l, rc );
    }

    public static boolean getProperty( Value obj, int propCode,
                                       Value result, RunContext rc ) {
        if ( rc.extension != null )
            return rc.extension.getProperty( obj, propCode, result, rc );
        return printError("getProperty", "lvalue has no properties", rc);
    }

    // lval must contain RunContext.PropertyRef. rval must contains value (not a variable ).
    public static boolean setProperty( RunContext rc ) {
        if ( !rc.l.isPropertyRef() )
            return printError("setProperty", "lvalue is not property reference", rc);
        RunContext.PropertyRef pr = (RunContext.PropertyRef) rc.l.val;
        return setProperty( pr, rc.r, rc );
    }


    public static boolean setProperty( RunContext.PropertyRef pr, Value v, RunContext rc ) {
        if ( rc.extension != null )
            return rc.extension.setProperty( pr, v, rc );
        return printError("setProperty", "value has no properties. DTS: " + pr.obj.dts, rc);
    }

    protected static boolean printError( String fname, String msg, RunContext rc ) {
        System.err.println("ERROR. PropertyAccess"+ "<" + rc.opPos + "> " + fname + " " + msg);
        return false;
    }

    protected static void printMsg( String fname, String msg, RunContext rc ) {
        System.out.println("PropertyAccess"+ "<" + rc.opPos + "> " + fname + " " + msg);
    }

}
