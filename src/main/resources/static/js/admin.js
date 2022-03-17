let rolesResponse;
let rolesList;

$(async function () {
    await getRolesList();
    getUsersTable();
    addNewUser();
})

const userFetchService = {
    head: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Referer': null
    },

    findAllUsers: async () => await fetch('admin/allUsers'),
    findAllRoles: async () => await fetch('admin/allRoles'),
    findUserById: async (id) => await fetch(`/admin/${id}`),
    addUser: async (user) => await fetch('/admin',
        {method: 'POST', headers: userFetchService.head, body: JSON.stringify(user)}),
    updateUser: async (user, id) => await fetch(`/admin`,
        {method: 'PUT', headers: userFetchService.head, body: JSON.stringify(user)}),
    deleteUser: async (id) => await fetch(`admin/${id}`,
        {method: 'DELETE', headers: userFetchService.head})
}

//получение списка всех ролей
async function getRolesList() {
    rolesResponse = await userFetchService.findAllRoles()
    rolesList = await rolesResponse.json();
}

//вывод таблицы юзеров
async function getUsersTable() {
    let table = $('#mainTableWithUsers tbody');
    table.empty();

    await userFetchService.findAllUsers()
        .then(res => res.json())
        .then(users => {
            users.forEach(user => {
                let tableFilling =
                    "<tr>" +
                    "<td>" + user.id + "</td>" +
                    "<td>" + user.firstName + "</td>" +
                    "<td>" + user.lastName + "</td>" +
                    "<td>" + user.age + "</td>" +
                    "<td>" + user.email + "</td>" +
                    "<td>";
                for(i in user.roles) {
                    tableFilling += user.roles[i].name + " ";
                }
                tableFilling += "</td>" +
                    "<td>" +
                    "<button type=\"button\" class=\"btn btn-primary btn-edit\"" +
                    " data-userid="+ user.id +
                    " data-action=\"edit\"" +
                    " data-toggle=\"modal\"" +
                    " data-target=\"#defaultModal\">Edit" +
                    "</button>" +
                    "</td>" +
                    "<td>" +
                    "<button type=\"button\" class=\"btn btn-danger btn-delete\"" +
                    " data-userid="+ user.id +
                    " data-action=\"delete\"" +
                    " data-toggle=\"modal\"" +
                    " data-target=\"#defaultModal\">Delete" +
                    "</button>" +
                    "</td>" +
                    "</tr>";
                table.append(tableFilling);
            })
        })

    // открытие модального окна по кнопке
    $("#mainTableWithUsers").find('button').on('click', (event) => {

        let defaultModal = $('#defaultModal');

        let targetButton = $(event.target);
        let buttonUserId = targetButton.attr('data-userid');
        let buttonAction = targetButton.attr('data-action');

        defaultModal.attr('data-userid', buttonUserId);
        defaultModal.attr('data-action', buttonAction);

        getDefaultModal();
    })
}

// заполнение модального окна, выбор между редактированием и удалением
async function getDefaultModal() {
    $('#defaultModal').modal({
        keyboard: true,
        backdrop: "static",
        show: false
    }).on("show.bs.modal", (event) => {
        let thisModal = $(event.target);
        let userid = thisModal.attr('data-userid');
        let action = thisModal.attr('data-action');
        thisModal.off("show.bs.modal");
        if (action === 'edit') {
            editUser(thisModal, userid);
        } else if (action === 'delete') {
            deleteUser(thisModal, userid);
        }
    }).on("hidden.bs.modal", (e) => {
        let thisModal = $(e.target);
        thisModal.find('.modal-title').html('');
        thisModal.find('.modal-body').html('');
        thisModal.find('.modal-footer').html('');
    })
}


