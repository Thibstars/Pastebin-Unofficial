import com.besaba.revonline.pastebinapi.paste.Paste;
import com.besaba.revonline.pastebinapi.paste.PasteBuilder;
import com.besaba.revonline.pastebinapi.paste.PasteExpire;
import com.besaba.revonline.pastebinapi.paste.PasteVisiblity;
import com.besaba.revonline.pastebinapi.response.Response;
import com.intellij.codeInsight.hint.HintManager;
import com.intellij.codeInsight.hint.HintManagerImpl;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.logging.Level;
import java.util.logging.Logger;

public class NewPasteAction extends AnAction {

    private static final Logger LOGGER = Logger.getLogger(NewPasteAction.class.getSimpleName());

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);

        //Get the filename of the currently opened file
        Document currentDoc = FileEditorManager.getInstance(project).getSelectedTextEditor().getDocument();
        VirtualFile currentFile = FileDocumentManager.getInstance().getFile(currentDoc);
        String fileName = currentFile.getPath().substring(currentFile.getPath().lastIndexOf('/') + 1);

        //Get paste data
        String title = Messages.showInputDialog(project, "Paste Title", "Input Your Paste Title", null, fileName, null);
        //Get the text in the currently opened editor
        String raw = "";
        if (e.getData(LangDataKeys.EDITOR).getDocument() != null) raw = e.getData(LangDataKeys.EDITOR).getDocument().getText();


        /*final Response<String> userLoginKeyResponse = Constants.PASTEBIN.login("" *//*user name*//*, "" *//*password*//*);

        if (userLoginKeyResponse.hasError()) {
            LOGGER.log(Level.INFO, "An error occurred while logging into Pastebin: " + userLoginKeyResponse.getError());
        }*/

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
        if (postResult.hasError()) {
            Notifications.Bus.notify(new Notification("Pastebin", "Error Posting Paste", "An error occurred while posting the paste: " + postResult.getError(), NotificationType.ERROR));
        } else
            Notifications.Bus.notify(new Notification("Pastebin", "Successful Paste", "Paste successfully posted! URL: " + postResult.get(), NotificationType.INFORMATION));
    }
}
