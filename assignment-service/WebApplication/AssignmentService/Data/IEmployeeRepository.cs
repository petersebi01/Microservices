using System.Collections.Generic;
using AssignmentService.Models;

namespace AssignmentService.Data
{
    public interface IEmployeeRepository
    {
        bool SaveChanges();
        IEnumerable<Employee> GetAllEmployees();
        Employee GetEmployeeById(int id);
        void CreateEmployee(Employee employee);
        void UpdateEmployee(Employee employee);
        void DeleteEmployee(Employee employee);
    }
}