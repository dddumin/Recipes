package recipes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import recipes.model.Account;
import recipes.service.AccountService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@Validated
public class AccountController {

    private AccountService accountService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(path = "/register")
    public ResponseEntity<?> createAccount(@Valid @RequestBody Account account) {
        try {
            System.out.println(account);
            accountService.add(new Account(account.getEmail(), passwordEncoder.encode(account.getPassword())));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
