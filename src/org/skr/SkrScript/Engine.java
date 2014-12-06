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
    }

    public boolean init( Slot slot ) {
        return exec(slot.script.initPoint, slot);
    }

    public boolean exec( Slot slot ) {
        return exec( slot.script.runPoint, slot );
    }

    boolean exec( int startPoint, Slot slot ) {

        printMsg("exec. start from: "  + startPoint );

        rc.setSlot(slot);
        rc.reset();
        rc.setExtension( extension );

        if ( !RunContext.run(startPoint, rc) ) {
            printError("exec. failed", rc);
            return false;
        }

        printMsg("exec.done");
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
