package recipes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import recipes.model.Account;
import recipes.model.dto.AccountDetails;
import recipes.repository.AccountRepository;

@Service
public class AccountDetailsServiceImpl implements UserDetailsService {

    private AccountRepository accountRepository;

    @Autowired
    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(username).orElse(null);
        if (account == null) {
            throw new UsernameNotFoundException(String.format("User with email %s not found", username));
        }
        return new AccountDetails(account);
    }
}
