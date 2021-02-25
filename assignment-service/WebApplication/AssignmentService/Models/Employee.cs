using System.Collections;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;

namespace AssignmentService.Models
{
    public class Employee
    {
        [Key]
        public int EmployeeId { get; set; }

        [Required]
        public string Firstname { get; set; }
        
        [Required]
        public string Lastname { get; set; }

        public Assignment Assignment { get; set; }
    }
}