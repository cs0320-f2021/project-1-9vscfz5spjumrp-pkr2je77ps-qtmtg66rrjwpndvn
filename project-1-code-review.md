##Objective Problems
1. KDTree's classify command not implemented
2. REPL doesn't properly handle users being loaded to database
3. REPL doesn't properly handle similar command
4. REPL doesn't handle classify command (see 1)
5. Don't handle the exceptions that can arise: NoSuchField, IllegalAccess, 

##Systems Test Coverage
While we have the basic systems tests as given in the handout, we don't handle 
any of the "error" cases. Some tests that should be written:
1. Trying to populate ORM from non-sql file 
2. database path/to/database.sqlite3 <other word> should fail
3. KDTree's similar command with the wrong number of arguments
4. KDTree's similar command with a unique ID that doesn't exist
5. KDTree's classify command with the wrong number of arguments
6. KDTree's classify command with a unique ID that doesn't exist
7. KDTree testing on field that are not sortable 
8. KDTree testing on wrong fields (not age, weight, height)

##Unit Test Coverage
While we have some basic unit tests,  we don't handle
any of the "error" cases.
1. KDTree doesn't test for any of the thrown exceptions
2. ORM doesn't test for any of the thrown exceptions
3. KDTree testing for wrong fields 
4. KDTree null exceptions
5. KDTree 0 (or negative) dimensions
6. KDTree and ORM for NoSuchField Exception

##Subjective Code Decisions
1. Some of the class names weren't ideal: DataManager, ClassInfoUtil, plural Users and Reviews.
I think that we should have done Users ---> User and Reviews ---> Review. DataManager could've
been something along the lines of ORMManager. Unsure what to do about ClassInfoUtil
2. I think that both the KDTree and the ORM are well commented. The inline comments are helpful and the 
top-level comments make functionality clear. 
3. The code is definitely not safe from bugs. Exceptions aren't really checked for or handled.
4. The code IS extensible. The KDTree can take in any type of object and sort it on any type of axis (including strings).
The ORM can also store any type of object. Users aren't able to add their own REPL commands, but that's coming up in this project.
5. The code is NOT effective at addressing the user stories. It actually can't handle the E2E on any of them.
6. Not sure why we have a REPL and a REPL Main class. I think this was for testing purposes. 
