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
