# UI Test Plan

<!-- test-ui-run: java -cp build/classes Edith -->

Run `javac -d build/classes src/main/java/*.java` with Java 25 before executing this plan. Each case starts a fresh application session.

## Test case 1: Add, list, mark, and unmark a todo

**Aim:** Verify that a user can add a todo, view it, mark it complete, and mark it incomplete again.

**Inputs:**
```text
todo read book
list
mark 1
unmark 1
bye
```

**Expected output:**
```text
 _____ ____ ___ _____ _   _
| ____|  _ \_ _|_   _| | | |
|  _| | | | | |  | | | |_| |
| |___| |_| | |  | | |  _  |
|_____|____/___| |_| |_| |_|

Hello! I'm EDITH.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [T][X] read book
____________________________________________________________
____________________________________________________________
OK, I've marked this task as not done yet:
  [T][ ] read book
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test case 2: Explain invalid command inputs

**Aim:** Verify that invalid commands and missing required fields produce specific, corrective error messages without ending the program.

**Inputs:**
```text
todo
deadline report
deadline /by tomorrow
deadline report /by
event meeting /from 2pm
event /from 2pm /to 3pm
event meeting /from /to 3pm
event meeting /from 2pm /to
mark
mark 1
blah
bye
```

**Expected output:**
```text
 _____ ____ ___ _____ _   _
| ____|  _ \_ _|_   _| | | |
|  _| | | | | | |  | | | |_| |
| |___| |_| | | |  | | |  _  |
|_____|____/___| |_| |_| |_|

Hello! I'm EDITH.
What can I do for you?
____________________________________________________________
____________________________________________________________
OOPS!!! The description of a todo cannot be empty.
____________________________________________________________
____________________________________________________________
OOPS!!! A deadline needs a description and a due time. Use: deadline DESCRIPTION /by TIME
____________________________________________________________
____________________________________________________________
OOPS!!! The description of a deadline cannot be empty. Use: deadline DESCRIPTION /by TIME
____________________________________________________________
____________________________________________________________
OOPS!!! A deadline needs a due time after /by. Use: deadline DESCRIPTION /by TIME
____________________________________________________________
____________________________________________________________
OOPS!!! An event needs a description, start time, and end time. Use: event DESCRIPTION /from START /to END
____________________________________________________________
____________________________________________________________
OOPS!!! The description of an event cannot be empty. Use: event DESCRIPTION /from START /to END
____________________________________________________________
____________________________________________________________
OOPS!!! An event needs a start time after /from. Use: event DESCRIPTION /from START /to END
____________________________________________________________
____________________________________________________________
OOPS!!! An event needs an end time after /to. Use: event DESCRIPTION /from START /to END
____________________________________________________________
____________________________________________________________
OOPS!!! Please provide a whole-number task number. Use: mark NUMBER
____________________________________________________________
____________________________________________________________
OOPS!!! There are no tasks to mark. Add a task first.
____________________________________________________________
____________________________________________________________
OOPS!!! I don't know what that means. Use todo, deadline, event, list, mark, unmark, or bye.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```
