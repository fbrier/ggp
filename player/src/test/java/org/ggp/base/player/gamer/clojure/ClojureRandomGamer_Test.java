package org.ggp.base.player.gamer.clojure;

import java.io.IOException;
import org.ggp.base.player.gamer.exception.InitializationException;
import org.ggp.base.player.gamer.exception.MetaGamingException;
import org.ggp.base.player.gamer.exception.MoveSelectionException;
import org.ggp.base.util.game.GameRepository;
import org.ggp.base.util.gdl.grammar.GdlPool;
import org.ggp.base.util.match.Match;

import junit.framework.TestCase;

/**
 * Unit tests for the ClojureGamer class, to verify that we can actually
 * instantiate a Clojure-based gamer and have it play moves in a game.
 *
 * @author Sam
 */
public class ClojureRandomGamer_Test extends TestCase
{
    public void testClojureGamer() throws MetaGamingException, MoveSelectionException, IOException, InitializationException
    {
        ClojureGamer g = new ClojureGamer();
        g.setClojureGamerFile( "random_gamer" );
        g.setName( "ClojureRandomGamer" );
        g.afterPropertiesSet();

        assertEquals( "ClojureRandomGamer", g.getName() );

        Match m = new Match( "", -1, 1000, 1000, GameRepository.getDefaultRepository().getGame( "ticTacToe" ) );
        g.setMatch( m );
        g.setRoleName( GdlPool.getConstant( "xplayer" ) );
        g.metaGame( 1000 );
        assertTrue( g.selectMove( 1000 ) != null );
    }
}