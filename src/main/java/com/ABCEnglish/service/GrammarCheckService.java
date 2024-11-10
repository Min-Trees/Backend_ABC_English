package com.ABCEnglish.service;

import com.ABCEnglish.entity.GrammarError;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.languagetool.JLanguageTool;
import org.languagetool.language.AmericanEnglish;
import org.languagetool.language.English;
import org.languagetool.rules.RuleMatch;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GrammarCheckService {
    private final JLanguageTool languageTool;

    public GrammarCheckService() {
        this.languageTool = new JLanguageTool(new English());
    }

    public List<GrammarError> checkGrammar(String text) throws IOException {
        JLanguageTool langTool = new JLanguageTool(new AmericanEnglish());
        List<RuleMatch> matches = langTool.check(text);

        return matches.stream()
                .map(match -> new GrammarError(
                        match.getMessage(),
                        match.getSuggestedReplacements(),
                        match.getFromPos(),
                        match.getToPos()
                ))
                .collect(Collectors.toList());
    }
}
