package org.zells.qi.cli.fakes;

import org.zells.qi.cli.User;

public class FakeUser extends User {

    public String told;

    @Override
    public void tell(String output) {
        told = output;
    }

    public void say(String input) {
        hear(input);
    }
}
