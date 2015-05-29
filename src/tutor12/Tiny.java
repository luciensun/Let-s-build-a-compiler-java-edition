package tutor12;

import java.io.IOException;

/**
 * Part XII: MISCELLANY
 * 
 * PROGRAM VAR xMan = 1, ladyGa = 2 BEGIN IF xMan < 3 ladyGa = 3 ELSE ladyGa = 8
 * ENDIF xMan = 5 END .
 * 
 * @author lucienSun
 *
 */
public class Tiny {

    // Constant Declarations
    private final static char TAB = '\t';
    private final static char CR = '\r';
    private final static char LF = '\n';

    // Label Counter
    private int lCount;

    // Variable Declarations
    // Lookahead Character
    private char lookAhead;
    // Encoded Token
    private char token;
    // Unencoded Token
    private String tokenVal;

    // entryTable aka variableTable
    private int currentEntry = 0;
    private final static int maxEntry = 800;

    private String[] entryTable = null;
    private char[] entryTypeTable = null;

    // Definition of Keywords and Token Types

    private final static String[] keyWordList = { "IF", "ELSE", "ENDIF",
            "WHILE", "ENDWHILE", "READ", "WRITE", "VAR", "END" };

    private final static String keyWordCode = "xileweRWve";

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

    // Report an Undefined Identifier
    public void undefined(String variable) {
        abort("Undefined Identifier " + variable);
    }

    // Report a Duplicate Identifier
    public void duplicate(String variable) {
        abort("Duplicate Identifier " + variable);
    }

    // Check to Make Sure the Current Token is an Identifier
    public void checkIdent() {
        if (token != 'x') {
            expected("Identifier");
        }
    }

    // Recognize an Alpha Character
    public boolean isAlpha(char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
    }

    // Recognize a Decimal Digit
    public boolean isDigit(char c) {
        return (c >= '0' && c <= '9');
    }

    // Recognize an Alphanumeric Character
    public boolean isAlNum(char c) {
        return isAlpha(c) || isDigit(c);
    }

    // Recognize an Addop
    public boolean isAddop(char c) {
        return c == '+' || c == '-';
    }

    // Recognize a Mulop
    public boolean isMulop(char c) {
        return c == '*' || c == '/';
    }

    // Recognize a Boolean Orop
    public boolean isOrop(char c) {
        return c == '|' || c == '~';
    }

    // Recognize a Relop
    public boolean isRelop(char c) {
        return c == '=' || c == '#' || c == '<' || c == '>';
    }

    // Recognize White Space
    public boolean isWhite(char c) {
        return c == ' ' || c == TAB || c == CR || c == LF || c == '{';
    }

    // Skip Over Leading White Space
    public void skipWhite() {
        while (isWhite(lookAhead)) {
            if (lookAhead == '{') {
                skipComment();
            } else {
                getChar();
            }
        }
    }

    // Skip A Comment Field
    public void skipComment() {
        while (lookAhead != '}') {
            getChar();
            // to deal with nested comments
            if (lookAhead == '{') {
                skipComment();
            }
        }
        getChar();
    }

