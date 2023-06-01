
import java.util.List;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class FriendFinder {
	
	protected ClassesDataSource classesDataSource;
	protected StudentsDataSource studentsDataSource;
	
	public FriendFinder(ClassesDataSource cds, StudentsDataSource sds) {
		classesDataSource = cds;
		studentsDataSource = sds;
	}
	
	/*
	 * This method takes a String representing the name of a student 
	 * and then returns a set containing the names of everyone else 
	 * who is taking the same classes as that student.
	 */
	public Set<String> findClassmates(Student theStudent) {
		
		if (theStudent == null) {
			throw new IllegalArgumentException("bad input");
		}
		String name = theStudent.getName();
		if (name == null) {
			throw new IllegalStateException("student name null");
		}
		
		// find the classes that this student is taking
		List<String> myClasses = classesDataSource.getClasses(name);
		if (myClasses == null || myClasses.isEmpty()) {
			return Collections.emptySet(); // return empty Set if the student isn't taking any classes
		}

		// use the classes to find the names of the students
		Set<String> classmates = new HashSet<String>();
		
		for (String myClass : myClasses) {
			
			if (myClass == null) {
				continue; // ignore if myClass is null
			}
			// list all the students in the class
			List<Student> students = studentsDataSource.getStudents(myClass);
			
			// if there is an inconsistency and a class that I am supposedly taking 
			// does not have any students, return an empty set
			if (students == null) {
				return Collections.emptySet();
			}
			for (Student otherStudent : students) {
				
				if (otherStudent == null) {
					continue; // ignore if otherStudent null
				}
				// find the other classes that they're taking
				List<String> theirClasses = classesDataSource.getClasses(otherStudent.getName());
				// if there is an inconsistency in the data and a student who supposedly 
				// participating in myClass is not taking any classes, we ignore this student
				if (theirClasses == null) {
					continue;
				}
							
				// see if all of the classes that they're taking are the same as the ones this student is taking
				boolean allSame = true;
				for (String c : myClasses) {
					if (c == null) {
						continue; // ignore if class is null
					}
					if (theirClasses.contains(c) == false) {
						allSame = false;
						break;
					}
				}
				
				// if they're taking all the same classes, then add to the set of classmates
				if (allSame) {
					if (otherStudent.getName().equals(name) == false) {
						classmates.add(otherStudent.getName());
					}
				}
			}

		}
				
		return classmates;
	}
	

}