// редактирование юзера в модальном окне
async function editUser(modal, id) {
    let preuser = await userFetchService.findUserById(id);
    let user = preuser.json();

    modal.find('.modal-title').html('Edit user');

    let editButton = `<button  class="btn btn-primary" id="editButton">Edit</button>`;
    let closeButton = `<button type="button" class="btn btn-secondary" 
        data-dismiss="modal">Close</button>`
    modal.find('.modal-footer').append(closeButton);
    modal.find('.modal-footer').append(editButton);


    user.then(user => {
        let bodyForm = `
            <div class="container text-center" style="max-width: 50%">
                <form class="form-group" id="editUser">
                    <p id="errorMessage" style="color: orangered"></p>
                    <label for="id" style="margin-top: 10px; margin-bottom: 0">Id</label>
                    <input type="text" class="form-control form-control-sm" id="id" 
                        name="id" value="${user.id}" disabled>
                    <label for="firstName" style="margin-bottom: 0">First name</label>
                    <input class="form-control form-control-sm" type="text" id="firstName" 
                        value="${user.firstName}">
                    <label for="lastName" style="margin-bottom: 0">Last name</label>
                    <input class="form-control form-control-sm" type="text" id="lastName" 
                        value="${user.lastName}">
                    <label for="age" style="margin-bottom: 0">Age</label>
                    <input class="form-control form-control-sm" id="age" type="number" 
                        value="${user.age}">
                    <label for="email" style="margin-bottom: 0">Email</label>
                    <input class="form-control form-control-sm" type="text" id="email" 
                        value="${user.email}"> 
                    <label for="password" style="margin-bottom: 0">Password</label>  
                    <input class="form-control form-control-sm" type="text" id="password" 
                        value=""> 
                    <label for="editRoles" style="margin-bottom: 0">Role</label>
                    <select multiple class="form-control" style="height: 60px" id="editRoles">
                    </select>
                </form>
            </div>      
        `;
        modal.find('.modal-body').append(bodyForm);

        for(let i in rolesList) {
            document.getElementById("editRoles").options[i] =
                new Option(rolesList[i].name, rolesList[i].id);
        }
        $("#editRoles :first").attr('selected', 'true');
    })

    $("#editButton").on('click', async () => {
        let id = modal.find("#id").val();
        let firstName = modal.find("#firstName").val();
        let lastName = modal.find("#lastName").val();
        let age = modal.find("#age").val();
        let email = modal.find("#email").val();
        let password = modal.find("#password").val();
        let roles = modal.find("#editRoles").val();

        //валидация данных
        if (firstName.length < 2 || firstName.length > 20) {
            document.getElementById("errorMessage").innerHTML =
                "First name length should be between 2 and 20";
        } else if (lastName.length < 2 || lastName.length > 20) {
            document.getElementById("errorMessage").innerHTML =
                "Last name length should be between 2 and 20";
        } else if (age < 0 || age > 100) {
            document.getElementById("errorMessage").innerHTML =
                "Age should be between 0 and 100";
        } else if (email.length < 5 || email.length > 40) {
            document.getElementById("errorMessage").innerHTML =
                "Email length should be between 5 and 40";
        }  else {
            //извлечение выделенных ролей из селектора
            let filteredRoles = [];
            let idRolesFilter;
            for (let i in roles) {
                idRolesFilter = rolesList.filter((role) => roles[i].includes(role.id));
                filteredRoles = filteredRoles.concat(idRolesFilter);
            }

            //сборка json для последующей отправки
            let editedUser = {
                id: id,
                firstName: firstName,
                lastName: lastName,
                age: age,
                email: email,
                password: password,
                roles: filteredRoles
            }
            const response = await userFetchService.updateUser(editedUser, id);

            if (response.ok) {
                getUsersTable();
                modal.modal('hide');
            } else {
                alert("Not valid user data");
            }
        }
    })
}

// удаление юзера
async function deleteUser(modal, id) {

    let preuser = await userFetchService.findUserById(id);
    let user = preuser.json();

    modal.find('.modal-title').html('Delete user');

    let deleteButton = `<button  class="btn btn-danger" id="deleteButton">Delete</button>`;
    let closeButton = `<button type="button" class="btn btn-secondary" 
        data-dismiss="modal">Close</button>`
    modal.find('.modal-footer').append(closeButton);
    modal.find('.modal-footer').append(deleteButton);

    user.then(user => {
        let bodyForm = `
            <div class="container text-center" style="max-width: 50%">
                <form class="form-group" id="deleteUser">
                    <label for="id" style="margin-top: 10px; margin-bottom: 0">Id</label>
                    <input type="text" class="form-control form-control-sm" id="id" 
                        name="id" value="${user.id}" disabled>
                    <label for="firstName" style="margin-bottom: 0">First name</label>
                    <input class="form-control form-control-sm" type="text" id="firstName" 
                        value="${user.firstName}" disabled>
                    <label for="lastName" style="margin-bottom: 0">Last name</label>
                    <input class="form-control form-control-sm" type="text" id="lastName" 
                        value="${user.lastName}" disabled>
                    <label for="age" style="margin-bottom: 0">Age</label>
                    <input class="form-control form-control-sm" id="age" type="number" 
                        value="${user.age}" disabled>
                    <label for="email" style="margin-bottom: 0">Email</label>
                    <input class="form-control form-control-sm" type="text" id="email" 
                        value="${user.email}" disabled> 
                    <label for="deleteRoles" style="margin-bottom: 0">Role</label>
                    <select multiple class="form-control" style="height: 60px" 
                    id="deleteRoles" disabled></select>
                </form>
            </div>      
        `;
        modal.find('.modal-body').append(bodyForm);

        for(let i in rolesList) {
            document.getElementById("deleteRoles").options[i] =
                new Option(rolesList[i].name, rolesList[i].id);
        }
    })

    $("#deleteButton").on('click', async () => {
        await userFetchService.deleteUser(id);
        getUsersTable();
        modal.modal('hide');
    })
}

