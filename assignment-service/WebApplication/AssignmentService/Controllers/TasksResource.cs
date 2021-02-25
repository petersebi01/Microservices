using System.Collections.Generic;
using AssignmentService.Data;
using AssignmentService.Dto;
using AssignmentService.Models;
using AutoMapper;
using Microsoft.AspNetCore.JsonPatch;
using Microsoft.AspNetCore.Mvc;

namespace AssignmentService.Controllers
{
    [Route("api/tasks")]
    [ApiController]
    public class TasksResource : ControllerBase
    {
        private readonly ITaskRepository _repository;
        private readonly IMapper _mapper;

        public TasksResource(ITaskRepository taskRepository, IMapper mapper)
        {
            _repository = taskRepository;
            _mapper = mapper;
        }

        [HttpGet]
        public ActionResult<IEnumerable<TaskDto>> FindAllTasks()
        {
            var tasks = _repository.GetAllTasks();
            return Ok(_mapper.Map<IEnumerable<TaskDto>>(tasks));
        }

        [HttpGet("{id}", Name = "findTaskById")]
        public ActionResult<TaskDto> FindTaskById(int id)
        {
            var task = _repository.GetTaskById(id);
            if (task != null)
            {
                return Ok(_mapper.Map<TaskDto>(task)); 
            }

            return NotFound();
        }

        [HttpPost("{id}")]
        public ActionResult<TaskDto> CreateTask(TaskDto taskDto)
        {
            var taskToCreate = _mapper.Map<Task>(taskDto);
            _repository.CreateTask(taskToCreate);
            _repository.SaveChanges();

            // sync communication here with user service
            var createdTask = _mapper.Map<TaskDto>(taskToCreate);
            return CreatedAtRoute(nameof(FindTaskById), new {Id = createdTask.TaskId}, taskDto);
        }
        
        [HttpPatch("{id}")]
        public ActionResult UpdateTask(int id, JsonPatchDocument<TaskDto> patchDocument)
        {
            var taskModelFromRepo = _repository.GetTaskById(id);
            if (taskModelFromRepo == null)
            {
                return NotFound();
            }

            var taskToPatch = _mapper.Map<TaskDto>(taskModelFromRepo);
            patchDocument.ApplyTo(taskToPatch, ModelState);

            if (!TryValidateModel(taskToPatch))
            {
                return ValidationProblem(ModelState);
            }

            _mapper.Map(taskToPatch, taskModelFromRepo);
            _repository.UpdateTask(taskModelFromRepo);
            _repository.SaveChanges();

            return NoContent();
        }
        
        [HttpDelete("{id}")]
        public ActionResult DeleteTask(int id)
        {
            var taskModelFromRepo = _repository.GetTaskById(id);
            if (taskModelFromRepo == null)
            {
                return NotFound();
            }
            
            _repository.DeleteTask(taskModelFromRepo);
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