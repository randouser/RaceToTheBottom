package org.group3.game.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping(value = "/game")
public class GameController {
	private static final Logger logger = LoggerFactory.getLogger(GameController.class);
	
	/**
	 * Rest response
	 */
	@RequestMapping(method = RequestMethod.POST, produces="text/plain")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody String gameHello() {
		logger.info("GameController Triggered");
		return "You reached the game controller";
	}
}
