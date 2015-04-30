package tutor6;

import java.io.IOException;

/**
 * tutor 5 CONTROL CONSTRUCTS
 * 
 * @author lucienSun
 *
 */
public class Cradle {
    // Constant Declarations
    private final static char TAB = '\t';
    private final static char CR = '\r';
    private final static char LF = '\n';
    // Variable Declarations

    // Lookahead Character
    private char lookAhead;
    // Label Counter
    private int lCount;

    /**
     * 
     * @Title: getChar
     * @Description: Read New Character From Input Stream
     * @param @throws IOException 设定文件
     * @return void 返回类型
     * @throws
     */
    public void getChar() {
        try {
            lookAhead = (char) System.in.read();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 
     * @Title: Error
     * @Description: Report an Error
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void error(String str) {
        System.out.println("\nError: " + str + ".");
    }

    /**
     * 
     * @Title: Abort
     * @Description: Report Error and Halt
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void abort(String str) {
        error(str);
        System.exit(1);
    }

    /**
     * 
     * @Title: Expected
     * @Description: Report What Was Expected
     * @param @param str 设定文件
     * @return void 返回类型
     * @throws
     */
    public void expected(String str) {
        abort(str + " expected");
    }

    /**
     * 
     * @Title: match
     * @Description: Match a Specific Input Character
     * @param @param x 设定文件
     * @return void 返回类型
     * @throws
     */
    public void match(char x) {
        if (lookAhead == x) {
            getChar();
        } else {
            expected(" ' " + x + " ' ");
        }
    }

    /**
     * 
     * @Title: isAlpha
     * @Description: Recognize an Alpha Character
     * @param @return 设定文件
     * @return boolean 返回类型
     * @throws
     */
    public boolean isAlpha(char c) {
        if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 
     * @Title: isDigit
     * @Description: Recognize a Decimal Digit
     * @param @param i
     * @param @return 设定文件
     * @return boolean 返回类型
     * @throws
     */
    public boolean isDigit(char c) {
        if (c >= '0' && c <= '9') {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 
     * @Title: isBoolean
     * @Description: Recognize a Boolean Literal
     * @param @param c
     * @param @return 设定文件
     * @return boolean 返回类型
     * @throws
     */
    public boolean isBoolean(char c) {
        if (c == 'T' || c == 'F') {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 
     * @Title: isOrOp
     * @Description: Recognize a Boolean Orop
     * @param @param c
     * @param @return 设定文件
     * @return boolean 返回类型
     * @throws
     */
    public boolean isOrOp(char c) {
        if (c == '|' || c == '~') {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 
     * @Title: isRelOp
     * @Description: Recognize a Relop
     * @param @param c
     * @param @return 设定文件
     * @return boolean 返回类型
     * @throws
     */
    public boolean isRelOp(char c) {
        if (c == '=' || c == '#' || c == '<' || c == '>') {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 
     * @Title: isAddOp
     * @Description: Recognize an AddOp
     * @param @param c
     * @param @return 设定文件
     * @return boolean 返回类型
     * @throws
     */
    public boolean isAddOp(char c) {
        if (c == '+' || c == '-') {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 
     * @Title: isWhite
     * @Description: Recognize White Space
     * @param @param c
     * @param @return 设定文件
     * @return boolean 返回类型
     * @throws
     */
    public boolean isWhite(char c) {
        if (c == ' ' || c == TAB) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 
     * @Title: skipWhite
     * @Description: Skip Over Leading White Space
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void skipWhite() {
        while (isWhite(lookAhead)) {
            getChar();
        }
    }
    
    /**
     * 
    * @Title: fin 
    * @Description: Skip a CRLF
    * @param     设定文件 
    * @return void    返回类型 
    * @throws
     */
    public void fin() {
        if (lookAhead == CR) {
            getChar();
        } else if (lookAhead == LF) {
            getChar();
        }
    }

    /**
     * 
     * @Title: getName
     * @Description: Get an Identifier
     * @param @return 设定文件
     * @return char 返回类型
     * @throws
     */
    public char getName() {
        char ch = lookAhead;

        if (!isAlpha(ch)) {
            expected("Name");
        } else {
            getChar();
        }
        return ch;
    }

    /**
     * 
     * @Title: getNum
     * @Description: Get a Number
     * @param @return 设定文件
     * @return char 返回类型
     * @throws
     */
    public char getNum() {
        char ch = lookAhead;
        if (!isDigit(ch)) {
            expected("Integer");
        } else {
            getChar();
        }
        return ch;
    }

    /**
     * 
     * @Title: getBoolean
     * @Description: Get a Boolean Literal
     * @param @return 设定文件
     * @return boolean 返回类型
     * @throws
     */
    public boolean getBoolean() {
        char ch = lookAhead;
        boolean bVal = false;
        if (!isBoolean(ch)) {
            expected("Boolean Literal");
        } else {
            bVal = lookAhead == 'T';
            getChar();
        }
        return bVal;
    }

    /**
     * 
     * @Title: ident
     * @Description: Parse and Translate an Identifier
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void ident() {
        char name;
        name = getName();
        if (lookAhead == '(') {
            match('(');
            match(')');
            emitLn("BSR " + name);
        } else {
            emitLn("MOVE " + name + "(PC),D0");
        }
    }

    /**
     * 
     * @Title: factor
     * @Description: Parse and Translate a Math Factor
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void factor() {
        if (lookAhead == '(') {
            match('(');
            expression();
            match(')');
        } else if (isAlpha(lookAhead)) {
            ident();
        } else {
            emitLn("MOVE #" + getNum() + ",D0");
        }
    }

    /**
     * 
     * @Title: signedFactor
     * @Description: Parse and Translate the First Math Factor
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void signedFactor() {
        if (lookAhead == '+') {
            getChar();
            factor();
        } else if (lookAhead == '-') {
            getChar();
            if (isDigit(lookAhead)) {
                emitLn("MOVE #-" + getNum() + ", D0");
            } else {
                factor();
                emitLn("NEG D0");
            }
        } else {
            factor();
        }
    }

    /**
     * 
     * @Title: muls
     * @Description: Recognize and Translate a Multiply
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void multiply() {
        match('*');
        factor();
        emitLn("MULS (SP)+,D0");
    }

    /**
     * 
     * @Title: divide
     * @Description: Recognize and Translate a Divide
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void divide() {
        match('/');
        factor();
        emitLn("MOVE (SP)+,D1");
        emitLn("EXS.L D0");
        emitLn("DIVS D1,D0");
    }

    /**
     * 
     * @Title: term
     * @Description: Parse and Translate a Math Term
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void term() {
        signedFactor();
        while (lookAhead == '*' || lookAhead == '/') {
            emitLn("MOVE D0,-(SP)");
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

    /**
     * 
     * @Title: add
     * @Description: Recognize and Translate an Add
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void add() {
        match('+');
        term();
        emitLn("ADD (SP)+, D0");
    }

    /**
     * 
     * @Title: subtract
     * @Description: Recognize and Translate a Subtract
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void subtract() {
        match('-');
        term();
        emitLn("SUB (SP)+, D0");
        emitLn("NEG D0");
    }

    /**
     * 
     * @Title: expression
     * @Description: Parse and Translate an Expression
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void expression() {
        term();
        while (isAddOp(lookAhead)) {
            emitLn("MOVE D0,-(SP)");
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

    /**
     * 
     * @Title: newLabel
     * @Description: Generate a Unique Label in the form of 'Lnn', where nn is a
     *               label number starting from zero.
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     */
    public String newLabel() {
        String str = null;
        str = String.valueOf(lCount);
        lCount++;
        return "L" + str;
    }

    /**
     * 
     * @Title: postLabel
     * @Description: Post a Label To Output
     * @param @param str 设定文件
     * @return void 返回类型
     * @throws
     */
    public void postLabel(String label) {
        System.out.println(label + ":");
    }

    /**
     * 
     * @Title: emit
     * @Description: Output a String with TAB
     * @param @param str 设定文件
     * @return void 返回类型
     * @throws
     */
    public void emit(String str) {
        System.out.print(TAB + str);
    }

    /**
     * 
     * @Title: emitLn
     * @Description: Output a String with TAB and CRLF
     * @param @param str 设定文件
     * @return void 返回类型
     * @throws
     */
    public void emitLn(String str) {
        emit(str);
        System.out.println("");
    }

    /**
     * 
     * @Title: other
     * @Description: Recognize and Translate an "Other"
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void other() {
        emitLn(String.valueOf(getName()));
    }

    /**
     * 
     * @Title: equals
     * @Description: Recognize and Translate a Relational "Equals"
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void equals() {
        match('=');
        expression();
        emitLn("CMP (SP)+, D0");
        emitLn("SEQ D0");
    }

    /**
     * 
     * @Title: notEquals
     * @Description: Recognize and Translate a Relational "Not Equals"
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void notEquals() {
        match('#');
        expression();
        emitLn("CMP (SP)+, D0");
        emitLn("SNE D0");
    }

    /**
     * 
     * @Title: less
     * @Description: Recognize and Translate a Relational "Less Than"
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void less() {
        match('<');
        expression();
        emitLn("CMP (SP)+, D0");
        emitLn("SGE D0");
    }

    /**
     * 
     * @Title: greater
     * @Description: Recognize and Translate a Relational "Greater Than"
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void greater() {
        match('>');
        expression();
        emitLn("CMP (SP)+, D0");
        emitLn("SLE D0");
    }

    /**
     * 
     * @Title: relation
     * @Description: Parse and Translate a Relation
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void relation() {
        expression();
        if (isRelOp(lookAhead)) {
            emitLn("MOVE D0, -(SP)");
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
            emitLn("TST D0");
        }
    }

    /**
     * 
     * @Title: booleanFactor
     * @Description: Parse and Translate a Boolean Factor
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void booleanFactor() {
        if (isBoolean(lookAhead)) {
            if (getBoolean()) {
                emitLn("MOVE #-1,D0");
            } else {
                emitLn("CLR D0");
            }
        } else {
            relation();
        }

    }

    /**
     * 
     * @Title: notFactor
     * @Description: Parse and Translate a Boolean Factor with NOT
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void notFactor() {
        if (lookAhead == '!') {
            match('!');
            booleanFactor();
            emitLn("EOR #-1,D0");
        } else {
            booleanFactor();
        }
    }

    /**
     * 
     * @Title: booleanTerm
     * @Description: Parse and Translate a Boolean Term
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void booleanTerm() {
        notFactor();
        while (lookAhead == '&') {
            emitLn("MOVE D0,-(SP)");
            match('&');
            notFactor();
            emitLn("AND (SP)+, D0");
        }
    }

    /**
     * 
     * @Title: booleanOr
     * @Description: Recognize and Translate a Boolean OR
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void booleanOr() {
        match('|');
        booleanTerm();
        emitLn("OR (SP)+, D0");
    }

    /**
     * 
     * @Title: booleanXor
     * @Description: Recognize and Translate an Exclusive Or
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void booleanXor() {
        match('~');
        booleanTerm();
        emitLn("EOR (SP)+, D0");
    }

    /**
     * 
     * @Title: booleanExpression
     * @Description: Parse and Translate a Boolean Expression
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void booleanExpression() {
        booleanTerm();
        while (isOrOp(lookAhead)) {
            emitLn("MOVE D0,-(SP)");
            switch (lookAhead) {
            case '|':
                booleanOr();
                break;
            case '~':
                booleanXor();
                break;
            }
        }
    }

    /**
     * 
    * @Title: assignment 
    * @Description: Parse and Translate an Assignment Statement
    * @param     设定文件 
    * @return void    返回类型 
    * @throws
     */
    public void assignment() {
        char name;
        name = getName();
        match('=');
        booleanExpression();
        emitLn("LEA " + name + "(PC),A0");
        emitLn("MOVE D0, (A0)");
    }
    
    /**
     * 
     * @Title: doIf
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param @param label 设定文件
     * @return void 返回类型
     * @throws
     */
    public void doIf(String label) {
        String label1 = null;
        String label2 = null;
        match('i');
        label1 = newLabel();
        label2 = label1;
        booleanExpression();
        emitLn("BEQ " + label1);
        block(label);
        if (lookAhead == 'l') {
            match('l');
            label2 = newLabel();
            emitLn("BRA " + label2);
            postLabel(label1);
            block(label);
        }
        match('e');
        postLabel(label2);
    }

    /**
     * 
     * @Title: doWhile
     * @Description: Parse and Translate a WHILE Statement
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void doWhile() {
        String label1 = null;
        String label2 = null;
        match('w');
        label1 = newLabel();
        label2 = newLabel();
        postLabel(label1);
        booleanExpression();
        emitLn("BEQ " + label2);
        block(label2);
        match('e');
        emitLn("BRA " + label1);
        postLabel(label2);
    }

    /**
     * 
     * @Title: doLoop
     * @Description: Parse and Translate a LOOP Statement
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void doLoop() {
        String label1 = null;
        String label2 = null;
        match('p');
        label1 = newLabel();
        label2 = newLabel();
        postLabel(label1);
        block(label2);
        match('e');
        emitLn("BRA " + label1);
        postLabel(label2);
    }

    /**
     * 
     * @Title: doRepeat
     * @Description: Parse and Translate a REPEAT Statement
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void doRepeat() {
        String label1 = null;
        String label2 = null;
        match('r');
        label1 = newLabel();
        label2 = newLabel();
        postLabel(label1);
        block(label2);
        match('u');
        booleanExpression();
        emitLn("BEQ " + label1);
        postLabel(label2);
    }

    /**
     * 
     * @Title: doFor
     * @Description: Parse and Translate a FOR Statement
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void doFor() {
        String label1 = null;
        String label2 = null;
        char name;
        match('f');
        label1 = newLabel();
        label2 = newLabel();
        // get name of loop counter
        name = getName();
        match('=');
        // get initial value
        expression();
        // predecrement it
        emitLn("SUBQ #1,D0");
        // address the loop counter
        emitLn("LEA " + name + "(PC),A0");
        // save the loop counter
        emitLn("MOVE D0,(A0)");
        // get upper limit
        expression();
        // save it on stack
        emitLn("MOVE D0,-(SP)");
        postLabel(label1);
        // address loop counter
        emitLn("LEA " + name + "(PC),A0");
        // fetch loop counter to D0
        emitLn("MOVE (A0),D0");
        // bump the loop counter
        emitLn("ADDQ #1,D0");
        // save new value
        emitLn("MOVE D0,(A0)");
        // check for range
        emitLn("CMP (SP),D0");
        // skip out if D0 > (SP)
        emitLn("BLE " + label2);
        block(label2);
        match('e');
        // loop for next pass
        emitLn("BRA " + label1);
        postLabel(label2);
        // clean up the stack
        emitLn("ADDQ #2,SP");
    }

    /**
     * 
     * @Title: doDo
     * @Description: Parse and Translate a DO Statement
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void doDo() {
        String label1 = null;
        String label2 = null;
        match('d');
        label1 = newLabel();
        label2 = newLabel();
        expression();
        emitLn("SUBQ #1,D0");
        postLabel(label1);
        emitLn("MOVE D0, -(SP)");
        block(label2);
        emitLn("MOVE (SP)+,D0");
        // decrement D0 and branch label1 if D0 is nonzero
        emitLn("DBRA D0," + label1);
        emitLn("SUBQ #2,SP");
        postLabel(label2);
        emitLn("ADDQ #2,SP");
    }

    /**
     * 
     * @Title: doBreak
     * @Description: Recognize and Translate a BREAK
     * @param @param label 设定文件
     * @return void 返回类型
     * @throws
     */
    public void doBreak(String label) {
        match('b');
        if (label != null && !("".equals(label))) {
            emitLn("BRA " + label);
        } else {
            abort("No loop to break from");
        }
    }

    /**
     * 
     * @Title: block
     * @Description: Recognize and Translate a Statement Block
     * @param @param label pass into block function the exit address of the
     *        innermost loop
     * @return void 返回类型
     * @throws
     */
    public void block(String label) {
        while (lookAhead != 'e' && lookAhead != 'l' && lookAhead != 'u') {
            fin();
            switch (lookAhead) {
            case 'i':
                doIf(label);
                break;
            case 'w':
                doWhile();
                break;
            case 'p':
                doLoop();
                break;
            case 'r':
                doRepeat();
                break;
            case 'f':
                doFor();
                break;
            case 'd':
                doDo();
                break;
            case 'b':
                doBreak(label);
                break;
            default:
                other();
            }
            fin();
        }
    }

    /**
     * 
     * @Title: program
     * @Description: Parse and Translate a Program
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void program() {
        block(null);
        if (lookAhead != 'e') {
            expected("end");
        }
        emitLn("END");
    }

    /**
     * 
     * @Title: init
     * @Description: Initialize
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void init() {
        lCount = 0;
        getChar();
    }

    /**
     * 
     * @Title: main
     * @Description: Main Program(aka entry point)
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public static void main(String[] args) {
        Cradle cradle = new Cradle();
        cradle.init();
        cradle.program();
    }
}
