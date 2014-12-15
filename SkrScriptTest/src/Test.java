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
        protected boolean opArithmetic(byte opCode, Value l, Value r, Value res, RunContext rc) {
            return false;
        }

        @Override
        protected String getDtsString(byte dts) {
            if ( dts == DTS_VECTOR2 )
                return "VECTOR2";
            return null;
        }

        @Override
        protected boolean typeCast(Value value, byte dstDts, RunContext rc) {
            return false;
        }

        @Override
        protected void setup() {

            setBuildInFunction(F_CRT_VEC, new FunctionPool.Adapter() {
                @Override
                public boolean act(RegisterPool args, int numOfArgs, Value res, RunContext rc) {
                    res.val = new Vector2( (Float) args.getValue(0), (Float) args.getValue(1));
                    res.dts = DTS_VECTOR2;
                    return true;
                }
            });

            setProperty(PROP_X, DTS_VECTOR2, new PropertyPool.Adapter() {
                @Override
                public boolean get(Value obj, Value res, RunContext rc) {
                    Float x = ((Vector2) obj.val).x;
                    res.setAsNumber( x );
                    return true;
                }

                @Override
                public boolean set(Value obj, Value value, RunContext rc) {
                    ((Vector2) obj.val).x = (Float) value.val;
                    return true;
                }
            });

            setProperty(PROP_Y, DTS_VECTOR2, new PropertyPool.Adapter() {
                @Override
                public boolean get(Value obj, Value res, RunContext rc) {
                    Float y = ((Vector2) obj.val).y;
                    res.setAsNumber( y );
                    return true;
                }

                @Override
                public boolean set(Value obj, Value value, RunContext rc) {
                    ((Vector2) obj.val).y = (Float) value.val;
                    return true;
                }
            });


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

        Builder.setVerboseLevel( 3 );

        if ( ! Builder.build(txt, script) )
            return;
        Slot slot = new Slot();
        slot.setScript(script);

        Dumper.dump(script);
//        ScriptDumper.dumpBytes( SkrScript, " ");

        Engine engine = new Engine();
        engine.setExtension( ee );


        System.out.println("Init point: " + slot.getScript().initPoint);

        engine.init(slot);

    }
}
