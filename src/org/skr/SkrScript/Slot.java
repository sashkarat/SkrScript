package org.skr.SkrScript;

/**
 * Created by rat on 22.11.14.
 */
public class Slot {

    RegisterPool registers;
    Script script;
    boolean enabled = true;

    int getRegNum() {
        if ( registers == null )
            return 0;
        return registers.len();
    }

    public void setScript( Script script ) {
        this.script = script;
        registers = new RegisterPool( this.script.regNum );
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
