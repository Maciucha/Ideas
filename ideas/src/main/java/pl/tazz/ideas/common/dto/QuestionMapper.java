package pl.tazz.ideas.common.dto;

import org.springframework.stereotype.Component;
import pl.tazz.ideas.question.domain.model.Question;

@Component
public class QuestionMapper {

    public void copyProperties(Question source, Question target) {
        target.setName(source.getName());
        target.setUsername(source.getUsername());
        target.setEmail(source.getEmail());
        target.setTitle(source.getTitle());
        target.setContent(source.getContent());
        target.setCategory(source.getCategory());
    }
}
