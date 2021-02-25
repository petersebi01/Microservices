using System;
using System.Collections.Generic;
using System.Linq;
using AssignmentService.Models;
using Microsoft.EntityFrameworkCore;

namespace AssignmentService.Data
{
    public class SqlAssignmentRepository : IAssignmentRepository
    {
        private readonly AssignmentServiceDbContext _assignmentContext;

        public SqlAssignmentRepository(AssignmentServiceDbContext assignmentContext)
        {
            _assignmentContext = assignmentContext;
        }
        
        public bool SaveChanges()
        {
            return (_assignmentContext.SaveChanges() >= 0);
        }

        public IEnumerable<Assignment> GetAllAssignments()
        {
            return _assignmentContext.Assignments.ToList();
        }

        public Assignment GetAssignmentById(int id)
        {
            return _assignmentContext.Assignments.FirstOrDefault(p => p.AssignmentId == id);
        }

        public IEnumerable<Assignment> GetEmployeesOnAssignment(int id)
        {
            return _assignmentContext.Assignments.Where(a => a.AssignmentId == id).Include(s => s.Employees).ToList();
        }

        public void CreateAssignment(Assignment assignment)
        {
            if (assignment == null)
            {
                throw new ArgumentNullException(nameof(assignment));
            }

            _assignmentContext.Assignments.Add(assignment);
        }

        public void UpdateAssignment(Assignment assignment)
        {
            //Nothing
        }

        public void DeleteAssignment(Assignment assignment)
        {
            if (assignment == null)
            {
                throw new ArgumentNullException();
            }

            _assignmentContext.Assignments.Remove(assignment);
        }
    }
}