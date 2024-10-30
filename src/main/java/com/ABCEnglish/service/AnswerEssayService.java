package com.ABCEnglish.service;

import com.ABCEnglish.dto.request.AnswerEssayRequest;
import com.ABCEnglish.dto.request.IntrospectRequest;
import com.ABCEnglish.dto.response.AnswerEssayResponse;
import com.ABCEnglish.entity.AnswerEssay;
import com.ABCEnglish.entity.GrammarError;
import com.ABCEnglish.entity.Question;
import com.ABCEnglish.entity.User;
import com.ABCEnglish.exceptioin.AppException;
import com.ABCEnglish.exceptioin.ErrorCode;
import com.ABCEnglish.mapper.AnswerEssayMapper;
import com.ABCEnglish.reponsesitory.AnswerEssayRepository;
import com.ABCEnglish.reponsesitory.QuestionRepository;
import com.ABCEnglish.reponsesitory.UserRepository;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.languagetool.rules.RuleMatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnswerEssayService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    AnswerEssayMapper answerEssayMapper;
    @Autowired
    AnswerEssayRepository answerEssayRepository;
    @Autowired
    private GrammarCheckService grammarCheckService;
    @Transactional
    public AnswerEssayResponse createAnswerEssay(Integer questionId, AnswerEssayRequest request, IntrospectRequest token) throws ParseException, JOSEException, IOException {
        Integer userId = authenticationService.introspectToken(token).getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUND));

        AnswerEssay answerEssay = answerEssayMapper.toAnswerEssay(request);
        answerEssay.setUser(user);
        answerEssay.setQuestion(question);

        // Kiểm tra ngữ pháp cho nội dung
        List<GrammarError> grammarErrors = grammarCheckService.checkGrammar(request.getContent());

        // Gán các lỗi ngữ pháp vào đối tượng AnswerEssayResponse
        AnswerEssayResponse response = answerEssayMapper.answerEssayResponse(answerEssay);
        response.setGrammarErrors(grammarErrors); // Cập nhật với danh sách lỗi ngữ pháp

        answerEssayRepository.save(answerEssay);
        return response;
    }

}
