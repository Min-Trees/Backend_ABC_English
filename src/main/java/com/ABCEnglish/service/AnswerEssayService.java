package com.ABCEnglish.service;

import com.ABCEnglish.dto.request.AnswerEssayRequest;
import com.ABCEnglish.dto.request.IntrospectRequest;
import com.ABCEnglish.dto.response.AnswerEssayResponse;
import com.ABCEnglish.entity.AnswerEssay;
import com.ABCEnglish.entity.GrammarError;
import com.ABCEnglish.entity.Question;
import com.ABCEnglish.entity.User;
import com.ABCEnglish.exception.AppException;
import com.ABCEnglish.exception.ErrorCode;
import com.ABCEnglish.mapper.AnswerEssayMapper;
import com.ABCEnglish.repository.AnswerEssayRepository;
import com.ABCEnglish.repository.QuestionRepository;
import com.ABCEnglish.repository.UserRepository;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

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
    @Autowired
    private TokenService tokenService;

    @Transactional
    public AnswerEssayResponse createAnswerEssay(Integer questionId, AnswerEssayRequest request, String token) throws ParseException, JOSEException, IOException {
        User user = tokenService.getTeacherOrAdmin(token,true);
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
