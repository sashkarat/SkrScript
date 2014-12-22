package org.skr.SkrScript;

/**
 * Created by rat on 22.11.14.
 */
public class Slot {

    ValuePool registers;
    Script script;
    boolean enabled = true;

    int getRegNum() {
        if ( registers == null )
            return 0;
        return registers.len();
    }

    public void setScript( Script script ) {
        this.script = script;
        registers = new ValuePool( this.script.numOfReg);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Script getScript() {
        return script;
    }
}
