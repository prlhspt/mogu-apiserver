package com.mogu.apiserver.application.user;

import com.mogu.apiserver.application.user.request.UpdateUserServiceRequest;
import com.mogu.apiserver.domain.account.Account;
import com.mogu.apiserver.domain.account.AccountRepository;
import com.mogu.apiserver.domain.user.User;
import com.mogu.apiserver.domain.user.UserRepository;
import com.mogu.apiserver.domain.user.exception.InactivateUserException;
import com.mogu.apiserver.domain.user.exception.UserNotFoundException;
import com.mogu.apiserver.presentation.user.response.CheckUserResponse;
import com.mogu.apiserver.presentation.user.response.DeleteUserResponse;
import com.mogu.apiserver.presentation.user.response.GetUserInfoResponse;
import com.mogu.apiserver.presentation.user.response.UpdateUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public GetUserInfoResponse getUserDetails(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        if (!user.isActivated()) {
            throw new InactivateUserException();
        }

        return GetUserInfoResponse.builder()
                .nickname(user.getNickname())
                .build();
    }

    public CheckUserResponse checkEmailRegistrationStatus(String email) {
        Optional<Account> optionalAccount = accountRepository.findByEmailWithUser(email);

        CheckUserResponse.CheckUserResponseBuilder builder = CheckUserResponse.builder();

        if (optionalAccount.isPresent()) {
            return builder
                    .isRegistered(true)
                    .userStatus(optionalAccount.get().getUser().getStatus())
                    .build();
        } else {
            return builder
                    .isRegistered(false)
                    .build();
        }
    }

    @Transactional
    public UpdateUserResponse updateUser(Long userId, UpdateUserServiceRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        if (!user.isActivated()) {
            throw new InactivateUserException();
        }

        user.updateNotNullValue(request.getNickname());

        return UpdateUserResponse.builder()
                .userId(user.getId())
                .build();
    }

    @Transactional
    public DeleteUserResponse deleteUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        if (!user.isActivated()) {
            throw new InactivateUserException();
        }

        user.delete();

        return DeleteUserResponse.builder()
                .userId(user.getId())
                .build();
    }

}
