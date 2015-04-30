package tutor4;

import java.io.IOException;

/**
 * tutor 4 INTERPRETERS
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
    /**
     * Lookahead Character
     */
    private char lookAhead;
    private int[] table;

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
            skipWhite();
        } else {
            expected(" ' " + x + " ' ");
        }
    }

    /**
     * 
     * @Title: newLine
     * @Description: Recognize and Skip Over a Newline
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void newLine() {
        if (lookAhead == CR) {
            getChar();
            if (lookAhead == LF) {
                getChar();
            }
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
    * @param @return    设定文件 
    * @return boolean    返回类型 
    * @throws
     */
    public boolean isWhite(char c) {
        if (c == ' ' || c == TAB) {
            return true;
        } else {
            return false;
        }
    }
    
    /**'
     * 
    * @Title: skipWhite 
    * @Description: Skip Over Leading White Space
    * @param     设定文件 
    * @return void    返回类型 
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
    public char getName() {
        char ch = lookAhead;

        if (!isAlpha(ch)) {
            expected("Name");
        } else {
            getChar();
        }
        skipWhite();
        return ch;
    }

    /**
     * 
     * @Title: getNum
     * @Description: Get a Number
     * @param @return 设定文件
     * @return int 返回类型
     * @throws
     */
    public int getNum() {
        int value;
        value = 0;
        if (!isDigit(lookAhead)) {
            expected("Integer");
        }
        while (isDigit(lookAhead)) {
            value = value * 10 + (lookAhead - '0');
            getChar();
        }
        skipWhite();
        return value;
    }

    /**
     * 
     * @Title: getIndex
     * @Description: convert char value to corresponding index in table
     * @param @param c
     * @param @return 设定文件
     * @return int 返回类型
     * @throws
     */
    public int getIndex(char c) {
        int ind = 0;
        if (c >= 'A' && c <= 'Z') {
            ind = c - 'A';
        } else {
            // c >= 'a' && c <= 'z'
            ind = c - 'a' + 26;
        }
        return ind;
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
     * @return int 返回类型
     * @throws
     */
    public int factor() {
        int value;
        if (lookAhead == '(') {
            match('(');
            value = expression();
            match(')');
        } else if (isAlpha(lookAhead)) {
            // ident();
            char c = getName();
            int ind = getIndex(c);
            value = table[ind];
        } else {
            value = getNum();
        }
        return value;
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
     * @return int 返回类型
     * @throws
     */
    public int term() {
        int value;
        value = factor();
        while (lookAhead == '*' || lookAhead == '/') {
            switch (lookAhead) {
            case '*':
                match('*');
                value = value * factor();
                break;
            case '/':
                match('/');
                value = value / factor();
                break;
            }
        }
        return value;
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
     * @return int 返回类型
     * @throws
     */
    public int expression() {
        int value;
        if (isAddop(lookAhead)) {
            value = 0;
        } else {
            value = term();
        }
        while (isAddop(lookAhead)) {
            switch (lookAhead) {
            case '+':
                match('+');
                value = value + term();
                break;
            case '-':
                match('-');
                value = value - term();
                break;
            default:
            }
        }
        return value;
    }

    public void assignment() {
        char name;
        name = getName();
        match('=');
        int ind = getIndex(name);
        table[ind] = expression();
    }

    /**
     * 
     * @Title: initTable
     * @Description: Initialize the Variable Area
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void initTable() {
        // the storage of 26 * 2 possible variables,
        // : one for each letter of the alphabet(both in uppercase or lowercase)
        table = new int[52];
        for (int i = 0; i < table.length; i++) {
            table[i] = 0;
        }
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
        initTable();
        getChar();
        skipWhite();
    }

    /**
     * 
     * @Title: input
     * @Description: Input Routine
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void input() {
        match('?');
        char c = getName();
        int ind = getIndex(c);
        table[ind] = getNum();
    }

    /**
     * 
     * @Title: output
     * @Description: Output Routine
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void output() {
        match('!');
        char c = getName();
        int ind = getIndex(c);
        emitLn(String.valueOf(table[ind]));
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
        while (cradle.lookAhead != '.') {
            switch (cradle.lookAhead) {
            case '?':
                cradle.input();
                break;
            case '!':
                cradle.output();
                break;
            default:
                cradle.assignment();
            }
            cradle.newLine();
        }

    }
}
