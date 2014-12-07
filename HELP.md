# Skr Script Engine

 This script engine was written to be used in my projects, so it has some special features.
 The basic requirement: work as fast as possible. In my graphical applications the script should be executed
 dozen of times per frame.

    (For now time I don't know yet: is it fast enough or not :(  )


##   Engine features:

 * executing in byte-code representation;
 * Assembler style low-level commands set;
 * no memory consumption while executing;
 * data types and build-in functions extensibility;


# Engine conceptions

## Script representation

 The SkrScript code has two representations:

  1. High-level programming language.
     It's the way to write the script source code ( let's call it the source code). The source code should be build to
     low-level byte code before it could be executed. Most of the source structure elements exist only at the
     high-level, e.g  loops, functions, defines etc.

  2. Low-level byte-code.
     The engine executes the low-level byte code ( let's call it the byte code or the script). Byte code is the set of
     Assembler style commands and script data saved as the byte array and string array( for string data ).

## Engine structure

### Engine elements

   The Script engine operates with next elements:
     1. Script - contains the byte code, hard coded strings array, entry points and registers requirements (see below).
        Each script source should define two functions: init() and run(). So the script has two entry points.
     2. Slot - contains the script and register set (also see below). One script could be set on different slots. So you
        can execute one code with different register set. Next the slot could be enabled or disabled.
     3. Engine - executes the slot i.e it executes the script of the slot with the slot register set and if the slot is
        enabled. Engine starts executing from init() function or from run() function. At your wish.

### Engine memory model

   There are two groups of variables available for the Script. The Registers and the Variables (Let's call it Regs and
   Vars). Each of them can save you data at runtime as the usual variables. Regs and vars are the same for usage.
   The difference is in place where those ones exist.
   The Regs are the variables  which placed at the slot. So it's possible to execute the script many
   times at the slot and regs will save script data from run to run. In other words the regs are the global
   variables. The reg can be defined in the inter-functions space and in the init() function body.
   The Vars are the variables which placed at the engine runtime context. It's possible to use the Vars only at runtime.
   One Engine shares one vars set between all the scripts and slots engine executes. Of course it's possible to use
   the Vars as global variables but it's not guaranteed that the var content will not be overwritten by the other
   script. So the Vars are the local variables. The var can be defined only in a function body.

   In addition the Engine has another memory elements. The rvalue and lvalue, the stack and the property reference.
   See the the engine extension description.

### init() and run() functions

   As written above the engine starts to execute the script from init() or from run() function. The idea of this
   conception is to use the init() function for the first time slot initialization and to use the run() function each
   time you need the script to execute. init() and run() functions could not be called from any other functions.
   So the reg can be defined only in the inter-functions (global) space and in the init() function.

# High-level script language.

   The language allows you to write the script code. It uses syntax which looks like a c-style.
   Let's call the HSLS text as the source.

## The structure of the source

   Any source can contains next elements:
   1. The reg definitions.
   2. The define macros.
   3. The function declarations.
   4. The function definitions.

   Each source must contains two functions: init() and run(). Those functions has no arguments and are not returnable.

### The structure of the function

   Any function can contains:
   1. The var definitions (init() can contain the reg definitions).
   2. Expressions: the syntax ordered set of the numbers, strings, variables, function calls, operators and so on.
   3. 'if-else' structures.
   4. Loops.
   5. 'return' keywords.
   6. Sections: the block of code placed between { }. The function body also should be defined as a section.

## The syntax

### Keywords

   Next words are the keywords:

   * function
   * return
   * for
   * while
   * continue
   * break
   * if
   * else
   * typeof
   * null
   * true
   * false
   * returnable
   * define

   The keywords could no be used as variable or function name and as the define macro

### Data types

   There are some data types. You cannot specify a data type of the var or the reg. They takes it automatically when
   takes the value.

   The engine basically supports next data types (the list of data type specifiers):

   * [null] - variable contains nothing.
   * [bool] - boolean value.
   * [string] - string value.
   * [number] - float number (no integer values supported).
   * [type] - data type specifier.
   * [property] - the property identifier.

### Operators

  The list of operators: * opSymbol - description. ( supported data types )

#### Unary operators

   * [+] - unary plus - just for fun. Does nothing.
   * [-] - unary minus (number).
   * [!] - not. (bool)
   * [typeof] - returns data type of operand.

#### Binary operators

   * [.] - get property (left: any object with properties, right: property identifier ).
   * [=] - assignment. Left operand must be a variable or get property operator combination (works as 'set property').
   * [,] - count arguments in function calls or divides var definitions.
   * [+] - addition (number, string).
   * [-] - subtraction (number).
   * [*] - multiplication (number).
   * [/] - division (number).
   * [>] - greater than (number).
   * [>=] - greater than or equal (number)
   * [<] - less then (number).
   * [<=] - less then or equal (number).
   * [==] - equals. (any type ).
   * [!=] - not equal ( any type).

   Operators: [+], [-], [*], [/], [>], [>=], [<], [<=] can be used with extended data types throw the engine extension.
   Operators: [==] and [!=] use java ".equals()" function inside.

### The reg and var definition:

  Reg definition:  [ reg name1, name2, ..., nameN; ]
  Var definition:  [ var name1, name2, ..., nameN; ]

  Reg-type variables could be defined at the inter-functions space or inside of the body of the init() function.
  Var-type variables could be defined inside of a function.

  Variable could be initialized by expression at the same time of definition.

  Reg definition and initialization (inside init() function only ): [ reg name = 'expression'; ]
  Var definition and initialization: [ var name = 'expression'; ]

  Any combinations of definition and initialization are possible in next way:
  e.g.:  [ var name1 = 'expression', name2, name3 = 'expression', ..., nameN; ]

###  Define macro syntax

  Define macro can be set only outside the functions and before it will be used

  Syntax:  [ define macroName someText ]

   define - keyword
   macroName - any word
   someText - Some text without space (e.g. number) or string ( text with quotes )

 Engine builder just makes replacement of macroName to someTex at all macroName occurence found in code below.

### Function declaration and definition

   Any function must be declared and defined or defined only.
   Function must be declared or defined above the code where the function will be called.
   Function is identifying by it's name and number of arguments. So it's possible to define different functions with
   same name but with different number of arguments.

#### Declaration syntax

   [ function name ( [argument list]  ) [returnable] ;   ]

   [ name ] -  name of the function.
   [ returnable ] - function must returns a value.
   [ argument list ] - list of arguments divided by comma.

   Argument is the name of variable (it will be defined automatically).

   Examples:
   function with no arguments and no returns: [ function reset();  ]
   function with 3 arguments and returnable:  [ function getWeight( x, y, z) returnable; ]

#### Definition syntax

  [ function name ( [argument list]  ) [returnable]  { 'section: function body' }   ]

### Section syntax

   Section is a block of code between { } curly brackets.

### Expression syntax

  Expression is a set of operators and operands.
  Expression is evaluating in a normal math order depends on operator priorities.
  Also expression can contains round parenthesis and function calls.
  Expressions can contain no operators.
  Expression cannot contain no operands or function calls.

  As operand could be used:
   * numbers
   * variables (Var-type or Reg-type )
   * strings ( text with quotes )
   * property identifiers
   * 'true' or 'false' keywords
   * data type specifiers

  The data type of the result that expression produce depends on used operators, called functions and so on.

  Stand alone expression ( the expression which is placed right in a section ) should be ended by ';' symbol.

#### Function call syntax

  [ funcName( [ list of arguments ] ) ]

  List of arguments: number of expressions divided by commas. The result of each expression will be sent to the function
  as the argument. Number of arguments and function name identify the calling function. Number of arguments could be
  equal to zero.

#### Expressions examples

  [ a = 4; ]
  [ b = 10 + a - mul( 8, 6 ) + (3 - 7*8) +2; ]
  [ msg( "v = " + v + " is a " + typeStr( v ) ); ]
  [ q = vec2( 0.1e-10, 1); ]

### 'if-else' syntax

   [ if (  'expression that produces the boolean '  ) { 'block of code for the true condition' }   ]
   [ else { 'block of code for the false condition' } ]

   [ else { ... } ] is not required.

   Combination as a  [ if() {...} else if() {...} else {...} ] is also possible.

### 'for' loop

  [ for( 'initial expression'; 'condition expression'; 'iteration expression' ) { 'section: loop body'}  ]

  keywords: break and continue are available to use.

###  'while' loop
  [ while( 'condition expression' ) { 'section: loop body' } ]

  keywords: break and continue are available to use.

### build-in functions

  There are a number of build-in functions ( shown expected data type specifiers as the arguments ):

  * msg( string ) : outputs string to the std out.
  * err( string ) : outputs string to the std error
  * setSlotEnabled( bool ) : sets slot enabled or disabled
  * typeStr( any data type ) returnable : returns data type specifier of the arg as a string.
  * sin( number ) returnable : returns number as a sinus of the arg.
  * cos( number ) returnable : returns number as a cosine of the arg.
  * tan( number ) returnable : returns number as a tangent of the arg.
  * acos( number ) returnable : returns number as an arc cosine of the arg.
  * asin( number ) returnable : returns number as an arc sinus of the arg.
  * atan( number ) returnable : returns number as an arc tangent of the arg.
  * cbrt( number ) returnable : returns number as a cube root of the arg.
  * sqrt( number ) returnable : returns number as a square root of the arg.
  * exp( number ) returnable : returns number as an exponent of the arg.
  * hypot( number, number ) returnable : returns a hypotenuse where args are the legs.
  * pow( number, number ) returnable : returns number as a 1'st arg in a pow of 2'd arg.
  * log( number ) returnable : returns number as a natural logarithm of arg.
  * log10( number ) returnable : returns number as a logarithm in base of 10 of the arg.
  * max( number, number ) returnable : returns number as the maximum of the args.
  * min( number, number ) returnable : returns number as the minimum of the args.
  * random( number ) returnable : returns number as a random value between 0 and 1.
  * toDeg( number ) returnable : returns number as a result of conversion from rad to degree.
  * toRad( number ) returnable : returns number as a result of conversion from degree to rad.
  * abs( number ) returnable : returns number as an absolute value of the arg.
  * floor( number ) returnable : returns number as a lowest integer value of the arg.
  * ceil( number ) returnable : returns number as a nearest high integer value of the arg.
  * round( number ) returnable : returns number as a rounded to integer value of the arg.

# API

TODO: write it.
