package tutor5;

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
        char name;
        name = getName();
        match('=');
        expression();
        emitLn("LEA " + name + "(PC),A0");
        emitLn("MOVE D0,(A0)");
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
     * @Title: condition
     * @Description: Parse and Translate a Boolean Condition
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void condition() {
        emitLn("<condition>");
    }

    /**
     * 
     * @Title: doIf
     * @Description: Recognize and Translate an IF Construct
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void doIf() {
        String label = null;
        match('i');
        label = newLabel();
        condition();
        emitLn("BEQ " + label);
        block();
        match('e');
        postLabel(label);

    }

    /**
     * 
     * @Title: block
     * @Description: Recognize and Translate a Statement Block
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void block() {
        while (lookAhead != 'e') {
            switch (lookAhead) {
            case 'i':
                doIf();
                break;
            default:
                other();
            }
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
        block();
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
