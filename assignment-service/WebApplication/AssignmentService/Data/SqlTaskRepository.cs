using System;
using System.Collections.Generic;
using System.Linq;
using AssignmentService.Models;

namespace AssignmentService.Data
{
    public class SqlTaskRepository : ITaskRepository
    {
        private readonly AssignmentServiceDbContext _taskContext;
        
        public SqlTaskRepository(AssignmentServiceDbContext taskContext)
        {
            _taskContext = taskContext;
        }
        
        public bool SaveChanges()
        {
            return (_taskContext.SaveChanges() >= 0);
        }

        public IEnumerable<Task> GetAllTasks()
        {
            return _taskContext.Tasks.ToList();
        }

        public Task GetTaskById(int id)
        {
            return _taskContext.Tasks.FirstOrDefault(p => p.TaskId == id);
        }

        public void CreateTask(Task task)
        {
            if (task == null)
            {
                throw new ArgumentNullException(nameof(task));
            }

            _taskContext.Tasks.Add(task);
        }

        public void UpdateTask(Task task)
        {
            throw new System.NotImplementedException();
        }

        public void DeleteTask(Task task)
        {
            if (task == null)
            {
                throw new ArgumentNullException();
            }

            _taskContext.Tasks.Remove(task);
        }
    }
}