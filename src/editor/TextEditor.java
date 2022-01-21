package editor;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

public class TextEditor extends JFrame {
    private LinkedList<Search.Indices> list;
    private int currentResultIndex = 0;

    private JTextArea jTextArea;
    private JScrollPane jScrollPane;

    private JTextField jTextField;
    private JButton loadButton;
    private JButton saveButton;
    private JButton searchButton;
    private JButton nextButton;
    private JButton previousButton;
    private JCheckBox jCheckBox;
    private JLabel useRegexJLabel = new JLabel("Use regex");

    private IconManager iconManager = new IconManager();

    private JFileChooser jFileChooser = new JFileChooser(System.getProperty("user.dir"));

    //String dir = System.getProperty("user.dir");
    private JMenuBar jMenuBar;
    private JMenu fileJMenu;
    private JMenuItem loadJMenuItem;
    private JMenuItem saveJMenuItem;
    private JMenuItem exitJMenuItem;

    private JMenu jSearchMenu;
    private JMenuItem startSearchItem;
    private JMenuItem previousItem;
    private JMenuItem nextItem;
    private JMenuItem useRegexItem;

    public TextEditor() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Text Editor");
        setSize(600, 600);
        setPreferredSize(new Dimension(625, 600));
        setVisible(true);

        jTextField = new JTextField();
        jTextField.setPreferredSize(new Dimension(100, 30));

        createButtons();

        loadButton.addActionListener(e -> {
            System.out.println("Loading file...");
            synchronized (this) {
                int returnValue = jFileChooser.showOpenDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = jFileChooser.getSelectedFile();

                    System.out.println("   Selelected file: " + selectedFile.toString());

                    String path = selectedFile.getAbsolutePath();
                    System.out.println("File to load path: " + path);
                    if (selectedFile.exists()) {
                        System.out.println("Selected file exists");
                        try {
                            //String str = Files.readString(Paths.get(path));
                            byte[] bytes = Files.readAllBytes(Paths.get(path));
                            jTextArea.setText(new String(bytes));
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                    } else {
                        jTextArea.setText("");
                    }
                }
            }
        });

