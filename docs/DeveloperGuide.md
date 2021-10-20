---
layout: page
title: Developer Guide
---
* Table of Contents
  {:toc}

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

This project is based on the AddressBook-Level3 project created by the [SE-EDU initiative](https://se-education.org).

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams in this document can be found in the [diagrams](https://github.com/se-edu/addressbook-level3/tree/master/docs/diagrams/) folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** has two classes called [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java). It is responsible for,
* At app launch: Initializes the components in the correct sequence, and connects them up with each other.
* At shut down: Shuts down the components and invokes cleanup methods where necessary.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

The rest of the App consists of four components.

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.


**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Student` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

How the `Logic` component works:
1. When `Logic` is called upon to execute a command, it uses the `AddressBookParser` class to parse the user command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `AddCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to add a student).
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

The Sequence Diagram below illustrates the interactions within the `Logic` component for the `execute("delete 1")` API call.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.
</div>

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](images/ModelClassDiagram.png)

<img src="images/ModelClassDiagram.png" width="450" />


The `Model` component,

* stores the tuitione data i.e., all `Student` objects (which are contained in a `UniqueStudentList` object).
* stores the tuitione data i.e., all `Lesson` objects (which are contained in a `UniqueLessonList` object).
* stores the currently 'selected' `Student` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Student>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores the currently 'selected' `Lesson` objects (e.g., results of a filter) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Lesson>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Remark` list in the `AddressBook`, which `Student` references. This allows `AddressBook` to only require one `Remark` object per unique remark, instead of each `Student` needing their own `Remark` objects.<br>

<img src="images/BetterModelClassDiagram.png" width="450" />

</div>


### Storage component

**API** : [`Storage.java`](https://github.com/AY2122S1-CS2103T-F13-4/tp/blob/master/src/main/java/seedu/tuitione/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both tuitione data and user preference data in json format, and read them back into corresponding objects.
* inherits from both `TuitioneStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.addressbook.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Add Lesson feature

#### Implementation

Object diagram

Sequence diagram

Activity diagram

#### Design considerations:

Command syntax

### Delete Lesson feature

#### Implementation

Object diagram

Sequence diagram

Activity diagram

#### Design considerations:

Command syntax

### Enroll feature

Description

#### Implementation

Object diagram

Sequence diagram

Activity diagram

#### Design considerations:

Command syntax

### Unenroll feature

#### Implementation

Object diagram

Sequence diagram

Activity diagram

#### Design considerations:

Command syntax

### Filter feature

#### Implementation

The filter operation is facilitated by `FilterCommand` and `FilterCommandParser`. `FilterCommandParser` first parses the user
input to extract out the command and the arguments, after which the `FilterCommand#execute(model)` method is invoked in
the `LogicManager` class to filter the `filteredStudents` and/or the `filteredLessons` list(s) in the `model` based on the given user inputs.
The filter performs differently based on the inputs given (grade, subject, or both):
* If only grade is given as input, TuitiONE filters both the student list and the lesson list based on the given grade.
* If only subject is given as input, TuitiONE filters only the lesson list based on the given subject.
* If both are given as input, TuitiONE filters the student list by the given grade, but filters the lesson list based
on both the given grade and subject.

Given below is an example usage scenario and how the filter operation works.

Step 1: The user launches the app with the stored student list holding the initial student data and the lesson list holding the
initial lesson data in TuitiONE (only the fields of each object relevant to filter are shown in the diagrams below).
![FilterState0](images/FilterState0.png)

Step 2: The user executes `filter g/S2 s/English`  to filter out S2 English lessons and S2 students. The `filter` command causes
the `FilterCommand#execute(model)` method to be called which then filters the respective lists to only show the relevant objects.
![FilterState1](images/FilterState1.png)

Step 3: The user executes `list` to get back the initial lists before the filter. 

The following sequence diagram shows how the filter operation works:
![FilterSequenceDiagram](images/FilterSequenceDiagram.png)
<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `FilterCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

The following activity diagram summarizes what happens when a user executes the filter command:
![FilterActivityDiagram](images/FilterActivityDiagram.png)


#### Design considerations:

**Aspect: How to implement filter**
* **Alternative 1 (current choice)**: one filter command that handles both grade and subject filtering
    * Pros: Less commands to remember, user will not feel overwhelmed.
    * Cons: Slightly more difficult to implement, as one command has to handle the 3 cases of user input as mentioned above.
* **Alternative 2**: 3 separate filter commands, one for each scenario stated above
    * Pros: Slightly more straightforward to implement.
    * Cons: Too many existing commands in the application, and may not be as intuitive to use.

Command syntax: `filter [g/GRADE] [s/SUBJECT]`

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedTuitione`. It extends `Tuitione` with an undo/redo history, stored internally as an `tuitioneStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedTuitione#commit()` — Saves the current tuitione state in its history.
* `VersionedTuitione#undo()` — Restores the previous tuitione state from its history.
* `VersionedTuitione#redo()` — Restores a previously undone tuitione state from its history.

