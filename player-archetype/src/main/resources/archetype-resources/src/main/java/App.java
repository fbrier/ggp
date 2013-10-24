package ${groupId};

import java.io.IOException;
import org.apache.commons.cli.ParseException;
import org.ggp.base.player.SpringPlayerHelper;

/**
 * GGP main class launcher
 */
public class App
{
    public static void main( String[] args ) throws ParseException, IOException
    {
        SpringPlayerHelper player = new SpringPlayerHelper( args );
        if ( player.init() )
            player.start();

        player.waitForQuitKey();
    }
}
