using System;
using System.Collections.Generic;
using System.Net.Http;
using AssignmentService.Data;
using AssignmentService.Dto;
using AssignmentService.Models;
using AutoMapper;
using Microsoft.AspNetCore.JsonPatch;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;

namespace AssignmentService.Controllers
{
    [Route("api/assignments")]
    [ApiController]
    public class AssignmentsResource : ControllerBase
    {
        private readonly IAssignmentRepository _repository;
        private readonly IEmployeeRepository _employeeRepository;
        private readonly IMapper _mapper;

        public AssignmentsResource(IAssignmentRepository assignmentRepository, IMapper mapper)
        {
            _repository = assignmentRepository;
            _mapper = mapper;
        }

        [HttpGet]
        public ActionResult<IEnumerable<AssignmentDto>> findAllAssignment()
        {
            var assignments = _repository.GetAllAssignments();
            return Ok(_mapper.Map<IEnumerable<AssignmentDto>>(assignments));
        }

        [HttpGet("{id}", Name = "findAssignmentById")]
        public ActionResult<AssignmentDto> findAssignmentById(int id)
        {
            var assignment = _repository.GetAssignmentById(id);
            if (assignment != null)
            {
                return Ok(_mapper.Map<AssignmentDto>(assignment)); 
            }

            return NotFound();
        }

        [HttpGet("{id}/employees", Name = "findEmployeesOnAssignments")]
        public ActionResult<IEnumerable<AssignmentDto>> findEmployeesOnAssignment(int id)
        {
            var assignments = _repository.GetEmployeesOnAssignment(id);
            return Ok(_mapper.Map<IEnumerable<AssignmentDto>>(assignments));
        }
        
        [HttpPost("{id}/employees", Name = "addEmployeeToAssignment")]
        public ActionResult<IEnumerable<AssignmentDto>> addEmployeeToAssignment([FromQuery(Name = "nickname")]string nickname, int id, JsonPatchDocument<AssignmentDto> patchDocument)
        {
            var assignmentModelFromRepo = _repository.GetAssignmentById(id);
            if (assignmentModelFromRepo == null)
            {
                return NotFound();
            }

            var assignmentToPatch = _mapper.Map<AssignmentDto>(assignmentModelFromRepo);
            patchDocument.ApplyTo(assignmentToPatch, ModelState);

            if (!TryValidateModel(assignmentToPatch))
            {
                return ValidationProblem(ModelState);
            }

            _mapper.Map(assignmentToPatch, assignmentModelFromRepo);
            _repository.UpdateAssignment(assignmentModelFromRepo);
            _repository.SaveChanges();
            
            
            string url = "http://localhost:8080/api/users/" + nickname;

            HttpClient httpClient = new HttpClient();

            var response = httpClient.GetAsync(url).Result;

            if (response.IsSuccessStatusCode)
            {
                Employee employee =  JsonConvert.DeserializeObject<Employee>(response.Content.ReadAsStringAsync().Result);
                
                Console.Write(employee);
                
                _employeeRepository.CreateEmployee(employee);
                _employeeRepository.SaveChanges();
            }

            return NoContent();
        }

        [HttpPost("{id}")]
        public ActionResult<AssignmentDto> createAssignment(AssignmentDto assignmentDto)
        {
            var assignmentToCreate = _mapper.Map<Assignment>(assignmentDto);
            
            _repository.CreateAssignment(assignmentToCreate);
            _repository.SaveChanges();

            var createdAssignment = _mapper.Map<AssignmentDto>(assignmentToCreate);
            return CreatedAtRoute(nameof(findAssignmentById), new {Id = createdAssignment.AssignmentId}, assignmentDto);
        }

        [HttpPatch("{id}")]
        public ActionResult UpdateAssignment(int id, JsonPatchDocument<AssignmentDto> patchDocument)
        {
            var assignmentModelFromRepo = _repository.GetAssignmentById(id);
            if (assignmentModelFromRepo == null)
            {
                return NotFound();
            }

            var assignmentToPatch = _mapper.Map<AssignmentDto>(assignmentModelFromRepo);
            patchDocument.ApplyTo(assignmentToPatch, ModelState);

            if (!TryValidateModel(assignmentToPatch))
            {
                return ValidationProblem(ModelState);
            }

            _mapper.Map(assignmentToPatch, assignmentModelFromRepo);
            _repository.UpdateAssignment(assignmentModelFromRepo);
            _repository.SaveChanges();

            return NoContent();
        }
        
        [HttpDelete("{id}")]
        public ActionResult DeleteAssignment(int id)
        {
            var assignmentModelFromRepo = _repository.GetAssignmentById(id);
            if (assignmentModelFromRepo == null)
            {
                return NotFound();
            }
            
            _repository.DeleteAssignment(assignmentModelFromRepo);
            _repository.SaveChanges();

            return NoContent();
        }
        
        // PUT api/commands/:id
        /*[HttpPut("{id}")]
        public ActionResult UpdateCommand(int id, CommandUpdateDto commandUpdateDto)
        {
            var commandModelFromRepo = _repository.GetCommandById(id);
            if (commandModelFromRepo == null)
            {
                return NotFound();
            }

            _mapper.Map(commandUpdateDto, commandModelFromRepo);
            _repository.UpdateCommand(commandModelFromRepo);
            _repository.SaveChanges();

            return NoContent();
        }*/
    }
}