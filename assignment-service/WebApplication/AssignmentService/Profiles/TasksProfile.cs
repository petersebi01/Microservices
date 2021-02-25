using AssignmentService.Dto;
using AssignmentService.Models;
using AutoMapper;

namespace AssignmentService.Profiles
{
    public class TasksProfile : Profile
    {
        public TasksProfile()
        {
            //Source -> Target
            CreateMap<Task, TaskDto>(); //TaskReadDto
            CreateMap<TaskDto, Task>(); //TaskCreateDto
            //CreateMap<TaskUpdateDto, Task>();
            //CreateMap<Task, TaskUpdateDto>();
        }
    }
}