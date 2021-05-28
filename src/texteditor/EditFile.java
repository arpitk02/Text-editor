package texteditor;

import form.ChangeFontForm;
import form.FindForm;
import form.ReplaceForm;
import form.MainForm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Action;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.DefaultEditorKit;
import javax.swing.undo.UndoManager;

public class EditFile {


    public void controller(MainForm mainForm) {
        UndoManager manager = new UndoManager();
        mainForm.getTxtArea().getDocument().addUndoableEditListener(manager);

        canEdit(mainForm);
        canUndoRedo(mainForm, manager);

        undo(mainForm, manager);
        redo(mainForm, manager);
        copyPasteCut(mainForm);
        selectAll(mainForm);

        findController(mainForm);
        replaceController(mainForm);
        changeFont(mainForm);
    }


    private void canEdit(MainForm mainForm) {
     
        mainForm.getEditCopy().setEnabled(false);
        mainForm.getEditCut().setEnabled(false);
        mainForm.getFind().setEnabled(false);
        mainForm.getReplace().setEnabled(false);

  
        mainForm.getTxtArea().addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                String textCurrent = mainForm.getTxtArea().getText();
              
                if (textCurrent.length() != 0) {
                    mainForm.getFind().setEnabled(true);
                    mainForm.getReplace().setEnabled(true);
                }
             
                if (mainForm.getTxtArea().getSelectedText() != null) {
                    mainForm.getEditCut().setEnabled(true);
                    mainForm.getEditCopy().setEnabled(true);
                } else {
                    mainForm.getEditCut().setEnabled(false);
                    mainForm.getEditCopy().setEnabled(false);
                }

            }
        });
    }

    private void canUndoRedo(MainForm mainForm, UndoManager manager) {
       
        mainForm.getEditUndo().setEnabled(false);
        mainForm.getEditRedo().setEnabled(false);
        mainForm.getTxtArea().addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {

                if (manager.canUndo()) {
                    mainForm.getEditUndo().setEnabled(true);
                } else {
                    mainForm.getEditUndo().setEnabled(false);
                }
                if (manager.canRedo()) {
                    mainForm.getEditRedo().setEnabled(true);
                } else {
                    mainForm.getEditRedo().setEnabled(false);
                }
            }
        });
    }


    private void undo(MainForm mainForm, UndoManager manager) {
        mainForm.getEditUndo().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manager.undo();
            }
        });
    }


    private void redo(MainForm mainForm, UndoManager manager) {
        mainForm.getEditRedo().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manager.redo();
            }
        });
    }


    private void copyPasteCut(MainForm mainForm) {
        Action copy = new DefaultEditorKit.CopyAction();
        Action paste = new DefaultEditorKit.PasteAction();
        Action cut = new DefaultEditorKit.CutAction();

        mainForm.getEditCopy().addActionListener(copy);
        mainForm.getEditPaste().addActionListener(paste);
        mainForm.getEditCut().addActionListener(cut);

    }


    private void selectAll(MainForm mainForm) {
        mainForm.getEditDelete().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainForm.getTxtArea().selectAll();
            }
        });
    }

    private void findController(MainForm mainForm) {
        mainForm.getFind().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FindForm findForm = new FindForm(mainForm, true);
                findForm.setVisible(true);
                findForm.getBtnFind().setEnabled(false);

                FindController findController = new FindController();
                findController.checkEmptyFind(findForm);
                findController.find(mainForm, findForm);
                findController.cancelFind(findForm);
            }
        });
    }


    private void replaceController(MainForm mainForm) {
        mainForm.getReplace().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ReplaceForm replaceForm = new ReplaceForm(mainForm, false);
                replaceForm.setVisible(true);
                replaceForm.getBtnReplace().setEnabled(false);
                replaceForm.getBtnReplaceAll().setEnabled(false);

                ReplaceController replaceController = new ReplaceController();
                replaceController.checkEmptyReplace(replaceForm);
                replaceController.replace(mainForm, replaceForm);
                replaceController.replaceAll(mainForm, replaceForm);
                replaceController.cancelReplace(replaceForm);
            }
        });
    }

  
    private void changeFont(MainForm mainForm) {
        mainForm.getEditChangeFont().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ChangeFontForm changeFontForm = new ChangeFontForm();
                changeFontForm.setVisible(true);

                ChangeFont changeFontController = new ChangeFont();
                changeFontController.setupChangeFont(mainForm, changeFontForm);
                changeFontController.setupChangeFontForm(changeFontForm);
                changeFontController.changeFont(mainForm, changeFontForm);
                changeFontController.changeSize(mainForm, changeFontForm);
                changeFontController.changeStyle(mainForm, changeFontForm);
                changeFontController.clickButtonChangeFontForm(mainForm, changeFontForm);
            }
        });
    }
}
