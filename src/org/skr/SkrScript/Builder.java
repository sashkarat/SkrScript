package org.skr.SkrScript;

import java.nio.ByteBuffer;
import java.util.*;

/**
 * Created by rat on 22.11.14.
 */

public class Builder {

    protected static class Triple <T, U, V> {
        T a;
        U b;
        V c;
        public Triple(T a, U b, V c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }
    }

    protected static class Array< T > {
        ArrayList<T> ba = new ArrayList<T>();
        int size = 0;

        public int lastIndex() {
            return size - 1;
        }

        public T peek() {
            if ( ba.isEmpty() )
                throw new IndexOutOfBoundsException();
            return ba.get( lastIndex() );
        }

        public T pop() {
            T b = peek();
            ba.remove( lastIndex() );
            size = ba.size();
            return b;
        }

        public void add( T b ) {
            ba.add( b );
            size = ba.size();
        }

        public void set( int idx, T v ) {
            ba.set( idx, v );
        }

        public void add(T b, int count) {
            while( count-- > 0)
                add( b );
        }

        public T get(int i) {
            return ba.get(i);
        }

        public String toString(String delimiter ) {
            String s = "";
            for ( T b : ba ) s += ( b.toString() + delimiter);
            return s;
        }
    }

    public static class FunctionDesc {
        public String name;
        public byte numOfArg;
        public boolean returnable;
        public int address;

        public FunctionDesc(String name, byte numOfArg, boolean returnable, int address) {
            this.name = name;
            this.numOfArg = numOfArg;
            this.returnable = returnable;
            this.address = address;
        }

        @Override
        public String toString() {
            return name + "@" + numOfArg + "," + returnable + "/" + address;
        }
    }


    static final HashMap<String, Byte > operators = new HashMap<String, Byte>();
    static final HashMap<String, Integer > properties = new HashMap<String, Integer>();
    static final ArrayList<String> keywords = new ArrayList<String>();
    static final HashMap<String, Byte > dataTypeSpec = new HashMap<String, Byte>();
    static final HashMap<Integer, FunctionDesc > bfuncMap = new HashMap<Integer, FunctionDesc>();
    static final HashMap<String, String > defines = new HashMap<String, String>();
    static int verboseLevel = 0;

    static final String delimitersStr = " \t\r\n+-*/<>=,;()[]{}!|&#\":.";
    static {
        operators.put("+", Def.OP_ADD);
        operators.put("-", Def.OP_SUB);
        operators.put("*", Def.OP_MUL);
        operators.put("/", Def.OP_DIV);
        operators.put("<", Def.OP_LESS);
        operators.put(">", Def.OP_GRT);
        operators.put("!", Def.OP_NOT);
        operators.put("&", Def.OP_AND);
        operators.put("|", Def.OP_OR);
        operators.put("=", Def.OP_ASSIGN);
        operators.put(",", Def.OP_COMMA);
        operators.put(".", Def.OP_GET_PROP);
        operators.put("==", Def.OP_EQ);
        operators.put("!=", Def.OP_NOT_EQ );
        operators.put(">=", Def.OP_GOEQ);
        operators.put("<=", Def.OP_LOEQ);
        operators.put("typeof", Def.OP_TYPEOF);

        dataTypeSpec.put("null",       Def.DTS_NULL );
        dataTypeSpec.put("bool",       Def.DTS_BOOL );
        dataTypeSpec.put("string",     Def.DTS_STRING );
        dataTypeSpec.put("number",     Def.DTS_NUMBER );
        dataTypeSpec.put("type",       Def.DTS_TYPE );
        dataTypeSpec.put("property",    Def.DTS_PROP_CODE );

        keywords.add("function");
        keywords.add("return");
        keywords.add("for");
        keywords.add("while");
        keywords.add("continue");
        keywords.add("break");
        keywords.add("if");
        keywords.add("else");
        keywords.add("typeof");
        keywords.add("null");
        keywords.add("true");
        keywords.add("false");
        keywords.add("returnable");
        keywords.add("define");
        keywords.add("var");
        keywords.add("reg");

        putFunction(bfuncMap, "msg", (byte) 1, false, Def.F_MSG);
        putFunction(bfuncMap, "err", (byte) 1, false, Def.F_ERR);
        putFunction(bfuncMap, "setSlotEnabled", (byte) 1, false, Def.F_SET_SLOT_ENABLED);
        putFunction(bfuncMap, "typeStr", (byte) 1, true, Def.F_TYPE_TO_STR);


        putFunction(bfuncMap, "sin", (byte) 1, true, Def.F_SIN);
        putFunction(bfuncMap, "cos", (byte) 1, true, Def.F_COS);
        putFunction(bfuncMap, "tan", (byte) 1, true, Def.F_TAN);
        putFunction(bfuncMap, "acos", (byte) 1, true, Def.F_ACOS);
        putFunction(bfuncMap, "asin", (byte) 1, true, Def.F_ASIN);
        putFunction(bfuncMap, "atan", (byte) 1, true, Def.F_ATAN);
        putFunction(bfuncMap, "cbrt", (byte) 1, true, Def.F_CBRT);
        putFunction(bfuncMap, "sqrt", (byte) 1, true, Def.F_SQRT);
        putFunction(bfuncMap, "exp", (byte) 1, true, Def.F_EXP);
        putFunction(bfuncMap, "hypot", (byte) 2, true, Def.F_HYPOT);
        putFunction(bfuncMap, "pow", (byte) 2, true, Def.F_POW);
        putFunction(bfuncMap, "log", (byte) 1, true, Def.F_LOG);
        putFunction(bfuncMap, "log10", (byte) 1, true, Def.F_LOG10);
        putFunction(bfuncMap, "max", (byte) 2, true, Def.F_MAX);
        putFunction(bfuncMap, "min", (byte) 2, true, Def.F_MIN);
        putFunction(bfuncMap, "random", (byte) 0, true, Def.F_RANDOM);
        putFunction(bfuncMap, "toDeg", (byte) 1, true, Def.F_TO_DEGREES);
        putFunction(bfuncMap, "toRad", (byte) 1, true, Def.F_TO_RADIANS);
        putFunction(bfuncMap, "abs", (byte) 1, true, Def.F_ABS);
        putFunction(bfuncMap, "floor", (byte) 1, true, Def.F_FLOOR);
        putFunction(bfuncMap, "ceil", (byte) 1, true, Def.F_CEIL);
        putFunction(bfuncMap, "round", (byte) 1, true, Def.F_ROUND);

        defines.put("PI", "3.1415927");
        defines.put("E", "2.7182818");
    }

    public static HashMap<String, Byte> getOperators() {
        return operators;
    }

    public static HashMap<String, String> getDefines() {
        return defines;
    }

    public static HashMap<String, Integer> getProperties() {
        return properties;
    }

    public static ArrayList<String> getKeywords() {
        return keywords;
    }

    public static HashMap<String, Byte> getDataTypeSpec() {
        return dataTypeSpec;
    }

    public static HashMap<Integer, FunctionDesc> getBfuncMap() {
        return bfuncMap;
    }

    public static int getVerboseLevel() {
        return verboseLevel;
    }

    public static String getDelimitersStr() {
        return delimitersStr;
    }

    public static void setVerboseLevel( int level ) {
        verboseLevel = level;
    }

    public static boolean addExtendedDTS( byte dtsCode, String dtsString ) {
        if ( dtsCode > Def.EXTENDED_DTS_CODE ) {
            printError("Builder.addExtendedDTS. New DTS code must be less or equal Def.EXTENDED_DTS_CODE", null);
            return false;
        }

        if ( isTokenInUsage( dtsString, null ) ) {
            printError("Builder.addExtendedDTS. Such a DTS is already defined: " + dtsString , null);
            return false;
        }
        dataTypeSpec.put( dtsString, dtsCode );
        return true;
    }

