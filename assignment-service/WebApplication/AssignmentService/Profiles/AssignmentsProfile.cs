using AssignmentService.Dto;
using AssignmentService.Models;
using AutoMapper;

namespace AssignmentService.Profiles
{
    public class AssignmentsProfile : Profile
    {
        public AssignmentsProfile()
        {
            //Source -> Target
            CreateMap<Assignment, AssignmentDto>(); //AssignmentReadDto
            CreateMap<AssignmentDto, Assignment>(); //AssignmentCreateDto
            //CreateMap<AssignmentUpdateDto, Assignment>();
            //CreateMap<Assignment, AssignmentUpdateDto>();
            
        }
    }
}