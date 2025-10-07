package nl.bioinf.io;

public enum Taxon {
    U("Unclassified"),
    R("Root"),
    D("Domain"),
    K("Kingdom"),
    P("Phylum"),
    C("Class"),
    O("Order"),
    F("Family"),
    G("Genus"),
    S("Species"),
    PC("Primitive Classification"),
    A("ALL");
    private final String name;

    Taxon(String name){this.name = name;}

    public static Taxon fromString(String taxon){
        if(taxon.isEmpty()){return Taxon.PC;}
        char temp = taxon.charAt(0);
        String input = Character.toString(temp);
        switch (input){
            case "U": return Taxon.U;
            case "R": return Taxon.R;
            case "D": return Taxon.D;
            case "K": return Taxon.K;
            case "P": return Taxon.P;
            case "C": return Taxon.C;
            case "O": return Taxon.O;
            case "F": return Taxon.F;
            case "G": return Taxon.G;
            case "S": return Taxon.S;
            case "A": return Taxon.A;
            case "1": return Taxon.PC; //TODO ander fiks?
            default: throw new IllegalArgumentException("Unknown taxon: " + taxon+" end");
        }
    }

    @Override
    public String toString(){return name;}
}
