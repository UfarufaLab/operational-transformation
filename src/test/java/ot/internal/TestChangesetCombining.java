package ot.internal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class TestChangesetCombining {
    private Text original;
    private Text user1;
    private Text user2;
    private Text expected;

    public TestChangesetCombining(String original, String user1, String user2, String expected) {
        this.original = Text.wrap(original);
        this.user1 = Text.wrap(user1);
        this.user2 = Text.wrap(user2);
        this.expected = Text.wrap(expected);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Object[][] d = {
                {"Big cat", "Big bat", "Small cat", "Small bat"},
                {"", "aaa", "bbb", "aaabbb"},
                {"", "Hello", "Hi", "HelloHi"},
                {"to be or not to be", "to be or", "or not to be", "or"},
                {"to be or not to be", "be or", "not to be", ""},
                {"HelloHi", "HelloHi", "Hi", "Hi"},
                {"Hi", "Hello", "Hi", "Hello"},
                {"Hello adventurer!", "Hello treasured adventurer!", "Good day adventurer!", "Good day treasured adventurer!"},
                {"Hello adventurer!", "Hello treasured adventurer!", "Good day adventurers, y'all!", "Good day treasured adventurers, y'all!"},
                {"123", "a123", "123b", "a123b"},
                {"123", "1a23", "1b23", "1ab23"},
                {"123", "12a3", "b123", "b12a3"},
                {"123", "1a23", "12", "1a2"},
                {"123", "1a23", "13", "1a3"},
                {"123", "12a3", "3", "a3"},
                {"123", "12a3", "23", "2a3"},
                {"123", "12a3", "1", "1a"},
                {"123", "12a3", "", "a"},
                {"1234", "124", "234", "24"},
                {"1234", "234", "124", "24"},
                {"123", "3", "13", "3"},
                {"123", "3", "23", "3"},
                {"1234", "4", "134", "4"},
                {"123", "13", "1", "1"},
                {"123", "12", "1", "1"},
                {"1234", "134", "4", "4"},
                {"123", "23", "123b", "23b"},
                {"123", "3", "1b23", "b3"},
                {"123", "13", "1b23", "1b3"},
                {"123", "1", "1b23", "1b"},
                {"123", "1", "1bbb23", "1bbb"},
                {"123", "12", "b123", "b12"},
                {"123", "1a2b3c", "123", "1a2b3c"}
        };
        return asList(d);
    }

    @Test
    public void test() throws Exception {
        Change diff1 = original.diff(user1);
        Change diff2 = original.diff(user2);
        Change fixedDiff1 = diff1.transform(diff2);
        Change fixedDiff2 = diff2.transform(diff1);

        Text actual1 = Text.copy(original).apply(diff1).apply(fixedDiff2);
        Text actual2 = Text.copy(original).apply(diff2).apply(fixedDiff1);

        assertEquals(String.format("'%s' != '%s'", expected, actual1), expected, actual1);
        assertEquals(String.format("'%s' != '%s'", expected, actual2), expected, actual2);
    }
}
