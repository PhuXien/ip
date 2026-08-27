# UI Test Session

Run command: `java -cp build/classes Edith`

## Test case 1: Add, list, mark, and unmark a todo

Result: **PASS** (exit code 0)

### Console input
```text
todo read book
list
mark 1
unmark 1
bye
```

### Actual console output
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
