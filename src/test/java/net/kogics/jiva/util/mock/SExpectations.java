package net.kogics.jiva.util.mock;

import org.hamcrest.Matcher;
import org.jmock.Expectations;

public class SExpectations extends Expectations {
    public <T> T withArg(Matcher<T> matcher) {
        return super.with(matcher);
    }

    public int withArg(Matcher<Integer> matcher) {
        return super.with(matcher);
    }
}
