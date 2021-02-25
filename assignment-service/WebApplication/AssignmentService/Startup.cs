using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using AssignmentService.Data;
using AutoMapper;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.HttpsPolicy;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;
using Newtonsoft.Json.Serialization;

namespace AssignmentService
{
    public class Startup
    {
        public Startup(IConfiguration configuration)
        {
            Configuration = configuration;
        }

        private IConfiguration Configuration { get; }

        // This method gets called by the runtime. Use this method to add services to the container.
        public void ConfigureServices(IServiceCollection services)
        {
            services.AddControllers().AddNewtonsoftJson(s => 
                s.SerializerSettings.ContractResolver = new CamelCasePropertyNamesContractResolver());
            services.AddAutoMapper(AppDomain.CurrentDomain.GetAssemblies());
            services.AddDbContext<AssignmentServiceDbContext>(opt =>
                opt.UseSqlServer(Configuration.GetConnectionString("Connection")));
            services.AddScoped<ITaskRepository, SqlTaskRepository>();
            services.AddScoped<IEmployeeRepository, SqlEmployeeRepository>();
            services.AddScoped<IAssignmentRepository, SqlAssignmentRepository>();
            /*services.AddDbContext<TaskContext>(opt =>
                opt.UseSqlServer(Configuration.GetConnectionString("Connection")));*/
        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        public void Configure(IApplicationBuilder app, IWebHostEnvironment env)
        {
            if (env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
            }

            app.UseHttpsRedirection();

            app.UseRouting();

            app.UseAuthorization();

            app.UseEndpoints(endpoints => { endpoints.MapControllers(); });
        }
    }
}