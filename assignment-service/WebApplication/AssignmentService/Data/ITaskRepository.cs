using System.Collections.Generic;
using AssignmentService.Models;

namespace AssignmentService.Data
{
    public interface ITaskRepository
    {
        bool SaveChanges();
        IEnumerable<Task> GetAllTasks();
        Task GetTaskById(int id);
        void CreateTask(Task task);
        void UpdateTask(Task task);
        void DeleteTask(Task task);
    }
}