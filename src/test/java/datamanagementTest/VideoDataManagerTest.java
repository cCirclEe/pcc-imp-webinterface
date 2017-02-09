package datamanagementTest;

import edu.kit.informatik.pcc.webinterface.datamanagement.Account;
import edu.kit.informatik.pcc.webinterface.datamanagement.AccountDataManager;
import edu.kit.informatik.pcc.webinterface.datamanagement.Video;
import edu.kit.informatik.pcc.webinterface.datamanagement.VideoDataManager;
import edu.kit.informatik.pcc.webinterface.serverconnection.ServerProxy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.when;


/**
 * Created by chris on 27.01.2017.
 */
@RunWith(PowerMockRunner.class)
public class VideoDataManagerTest {

    @Before
    public void setUp() {

    }

    @Test
    @PrepareForTest({VideoDataManager.class, ServerProxy.class, AccountDataManager.class})
    public void updateVideosAndInfoTest() {
        PowerMockito.mockStatic(ServerProxy.class);

        Account a = new Account("mail","password");
        AccountDataManager.setAccount(a);
        String testVideos = "[{\"videoInfo\":{\"name\":\"input\",\"id\":\"165\"}},{\"videoInfo\":{\"name\":\"input2\",\"id\":\"166\"}}]";
        when(ServerProxy.getVideos(a)).thenReturn(testVideos);
        when(ServerProxy.videoInfo(165,a)).thenReturn("info1");
        when(ServerProxy.videoInfo(166,a)).thenReturn("info2");

        VideoDataManager.updateVideosAndInfo();
        LinkedList<Video> videos = new LinkedList<Video>();
        videos = VideoDataManager.getVideos();
        assertEquals(videos.getFirst().getName(), "input");
        assertEquals(videos.getFirst().getId(), Integer.parseInt("165"));
        assertEquals(videos.getFirst().getInfo(), "info1");

        assertEquals(videos.getLast().getName(), "input2");
        assertEquals(videos.getLast().getId(), Integer.parseInt("166"));
        assertEquals(videos.getLast().getInfo(), "info2");
    }
}