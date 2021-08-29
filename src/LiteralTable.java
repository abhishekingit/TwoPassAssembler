public class LiteralTable {
    int litNumber;
    int litAddr;

    public LiteralTable(int literalNumber, int literalAddress) {
        this.litNumber = literalNumber;
        this.litAddr = literalAddress;
    }

    public int getLiteralNumber() {
        return litNumber;
    }

    public int getLiteralAddress() {
        return litAddr;
    }

    public String getLiteralTable() {
        return (this.getLiteralNumber() + "\t" + this.getLiteralAddress());
    }

}
