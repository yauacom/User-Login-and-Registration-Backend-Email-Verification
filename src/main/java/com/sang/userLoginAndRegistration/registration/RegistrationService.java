package com.sang.userLoginAndRegistration.registration;

import com.sang.userLoginAndRegistration.appuser.AppUser;
import com.sang.userLoginAndRegistration.appuser.AppUserRole;
import com.sang.userLoginAndRegistration.appuser.AppUserService;
import com.sang.userLoginAndRegistration.registration.token.ConfirmationToken;
import com.sang.userLoginAndRegistration.registration.token.ConfirmationTokenService;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class RegistrationService {

  private final AppUserService appUserService;
  private final EmailValidator emailValidator;
  private final ConfirmationTokenService confirmationTokenService;

  public String register(RegistrationRequest request) {
    boolean isValidEmail = emailValidator.test(request.getEmail());
    if(!isValidEmail) {
      throw new IllegalStateException(String.format("email %s is not valid", request.getEmail()));
    }

    return appUserService.signUpUser(
        new AppUser(
            request.getFirstName(),
            request.getLastName(),
            request.getEmail(),
            request.getPassword(),
            AppUserRole.USER
        )
    );
  }

  @Transactional
  public String confirmToken(String token) {
    ConfirmationToken confirmationToken = confirmationTokenService
        .getToken(token)
        .orElseThrow(() ->
            new IllegalStateException("token not found"));

    if (confirmationToken.getConfirmedAt() != null) {
      throw new IllegalStateException("email already confirmed");
    }

    LocalDateTime expiredAt = confirmationToken.getExpiresAt();

    if (expiredAt.isBefore(LocalDateTime.now())) {
      throw new IllegalStateException("token expired");
    }

    confirmationTokenService.setConfirmedAt(token);
    appUserService.enableAppUser(
        confirmationToken.getAppUser().getEmail());
    return "confirmed";
  }



}