These operations are exposed in the `Model` interface as `Model#commitTuitione()`, `Model#undoTuitione()` and `Model#redoTuitione()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedTuitione` will be initialized with the initial tuitione state, and the `currentStatePointer` pointing to that single tuitione state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th student in the tuitione. The `delete` command calls `Model#commit**Tuitione**()`, causing the modified state of the tuitione after the `delete 5` command executes to be saved in the `tuitioneStateList`, and the `currentStatePointer` is shifted to the newly inserted tuitione state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add n/David …​` to add a new student. The `add` command also calls `Model#commitTuitione()`, causing another modified tuitione state to be saved into the `tuitioneStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitTuitione()`, so the tuitione state will not be saved into the `tuitioneStateList`.

</div>

Step 4. The user now decides that adding the student was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoTuitione()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous tuitione state, and restores the tuitione to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial Tuitione state, then there are no previous Tuitione states to restore. The `undo` command uses `Model#canUndoTuitione()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the undo.

</div>

The following sequence diagram shows how the undo operation works:

![UndoSequenceDiagram](images/UndoSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

The `redo` command does the opposite — it calls `Model#redoTuitione()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the tuitione to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `tuitioneStateList.size() - 1`, pointing to the latest tuitione state, then there are no undone Tuitione states to restore. The `redo` command uses `Model#canRedoTuitione()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the tuitione, such as `list`, will usually not call `Model#commitTuitione()`, `Model#undoTuitione()` or `Model#redoTuitione()`. Thus, the `tuitioneStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `Model#commitTuitione()`. Since the `currentStatePointer` is not pointing at the end of the `tuitioneStateList`, all tuitione states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire tuitione.
    * Pros: Easy to implement.
    * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
    * Pros: Will use less memory (e.g. for `delete`, just save the student being deleted).
    * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* has a need to manage a significant number of student details in a tuition centre
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**: Provide a more streamlined platform, as compared to conventional excel which might have numerous irrelevant functions. This platform also offers a more intuitive UI - with a clean and minimalist layout. Helps manage student admin information faster than a typical mouse-driven app. The app is only used for one tuition centre.


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                                 | I want to …​                      | So that I can …​                                              |
| -------- | ------------------------------------------ | ------------------------------------ | -----------------------------------------------------------------|
| `* * *`  | Customer Service Officer                   | add a new student                    | keep track of student count and details                          |
| `* * *`  | Customer Service Officer                   | delete a student                     | keep track of student count and details                          |
| `* * *`  | Customer Service Officer                   | know a student's grade               | schedule them into the right lessons (eg. P5 student to P5 lessons) |
| `* * *`  | Customer Service Officer                   | look up a student by their name      | find out the student’s details                                   |
| `* * *`  | Customer Service Officer                   | know the lessons a student is enrolled in  | so that I can keep track of what lessons they need to pay for                                          |
| `* * *`  | Customer Service Officer                   | enroll a students into a lesson      | keep track of student count and details in lessons               |
| `* * *`  | Customer Service Officer                   | unenroll a student from a lesson     | keep track of student count and details in lessons               |
| `* * *`  | Customer Service Officer                   | know the contact of a student’s parent | call the parent if he is late or did not attend                |
| `* * *`  | Customer Service Officer                   | know a student's address             | send physical letters/ documents                                 |
| `* * *`  | Customer Service Officer                   | know a student's grade               | keep track of a student's grade                                  |
| `* * *`  | Customer Service Officer                   | know the email of a student’s parent | send emails to update the student                                |
| `* * *`  | Customer Service Officer                   | know the list of all lessons         | check the status of other lessons                                |
| `* * *`  | Customer Service Officer                   | add a new lesson                     | enroll students into the lesson                                  |
| `* * *`  | Customer Service Officer                   | delete an existing lesson            | remove outdated lessons                                          |
| `* * *`  | Customer Service Officer                   | know what time lessons start/end     | update relevant parties about the lesson timings                 |
| `* *`    | Customer Service Officer                   | update a student’s address           | ensure the details are updated to date                           |
| `* *`    | Customer Service Officer                   | update a student’s grade             | ensure the details are updated to date                           |
| `* *`    | Customer Service Officer                   | update the contact of a student’s parent | ensure the details are updated to date                       |
| `* *`    | Customer Service Officer                   | update the email of a student’s parent | ensure the details are updated to date                         |
| `* *`    | Customer Service Officer                   | filter students by their grade       | categorise students by their grades                              |
| `* *`    | Customer Service Officer                   | filter lessons by their grade        | categorise lessons by their grades                               |
| `* *`    | Customer Service Officer                   | filter lessons by their subject        | categorise lessons by their subjects                               |
| `* *`    | Customer Service Officer                   | know how many students are in the lesson | help tutors plan resources accordingly for the lesson        |
| `* *`    | Customer Service Officer                   | know the command format to enter     | learn to use the application                                     |
| `* *`    | Customer Service Officer                   | know how much a student has to pay per week | remind the parents to pay punctually                      |
| `* *`    | Customer Service Officer                   | leave remarks                        | make lessons more convenient for tutors and students in the case they are unable to make it for a specific lesson |
| `*`      | Customer Service Officer                   | know a student's attendance          | inform teachers or find out why students are missing lessons     |
| `*`      | Customer Service Officer                   | record a student's attendance        | keep track of student's attendance                               |
| `*`      | Customer Service Officer                   | update details of existing lessons   | change the specifics of the lesson                               |
| `*`      | Customer Service Officer                   | update a student’s performance stats | inform tuition teachers of their proficiency                     |
| `*`      | Customer Service Officer                   | know the lessons each teacher has    | remind them of their lessons                                     |
| `*`      | Customer Service Officer                   | add a teacher                        | keep track of their details                                      |
| `*`      | Customer Service Officer                   | delete a teacher                     | delete irrelevant teachers                                       |
| `*`      | Customer Service Officer                   | assign teachers to lessons           | keep track of their classes                                      |
| `*`      | Customer Service Officer                   | know students performance stats      | update the teachers/ students if they need that information      |
| `*`      | Customer Service Officer                   | keep track of student’s outstanding payments | know who has paid and remind parents to pay if they have yet to complete payment |
| `*`      | Customer Service Officer                   | read excel sheet                     | import my pre-existing administrative details                    |
| `*`      | Customer Service Officer                   | write to excel sheet                 | export my existing administrative details                        |


