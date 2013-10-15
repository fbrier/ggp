package org.ggp.base.apps.consoles;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import org.python.core.PyString;
import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;

/**
 * PythonConsole is a Jython-based app that lets you interact with a Python
 * console that has full access to all of the Java classes in the project.
 * This allows you to quickly experiment with the classes, without having to
 * write a full-blown Java program. There are also helpful scripts so that
 * you can get started quickly.
 * 
 * For example, try out:
 * 
 * >> display_random_walk(load_game('ticTacToe'))
 * 
 * This will load the game Tic-Tac-Toe and play through it randomly.
 *
 * This uses the JythonConsole implementation of a console with all of the
 * nice features like history, tab completion, et cetera. JythonConsole is an
 * external open source project, accessible at:
 * 
 *    http://code.google.com/p/jythonconsole/
 *    
 * The license for JythonConsole is available in the licenses/ directory.
 * 
 * @author Sam
 */
public class PythonConsole {
    public static void main(String[] args) {
        PythonInterpreter interpreter = new PythonInterpreter( null, new PySystemState() );
        PySystemState sysState = interpreter.getSystemState();
        URL url = ClassLoader.getSystemClassLoader().getResource( "scripts.py" );
        if ( null == url )
            throw new RuntimeException( "Could not find scripts.py" );

        File file = null;
        try
        {
            file = new File( url.toURI() );
            sysState.path.append( new PyString( file.getParent() ) );
        }
        catch ( URISyntaxException e )
        {
            throw new RuntimeException( e );
        }

        interpreter.exec("from scripts import *");
        interpreter.exec("import console");
        interpreter.exec("console.main(locals())");
    }
}