package com.sang.userLoginAndRegistration.registration;

import java.util.function.Predicate;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
public class EmailValidator implements Predicate<String> {
  private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

  @Override
  public boolean test(String email) {
    Predicate<String> emailFilter = EMAIL_PATTERN.asPredicate();
    return emailFilter.test(email);
  }
}
