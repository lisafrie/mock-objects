import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;


/*
 * This class includes test cases for the basic/normal functionality of the 
 * FriendFinder.findClassmates method, but does not check for any error handling.
 */

public class FindClassmatesTest {
	
	protected FriendFinder ff;
		
	protected ClassesDataSource defaultClassesDataSource = new ClassesDataSource() {

		@Override
		public List<String> getClasses(String studentName) {

			if (studentName.equals("A")) {
				return List.of("1", "2", "3");
			}
			else if (studentName.equals("B")) {
				return List.of("1", "2", "3");
			}
			else if (studentName.equals("C")) {
				return List.of("2", "4");
			}
			else return null;			
		
		}
		
	};
	
	protected StudentsDataSource defaultStudentsDataSource = new StudentsDataSource() {

		@Override
		public List<Student> getStudents(String className) {
			
			Student a = new Student("A", 101);
			Student b = new Student("B", 102);
			Student c = new Student("C", 103);

			if (className.equals("1")) {
				return List.of(a, b);
			}
			else if (className.equals("2")) {
				return List.of(a, b, c);
			}
			else if (className.equals("3")) {
				return List.of(a, b);
			}
			else if (className.equals("4")) {
				return List.of(c);
			}
			else return null;
		}
		
	};
	

	@Test
	public void testFindOneFriend() { 
		
		ff = new FriendFinder(defaultClassesDataSource, defaultStudentsDataSource);
		Set<String> response = ff.findClassmates(new Student("A", 101));
		assertNotNull(response);
		assertEquals(1, response.size());
		assertTrue(response.contains("B"));

	}

	@Test
	public void testFindNoFriends() { 
		
		ff = new FriendFinder(defaultClassesDataSource, defaultStudentsDataSource);
		Set<String> response = ff.findClassmates(new Student("C", 103));
		assertNotNull(response);
		assertTrue(response.isEmpty());

	}
	
	@Test
	public void testClassesDataSourceReturnsNullForInputStudent() { 
		
		ff = new FriendFinder(defaultClassesDataSource, defaultStudentsDataSource);
		Set<String> response = ff.findClassmates(new Student("D", 104));
		assertNotNull(response);
		assertTrue(response.isEmpty());

	}

	@Test
	public void testStudentNotTakingClasses() { 
		
		ff = new FriendFinder(new ClassesDataSource() {

			@Override
			public List<String> getClasses(String studentName) {
	
				if (studentName.equals("A")) {
					return Collections.emptyList();
				}
				else if (studentName.equals("B")) {
					return List.of("1", "2");
				}
				else if (studentName.equals("C")) {
					return List.of("2", "3", "4");
				}
				else return null;			
			
			}
			
		}, new StudentsDataSource() {
			
			@Override
			public List<Student> getStudents(String className) {
				Student b = new Student("B", 102);
				Student c = new Student("C", 103);
	
				if (className.equals("1")) {
					return List.of(b);
				}
				else if (className.equals("2")) {
					return List.of(b, c);
				}
				else if (className.equals("3")) {
					return List.of(c);
				}
				else if (className.equals("4")) {
					return List.of(c);
				}
				else return null;
			}
		});
		Set<String> response = ff.findClassmates(new Student("A", 101));
		assertNotNull(response);
		assertTrue(response.isEmpty());
	}

	@Test
	public void testStudentAloneInClasses() { 
		
		ff = new FriendFinder(new ClassesDataSource() {

			@Override
			public List<String> getClasses(String studentName) {
	
				if (studentName.equals("A")) {
					return List.of("1", "2");
				}
				else if (studentName.equals("B")) {
					return List.of("1", "2");
				}
				else if (studentName.equals("C")) {
					return List.of("3", "4");
				}
				else return null;			
			
			}
			
		}, new StudentsDataSource() {
			
			@Override
			public List<Student> getStudents(String className) {
				Student a = new Student("A", 101);
				Student b = new Student("B", 102);
				Student c = new Student("C", 103);
	
				if (className.equals("1")) {
					return List.of(a, b);
				}
				else if (className.equals("2")) {
					return List.of(a, b);
				}
				else if (className.equals("3")) {
					return List.of(c);
				}
				else if (className.equals("4")) {
					return List.of(c);
				}
				else return null;
			}
		});
		Set<String> response = ff.findClassmates(new Student("C", 103));
		assertNotNull(response);
		assertTrue(response.isEmpty());
	}

	@Test(expected=IllegalArgumentException.class)
	public void testStudentInputNull() { 
		
		ff = new FriendFinder(defaultClassesDataSource, defaultStudentsDataSource);
		ff.findClassmates(null);
	}

