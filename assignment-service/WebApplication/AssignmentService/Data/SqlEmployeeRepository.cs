using System;
using System.Collections.Generic;
using System.Linq;
using AssignmentService.Models;

namespace AssignmentService.Data
{
    public class SqlEmployeeRepository : IEmployeeRepository
    {
        private readonly AssignmentServiceDbContext _employeeContext;

        public SqlEmployeeRepository(AssignmentServiceDbContext employeeContext)
        {
            _employeeContext = employeeContext;
        }
        
        public bool SaveChanges()
        {
            return (_employeeContext.SaveChanges() >= 0);
        }

        public IEnumerable<Employee> GetAllEmployees()
        {
            return _employeeContext.Employees.ToList();
        }

        public Employee GetEmployeeById(int id)
        {
            return _employeeContext.Employees.FirstOrDefault(p => p.EmployeeId == id);
        }

        public void CreateEmployee(Employee employee)
        {
            if (employee == null)
            {
                throw new ArgumentNullException(nameof(employee));
            }

            _employeeContext.Employees.Add(employee);
        }

        public void UpdateEmployee(Employee employee)
        {
            throw new System.NotImplementedException();
        }

        public void DeleteEmployee(Employee employee)
        {
            if (employee == null)
            {
                throw new ArgumentNullException();
            }

            _employeeContext.Employees.Remove(employee);
        }
    }
}