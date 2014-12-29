package org.skr.SkrScript;

/**
 * Created by rat on 03.12.14.
 */
public class PropertyAccess {

    // lval must contains object. rval must contains prop code
    public static boolean getProperty( RunContext rc ) {
        rc.l.obtain( rc );
        rc.r.obtain( rc );
        if (!rc.r.isPropertyCode())
            return Engine.printError("getProperty", "rvalue is not a property", rc);
        PropertyPool.Adapter adapter = PropertyPool.getPropertyAdapter(rc.l.dts(), (Integer) rc.r.val());
        return adapter != null && adapter.get(rc.l, rc.l, rc);
    }

    public static boolean getProperty( Value obj, int propCode,
                                       Value result, RunContext rc ) {
        PropertyPool.Adapter adapter = PropertyPool.getPropertyAdapter(obj.dts(), propCode);
        return adapter != null && adapter.get(obj, result, rc);
    }

    // lval must contain RunContext.PropertyRef. rval must contains value (not a variable ).
    public static boolean setProperty( RunContext rc ) {
        if ( !rc.l.isPropertyRef() )
            return Engine.printError("setProperty", "lvalue is not a property reference", rc);
        RunContext.PropertyRef pr = (RunContext.PropertyRef) rc.l.val();
        PropertyPool.Adapter adapter = PropertyPool.getPropertyAdapter(pr.obj.dts(), pr.prop);
        return adapter != null && adapter.set(pr.obj, rc.r, rc);
    }

    public static boolean setProperty( RunContext.PropertyRef pr, Value v, RunContext rc ) {
        PropertyPool.Adapter adapter = PropertyPool.getPropertyAdapter(pr.obj.dts(), pr.prop);
        return adapter != null && adapter.set(pr.obj, v, rc);
    }

}
