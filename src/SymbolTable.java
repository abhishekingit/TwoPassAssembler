public class SymbolTable {
    int symNumber;
    int symAddr;
    String length;

    public SymbolTable(int symbolNumber, int symbolAddress, String symbolLength) {
        this.symNumber = symbolNumber;
        this.symAddr = symbolAddress;
        this.length = symbolLength;
    }

    public int getSymbolNumber() {
        return symNumber;
    }

    public int getSymbolAddress() {
        return symAddr;
    }

    public String getSymbolLength() {
        return length;
    }

    public String getSymbolTable() {
        return (this.getSymbolNumber() + "\t" + this.getSymbolAddress() + "\t" + this.getSymbolLength());
    }

}
