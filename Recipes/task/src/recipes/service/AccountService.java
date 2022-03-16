package recipes.service;

import recipes.model.Account;

public interface AccountService {
    void add(Account account);
    Account get(Long id);
    Account getByEmail(String email);
}
