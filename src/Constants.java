import com.besaba.revonline.pastebinapi.Pastebin;
import com.besaba.revonline.pastebinapi.impl.factory.PastebinFactory;

/**
 * Created by Thibault Helsmoortel on 7/10/2016.
 */
public class Constants {

    //Library used: https://github.com/marcoacierno/pastebin-java-api
    private static final String DEV_KEY = "310ca155a042883390d45ae199b3a70f";
    public static final PastebinFactory FACTORY = new PastebinFactory();
    public static final Pastebin PASTEBIN = FACTORY.createPastebin(DEV_KEY);
}
