package com.exemple.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.exemple.entity.*;
import com.exemple.implementation.*;

@WebServlet("/task")
public class TaskController extends HttpServlet implements jakarta.servlet.Servlet {
	private static final long serialVersionUID = 1L;

	private TaskDao taskdao = new TaskDao();

	private static final String CREATE_ACTION = "Create Task";
	private static final String UPDATE_ACTION = "Update Task";
	private static final String REMOVE_ACTION = "Remove Task";
	private static final String LIST_ACTION = "List Of Tasks";

	public TaskController() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getServletPath();

		switch (action) {
		case "/new":
			showNewForm(request, response);
			break;
		case "/insert":
			createTask(request, response);
			break;
		case "/delete":
			removeTask(request, response);
			break;
		case "/update":
			updateTask(request, response);
			break;
		case "/listtask":
			listTasks(request, response);
			break;
		default:
			showNewForm(request, response);
			break;

		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");

		if (CREATE_ACTION.equals(action)) {
			createTask(request, response);
		} else if (UPDATE_ACTION.equals(action)) {
			updateTask(request, response);
		} else if (REMOVE_ACTION.equals(action)) {
			removeTask(request, response);
		} else if (LIST_ACTION.equals(action)) {
			listTasks(request, response);
		}
	}

	private void showNewForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<Project> projects = null;
		try {
			projects = taskdao.getAllProjects();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		request.setAttribute("projects", projects);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/task.jsp");
		dispatcher.forward(request, response);
	}

	private boolean validateTaskData(String code, String description, String startDate, String endDate) {
		return code != null && !code.isEmpty() && description != null && !description.isEmpty() && startDate != null
				&& !startDate.isEmpty() && endDate != null && !endDate.isEmpty();
	}

	private void createTask(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String code = request.getParameter("code");
		String description = request.getParameter("description");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String projectCode = request.getParameter("projectCode");

		if (!validateTaskData(code, description, startDate, endDate)) {
			String errorMessage = "Please complete all fields on the form.";
			request.setAttribute("errorType", "Validation");
			request.setAttribute("errorMessage", errorMessage);
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/failedInsertTask.jsp");
			dispatcher.forward(request, response);
			return;
		}

		if (projectCode == null || projectCode.isEmpty()) {
			String errorMessage = "Veuillez sélectionner un projet.";
			request.setAttribute("errorType", "Validation");
			request.setAttribute("errorMessage", errorMessage);
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/failedInsertTask.jsp");
			dispatcher.forward(request, response);
			return;
		}

		Project selectedProject = null;
		try {
			selectedProject = taskdao.getProjectById(projectCode);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (selectedProject == null) {
			String errorMessage = "Projet non trouvé.";
			request.setAttribute("errorType", "Validation");
			request.setAttribute("errorMessage", errorMessage);
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/failedInsertTask.jsp");
			dispatcher.forward(request, response);
			return;
		}

		Task task = new Task();
		task.setCode(code);
		task.setDescription(description);
		task.setStartDate(startDate);
		task.setEndDate(endDate);
		task.setProject(selectedProject);

		try {
			taskdao.addTask(task);
			System.out.println("Row successfully inserted!");
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/successInsertTask.jsp");
			dispatcher.forward(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
			String errorMessage = "Failed to insert task: " + e.getMessage();
			System.out.println(errorMessage);
			request.setAttribute("errorMessage", errorMessage);
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/failedInsertTask.jsp");
			dispatcher.forward(request, response);
		}
	}

	private void updateTask(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String code = request.getParameter("code");
		String description = request.getParameter("description");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String projectCode = request.getParameter("projectCode");

		Task updatedTask = new Task();
		updatedTask.setCode(code);
		updatedTask.setDescription(description);
		updatedTask.setStartDate(startDate);
		updatedTask.setEndDate(endDate);

		try {
			Task existingTask = taskdao.getTaskByCode(code);
			if (existingTask != null) {
				existingTask.setDescription(updatedTask.getDescription());
				existingTask.setStartDate(updatedTask.getStartDate());
				existingTask.setEndDate(updatedTask.getEndDate());

				Project project = taskdao.getProjectById(projectCode);
				existingTask.setProject(project);

				boolean success = taskdao.updateTask(existingTask);
				if (success) {
					RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/successUpdateTask.jsp");
					dispatcher.forward(request, response);
				} else {
					String errorMessage = "Failed to update task. Please try again.";
					request.setAttribute("errorType", "Update Error");
					request.setAttribute("errorMessage", errorMessage);
					RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/failedUpdateTask.jsp");
					dispatcher.forward(request, response);
				}
			} else {
				String errorMessage = "Task not found.";
				request.setAttribute("errorType", "Update Error");
				request.setAttribute("errorMessage", errorMessage);
				RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/failedUpdateTask.jsp");
				dispatcher.forward(request, response);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			String errorMessage = "Failed to update task: " + e.getMessage();
			request.setAttribute("errorType", "Update Error");
			request.setAttribute("errorMessage", errorMessage);
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/failedUpdateTask.jsp");
			dispatcher.forward(request, response);
		}
	}

	private void removeTask(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String code = request.getParameter("code");

		try {
			boolean success = taskdao.deleteTask(code);

			if (success) {
				RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/successRemoveTask.jsp");
				dispatcher.forward(request, response);
			} else {
				String errorMessage = "Task not found. The deletion failed.";
				request.setAttribute("errorType", "Deletion error");
				request.setAttribute("errorMessage", errorMessage);
				RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/failedRemoveTask.jsp");
				dispatcher.forward(request, response);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			String errorMessage = "Failed to delete task : " + e.getMessage();
			request.setAttribute("errorType", "Critical error");
			request.setAttribute("errorMessage", errorMessage);
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/failedRemoveTask.jsp");
			dispatcher.forward(request, response);
		}
	}

	private void listTasks(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			List<Task> tasks = taskdao.getAllTasks();
			request.setAttribute("tasks", tasks);
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/listTasks.jsp");
			dispatcher.forward(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
			String errorMessage = "Failed to retrieve projects: " + e.getMessage();
			request.setAttribute("errorMessage", errorMessage);
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/failedListTasks.jsp");
			dispatcher.forward(request, response);
		}
	}

}