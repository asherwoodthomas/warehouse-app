const editButtonList = document.querySelectorAll('.editButton');

editButtonList.forEach((tr) => {
	tr.addEventListener('click', function (e) {
		editButtonList.forEach((tr) => {tr.hidden = true;})
		
		const actionDiv = e.target.closest("div");
		actionDiv.appendChild(createButtons());
		
		const row = e.target.closest("tr");
		const cellList= row.querySelectorAll('.canEdit');
		
		cellList.forEach((td) => {
			const value = td.textContent;
			const inputName = td.dataset.inputName;
			const newInput = document.createElement("input");
			newInput.setAttribute("value", value);
			newInput.setAttribute("name", inputName);
			newInput.setAttribute("class", "editInput")
			if (inputName === "productName" || inputName === "productDescription") {
				newInput.setAttribute("type", "text");
			} else {
				newInput.setAttribute("type", "number");
			}
			td.replaceChildren(newInput);
			});
		
	})
});


function createButtons(){
	
	const buttonDiv = document.createElement("div");
	buttonDiv.setAttribute("id","buttonDiv")
	const submitBtn = document.createElement("input");
	submitBtn.setAttribute("type", "button");
	submitBtn.setAttribute("value", "Save");
	submitBtn.setAttribute("id", "submitBtn");
	submitBtn.setAttribute("class", "actionButton");
	
	submitBtn.addEventListener('click', function(e){submitForm(e, "UPDATE")})
	
	buttonDiv.appendChild(submitBtn);
	
	const cancelBtn = document.createElement("input");
	cancelBtn.setAttribute("type", "button");
	cancelBtn.setAttribute("value", "Cancel");
	cancelBtn.setAttribute("id", "cancelBtn");
	cancelBtn.setAttribute("class", "actionButton");
	
	cancelBtn.addEventListener('click', function () {
		buttonDiv.remove();
		editButtonList.forEach((tr) => {tr.hidden = false;})
		
		const cellList= document.querySelectorAll('.editInput');
		cellList.forEach((td) => {
				const value = td.value;
				const newNode = document.createTextNode(value);
				td.replaceWith(newNode);
				})
		
	})
	buttonDiv.appendChild(cancelBtn);
	
	const deleteBtn = document.createElement("input");
	deleteBtn.setAttribute("type", "button");
		deleteBtn.setAttribute("value", "Delete");
		deleteBtn.setAttribute("id", "deleteBtn");
		deleteBtn.setAttribute("class", "actionButton");
		deleteBtn.addEventListener('click', function(e){
			if (confirm("Are you sure you want to delete this item from inventory?")){
				submitForm(e, "DELETE")
			}
		})
		buttonDiv.appendChild(deleteBtn);
	
	return buttonDiv;
}

function submitForm(e, commandName) {
	const quantityOnHand = document.getElementsByName("quantityOnHand")[0];
	if (parseInt(quantityOnHand.value) > 32767 || parseInt(quantityOnHand.value) < -32768) {
		alert("Invalid Quantity. Quantity must be between -32768 and 32767")
	} else {
	const form = document.createElement("form");
	document.body.appendChild(form);
	form.method = "POST";
	form.action = "WarehouseController";
	const command = document.createElement("input");
	command.setAttribute("type", "hidden");
	command.setAttribute("name", "command");
	command.setAttribute("value", commandName);
	form.appendChild(command);
	const row = e.target.closest("tr");
	const cellList = row.querySelectorAll('input');
	cellList.forEach((td) => { form.appendChild(td); })
	console.log(form);
	form.submit();
	}
}

function enforceMinMax(el) {
  if (el.value != "") {
    if (parseInt(el.value) < parseInt(el.min)) {
      el.value = el.min;
    }
    if (parseInt(el.value) > parseInt(el.max)) {
      el.value = el.max;
    }
  }
}


