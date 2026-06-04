package laki.webide.core;

public class CreateBlock {
    public String type;
    public String spec;
    public String category;
    public String cssTemplate;
    public String opCode;

    public CreateBlock(String type, String spec, String category, String opCode, String cssTemplate) {
        this.type = type;
        this.spec = spec;
        this.category = category;
        this.opCode = opCode;
        this.cssTemplate = cssTemplate;
    }

    public int getColor() {
        return CategoryRegistry.getColor(this.category);
    }
}
