package org.skr.SkrScript;

/**
 * Created by rat on 22.11.14.
 */
public class Def {

    // opCodes and  priorities


    protected static final int PRIO_MIN             = 1; //


    protected static final byte OP_GET_PROP        = 1; // get property operator: obj.prop
    protected static final byte OP_GET_PROP_REF    = 2; // get property reference operator: obj.prop


    protected static final int LIM_UNARY_MIN      = 4; //
    protected static final int PRIO_A             = 4; //

    protected static final byte OP_NOT             = 4; //  ! operator
    public static final byte OP_U_PLUS          = 5; // unary + operator
    public static final byte OP_U_MINUS         = 6; // unary - operator
    protected static final byte OP_TYPEOF          = 7; // sets dts of rval to lval

    protected static final byte OP_F_CALL          = 8; // function call [ push environment, set new environment, jump ]


    protected static final int LIM_UNARY_MAX      = 9; //
    protected static final int PRIO_B             = 10; //

    public static final byte OP_DIV             = 10; //  / operator
    public static final byte OP_MUL             = 11; //  * operator
    public static final byte OP_MOD             = 12; //  * operator


    protected static final int PRIO_C             = 13; //

    public static final byte OP_ADD             = 13; //  + operator
    public static final byte OP_SUB             = 14; //  - operator

    protected static final int PRIO_D             = 15; //

    public static final byte OP_LESS            = 15; //  < operator
    public static final byte OP_GRT             = 16; //  > operator
    public static final byte OP_LOEQ            = 17; // <= operator
    public static final byte OP_GOEQ            = 18; // >= operator


    protected static final int PRIO_E             = 19; //

    protected static final byte OP_NOT_EQ          = 19; // != operator
    protected static final byte OP_EQ              = 20; // == operator


    protected static final int PRIO_F             = 21; //

    protected static final byte OP_AND             = 21; // & operator

    protected static final int PRIO_G             = 22; //

    protected static final byte OP_OR              = 22; // | operator

    protected static final int PRIO_H             = 23; //

    protected static final byte OP_ASSIGN          = 23; // = operator

    protected static final int PRIO_I             = 24; //

    protected static final byte OP_COMMA           = 24; // , operator(virtual). Builder converts it to "push right value"

    protected static final int PRIO_J             = 25;

    // engine commands

    protected static final byte CMD_OFFSET         = 30;

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
    protected static final byte DECVARNUM          = 40; // decrement environment.varNum
    protected static final byte OBTAINLV           = 41; // converts lval DTS_REG (DTS_VAR) to not Reg(Var) type or does nothing
    protected static final byte OBTAINRV           = 42; // converts rval DTS_REG (DTS_VAR) to not Reg(Var) type or does nothing
    protected static final byte LVTORV             = 43; // copies lvalue to rvalue
    protected static final byte RVTOLV             = 44; // copies rvalue to lvalue
    protected static final byte POPVAR             = 45; // pop data to var
    protected static final byte PUSHLVRV           = 46; // push lval and rval
    protected static final byte LVTORVPOPLV        = 47; // copies lval to rval and pop lval
    protected static final byte RVTOVAR            = 48; // set rval to var or reg



    // data type specifiers

    public static final byte DTS_NULL           = 0; // null data
    public static final byte DTS_REG            = -1; // index of ScriptContext registry
    public static final byte DTS_VAR            = -2; // index of ScriptRunContext registry ( variable )
    public static final byte DTS_INT            = -3; // Integer value
    public static final byte DTS_FLOAT          = -4; // Float value
    public static final byte DTS_BOOL           = -5; // bool value
    public static final byte DTS_STRING         = -6; // index of a string in the Script.string array
    public static final byte DTS_TYPE           = -7; // data type specifier
    public static final byte DTS_PROP_CODE      = -8; // property code
    public static final byte DTS_PROP_REF       = -9; // property reference


    public static final byte EXTENDED_DTS_CODE = -10; // top code for extended data types


    // build-in function addresses

    public static final int FUNCTIONS_START_ADDR = -1;

    protected static final int F_MSG                 = - 1; // send message to the std.out
    protected static final int F_ERR                 = - 2; // send message to the std.err
    protected static final int F_SET_SLOT_ENABLED    = - 3; // send message to the std.err
    protected static final int F_TYPE_TO_STR         = - 4; // convert value of DTS_TYPE to Strings


    protected static final int F_SIN                 = -5;
    protected static final int F_COS                 = -6;
    protected static final int F_TAN                 = -7;
    protected static final int F_ACOS                = -8;
    protected static final int F_ASIN                = -9;
    protected static final int F_ATAN                = -10;
    protected static final int F_CBRT                = -11;
    protected static final int F_SQRT                = -12;
    protected static final int F_EXP                 = -13;
    protected static final int F_HYPOT               = -14;
    protected static final int F_POW                 = -15;
    protected static final int F_LOG                 = -16;
    protected static final int F_LOG10               = -17;
    protected static final int F_MAX                 = -18;
    protected static final int F_MIN                 = -19;
    protected static final int F_RANDOM              = -20;
    protected static final int F_TO_DEGREES          = -21;
    protected static final int F_TO_RADIANS          = -22;
    protected static final int F_ABS                 = -23;
    protected static final int F_FLOOR               = -24;
    protected static final int F_CEIL                = -25;
    protected static final int F_ROUND               = -26;


    protected static final int F_GET_DUMP_ENV_STR = -27;

    public static final int EXTENDED_BUILD_IN_FUNCTION_ADDRESS = -100; // top address for extended build-in functions;

    // aid functions

    protected static boolean isDts( byte code ) {
        return  code <= 0;
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
