package com.sang.userLoginAndRegistration.registration;

import com.sang.userLoginAndRegistration.appuser.AppUser;
import com.sang.userLoginAndRegistration.appuser.AppUserRole;
import com.sang.userLoginAndRegistration.appuser.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistrationService {

  private final AppUserService appUserService;
  private final EmailValidator emailValidator;

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
}
