package io.examples.account.application;

import io.vlingo.xoom.server.VlingoScene;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;

@Singleton
public class AccountContext {

    private final VlingoScene scene;
    private final AccountClient accountClient;
    private Account account;

    public AccountContext(AccountClient accountClient, VlingoScene scene) {
        this.accountClient = accountClient;
        this.scene = scene;
    }

    public Account getAccount() {
        return account;
    }

    @PostConstruct
    void init() {
        if (!scene.isRunning())
            scene.start();

        account = scene.getWorld().stage().actorFor(Account.class, AccountActor.class, accountClient);
    }
}
