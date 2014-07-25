package org.group3.game.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.group3.game.model.user.User;
import org.group3.game.model.user.UserService;
import org.group3.game.model.game.Game;
import org.group3.game.model.game.GameService;
import org.group3.game.services.MaintenanceComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ArrayList;

import org.joda.time.DateTime;

//Skipped adding stereotype annotations, as Spring container will create an instance
//and the scheduling annotations create an instance, so it runs twice. Manually
//defined beans in root-context.xml instead

@EnableScheduling
public class MaintenanceService {
	
	private static final Logger logger = LoggerFactory.getLogger(MaintenanceService.class);
	
	@Autowired
	private MaintenanceComponent maintWorker;
	
	//runs every 7 days
	@Scheduled(cron="* * * */7 * *")
	public void runMaintenance()
	{
		
		logger.info("Entered scheduled maintenance service");
		
		maintWorker.maintainDb();
		
		logger.info("Ended scheduled maintenance service");
		
	}
	
}