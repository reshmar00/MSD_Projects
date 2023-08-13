using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.Json;
using System.Threading.Tasks;
using System.Xml.Linq;
using LMS.Controllers;
using LMS.Models.LMSModels;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

// For more information on enabling MVC for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace LMS_CustomIdentity.Controllers
{
    [Authorize(Roles = "Professor")]
    public class ProfessorController : Controller
    {

        private readonly LMSContext db;

        public ProfessorController(LMSContext _db)
        {
            db = _db;
        }

        public IActionResult Index()
        {
            return View();
        }

        public IActionResult Students(string subject, string num, string season, string year)
        {
            ViewData["subject"] = subject;
            ViewData["num"] = num;
            ViewData["season"] = season;
            ViewData["year"] = year;
            return View();
        }

        public IActionResult Class(string subject, string num, string season, string year)
        {
            ViewData["subject"] = subject;
            ViewData["num"] = num;
            ViewData["season"] = season;
            ViewData["year"] = year;
            return View();
        }

        public IActionResult Categories(string subject, string num, string season, string year)
        {
            ViewData["subject"] = subject;
            ViewData["num"] = num;
            ViewData["season"] = season;
            ViewData["year"] = year;
            return View();
        }

        public IActionResult CatAssignments(string subject, string num, string season, string year, string cat)
        {
            ViewData["subject"] = subject;
            ViewData["num"] = num;
            ViewData["season"] = season;
            ViewData["year"] = year;
            ViewData["cat"] = cat;
            return View();
        }

        public IActionResult Assignment(string subject, string num, string season, string year, string cat, string aname)
        {
            ViewData["subject"] = subject;
            ViewData["num"] = num;
            ViewData["season"] = season;
            ViewData["year"] = year;
            ViewData["cat"] = cat;
            ViewData["aname"] = aname;
            return View();
        }

        public IActionResult Submissions(string subject, string num, string season, string year, string cat, string aname)
        {
            ViewData["subject"] = subject;
            ViewData["num"] = num;
            ViewData["season"] = season;
            ViewData["year"] = year;
            ViewData["cat"] = cat;
            ViewData["aname"] = aname;
            return View();
        }

        public IActionResult Grade(string subject, string num, string season, string year, string cat, string aname, string uid)
        {
            ViewData["subject"] = subject;
            ViewData["num"] = num;
            ViewData["season"] = season;
            ViewData["year"] = year;
            ViewData["cat"] = cat;
            ViewData["aname"] = aname;
            ViewData["uid"] = uid;
            return View();
        }

        /*******Begin code to modify********/


        /// <summary>
        /// Returns a JSON array of all the students in a class.
        /// Each object in the array should have the following fields:
        /// "fname" - first name
        /// "lname" - last name
        /// "uid" - user ID
        /// "dob" - date of birth
        /// "grade" - the student's grade in this class
        /// </summary>
        /// <param name="subject">The course subject abbreviation</param>
        /// <param name="num">The course number</param>
        /// <param name="season">The season part of the semester for the class the assignment belongs to</param>
        /// <param name="year">The year part of the semester for the class the assignment belongs to</param>
        /// <returns>The JSON array</returns>
        public IActionResult GetStudentsInClass(string subject, int num, string season, int year)
        {
            
            var students = from e in db.Enrolleds
                           where e.ClassNavigation.ListingNavigation.Department == subject &&
                                 e.ClassNavigation.ListingNavigation.Number == num &&
                                 e.ClassNavigation.Season == season &&
                                 e.ClassNavigation.Year == year
                           select new
                           {
                               fname = e.StudentNavigation.FName,
                               lname = e.StudentNavigation.LName,
                               uid = e.StudentNavigation.UId,
                               dob = e.StudentNavigation.Dob,
                               grade = e.Grade
                           };

            return Json(students);
        }



        /// <summary>
        /// Returns a JSON array with all the assignments in an assignment category for a class.
        /// If the "category" parameter is null, return all assignments in the class.
        /// Each object in the array should have the following fields:
        /// "aname" - The assignment name
        /// "cname" - The assignment category name.
        /// "due" - The due DateTime
        /// "submissions" - The number of submissions to the assignment
        /// </summary>
        /// <param name="subject">The course subject abbreviation</param>
        /// <param name="num">The course number</param>
        /// <param name="season">The season part of the semester for the class the assignment belongs to</param>
        /// <param name="year">The year part of the semester for the class the assignment belongs to</param>
        /// <param name="category">The name of the assignment category in the class, 
        /// or null to return assignments from all categories</param>
        /// <returns>The JSON array</returns>
        public IActionResult GetAssignmentsInCategory(string subject, int num, string season, int year, string category)
        {
            //var classes = db.Classes.Where(c => c.Listing == num &&
  

            var assignments = from a in db.Assignments
                              where a.CategoryNavigation.InClassNavigation.ListingNavigation.Department == subject &&
                                    a.CategoryNavigation.InClassNavigation.ListingNavigation.Number == num &&
                                    a.CategoryNavigation.InClassNavigation.Season == season &&
                                    a.CategoryNavigation.InClassNavigation.Year == year &&
                                    (category == null || a.CategoryNavigation.Name == category)
                              select new
                              {
                                  aname = a.Name,
                                  cname = a.CategoryNavigation.Name,
                                  due = a.Due,
                                  submissions = a.Submissions.Count()
                              };

            return Json(assignments);

        }


        /// <summary>
        /// Returns a JSON array of the assignment categories for a certain class.
        /// Each object in the array should have the folling fields:
        /// "name" - The category name
        /// "weight" - The category weight
        /// </summary>
        /// <param name="subject">The course subject abbreviation</param>
        /// <param name="num">The course number</param>
        /// <param name="season">The season part of the semester for the class the assignment belongs to</param>
        /// <param name="year">The year part of the semester for the class the assignment belongs to</param>
        /// <param name="category">The name of the assignment category in the class</param>
        /// <returns>The JSON array</returns>
        public IActionResult GetAssignmentCategories(string subject, int num, string season, int year)
        {

            var categories = from ac in db.AssignmentCategories
                             where ac.InClassNavigation.ListingNavigation.Department == subject &&
                                   ac.InClassNavigation.ListingNavigation.Number == num &&
                                   ac.InClassNavigation.Season == season &&
                                   ac.InClassNavigation.Year == year
                             select new
                             {
                                 name = ac.Name,
                                 weight = ac.Weight
                             };

            return Json(categories);

        }

        /// <summary>
        /// Creates a new assignment category for the specified class.
        /// If a category of the given class with the given name already exists, return success = false.
        /// </summary>
        /// <param name="subject">The course subject abbreviation</param>x`
        /// <param name="num">The course number</param>
        /// <param name="season">The season part of the semester for the class the assignment belongs to</param>
        /// <param name="year">The year part of the semester for the class the assignment belongs to</param>
        /// <param name="category">The new category name</param>
        /// <param name="catweight">The new category weight</param>
        /// <returns>A JSON object containing {success = true/false} </returns>
        public IActionResult CreateAssignmentCategory(string subject, int num, string season, int year, string category, int catweight)
        {
            var course = db.Courses.FirstOrDefault(c => c.Department == subject && c.Number == num);
            if (course == null)
            {
                return Json(new { success = false });
            }

            var classObj = db.Classes.FirstOrDefault(c =>
                c.Listing == course.CatalogId &&
                c.Season == season &&
                c.Year == year);

            if (classObj == null)
            {
                return Json(new { success = false });
            }

            var existingCategory = db.AssignmentCategories.FirstOrDefault(ac =>
                ac.InClassNavigation.Listing == course.CatalogId &&
                ac.InClassNavigation.Season == season &&
                ac.InClassNavigation.Year == year &&
                ac.Name == category);

            if (existingCategory != null)
            {
                return Json(new { success = false });
            }

            var newCategory = new AssignmentCategory
            {
                Name = category,
                Weight = (ushort)catweight,
                InClass = classObj.ClassId
            };

            db.AssignmentCategories.Add(newCategory);
            db.SaveChanges();

            return Json(new { success = true });
        }

        /// <summary>
        /// Creates a new assignment for the given class and category.
        /// </summary>
        /// <param name="subject">The course subject abbreviation</param>
        /// <param name="num">The course number</param>
        /// <param name="season">The season part of the semester for the class the assignment belongs to</param>
        /// <param name="year">The year part of the semester for the class the assignment belongs to</param>
        /// <param name="category">The name of the assignment category in the class</param>
        /// <param name="asgname">The new assignment name</param>
        /// <param name="asgpoints">The max point value for the new assignment</param>
        /// <param name="asgdue">The due DateTime for the new assignment</param>
        /// <param name="asgcontents">The contents of the new assignment</param>
        /// <returns>A JSON object containing success = true/false</returns>
        public IActionResult CreateAssignment(string subject, int num, string season, int year, string category, string asgname, int asgpoints, DateTime asgdue, string asgcontents)
        {
            var course = db.Courses.FirstOrDefault(c => c.Department == subject && c.Number == num);
            if (course == null)
            {
                return Json(new { success = false });
            }

            var classObj = db.Classes.FirstOrDefault(c =>
                c.Listing == course.CatalogId &&
                c.Season == season &&
                c.Year == year);

            if (classObj == null)
            {
                return Json(new { success = false });
            }

            var assignmentCategory = db.AssignmentCategories.FirstOrDefault(ac =>
                ac.InClass == classObj.ClassId &&
                ac.Name == category);

            if (assignmentCategory == null)
            {
                return Json(new { success = false });
            }

            var existingAssignment = db.Assignments.FirstOrDefault(a =>
                a.Category == assignmentCategory.CategoryId &&
                a.Name == asgname);

            if (existingAssignment != null)
            {
                return Json(new { success = false });
            }

            var newAssignment = new Assignment
            {
                Category = assignmentCategory.CategoryId,
                Name = asgname,
                Contents = asgcontents,
                MaxPoints = (ushort)asgpoints,
                Due = asgdue
            };

            db.Assignments.Add(newAssignment);
            db.SaveChanges();

            UpdateAllStudentGrades(classObj.ClassId);

            return Json(new { success = true });
        }

        public void UpdateAllStudentGrades(uint classId)
        {
            var enrollments = db.Enrolleds.Where(e => e.Class == classId).ToList();

            foreach (var enrollment in enrollments)
            {
                UpdateStudentGrade(enrollment.Student, classId);
            }
        }

        /// <summary>
        /// Gets a JSON array of all the submissions to a certain assignment.
        /// Each object in the array should have the following fields:
        /// "fname" - first name
        /// "lname" - last name
        /// "uid" - user ID
        /// "time" - DateTime of the submission
        /// "score" - The score given to the submission
        /// 
        /// </summary>
        /// <param name="subject">The course subject abbreviation</param>
        /// <param name="num">The course number</param>
        /// <param name="season">The season part of the semester for the class the assignment belongs to</param>
        /// <param name="year">The year part of the semester for the class the assignment belongs to</param>
        /// <param name="category">The name of the assignment category in the class</param>
        /// <param name="asgname">The name of the assignment</param>
        /// <returns>The JSON array</returns>
        public IActionResult GetSubmissionsToAssignment(string subject, int num, string season, int year, string category, string asgname)
        {
            var course = db.Courses.FirstOrDefault(c => c.Department == subject && c.Number == num);
            if (course == null)
            {
                return Json(null);
            }

            var classObj = db.Classes.FirstOrDefault(c =>
                c.Listing == course.CatalogId &&
                c.Season == season &&
                c.Year == year);

            if (classObj == null)
            {
                return Json(null);
            }

            var assignmentCategory = db.AssignmentCategories.FirstOrDefault(ac =>
                ac.InClass == classObj.ClassId &&
                ac.Name == category);

            if (assignmentCategory == null)
            {
                return Json(null);
            }

            var assignment = db.Assignments.FirstOrDefault(a =>
                a.Category == assignmentCategory.CategoryId &&
                a.Name == asgname);

            if (assignment == null)
            {
                return Json(null);
            }

            var submissions = db.Submissions
                .Where(s => s.Assignment == assignment.AssignmentId)
                .Select(s => new
                {
                    fname = s.StudentNavigation.FName,
                    lname = s.StudentNavigation.LName,
                    uid = s.StudentNavigation.UId,
                    time = s.Time,
                    score = s.Score
                })
                .ToList();

            return Json(submissions);

        }


        /// <summary>
        /// Set the score of an assignment submission
        /// </summary>
        /// <param name="subject">The course subject abbreviation</param>
        /// <param name="num">The course number</param>
        /// <param name="season">The season part of the semester for the class the assignment belongs to</param>
        /// <param name="year">The year part of the semester for the class the assignment belongs to</param>
        /// <param name="category">The name of the assignment category in the class</param>
        /// <param name="asgname">The name of the assignment</param>
        /// <param name="uid">The uid of the student who's submission is being graded</param>
        /// <param name="score">The new score for the submission</param>
        /// <returns>A JSON object containing success = true/false</returns>
        public IActionResult GradeSubmission(string subject, int num, string season, int year, string category, string asgname, string uid, int score)
        {

            var course = db.Courses.FirstOrDefault(c => c.Department == subject && c.Number == num);
            if (course == null)
            {
                return Json(new { success = false });
            }

            var classObj = db.Classes.FirstOrDefault(c =>
                c.Listing == course.CatalogId &&
                c.Season == season &&
                c.Year == year);

            if (classObj == null)
            {
                return Json(new { success = false });
            }

            var assignmentCategory = db.AssignmentCategories.FirstOrDefault(ac =>
                ac.InClass == classObj.ClassId &&
                ac.Name == category);

            if (assignmentCategory == null)
            {
                return Json(new { success = false });
            }

            var assignment = db.Assignments.FirstOrDefault(a =>
                a.Category == assignmentCategory.CategoryId &&
                a.Name == asgname);

            if (assignment == null)
            {
                return Json(new { success = false });
            }

            var submission = db.Submissions.FirstOrDefault(s =>
                s.Assignment == assignment.AssignmentId &&
                s.Student == uid);

            if (submission == null)
            {
                return Json(new { success = false });
            }

            submission.Score = (ushort)score;

            db.SaveChanges();

            // Update the student's grade for the class
            UpdateStudentGrade(uid, classObj.ClassId);

            return Json(new { success = true });
        }

        private void UpdateStudentGrade(string studentId, uint classId)
        {
            var enrollment = db.Enrolleds.FirstOrDefault(e =>
                e.Class == classId &&
                e.Student == studentId);

            if (enrollment != null)
            {
                var classObj = enrollment.ClassNavigation;

                // Get all assignment categories for the class
                var assignmentCategories = db.AssignmentCategories.Where(ac => ac.InClass == classObj.ClassId).ToList();

                double cumulativePoints = 0.0;
                double totalPoints = 0.0;

                foreach (var category in assignmentCategories)
                {
                    // Get all assignments for the category
                    var assignments = db.Assignments.Where(a => a.Category == category.CategoryId).ToList();
                    bool hasAssignments = assignments.Count() != 0;

                    // Calculate category total points earned and total max points
                    double categoryTotalPointsEarned = 0.0;
                    double categoryTotalMaxPoints = 0.0;

                    foreach (var assignment in assignments)
                    {

                        // Find the highest score for each assignment
                        double assignmentMaxPoints = assignment.MaxPoints;
                        categoryTotalMaxPoints += assignmentMaxPoints;

                        var submission = db.Submissions.SingleOrDefault(s => s.Assignment == assignment.AssignmentId && s.Student == studentId);
                        if (submission != null)
                        {
                            // Calculate the total points earned for this assignment
                            double assignmentTotalPoints = (double)submission.Score;
                            categoryTotalPointsEarned += assignmentTotalPoints;
                            Console.WriteLine($"assignmentMaxPoints: {assignmentMaxPoints}, assignmentTotalPoints: {assignmentTotalPoints}");

                        }
                    }

                    if (hasAssignments)
                    {
                        // Calculate the category percentage
                        double categoryPercentage = categoryTotalMaxPoints > 0 ? categoryTotalPointsEarned / categoryTotalMaxPoints : 0.0;

                        // Scale the category percentage by its weight
                        double scaledCategoryTotal = categoryPercentage * category.Weight;

                        // Add the scaled category total to cumulativePoints and totalPoints
                        cumulativePoints += scaledCategoryTotal;
                        totalPoints += category.Weight;
                    }
                }

                Console.WriteLine("UpdateStudentGrade calculate GPA");
                Console.WriteLine($"cumulativePoints: {cumulativePoints}, totalPoints: {totalPoints}");

                string letterGrade = GradeConverter.PercentageToGradePoint(cumulativePoints, totalPoints);

                enrollment.Grade = letterGrade;

                db.SaveChanges();
            }
        }

        /// <summary>
        /// Returns a JSON array of the classes taught by the specified professor
        /// Each object in the array should have the following fields:
        /// "subject" - The subject abbreviation of the class (such as "CS")
        /// "number" - The course number (such as 5530)
        /// "name" - The course name
        /// "season" - The season part of the semester in which the class is taught
        /// "year" - The year part of the semester in which the class is taught
        /// </summary>
        /// <param name="uid">The professor's uid</param>
        /// <returns>The JSON array</returns>
        public IActionResult GetMyClasses(string uid)
        {
            var professor = db.Professors.FirstOrDefault(p => p.UId == uid);
            if (professor == null)
            {
                return Json(new List<object>());
            }

            var classes = db.Classes.Where(c => c.TaughtBy == uid)
                .Select(c => new
                {
                    subject = c.ListingNavigation.Department,
                    number = c.ListingNavigation.Number,
                    name = c.ListingNavigation.Name,
                    season = c.Season,
                    year = c.Year
                }).ToList();

            return Json(classes);
        }


        /*******End code to modify********/
    }
}

