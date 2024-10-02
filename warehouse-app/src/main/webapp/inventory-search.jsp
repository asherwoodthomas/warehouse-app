<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Inventory Search</title>
<link rel="stylesheet" href="inventory-style.css">
</head>
<body>

	<h1>Warehouse Inventory</h1>

	<div class="searchBox">
		<form action="WarehouseController" method="GET">
			<input type="hidden" name="command" Value="SEARCH" /> 
			<input type="text" name=searchTerm id="searchInput"> 
			<input type="submit" value="Search" id=searchBtn>
		</form>
	</div>
	<br>
	<div>

		<table class="table">
 <tr id="headRow">
				<th>Product Number</th>
				<th>Name</th>
				<th>Description</th>
				<th>Retail Price</th>
				<th>Quantity In Stock</th>
				<th>Category ID</th>
				<th>Actions</th>
			</tr>
			<c:forEach var="tempItem" items="${sessionScope.SEARCH_RESULT}">

				<tr class="searchRow">

					<td><input type="hidden" name="productNumber"
						Value="${tempItem.productNumber}" /> ${tempItem.productNumber}</td>
					<td class="canEdit" data-input-name="productName">${tempItem.productName}</td>
					<td class="canEdit" data-input-name="productDescription">${tempItem.productDescription}</td>
					<td class="canEdit" data-input-name="retailPrice">${tempItem.retailPrice}</td>
					<td class="canEdit" data-input-name="quantityOnHand">${tempItem.quantityOnHand}</td>
					<td><input type="hidden" name="categoryID" Value="${tempItem.categoryID}"/> ${tempItem.categoryID}</td>
					<td>
						<div class="actionBtns">
							<input type="button" value="Edit" class="editButton">
						</div>
					</td>
			</c:forEach>
			<tr>
				<td>#</td>
				<td><input type="text" name="productName" required
					form="addItem" class="editInput"></td>
				<td><input type="text" name="productDescription" required
					form="addItem" class="editInput"></td>
				<td><input type="number" name="retailPrice" required
					form="addItem" class="editInput"></td>
				<td><input type="number" name="quantityOnHand" required
					form="addItem" min="-32768" max="32767" onchange=enforceMinMax
					class="editInput"></td>
				<td><input type="number" name="categoryID" required
					form="addItem" class="editInput"></td>
				<td><input type="submit" value="Add Item" form="addItem"
					id="addItemSubmit"></td>
			</tr>

		</table>
	</div>
	<form action="WarehouseController" method="POST" id="addItem"
		name="addItem">
		<input type="hidden" name="command" Value="ADD" />
	</form>
	<script src="tableScript.js"></script>
</body>
</html>