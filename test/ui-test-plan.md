# UI Test Plan

<!-- test-ui-run: java -cp build/classes edith.Edith -->

Run `javac -d build/classes (Get-ChildItem -Recurse src/main/java -Filter *.java)` with Java 25 before executing this plan. Begin with no `data/` directory. Each case starts a fresh application session; later cases intentionally retain the data saved by earlier cases to test persistence.

## Test case 1: Delete a task and renumber the remaining list

**Aim:** Verify that deletion removes the requested task, reports the new count, and renumbers subsequent tasks.

**Inputs:**
```text
todo read book
deadline return book /by 2019-12-02 1800
event project meeting /from 2019-08-06 1400 /to 2019-08-06 1600
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
  [D][ ] return book (by: Dec 02 2019 18:00)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [E][ ] project meeting (from: Aug 06 2019 14:00 to: Aug 06 2019 16:00)
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
  [D][X] return book (by: Dec 02 2019 18:00)
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [T][X] join sports club
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][X] read book
2.[D][X] return book (by: Dec 02 2019 18:00)
3.[E][ ] project meeting (from: Aug 06 2019 14:00 to: Aug 06 2019 16:00)
4.[T][X] join sports club
5.[T][ ] borrow book
____________________________________________________________
____________________________________________________________
Noted. I've removed this task:
  [E][ ] project meeting (from: Aug 06 2019 14:00 to: Aug 06 2019 16:00)
Now you have 4 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][X] read book
2.[D][X] return book (by: Dec 02 2019 18:00)
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
deadline report /by tomorrow
event meeting /from 2pm /to 2019-08-06
event meeting /from 2019-08-06 /to 2pm
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
OOPS!!! A deadline needs a description and due date. Use: deadline DESCRIPTION /by yyyy-MM-dd [HHmm]
____________________________________________________________
____________________________________________________________
OOPS!!! The description of a deadline cannot be empty. Use: deadline DESCRIPTION /by yyyy-MM-dd [HHmm]
____________________________________________________________
____________________________________________________________
OOPS!!! A deadline needs a due date after /by. Use: deadline DESCRIPTION /by yyyy-MM-dd [HHmm]
____________________________________________________________
____________________________________________________________
OOPS!!! The deadline date must use yyyy-MM-dd, optionally followed by a time in the format HHmm.
____________________________________________________________
____________________________________________________________
OOPS!!! The event start date must use yyyy-MM-dd, optionally followed by a time in the format HHmm.
____________________________________________________________
____________________________________________________________
OOPS!!! The event end date must use yyyy-MM-dd, optionally followed by a time in the format HHmm.
____________________________________________________________
____________________________________________________________
OOPS!!! An event needs a description, start date, and end date. Use: event DESCRIPTION /from yyyy-MM-dd [HHmm] /to yyyy-MM-dd [HHmm]
____________________________________________________________
____________________________________________________________
OOPS!!! The description of an event cannot be empty. Use: event DESCRIPTION /from yyyy-MM-dd [HHmm] /to yyyy-MM-dd [HHmm]
____________________________________________________________
____________________________________________________________
OOPS!!! An event needs a start date after /from. Use: event DESCRIPTION /from yyyy-MM-dd [HHmm] /to yyyy-MM-dd [HHmm]
____________________________________________________________
____________________________________________________________
OOPS!!! An event needs an end date after /to. Use: event DESCRIPTION /from yyyy-MM-dd [HHmm] /to yyyy-MM-dd [HHmm]
____________________________________________________________
____________________________________________________________
OOPS!!! Please provide a whole-number task number. Use: mark NUMBER
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [T][X] read book
____________________________________________________________
____________________________________________________________
OOPS!!! Please provide a whole-number task number. Use: delete NUMBER
____________________________________________________________
____________________________________________________________
Noted. I've removed this task:
  [T][X] read book
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
OOPS!!! I don't know what that means. Use todo, deadline, event, list, mark, unmark, delete, or bye.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test case 3: Restore tasks in a new session

**Aim:** Verify that tasks saved by earlier commands are loaded when Edith starts in a new application session.

**Inputs:**
```text
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
Here are the tasks in your list:
1.[D][X] return book (by: Dec 02 2019 18:00)
2.[T][X] join sports club
3.[T][ ] borrow book
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test case 4: Mark a restored task as incomplete

**Aim:** Verify that unmarking updates a restored task, saves it, and displays its new status.

**Inputs:**
```text
unmark 1
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
OK, I've marked this task as not done yet:
  [D][ ] return book (by: Dec 02 2019 18:00)
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[D][ ] return book (by: Dec 02 2019 18:00)
2.[T][X] join sports club
3.[T][ ] borrow book
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```
