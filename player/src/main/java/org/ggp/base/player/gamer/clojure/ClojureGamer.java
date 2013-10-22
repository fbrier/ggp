package org.ggp.base.player.gamer.clojure;

import clojure.lang.RT;
import clojure.lang.Var;
import java.io.IOException;
import org.ggp.base.player.gamer.Gamer;
import org.ggp.base.player.gamer.exception.AbortingException;
import org.ggp.base.player.gamer.exception.GamePreviewException;
import org.ggp.base.player.gamer.exception.InitializationException;
import org.ggp.base.player.gamer.exception.MetaGamingException;
import org.ggp.base.player.gamer.exception.MoveSelectionException;
import org.ggp.base.player.gamer.exception.StoppingException;
import org.ggp.base.util.game.Game;
import org.ggp.base.util.gdl.grammar.GdlTerm;
import org.ggp.base.util.logging.GamerLogger;
import org.springframework.beans.factory.InitializingBean;

/**
 * ClojureGamer is a superclass that allows you to hook Clojure gamers into the
 * rest of the Java framework. In order to do this, do the following:
 *
 * 1) Create a subclass of ClojureGamer that overrides getClojureGamerFile() and
 *    getName() to indicate where the Clojure source code file is.
 *    This is the Java stub that refers to the real Clojure gamer class.
 *    
 * 2) Create the Clojure source code file, in the /src_clj/ directory in the root
 *    directory for this project. Make sure that the stub points to this class,
 *    and that the Clojure class is a valid subclass of Gamer.
 *
 * For examples where this has already been done, see @ClojureLegalGamerStub,
 * which is implemented in Clojure and hook into the Java framework using the
 * ClojureGamer stub.
 * 
 * @author Sam Schreiber
 */
public class ClojureGamer extends Gamer implements InitializingBean
{
    private Gamer theClojureGamer;

    private String clojureGamerFile;

    private String name;

    public String getClojureGamerFile()
    {
        return clojureGamerFile;
    }

    public void setClojureGamerFile( String clojureGamerFile )
    {
        this.clojureGamerFile = clojureGamerFile;
    }

    @Override
    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    /**
     * Spring framework initialization.
     */
    public void afterPropertiesSet() throws IOException, InitializationException
    {
    	if (theClojureGamer == null) {
	        try {
	            // Load the Clojure script -- as a side effect this initializes the runtime.
	            RT.loadResourceScript(getClojureGamerFile() + ".clj");
	
	            // Get a reference to the gamer-generating function.
	            Var gamerVar = RT.var("gamer_namespace", getName());
	     
	            // Call it!
	            theClojureGamer = (Gamer)gamerVar.invoke();
	        } catch(Exception e) {
	            GamerLogger.logError("GamePlayer", "Caught exception in Clojure initialization:");
	            throw new InitializationException( e );
	        }
    	}
    }

    // The following methods are overriden as 'final' because they should not
    // be changed in subclasses of this class. Subclasses of this class should
    // only implement getClojureGamerFile() and getName(), and then
    // implement the real methods in the actual Clojure gamer. Essentially, any
    // subclass of this class is a Java-implementation stub for the actual real
    // Clojure implementation.
    
    @Override
    public final void preview(Game game, long timeout) throws GamePreviewException {
        theClojureGamer.preview(game, timeout);
    }
    
    @Override
    public final void metaGame(long timeout) throws MetaGamingException
    {
        theClojureGamer.setMatch(getMatch());
        theClojureGamer.setRoleName(getRoleName());
        theClojureGamer.metaGame(timeout);
    }
    
    @Override
    public final GdlTerm selectMove(long timeout) throws MoveSelectionException {
        theClojureGamer.setMatch(getMatch());
        theClojureGamer.setRoleName(getRoleName());
        return theClojureGamer.selectMove(timeout);
    }
    
    @Override
    public final void stop() throws StoppingException
    {
        theClojureGamer.setMatch(getMatch());
        theClojureGamer.setRoleName(getRoleName());
        theClojureGamer.stop();
    }
    
    @Override
    public final void abort() throws AbortingException
    {
        theClojureGamer.setMatch(getMatch());
        theClojureGamer.setRoleName(getRoleName());
        theClojureGamer.abort();
    }
}