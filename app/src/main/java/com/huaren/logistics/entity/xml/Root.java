package com.huaren.logistics.entity.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by bj on 2017/8/21.
 */
public class Root {
    @XStreamAlias("LoginResult")
    private LoginResult loginResult;
    @XStreamAlias("CommandResult")
    private CommandResult commandResult;

    public LoginResult getLoginResult() {
        return loginResult;
    }

    public void setLoginResult(LoginResult loginResult) {
        this.loginResult = loginResult;
    }

    public CommandResult getCommandResult() {
        return commandResult;
    }

    public void setCommandResult(CommandResult commandResult) {
        this.commandResult = commandResult;
    }

    @Override
    public String toString() {
        return "Root{" +
                "loginResult=" + loginResult +
                ", commandResult=" + commandResult +
                '}';
    }
}