### Use cases

For all use cases below, the **System** is the `TuitiONE` and the **Actor** is the `Customer Service Officer (CSO)`, unless specified otherwise.

**UC01: View all Students and Lessons**

**MSS**

1. CSO enters to view all information.
2. TuitiONE displays all students and lessons present.

    Use case ends.

**UC02: Add a Student**

**MSS**

1. CSO enters details to add a student.
2. TuitiONE adds specified student to the list.

    Use case ends.

**Extensions**

* 1a. Missing compulsory details in command.
    * 1a1. TuitiONE requests CSO to input a valid command.
    * 1a2. CSO enters new command.
      Steps 1a1-1a2 are repeated until the data entered are correct.

    Use case resumes from step 2.

* 1b. TuitiONE detects an error in entered command.
    * 1b1. TuitiONE requests CSO to input a valid command.
    * 1b2. CSO enters new command.
      Steps 1b1-1b2 are repeated until the data entered are correct.

    Use case resumes at step 2.

* 1c. Student already exists in TuitiONE.
    * 1c1. TuitiONE informs that there already exist such a student.

    Use case ends.

**UC03: Look up Student(s)**

**MSS**

1. CSO enters keywords(s) to find specific student(s).
2. TuitiONE shows a list of relevant students.
3. CSO looks through the given results of student(s) and their details, such as:
    * Their grade
    * Their parent’s contact number
    * Their address
    * Their enrolled lessons
    * Their tuition subscription

    Use case ends.

**Extensions**

* 1a. TuitiONE detects an error in entered command.
    * 1a1. TuitiONE requests CSO to input a valid command.
    * 1a2. CSO enters new command.
      Steps 1a1-1a2 are repeated until the data entered are correct.

    Use case resumes at step 2.

* 2a. TuitiONE cannot find any results relevant to the search keyword.
    * 2a1. TuitiONE informs that there are no such students.

    Use case ends.

**UC04: Filter Students by grade and Lessons by grade and/or subject**

**MSS**
1. CSO wants to filter the student and/or lesson list by their grade and/or subject.
2. TuitiONE lists the students and/or lessons that matches the filter criteria.

    Use case ends.

