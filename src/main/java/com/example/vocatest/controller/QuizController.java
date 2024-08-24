package com.example.vocatest.controller;

import com.example.vocatest.controllerDocs.QuizControllerDocs;
import com.example.vocatest.entity.VocaContentEntity;
import com.example.vocatest.service.UserService;
import com.example.vocatest.service.VocaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/quiz")
public class QuizController implements QuizControllerDocs {

    private final VocaService vocaService;
    private final UserService userService;

    public QuizController(VocaService vocaService, UserService userService){
        this.vocaService = vocaService;
        this.userService = userService;
    }

    @GetMapping("/{id}/{quizcount}")
    public List<VocaContentEntity> showRandomQuiz(@PathVariable("id") Long id, @PathVariable("quizcount") Long quizcount){

        List<VocaContentEntity> selectedVocaContents = vocaService.findAllVocasByVocaListId(id);
        Collections.shuffle(selectedVocaContents);

        if (quizcount > selectedVocaContents.size()) {
            quizcount = (long) selectedVocaContents.size();
        }

        return selectedVocaContents.subList(0, quizcount.intValue());
    }
}