    public static boolean addExtendedDefine(String defineStr, String value ) {
        if ( defines.containsKey( defineStr ) ) {
            printError("Builder.addExtendedDefine. " + defineStr + " is already defined", null );
            return false;
        }
        defines.put( defineStr, value );
        return true;
    }

    public static boolean addExtendedProperty( Integer propertyCode, String properyString ) {
        if ( isTokenInUsage( properyString, null) ) {
            printError("Builder.addExtendedProperty. Property: " + properyString + " could not be added.", null );
            return false;
        }
        properties.put(properyString, propertyCode );
        return true;
    }

    public static boolean addExtendedBuildInFunction(String name, byte numOfArg, boolean returnable, int address ) {
        if ( address > Def.EXTENDED_BUILD_IN_FUNCTION_ADDRESS  ) {
            printError("Builder.addExtendedBuildInFunction. New function address must be less or equal " +
                    "Def.EXTENDED_BUILD_IN_FUNCTION_ADDRESS", null);
            return false;
        }

        int id = getFunctionId( name, numOfArg );

        if ( bfuncMap.containsKey( id ) ) {
            printError("Builder.addExtendedBuildInFunction. Such a function is already defined " + name, null);
            return false;
        }

        putFunction( bfuncMap, name, numOfArg, returnable, address );
        return true;
    }

    protected static boolean isTokenInUsage( String token, BuildContext bc ) {
        return keywords.contains( token ) || fmapContainsName( bfuncMap, token )
                || properties.containsKey( token ) || dataTypeSpec.containsKey( token ) ||
                ((bc != null) && bc.defines.containsKey(token) || defines.containsKey( token ) );
    }

    protected static int getFunctionId( String name, Byte argNum ) {
        return (name+"@"+argNum.toString()).hashCode();
    }

    protected static void putFunction(HashMap<Integer, FunctionDesc> fmap,
                                   String name, Byte numOfArg, Boolean returnable, Integer address) {
        Integer id = getFunctionId(name, numOfArg);
        fmap.put(id, new FunctionDesc(name, numOfArg, returnable, address));
    }

    protected static void putFunction(HashMap<Integer, FunctionDesc> fmap,
                                   FunctionDesc fdesc) {
        Integer id = getFunctionId(fdesc.name, fdesc.numOfArg);
        fmap.put(id, fdesc );
    }

    public static boolean fmapContainsName( HashMap<Integer, FunctionDesc > fmap, String name ) {
        for ( Integer key : fmap.keySet() ) {
            if ( fmap.get( key).name.equals( name ) )
                return true;
        }
        return false;
    }

    public static String getFName(int address ) {
        if ( address >= 0 )
            return "";
        for ( Integer id : bfuncMap.keySet() )
            if ( address == bfuncMap.get(id).address )
                return bfuncMap.get(id).name;
        return "";
    }

    static class VariableMap {
        int idx = -1;
        HashMap<String, Integer > map = new HashMap<String, Integer>();
        VariableMap parentMap = null;

        int getIndex(String varName) {
            if ( map.containsKey( varName ) )
                return map.get( varName );
            if ( parentMap != null )
                return parentMap.getIndex(varName);
            return -1;
        }

        boolean isVariable( String vname ) {
            return map.containsKey(vname) || (parentMap != null && parentMap.isVariable(vname));
        }

        boolean addVar(String name, BuildContext bc) {
            if ( !isWord( name ) || Builder.isTokenInUsage( name, bc ) ) {
                printError("addVar. Wrong variable name: " + name , bc);
                return false;
            }
            if ( map.containsKey( name ) ) {
               printError("addVar. Variable: " + name + " is already defined.", bc);
                return false;
            }
            if ( parentMap != null ) {
                if ( idx < parentMap.idx )
                    idx = parentMap.idx;
            }
            map.put( name, ++idx);
            if ( verboseLevel > 1)
                printMsg("addVar. " + (( map == bc.rmap.map)?"<reg> " : "<var> " ) + name  +   " idx: " + idx, bc);
            return true;
        }

    }

    static class BuildContext {

        int lineNum = 0;
        HashMap<Integer, FunctionDesc> funcMap = new HashMap<Integer, FunctionDesc>();
        Stack<Triple<String, Byte, Integer>> fcallAddrStack = new Stack<Triple<String, Byte, Integer>>(2);
        HashMap<String, String > defines;

        Array<Byte> bytes = new Array<Byte>();
        VariableMap rmap = new VariableMap();
        StringTokenizer tknzr;
        Array< String > strings = new Array<String>();
        FunctionDesc thisFunc;
        String lastCalledFunc;
        byte lastSetRvDts = 0;
        byte lastSetLvDts = 0;
        byte lastOpCode = 0;

        public BuildContext() {
            defines = new HashMap<String, String>();
            this.defines.putAll( Builder.defines );
        }

        public void putOp( byte opCode ) {
            if ( Builder.verboseLevel > 2 )
                printMsg("putOp: " + Dumper.getOpCodeStr(opCode), this);
            optimizedAdd( opCode );
        }

        private void optimizedAdd(byte opCode ) {
            int lc = getLastOpCode();

            if ( Def.isDts(opCode) ) {

                if ( lc == Def.SETRV )
                    lastSetRvDts = opCode;
                else if ( lc == Def.SETLV )
                    lastSetLvDts = opCode;
            }

            switch ( opCode ) {
                case Def.POPLV :
                    if ( lc == Def.PUSHRV ) {
                        bytes.pop();
                        opCode = Def.RVTOLV;
                        byte t = lastSetRvDts;
                        lastSetRvDts = lastSetLvDts;
                        lastSetLvDts = t;
                        break;
                    }
                    if ( lc == Def.PUSHLV ) {
                        bytes.pop();
                        lastOpCode = 0;
                        return;
                    }
                    lastSetLvDts = 0;
                    break;
                case Def.POPRV :
                    if ( lc == Def.PUSHLV ) {
                        bytes.pop();
                        opCode = Def.LVTORV;
                        byte t = lastSetRvDts;
                        lastSetRvDts = lastSetLvDts;
                        lastSetLvDts = t;
                        break;
                    }
                    if ( lc == Def.PUSHRV ) {
                        bytes.pop();
                        lastOpCode = 0;
                        return;
                    }
                    lastSetRvDts = 0;
                    break;
                case Def.OBTAINRV :
                    if ( lastSetRvDts != Def.DTS_REG && lastSetRvDts != Def.DTS_VAR )
                        return;
                    break;
                case Def.OBTAINLV :
                    if ( lastSetLvDts != Def.DTS_REG && lastSetLvDts != Def.DTS_VAR )
                        return;
                    break;
                case Def.PUSHLV:
                    if ( lc == Def.RVTOLV ) {
                        bytes.pop();
                        opCode = Def.PUSHRV;
                        break;
                    }
                    if ( lc == Def.POPLV ) {
                        bytes.pop();
                        lastOpCode = 0;
                        return;
                    }
                    break;
            }

            bytes.add(opCode);
            lastOpCode = opCode;
        }

        public boolean isFuncReturnable(String name, byte argNum ) {
            int id = getFunctionId(name, argNum);
            if (bfuncMap.containsKey(id))
                return bfuncMap.get(id).returnable;
            return funcMap.containsKey(id) && funcMap.get(id).returnable;
        }

        public byte getLastOpCode() {
            if ( bytes.size > 0 ) {
                if ( lastOpCode == bytes.peek() )
                    return lastOpCode;
            }
            return lastOpCode;
        }
    }



