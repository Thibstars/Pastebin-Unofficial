package be.thibaulthelsmoortel.pastebin.util;

import com.intellij.ide.BrowserUtil;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationListener;
import org.jetbrains.annotations.NotNull;

import javax.swing.event.HyperlinkEvent;

/**
 * Class representing a notification listener that opens hyperlinks in the browser when clicked.
 *
 * @author Thibault Helsmoortel
 */
public class HyperlinkNotificationListener implements NotificationListener {

    @Override
    public void hyperlinkUpdate(@NotNull Notification notification, @NotNull HyperlinkEvent hyperlinkEvent) {
        if (hyperlinkEvent.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            BrowserUtil.browse(hyperlinkEvent.getURL());
        }
    }
}
