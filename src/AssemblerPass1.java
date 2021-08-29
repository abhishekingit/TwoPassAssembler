
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
// import java.io.IOException;
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

        // HashMap for Symbol table
        HashMap<String, SymbolTable> symbTable = new HashMap<>();

        // HashMap for Literal table
        HashMap<String, LiteralTable> litTable = new HashMap<>();

        // LiteralQueue for storing literals in FIFO
        List<String> litqueue = new ArrayList<>();

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

        while ((name = br.readLine()) != null) {
            s1 = name.split(" ");
            if (s1[1].equals("START")) {
                s2 = s1[2];
                locCounter = Integer.parseInt(s2);
                for (String entry : AD.keySet()) {
                    if (s1[1].equals(entry)) {
                        instOPCODE = AD.get(entry);
                    }
                }
                bw.write("-\t" + "AD," + instOPCODE + "\t-\t" + "C, " + locCounter + "\r\n");

            } else if (IS.containsKey(s1[1])) {
                s2 = s1[2];
                s3 = s1[3];
                if (s2.equals("AREG")) {
                    reg = 1;
                } else if (s2.equals("BREG")) {
                    reg = 2;
                } else if (s2.equals("CREG")) {
                    reg = 3;
                } else {
                    reg = 4;
                }
                instOPCODE = IS.get(s1[1]);
                if (s3.contains("=")) {
                    litTable.put(s3, new LiteralTable(litNo, 0));
                    litqueue.add(s3);
                    bw.write(locCounter + "\t" + "IS," + instOPCODE + "\t" + reg + "\t" + "L, " + litNo + "\r\n");
                    litNo++;
                } else {
                    if (symbTable.containsKey(s3)) {
                        SymbolTable tempSym = symbTable.get(s3);
                        bw.write(locCounter + "\t" + "IS," + instOPCODE + "\t" + reg + "\t" + "S, "
                                + tempSym.getSymbolNumber() + "\r\n");

                    } else {
                        symbTable.put(s3, new SymbolTable(symNo, 0, "1"));
                        bw.write(locCounter + "\t" + "IS," + instOPCODE + "\t" + reg + "\t" + "S, " + symNo + "\r\n");
                        symNo++;
                    }

                }

                if (!s1[0].equals("-")) {
                    symbTable.put(s1[0], new SymbolTable(symNo, locCounter, "1"));
                    symNo++;
                }

                locCounter++;

            } else if (AD.containsKey(s1[1])) {
                instOPCODE = AD.get(s1[1]);
                if (s1[1].equals("ORIGIN")) {
                    operandSpec = s1[2].split("\\+");
                    if (symbTable.containsKey(operandSpec[0])) {
                        SymbolTable sym1 = symbTable.get(operandSpec[0]);
                        bw.write("\t" + "AD," + instOPCODE + "\t-\t" + "(S, " + sym1.getSymbolNumber() + ")+"
                                + operandSpec[1] + "\r\n");
                        locCounter = sym1.getSymbolAddress() + Integer.parseInt(operandSpec[1]);
                    } else {
                        locCounter = Integer.parseInt(s1[2]);
                    }
                } else if (s1[1].equals("EQU")) {
                    if (symbTable.containsKey(s1[2])) {
                        SymbolTable sym1 = symbTable.get(s1[2]);
                        bw.write("\t" + "AD," + instOPCODE + "\t-\t" + "S, " + sym1.getSymbolNumber() + "\r\n");
                        symbTable.put(s1[0], new SymbolTable(symNo, sym1.getSymbolAddress(), "1"));
                        symNo++;
                    } else {
                        bw.write("\t" + "AD," + instOPCODE + "\t-\t" + "S, " + s1[2] + "\r\n");
                        symbTable.put(s1[0], new SymbolTable(symNo, Integer.parseInt(s1[2]), "1"));
                        symNo++;
                    }
                } else if (s1[1].equals("LTORG")) {
                    for (String lit : litqueue) {
                        int tempLit = litTable.get(lit).getLiteralNumber();
                        bw.write(locCounter + "\t" + "AD," + instOPCODE + "\t-\t" + "00" + lit + "\r\n");
                        litTable.replace(lit, new LiteralTable(tempLit, locCounter));
                        locCounter++;
                    }
                    for (int i = 0; i < litqueue.size(); i++) {
                        litqueue.remove(i);
                    }

                } else if (s1[1].equals("END")) {
                    for (String lit : litqueue) {
                        int tempLit = litTable.get(lit).getLiteralNumber();
                        bw.write(locCounter + "\t" + "AD," + instOPCODE + "\t-\t" + "00" + lit + "\r\n");
                        litTable.replace(lit, new LiteralTable(tempLit, locCounter));
                        litqueue.remove(0);
                    }
                }

            } else if (DL.containsKey(s1[1])) {
                instOPCODE = DL.get(s1[1]);
                bw.write(locCounter + "\t" + "DL," + instOPCODE + "\t-\t" + "C, " + s1[2] + "\r\n");
                if (s1[1].equals("DC")) {
                    int tempSym = symbTable.get(s1[0]).getSymbolNumber();
                    symbTable.replace(s1[0], new SymbolTable(tempSym, locCounter, "1"));
                    locCounter++;
                } else {
                    int tempSym = symbTable.get(s1[0]).getSymbolNumber();
                    symbTable.replace(s1[0], new SymbolTable(tempSym, locCounter, s1[2]));
                    locCounter = locCounter + Integer.parseInt(s1[2]);
                }

            }

        }

        for (Map.Entry symElement : symbTable.entrySet()) {
            String symKey = (String) symElement.getKey();
            SymbolTable sym1 = symbTable.get(symKey);
            bwSym.write(sym1.getSymbolNumber() + "\t" + symKey + "\t" + sym1.getSymbolAddress() + "\t"
                    + sym1.getSymbolLength() + "\r\n");
        }
        for (Map.Entry litElement : litTable.entrySet()) {
            String litKey = (String) litElement.getKey();
            LiteralTable lit1 = litTable.get(litKey);
            bwLit.write(lit1.getLiteralNumber() + "\t" + litKey + "\t" + lit1.getLiteralAddress() + "\r\n");
        }

        br.close();
        bw.close();
        bwSym.close();
        bwLit.close();

        symbTable.clear();
        litTable.clear();
    }
}
