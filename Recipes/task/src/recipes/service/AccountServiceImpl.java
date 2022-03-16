package recipes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recipes.model.Account;
import recipes.repository.AccountRepository;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;

    @Autowired
    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void add(Account account) {
        accountRepository.save(account);
    }

    @Override
    public Account get(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    @Override
    public Account getByEmail(String email) {
        return accountRepository.findByEmail(email).orElse(null);
    }
}
