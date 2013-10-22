package org.ggp.base.player.gamer.exception;

@SuppressWarnings("serial")
public final class InitializationException extends Exception
{
	public InitializationException( Throwable cause ) {
		super(cause);
	}

	@Override
	public String toString()
	{
		return "An unhandled exception occurred during aborting: " + super.toString();
	}

}
