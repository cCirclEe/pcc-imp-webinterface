import edu.kit.informatik.pcc.webinterface.mailservice.MailService;
import org.apache.commons.mail.EmailException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Created by chris on 03.03.2017.
 */
public class MailServiceTest {
    public final String VALIDMAIL = "mail@mail.mail";
    public final String NOTVALIDMAIL = "mail";

    @Test
    public void isValidMailTest() {
        assertEquals(MailService.isValidEmailAddress(VALIDMAIL), true);
        assertEquals(MailService.isValidEmailAddress(NOTVALIDMAIL), false);
    }

    @Test
    public void isValidMailFailTest() {
        assertEquals(MailService.isValidEmailAddress(NOTVALIDMAIL), false);
    }

    @Test(expected = EmailException.class)
    public void sendMailFailTest() throws IOException, EmailException {
        MailService.send(VALIDMAIL, NOTVALIDMAIL, "hallo", "abc");
    }
}
