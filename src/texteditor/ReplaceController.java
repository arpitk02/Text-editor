package texteditor;

import form.MainForm;
import form.ReplaceForm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;


public class ReplaceController {


    public void replace(MainForm mainForm, ReplaceForm replaceForm) {
        replaceForm.getBtnReplace().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String textAreaCurrent = mainForm.getTxtArea().getText();
                String textFind = replaceForm.getTxtFind().getText();
                String textReplace = replaceForm.getTxtReplace().getText();
                mainForm.getTxtArea().setText(textAreaCurrent.replaceFirst(textFind, textReplace));
                int indexCurrent = mainForm.getTxtArea().getText().lastIndexOf(textReplace) + textReplace.length();

                int indexTextSearch = -1;
                indexTextSearch = mainForm.getTxtArea().getText().indexOf(textFind, indexCurrent);
               
                if (indexTextSearch != -1) {
                    mainForm.getTxtArea().setSelectionStart(indexTextSearch);
                    mainForm.getTxtArea().setSelectionEnd(indexTextSearch + textFind.length());
                } else {
                    JOptionPane.showMessageDialog(replaceForm, "Cannot find \"" + textFind + "\"", "Result", 2);
                }
            }
        });
    }


    public void replaceAll(MainForm mainForm, ReplaceForm replaceForm) {
        replaceForm.getBtnReplaceAll().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               
                if (mainForm.getTxtArea().getText().isEmpty()) {
                    JOptionPane.showMessageDialog(replaceForm, "No text field found!!!", "Error", 2);
                } else {
                    String textAreaCurrent = mainForm.getTxtArea().getText();
                    String textFind = replaceForm.getTxtFind().getText();
                    String textReplace = replaceForm.getTxtReplace().getText();
                    mainForm.getTxtArea().setText(textAreaCurrent.replaceAll(textFind, textReplace));
                }
            }
        });
    }

    public void checkEmptyReplace(ReplaceForm replaceForm) {
        replaceForm.getTxtFind().addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                if (replaceForm.getTxtFind().getText().trim().isEmpty()) {
                    replaceForm.getBtnReplace().setEnabled(false);
                    replaceForm.getBtnReplaceAll().setEnabled(false);
                } else {
                    replaceForm.getBtnReplace().setEnabled(true);
                    replaceForm.getBtnReplaceAll().setEnabled(true);
                }
            }
        });
    }

  
    public void cancelReplace(ReplaceForm replaceForm) {
        replaceForm.getBtnCancel().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                replaceForm.setVisible(false);
            }
        });
    }
}
