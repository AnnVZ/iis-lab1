import javax.swing.*;
import java.util.*;

public class Algorithm {
    private List<Rule> rules;
    private Map<String, Set<String>> attributesSets;
    private boolean logicalAttribute;

    public Algorithm(List<Rule> rules, Map<String, Set<String>> attributesSets) {
        this.rules = rules;
        this.attributesSets = attributesSets;
    }

    private int checkRule(int ruleNumber, Map<String, String> contextStack) {
        Rule rule = rules.get(ruleNumber);
        for (AttributeValue attributeValue : rule.getAttributeValues()) {
            String value = contextStack.get(attributeValue.getName());
            if (value == null) {
                return -1;
            }
            if (!value.equals(attributeValue.getValue())) {
                return 0;
            }
        }
        return 1;
    }

    private void handleRuleCheckResult(int rule, String target, int checkResult, Deque<String> targetsStack, Deque<Integer> ruleNumbersStack, Map<String, String> contextStack, List<Integer> acceptedRules, List<Integer> deniedRules) {
        if (checkResult == 1) {
            contextStack.put(rules.get(rule).getResult().getName(), rules.get(rule).getResult().getValue());
            acceptedRules.add(rule);
            System.out.println("стек контекста: " + contextStack);
            System.out.println("колода принятых правил: " + acceptedRules);
            targetsStack.removeLast();
            ruleNumbersStack.removeLast();
            if (targetsStack.isEmpty()) {
                logicalAttribute = true;
            }
        } else if (checkResult == 0) {
            deniedRules.add(rule);
            System.out.println("колода сброса: " + deniedRules);
        } else {
            for (int j = 0; j < rules.get(rule).getAttributeValues().size(); j++) {
                String attribute = rules.get(rule).getAttributeValues().get(j).getName();
                if (!contextStack.containsKey(attribute)) {
                    targetsStack.add(attribute);
                    System.out.println("стек целей: " + targetsStack);
                    ruleNumbersStack.add(rule);
                    break;
                }
            }
        }
    }

    public String run(String target, GUI gui) {
        List<Integer> acceptedRules = new ArrayList<>();
        List<Integer> deniedRules = new ArrayList<>();
        Map<String, String> contextStack = new HashMap<>();
        Deque<String> targetsStack = new ArrayDeque<>();
        targetsStack.add(target);
        System.out.println("стек целей: " + targetsStack);
        Deque<Integer> ruleNumbersStack = new ArrayDeque<>();
        ruleNumbersStack.add(-1);
        logicalAttribute = false;
        while (!logicalAttribute) {
            String currentTarget = targetsStack.getLast();
            boolean ruleFound = false;
            for (int i = 0; i < rules.size(); i++) {
                if (rules.get(i).getResult().getName().equals(currentTarget) && !deniedRules.contains(i) && !acceptedRules.contains(i)) {
                    ruleFound = true;
                    int check = checkRule(i, contextStack);
                    System.out.println("правило: " + i + " - " + (check == 1 ? "истина" : check == 0 ? "ложь" : "неизвестно"));
                    handleRuleCheckResult(i, currentTarget, check, targetsStack,  ruleNumbersStack,  contextStack,  acceptedRules,  deniedRules);
                    break;
                }
            }
            if (!ruleFound) {
                if (!contextStack.containsKey(currentTarget) && !currentTarget.equals(target)) {
                    Scanner in = new Scanner(System.in);
                    StringBuilder values = new StringBuilder();
                    for (String str : attributesSets.get(currentTarget)) {
                        values.append(str + ", ");
                    }
                    String currentTargetValue = JOptionPane.showInputDialog(gui, "Введите " + currentTarget + "(" + values.substring(0, values.length() - 2) + "): ");
                    if (currentTargetValue == null) {
                        return "отменено";
                    }
                    contextStack.put(currentTarget, currentTargetValue);
                    System.out.println("стек контекста: " + contextStack);
                    targetsStack.removeLast();
                    System.out.println("стек целей: " + targetsStack);
                    int rule = ruleNumbersStack.removeLast();
                    int check = checkRule(rule, contextStack);
                    System.out.println("правило: " + rule + " - " + (check == 1 ? "истина" : check == 0 ? "ложь" : "неизвестно"));
                    handleRuleCheckResult(rule, currentTarget, check, targetsStack,  ruleNumbersStack,  contextStack,  acceptedRules,  deniedRules);
                } else {
                    logicalAttribute = true;
                }
            }
        }
        return contextStack.getOrDefault(target, "нет ответа");
    }
}
