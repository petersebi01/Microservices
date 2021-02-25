using System.Collections.Generic;
using AssignmentService.Models;

namespace AssignmentService.Data
{
    public interface IAssignmentRepository
    {
        bool SaveChanges();
        IEnumerable<Assignment> GetAllAssignments();
        Assignment GetAssignmentById(int id);
        IEnumerable<Assignment> GetEmployeesOnAssignment(int id);
        void CreateAssignment(Assignment assignment);
        void UpdateAssignment(Assignment assignment);
        void DeleteAssignment(Assignment assignment);
    }
}