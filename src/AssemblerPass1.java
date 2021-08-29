
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class AssemblerPass1 {
    public static void main(String[] args) throws Exception {
        int locCounter = 0;
        int reg = 0;
        int symNo = 1;
        int litNo = 1;
        String name, s2, s3 = null;
        String instOPCODE = null;
        String[] s1;
        String[] operandSpec;

        FileReader fr = new FileReader("input.txt");
        BufferedReader br = new BufferedReader(fr);

        FileWriter fw = new FileWriter("output.txt");
        BufferedWriter bw = new BufferedWriter(fw);

        FileWriter fwSym = new FileWriter("symboltable.txt");
        BufferedWriter bwSym = new BufferedWriter(fwSym);

        FileWriter fwLit = new FileWriter("literaltable.txt");
        BufferedWriter bwLit = new BufferedWriter(fwLit);

        // Hashtable for Imperative Statements
        Hashtable<String, String> IS = new Hashtable<String, String>();
        IS.put("STOP", "00");
        IS.put("ADD", "01");
        IS.put("SUB", "02");
        IS.put("MULT", "03");
        IS.put("MOVER", "04");
        IS.put("MOVEM", "05");
        IS.put("COMP", "06");
        IS.put("BC", "07");
        IS.put("DIV", "08");
        IS.put("READ", "09");
        IS.put("PRINT", "10");

        // Hashtable for Assembler Directives
        Hashtable<String, String> AD = new Hashtable<String, String>();
        AD.put("START", "01");
        AD.put("END", "02");
        AD.put("ORIGIN", "03");
        AD.put("EQU", "04");
        AD.put("LTORG", "05");

        // Hashtable for Declarative Statements
        Hashtable<String, String> DL = new Hashtable<String, String>();
        DL.put("DC", "01");
        DL.put("DS", "02");

        br.close();
        bw.close();
        bwSym.close();
        bwLit.close();
    }
}
