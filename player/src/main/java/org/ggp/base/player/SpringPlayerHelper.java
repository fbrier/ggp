package org.ggp.base.player;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Logger;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.ggp.base.player.gamer.Gamer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Helper for Spring Framework configurable Player Runner.  Based on Sam's PlayerRunner.
 * 
 * @author Frederick N. Brier
 */
public class SpringPlayerHelper
{
    final static Logger logger = Logger.getLogger(SpringPlayerHelper.class.toString());
    private String[] args;
    private ApplicationContext applicationContext;
    private GamePlayer gamePlayer = null;
    private int port = 9147;
    private Gamer gamer = null;
    private String gameName = null;

    public SpringPlayerHelper(String[] args)
    {

        logger.info("Initializing Spring context.");
        applicationContext = new ClassPathXmlApplicationContext("/application-context.xml");
        logger.info("Spring context initialized.");
        this.args = args;
	}

    /**
     * Parse the command line and determine that there is a selected game.
     * @return true if the command line was properly processed and a gamer was found to use, false otherwise.
     * @throws ParseException
     */
    public boolean init() throws ParseException
    {
        if ( ! processCommandLine( args ) )
            return false;

        Map<String, Gamer> gamerMap = (Map<String, Gamer>)applicationContext.getBean( "gamerMap" );

        if ( ( null == gameName ) && ( 1 == gamerMap.size() ) )
        {
            Map.Entry<String,Gamer> onlyGame = gamerMap.entrySet().iterator().next();
            gamer = onlyGame.getValue();
        }
        else
        {
            gamer = (Gamer)applicationContext.getBean( gameName );
        }

        if ( null == gamer )
        {
            // Gamer not found - print a list of available gamers.
            System.out.println( "Could not find player class with that name. Available choices are: " +
                    Arrays.toString( gamerMap.keySet().toArray() ) );
            return false;
        }

        return true;
    }

    /**
     * Start up the player runner daemon running the specified game on the specified port.
     */
    public void start() throws IOException
    {
        if ( null != gamer )
        {
            System.out.println( "Starting up pre-configured player on port " + port + " using player class named " + gamer.getName() );
            gamePlayer = new GamePlayer( port, gamer );
            gamePlayer.start();
        }
    }

    public void join()
    {
        if ( null != gamePlayer )
            try
            {
                gamePlayer.join();
            }
            catch ( InterruptedException e )
            {
                logger.severe( e.toString() );
            }
    }

    public void shutdown() throws IOException
    {
        if ( null != gamePlayer )
            gamePlayer.shutdown();
    }

    private boolean processCommandLine( String[] args ) throws ParseException
    {
        // Create Options object
        Options options = new Options();
        options.addOption("p", "port", true, "Port number exposed for game server, defaults to " + port );
        options.addOption("n", "name", true, "Name of the game to be run, defaults if only one game defined");
        options.addOption("help", false, "Displays this help.");

        CommandLineParser parser = new BasicParser();
        CommandLine cmd = parser.parse( options, args );
        if( cmd.hasOption( "help" ) )
        {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp( "run-gamer", options );
            return false;
        }
        port = Integer.valueOf( cmd.getOptionValue( "p", String.valueOf( port ) ) );
        gameName = cmd.getOptionValue( "n" );
        return true;
    }

    public void waitForQuitKey() throws IOException
    {
        System.out.print( "Press <enter> to quit...");
        System.in.read();
        shutdown();
        join();
        System.out.println( "Exiting..." );
    }
}