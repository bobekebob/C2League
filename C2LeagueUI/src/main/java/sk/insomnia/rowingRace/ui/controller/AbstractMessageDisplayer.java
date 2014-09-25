package sk.insomnia.rowingRace.ui.controller;

import javafx.scene.control.Dialogs;
import javafx.stage.Stage;

/**
 * Created by bobek on 6/28/2014.
 */
public abstract class AbstractMessageDisplayer {

    public void displayMessage(String errorMessage, String windowTitle, String windowLabel) {
        Dialogs.showErrorDialog(new Stage(), errorMessage,
                windowTitle,
                windowLabel);
    }

}