    public static boolean build( String text, Script script ) {
        if ( verboseLevel > 0)
            System.out.println( "build.started");

        BuildContext bc = new BuildContext();

        bc.tknzr = new StringTokenizer( text, delimitersStr, true);

        while ( bc.tknzr.hasMoreTokens() ) {
            String token = nextToken( bc );
            if ( token == null )
                break;
            if ( token.equals("function") ) {
                if (!buildFunction(null, bc)) {
                    printError("build.failed", null);
                    return false;
                }
            } else if ( token.equals("reg") ) {
                if (!parseGlobalRegDeclaration(bc)) {
                    printError("build.failed", null);
                    return false;
                }
            } else if ( token.equals("define") ) {
                token = nextToken( bc );
                if ( token == null )
                    break;
                if ( !isWord( token ) || isTokenInUsage( token, null ) ) {
                    printError("build. define: token is already in usage", bc );
                    return false;
                }
                String defineStr = token;
                token = nextToken( bc );
                if ( token ==  null )
                    break;
                token = readDefinitionToken( token,  bc );
                if ( verboseLevel > 1)
                    printMsg("Build. define: " + defineStr + " " + token, bc );
                if ( token == null ) {
                    break;
                }
                bc.defines.put( defineStr, token );

            } else {
                printError("build. Unexpected token: " + token, bc);
                return false;
            }
        }

        while ( !bc.fcallAddrStack.isEmpty() ) {
            Triple<String, Byte, Integer> tp = bc.fcallAddrStack.pop();
            int fid = getFunctionId( tp.a, tp.b);
            if ( !bc.funcMap.containsKey( fid ) ) {
                printError("build. Call function: " + tp.a + "@" + tp.b + " Function nod found.", null );
                return false;
            }
            FunctionDesc fdesc = bc.funcMap.get(fid);
            int addr = fdesc.address;
            if (addr < 0) {
                printError("build. Call function: " + fdesc + ". Entry point not found ", null );
                return false;
            }
            setInt( addr, bc.bytes, tp.c);
        }

        if ( !fmapContainsName(bc.funcMap, "init") ) {
            printError("build. init function is not defined.", null);
            return false;
        }
        if ( !fmapContainsName(bc.funcMap, "run")) {
            printError("build. \"run\" function is not defined.", null);
            return false;
        }

        script.initPoint = bc.funcMap.get( getFunctionId( "init", (byte)0) ).address;
        script.runPoint = bc.funcMap.get( getFunctionId( "run", (byte)0) ).address;
        script.numOfReg = bc.rmap.idx + 1;

        script.bytes = new byte[ bc.bytes.size ];
        for(int i = 0; i < bc.bytes.size; i++ )
            script.bytes[i] = bc.bytes.get(i);
        if ( bc.strings.size > 0 ) {
            script.strings = new String[ bc.strings.size ];
            for ( int i = 0; i < bc.strings.size; i++)
                script.strings[i] = bc.strings.get(i);
        }
        if ( verboseLevel > 0)
            printMsg("build.success.", bc);
        return true;
    }

    static String readDefinitionToken(String firstToken,  BuildContext bc ) {
        String out = null;
        String token = null;
        while ( bc.tknzr.hasMoreTokens() ) {
            if ( token == null && firstToken != null ) {
                token = firstToken;
            } else {
                token = bc.tknzr.nextToken();
            }
            if ( isSpace( token ) )
                return out;

            if ( isEol( token ) ) {
                bc.lineNum++;
                return out;
            }

            if ( token.charAt(0) == '\"' ) {
                out = extractString( bc );
                return out;
            }
            if ( out == null )
                out = token;
            else
                out += token;
        }
        return null;
    }

    static boolean isComment( String token ){
        return token.equals("#") ;
    }

    static boolean isSpace( String token ) {
        if ( token.length() == 1 ) {
            if ( token.equals(" ") || token.equals("\t") )
                return true;
        }
        return false;
    }

    static boolean isEol( String token ) {
        return token.equals("\n");
    }

    static boolean isWord( String token ) {
        return Character.isLetter( token.charAt(0) );
    }

