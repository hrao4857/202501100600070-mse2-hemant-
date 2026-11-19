# Student Profile and Skill Development Management System

Spring Boot + MySQL solution for the uploaded CS336B MSE-II question paper.

## Requirements
- JDK 17 or later
- MySQL Server 8+
- Eclipse IDE (or IntelliJ)
- Maven (Eclipse m2e can import the project)

## 1. Create database
Run `database/create_database.sql` in MySQL Workbench or the MySQL command line.

## 2. Configure MySQL password
Open `src/main/resources/application.properties`. The default username/password are `root` / `root`.
Change the password to your MySQL password, or set environment variables:
- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`

## 3. Run in Eclipse
1. Extract the ZIP.
2. Open Eclipse -> File -> Import -> Existing Maven Projects.
3. Select the extracted `Student_Profile_Skill_Management` folder.
4. Wait for Maven dependencies to download.
5. Open `StudentProfileApplication.java`.
6. Right-click -> Run As -> Java Application (or Spring Boot App).
7. Server runs at `http://localhost:8081`.

## API endpoints (Q3)

Base URL: `http://localhost:8081/api/students`

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/students` | Add student |
| GET | `/api/students` | Retrieve all |
| GET | `/api/students/{id}` | Search by student ID |
| GET | `/api/students/search?email=a@b.com` | Search by email |
| GET | `/api/students/search?course=BCA` | Search by course |
| PUT | `/api/students/{id}/scores-skills` | Update scores and skills |
| DELETE | `/api/students/{id}` | Delete student |
| GET | `/api/students/merit-list` | Merit list by average descending |
| GET | `/api/students/same-skills` | Students having identical skill sets |
| GET | `/api/students/programming-needs-improvement` | Programming below 50 and Maths >= 75 |

## Example JSON: add student
Send this as the body of a POST request to `/api/students`:

```json
{
  "studentId": "S101",
  "studentName": "Aman Kumar",
  "email": "aman@example.com",
  "courseName": "BCA",
  "programmingScore": 88,
  "mathematicsScore": 91,
  "technicalSkills": ["Java", "MySQL", "HTML"]
}
```

The API response includes `averageScore`, `performanceCategory`, and `strongerSubject` as calculated properties.

## Example JSON: update scores and skills
PUT `/api/students/S101/scores-skills`

```json
{
  "programmingScore": 76,
  "mathematicsScore": 82,
  "technicalSkills": ["Java", "Spring Boot", "MySQL"]
}
```

## Q1: Student class
`model/Student.java` contains all seven requested attributes, encapsulation, no-argument and parameterized constructors, getters, setters, and calculated object methods.

## Q2: Database and business logic
- MySQL persistence: `StudentRepository` and JPA entity mapping.
- Average score: `(programmingScore + mathematicsScore) / 2`.
- Category: Excellent (>=85), Good (>=70), Satisfactory (>=50), Needs Improvement (<50).
- Stronger subject: compares programming and mathematics scores.
- Search by ID/email/course: repository and service search methods.
- Merit list: Java `ArrayList` sorted with `Comparator` by average descending.
- Same skills: Java `Map` groups students by normalized skill list.
- Programming improvement list: programming < 50 and mathematics >= 75.
- Duplicate ID: primary key + explicit conflict check.
- Score validation: Bean Validation limits scores to 0–100.

## Q3: REST API
All six required endpoints are implemented in `StudentController`.

## Notes for the exam
- If your MySQL password is not `root`, update `application.properties` before running.
- For POST/PUT requests use Postman, Thunder Client, or curl with `Content-Type: application/json`.
- `ddl-auto=update` creates/updates the tables automatically. Do not use `create` if you want to preserve existing records.

## Three AI prompts and brief reflections (submission support)
1. Prompt: "Create a Java Student class with encapsulation, constructors, getters, setters, and a list of technical skills."
   Reflection: The generated class helped organize student data and demonstrated encapsulation. I checked that every required field was present.
2. Prompt: "Implement student score business rules: average, performance category, stronger subject, merit list, and skill grouping."
   Reflection: The logic showed how methods and Java collections can implement the requirements. I verified category boundaries and descending merit order.
3. Prompt: "Build Spring Boot REST APIs with MySQL persistence to add, retrieve, update, delete, and list students by merit."
   Reflection: The controller-service-repository structure separated HTTP requests, business rules, and database operations. I tested each endpoint with sample JSON.

## Viva preparation: collections used
- `List<String>` stores technical skills.
- `ArrayList<Student>` stores a sortable in-memory copy for the merit list.
- `Comparator<Student>` sorts by average score descending.
- `Map<String, List<String>>` groups student IDs that share the same skill set.
