import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class TextEdtitor extends JFrame{
    private JPanel MainPanel;
    private JTabbedPane editor;
    private JButton newFile;
    private JButton openFile;
    private JButton save;
    private JPanel fileView;
    private JPanel getStarted;
    private JLabel nfprompt;
    private JButton okButton;
    private JButton closeFileButton;
    private JSlider textSizeSlider;
    private JLabel ofprompt;

    public TextEdtitor() {
        this.setContentPane(MainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Text Edtitor");
        this.setSize(800, 500);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editor.removeTabAt(0);
            }
        });
        newFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextArea jTextArea = new JTextArea();
                jTextArea.setLineWrap(true);
                jTextArea.setWrapStyleWord(false);

                JScrollPane jScrollPane = new JScrollPane(jTextArea);
                jScrollPane.setFocusable(false);
                jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

                editor.addTab("Untitled", jScrollPane);
            }
        });
        openFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setCurrentDirectory(new File("."));

                int response = jFileChooser.showOpenDialog(null);

                if (response == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = jFileChooser.getSelectedFile();
                    JTextArea jTextArea = new JTextArea();

                    try (Scanner fileIn = new Scanner(selectedFile)) {
                        while (fileIn.hasNextLine()) {
                            String line = fileIn.nextLine() + "\n";
                            jTextArea.append(line);
                        }

                        JScrollPane jScrollPane = new JScrollPane(jTextArea);
                        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                        jTextArea.setLineWrap(true);
                        jTextArea.setWrapStyleWord(true);

                        editor.addTab(selectedFile.getName(), jScrollPane);
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });


        closeFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int currentTab = editor.getSelectedIndex();
                if (currentTab >= 0) {
                    editor.removeTabAt(currentTab);
                } else {
                    return;
                }

            }
        });


        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int response = fileChooser.showSaveDialog(null);

                Component selectedComponent = editor.getSelectedComponent();

                if (selectedComponent instanceof JScrollPane) {
                    JScrollPane selectedScrollPane = (JScrollPane) selectedComponent;
                    JTextArea jTextArea = (JTextArea) selectedScrollPane.getViewport().getView();

                    if (response == JFileChooser.APPROVE_OPTION) {
                        File file;
                        PrintWriter fileOut = null;

                        file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                        try {
                            fileOut = new PrintWriter(file);
                            fileOut.println(jTextArea.getText());
                        } catch (FileNotFoundException ex) {
                            ex.printStackTrace();
                        } finally {
                            if (fileOut != null) {
                                fileOut.close();
                            }
                        }
                    }
                }
            }
        });
        textSizeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();

                // Check if the value has changed and the user has stopped sliding
                if (!source.getValueIsAdjusting()) {
                    Component selectedComponent = editor.getSelectedComponent();

                    // Check if the selected component is a JScrollPane
                    if (selectedComponent instanceof JScrollPane) {
                        JScrollPane selectedScrollPane = (JScrollPane) selectedComponent;
                        JTextArea jTextArea = (JTextArea) selectedScrollPane.getViewport().getView();

                        // Update the font size based on the slider value
                        int fontSize = (int) source.getValue();
                        Font currentFont = jTextArea.getFont();
                        Font newFont = new Font(currentFont.getFontName(), currentFont.getStyle(), fontSize);
                        jTextArea.setFont(newFont);
                    }
                }
            }
        });

    }
    public static void main(String[]args) throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new FlatMacDarkLaf());
        new TextEdtitor();
    }

}