    static boolean isNumber( String token ) {
        if ( !Character.isDigit( token.charAt(0)) )
            return false;
        //test conversion
        try {
            Float f = Float.valueOf( token );
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    private static ByteBuffer tmpBb = ByteBuffer.allocateDirect(4);

    static void putInt(int val, Array<Byte>  ba ) {
        tmpBb.putInt(0, val);
        ba.add(tmpBb.get(0));
        ba.add(tmpBb.get(1));
        ba.add(tmpBb.get(2));
        ba.add(tmpBb.get(3));
    }

    static void putFloat( float val, Array<Byte> ba ) {
        tmpBb.putFloat(0, val);
        ba.add(tmpBb.get(0));
        ba.add(tmpBb.get(1));
        ba.add(tmpBb.get(2));
        ba.add(tmpBb.get(3));
    }

    static void setInt( int val, Array<Byte> ba, int pos ) {
        tmpBb.putInt(0, val);
        ba.set(pos++, tmpBb.get(0));
        ba.set(pos++, tmpBb.get(1));
        ba.set(pos++, tmpBb.get(2));
        ba.set(pos, tmpBb.get(3));
    }

    static void printError(String errMsg, BuildContext bc ) {
        System.err.println( errMsg + ( (bc != null)?(" <line: " + (bc.lineNum+1) + ">"):" " ) );
    }

    static void printMsg(String msg, BuildContext bc ) {
        System.out.println(msg + ( (bc != null)?(" <line: " + (bc.lineNum+1) + ">"):" " ) );
    }

    static String nextToken( BuildContext bc  ) {
        while ( bc.tknzr.hasMoreTokens() ) {
            String token = bc.tknzr.nextToken();
            if (token.length() == 1) {
                if ( isSpace(token) )
                    continue;
                if ( isEol(token)) {
                    bc.lineNum ++;
                    continue;
                }
                if ( isComment(token) ) {
                    skipLine(bc.tknzr, bc);
                    continue;
                }
            }
            return token;
        }
        return null;
    }

    static void skipLine( StringTokenizer tokenizer, BuildContext bc ) {
        while( tokenizer.hasMoreTokens() ) {
            if ( isEol(tokenizer.nextToken()) ) {
                bc.lineNum ++;
                return;
            }
        }
    }

    static boolean buildFunction(String firstToken, BuildContext bc ) {

        String token;
        if ( firstToken == null )
            token = nextToken( bc );
        else
            token = firstToken;

        if ( token == null ) {
            printError("buildFunction. unexpected end of file", bc);
            return false;
        }

        if ( fmapContainsName(bfuncMap, token) ) {
            printError("buildFunction. Function is a build-in function: " + token, bc);
            return false;
        }

        if ( !isWord( token ) || dataTypeSpec.containsKey( token ) || properties.containsKey( token )
            || bc.rmap.isVariable( token ) ) {
            printError("buildFunction. Wrong function name: " + token, bc);
            return false;
        }



        bc.thisFunc = new FunctionDesc( token, (byte) 0, false, -1 );

        VariableMap vmap = new VariableMap();

        token = nextToken( bc );
        if ( ! token.equals("(") ) {
            printError("buildFunction. ( not found.", bc);
            return false;
        }

        bc.thisFunc.numOfArg = 0;

        while ( bc.tknzr.hasMoreTokens() ) {
            token = nextToken( bc);
            if ( token == null )
                return false;
            if ( token.equals(")") )
                break;

            if ( ! vmap.addVar(token, bc) )
                return false;

            bc.thisFunc.numOfArg++;

            token = nextToken( bc );
            if ( token == null )
                break;
            if ( token.equals(")") )
                break;
            if ( !token.equals(",") ) {
                printError("buildFunction. Unexpected token: " + token, bc);
                return false;
            }
        }
        if ( token == null ) {
            printError("buildFunction. Error occurred when function signature parsed", bc);
            return false;
        }

        token = nextToken( bc );

        if ( token == null ) {
            printError("buildFunction. Unexpected end of file", bc );
            return false;
        }

        if ( token.equals("returnable") ) {
            bc.thisFunc.returnable = true;
            token = nextToken( bc );
        }

        if ( token == null ) {
            printError("buildFunction. Unexpected end of file.", bc );
            return false;
        }


        if ( token.equals(";") ) {
            putFunction(bc.funcMap, bc.thisFunc);
            if ( verboseLevel > 0 )
                printMsg("buildFunction. Function declaration: \"" + bc.thisFunc + "\"", bc);
            return true;
        }

        if ( !token.equals("{") ) {
            printError("buildFunction. Not found { ", bc );
            return false;
        }

        bc.thisFunc.address = bc.bytes.size;
        if ( verboseLevel > 0 )
            printMsg("\n\nbuildFunction. Function definition: \"" + bc.thisFunc + "\"", bc);
        putFunction(bc.funcMap, bc.thisFunc);

        for ( int i = 0; i < bc.thisFunc.numOfArg; i ++ ) {
            bc.putOp(Def.POPRV);
            bc.putOp(Def.SETLV);
            bc.putOp(Def.DTS_VAR);
            putInt( bc.thisFunc.numOfArg - i - 1, bc.bytes );
            bc.putOp(Def.OP_ASSIGN);
        }

        int secSize = bc.bytes.size;

        if ( ! buildSection( vmap, false, new SectionBuildContext( bc ) ) )
            return false;

        byte lastOpCode = bc.getLastOpCode();

        secSize = bc.bytes.size - secSize;

        if ( lastOpCode != Def.RET || secSize == 0 ) {
            if ( bc.thisFunc.returnable) {
                printError("buildFunction. function \"" + bc.thisFunc + "\" should return a value.", bc );
                return false;
            }
            bc.putOp(Def.RET);
        }
        if ( verboseLevel > 0 ) {
            printMsg("buildFunction. function: \"" + bc.thisFunc + "\" definition is done.", bc);
            printMsg("\n", null);
        }
        return true;
    }

    static boolean parseGlobalRegDeclaration( BuildContext bc ) {
        while ( bc.tknzr.hasMoreTokens() ) {
            String token = nextToken( bc );
            if ( token == null ) {
                printError("parseGlobalRegDeclaration. Unexpected end of file.", bc);
                return false;
            }
            if ( isWord(token) ) {
                if ( !bc.rmap.addVar( token, bc) )
                    return false;
            } else if ( token.equals(";") ) {
                return true;
            }

            token = nextToken( bc );
            if ( token == null ) {
                printError("parseGlobalRegDeclaration. Unexpected end of file.", bc);
                return false;
            }

            if ( token.equals(",") )
                continue;
            else if ( token.equals(";") )
                return true;
            printError("parseGlobalRegDeclaration. Unexpected token: " + token, bc );
            return false;
        }
        printError("parseGlobalRegDeclaration. Unexpected end of file.", bc);
        return false;
    }

    static class SectionBuildContext {
        BuildContext bc;
        Stack<Integer> breakPosStack;
        int continuePos;

        public SectionBuildContext(BuildContext bc) {
            this.bc = bc;
        }

        public SectionBuildContext(BuildContext bc, int continuePos) {
            this.bc = bc;
            this.continuePos = continuePos;
            breakPosStack = new Stack<Integer>(2);
        }
    }

    static boolean buildSection( VariableMap vmap, boolean createVMap, SectionBuildContext sbc ) {
        if ( verboseLevel > 0 )
            printMsg("buildSection. ..... start", sbc.bc);
        if ( createVMap ) {
            VariableMap vm = new VariableMap();
            vm.parentMap = vmap;
            vmap = vm;
        }

        String token;

        while ( sbc.bc.tknzr.hasMoreTokens() ) {
            token = nextToken( sbc.bc );
            if ( token == null )
                break;

//            printMsg("buildSection. token: " + token, sbc.bc);
            if ( token.equals("}") ) {
                if ( verboseLevel > 0 )
                    printMsg("buildSection. ..... finish", sbc.bc);
                return true;
            }
            if ( token.equals("{") ) {
                if ( ! buildSection( vmap, true, sbc ) )
                    return false;
                continue;
            }

            if ( token.equals("if") ) {
                if ( ! buildIf( vmap, sbc) )
                    return false;
                continue;
            }

            if ( token.equals("else") ) {
                if ( ! buildElse( vmap, sbc) )
                    return false;
                continue;
            }

            if ( token.equals("for") ) {
                if ( ! buildFor( vmap, sbc ) )
                    return false;
                continue;
            }

            if ( token.equals("while") ) {
                if ( ! buildWhile( vmap, sbc) )
                    return false;
                continue;
            }

            if ( sbc.breakPosStack != null ) {
                if (token.equals("break") ) {
                    sbc.bc.putOp(Def.JUMP);
                    sbc.breakPosStack.push( sbc.bc.bytes.size );
                    sbc.bc.putOp((byte) 0); sbc.bc.putOp((byte) 0);
                    sbc.bc.putOp((byte) 0); sbc.bc.putOp((byte) 0);
                    continue;
                }

                if ( token.equals("continue") ) {
                    sbc.bc.putOp(Def.JUMP);
                    putInt( sbc.continuePos, sbc.bc.bytes );
                    continue;
                }
            }

            boolean addReturn = false;

            if ( token.equals("return") ) {
                addReturn = true;

                token = nextToken( sbc.bc );
                if ( token == null )
                    return false;

                if ( token.equals(";") && sbc.bc.thisFunc.returnable ) {
                    printError("Builder.buildSection. Function: " + sbc.bc.thisFunc + " should return a value",
                            sbc.bc );
                    return false;
                } else if ( !token.equals(";") && !sbc.bc.thisFunc.returnable ) {
                    printError("Builder.buildSection. Function: " + sbc.bc.thisFunc + " is not returnable",
                            sbc.bc );
                    return false;
                }

            }

            token = buildExpression( token, vmap, sbc.bc ) ;
            if ( token == null )
                break;
            if ( !token.equals(";") ) {
                printError("buildSection. Unexpected end of expression: " + token, sbc.bc);
                return false;
            }

            if ( addReturn ) {
                sbc.bc.putOp( Def.RET );
            }
        }
        printError("buildSection. Error occurred  when section parsed", sbc.bc);
        return false;
    }

    static boolean buildWhile(VariableMap vmap, SectionBuildContext sbc) {

        while( sbc.bc.tknzr.hasMoreTokens() ) {
            String token = nextToken( sbc.bc );
            if ( token == null )
                break;

            if ( ! token.equals("(") ) {
                printError("buildWhile. ( not found ", sbc.bc );
                return false;
            }
            int loopStartPos = sbc.bc.bytes.size;

            token = buildExpression( null, vmap, sbc.bc );
            if ( token == null )
                break;
            if ( ! token.equals(")") ) {
                printError("buildWhile. ) not found ", sbc.bc);
                return false;
            }

            sbc.bc.putOp(Def.JUMPF);
            int exitAddrPos = sbc.bc.bytes.size;
            sbc.bc.putOp((byte) 0); sbc.bc.putOp((byte) 0);
            sbc.bc.putOp((byte) 0); sbc.bc.putOp((byte) 0);

            token = nextToken( sbc.bc );
            if ( token == null )
                break;
            if ( ! token.equals("{") ) {
                printError("buildWhile. { not found", sbc.bc );
                return false;
            }

            SectionBuildContext subSbc = new SectionBuildContext( sbc.bc, loopStartPos );

            if ( ! buildSection(vmap, true, subSbc ) )
                return false;
            sbc.bc.putOp(Def.JUMP);
            putInt( loopStartPos, sbc.bc.bytes );
            setInt( sbc.bc.bytes.size, sbc.bc.bytes, exitAddrPos );

            while ( !subSbc.breakPosStack.isEmpty() ) {
                setInt( sbc.bc.bytes.size, sbc.bc.bytes, subSbc.breakPosStack.pop() );
            }

            return true;
        }


        return false;
    }

    static boolean buildFor(VariableMap vmap, SectionBuildContext sbc ) {
        while ( sbc.bc.tknzr.hasMoreTokens() ) {
            String token = nextToken( sbc.bc );
            if ( token == null )
                break;
            if ( ! token.equals("(") ) {
                printError("buildFor. ( not found", sbc.bc);
                return false;
            }
            // build init section of a "for"
            token = buildExpression( null, vmap, sbc.bc );
            if ( token == null )
                break;
            if ( ! token.equals(";") ) {
                printError("buildFor. 1'st ; not found", sbc.bc);
                return false;
            }

            int cmprSecPos = sbc.bc.bytes.size;

            // build compare section of a "for"
            token = buildExpression( null, vmap, sbc.bc );
            if ( ! token.equals(";") ) {
                printError("buildFor. 2'nd ; not found", sbc.bc);
                return false;
            }
            sbc.bc.putOp(Def.JUMPF);
            int exitAddrPos = sbc.bc.bytes.size;
            sbc.bc.putOp((byte) 0); sbc.bc.putOp((byte) 0);
            sbc.bc.putOp((byte) 0); sbc.bc.putOp((byte) 0);
            sbc.bc.putOp(Def.JUMP);
            int loopInAddrPos = sbc.bc.bytes.size;
            sbc.bc.putOp((byte) 0); sbc.bc.putOp((byte) 0);
            sbc.bc.putOp((byte) 0); sbc.bc.putOp((byte) 0);
            int iterSecAddrPos = sbc.bc.bytes.size;
            //build iter section of a "for"
            token = buildExpression(null, vmap, sbc.bc );
            if ( token == null )
                break;
            if ( ! token.equals(")") ) {
                printError("buildFor. ) not found", sbc.bc);
                return false;
            }
            sbc.bc.putOp(Def.JUMP);
            putInt( cmprSecPos, sbc.bc.bytes );
            setInt( sbc.bc.bytes.size, sbc.bc.bytes, loopInAddrPos );

            // build in-loop section
            token = nextToken( sbc.bc );
            if ( token == null )
                break;
            if ( ! token.equals("{") ) {
                printError("buildFor. { not found", sbc.bc);
                return false;
            }

            SectionBuildContext subSbc = new SectionBuildContext( sbc.bc, iterSecAddrPos );

            if ( ! buildSection( vmap, true, subSbc ) )
                break;
            sbc.bc.putOp(Def.JUMP);
            putInt( iterSecAddrPos, sbc.bc.bytes );
            setInt(sbc.bc.bytes.size, sbc.bc.bytes, exitAddrPos);

            while ( !subSbc.breakPosStack.isEmpty() ) {
                setInt( sbc.bc.bytes.size, sbc.bc.bytes, subSbc.breakPosStack.pop() );
            }

            return true;
        }
        return false;
    }


    static boolean buildIf( VariableMap vmap, SectionBuildContext sbc ) {
        while ( sbc.bc.tknzr.hasMoreTokens() ) {
            String token = nextToken( sbc.bc );
            if ( token == null )
                break;
            if ( ! token.equals( "(") ) {
                printError("buildIf. ( not found ", sbc.bc);
                return false;
            }

            token = buildExpression( null, vmap, sbc.bc );
            if ( token == null )
                return false;
            if ( !token.equals(")") ) {
                printError("buildIf. ) not found", sbc.bc);
                return false;
            }

            sbc.bc.putOp(Def.JUMPF);
            int posJmpF = sbc.bc.bytes.size;
            if ( verboseLevel > 1 )
                printMsg("buildIf. posJmpF: " + posJmpF, sbc.bc);
            sbc.bc.bytes.add((byte) 0, 4);

            token = nextToken( sbc.bc );
            if ( token == null )
                break;
            if ( token.equals("{") ) {
                if ( !buildSection( vmap, true, sbc ) )
                    return false;
            } else {
                printError("buildIf. { not found ", sbc.bc);
                return false;
            }
            sbc.bc.putOp(Def.JUMP);
            putInt(sbc.bc.bytes.size+4, sbc.bc.bytes);
            setInt(sbc.bc.bytes.size, sbc.bc.bytes, posJmpF);
            return true;
        }
        printError("buildIf.Unexpected end of file", sbc.bc);
        return false;
    }

    static boolean buildElse( VariableMap vmap, SectionBuildContext sbc ) {
        while ( sbc.bc.tknzr.hasMoreTokens() ) {
            String token = nextToken( sbc.bc);
            if ( token == null )
                break;
            int posJmp = sbc.bc.bytes.size - 4;

            if ( token.equals("{") ) {
                if ( !buildSection( vmap, true, sbc ) )
                    return false;
            } else if ( token.equals("if") ) {
                if ( !buildIf( vmap, sbc ) )
                    return false;
            } else {
                printError("buildElse. if or { not found", sbc.bc);
                return false;
            }
            setInt( sbc.bc.bytes.size, sbc.bc.bytes, posJmp );
            return true;
        }
        printError("buildElse. Unexpected end of file", sbc.bc);
        return false;
    }

    static String buildExpression( String firstToken, VariableMap vmap, BuildContext bc ) {

        String token = firstToken;
        if ( token == null )
            token = nextToken( bc );
        if ( token == null )
            return null;

        VariableMap tmap = null;

        if ( token.equals("var") )
            tmap = vmap;
        if ( token.equals("reg") ) {
            if ( !bc.thisFunc.name.equals("init") ) {
                printError("buildExpression. definition of the reg variable is enabled inside init function only, " +
                        " or at the global space.", bc);
                return null;
            }
            tmap = bc.rmap;
        }


        // there are no var and no reg instructions before the expression
        if ( tmap == null ) {
            token =  buildExpression(token, vmap, bc, false);
            if ( token == null)
                return null;
            return token;
        }

        // process variable definition and variable initialization instructions
        // e.g. var a = 4, b, z = -1;
        while ( bc.tknzr.hasMoreTokens() ) {
            token = nextToken( bc );
            if ( token == null )
                break;
            if ( token.equals(";") )
                break;
            if ( ! tmap.addVar(token, bc) )
                break;
            if ( tmap == vmap )
                bc.putOp(Def.INCVARNUM);
            token = buildExpression( token, vmap, bc, true );
            if ( token == null )
                break;
            if ( token.equals(",") )
                continue;
            if ( token.equals(";") )
                return token;
            printError("buildExpression. Unexpected end of expression: " + token, bc);
            return null;
        }

        return null;
    }

    static String buildExpression(String firstToken, VariableMap vmap, BuildContext bc, boolean breakOnComma ) {

        Array<String> expression = new Array<String>();
        String token = tokenizeExpression( firstToken, bc, expression, breakOnComma );
        if ( token == null )
            return null;
        if ( verboseLevel > 0 )
            printMsg("\n buildExpression. >>>>>>>>>>:\t\t\t[ " + expression.toString(" ") + "]", bc );

        if ( ! compileExpression( expression, vmap, bc) )
            return null;
        // remove PUSHLV at the end of expression
        if ( bc.getLastOpCode() == Def.PUSHLV && bc.bytes.peek() == Def.PUSHLV )
            bc.bytes.pop();
        return token;
    }

    static String tokenizeExpression(String firstToken, BuildContext bc, Array<String> expression, boolean breakOnComma ) {
        String token = null;

        int z = 0;
        byte prevOpCode = 0;

        while ( bc.tknzr.hasMoreTokens() ) {

            if ( token == null ) {
                if ( firstToken != null )
                    token = firstToken;
                else
                    token = bc.tknzr.nextToken();
            } else {
                token = bc.tknzr.nextToken();
            }

            if ( verboseLevel > 3)
                printMsg("tokenizeExpression. token: " + token, bc);

            if ( bc.defines.containsKey( token ) )
                token = bc.defines.get( token );

            String prevToken = null;
            if ( expression.size > 0 )
                prevToken = expression.peek();


            if ( token.equals(";") ) {
                if ( z > 0 ) {
                    printError("tokenizeExpression.  ) not found", bc );
                    return null;
                } else if ( z < 0 ) {
                    printError("tokenizeExpression.  ( not found", bc );
                    return null;
                }
                break;
            }

            if ( breakOnComma && z== 0 && token.equals(",") )
                break;

            if ( token.equals("\"") ) {
                String string = extractString( bc );
                if ( string == null )
                    return null;
                expression.add( string );
                continue;
            }

            if ( prevToken != null && !prevToken.isEmpty() ) {
                char fch = prevToken.charAt(0);
                char lch = prevToken.charAt(prevToken.length() - 1);

                if ( Character.isDigit( fch ) && lch == '.') {
                    expression.add(expression.pop() + token);
                    prevOpCode = 0;
                    continue;
                }

                if (token.equals("-")) {
                    //0.2e'-'
                    if ((lch == 'E' || lch == 'e') && Character.isDigit(fch)) {
                        expression.add(expression.pop() + token);
                        prevOpCode = 0;
                        continue;
                    }
                }


                if (isNumber(token)) {
                    // 0.2e'4' or .'4'
                    if ( verboseLevel > 2)
                        printMsg("tokenizeExpression. isNumber: " + " lch: " + lch, bc);
                    if (lch == 'E' || lch == 'e' || lch == '.'
                            || (lch == '-' && prevToken.length() > 1) ) {
                        expression.add(expression.pop() + token);
                        prevOpCode = 0;
                        continue;
                    }
                }

                if ( token.equals("E") || token.equals("e") ) {
                    if ( lch == '.' || Character.isDigit( fch ) ) {
                        expression.add(expression.pop() + token);
                        prevOpCode = 0;
                        continue;
                    }
                }

                if ( token.equals(".") && Character.isDigit( fch ) ) {
                    expression.add(expression.pop() + token);
                    prevOpCode = 0;
                    continue;
                }

                if ( token.length() >= 2 && isNumber( token.substring(1) ) ) {
                    char tfch = token.charAt(0);
                    if ((tfch == 'E' || tfch == 'e') && isNumber(prevToken) ) {
                        expression.add(expression.pop() + token);
                        prevOpCode = 0;
                        continue;
                    }
                }
            }



            if ( operators.containsKey( token ) ) {
                byte opCode = operators.get( token );
                prevOpCode = mergeOpCodes( prevOpCode, opCode );
                if ( opCode != prevOpCode ) {
                    expression.add(expression.pop() + token);
                } else {
                    expression.add( token );
                }
                continue;
            }

            prevOpCode = 0;

            if ( isSpace( token ) ) {
                expression.add("");
                continue;
            }
            if ( isEol( token ) ) {
                bc.lineNum++;
                expression.add("");
                continue;
            }
            if ( token.equals("(") ) {
                z++;
                expression.add( token );
                continue;
            }
            if ( token.equals(")") ) {
                z--;
                if ( z < 0) {
                    break;
                }
                expression.add( token );
                continue;
            }

            expression.add( token );
        }

        return token;
    }

    static String extractString(BuildContext bc) {
        StringBuilder sb = new StringBuilder("\"");

        boolean slash = false;
        while ( bc.tknzr.hasMoreTokens() ) {
            String token = bc.tknzr.nextToken();
            if ( token.equals("\\") ) {
                slash = true;
                sb.append( token );
                continue;
            }
            if ( token.equals("\"") && ! slash ) {
                sb.append(token);
                return sb.toString();
            }
            sb.append( token );
            slash = false;
        }
        printError("extractString. Unexpected end of file", bc);
        return null;
    }


    static class ExpressionCompilingContext {

        Stack<String> oStack = new Stack<String>(2);
        Stack<Byte>  wStack = new Stack<Byte>(2);
        Stack<Byte>  pStack = new Stack<Byte>(2);
        Stack< String> fStack = new Stack<String>(2);
        VariableMap vmap;
        BuildContext bc;
        int pushlvReqMode = 0;

        Stack<Byte> fcallArgCntrStack = new Stack<Byte>(2);
        byte fcallArgCntr = 0;

        boolean zeroOpEnabled = false;

        public ExpressionCompilingContext(VariableMap vmap, BuildContext bc) {
            this.vmap = vmap;
            this.bc = bc;
        }

        void pushOperand( String token ) {
            oStack.push( token );
            if ( zeroOpEnabled && token != null ) {
                wStack.push((byte)0);
                pStack.push((byte) 125);
                zeroOpEnabled = false;
            }
        }

        void pushOperator( byte opCode, byte prio ) {
            if ( ! wStack.isEmpty() ) {
                if ( wStack.peek() == 0 ) {
                    wStack.pop();
                    pStack.pop();
                }
            }

            wStack.push( opCode );
            pStack.push( prio );
        }

        void pushParenthesis() {
            if ( !wStack.isEmpty() && wStack.peek() == Def.OP_F_CALL )
                pushlvReqMode++;
            wStack.push( (byte) -1 );
            pStack.push( (byte) 126 );
//            oStack.push( null );
            zeroOpEnabled = true;
            fcallArgCntrStack.push( fcallArgCntr );
            fcallArgCntr = 0;
        }

        boolean hasOperators() {
            return !wStack.isEmpty() && wStack.peek() != 0;
        }

        byte prevPrio() {
            if ( pStack.isEmpty() )
                return 127;
            return pStack.peek();
        }

        byte prevOperatorCode() {
            if ( wStack.isEmpty() )
                return 0;
            return wStack.peek();
        }

        void dumpStacks() {
            if ( verboseLevel < 3 )
                return;
            System.out.print("ExpressionCompilingContext. dumpStacks:\n*...oStack[ ");
            for ( int i = 0; i < oStack.size ; i++ ) {
                System.out.print(oStack.get( i ));
                if ( i == oStack.size - 1 )
                    break;
                System.out.print(" | ");
            }
            System.out.print(" ]\n*...wStack[ ");
            for ( int i = 0; i < wStack.size ; i++ ) {
                System.out.print( getOpName( wStack.get( i ) ) + "(" + pStack.get(i) + ")" );
                if ( i == wStack.size - 1 )
                    break;
                System.out.print(" | ");
            }
            System.out.print(" ]\n");
        }
    }

    static boolean compileExpression(Array<String> expression, VariableMap vmap, BuildContext bc ) {
        byte prevOpCode = 0;

        ExpressionCompilingContext ecc = new ExpressionCompilingContext( vmap, bc);

        for ( int i = 0; i < expression.size; i++ ) {
            String token = expression.get(i);
            if ( token.isEmpty() )
                continue;
            boolean fcall = ( fmapContainsName(bfuncMap, token) || fmapContainsName(bc.funcMap, token) );
            if ( verboseLevel > 3 )
                printMsg("  cE. token: " + token, bc);

            if ( prevOpCode == Def.OP_F_CALL && !token.equals("(") ) {
                printError("Buidl.compileExpression: \'(\' expected when calling function.", bc);
                return false;
            }

            if ( operators.containsKey(token) || fcall ) {
                byte opCode;
                if ( !fcall )
                    opCode =  correctOpCodeToUnary( prevOpCode, operators.get(token) );
                else
                    opCode = Def.OP_F_CALL;
                if ( verboseLevel > 1 )
                    printMsg("  cE. opCode: " + getOpName( opCode) + " prevOpCode: " + getOpName( prevOpCode ), bc);

                if ( Def.isLvalueAsVarRequired( opCode ) && ecc.prevOperatorCode() == Def.OP_GET_PROP ) {
                    ecc.wStack.pop();
                    ecc.wStack.push( Def.OP_GET_PROP_REF );
                }

                prevOpCode = opCode;

                byte prio = Def.getOpPrior(opCode);

                byte prevPrio = ecc.prevPrio();
                if ( prio >= prevPrio ) {
                    if ( ! collapseStacks( ecc, prio ) )
                        return false;
                }

                ecc.pushOperator(opCode, prio);

                if ( fcall ) {
                    if ( token.equals("init") || token.equals("run") ) {
                        printError("compileExpression. \"" + token + "\"" +
                        " function cannot be called from the code", bc);
                        return false;
                    }
                    ecc.fStack.push(token);
                    if ( verboseLevel > 1)
                        printMsg("  cE. _push operator: _fcall <" + token + "> prio: " + prio + " prevPrio: " + prevPrio, bc);
                } else if ( verboseLevel > 1 ) {
                    printMsg("  cE. _push operator: " + getOpName(opCode) + " (" + opCode + ")" +
                            " prio: " + prio + " prevPrio: " + prevPrio, bc);
                }
                continue;
            }

            if (token.equals("(") ) {
                ecc.pushParenthesis();
                prevOpCode = 0;
                if ( verboseLevel > 1)
                    printMsg("  cE. push \'(\' (push op -1)", bc);
                continue;
            }

            if ( token.equals(")") ) {
                if ( verboseLevel > 1 )
                    printMsg("  cE. parse \')\'", bc);
                if ( ! collapseStacks( ecc, (byte) 126 ) )
                    return false;
                prevOpCode = Def.DTS_NUMBER;
                continue;
            }

            ecc.pushOperand( token );
            if ( verboseLevel > 1 )
                printMsg("  cE. push token: " + token + " oS:" + ecc.oStack.size, bc);
            prevOpCode = Def.DTS_NUMBER;
        }
        return collapseStacks( ecc, (byte) 127);
    }

    static boolean collapseStacks( ExpressionCompilingContext ecc, byte targetPrio ) {
        if ( verboseLevel > 1 )
            printMsg("      cS>. Start. target prio: " + targetPrio, ecc.bc);
        ecc.dumpStacks();
//        int codeSize = ecc.bc.bytes.size;

        while ( !ecc.wStack.isEmpty() ) {
            byte prio = ecc.pStack.peek();
            if ( prio >  targetPrio ) {
                if ( verboseLevel > 1 )
                    printMsg("      cS>. Finish (prio compared)", ecc.bc);
                ecc.dumpStacks();
                return true;
            }

            // collapse  ( ... ) expression
            if ( prio == 126 && targetPrio == 126 ) {
                // pop operator: -1 with priority: 126
                ecc.wStack.pop();
                ecc.pStack.pop();
                if ( verboseLevel > 1 )
                    printMsg("      cS>. Finish _pop op: (" + " prio: " + prio, ecc.bc);
                ecc.dumpStacks();
                return true;
            }

            ecc.pStack.pop();
            byte opCode = ecc.wStack.pop();
            if ( verboseLevel > 1 )
                printMsg("      cS>. _pop op: " + getOpName(opCode) + " prio: " + prio, ecc.bc );

            //all operators have an rvalue, except function call ;
            if ( opCode != Def.OP_F_CALL ) {
                if ( ! setupValue( ecc, 1 ) )
                    return false;
            }

            //only non unary operators work with lvalue. ( function call is a like-unary operator. )
            if ( ! Def.isUnaryOperator(opCode) && opCode != 0 ) {
                if ( ! setupValue( ecc, 0 ) )
                    return false;
            }

            if ( opCode == Def.OP_COMMA ) {
                ecc.bc.putOp( Def.PUSHLV );
                ecc.bc.putOp( Def.RVTOLV );
                if (ecc.fcallArgCntr == 0)
                    ecc.fcallArgCntr = 1;
                ecc.fcallArgCntr++;
            } else if ( opCode == 0 ) {
                ecc.bc.putOp( Def.PUSHRV );
                if (ecc.fcallArgCntr == 0)
                    ecc.fcallArgCntr = 1;
            } else {
                ecc.bc.putOp(opCode);
            }


            if ( opCode == Def.OP_F_CALL ) {
                String fname = ecc.fStack.pop();
                int addr = -16500;
                int fid = getFunctionId( fname, ecc.fcallArgCntr );
                if ( bfuncMap.containsKey( fid ) ) {
                    addr = bfuncMap.get( fid ).address;
                } else if ( ecc.bc.funcMap.containsKey( fid ) ) {
                    FunctionDesc fdes = ecc.bc.funcMap.get( fid );
                    addr = fdes.address;
                    if ( addr < 0 )
                        ecc.bc.fcallAddrStack.push( new Triple<String, Byte, Integer>( fname, ecc.fcallArgCntr, ecc.bc.bytes.size ) );
                } else {
                    printError("collapseStacks. Undefined function call: " + fname + "( number of arguments: " +
                    ecc.fcallArgCntr + ")", ecc.bc);
                    return false;
                }
                if ( verboseLevel > 1 )
                    printMsg("      cS>. fcall: " + fname + " addr: " + addr + " arg cntr: " + ecc.fcallArgCntr, ecc.bc);
                putInt(addr, ecc.bc.bytes);
                ecc.bc.bytes.add( ecc.fcallArgCntr );

                if ( ecc.hasOperators() && !ecc.bc.isFuncReturnable( fname, ecc.fcallArgCntr ) ) {
                    printError("collapseStacks. Usage of none-returnable function: "
                            + fname + " in expression.", ecc.bc);
                    return false;
                }

                // function without arguments, but it returns a value maybe
                if ( ecc.fcallArgCntr == 0 ) {
                    ecc.pushOperand( null );
                    if ( verboseLevel > 1 )
                        printMsg("      cS>. *' push null." + " oStackSize: " + ecc.oStack.size, ecc.bc);
                }

                ecc.bc.lastCalledFunc = fname;
                ecc.pushlvReqMode--;
                ecc.fcallArgCntr = ecc.fcallArgCntrStack.pop();
            } else {
                if ( verboseLevel > 1 )
                    printMsg("      cS>. finish op collapse: " + Dumper.getOpCodeStr(opCode) +
                             " pushlvReqMode: " + ecc.pushlvReqMode + " wS.s " + ecc.wStack.size, ecc.bc);
                ecc.pushOperand(null);
                if ( verboseLevel > 1 )
                    printMsg("      cS>. *  push null." + " oStackSize: " + ecc.oStack.size, ecc.bc);
                ecc.dumpStacks();
            }
            // zero operator makes this operation with the rvalue
            if ( opCode != 0 ) {
                ecc.bc.putOp(Def.PUSHLV);
                if (ecc.fcallArgCntr == 0)
                    ecc.fcallArgCntr = 1;
            }

        }

        if ( targetPrio < 127) {
            if ( verboseLevel > 1 )
                printMsg("      cs. Finish.  ", ecc.bc);
            ecc.dumpStacks();
            return true;
        }
        // clean up oStack after the final collapse
        if ( !cleanUpOStack( ecc ) )
            return false;
        if ( verboseLevel > 1 )
            printMsg("      cs. Finish (final).  ", ecc.bc);
        ecc.dumpStacks();
//        printMsg("cS>. " + ecc.bc.bytes.toString(" "), ecc.bc);
        return true;
    }

    // value codes: 0 1 2:   0 - lvalue, 1 - rvalue, 2 - xvalue
    static public boolean setupValue( ExpressionCompilingContext ecc, int valueCode ) {

        byte popOpCode = Def.POPRV;
        byte setOpCode = Def.SETRV;
        String msgVal = "rvalue";

        if ( valueCode == 0 ) {
            popOpCode = Def.POPLV;
            setOpCode = Def.SETLV;
            msgVal = "lvalue";
        }

        if ( ecc.oStack.isEmpty() ) {
            printError("setupValue. " + msgVal + " required", ecc.bc);
            return false;
        }

        String val = ecc.oStack.pop();
        if ( verboseLevel > 1 )
            printMsg("              setupValue. pop " + msgVal + ": " + val + " oS:" + ecc.oStack.size +
                        " pushlvReqMode: " + ecc.pushlvReqMode, ecc.bc );
        if ( val != null ) {
            ecc.bc.putOp( setOpCode );
            if ( ! putVal( val, ecc, ( ecc.pushlvReqMode > 0 ), (valueCode == 0), ecc.bc ) )
                return false;
        } else {
            ecc.bc.putOp( popOpCode );
        }
        return true;
    }

    static public boolean cleanUpOStack( ExpressionCompilingContext ecc ) {
        while ( ! ecc.oStack.isEmpty() ) {
            String token = ecc.oStack.pop();
            if ( verboseLevel > 1 )
                printMsg("              cleanUpOStack>. pop: " + token + " oS:" + ecc.oStack.size , ecc.bc );
            if ( token == null ) {
//                ecc.bc.putOp(ScriptCodes.PUSHLV );
                return true;
            }
            ecc.bc.putOp(Def.SETLV );
            if ( ! putVal( token, ecc, ( ecc.pushlvReqMode > 0 ), true, ecc.bc ) )
                return false;
//            ecc.bc.putOp(ScriptCodes.OBTAINLV );
        }
        return true;
    }

    static boolean putVal( String token, ExpressionCompilingContext ecc, boolean obtainVar,
                           boolean leftValue, BuildContext bc ) {

        if ( token.equals("true") ) {
            ecc.bc.putOp(Def.DTS_BOOL);
            ecc.bc.putOp((byte) 1);
            return true;
        }

        if ( token.equals("false") ) {
            ecc.bc.putOp(Def.DTS_BOOL );
            ecc.bc.putOp((byte) 0);
            return true;
        }

        if ( token.equals("null")) {
            ecc.bc.putOp(Def.DTS_NULL);
            return true;
        }

        if ( properties.containsKey( token ) ) {
            ecc.bc.putOp(Def.DTS_PROP_CODE);
            putInt( properties.get( token), ecc.bc.bytes);
            return true;
        }

        if ( dataTypeSpec.containsKey( token ) ) {
            ecc.bc.putOp(Def.DTS_TYPE);
            ecc.bc.putOp(dataTypeSpec.get(token ) );
            return true;
        }

        if ( token.charAt(0) == '\"' ) {
            if ( token.length() == 2 )
                ecc.bc.strings.add("");
            else
                ecc.bc.strings.add( token.substring( 1, token.length() - 1) );
            ecc.bc.putOp(Def.DTS_STRING);
            putInt(ecc.bc.strings.size - 1, ecc.bc.bytes);
            return true;
        }

        if ( ecc.vmap.isVariable( token ) ) {
            if ( ! putVariable(token, ecc, false) )
                return false;
            if ( obtainVar )
                ecc.bc.putOp( (leftValue) ? Def.OBTAINLV : Def.OBTAINRV );
            return true;
        }

        if ( ecc.bc.rmap.isVariable( token )  ) {
            if ( ! putVariable(token, ecc, true) )
                return false;
            if ( obtainVar )
                ecc.bc.putOp( (leftValue) ? Def.OBTAINLV : Def.OBTAINRV );
            return true;
        }

        if ( isWord( token ) ) {
            printError("putVal. Undefined token: " + token, bc);
            return false;
        }

        try {
            float n = Float.valueOf( token );
            ecc.bc.putOp(Def.DTS_NUMBER);
            putFloat( n , ecc.bc.bytes );
            return true;
        } catch ( NumberFormatException e ) {
            e.printStackTrace();
            return false;
        }
    }

    static boolean putVariable(String name, ExpressionCompilingContext ecc, boolean global) {
        int idx = -1;
        byte flag = Def.DTS_VAR;

        if ( ! global ) {
            idx = ecc.vmap.getIndex( name );
        }

        if ( idx < 0 ) {
            idx = ecc.bc.rmap.getIndex( name );
            flag = Def.DTS_REG;
        }

        if ( idx < 0 ) {
            printError("putVariable. Undefined variable: " + name, ecc.bc);
            return false;
        }
        ecc.bc.putOp(flag);
        putInt(idx, ecc.bc.bytes);

        return true;
    }

    static byte correctOpCodeToUnary(byte prevOpCode, byte opCode) {
        if ( isLValue( prevOpCode ) )
            return opCode;
        if ( opCode == Def.OP_ADD ) {
            return Def.OP_U_ADD;
        } else if ( opCode == Def.OP_SUB ) {
            return Def.OP_U_SUB;
        }

        return opCode;
    }

    static boolean isLValue( byte prevOpCode ) {
        return Def.isDts(prevOpCode);
    }

    static byte mergeOpCodes(byte first, byte second ) {
            switch ( first ) {
                case Def.OP_NOT:
                    if ( second == Def.OP_ASSIGN )
                        return Def.OP_NOT_EQ;
                    break;
                case Def.OP_ASSIGN:
                    if ( second == Def.OP_ASSIGN )
                        return Def.OP_EQ;
                    break;
                case Def.OP_LESS:
                    if ( second == Def.OP_ASSIGN )
                        return Def.OP_LOEQ;
                    break;
                case Def.OP_GRT:
                    if ( second == Def.OP_ASSIGN )
                        return Def.OP_GOEQ;
                    break;
            }
        return second;
    }

    static String getOpName( byte opCode ) {
        if ( opCode == Def.OP_F_CALL )
            return "_fcall";
        for ( String key : operators.keySet() ) {
            if ( opCode == operators.get( key ) )
                return key;
        }
        if ( opCode == Def.OP_GET_PROP_REF )
            return ".\'";
        return "_" + opCode ;
    }

    static String getDtsString( byte dts ) {
        for ( String key : dataTypeSpec.keySet() ) {
            if ( dts == dataTypeSpec.get(key ) )
                return key;
        }
        switch ( dts ) {
            case Def.DTS_VAR:
                return "VAR";
            case Def.DTS_REG:
                return "REG";
        }
        return "DTS?" + dts;
    }
}