        saveButton.addActionListener(e -> {
            int returnValue = jFileChooser.showSaveDialog(null);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = jFileChooser.getSelectedFile();
                String path = selectedFile.getAbsolutePath();

                System.out.println("Path to save file: " + path);

                if (selectedFile.exists()) {
                    System.out.println("   Selected file exists");
                    try {
                        FileOutputStream fos = new FileOutputStream(path);
                        BufferedOutputStream bos = new BufferedOutputStream(fos);

                        bos.write(jTextArea.getText().getBytes());
                        bos.close();
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                } else {
                    System.out.println("   Selected file does not exist. Creating file");
                    try {
                        FileOutputStream fos = new FileOutputStream(path);
                        BufferedOutputStream bos = new BufferedOutputStream(fos);

                        bos.write(jTextArea.getText().getBytes());
                        bos.close();
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });

        searchButton.addActionListener(e -> {
            Search search = new Search(jTextField.getText(), jCheckBox.isSelected(), jTextArea.getText());
            search.start();
            try {
                search.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            System.out.println(search.getResult());
            list = search.getResult();

            if (list.size() != 0) {
                Search.Indices ind = list.get(0);
                this.currentResultIndex = 0;

                jTextArea.setCaretPosition(ind.start);
                jTextArea.select(ind.start, ind.end);
                jTextArea.grabFocus();
            }
        });

        previousButton.addActionListener(e -> {
            synchronized (this) {
                if (currentResultIndex - 1 < 0) {
                    currentResultIndex = list.size() - 1;
                } else {
                    currentResultIndex--;
                }
                Search.Indices ind = list.get(currentResultIndex);

                jTextArea.setCaretPosition(ind.start);
                jTextArea.select(ind.start, ind.end);
                jTextArea.grabFocus();
            }
        });

        nextButton.addActionListener(e -> {
            synchronized (this) {
                if (currentResultIndex + 1 >= list.size()) {
                    currentResultIndex = 0;
                } else {
                    currentResultIndex++;
                }

                Search.Indices ind = list.get(currentResultIndex);

                jTextArea.setCaretPosition(ind.start);
                jTextArea.select(ind.start, ind.end);
                jTextArea.grabFocus();
            }
        });

        jCheckBox = new JCheckBox();

        jTextArea = new JTextArea();
        jTextArea.setFont(new Font("Arial Black", Font.BOLD, 18));

        jScrollPane = new JScrollPane(jTextArea);

        jScrollPane.setPreferredSize(new Dimension(600, 500));

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new FlowLayout());
        northPanel.add(loadButton);
        northPanel.add(saveButton);
        northPanel.add(jTextField);
        northPanel.add(searchButton);
        northPanel.add(previousButton);
        northPanel.add(nextButton);
        northPanel.add(jCheckBox);
        northPanel.add(useRegexJLabel);

        JPanel jPanel = new JPanel();
        jPanel.add(jScrollPane);

        createMenu();

        add(jFileChooser);

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

        jSearchMenu = new JMenu("Search");
        jMenuBar.add(jSearchMenu);

        startSearchItem = new JMenuItem("Start search");
        previousItem = new JMenuItem("Previous match");
        nextItem = new JMenuItem("Next match");
        useRegexItem = new JMenuItem("Use regular expressions");

        jSearchMenu.add(startSearchItem);
        jSearchMenu.add(previousItem);
        jSearchMenu.add(nextItem);
        jSearchMenu.add(useRegexItem);

        JMenu viewMenu = new JMenu("View");
        jMenuBar.add(viewMenu);


        JMenuItem largerFontMenuItem = new JMenuItem("Increase font size");
        viewMenu.add(largerFontMenuItem);
        largerFontMenuItem.addActionListener(e -> {
            int size = jTextArea.getFont().getSize();
            String fontName = jTextArea.getFont().getFontName();
            int style = jTextArea.getFont().getStyle();
            jTextArea.setFont(new Font(fontName, style,size + 2));
        });

        JMenuItem smallerFontMenuItem = new JMenuItem("Decrease font size");
        viewMenu.add(smallerFontMenuItem);
        smallerFontMenuItem.addActionListener(e -> {
            int size = jTextArea.getFont().getSize();
            String fontName = jTextArea.getFont().getFontName();
            int style = jTextArea.getFont().getStyle();
            jTextArea.setFont(new Font(fontName, style,size - 2));
        });

        JMenuItem changeFontColorMenuItem = new JMenuItem("Change font color");
        viewMenu.add(changeFontColorMenuItem);
        changeFontColorMenuItem.addActionListener(e ->
            jTextArea.setForeground(JColorChooser.showDialog(null, "Choose Font Color", Color.BLACK)));

        JMenuItem changeBackgroundColorMenuItem = new JMenuItem("Change text background color");
        viewMenu.add(changeBackgroundColorMenuItem);
        changeBackgroundColorMenuItem.addActionListener(e ->
            jTextArea.setBackground(JColorChooser.showDialog(null, "Choose Font Color", Color.BLACK)));



        startSearchItem.addActionListener(searchButton.getActionListeners()[0]);
        previousItem.addActionListener(previousButton.getActionListeners()[0]);
        nextItem.addActionListener(nextButton.getActionListeners()[0]);
        useRegexItem.addActionListener(e -> jCheckBox.setSelected(!jCheckBox.isSelected()));

        //System.out.println("listeners:" + loadButton.getActionListeners().length);
        loadJMenuItem.addActionListener(loadButton.getActionListeners()[0]);
        saveJMenuItem.addActionListener(saveButton.getActionListeners()[0]);
        exitJMenuItem.addActionListener(e -> System.exit(0));

    }

    private void createButtons() {
        loadButton = new JButton(iconManager.getOpenFileImageIcon());
        saveButton = new JButton(iconManager.getSaveFileImageIcon());
        searchButton = new JButton(iconManager.getSearchImageIcon());
        previousButton = new JButton(iconManager.getPreviousImageIcon());
        nextButton = new JButton(iconManager.getNextImageIcon());
    }

    private void setNames() {
        jTextArea.setName("TextArea");
        jTextField.setName("SearchField");
        loadButton.setName("OpenButton");
        saveButton.setName("SaveButton");
        searchButton.setName("StartSearchButton");
        previousButton.setName("PreviousMatchButton");
        nextButton.setName("NextMatchButton");
        jCheckBox.setName("UseRegExCheckbox");
        jFileChooser.setName("FileChooser");
        useRegexItem.setName("MenuUseRegExp");

        jSearchMenu.setName("MenuSearch");
        startSearchItem.setName("MenuStartSearch");
        previousItem.setName("MenuPreviousMatch");
        nextItem.setName("MenuNextMatch");

        jScrollPane.setName("ScrollPane");

        fileJMenu.setName("MenuFile");
        loadJMenuItem.setName("MenuOpen");
        saveJMenuItem.setName("MenuSave");
        exitJMenuItem.setName("MenuExit");
    }

    void sleep(int timeInMillis) {
        try {
            Thread.sleep(timeInMillis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
