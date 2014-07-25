package org.group3.game.services;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.group3.game.controllers.GameController;
import org.group3.game.model.user.User;
import org.group3.game.model.user.UserService;
import org.group3.game.model.game.Game;
import org.group3.game.model.game.GameService;
import org.group3.game.model.game.GameDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ArrayList;

import org.joda.time.DateTime;

public class MaintenanceComponent {
	
	@Autowired
	UserService userService;
	
	@Autowired
	GameService gameService;
	
	@Autowired
	GameDao gameDao;
	
	private static final Logger logger = LoggerFactory.getLogger(MaintenanceComponent.class);
	
	public void maintainDb()
	{
		
		//Users
		logger.info("clean old users routine started");
		
		deleteUsers(userService.getInactivePlayers());
		
		//Games
		logger.info("clean old games routine started");
		
		deleteGames(gameService.getInactiveGameIds());
		
	}
	
	public void deleteGames(List<Integer> oldGameIds)
	{
		
		for (Integer gameId: oldGameIds)
		{
			
			logger.info("Game ID flagged " + gameId.intValue());
			
			gameService.deleteGameById(gameId.intValue());
			
			logger.info("Game ID send to delete" + gameId.intValue());
			
		}
		
	}
	
	public void deleteUsers(List<User> oldUsersToDelete)
	{
		
		for (User curUser: oldUsersToDelete)
		{
			
			//get all games the deleted user is in, delete them first
			List<Game> gamesToDelete = gameDao.getSavedGamesForUser(curUser);
			
			List<Integer> gamesToDeleteIds = new ArrayList<>();
			
			for (Game userOldGames : gamesToDelete)
			{
				
				gamesToDeleteIds.add(userOldGames.getGameId());
				
			}
			
			//Delete games old user is in
			deleteGames(gamesToDeleteIds);
			
			//Then get rid of user
			userService.deleteUserByEmail(curUser.getEmail());
			
			logger.info("user send to delete" + curUser.getEmail());
			
		}
		
	}
	
}