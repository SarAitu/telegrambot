package com.whiskels.telegram.bot.handler;

import com.whiskels.telegram.bot.State;
import com.whiskels.telegram.model.User;
import com.whiskels.telegram.repository.JpaUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import static com.whiskels.telegram.util.TelegramUtil.createMessageTemplate;

@Component
public class StartHandler implements Handler {
    @Value("${bot.name}")
    private String botUsername;

    private final JpaUserRepository userRepository;

    public StartHandler(JpaUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        // Welcoming user
        SendMessage welcomeMessage = createMessageTemplate(user)
                .setText(String.format(
                        "Приветик!! Я *%s*%nВыбери опцию:", botUsername
                ));
        // Asking to input name
        SendMessage registrationMessage = createMessageTemplate(user)
                .setText("In order to start our journey tell me your name");
        // Changing user state to "awaiting for name input"
        user.setBotState(State.ENTER_NAME);
        userRepository.save(user);

        return List.of(welcomeMessage, registrationMessage);
    }

    @Override
    public State operatedBotState() {
        return State.START;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return Collections.emptyList();
    }
}
