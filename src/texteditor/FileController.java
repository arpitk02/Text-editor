package texteditor;

import form.MainForm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.filechooser.FileFilter;


public class FileController {

   
    public void controller(MainForm mainForm) {
        JFileChooser chooser = new JFileChooser();

        setupFileChoose(chooser);
        newFile(mainForm, chooser);
        openFile(mainForm, chooser);
        saveFile(mainForm, chooser);
        saveAsFile(mainForm, chooser);
        exitNote(mainForm, chooser);
        checkClose(mainForm, chooser);

        checkSaved(mainForm);
    }

   
    private void setupFileChoose(JFileChooser chooser) {
       
        chooser.addChoosableFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                } else {
                    return f.getName().endsWith(".java");
                }
            }

            @Override
            public String getDescription() {
                return "Java Source File(*.java)";
            }
        });

        chooser.addChoosableFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                } else {
                    return f.getName().endsWith(".txt");
                }
            }

            @Override
            public String getDescription() {
                return "Text Files(*.txt)";
            }
        });

  
        chooser.setCurrentDirectory(new File("."));

    }

 
    private void saveFile(MainForm mainForm, JFileChooser chooser) {
        mainForm.getFileSave().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                writeTextAreaToFile(mainForm, chooser);
                mainForm.setSaved(true);
            }
        });
    }

   
    private void saveAsFile(MainForm mainForm, JFileChooser chooser) {
        mainForm.getFileSaveAs().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               
                if (checkSave(mainForm, chooser)) {
                    return;
                }
                saveAsToFile(mainForm, chooser);
            }
        });
    }

  
    private void openFile(MainForm mainForm, JFileChooser chooser) {
        mainForm.getFileOpen().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                if (checkSave(mainForm, chooser)) {
                    return;
                }
                
                File fileCheck = null;
                while (true) {
                    chooser.showOpenDialog(mainForm);
                    fileCheck = chooser.getSelectedFile();
                    if (fileCheck.exists()) {
                        break;
                    }
                    JOptionPane.showMessageDialog(mainForm, "File not found", "Open", JOptionPane.INFORMATION_MESSAGE);
                }
                mainForm.setFile(fileCheck);
                
                mainForm.getTxtArea().setText("");
                
                writeFileToTextArea(mainForm);
                mainForm.setSaved(true);
            }
        });
    }

  
    private void newFile(MainForm mainForm, JFileChooser chooser) {
        mainForm.getFileNew().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                if (checkSave(mainForm, chooser)) {
                    return;
                }
                mainForm.getTxtArea().setText("");
            }
        });
    }

    private void exitNote(MainForm mainForm, JFileChooser chooser) {
        mainForm.getFileExit().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                if (checkSave(mainForm, chooser)) {
                    return;
                }
                System.exit(0);
            }
        });
    }

    
    public void writeFileToTextArea(MainForm mainForm) {
        try {
           
            FileInputStream fin = new FileInputStream(mainForm.getFile());
            BufferedReader din = new BufferedReader(new InputStreamReader(fin));
            String str = "";
            while (str != null) {
                str = din.readLine();
                if (str == null) {
                    break;
                }
                mainForm.getTxtArea().append(str + "\n");
            }
            
            mainForm.setTextCheckSaved(mainForm.getTxtArea().getText());
            mainForm.setSaved(true);
            mainForm.getTxtArea().setCaretPosition(0);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

   
    private void writeTextAreaToFile(MainForm mainForm, JFileChooser chooser) {
        FileWriter fout = null;
        try {
            if (mainForm.getFile() == null) {
                saveAsToFile(mainForm, chooser);
                return;
            }
            fout = new FileWriter(mainForm.getFile());
            fout.write(mainForm.getTxtArea().getText());
            mainForm.setTextCheckSaved(mainForm.getTxtArea().getText());
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                fout.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    
    private void saveAsToFile(MainForm mainForm, JFileChooser chooser) {
        File checkFile = null;
        while (true) {
            chooser.showSaveDialog(mainForm);
            checkFile = chooser.getSelectedFile();
            if (!checkFile.exists()) {
                break;
            }
            int option = JOptionPane.showConfirmDialog(mainForm, "Do you want to replace it?", "Save As", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                break;
            }
        }
        mainForm.setFile(checkFile);
        mainForm.setTextCheckSaved("");
        writeTextAreaToFile(mainForm, chooser);
    }

    
    private void checkSaved(MainForm mainForm) {
        mainForm.getTxtArea().addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                if (!mainForm.getTxtArea().getText().equals(mainForm.getTextCheckSaved())) {
                    mainForm.setSaved(false);
                }
            }
        });
    }

  
    private boolean checkSave(MainForm mainForm, JFileChooser chooser) {
        String message = "<html><div style = 'color:blue'>Do you want to save the changes to file </div><html>";
        
        boolean checkSaved = mainForm.isSaved();
        if (checkSaved == false) {
            int x = JOptionPane.showConfirmDialog(mainForm, message, "Confirm", JOptionPane.YES_NO_CANCEL_OPTION);
            if (x == JOptionPane.YES_OPTION) {
                if (mainForm.getFile() == null) {
                    saveAsToFile(mainForm, chooser);
                } else {
                    writeTextAreaToFile(mainForm, chooser);
                }
            }
           
            if (x == JOptionPane.CANCEL_OPTION || x == -1) {
                return true;
            }
        }
        return false;
    }

    
    private void checkClose(MainForm mainForm, JFileChooser chooser) {
        mainForm.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainForm.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                String message = "<html><div style = 'color:blue'>Do you want to save the changes to file </div><html>";
               
                boolean checkSaved = mainForm.isSaved();
                if (checkSaved == false) {
                    int x = JOptionPane.showConfirmDialog(mainForm, message, "Confirm", JOptionPane.YES_NO_CANCEL_OPTION);
                    if (x == JOptionPane.YES_OPTION) {
                        if (mainForm.getFile() == null) {
                            saveAsToFile(mainForm, chooser);
                        } else {
                            writeTextAreaToFile(mainForm, chooser);
                        }
                    }
             
                    if (x == JOptionPane.CANCEL_OPTION) {
                        return;
                    }
                 
                    if (x == JOptionPane.NO_OPTION) {
                        System.exit(0);
                    }
                } else {
                    System.exit(0);
                }
            }
        });
    }
}
