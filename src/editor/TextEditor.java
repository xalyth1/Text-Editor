package editor;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class TextEditor extends JFrame {
    private String currentFilePath;

    JTextArea jTextArea;
    JScrollPane jScrollPane;

    JTextField jTextField;
    JButton loadButton;
    JButton saveButton;
    private JButton searchButton;
    private JButton nextButton;
    private JButton previousButton;
    private JCheckBox jCheckBox;
    private JLabel jLabel;

    //String dir = System.getProperty("user.dir") + "\\Text Editor\\Text Editor\\task\\src\\";
    private JMenuBar jMenuBar;
    private JMenu fileJMenu;
    private JMenuItem loadJMenuItem;
    private JMenuItem saveJMenuItem;
    private JMenuItem exitJMenuItem;



    public TextEditor() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Text Editor");
        setSize(600, 600);
        setPreferredSize(new Dimension(600,600));
        setVisible(true);

        jTextField = new JTextField();
        jTextField.setPreferredSize(new Dimension(100, 30));
        //System.out.println( "ścieżka " + System.getProperty("user.dir"));
        ImageIcon openFileImageIcon = new ImageIcon(
                "C:\\Users\\Pawel\\IdeaProjects\\Text Editor\\Text Editor\\icons\\open-file.jpg",
                "open file image-icon");

        loadButton = new JButton(openFileImageIcon);

        ImageIcon saveFileImageIcon = new ImageIcon(
                System.getProperty("user.dir") + "\\Text Editor\\icons\\save.png",
                "save file image-icon");


        saveButton = new JButton(saveFileImageIcon);

        loadButton.addActionListener(e -> {
            JFileChooser jfc = new JFileChooser(System.getProperty("user.dir"));

            int returnValue = jfc.showOpenDialog(null);
            // int returnValue = jfc.showSaveDialog(null);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = jfc.getSelectedFile();
                String path = selectedFile.getAbsolutePath();
                //System.out.println(path);
                if (selectedFile.exists()) {
                try {
                    String str = Files.readString(Paths.get(path));
                    byte[] bytes = Files.readAllBytes(Paths.get(path));
                    jTextArea.setText(new String(bytes));
                    currentFilePath = path;
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            } else {
                jTextArea.setText("");
            }
            }
        });

        saveButton.addActionListener(e -> {
            if (currentFilePath != null && currentFilePath != "") {
                try {
                    FileOutputStream fos = new FileOutputStream(currentFilePath);
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    bos.write(jTextArea.getText().getBytes());
                    bos.close();
                } catch (IOException exception ) {
                    exception.printStackTrace();
                }
            }
        });


        ImageIcon searchImageIcon = new ImageIcon(
                System.getProperty("user.dir") + "\\Text Editor\\icons\\search.png",
                "search image-icon");

        searchButton = new JButton(searchImageIcon);

        searchButton.addActionListener( e -> {
            Search search = new Search(jTextField.getText(), jCheckBox.isSelected(), jTextArea.getText());
            search.start();
            try {
                search.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            System.out.println(search.getResult());
        });

        ImageIcon previousImageIcon = new ImageIcon(
                System.getProperty("user.dir") + "\\Text Editor\\icons\\previous.png",
                "previous image-icon");

        previousButton = new JButton(previousImageIcon);

        ImageIcon nextImageIcon = new ImageIcon(
                System.getProperty("user.dir") + "\\Text Editor\\icons\\next.png",
                "nest image-icon");

        nextButton = new JButton(nextImageIcon);


        jCheckBox = new JCheckBox();
        jLabel = new JLabel("Use regex");




        jTextArea = new JTextArea();


        jScrollPane = new JScrollPane(jTextArea);

        jScrollPane.setPreferredSize(new Dimension(600, 500));
        //jScrollPane.setVerticalScrollBar(new JScrollBar(Adjustable.VERTICAL));


        JPanel northPanel = new JPanel();
        northPanel.setLayout(new FlowLayout());
        //northPanel.setAlignmentX(0);
        northPanel.add(loadButton);
        northPanel.add(saveButton);
        northPanel.add(jTextField);
        northPanel.add(searchButton);
        northPanel.add(previousButton);
        northPanel.add(nextButton);
        northPanel.add(jCheckBox);
        northPanel.add(jLabel);


        JPanel jPanel = new JPanel();
        jPanel.add(jScrollPane);

        createMenu();

        setLayout(new BorderLayout());

        add(northPanel, BorderLayout.NORTH);
        add(jPanel, BorderLayout.CENTER);
        setNames();
        pack();
    }

    private void createMenu() {
        jMenuBar = new JMenuBar();
        setJMenuBar(jMenuBar);

        fileJMenu = new JMenu("File");
        loadJMenuItem = new JMenuItem("Load");
        saveJMenuItem = new JMenuItem("Save");
        exitJMenuItem = new JMenuItem("Exit");

        jMenuBar.add(fileJMenu);
        fileJMenu.add(loadJMenuItem);
        fileJMenu.add(saveJMenuItem);
        fileJMenu.add(exitJMenuItem);

        //System.out.println("listeners:" + loadButton.getActionListeners().length);
        loadJMenuItem.addActionListener(loadButton.getActionListeners()[0]);
        saveJMenuItem.addActionListener(saveButton.getActionListeners()[0]);
        exitJMenuItem.addActionListener(e -> System.exit(0));

    }

    private void setNames() {
        jTextArea.setName("TextArea");
        jTextField.setName("FilenameField");
        loadButton.setName("LoadButton");
        saveButton.setName("SaveButton");
        jScrollPane.setName("ScrollPane");

        fileJMenu.setName("MenuFile");
        loadJMenuItem.setName("MenuLoad");
        saveJMenuItem.setName("MenuSave");
        exitJMenuItem.setName("MenuExit");
    }

    void sleep(int x) {
        try {
            Thread.sleep(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