async function addNewUser() {
    let addForm = $('#newUserForm');

    let bodyForm = `
            <div class="container text-center" style="max-width: 30%">
                <div class="form-group" id="addNewUser">
                    <p id="errorMessage" style="color: orangered"></p>
                    <label for="firstName" style="margin-bottom: 0">First name</label>
                    <input class="form-control form-control-sm" type="text" id="firstName">
                    <label for="lastName" style="margin-bottom: 0">Last name</label>
                    <input class="form-control form-control-sm" type="text" id="lastName">
                    <label for="age" style="margin-bottom: 0">Age</label>
                    <input class="form-control form-control-sm" id="age" type="number">
                    <label for="email" style="margin-bottom: 0">Email</label>
                    <input class="form-control form-control-sm" type="text" id="email"> 
                    <label for="password" style="margin-bottom: 0">Password</label>  
                    <input class="form-control form-control-sm" type="text" id="password"> 
                    <label for="newRoles" style="margin-bottom: 0">Role</label>
                    <select multiple class="form-control" style="height: 60px" id="newRoles">
                    </select><br>
                    <button class="btn btn-success rounded"
                        style="margin-bottom: 10px" id="addButton">Add new user
                    </button>
                </div>
            </div>      
        `;
    addForm.append(bodyForm);

    for(let i in rolesList) {
        document.getElementById("newRoles").options[i] =
            new Option(rolesList[i].name, rolesList[i].id);
    }
    $("#newRoles :first").attr('selected', 'true');

    $("#addButton").on('click', async () => {
        let firstName = addForm.find("#firstName").val();
        let lastName = addForm.find("#lastName").val();
        let age = addForm.find("#age").val();
        let email = addForm.find("#email").val();
        let password = addForm.find("#password").val();
        let roles = addForm.find("#newRoles").val();

        //валидация данных
        if (firstName.length < 2 || firstName.length > 20) {
            document.getElementById("errorMessage").innerHTML =
                "First name length should be between 2 and 20";
        } else if (lastName.length < 2 || lastName.length > 20) {
            document.getElementById("errorMessage").innerHTML =
                "Last name length should be between 2 and 20";
        } else if (age < 0 || age > 100) {
            document.getElementById("errorMessage").innerHTML =
                "Age should be between 0 and 100";
        } else if (email.length < 5 || email.length > 40) {
            document.getElementById("errorMessage").innerHTML =
                "Email length should be between 5 and 40";
        } else if (password.length < 1) {
            document.getElementById("errorMessage").innerHTML =
                "Password length should be more 1";
        } else {
            //извлечение выделенных ролей из селектора
            let newFilteredRoles = [];
            let newIdRolesFilter;
            for (let i in roles) {
                newIdRolesFilter = rolesList.filter((role) => roles[i].includes(role.id));
                newFilteredRoles = newFilteredRoles.concat(newIdRolesFilter);
            }
            //сборка json для последующей отправки
            let addedUser = {
                id: null,
                firstName: firstName,
                lastName: lastName,
                age: age,
                email: email,
                password: password,
                roles: newFilteredRoles
            }
            const response = await userFetchService.addUser(addedUser);

            if (response.ok) {
                let body = await response.json();
                if (body.message === "Added successful") {
                    getUsersTable();
                    $('.nav-tabs a:first').tab('show');
                } else {
                    document.getElementById("errorMessage").innerHTML =
                        body.message;
                }

            } else {
                alert("Not valid user data");
            }
        }
    })
}