    // Match a Semicolon
    public void semi() {
        if (token == ';') {
            next();
        }
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

    // Look for entry in Table
    public boolean inTable(String entry) {
        return lookup(entryTable, entry) >= 0;
    }

    // Check to See if an Identifier is in the Symbol Table
    // Report an error if it isn't.
    public void checkTable(String entry) {
        if (!inTable(entry)) {
            undefined(entry);
        }
    }

    // Check the Symbol Table for a Duplicate Identifier
    // Report an error if identifier is already in table.
    public void checkDup(String entry) {
        if (inTable(entry)) {
            duplicate(entry);
        }
    }

    // Add a New Entry to Entry/Symbol Table
    public void addEntry(String entry, char entryType) {
        checkDup(entry);
        if (currentEntry >= maxEntry) {
            abort("Entry table is full");
        }
        entryTable[currentEntry] = entry;
        entryTypeTable[currentEntry] = entryType;
        currentEntry++;
    }

    // Get an Identifier
    public void getName() {
        skipWhite();
        if (!isAlpha(lookAhead)) {
            expected("Identifier");
        }
        token = 'x';
        tokenVal = "";
        while (isAlNum(lookAhead)) {
            tokenVal = tokenVal + lookAhead;
            getChar();
        }
    }

    // Get a Number
    public void getNum() {
        skipWhite();
        if (!isDigit(lookAhead)) {
            expected("Number");
        }
        token = '#';
        tokenVal = "";
        while (isDigit(lookAhead)) {
            tokenVal = tokenVal + lookAhead;
            getChar();
        }
    }

    // Get an Operator
    public void getOp() {
        skipWhite();
        token = lookAhead;
        tokenVal = String.valueOf(lookAhead);
        getChar();
    }

    // Get the Next Input Token
    public void next() {
        skipWhite();
        if (isAlpha(lookAhead)) {
            getName();
        } else if (isDigit(lookAhead)) {
            getNum();
        } else {
            getOp();
        }
    }

    // Scan the Current Identifier for Keywords
    public void scan() {
        if (token == 'x') {
            token = keyWordCode.charAt(lookup(keyWordList, tokenVal) + 1);
        }
    }

    // Match a Specific Input String
    public void matchString(String str) {
        if (!tokenVal.equals(str)) {
            expected("'" + str + "'");
        }
        next();
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

    // Here begins the code generation routines, we use it to retarget the cpu
    // Clear the Primary Register
    public void clear() {
        emitLn("CLR D0");
    }

    // Negate the Primary Register
    public void negate() {
        emitLn("NEG D0");
    }

    // Complement the Primary Register
    public void notIt() {
        emitLn("NOT D0");
    }

    // Load a Constant Value to Primary Register
    public void loadConst(String constant) {
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

    // Set D0 If Compare was <=
    public void setLessOrEqual() {
        emitLn("SGE D0");
        emitLn("EXT D0");
    }

    // Set D0 If Compare was >=
    public void setGreaterOrEqual() {
        emitLn("SLE D0");
        emitLn("EXT D0");
    }

    // Store Primary to Variable
    public void store(String variable) {
        emitLn("LEA " + variable + "(PC),A0");
        emitLn("MOVE D0,(A0)");
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

    // read value in Primary Register(D0) to Variable
    public void readVar() {
        checkIdent();
        checkTable(tokenVal);
        emitLn("BSR READ");
        store(tokenVal);
        next();
    }

    // Write from Primary Register(D0)
    public void writeVar() {
        emitLn("BSR WRITE");
    }

    // Here ends the code generation routines, we use it to retarget the cpu

    // Write Header Info
    public void header() {
        System.out.println("WARMST" + TAB + "EQU $A01E");
        emitLn("LIB TINYLIB");
    }

    // Write the Prolog
    public void prolog() {
        postLabel("MAIN");
    }

    // Write the Epilog
    public void epilog() {
        emitLn("DC WARMST");
        emitLn("END MAIN");
    }

    // Get Another Expression and Compare
    public void compareExpression() {
        expression();
        popCompare();
    }

    // Get The Next Expression and Compare
    public void nextExpression() {
        next();
        compareExpression();
    }

    // Recognize and Translate a Relational "Equals"
    public void equals() {
        nextExpression();
        setEqual();
    }

    // Recognize and Translate a Relational "Not Equals"
    public void notEquals() {
        nextExpression();
        setNEqual();
    }

    // Recognize and Translate a Relational "Less Than or Equal"
    public void lessOrEqual() {
        nextExpression();
        setLessOrEqual();
    }

    // Recognize and Translate a Relational "Less Than"
    public void less() {
        next();
        switch (token) {
        case '=':
            lessOrEqual();
            break;
        case '>':
            notEquals();
            break;
        default:
            compareExpression();
            setLess();
        }
    }

    // Recognize and Translate a Relational "Greater Than"
    public void greater() {
        next();
        if (token == '=') {
            nextExpression();
            setGreaterOrEqual();
        } else {
            compareExpression();
            setGreater();
        }
    }

    // Parse and Translate a Relation
    public void relation() {
        expression();
        if (isRelop(token)) {
            push();
            switch (token) {
            case '=':
                equals();
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
        if (token == '!') {
            next();
            relation();
            notIt();
        } else {
            relation();
        }
    }

    // Parse and Translate a Boolean Term
    public void boolTerm() {
        notFactor();
        while (token == '&') {
            push();
            next();
            notFactor();
            popAnd();
        }
    }

    // Recognize and Translate a Boolean OR
    public void boolOr() {
        next();
        boolTerm();
        popOr();
    }

    // Recognize and Translate an Exclusive Or
    public void boolXor() {
        next();
        boolTerm();
        popXor();
    }

    // Parse and Translate a Boolean Expression
    public void boolExpression() {
        boolTerm();
        while (isOrop(token)) {
            push();
            switch (token) {
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
        if (token == '(') {
            next();
            boolExpression();
            matchString(")");
        } else if (token == 'x') {
            loadVar(tokenVal);
            next();
        } else if (token == '#') {
            loadConst(tokenVal);
            next();
        } else {
            expected("Math Factor");
        }
    }

    // Parse and Translate a Negative Factor
    public void negFactor() {
        next();
        if (token == '#') {
            getNum();
            loadConst("-" + tokenVal);
        } else {
            factor();
            negate();
        }
    }

    // Parse and Translate a Leading Factor
    public void firstFactor() {
        switch (token) {
        case '+':
            next();
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
        next();
        factor();
        popMul();
    }

    // Recognize and Translate a Divide
    public void divide() {
        next();
        factor();
        popDiv();
    }

    // Common Code Used by Term and FirstTerm
    public void term1() {
        while (isMulop(token)) {
            push();
            switch (token) {
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
        next();
        term();
        popAdd();
    }

    // Recognize and Translate a Subtract
    public void subtract() {
        next();
        term();
        popSub();
    }

    // Parse and Translate an Expression
    public void expression() {
        firstTerm();
        while (isAddop(token)) {
            push();
            switch (token) {
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
        String name = null;
        checkTable(tokenVal);
        name = tokenVal;
        next();
        matchString("=");
        boolExpression();
        store(name);
    }

    // Recognize and Translate an IF Construct
    public void doIf() {
        String label1, label2;
        next();
        boolExpression();
        label1 = newLabel();
        label2 = label1;
        branchFalse(label1);
        block();
        if (token == 'l') {
            next();
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
        next();
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

    // Process a Read Statement
    public void doRead() {
        next();
        matchString("(");
        readVar();
        while (token == ',') {
            next();
            readVar();
        }
        matchString(")");
    }

    // Process a Write Statement
    public void doWrite() {
        next();
        matchString("(");
        expression();
        writeVar();
        while (token == ',') {
            next();
            expression();
            writeVar();
        }
        matchString(")");
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
            case 'R':
                doRead();
                break;
            case 'W':
                doWrite();
                break;
            case 'x':
                assignment();
                break;
            }
            semi();
            scan();
        }
    }

    // Allocate Storage for a Static Variable
    public void allocate(String entry, String entryVal) {
        System.out.println(entry + ":" + TAB + "DC " + entryVal);
    }

    // Allocate Storage for a Variable
    public void alloc() {
        next();
        if (token != 'x') {
            expected("Variable Name");
        }
        String entry = tokenVal;
        checkDup(entry);
        addEntry(entry, 'v');
        // get the next token
        next();
        if (token == '=') {
            // if variable is assigned certain value it will be
            // initialized with the given value
            next();
            if (token == '-') {
                next();
                allocate(entry, "-" + tokenVal);
                next();
            } else {
                allocate(entry, tokenVal);
                next();
            }
        } else {
            // otherwise variable is initialized to be zero
            allocate(entry, "0");
        }
    }

    // Parse and Translate Global Declarations
    public void topDecls() {
        scan();
        while (token == 'v') {
            alloc();
            while (token == ',') {
                alloc();
            }
            semi();
        }
    }

    // Initialize
    public void init() {
        lCount = 0;
        entryTable = new String[maxEntry];
        entryTypeTable = new char[maxEntry];
        for (int i = 0; i < maxEntry; i++) {
            entryTable[i] = "";
            entryTypeTable[i] = '\0';
        }

        getChar();
        next();
    }

    // Parse and Translate a Program
    public void program() {
        init();
        matchString("PROGRAM");
        semi();
        header();
        topDecls();
        matchString("BEGIN");
        prolog();
        block();
        matchString("END");
        epilog();
    }

    // Main Program the entry point
    public static void main(String[] args) {
        Tiny tiny = new Tiny();
        tiny.program();
    }

    // all necessary bnfs
    // <program> ::= PROGRAM <top-level decl> <main> '.'
    // <main> ::= BEGIN <block> END
    // <top-level decls> ::= ( <data declaration> )*
    // <data declaration> ::= VAR <var-list>
    // <var-list> ::= <var> ( <var> )*
    // <var> ::= <ident> [ = <integer> ]
    // <block> ::= ( <statement> )*
    // <statement> ：：= <if> | <while> | <read> | <write> | <assignment>
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
