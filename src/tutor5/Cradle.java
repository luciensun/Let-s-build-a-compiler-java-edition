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
    
    /**
     * 
    * @Title: skipWhite 
    * @Description: Skip Over Leading White Space
    * @param     设定文件 
    * @return void    返回类型 
    * @throws
     */
    public void skipWhite() {
        while(isWhite(lookAhead)) {
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
    * @Title: expression 
    * @Description: Parse and Translate a Math Expression
    * @param     设定文件 
    * @return void    返回类型 
    * @throws
     */
    public void expression() {
        emitLn("<expr>");
    }
    
    /**
     * 
     * @Title: doIf
     * @Description: Recognize and Translate an IF Construct
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void doIf(String label) {
        String label1 = null;
        String label2 = null;
        match('i');
        label1 = newLabel();
        label2 = label1;
        condition();
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
        condition();
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
        condition();
        emitLn("BEQ " + label1);
        postLabel(label2);
    }
    
    /**
     * 
    * @Title: doFor 
    * @Description: Parse and Translate a FOR Statement
    * @param     设定文件 
    * @return void    返回类型 
    * @throws
     */
    public void doFor() {
        String label1 = null;
        String label2 = null;
        char name;
        match('f');
        label1 = newLabel();
        label2 = newLabel();
        name = getName();
        match('=');
        expression();
        emitLn("SUBQ #1,D0");
        emitLn("LEA " + name + "(PC),A0");
        emitLn("MOVE D0,(A0)");
        expression();
        emitLn("MOVE D0,-(SP)");
        postLabel(label1);
        emitLn("LEA " + name + "(PC),A0");
        emitLn("MOVE (A0),D0");
        emitLn("ADDQ #1,D0");
        emitLn("MOVE D0,(A0)");
        emitLn("CMP (SP),D0");
        emitLn("BGT " + label2);
        block(label2);
        match('e');
        emitLn("BRA " + label1);
        postLabel(label2);
        emitLn("ADDQ #2,SP");
    }
    
    /**
     * 
    * @Title: doDo 
    * @Description: Parse and Translate a DO Statement
    * @param     设定文件 
    * @return void    返回类型 
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
        emitLn("DBRA D0," + label1);
        emitLn("SUBQ #2,SP");
        postLabel(label2);
        emitLn("SUBQ #2,SP");
        postLabel(label2);
        emitLn("ADDQ #2,SP");
    }
    
    /**
     * 
    * @Title: doBreak 
    * @Description: Recognize and Translate a BREAK
    * @param @param label    设定文件 
    * @return void    返回类型 
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
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void block(String label) {
        while (lookAhead != 'e' && lookAhead != 'l' && lookAhead != 'u') {
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