**Extension**

* 1a. TuitiONE detects an error in entered command.
    * 1a1. TuitiONE requests CSO to input a valid command.
    * 1a2. CSO enters new command.
      Steps 1a1-1a2 are repeated until the input entered is correct.

    Use case resumes at step 2.

* 1b. CSO decides to filter by grade only
    * 1b1. TuitiONE filters the student and lesson list based on the given grade.

    Use case resumes at step 2.

* 1c. CSO decides to filter by subject only
    * 1c1. TuitiONE filters the lesson list based on the given subject.

    Use case resumes at step 2.

* 1d. CSO decides to filter by grade and subject
    * 1d1. TuitiONE filters the student list based on the given grade, and filters the lesson list based on both the
      given grade and subject.

    Use case resumes at step 2.

* 1e. TuitiONE detects an error in the input fields for grade and/or subject.
    * 1e1. TuitiONE requests CSO to input command with valid fields.
    * 1e2. CSO enters new command.
      Steps 1a1-1a2 are repeated until the input entered is correct.

    Use case resumes at step 2.

**UC05: Delete a Student**

**MSS**

1. CSO views the current list of students, <ins>look up student(s) (UC03)</ins>, or <ins>filter student(s) by their grade (UC04)</ins>.
2. CSO enters a specific student to delete from the list.
3. TuitiONE deletes the student.

    Use case ends.

**Extensions**

* 1a. The list is empty.

    Use case ends.

* 2a. TuitiONE detects an error in entered command.
    * 2a1. TuitiONE requests CSO to input a valid command.
    * 2a2. CSO enters new command.
      Steps 2a1-2a2 are repeated until the data entered are correct.

    Use case resumes at step 3.

* 2b. The student provided is not present in the list.
    * 2b1. TuitiONE informs that there is no such student.

    Use case ends.

**UC06: Add a Lesson**

**MSS**

1. CSO requests to add a lesson with relevant details.
2. TuitiONE adds the lesson.

    Use case ends.

**Extensions**

* 1a. TuitiONE detects an error in entered command.
    * 1a1. TuitiONE requests CSO to input a valid command.
    * 1a2. CSO enters new command.
      Steps 1a1-1a2 are repeated until the data entered are correct.

    Use case resumes at step 2.

* 1b. Lesson already exists in TuitiONE.
    * 1b1. TuitiONE informs that there already exist such a Lesson.

    Use case ends.

**UC07: View details of a Lesson**

**MSS**

1. CSO views a list of lessons.
2. TuitiONE shows a list of lessons, with their respective details:
    * Lesson subject
    * Grade
    * Time period
    * Price
    * Number of students enrolled

    Use case ends.

**UC08: Filter Lesson(s) by their Grade and/or Subject**

**MSS**
1. CSO enters grade and/or subject to filter lessons by.
2. TuitiONE lists the lessons that matches the grade and/or subject.

    Use case ends.

**Extension**

* 1a. TuitiONE detects an error in entered command.
    * 1a1. TuitiONE requests CSO to input a valid command.
    * 1a2. CSO enters new command.
      Steps 1a1-1a2 are repeated until the data entered are correct.

    Use case resumes at step 2.

**UC09: Delete a Lesson**

**MSS**

1. CSO views the current list of lessons, or <ins>filter lesson(s) by their grade and/or subject (UC08)</ins>.
2. CSO requests to delete a specific lesson in the list.
3. TuitiONE deletes the lesson.

    Use case ends.

**Extensions**

* 1a. The list is empty.

    Use case ends.

* 2a. TuitiONE detects an error in entered command.
    * 2a1. TuitiONE requests CSO to input a valid command.
    * 2a2. CSO enters new command.
      Steps 2a1-2a2 are repeated until the data entered are correct.

    Use case resumes at step 3.

* 2b. Lesson does not exists in TuitiONE.
    * 2b1. TuitiONE informs that there does not exist such a Lesson.

    Use case ends.

**UC10 - Update a specific Student’s Details**

**MSS**

1. CSO finds a student to update from the existing list, by <ins>looking up student(s) (UC03)</ins>, or by <ins>filtering student(s) by their grade (UC04)</ins>.
2. CSO enters the student and the details to update.
3. TuitiONE reflects the updated details of the student.

    Use case ends.

  **Extension**

* 2a. TuitiONE detects an error in entered command.
      * 2a1. TuitiONE requests CSO to input a valid command.
      * 2a2. CSO enters new command.
        Steps 2a1-2a2 are repeated until the data entered are correct.

    Use case resumes at step 3.

