package tutor3;

import java.io.IOException;

/**
 * tutor 3 MORE EXPRESSIONS
 * 
 * @author lucienSun
 *
 */
public class CradleMultiChar {
    // Constant Declarations
    private final static char TAB = '\t';
    private final static char CR = '\r';
    // Variable Declarations
    /**
     * Lookahead Character
     */
    private char lookAhead;

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
        if (lookAhead != x) {
            expected(" ' " + x + " ' ");
        } else {
            getChar();
            skipWhite();
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
     * @Title: isAlNum
     * @Description: Recognize an Alphanumeric
     * @param @param c
     * @param @return 设定文件
     * @return boolean 返回类型
     * @throws
     */
    public boolean isAlNum(char c) {
        return isAlpha(c) || isDigit(c);
    }

    /**
     * 
     * @Title: isAddop
     * @Description: Recognize an Addop
     * @param @param c
     * @param @return 设定文件
     * @return boolean 返回类型
     * @throws
     */
    public boolean isAddop(char c) {
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
     * @Title: getName
     * @Description: Get an Identifier
     * @param @return 设定文件
     * @return char 返回类型
     * @throws
     */
    public String getName() {
        String token = "";

        if (!isAlpha(lookAhead)) {
            expected("Name");
        }
        while (isAlNum(lookAhead)) {
            token = token + lookAhead;
            getChar();
        }
        skipWhite();
        return token;
    }

    /**
     * 
     * @Title: getNum
     * @Description: Get a Number
     * @param @return 设定文件
     * @return char 返回类型
     * @throws
     */
    public String getNum() {
        String value = "";

        if (!isDigit(lookAhead)) {
            expected("Integer");
        }
        while (isDigit(lookAhead)) {
            value = value + lookAhead;
            getChar();
        }
        skipWhite();
        return value;
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

    public void ident() {
        String name;
        name = getName();
        if (lookAhead == '(') {
            match('(');
            match(')');
            emitLn("BSR " + name);
        } else {
            emitLn("MOVE " + getName() + "(PC),D0");
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
     * @Title: muls
     * @Description: Recognize and Translate a Multiply
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void muls() {
        match('*');
        factor();
        emitLn("MULS (SP)+,D0");
    }

    /**
     * 
     * @Title: divs
     * @Description: Recognize and Translate a Divide
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void divs() {
        match('/');
        factor();
        emitLn("MOVE (SP)+,D1");
        emitLn("DIVS D1,D0");
    }

    /**
     * 
     * @Title: term
     * @Description: Parse and Translate a Term
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void term() {
        factor();
        while (lookAhead == '*' || lookAhead == '/') {
            emitLn("MOVE D0,-(SP)");
            switch (lookAhead) {
            case '*':
                muls();
                break;
            case '/':
                divs();
                break;
            default:
                // expected("Mulop");
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
        emitLn("ADD (SP)+,D0");
    }

    /**
     * 
     * @Title: sub
     * @Description: Recognize and Translate a Subtract
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void sub() {
        match('-');
        term();
        emitLn("SUB (SP)+,D0");
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
        if (isAddop(lookAhead)) {
            emitLn("CLR D0");
        } else {
            term();
        }
        while (isAddop(lookAhead)) {
            emitLn("MOVE D0,-(SP)");
            switch (lookAhead) {
            case '+':
                add();
                break;
            case '-':
                sub();
                break;
            default:
                // expected("Addop");
            }
        }
    }

    public void assignment() {
        String name;
        name = getName();
        match('=');
        expression();
        emitLn("LEA " + name + "(PC),A0");
        emitLn("MOVE D0,(A0)");
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
        getChar();
        skipWhite();
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
        CradleMultiChar cradle = new CradleMultiChar();
        cradle.init();
        cradle.assignment();
        if (cradle.lookAhead != CR) {
            cradle.expected("Newline");
        }
    }
}
