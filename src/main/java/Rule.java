import java.util.ArrayList;
import java.util.List;

public class Rule {
    private List<AttributeValue> attributeValues;
    private AttributeValue result;

    public Rule() {
        attributeValues = new ArrayList<>();
    }

    public void setAttributes(List<AttributeValue> attributeValues) {
        this.attributeValues = attributeValues;
    }

    public void setResult(AttributeValue result) {
        this.result = result;
    }

    public List<AttributeValue> getAttributeValues() {
        return attributeValues;
    }

    public AttributeValue getResult() {
        return result;
    }

    public String getValueByAttribute(String attribute) {
        for (AttributeValue attributeValue : attributeValues) {
            if (attributeValue.getName().equals(attribute)) {
                return attributeValue.getValue();
            }
        }
        return null;
    }

    public void addAttribute(AttributeValue newAttributeValue) {
        for (AttributeValue attributeValue : attributeValues) {
            if (attributeValue.getName().equals(newAttributeValue.getName())) {
                attributeValues.remove(attributeValue);
                break;
            }
        }
        attributeValues.add(newAttributeValue);
    }
}