* 2b. The student requested to edit is not present in the list.
      * 2b1. TuitiONE informs that there is no such student.

    Use case ends.

**UC11: Enroll a Student to a Lesson**

**MSS**

1. CSO finds a student to enroll from the existing list, by <ins>looking up student(s) (UC03)</ins>, or by <ins>filtering student(s) by their grade (UC04)</ins>.
2. CSO finds a lesson for the student to enroll to based on the existing list, or by <ins>filtering lesson(s) by their grade and/or subject (UC08)</ins>.
3. CSO enters a student and a lesson to enroll the student to the said lesson.
4. TuitiONE adds the student to the lesson.

    Use case ends.

**Extensions**

* 3a. TuitiONE detects an error in entered command.
    * 2a1. TuitiONE requests CSO to input a valid command.
    * 2a2. CSO enters new command.
      Steps 2a1-2a2 are repeated until the data entered are correct.

    Use case resumes at step 4.

* 3b. TuitiONE cannot find the lesson.
    * 3b1. TuitiONE requests CSO to enter a valid lesson.
    * 3b2. CSO enters new command.
      Steps 3b1-3b2 are repeated until the data entered are valid.

    Use case resumes at step 4.

* 3c. TuitiONE cannot find the student.
    * 3c1. TuitiONE requests CSO to enter a valid student.
    * 3c2. CSO enters new command.
      Steps 3c1-3c2 are repeated until the data entered are valid.

    Use case resumes at step 4.

* 3d. Student is already enrolled to lesson.
    * 3d1. TuitiONE informs that student is already enrolled to lesson.

    Use case ends.

**UC12: Unenroll a Student from a Lesson**

**MSS**

1. CSO finds a student to unenroll from the existing list, by <ins>looking up student(s) (UC03)</ins>, or by <ins>filtering student(s) by their grade (UC04)</ins>.
2. CSO finds a lesson for the student to unenroll from based on the existing list, or by <ins>filtering lesson(s) by their grade and/or subject (UC08)</ins>.
3. CSO requests for student to be unenrolled from the lesson.
4. TuitiONE removes the student from the lesson.

    Use case ends.

**Extensions**

* 3a. TuitiONE detects an error in entered command.
    * 3a1. TuitiONE requests CSO to input a valid command.
    * 3a2. CSO enters new command.
      Steps 3a1-3a2 are repeated until the data entered are correct.

    Use case resumes at step 4.

* 3b. TuitiONE cannot find the lesson.
    * 3b1. TuitiONE requests CSO to enter a valid lesson.
    * 3b2. CSO enters new command.
      Steps 3b1-3b2 are repeated until the data entered are valid.

    Use case resumes at step 4.

* 3c. TuitiONE cannot find the student.
    * 3c1. TuitiONE requests CSO to enter a valid student.
    * 3c2. CSO enters new command.
      Steps 3c1-3c2 are repeated until the data entered are valid.

    Use case resumes at step 4.

* 3d. Student is not enrolled to lesson.
    * 3d1. TuitiONE informs that student is not enrolled to lesson.

    Use case ends.

**UC13: Review Commands**

**MSS**

1. CSO enters help.
2. TuitiONE provides the basic commands, as well as the user guide link.

    Use case ends.

### Non-Functional Requirements

1. Should work on any mainstream OS as long as it has Java 11 or above installed.
2. Should be able to hold up to 1000 students without a noticeable sluggishness in performance for typical usage.
    * Performance requirements: the system should respond within 2 seconds.
3. A user with above-average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4. Technical requirements: The system should work in both 32-bit and 64-bit environments.
5. Quality requirements:
    * User interface not produce excessive colour changes/flashing on command execution.
    * The user interface should use readable text styling, i.e. appropriate size and font.
    * All string output must be in UTF-8 encoding.

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, OS-X
* **Private contact detail**: A contact detail that is not meant to be shared with others
* **CSO**: Customer Service Officer
* **GUI**: Graphical User Interface

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

    1. Download the jar file and copy into an empty folder

    1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

    1. Resize the window to an optimum size. Move the window to a different location. Close the window.

    1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a student

1. Deleting a student while all students are being shown

    1. Prerequisites: List all students using the `list` command. Multiple "Students" in the list.

    1. Test case: `delete 1`<br>
       Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

    1. Test case: `delete 0`<br>
       Expected: No student is deleted. Error details shown in the status message. Status bar remains the same.

    1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
       Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

    1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
