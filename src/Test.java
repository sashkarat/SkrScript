import org.skr.SkrScript.*;

import java.io.*;

/**
 * Created by rat on 22.11.14.
 */
public class Test {

    public static class Vector2 {
        float x, y;

        public Vector2(float x, float y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "[" + x + ":" + y + "]";
        }
    }

    public static class EngineTestExtension extends EngineExtension {

        static final byte DTS_VECTOR2 = Def.EXTENDED_DTS_CODE - 1;

        static final int PROP_X = 0;
        static final int PROP_Y = 1;

        static final int F_CRT_VEC = Def.EXTENDED_BUILD_IN_FUNCTION_ADDRESS;

        public void init() {
            Builder.addExtendedDTS(DTS_VECTOR2, "VECTOR2");
            Builder.addExtendedProperty(PROP_X, "_x");
            Builder.addExtendedProperty( PROP_Y, "_y");

            Builder.addExtendedBuildInFunction("vec2", (byte)2, true, F_CRT_VEC );
        }

        @Override
        protected boolean opArithmetic(byte opCode, RunContext rc) {
            return false;
        }

        @Override
        protected boolean getProperty(Value obj, int propCode, Value result, RunContext rc) {

            if ( obj.dts != DTS_VECTOR2 )
                return false;
            Vector2 v = (Vector2) obj.val;
            if ( propCode == PROP_X ) {
                result.setAsNumber( v.x );
                return true;
            }

            if ( propCode == PROP_Y ) {
                result.setAsNumber( v.y );
                return true;
            }
            return false;
        }

        @Override
        protected boolean setProperty(RunContext.PropertyRef pr, Value v, RunContext rc) {
            if ( pr.obj.dts != DTS_VECTOR2 )
                return false;
            if ( !v.isNumber() )
                return false;
            Vector2 vec = (Vector2) pr.obj.val;

            if ( pr.prop == PROP_X ) {
                vec.x = (Float) v.val;
                return true;
            }

            if ( pr.prop == PROP_Y ) {
                vec.y = (Float) v.val;
                return true;
            }
            return false;
        }

        @Override
        protected String getDtsString(byte dts) {
            if ( dts == DTS_VECTOR2 )
                return "VECTOR2";
            return null;
        }

        @Override
        protected boolean buildInFunc(int address, RegisterPool args, int numOfArgs, Value result, RunContext rc) {

            if ( address != F_CRT_VEC )
                return false;

            result.val = new Vector2( (Float) args.getValue(0), (Float) args.getValue(1));
            result.dts = DTS_VECTOR2;

            return true;
        }
    }



    public static void main( String [] arg ) {
        System.out.println(new File(".").getAbsoluteFile());
        File file = new File("data/testScript.policy");
        BufferedReader br;
        try {
            br = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(file), "UTF-8"
                    )
            );
        } catch ( FileNotFoundException e ) {
            e.printStackTrace();
            return;
        } catch ( UnsupportedEncodingException e ) {
            e.printStackTrace();
            return;
        }
        String s;
        String txt = "";
        try {
            while ((s = br.readLine()) != null) {
                txt += s;
                txt += "\n";
            }
        } catch ( IOException e ) {
            e.printStackTrace();
        }

        EngineTestExtension ee = new EngineTestExtension();
        ee.init();

        Script script = new Script();
        if ( ! Builder.build(txt, script) )
            return;
        Slot slot = new Slot();
        slot.setScript(script);

        Dumper.dump(script);
//        ScriptDumper.dumpBytes( SkrScript, " ");

        Engine engine = new Engine();
        engine.setExtension( ee );

        engine.init(slot);

    }
}
