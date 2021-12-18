package editor;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class TextEditor extends JFrame {
    JTextArea jTextArea;
    JScrollPane jScrollPane;

    JTextField jTextField;
    JButton loadButton;
    JButton saveButton;
    String dir = System.getProperty("user.dir") + "\\Text Editor\\task\\src\\";

    public TextEditor() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Text Editor");
        setSize(300, 300);
        setVisible(true);

        jTextField = new JTextField();
        jTextField.setPreferredSize(new Dimension(100, 30));
        loadButton = new JButton("Load");

        saveButton = new JButton("Save");

        loadButton.addActionListener(e -> {
            String path = jTextField.getText();
            StringBuilder content = new StringBuilder();
            if (new File(path).exists()) {
                try {
                    String str = Files.readString(Paths.get(path));
                    byte[] bytes = Files.readAllBytes(Paths.get(path));
                    jTextArea.setText(new String(bytes));

                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            } else {
                jTextArea.setText("");
            }
        });

        saveButton.addActionListener(e -> {
            String path = jTextField.getText();

            FileOutputStream fos = null;

            try {
                fos = new FileOutputStream(path);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                bos.write(jTextArea.getText().getBytes()); // in loop probably
                bos.close();
            } catch (IOException exception ) {
                exception.printStackTrace();
            }
        });


        jTextArea = new JTextArea();


        jScrollPane = new JScrollPane(jTextArea);

        jScrollPane.setPreferredSize(new Dimension(200, 200));
        //jScrollPane.setVerticalScrollBar(new JScrollBar(Adjustable.VERTICAL));


        JPanel northPanel = new JPanel();
        northPanel.setLayout(new FlowLayout());
        northPanel.add(jTextField);
        northPanel.add(loadButton);
        northPanel.add(saveButton);

        JPanel jPanel = new JPanel();
        jPanel.add(jScrollPane);

        setLayout(new BorderLayout());

        add(northPanel, BorderLayout.NORTH);
        add(jPanel, BorderLayout.CENTER);
        setNames();
        pack();
    }

    private void setNames() {
        jTextArea.setName("TextArea");
        jTextField.setName("FilenameField");
        loadButton.setName("LoadButton");
        saveButton.setName("SaveButton");
        jScrollPane.setName("ScrollPane");
    }

    void sleep(int x) {
        try {
            Thread.sleep(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
