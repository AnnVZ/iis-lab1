public class AttributeValue {
    private String name;
    private String value;

    public AttributeValue(String attribute, String value) {
        this.name = attribute;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
