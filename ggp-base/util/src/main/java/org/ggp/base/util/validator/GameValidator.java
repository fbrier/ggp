package org.ggp.base.util.validator;

import org.ggp.base.util.game.Game;

public interface GameValidator {
	public void checkValidity(Game theGame) throws ValidatorException;
}