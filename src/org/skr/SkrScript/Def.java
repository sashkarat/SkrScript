package org.skr.SkrScript;

/**
 * Created by rat on 22.11.14.
 */
public class Def {

    // operations and  priorities

    protected static final byte EOE                = 0; // end of expression



    protected static final int PRIO_MIN             = 1; //


    protected static final byte OP_GET_PROP        = 1; // get property operator: obj.prop
    protected static final byte OP_GET_PROP_REF    = 2; // get property reference operator: obj.prop


    protected static final int LIM_UNARY_MIN      = 4; //
    protected static final int PRIO_A             = 4; //

    protected static final byte OP_NOT             = 4; //  ! operator
    protected static final byte OP_U_ADD           = 5; // unary + operator
    protected static final byte OP_U_SUB           = 6; // unary - operator
    protected static final byte OP_TYPEOF          = 7; // sets dts of rval to lval

    protected static final byte OP_F_CALL          = 8; // function call [ push environment, set new environment, jump ]


    protected static final int LIM_UNARY_MAX      = 9; //
    protected static final int PRIO_B             = 10; //

    public static final byte OP_DIV             = 10; //  / operator
    public static final byte OP_MUL             = 11; //  * operator

    protected static final int PRIO_C             = 12; //

    public static final byte OP_ADD             = 12; //  + operator
    public static final byte OP_SUB             = 13; //  - operator

    protected static final int PRIO_D             = 14; //

    public static final byte OP_LESS            = 14; //  < operator
    public static final byte OP_GRT             = 15; //  > operator
    public static final byte OP_LOEQ            = 16; // <= operator
    public static final byte OP_GOEQ            = 17; // >= operator


    protected static final int PRIO_E             = 18; //

    protected static final byte OP_NOT_EQ          = 18; // != operator
    protected static final byte OP_EQ              = 19; // == operator


    protected static final int PRIO_F             = 20; //

    protected static final byte OP_AND             = 20; // & operator

    protected static final int PRIO_G             = 21; //

    protected static final byte OP_OR              = 21; // | operator

    protected static final int PRIO_H             = 22; //

    protected static final byte OP_ASSIGN          = 22; // = operator

    protected static final int PRIO_I             = 23; //

    protected static final byte OP_COMMA           = 23; // , operator(virtual). Builder converts it to "push right value"

    protected static final int PRIO_J             = 24;

    // engine commands

    protected static final byte SETRV              = 30; // set data to right value
    protected static final byte SETLV              = 31; // set data to left value
    protected static final byte POPRV              = 32; // pop data from stack to right value
    protected static final byte PUSHRV             = 33; // push data from right value to stack
    protected static final byte POPLV              = 34; // pop data from stack to left value
    protected static final byte PUSHLV             = 35; // push data from left value to stack
    protected static final byte JUMP               = 36; // jump to position
    protected static final byte JUMPF              = 37; // jump if left value is boolean and false
    protected static final byte RET                = 38; // jump  to retCode position. pop environment.
    protected static final byte INCVARNUM          = 39; // increment environment.varNum
    protected static final byte OBTAINLV           = 40; // converts lval DTS_REG (DTS_VAR) to not Reg(Var) type or does nothing
    protected static final byte OBTAINRV           = 41; // converts rval DTS_REG (DTS_VAR) to not Reg(Var) type or does nothing
    protected static final byte LVTORV             = 42; // copies lvalue to rvalue
    protected static final byte RVTOLV             = 43; // copies rvalue to lvalue


    // data type specifiers

    protected static final byte DTS_NULL           = -8; // null data
    protected static final byte DTS_REG            = -9; // index of ScriptContext registry
    protected static final byte DTS_VAR            = -10; // index of ScriptRunContext registry ( variable )
    protected static final byte DTS_NUMBER         = -11; // float number
    protected static final byte DTS_BOOL           = -12; // bool value
    protected static final byte DTS_STRING         = -13; // index of a string in the Script.string array
    protected static final byte DTS_TYPE           = -14; // data type specifier
    protected static final byte DTS_PROP_CODE      = -15; // property code
    protected static final byte DTS_PROP_REF       = -16; // property reference


    public static final byte EXTENDED_DTS_CODE = -20; // top code for extended data types


    // build-in function addresses

    public static final int FUNCTIONS_START_ADDR = -30;

    protected static final int F_MSG                 = - 30; // send message to the std.out
    protected static final int F_ERR                 = - 31; // send message to the std.err
    protected static final int F_SET_SLOT_ENABLED    = - 32; // send message to the std.err
    protected static final int F_TYPE_TO_STR         = - 33; // convert value of DTS_TYPE to Strings


    protected static final int F_SIN                 = -34;
    protected static final int F_COS                 = -35;
    protected static final int F_TAN                 = -36;
    protected static final int F_ACOS                = -37;
    protected static final int F_ASIN                = -38;
    protected static final int F_ATAN                = -39;
    protected static final int F_CBRT                = -40;
    protected static final int F_SQRT                = -41;
    protected static final int F_EXP                 = -42;
    protected static final int F_HYPOT               = -43;
    protected static final int F_POW                 = -44;
    protected static final int F_LOG                 = -45;
    protected static final int F_LOG10               = -46;
    protected static final int F_MAX                 = -47;
    protected static final int F_MIN                 = -48;
    protected static final int F_RANDOM              = -49;
    protected static final int F_TO_DEGREES          = -50;
    protected static final int F_TO_RADIANS          = -51;
    protected static final int F_ABS                 = -52;
    protected static final int F_FLOOR               = -53;
    protected static final int F_CEIL                = -54;
    protected static final int F_ROUND               = -55;

    public static final int EXTENDED_BUILD_IN_FUNCTION_ADDRESS = -64; // top address for extended build-in functions;

    // aid functions

    protected static boolean isDts( byte code ) {
        return ( code <= -10 );
    }

    protected static  boolean isUnaryOperator(byte code) {
        return ( code >= LIM_UNARY_MIN && code <= LIM_UNARY_MAX );
    }

    protected static boolean isOperator( byte code ) {
        return (code >= PRIO_MIN && code < PRIO_J );
    }

    protected static byte getOpPrior( byte code ) {
        if ( code >= PRIO_MIN && code < PRIO_A )
        return PRIO_MIN;
        if ( code >= PRIO_A && code < PRIO_B )
            return PRIO_A;
        if ( code >= PRIO_B && code < PRIO_C )
            return PRIO_B;
        if ( code >= PRIO_C && code < PRIO_D )
            return PRIO_C;
        if ( code >= PRIO_D && code < PRIO_E )
            return PRIO_D;
        if ( code >= PRIO_E && code < PRIO_F )
            return PRIO_E;
        if ( code >= PRIO_F && code < PRIO_G )
            return PRIO_F;
        if ( code >= PRIO_G && code < PRIO_H )
            return PRIO_G;
        if ( code >= PRIO_H && code < PRIO_I )
            return PRIO_H;
        if ( code >= PRIO_I && code < PRIO_J )
            return PRIO_I;
        return PRIO_I;
    }

    protected static boolean isLvalueAsVarRequired(byte opCode ) {
        return ( getOpPrior( opCode) == PRIO_H );
    }

}
