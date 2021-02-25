using System.Collections.Generic;
using AssignmentService.Data;
using AssignmentService.Dto;
using AutoMapper;
using Microsoft.AspNetCore.Mvc;

namespace AssignmentService.Controllers
{
    [Route("api/employees")]
    [ApiController]
    public class EmployeeResource : ControllerBase
    {
        private readonly IEmployeeRepository _repository;
        private readonly IMapper _mapper;

        public EmployeeResource(IEmployeeRepository employeeRepository, IMapper mapper)
        {
            _repository = employeeRepository;
            _mapper = mapper;
        }

        [HttpGet]
        public ActionResult<IEnumerable<EmployeeDto>> findAllTasks()
        {
            var tasks = _repository.GetAllEmployees();
            return Ok(_mapper.Map<IEnumerable<TaskDto>>(tasks));
        }

        [HttpGet("{id}", Name = "findEmployeeById")]
        public ActionResult<EmployeeDto> findTaskById(int id)
        {
            var task = _repository.GetEmployeeById(id);
            if (task != null)
            {
                return Ok(_mapper.Map<TaskDto>(task)); 
            }

            return NotFound();
        }
    }
}