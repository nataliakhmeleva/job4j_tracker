package ru.job4j.tracker;

import org.hamcrest.Matchers;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.*;

public class StartUITest {
    @Test
    public void whenAddItem() {
        Output output = new StubOutput();
        Input input = new StubInput(new String[]{"0", "Item name", "1"});
        Tracker tracker = Tracker.getInstance();
        UserAction[] actions = {new CreateAction(output), new ExitAction()};
        new StartUI(output).init(input, tracker, actions);
        assertThat(tracker.findAll()[0].getName(), is("Item name"));
    }

    @Test
    public void whenShowAllItem() {
        Output output = new StubOutput();
        Tracker tracker = Tracker.getInstance();
        Item item = tracker.add(new Item("Item name"));
        Input input = new StubInput(new String[]{"0", "1"});
        UserAction[] actions = {new ShowAction(output), new ExitAction()};
        new StartUI(output).init(input, tracker, actions);
        assertThat(output.toString(), is("Menu." + System.lineSeparator() + "0. Show all" +
                System.lineSeparator() + "1. Exit" + System.lineSeparator() +
                "=== Show all items ====" + System.lineSeparator() + "Item{id=1, name='Item name'}" +
                System.lineSeparator() + "Menu." + System.lineSeparator() + "0. Show all" +
                System.lineSeparator() + "1. Exit" + System.lineSeparator()));
    }

    @Test
    public void whenReplaceItem() {
        Output output = new StubOutput();
        Tracker tracker = Tracker.getInstance();
        Item item = tracker.add(new Item("new"));
        String replacedName = "replaced";
        Input input = new StubInput(new String[]{"0", String.valueOf(item.getId()), replacedName, "1"});
        UserAction[] actions = {new ReplaceAction(output), new ExitAction()};
        new StartUI(output).init(input, tracker, actions);
        assertThat(tracker.findById(item.getId()).getName(), is("replaced"));
    }

    @Test
    public void whenDeleteItem() {
        Output output = new StubOutput();
        Tracker tracker = Tracker.getInstance();
        Item item = tracker.add(new Item("new"));
        Input input = new StubInput(new String[]{"0", String.valueOf(item.getId()), "1"});
        UserAction[] actions = {new DeleteAction(output), new ExitAction()};
        new StartUI(output).init(input, tracker, actions);
        assertThat(tracker.findById(item.getId()), is(nullValue()));
    }

    @Test
    public void whenFindItemById() {
        Output output = new StubOutput();
        Tracker tracker = Tracker.getInstance();
        Item item = tracker.add(new Item("Item name"));
        Input input = new StubInput(new String[]{"0", String.valueOf(item.getId()), "1"});
        UserAction[] actions = {new FindByIdAction(output), new ExitAction()};
        new StartUI(output).init(input, tracker, actions);
        assertThat(output.toString(), is("Menu." + System.lineSeparator() + "0. Find by Id" +
                System.lineSeparator() + "1. Exit" + System.lineSeparator() +
                "=== Find item by Id ====" + System.lineSeparator() + "Item{id=1, name='Item name'}" +
                System.lineSeparator() + "Menu." + System.lineSeparator() + "0. Find by Id" +
                System.lineSeparator() + "1. Exit" + System.lineSeparator()));
    }

    @Test
    public void whenFindItemByName() {
        Output output = new StubOutput();
        Tracker tracker = Tracker.getInstance();
        Item item = tracker.add(new Item("Item name"));
        Input input = new StubInput(new String[]{"0", item.getName(), "1"});
        UserAction[] actions = {new FindByNameAction(output), new ExitAction()};
        new StartUI(output).init(input, tracker, actions);
        assertThat(output.toString(), is("Menu." + System.lineSeparator() + "0. Find by name" +
                System.lineSeparator() + "1. Exit" + System.lineSeparator() +
                "=== Find items by name ====" + System.lineSeparator() + "Item{id=1, name='Item name'}" +
                System.lineSeparator() + "Menu." + System.lineSeparator() + "0. Find by name" +
                System.lineSeparator() + "1. Exit" + System.lineSeparator()));
    }

    @Test
    public void whenExit() {
        Output out = new StubOutput();
        Input in = new StubInput(
                new String[]{"0"}
        );
        Tracker tracker = Tracker.getInstance();
        UserAction[] actions = {
                new ExitAction()
        };
        new StartUI(out).init(in, tracker, actions);
        assertThat(out.toString(), is(
                "Menu." + System.lineSeparator() +
                        "0. Exit" + System.lineSeparator()
        ));
    }

    @Test
    public void whenInvalidExit() {
        Output out = new StubOutput();
        Input in = new StubInput(
                new String[] {"1", "0"}
        );
        Tracker tracker = Tracker.getInstance();
        UserAction[] actions = {
                new ExitAction()
        };
        new StartUI(out).init(in, tracker, actions);
        assertThat(out.toString(), is(
                String.format(
                        "Menu.%n"
                                + "0. Exit%n"
                                + "Wrong input, you can select: 0 .. 0%n"
                                + "Menu.%n"
                                + "0. Exit%n"
                )
        ));
    }
}