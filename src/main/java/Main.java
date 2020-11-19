import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    private static List<Rule> rules = new ArrayList<>();
    private static Map<String, Set<String>> attributesSets = new HashMap<>();
    private static Set<String> targets = new HashSet<>();

    private static void readRules(String fileName) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader(fileName);
        JSONArray rulesArray = (JSONArray) jsonParser.parse(reader);
        rulesArray.forEach(rule -> {
            Rule newRule = new Rule();
            JSONArray ifCase = (JSONArray) ((JSONObject) rule).get("если");
            ifCase.forEach(attribute -> {
                String name = ((JSONObject) attribute).get("атрибут").toString();
                String value = ((JSONObject) attribute).get("значение").toString();
                newRule.addAttribute(new AttributeValue(name, value));
                Set<String> set = attributesSets.getOrDefault(name, new HashSet<>());
                set.add(value);
                attributesSets.put(name, set);
            });
            JSONObject thenCase = (JSONObject) ((JSONObject) rule).get("то");
            String name = thenCase.get("атрибут").toString();
            String value = thenCase.get("значение").toString();
            newRule.setResult(new AttributeValue(name, value));
            rules.add(newRule);
            targets.add(name);
            Set<String> set = attributesSets.getOrDefault(name, new HashSet<>());
            set.add(value);
            attributesSets.put(name, set);
        });
        for (Set<String> set : attributesSets.values()) {
            set.add("другой ответ");
        }
    }

    public static void main(String[] args) {
        try {
            readRules("input.json");
            GUI gui = new GUI(rules, attributesSets, targets);
        } catch (IOException | ParseException exception) {
            exception.printStackTrace();
        }
    }
}
