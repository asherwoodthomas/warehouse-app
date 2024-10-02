package com.loremipsumlogistics.web.jdbc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class WarehouseController
 */
@WebServlet("/WarehouseController")
public class WarehouseController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private WarehouseDbUtil warehouseDbUtil;

	@Resource(name = "jdbc/SalesOrdersExample")
	private DataSource dataSource;

	@Override
	public void init() throws ServletException {
		super.init();

		// create our student db uitl and pass in the conn pool / datasource
		try {
			warehouseDbUtil = new WarehouseDbUtil(dataSource);
		} catch (Exception exc) {
			throw new ServletException(exc);
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			String theCommand = request.getParameter("command");

			// if missing then return All
			if (theCommand == null) {
				theCommand = "SEARCH";
			}
			// rout to the appropriate method
			switch (theCommand) {
			case "SEARCH":
				// list the students in MVC fashion
				searchItem(request, response);
				break;
			case "ADD":
				addItem(request, response);
				break;
//			case "LOAD":
//				loadItem(request, response);
//				break;
			case "UPDATE":
				updateItem(request, response);
				break;
			case "DELETE":
				deleteItem(request, response);
				break;
			default:
				searchItem(request, response);
			}

		} catch (Exception exc) {
			throw new ServletException(exc);
		}

	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	private void searchItem(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// get search term from request
		String searchTerm = request.getParameter("searchTerm");
		if(searchTerm != null) {
			// get search items from dbUtil
			List<Item> items = warehouseDbUtil.searchItems(searchTerm);	
			// add items to request
			request.getSession().setAttribute("SEARCH_RESULT", items);

			request.getSession().setAttribute("SEARCH_TERM", searchTerm);
		}
		// sent to jsp page
		RequestDispatcher dispatcher = request.getRequestDispatcher("/inventory-search.jsp");
		dispatcher.forward(request, response);

	}

	private void addItem(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// get params from request
		String productName = request.getParameter("productName");
		String productDescription = request.getParameter("productDescription");
		double retailPrice = doubleVal(request.getParameter("retailPrice"));
		int quantityOnHand = intVal(request.getParameter("quantityOnHand"));;
		int categoryID = intVal(request.getParameter("categoryID"));;
		//Validate on hand range
		if(quantityOnHand <-32768 || quantityOnHand > 32767) {
			quantityOnHand = -1;
		}
		int productNumber = warehouseDbUtil.fetchProductNumber();
		// new item object
		Item newItem = new Item(productNumber, productName, productDescription, retailPrice, quantityOnHand,
				categoryID);
		// add to session search list
		List<Item> items = new ArrayList<>();
		items.addAll((ArrayList<Item>) request.getSession().getAttribute("SEARCH_RESULT"));
		items.add(newItem);
		request.getSession().setAttribute("SEARCH_RESULT", items);
		// send to db
		warehouseDbUtil.addItem(newItem);

		// return to search
		RequestDispatcher dispatcher = request.getRequestDispatcher("/item-added.jsp");
		dispatcher.forward(request, response);

	}

	private void updateItem(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// get params from request
		String productName = request.getParameter("productName");
		String productDescription = request.getParameter("productDescription");
		int productNumber = intVal(request.getParameter("productNumber"));
		double retailPrice = doubleVal(request.getParameter("retailPrice"));
		int quantityOnHand = intVal(request.getParameter("quantityOnHand"));;
		int categoryID = intVal(request.getParameter("categoryID"));;
		//Validate on hand range
		if(quantityOnHand <-32768 || quantityOnHand > 32767) {
			quantityOnHand = -1;
		}
		// new item object
				Item newItem = new Item(productNumber, productName, productDescription, retailPrice, quantityOnHand,
						categoryID);
		// edit session search list
				List<Item> items = new ArrayList<>();
				items.addAll((ArrayList<Item>) request.getSession().getAttribute("SEARCH_RESULT"));
				for(int i =0; i< items.size();i++) {
					Item thisItem = items.get(i);
					if(thisItem.getProductNumber() == productNumber) {
						items.set(i, newItem);
					}
				}
				request.getSession().setAttribute("SEARCH_RESULT", items);
		// send to db
				warehouseDbUtil.updateItem(newItem);
		// return to search
		searchItem(request, response);

	}

	private void deleteItem(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// get productNumber from request
		String productNumberString = request.getParameter("productNumber");
		// convert to int
		int productNumber = Integer.parseInt(productNumberString);
		// edit session search list
		List<Item> items = new ArrayList<>();
		items.addAll((ArrayList<Item>) request.getSession().getAttribute("SEARCH_RESULT"));
		for(int i =0; i< items.size();i++) {
			Item thisItem = items.get(i);
			if(thisItem.getProductNumber() == productNumber) {
				items.remove(i);
			}
		}
		request.getSession().setAttribute("SEARCH_RESULT", items);
		// send to db
		warehouseDbUtil.deleteItem(productNumber);
		// return to search
		searchItem(request, response);

	}
	
	private int intVal (String input) {
		int out = 0;
		try {
			out = Integer.parseInt(input);
		} catch (final NumberFormatException e) {
		}
		return out;
	}
	
	private double doubleVal (String input) {
		double out = 0;
		try {
			out = Double.parseDouble(input);
		} catch (final NumberFormatException e) {
		}
		return out;
	}

}
