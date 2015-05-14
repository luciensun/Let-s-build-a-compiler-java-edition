package tutor10;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Part X: INTRODUCING "TINY"
 * sample program for tiny in part X is below
 * 
PROGRAM
VAR xMan = 1, ladyGa = 2
BEGIN 
IF xMan < 3 ladyGa = 3 ELSE ladyGa = 8 ENDIF
xMan = 5
END
.
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

    // Encoded Token
    private char token;
    // Unencoded Token
    private String tokenVal;

    // Label Counter
    private int lCount;

    // entryTable aka variableTable
    private int currentEntry = 0;
    private final static int maxEntry = 800;
    
    private String[] entryTable = null;
    private char[] entryTypeTable = null;

    // Definition of Keywords and Token Types

    private final static String[] keyWordList = { "IF", "ELSE", "ENDIF",
            "WHILE", "ENDWHILE", "VAR", "BEGIN", "END", "PROGRAM" };

    private final static String keyWordCode = "xilewevbep";

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
    public void loadVar(String variable) {
        if (!inTable(variable)) {
            undefined(variable);
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
    public void store(String variable) {
        if (!inTable(variable)) {
            undefined(variable);
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

    // Branch Unconditional
    public void branch(String label) {
        emitLn("BRA " + label);
    }

    // Branch False
    public void branchFalse(String label) {
        emitLn("TST D0");
        emitLn("BEQ " + label);
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

    // Look for entry in Table
    public boolean inTable(String entry) {
        return lookup(entryTable, entry) >= 0;
    }

    // Table Lookup return -1 if the str is not in the keyworklist
    public int lookup(String[] keyWordList, String str) {
        int i = keyWordList.length - 1;
        boolean found = false;
        while (i >= 0 && !found) {
            if (keyWordList[i].equals(str)) {
                found = true;
            } else {
                i--;
            }
        }
        return i;
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

    // Skip Over an End-of-Line
    public void newLine() {
        while (lookAhead == CR) {
            getChar();
            if (lookAhead == LF) {
                getChar();
            }
            skipWhite();
        }
    }

    // Match a Specific Input Character
    public void match(char x) {
        newLine();
        if (lookAhead == x) {
            getChar();
            skipWhite();
        } else {
            expected("'" + x + "'");
        }
    }

    // Match a Specific Input String
    public void matchString(String str) {
        if (!tokenVal.equals(str)) {
            expected("'" + str + "'");
        }
    }

    // temporary getName
    public void getName() {
        // skip an E-O-L
        newLine();
        tokenVal = "";
        if (!isAlpha(lookAhead)) {
            expected("Name");
        }
        while (isAlNum(lookAhead)) {
            tokenVal = tokenVal + lookAhead;
            getChar();
        }
        // skip whitespace
        skipWhite();
    }

    // Get a Number
    public int getNum() {
        // skip an E-O-L
        newLine();
        int val = 0;
        if (!isDigit(lookAhead)) {
            expected("Number");
        }
        while (isDigit(lookAhead)) {
            val = val * 10 + (lookAhead - '0');
            getChar();
        }
        // skip whitespace
        skipWhite();
        return val;
    }

    public void scan() {
        getName();
        token = keyWordCode.charAt(lookup(keyWordList, tokenVal) + 1);
    }

    // Generate a Unique Label in the form of 'Lnn',
    // where nn is a label number starting from zero.
    public String newLabel() {
        String str = null;
        str = String.valueOf(lCount);
        lCount++;
        return "L" + str;
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
    
    // Add a New Entry to Entry Table
    public void addEntry(String entry, char entryType) {
        if (inTable(entry)) {
            abort("Duplicate Identifier " + entry);
        }
        if (currentEntry >= maxEntry) {
            abort("Entry table is full");
        }
        entryTable[currentEntry] = entry;
        entryTypeTable[currentEntry] = entryType;
        currentEntry++;    
    }

    // Allocate Storage for a Variable
    public void alloc(String entry) {
        if (inTable(entry)) {
            abort("Duplicate Variable Name " + entry);
        }
        addEntry(entry, 'v');
        System.out.print(entry + ":" + TAB + "DC ");
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
        matchString("VAR");
        getName();
        alloc(tokenVal);
        while (lookAhead == ',') {
            match(',');
            getName();
            alloc(tokenVal);
        }
    }

    // Parse and Translate Global Declarations
    public void topDecls() {
        scan();
        while (token != 'b') {
            switch (token) {
            case 'v':
                decl();
                break;
            default:
                abort("Unrecognized Keyword " + tokenVal);
                break;
            }
            scan();
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
            switch (lookAhead) {
            case '=':
                equals();
                break;
            case '#':
                notEquals();
                break;
            case '<':
                less();
                break;
            case '>':
                greater();
                break;
            }
        }
    }

    // Parse and Translate a Boolean Factor with Leading NOT
    public void notFactor() {
        if (lookAhead == '!') {
            match('!');
            relation();
            notIt();
        } else {
            relation();
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
            switch (lookAhead) {
            case '|':
                boolOr();
                break;
            case '~':
                boolXor();
                break;
            }
        }
    }

    // Parse and Translate a Math Factor
    public void factor() {
        if (lookAhead == '(') {
            match('(');
            boolExpression();
            match(')');
        } else if (isAlpha(lookAhead)) {
            getName();
            loadVar(tokenVal);
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
        String name;
        name = tokenVal;
        match('=');
        boolExpression();
        store(name);
    }

    // Recognize and Translate an IF Construct
    public void doIf() {
        String label1, label2;
        matchString("IF");
        boolExpression();
        label1 = newLabel();
        label2 = label1;
        branchFalse(label1);
        block();
        if (token == 'l') {
            label2 = newLabel();
            branch(label2);
            postLabel(label1);
            block();
        }
        postLabel(label2);
        matchString("ENDIF");
    }

    // Parse and Translate a WHILE Statement
    public void doWhile() {
        String label1, label2;
        label1 = newLabel();
        label2 = newLabel();
        postLabel(label1);
        boolExpression();
        branchFalse(label2);
        block();
        matchString("ENDWHILE");
        branch(label1);
        postLabel(label2);
    }

    // Parse and Translate a Block of Statements
    public void block() {
        scan();
        while (token != 'e' && token != 'l') {
            switch (token) {
            case 'i':
                doIf();
                break;
            case 'w':
                doWhile();
                break;
            default:
                assignment();
                break;
            }
            scan();
        }
    }

    // Parse and Translate a Main Program
    public void tMain() {
        matchString("BEGIN");
        prolog();
        block();
        matchString("END");
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
        scan();
        entryTable = new String[maxEntry];
        entryTypeTable = new char[maxEntry];
        for (int i = 0; i < maxEntry; i++ ) {
            entryTable[i] = "";
            entryTypeTable[i] = '\0';
        }
    }

    // Parse and Translate a Program
    public void program() {
        matchString("PROGRAM");
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
    // <block> ::= ( <statement> )*
    // <statement> ：：= <if> | <while> | <assignment>
    // <assignment> ::= <ident> = <bool-expr>
    // <expression> ::= <first term> ( <addop> <term> )*
    // <first term> ::= <first factor> <rest>
    // <term> ::= <factor> <rest>
    // <rest> ::= ( <mulop> <factor> )*
    // <first factor> ::= [ <addop> ] <factor>
    // <factor> ::= <var> | <number> | ( <bool-expr> )
    // <bool-expr> ::= <bool-term> ( <orop> <bool-term> )*
    // <bool-term> ::= <not-factor> ( <andop> <not-factor> )*
    // <not-factor> ::= [ '!' ] <relation>
    // <relation> ::= <expression> [ <relop> <expression> ]
    // <if> ::= IF <bool-expression> <block> [ ELSE <block>] ENDIF
    // <while> ::= WHILE <bool-expression> <block> ENDWHILE

}
