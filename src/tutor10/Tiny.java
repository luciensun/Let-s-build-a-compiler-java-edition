package tutor10;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Part X: INTRODUCING "TINY"
 * 
 * @author lucienSun
 *
 */
public class Tiny {

    // Constant Declarations
    private final static char TAB = '\t';
    private final static char CR = '\r';
    private final static char LF = '\n';

    // Variable Declarations
    // Lookahead Character
    private char lookAhead;

    // Label Counter
    private int lCount;

    // symbol Table
    List<Character> symbolTable = null;

    // Here begins the code generation routines, we use it to retarget the cpu
    // Clear the Primary Register
    public void clear() {
        emitLn("CLR D0");
    }

    // Negate the Primary Register
    public void negate() {
        emitLn("NEG D0");
    }

    // Load a Constant Value to Primary Register
    public void loadConst(int constant) {
        emitLn("MOVE #" + constant + ",D0");
    }

    // Load a Variable to Primary Register
    public void loadVar(char variable) {
        if (!inTable(variable)) {
            undefined(String.valueOf(variable));
        }
        emitLn("MOVE " + variable + "(PC),D0");
    }

    // Push Primary onto Stack
    public void push() {
        emitLn("MOVE D0,-(SP)");
    }

    // Add Top of Stack to Primary
    public void popAdd() {
        emitLn("ADD (SP)+,D0");
    }

    // Subtract Primary from Top of Stack
    public void popSub() {
        emitLn("SUB (SP)+,D0");
        emitLn("NEG D0");
    }

    // Multiply Top of Stack by Primary
    public void popMul() {
        emitLn("MULS (SP)+,D0");
    }

    // Divide Top of Stack by Primary
    public void popDiv() {
        emitLn("MOVE (SP)+,D7");
        emitLn("EXT.L D7");
        emitLn("DIVS D0,D7");
        emitLn("MOVE D7,D0");
    }

    // Store Primary to Variable
    public void store(char variable) {
        if (!inTable(variable)) {
            undefined(String.valueOf(variable));
        }
        emitLn("LEA " + variable + "(PC),A0");
        emitLn("MOVE D0,(A0)");
    }

    // Report an Undefined Identifier
    public void undefined(String variable) {
        abort("Undefined Identifier " + variable);
    }
    
    // Complement the Primary Register
    public void notIt() {
        emitLn("NOT D0");
    }
    
    // AND Top of Stack with Primary
    public void popAnd() {
        emitLn("AND (SP)+,D0");
    }
    
    // OR Top of Stack with Primary
    public void popOr() {
        emitLn("OR (SP)+,D0");
    }

    // XOR Top of Stack with Primary
    public void popXor() {
        emitLn("EOR (SP)+,D0");
    }
    
    // Compare Top of Stack with Primary
    public void popCompare() {
        emitLn("CMP (SP)+,D0");
    }
    
    // Set D0 If Compare was =
    public void setEqual() {
        emitLn("SEQ D0");
        emitLn("EXT D0");
    }
    
    // Set D0 If Compare was !=
    public void setNEqual() {
        emitLn("SNE D0");
        emitLn("EXT D0");
    }
    
    // Set D0 If Compare was >
    public void setGreater() {
        emitLn("SLT D0");
        emitLn("EXT D0");
    }
    
    // Set D0 If Compare was < 
    public void setLess() {
        emitLn("SGT D0");
        emitLn("EXT D0");
    }
    
