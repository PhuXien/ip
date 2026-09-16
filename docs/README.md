# Edith User Guide

// Update the title above to match the actual product name

// Product screenshot goes here

// Product intro goes here

## Adding tags when creating tasks

Add one or more tags at the end of a task command with `/tags`. A tag starts with `#` and contains
letters, digits, underscores, or hyphens. Tag names are compared without regard to case; Edith keeps
the spelling used when a tag is first added. Tags appear after any date details.

Examples:

```text
todo read book /tags #fun #school
deadline submit work /by 2026-09-20 /tags #school
event meeting /from 2026-09-20 /to 2026-09-21 /tags #team
```

For the first example, Edith replies:

```text
Got it. I've added this task:
  [T][ ] read book [tags: #fun #school]
Now you have 1 tasks in the list.
```

The `/tags` marker is optional and must come after the task description and any dates. For example,
`todo read #fun` creates an untagged task whose description includes `#fun`.

## Changing tags on existing tasks

Use `tag NUMBER #tag [#tag ...]` to add tags or `untag NUMBER #tag [#tag ...]` to remove them.
`NUMBER` is the task's position in the full `list`, even after a `find` result shows different numbers.
Edith skips tags that are already present when adding and tags that are absent when removing.

For example, if task 2 is `[T][ ] read book [tags: #fun]`, entering `tag 2 #Fun #school` gives:

```text
Got it. I've added tags to this task:
  [T][ ] read book [tags: #fun #school]
```

Entering `untag 2 #fun #missing` then gives:

```text
Noted. I've removed tags from this task:
  [T][ ] read book [tags: #school]
```

When every requested tag is already present or absent, Edith reports that no tags changed.
All tags in a command must be valid before Edith changes the task.

## Finding tagged tasks

Use `find #tag` to find tasks with that exact tag, regardless of letter case. For example,
`find #fun` matches a task tagged `#Fun`, but does not match `#funny` or a description that
merely contains `#fun`.

Other nonempty searches look for the text in task descriptions and individual tags, without
regard to case. For example, `find fun` can match a description containing `fun` and tasks
tagged `#fun` or `#funny`. A query such as `find #fun party` searches for that whole phrase
in descriptions and tags because the full query is not a valid tag. Matches appear in their
original order, numbered from 1 within the results.

## Adding deadlines

// Describe the action and its outcome.

// Give examples of usage

Example: `keyword (optional arguments)`

// A description of the expected outcome goes here

```
expected output
```

## Feature ABC

// Feature details


## Feature XYZ

// Feature details
