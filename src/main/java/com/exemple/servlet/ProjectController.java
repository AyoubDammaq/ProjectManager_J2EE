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

@WebServlet("/project")
public class ProjectController extends HttpServlet implements jakarta.servlet.Servlet {
	private static final long serialVersionUID = 1L;

	private ProjectDao projectdao = new ProjectDao();

	private static final String CREATE_ACTION = "Create Project";
	private static final String UPDATE_ACTION = "Update Project";
	private static final String REMOVE_ACTION = "Remove Project";
	private static final String LIST_ACTION = "List Of Projects";

	public ProjectController() {
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
			createProject(request, response);
			break;
		case "/delete":
			removeProject(request, response);
			break;
		case "/update":
			updateProject(request, response);
			break;
		case "/listproject":
			listProjects(request, response);
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
			createProject(request, response);
		} else if (UPDATE_ACTION.equals(action)) {
			updateProject(request, response);
		} else if (REMOVE_ACTION.equals(action)) {
			removeProject(request, response);
		} else if (LIST_ACTION.equals(action)) {
			listProjects(request, response);
		}
	}

	private void showNewForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/project.jsp");
		dispatcher.forward(request, response);
	}

	private boolean validateProjectData(String code, String description, String startDate) {
		return code != null && !code.isEmpty() && description != null && !description.isEmpty() && startDate != null
				&& !startDate.isEmpty();
	}

	private void createProject(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String code = request.getParameter("code");
		String description = request.getParameter("description");
		String startDate = request.getParameter("startDate");

		if (!validateProjectData(code, description, startDate)) {
			String errorMessage = "Please complete all fields on the form.";
			request.setAttribute("errorType", "Validation");
			request.setAttribute("errorMessage", errorMessage);
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/failedInsertProject.jsp");
			dispatcher.forward(request, response);
			return;
		}

		Project project = new Project();
		project.setCode(code);
		project.setDescription(description);
		project.setStartDate(startDate);

		try {
			projectdao.addProject(project);
			System.out.println("Row successfully inserted!");
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/successInsertProject.jsp");
			dispatcher.forward(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
			String errorMessage = "Failed to insert project: " + e.getMessage();
			System.out.println(errorMessage);
			request.setAttribute("errorMessage", errorMessage);
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/failedInsertProject.jsp");
			dispatcher.forward(request, response);
		}
	}

	private void updateProject(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String code = request.getParameter("code");
		String description = request.getParameter("description");
		String startDate = request.getParameter("startDate");
		Project updatedProject = new Project();
		updatedProject.setCode(code);
		updatedProject.setDescription(description);
		updatedProject.setStartDate(startDate);

		try {
			Project existingProject = projectdao.getProjectByCode(code);
			if (existingProject != null) {
				existingProject.setDescription(updatedProject.getDescription());
				existingProject.setStartDate(updatedProject.getStartDate());
				boolean success = projectdao.updateProject(existingProject);
				if (success) {
					RequestDispatcher dispatcher = request
							.getRequestDispatcher("/WEB-INF/views/successUpdateProject.jsp");
					dispatcher.forward(request, response);
				} else {
					String errorMessage = "Failed to update project. Please try again.";
					request.setAttribute("errorType", "Update Error");
					request.setAttribute("errorMessage", errorMessage);
					RequestDispatcher dispatcher = request
							.getRequestDispatcher("/WEB-INF/views/failedUpdateProject.jsp");
					dispatcher.forward(request, response);
				}
			} else {
				String errorMessage = "Project not found.";
				request.setAttribute("errorType", "Update Error");
				request.setAttribute("errorMessage", errorMessage);
				RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/failedUpdateProject.jsp");
				dispatcher.forward(request, response);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			String errorMessage = "Failed to update project: " + e.getMessage();
			request.setAttribute("errorType", "Update Error");
			request.setAttribute("errorMessage", errorMessage);
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/failedUpdateProject.jsp");
			dispatcher.forward(request, response);
		}
	}

	private void removeProject(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String code = request.getParameter("code");

		try {
			boolean success = projectdao.deleteProject(code);

			if (success) {
				RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/successRemoveProject.jsp");
				dispatcher.forward(request, response);
			} else {
				String errorMessage = "Project not found. The deletion failed.";
				request.setAttribute("errorType", "Deletion error");
				request.setAttribute("errorMessage", errorMessage);
				RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/failedRemoveProject.jsp");
				dispatcher.forward(request, response);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			String errorMessage = "Failed to delete project : " + e.getMessage();
			request.setAttribute("errorType", "Critical error");
			request.setAttribute("errorMessage", errorMessage);
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/failedRemoveProject.jsp");
			dispatcher.forward(request, response);
		}
	}

	private void listProjects(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			List<Project> projects = projectdao.getAllProjects();

			request.setAttribute("projects", projects);

			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/listProjects.jsp");
			dispatcher.forward(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
			String errorMessage = "Failed to retrieve projects: " + e.getMessage();
			request.setAttribute("errorMessage", errorMessage);
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/failedListProjects.jsp");
			dispatcher.forward(request, response);
		}
	}

}
