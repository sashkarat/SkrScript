package org.skr.SkrScript;

/**
 * Created by rat on 22.11.14.
 */
public class Engine {

    RunContext rc = new RunContext( 8, 16);
    EngineExtension extension;

    public Engine() {
    }

    public void setExtension( EngineExtension extension ) {
        this.extension = extension;
        extension.setup();
    }

    public boolean init( Slot slot ) {
        return slot.script == null || exec(slot.script.initPoint, slot);
    }

    public boolean run(Slot slot) {
        return slot.script == null || exec(slot.script.runPoint, slot);
    }

    boolean exec( int startPoint, Slot slot ) {

//        printMsg("run. start from: "  + startPoint );

        if ( ! (slot.enabled && slot.script.enabled )  )
            return true;

        rc.setSlot(slot);
        rc.reset();
        rc.setExtension( extension );

        if ( !RunContext.run(startPoint, rc) ) {
            printError("SkrScriptEngine. script execution failed. Script will be disabled.", rc);
            slot.script.enabled = false;
            return false;
        }

//        printMsg("run.done");
        return true;
    }

    static void printError( String msg, RunContext rc ) {
        System.err.println( msg + " <pos: " + rc.opPos + "> ");
    }

    static void printMsg( String msg, RunContext rc ) {
        System.out.println( msg + " <pos: " + rc.opPos + "> ");
    }

    static void printMsg( String msg) {
        System.out.println( msg );
    }
}
