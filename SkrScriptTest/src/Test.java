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

        static final int F_CRT_VEC = Def.EXTENDED_BUILD_IN_FUNCTION_ADDRESS - 1;

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
        protected boolean unaryOpArithmetic(byte opCode, Value r, Value res, RunContext rc) {
            return false;
        }

        @Override
        protected String getDtsString(byte dts) {
            if ( dts == DTS_VECTOR2 )
                return "VECTOR2";
            return null;
        }

        @Override
        protected Object cast(Value value, byte dstDts, RunContext rc) {
            return null;
        }

        @Override
        protected void setup() {

            setBuildInFunction(F_CRT_VEC, new FunctionPool.Adapter() {
                @Override
                public boolean act(ValuePool args, int numOfArgs, Value res, RunContext rc) {
                    res.set(
                            new Vector2( args.get(0).asFloat( rc ), args.get(1).asFloat(rc)),
                            DTS_VECTOR2
                    );
                    return true;
                }
            });

            setProperty(PROP_X, DTS_VECTOR2, new PropertyPool.Adapter() {
                @Override
                public boolean get(Value obj, Value res, RunContext rc) {
                    Float x = ((Vector2) obj.val()).x;
                    res.setAsFloat(x);
                    return true;
                }

                @Override
                public boolean set(Value obj, Value value, RunContext rc) {
                    ((Vector2) obj.val()).x = value.asFloat( rc );
                    return true;
                }
            });

            setProperty(PROP_Y, DTS_VECTOR2, new PropertyPool.Adapter() {
                @Override
                public boolean get(Value obj, Value res, RunContext rc) {
                    Float y = ((Vector2) obj.val()).y;
                    res.setAsFloat(y);
                    return true;
                }

                @Override
                public boolean set(Value obj, Value value, RunContext rc) {
                    ((Vector2) obj.val()).y = (Float) value.val();
                    return true;
                }
            });


        }
    }


    public static void main( String [] arg ) throws IOException {

        String path = new File(".").getCanonicalPath();
        path = path+"/data/testIter.script";

        File file = new File(path);

        InputStream stream = Test.class.getResourceAsStream("/data/testIter.script");

        if ( stream == null )
            stream = new FileInputStream(file);

        BufferedReader br;
        try {
            br = new BufferedReader(
                    new InputStreamReader(
                            stream, "UTF-8"
                    )
            );
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

//        Builder.setVerboseLevel( 1 );

        if ( ! Builder.build(txt, script) )
            return;
        Slot slot = new Slot();
        slot.setScript(script);

        Dumper.dump(script);
//        ScriptDumper.dumpBytes( SkrScript, " ");

        Engine engine = new Engine();
        engine.setExtension( ee );

        engine.setOutEnabled(true);

        System.out.println("Init point: " + slot.getScript().initPoint);

        float cost;
        long start = System.currentTimeMillis();

        engine.init(slot);

        long tLen = System.currentTimeMillis() - start;

        System.out.println(" PT: " + tLen + " ms ");

        long rLen = referenceTest();
        System.out.println(" PT: " + rLen + " ms ");

        cost = (float) tLen / (float) rLen;
        System.out.println(" cost: " + cost);
    }

    private static long referenceTest() {
        long start = System.currentTimeMillis();
        int acum = 0;
        int acum2 = 0;
        int z = 1000;
        while ( z > 0 ) {
            acum = acum2 = 0;
            for (int i = 0; i < 10000; i = i + 1) {
                acum = acum + i;
                acum2 = -acum + i;
            }
            z--;
        }
        System.out.println("Ref. test:  a: " + acum + " a2: " + acum2 );
        return System.currentTimeMillis() - start;
    }
}
