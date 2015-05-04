package tutor7;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Kiss {
    // Constant Declarations
    private final static char TAB = '\t';
    private final static char CR = '\r';
    private final static char LF = '\n';

    // Type Declarations
    private List<String> symTab;

    // Variable Declarations
    // Lookahead Character
    private char lookAhead;

    // Token Types
    private enum Symbol {
        IF_SYMBOL, ELSE_SYMBOL, ENDIF_SYMBOL, END_SYMBOL, IDENT, NUMBER, OPERATOR;
        public void write() {
            switch (this) {
            case IF_SYMBOL:
            case ELSE_SYMBOL:
            case ENDIF_SYMBOL:
            case END_SYMBOL:
                System.out.print("Keyword ");
                break;
            case IDENT:
                System.out.print("Ident ");
                break;
            case NUMBER:
                System.out.print("Number ");
                break;
            case OPERATOR:
                System.out.print("Operator ");
                break;
            }
        }
    }

    // Current Token
    private Symbol token;
    // Unencoded Token
    private String tokenVal;
    // Label Counter
    private int lCount;

    // Definition of Keywords
    private List<String> keywordList;

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
     * @Description: Recognize an Alphanumeric Character
     * @param @param c
     * @param @return 设定文件
     * @return boolean 返回类型
     * @throws
     */
    public boolean isAlNum(char c) {
        if (isAlpha(c) || isDigit(c)) {
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
     * @Title: isMulop
     * @Description: Recognize a Mulop
     * @param @param c
     * @param @return 设定文件
     * @return boolean 返回类型
     * @throws
     */
    public boolean isMulop(char c) {
        if (c == '*' || c == '/') {
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
            expected("'" + x + "'");
        }
    }

    /**
     * 
     * @Title: fin
     * @Description: Skip a CRLF
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void fin() {
        if (lookAhead == CR) {
            getChar();
        }
        if (lookAhead == LF) {
            getChar();
        }
        skipWhite();
    }

    /**
     * 
     * @Title: lookup
     * @Description: Symbol Table Lookup, If the input string matches a table
     *               entry, return the entry index. If not, return a negative
     *               one .
     * @param @param symTab
     * @param @param str
     * @param @return 设定文件
     * @return int 返回类型
     * @throws
     */
    public int lookup(List<String> symTab, String str) {
        int n = symTab.size();
        int i;
        for (i = n - 1; i >= 0; i--) {
            if (str.equals(symTab.get(i))) {
                break;
            }
        }
        return i;
    }

    /**
     * 
     * @Title: getName
     * @Description: Get an Identifier
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void getName() {
        StringBuffer buffer = new StringBuffer("");
        while (lookAhead == CR) {
            fin();
        }
        if (!isAlpha(lookAhead)) {
            expected("Name");
        }
        while (isAlNum(lookAhead)) {
            buffer.append(lookAhead);
            getChar();
        }
        tokenVal = buffer.toString();

        skipWhite();
    }

    /**
     * 
     * @Title: getNum
     * @Description: Get a Number
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void getNum() {
        StringBuffer buffer = new StringBuffer("");
        if (!isDigit(lookAhead)) {
            expected("Integer");
        }
        while (isDigit(lookAhead)) {
            buffer.append(lookAhead);
            getChar();
        }
        tokenVal = buffer.toString();
        token = Symbol.NUMBER;

        skipWhite();
    }

    /**
     * 
     * @Title: scanner
     * @Description: Get an Identifier and Scan it for Keywords
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void scanner() {
        int k;
        getName();
        k = lookup(keywordList, tokenVal);
        if (k == -1) {
            token = Symbol.IDENT;
        } else {
            token = Symbol.values()[k];
        }
    }

    /**
     * 
     * @Title: matchString
     * @Description: Match a Specific Input String
     * @param @param str 设定文件
     * @return void 返回类型
     * @throws
     */
    public void matchString(String str) {
        if (!tokenVal.equals(str)) {
            expected("'" + str + "'");
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
     * @Title: ident
     * @Description: Parse and Translate an Identifier
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void ident() {
        getName();
        if (lookAhead == '(') {
            match('(');
            match(')');
            emitLn("BSR " + tokenVal);
        } else {
            emitLn("MOVE " + tokenVal + "(PC), D0");
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
            getNum();
            emitLn("MOVE #" + tokenVal + ",D0");
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
        boolean sign = (lookAhead == '-');
        if (isAddop(lookAhead)) {
            getChar();
            skipWhite();
        }
        factor();
        if (sign) {
            emitLn("NEG D0");
        }
    }

    /**
     * 
     * @Title: multiply
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
     * @Title: term1
     * @Description: Completion of Term Processing (called by Term and FirstTerm
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void term1() {
        while (isMulop(lookAhead)) {
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
     * @Title: term
     * @Description: Parse and Translate a Math Term
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void term() {
        factor();
        term1();
    }

    /**
     * 
     * @Title: firstTerm
     * @Description: Parse and Translate a Math Term with Possible Leading Sign
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void firstTerm() {
        signedFactor();
        term1();
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
     * @Title: subtract
     * @Description: Recognize and Translate a Subtract
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void subtract() {
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
        firstTerm();
        while (isAddop(lookAhead)) {
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
     * @Title: condition
     * @Description: Parse and Translate a Boolean Condition
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void condition() {
        emitLn("Condition");
    }

    /**
     * 
     * @Title: doIf
     * @Description: Recognize and Translate an IF Construct
     * @param @param label 设定文件
     * @return void 返回类型
     * @throws
     */
    public void doIf() {
        String label1 = null;
        String label2 = null;
        label1 = newLabel();
        label2 = label1;
        condition();
        emitLn("BEQ " + label1);
        block();
        if (token == Symbol.ELSE_SYMBOL) {
            label2 = newLabel();
            emitLn("BRA " + label2);
            postLabel(label1);
            block();
        }
        postLabel(label2);
        matchString("ENDIF");
    }

    /**
     * 
     * @Title: assignment
     * @Description: Parse and Translate an Assignment Statement
     * @param 设定文件
     * @return void 返回类型
     * @throws
     */
    public void assignment() {
        String name;
        name = tokenVal;
        match('=');
        expression();
        emitLn("LEA " + name + "(PC),A0");
        emitLn("MOVE D0, (A0)");
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
        scanner();
        while (token != Symbol.ENDIF_SYMBOL && token != Symbol.ELSE_SYMBOL
                && token != Symbol.END_SYMBOL) {
            switch (token) {
            case IF_SYMBOL:
                doIf();
                break;
            default:
                assignment();
                break;
            }
            scanner();
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
        matchString("END");
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

        symTab = new ArrayList<String>();
        keywordList = new ArrayList<String>();

        keywordList.add("IF");
        keywordList.add("ELSE");
        keywordList.add("ENDIF");
        keywordList.add("END");

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
        Kiss kiss = new Kiss();
        kiss.init();
        kiss.program();
    }
}