	@Test(expected=IllegalStateException.class)
	public void testStudentNameNull() { 
		
		ff = new FriendFinder(defaultClassesDataSource, defaultStudentsDataSource);
		ff.findClassmates(new Student(null, 0));
	}

	@Test
	public void testStudentsDataSourceReturnsNullForClassTaken() {
		ff = new FriendFinder(defaultClassesDataSource, new StudentsDataSource() {

			@Override
			public List<Student> getStudents(String className) {
				
				Student a = new Student("A", 101);
				Student b = new Student("B", 102);
				Student c = new Student("C", 103);
	
				if (className.equals("1")) {
					return List.of(a, b);
				}
				else if (className.equals("2")) {
					return null;
				}
				else if (className.equals("3")) {
					return List.of(a, b);
				}
				else if (className.equals("4")) {
					return List.of(c);
				}
				else return null;
			}
			
		});

		Set<String> response = ff.findClassmates(new Student("A", 101));
		assertNotNull(response);
		assertTrue(response.isEmpty());
	}

	@Test
	public void testClassesDataSourceReturnsNullForClassMate() {
		ff = new FriendFinder(new ClassesDataSource() {

			@Override
			public List<String> getClasses(String studentName) {
	
				if (studentName.equals("A")) {
					return List.of("1", "2", "3");
				}
				else if (studentName.equals("B")) {
					return null;
				}
				else if (studentName.equals("C")) {
					return List.of("1", "2", "3");
				}
				else return null;			
			
			}
			
		}, new StudentsDataSource() {

			@Override
			public List<Student> getStudents(String className) {
				
				Student a = new Student("A", 101);
				Student b = new Student("B", 102);
				Student c = new Student("C", 103);

				if (className.equals("1")) {
					return List.of(a, b, c);
				}
				else if (className.equals("2")) {
					return List.of(a, b, c);
				}
				else if (className.equals("3")) {
					return List.of(a, b, c);
				}
				else return null;
			}
		});

		Set<String> response = ff.findClassmates(new Student("A", 101));
		assertNotNull(response);
		assertEquals(1, response.size());
		assertTrue(response.contains("C"));
	}

	@Test
	public void testOneClassNull() {
		ff = new FriendFinder(new ClassesDataSource() {

			@Override
			public List<String> getClasses(String studentName) {
	
				if (studentName.equals("A")) {
					List<String> classes = List.of("1", "2");
					classes = new ArrayList<>(classes);
					classes.add(null);
					return classes;
				}
				else if (studentName.equals("B")) {
					return List.of("1", "2");
				}
				else if (studentName.equals("C")) {
					return List.of("2", "3", "4");
				}
				else return null;			
			
			}
			
		}, new StudentsDataSource() {
			
			@Override
			public List<Student> getStudents(String className) {
				Student a = new Student("A", 101);
				Student b = new Student("B", 102);
				Student c = new Student("C", 103);
	
				if (className.equals("1")) {
					return List.of(a, b);
				}
				else if (className.equals("2")) {
					return List.of(a, b, c);
				}
				else if (className.equals("3")) {
					return List.of(c);
				}
				else if (className.equals("4")) {
					return List.of(c);
				}
				else return null;
			}
		});

		Set<String> response = ff.findClassmates(new Student("A", 101));
		assertNotNull(response);
		assertEquals(1, response.size());
		assertTrue(response.contains("B"));
	}

	@Test
	public void testOneStudentNull() {
		ff = new FriendFinder(new ClassesDataSource() {

			@Override
			public List<String> getClasses(String studentName) {
	
				if (studentName.equals("A")) {
					return List.of("1", "2");
				}
				else if (studentName.equals("B")) {
					return List.of("1", "2");
				}
				else if (studentName.equals("C")) {
					return List.of("2", "3", "4");
				}
				else return null;			
			
			}
			
		}, new StudentsDataSource() {
			
			@Override
			public List<Student> getStudents(String className) {
				Student a = new Student("A", 101);
				Student b = new Student("B", 102);
				Student c = new Student("C", 103);
	
				if (className.equals("1")) {
					List<Student> students = List.of(a, b);
					students = new ArrayList<>(students);
					students.add(null);
					return students;
				}
				else if (className.equals("2")) {
					return List.of(a, b, c);
				}
				else if (className.equals("3")) {
					return List.of(c);
				}
				else if (className.equals("4")) {
					return List.of(c);
				}
				else return null;
			}
		});

		Set<String> response = ff.findClassmates(new Student("A", 101));
		assertNotNull(response);
		assertEquals(1, response.size());
		assertTrue(response.contains("B"));
	}
}
