import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Iterator;

public class SimpleToA86Translator {
    /**
     * a86 code for the myprint procedure
     */
    public static String printProc2="myprint proc \n" +
            " cmp ax, 0\n" +
            " jge poz \n" +
            " not ax\n" +
            " add ax,1\n" +
            " push ax\n" +
            " mov dl, '-' ; print '-'\n" +
            " mov ah, 02h\n" +
            " int 21h   \n" +
            " pop ax\n" +
            "poz:\n" +
            " mov si,10d\n" +
            " xor dx,dx\n" +
            " push 10 ; newline\n" +
            " mov cx,1d\n" +
            "nonzero:\n" +
            " div si\n" +
            " add dx,48d\n" +
            " push dx\n" +
            " inc cx\n" +
            " xor dx,dx\n" +
            " cmp ax,0h\n" +
            " jne nonzero\n" +
            "writeloop:\n" +
            " pop dx\n" +
            " mov ah,02h\n" +
            " int 21h\n" +
            " dec cx\n" +
            " jnz writeloop \n" +
            " mov dl, 13 ; carriage return\n" +
            " mov ah, 02h ; \n" +
            " int 21h ;  \n" +
            "ret\n" +
            "endp\n";

    public static String handleDiv="handlediv proc\n" +
            " cmp ax,0\n" +
            " jg axpos\n" +
            " xor dx, dx\n" +
            " cwd\n" +
            " idiv bx\n" +
            " ret\n" +
            "axpos:\n" +
            " cmp bx,0\n" +
            " jge bxpos\n" +
            " xor dx, dx\n" +
            " idiv bx\n" +
            " ret\n" +
            "bxpos:\n" +
            " xor dx, dx\n" +
            " div bx\n" +
            " ret\n";

    public static String printProc=
            "myprint proc\n" +
                    " mov si,10d\n" +
                    " xor dx,dx\n" +
                    " push 10 ; newline\n" +
                    " mov cx,1d\n" +
                    "nonzero:\n" +
                    " div si\n" +
                    " add dx,48d\n" +
                    " push dx\n" +
                    " inc cx\n" +
                    " xor dx,dx\n" +
                    " cmp ax,0h\n" +
                    " jne nonzero\n" +
                    "writeloop:\n" +
                    " pop dx\n" +
                    " mov ah,02h\n" +
                    " int 21h\n" +
                    " dec cx\n" +
                    " jnz writeloop \n" +
                    " mov dl, 13 ; carriage return\n" +
                    " mov ah, 02h ; \n" +
                    " int 21h ;  \n" +
                    "ret\n" +
                    "endp\n";

    public static String dosTerminate=";dos terminate\nmov ah,0x4C\nint 0x21\n";

    public static void main(String[] args) throws Exception{
        String inputFile = null;
        if ( args.length>0 ) inputFile = args[0];
        InputStream is = System.in;
        if ( inputFile!=null ) {
            is = new FileInputStream(inputFile);
        }
        ANTLRInputStream input = new ANTLRInputStream(is);
        SimpleLexer lexer = new SimpleLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SimpleParser parser = new SimpleParser(tokens);
        ParseTree tree = parser.prog(); // parse

        SimpleToA86Visitor v = new SimpleToA86Visitor();
        String out="org 100h\n";
        out=out+v.visit(tree);
        out=out+dosTerminate;
        out=out+"\n";
        out=out+printProc2;
        out=out+handleDiv;

        Iterator it = v.variableDefs.iterator();
        String vardefs="";
        while (it.hasNext()) {
            String varName = (String)it.next();
            vardefs=vardefs+(varName+" dw ?\n");
        }
        out=out+vardefs;

        PrintWriter writer = new PrintWriter("out.asm", "UTF-8");
        writer.println(out);
        writer.close();
    }
}
