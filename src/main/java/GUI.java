import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GUI extends JFrame {
    private JComboBox targetsList;

    public GUI(List<Rule> rules, Map<String, Set<String>> attributesSets, Set<String> targets) throws HeadlessException {
        super("База знаний");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setPreferredSize(new Dimension(600, 200));
        Container container = this.getContentPane();
        container.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel labelChoose = new JLabel("Выберите целевой признак: ");
        container.add(labelChoose);
        targetsList = new JComboBox();
        for (String target : targets) {
            targetsList.addItem(target);
        }
        container.add(targetsList);
        Algorithm algorithm = new Algorithm(rules, attributesSets);
        JButton button = new JButton("Начать");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String answer = algorithm.run(targetsList.getSelectedItem().toString(), GUI.this);
                System.out.println("Ответ: " + answer);
                JOptionPane.showMessageDialog(GUI.this, "Ответ: " + answer);
            }
        });
        container.add(button);
        pack();
    }
}