    // Read New Character From Input Stream
    public void getChar() {
        try {
            lookAhead = (char) System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Report an Error
    public void error(String str) {
        System.out.println("\nError: " + str + ".");
    }

    // Report Error and Halt
    public void abort(String str) {
        error(str);
        System.exit(1);
    }

    // Report What Was Expected
    public void expected(String str) {
        abort(str + " expected");
    }

    // Recognize an Alpha Character
    public boolean isAlpha(char c) {
        if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
            return true;
        } else {
            return false;
        }
    }

    // Recognize a Decimal Digit
    public boolean isDigit(char c) {
        if (c >= '0' && c <= '9') {
            return true;
        } else {
            return false;
        }
    }

    // Recognize an Alphanumeric Character
    public boolean isAlNum(char c) {
        if (isAlpha(c) || isDigit(c)) {
            return true;
        } else {
            return false;
        }
    }

    // Recognize an Addop
    public boolean isAddop(char c) {
        if (c == '+' || c == '-') {
            return true;
        } else {
            return false;
        }
    }

    // Recognize a Mulop
    public boolean isMulop(char c) {
        if (c == '*' || c == '/') {
            return true;
        } else {
            return false;
        }
    }
    
    // Recognize a Boolean Orop
    public boolean isOrop(char c) {
        if (c == '|' || c == '~') {
            return true;
        } else {
            return false;
        }
    }
    
    // Recognize a Relop
    public boolean isRelop(char c) {
        if (c == '=' || c == '#' || c == '<' || c == '>') {
            return true;
        } else {
            return false;
        }
    }

    // Look for Symbol in Table
    public boolean inTable(char variable) {
        return symbolTable.contains(variable);
    }

    // Recognize White Space
    public boolean isWhite(char c) {
        if (c == ' ' || c == TAB) {
            return true;
        } else {
            return false;
        }
    }

    // Skip Over Leading White Space
    public void skipWhite() {
        while (isWhite(lookAhead)) {
            getChar();
        }
    }

    // Match a Specific Input Character
    public void match(char x) {
        if (lookAhead == x) {
            getChar();
            skipWhite();
        } else {
            expected("'" + x + "'");
        }
    }

    // Skip a CRLF
    public void fin() {
        if (lookAhead == CR) {
            getChar();
        }
        if (lookAhead == LF) {
            getChar();
        }
        skipWhite();
    }

    // temporary getName
    public char getName() {
        char ch = lookAhead;
        if (!isAlpha(lookAhead)) {
            expected("Name");
        }
        getChar();

        return ch;
    }

    // Get a Number
    public int getNum() {
        int val = 0;
        if (!isDigit(lookAhead)) {
            expected("Number");
        }
        while (isDigit(lookAhead)) {
            val = val * 10 + (lookAhead - '0');
            getChar();
        }

        return val;
    }

    // Post a Label To Output
    public void postLabel(String label) {
        System.out.println(label + ":");
    }

    // Output a String with TAB
    public void emit(String str) {
        System.out.print(TAB + str);
    }

    // Output a String with TAB and CRLF
    public void emitLn(String str) {
        emit(str);
        System.out.println("");
    }

    // Allocate Storage for a Variable
    public void alloc(char variable) {
        if (inTable(variable)) {
            abort("Duplicate Variable Name " + variable);
        }
        symbolTable.add(variable);
        System.out.print(variable + ":" + TAB + "DC ");
        if (lookAhead == '=') {
            match('=');
            if (lookAhead == '-') {
                System.out.print(lookAhead);
                match('-');
            }
            System.out.println(getNum());
        } else {
            System.out.println("0");
        }
    }

    // Write Header Info
    public void header() {
        System.out.println("WARMST" + TAB + "EQU $A01E");
    }

    // Parse and Translate a Data Declaration
    public void decl() {
        match('v');
        alloc(getName());
        while (lookAhead == ',') {
            getChar();
            alloc(getName());
        }
    }

    // Parse and Translate Global Declarations
    public void topDecls() {
        while (lookAhead != 'b') {
            switch (lookAhead) {
            case 'v':
                decl();
                break;
            default:
                abort("Unrecognized Keyword '" + lookAhead + "'");
                break;
            }
        }
    }
    
    // Recognize and Translate a Relational "Equals"
    public void equals() {
        match('=');
        expression();
        popCompare();
        setEqual();
    }
    
    // Recognize and Translate a Relational "Not Equals" 
    public void notEquals() {
        match('#');
        expression();
        popCompare();
        setNEqual();
    }
    
    // Recognize and Translate a Relational "Less Than"
    public void less() {
        match('<');
        expression();
        popCompare();
        setLess();
    }

    // Recognize and Translate a Relational "Greater Than"
    public void greater() {
        match('>');
        expression();
        popCompare();
        setGreater();
    }
    
    // Parse and Translate a Relation
    public void relation() {
        expression();
        if (isRelop(lookAhead)) {
            push();
            switch(lookAhead) {
            case '=': equals();break;
            case '#':notEquals();break;
            case '<':less();break;
            case '>':greater();break;
            }
        }
    }
    
    // Parse and Translate a Boolean Factor with Leading NOT
    public void notFactor() {
        if (lookAhead == '!') {
            match('!');
            relation();
            notIt();
        }
    }
    
    // Parse and Translate a Boolean Term
    public void boolTerm() {
        notFactor();
        while (lookAhead == '&') {
            push();
            match('&');
            notFactor();
            popAnd();
        }
    }
    
    // Recognize and Translate a Boolean OR
    public void boolOr() {
        match('|');
        boolTerm();
        popOr();
    }
    
    // Recognize and Translate an Exclusive Or
    public void boolXor() {
        match('~');
        boolTerm();
        popXor();
    }
    
    // Parse and Translate a Boolean Expression
    public void boolExpression() {
        boolTerm();
        while (isOrop(lookAhead)) {
            push();
            switch(lookAhead) {
            case '|':boolOr();break;
            case '~':boolXor();break;
            }
        }
    }
    
    // Parse and Translate a Math Factor
    public void factor() {
        if (lookAhead == '(') {
            match('(');
            expression();
            match(')');
        } else if (isAlpha(lookAhead)) {
            loadVar(getName());
        } else {
            loadConst(getNum());
        }
    }

    // Parse and Translate a Negative Factor
    public void negFactor() {
        match('-');
        if (isDigit(lookAhead)) {
            loadConst(-getNum());
        } else {
            factor();
            negate();
        }
    }

    // Parse and Translate a Leading Factor
    public void firstFactor() {
        switch (lookAhead) {
        case '+':
            match('+');
            factor();
            break;
        case '-':
            negFactor();
            break;
        default:
            factor();
            break;
        }
    }

    // Recognize and Translate a Multiply
    public void multiply() {
        match('*');
        factor();
        popMul();
    }

    // Recognize and Translate a Divide
    public void divide() {
        match('/');
        factor();
        popDiv();
    }

    // Common Code Used by Term and FirstTerm
    public void term1() {
        while (isMulop(lookAhead)) {
            push();
            switch (lookAhead) {
            case '*':
                multiply();
                break;
            case '/':
                divide();
                break;
            }
        }
    }

    // Parse and Translate a Math Term
    public void term() {
        factor();
        term1();
    }

    // Parse and Translate a Leading Term
    public void firstTerm() {
        firstFactor();
        term1();
    }

    // Recognize and Translate an Add
    public void add() {
        match('+');
        term();
        popAdd();
    }

    // Recognize and Translate a Subtract
    public void subtract() {
        match('-');
        term();
        popSub();
    }

    // Parse and Translate an Expression
    public void expression() {
        firstTerm();
        while (isAddop(lookAhead)) {
            push();
            switch (lookAhead) {
            case '+':
                add();
                break;
            case '-':
                subtract();
                break;
            }
        }
    }

    // Parse and Translate an Assignment Statement
    public void assignment() {
        char name;
        name = getName();
        match('=');
        expression();
        store(name);
    }

    // Parse and Translate a Block of Statements
    public void block() {
        while (lookAhead != 'e') {
            assignment();
        }
    }

    // Parse and Translate a Main Program
    public void tMain() {
        match('b');
        prolog();
        block();
        match('e');
        epilog();
    }

    // Write the Prolog
    public void prolog() {
        postLabel("MAIN");
    }

    public void epilog() {
        emitLn("DC WARMST");
        emitLn("END MAIN");
    }

    // Initialize
    public void init() {
        lCount = 0;
        getChar();

        symbolTable = new ArrayList<Character>();
    }

    // Parse and Translate a Program
    public void program() {
        match('p');
        header();
        topDecls();
        tMain();
        match('.');
    }

    // Main Program the entry point
    public static void main(String[] args) {
        Tiny tiny = new Tiny();
        tiny.init();
        tiny.program();
        if (tiny.lookAhead != CR) {
            tiny.abort("Unexpected data after '.'");
        }
    }

    // all necessary bnfs
    // <program> ::= PROGRAM <top-level decl> <main> '.'
    // <main> ::= BEGIN <block> END
    // <top-level decls> ::= ( <data declaration> )*
    // <data declaration> ::= VAR <var-list>
    // <var-list> ::= <var> ( <var> )*
    // <var> ::= <ident> [ = <integer> ]
    // <block> ::= (Assignment)*
    // <assignment> ::= <ident> = <expression>
    // <expression> ::= <first term> ( <addop> <term> )*
    // <first term> ::= <first factor> <rest>
    // <term> ::= <factor> <rest>
    // <rest> ::= ( <mulop> <factor> )*
    // <first factor> ::= [ <addop> ] <factor>
    // <factor> ::= <var> | <number> | ( <expression> )
    // <bool-expr> ::= <bool-term> ( <orop> <bool-term> )*
    // <bool-term> ::= <not-factor> ( <andop> <not-factor> )*
    // <not-factor> ::= [ '!' ] <relation>
    // <relation> ::= <expression> [ <relop> <expression> ]
}
