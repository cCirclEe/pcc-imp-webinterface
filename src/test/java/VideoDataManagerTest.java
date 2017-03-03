import edu.kit.informatik.pcc.webinterface.datamanagement.VideoDataManager;
import edu.kit.informatik.pcc.webinterface.serverconnection.ServerProxy;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Created by chris on 03.03.2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ServerProxy.class})
public class VideoDataManagerTest {
    private SessionStub sessionStub;

    @Before
    public void setUp() {
        sessionStub = new SessionStub(null);
    }

    @Ignore
    @Test
    public void getVideosTest() {
        VideoDataManager.getVideos(sessionStub);
    }
}
