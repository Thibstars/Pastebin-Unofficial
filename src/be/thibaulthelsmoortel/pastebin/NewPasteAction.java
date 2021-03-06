package be.thibaulthelsmoortel.pastebin;

import com.besaba.revonline.pastebinapi.paste.Paste;
import com.besaba.revonline.pastebinapi.paste.PasteBuilder;
import com.besaba.revonline.pastebinapi.paste.PasteExpire;
import com.besaba.revonline.pastebinapi.paste.PasteVisiblity;
import com.besaba.revonline.pastebinapi.response.Response;
import com.intellij.ide.BrowserUtil;
import com.intellij.notification.*;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;

import javax.swing.event.HyperlinkEvent;
import java.util.logging.Logger;

/**
 * Class representing the action responsible for paste creation.
 *
 * @author Thibault Helsmoortel
 */
public class NewPasteAction extends AnAction {

    private static final Logger LOGGER = Logger.getLogger(NewPasteAction.class.getSimpleName());
    private static Project project;

    @Override
    public void actionPerformed(AnActionEvent e) {
        project = e.getData(PlatformDataKeys.PROJECT);

        //Get the filename of the currently opened file
        Document currentDoc = FileEditorManager.getInstance(project).getSelectedTextEditor().getDocument();
        VirtualFile currentFile = FileDocumentManager.getInstance().getFile(currentDoc);
        //String fileName = currentFile.getPath().substring(currentFile.getPath().lastIndexOf('/') + 1);
        String fileName = currentFile.getName();

        //Get paste data
        String title =  Messages.showInputDialog(project, "Paste Title", "Input Your Paste Title", null, fileName, null);
        //Get the text in the currently opened editor
        String raw = "";
        if (currentDoc != null) raw = currentDoc.getText();


        /*final Response<String> userLoginKeyResponse = be.thibaulthelsmoortel.pastebin.Constants.PASTEBIN.login("" *//*user name*//*, "" *//*password*//*);

        if (userLoginKeyResponse.hasError()) {
            LOGGER.log(Level.INFO, "An error occurred while logging into Pastebin: " + userLoginKeyResponse.getError());
        }*/

        createPaste(title, raw);
    }

    /**
     * Creates the paste based on specified title and content.
     * After execution a balloon notification will be shown.
     *
     * @param title the paste title
     * @param raw the paste content
     */
    private void createPaste(String title, String raw) {
        //Create paste
        final PasteBuilder pasteBuilder = Constants.FACTORY.createPaste();
        //Title
        pasteBuilder.setTitle(title);
        //Raw data
        pasteBuilder.setRaw(raw);
        //Syntax
        pasteBuilder.setMachineFriendlyLanguage("text");
        //Visibility
        pasteBuilder.setVisiblity(PasteVisiblity.Public);
        //Expiration
        pasteBuilder.setExpire(PasteExpire.TenMinutes);

        final Paste paste = pasteBuilder.build();

        final Response<String> postResult = Constants.PASTEBIN.post(paste);
        NotificationGroup balloonNotifications = new NotificationGroup("Balloon notifications", NotificationDisplayType.BALLOON, true);
        if (postResult.hasError()) {
            Notification fail =
                    balloonNotifications.createNotification(
                            "<html>Error Posting Paste", "An error occurred while posting the <a href=\"" + postResult.get() + "\" target=\"blank\">paste</a> ",
                            NotificationType.ERROR, new NotificationListener.UrlOpeningListener(true));
            Notifications.Bus.notify(fail, project);
        } else {
            Notification success =
                    balloonNotifications.createNotification(
                            "<html>Successful Paste", "<a href=\"" + postResult.get() + "\" target=\"blank\">Paste</a> successfully posted!</html>",
                            NotificationType.INFORMATION, new NotificationListener.UrlOpeningListener(true));
            Notifications.Bus.notify(success, project);
        }
    }
}
