package com.harlyn.listener;

import com.harlyn.config.WebSocketConfig;
import com.harlyn.domain.chat.ChatMessage;
import com.harlyn.domain.chat.CompetitionLeaderboardChatMessage;
import com.harlyn.domain.problems.Problem;
import com.harlyn.event.ProblemSolvedEvent;
import com.harlyn.service.CompetitionService;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.generic.DateTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wannabe on 22.03.16.
 */
@Component
public class ProblemSolvedListener implements ApplicationListener<ProblemSolvedEvent> {

	@Autowired
	private SimpMessagingTemplate template;
	@Resource
	private Map<Class<? extends ChatMessage>, WebSocketConfig.MessageEndpointResolver> chatEndpointResolvers;
	@Autowired
	private VelocityEngine velocityEngine;
	@Autowired
	private CompetitionService competitionService;
	@Autowired
	private String currentHost;

	@Override
	public void onApplicationEvent(ProblemSolvedEvent event) {
		Map<String, Object> model = new HashMap<>();
		model.put("competition", competitionService.findById(event
			.getCompetitionLeaderboardChatMessage()
			.getCompetition().getId()));
		model.put("currentHost", currentHost);
		model.put("dateFormat", Problem.DATE_FORMAT);
		model.put("date", new DateTool());
		String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "templates/competition/leaderboard.vm", "UTF-8", model)
			.replace("\n", "").replace("\t", "");
		template.convertAndSend(chatEndpointResolvers.get(CompetitionLeaderboardChatMessage.class)
				.resolveEndpoint(event.getCompetitionLeaderboardChatMessage()),
			text);
	}
}
