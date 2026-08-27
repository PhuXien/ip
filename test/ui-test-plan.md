# UI Test Plan

<!-- test-ui-run: java -cp build/classes Edith -->

Run `javac -d build/classes src/main/java/*.java` with Java 25 before executing this plan. Each case starts a fresh application session.

## Test case 1: Delete a task and renumber the remaining list

**Aim:** Verify that deletion removes the requested task, reports the new count, and renumbers subsequent tasks.

**Inputs:**
```text
todo read book
deadline return book /by June 6th
event project meeting /from Aug 6th 2pm /to 4pm
todo join sports club
todo borrow book
mark 1
mark 2
mark 4
list
delete 3
list
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
Got it. I've added this task:
  [D][ ] return book (by: June 6th)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] join sports club
Now you have 4 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] borrow book
Now you have 5 tasks in the list.
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [T][X] read book
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [D][X] return book (by: June 6th)
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [T][X] join sports club
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][X] read book
2.[D][X] return book (by: June 6th)
3.[E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
4.[T][X] join sports club
5.[T][ ] borrow book
____________________________________________________________
____________________________________________________________
Noted. I've removed this task:
  [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
Now you have 4 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][X] read book
2.[D][X] return book (by: June 6th)
3.[T][X] join sports club
4.[T][ ] borrow book
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
delete
delete 1
blah
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
OOPS!!! Please provide a whole-number task number. Use: delete NUMBER
____________________________________________________________
____________________________________________________________
OOPS!!! There are no tasks to delete. Add a task first.
____________________________________________________________
____________________________________________________________
OOPS!!! I don't know what that means. Use todo, deadline, event, list, mark, unmark, delete, or bye.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```
