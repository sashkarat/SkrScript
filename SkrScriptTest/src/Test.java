import bsh.EvalError;
import bsh.Interpreter;
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


    public static void main( String [] arg ) throws IOException, EvalError {

        String txt = readFileFromData("testIter.script");
        txt = readFileFromData("testHeartCurve.script");

        EngineTestExtension ee = new EngineTestExtension();
        ee.init();

        Script script = new Script();

//        Builder.setVerboseLevel( 1 );

        if ( ! Builder.build(txt, script) )
            return;
        Slot slot = new Slot();
        slot.setScript(script);

//        Dumper.dump(script);
//        ScriptDumper.dumpBytes( SkrScript, " ");

        Engine engine = new Engine();
        engine.setExtension( ee );

//        engine.setOutEnabled(true);

        System.out.println("Init point: " + slot.getScript().initPoint);

        // execution tests

        System.out.println("Execution tests");

        float refDur = referenceTest();

        float skrDur = skrSkriptTest( engine, slot );

        float bshDur = beanShellTest();

        float skrCost = skrDur / refDur;
        float bshCost = bshDur / refDur;

        float bshSkrCost = bshDur / skrDur;

        System.out.println("SKR cost: " + skrCost );
        System.out.println("BSH cost: " + bshCost );
        System.out.println("BSH/SKR cost: " + bshSkrCost );
    }

    private static long skrSkriptTest(Engine engine, Slot slot ) {

        long start = System.nanoTime();
        engine.init(slot);
        start = System.nanoTime() - start;

        printProcessingTime("skrSkriptTest", start);

        return start;
    }


    private static long referenceTest() {
        long start = System.nanoTime();

        //payload code begin
//        refIter();
        refHeartCurve();
        //payload code end

        start = System.nanoTime() - start;

        printProcessingTime("referenceTest", start);


        return start;
    }



    private static long beanShellTest() throws IOException, EvalError {
        String txt = readFileFromData( "testIter.bsh" );
        txt = readFileFromData( "testHeartCurve.bsh" );

        Interpreter intr = new Interpreter();

        long start = System.nanoTime();
        intr.eval(txt);
        start = System.nanoTime() - start;

        printProcessingTime("beanShellTest", start);

        return start;
    }

    public static String readFileFromData(String fileName ) throws IOException {

        String path = new File(".").getCanonicalPath();
        path = path+"/data/" + fileName;

        File file = new File(path);

        InputStream stream = Test.class.getResourceAsStream("/data/" + fileName);

        if ( stream == null )
            stream = new FileInputStream(file);

        BufferedReader br;
        br = new BufferedReader(
                new InputStreamReader(
                        stream, "UTF-8"
                )
        );
        String s;
        String txt = "";
        while ((s = br.readLine()) != null) {
            txt += s;
            txt += "\n";
        }

        return txt;
    }

    private static void printProcessingTime(String tag, long time ) {
        System.out.println( tag + " PT: " + time + "ns " + (float) time * 0.000001 + "ms ");
    }



    private static int refIter() {
        int acum = 0;
        int acum2 = 0;
        int z = 100;
        while ( z > 0 ) {
            acum = acum2 = 0;
            for (int i = 0; i < 1000; i = i + 1) {
                acum = acum + i;
                acum2 = -acum + i;
            }
            z--;
        }
        return acum2;
    }

    private static float refHeartCurve() {

        float acuum = 0;
        float delta = 0.00001f;

        for ( float phi = 0; phi < Math.PI * 2; phi += delta ) {
            float r = 2f - 2f * (float)Math.sin( phi ) + (float)Math.sin( phi ) *
                    (float)Math.sqrt((float)Math.abs( (float)Math.cos( phi ) ) ) / ( (float)Math.sin( phi ) + 1.4f );

//            float a = (float)Math.sin( phi );
//            float b = (float)Math.sqrt((float)Math.abs( (float)Math.cos( phi ) ) );
//            float c = (float)Math.sin( phi ) + 1.4f ;
//
//            float d = b / c;
//
//            float a2 = a * d;
//
//
//            float r = 2f - 2f*a + a2;



            acuum += r;
//            System.out.println("refHeartCurve: " + phi + " " + a + " " + b + " " + c +
//                    " " + d + " " + a2 + " " + r + " " + acuum);
        }

//        System.out.println("refHeartCurve: acuum: " + acuum );

        return acuum;
    }


}
