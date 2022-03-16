let rolesResponse;
let rolesList;

$(async function () {
    await getRolesList();
    getUsersTable();
    getNewUserForm();
    addNewUser();
})

const userFetchService = {
    head: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Referer': null
    },
    // bodyAdd : async function(user) {return {'method': 'POST', 'headers': this.head, 'body': user}},
    findAllUsers: async () => await fetch('admin/allUsers'),
    findAllRoles: async () => await fetch('admin/allRoles'),
    findUserById: async (id) => await fetch(`/admin/${id}`),
    addNewUser: async (user) => await fetch('api/users', {method: 'POST', headers: userFetchService.head, body: JSON.stringify(user)}),
    updateUser: async (user, id) => await fetch(`/admin`, {method: 'PUT', headers: userFetchService.head, body: JSON.stringify(user)}),
    deleteUser: async (id) => await fetch(`admin/${id}`, {method: 'DELETE', headers: userFetchService.head})
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

    let editButton = `<button  class="btn btn-outline-success" id="editButton">Edit</button>`;
    let closeButton = `<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>`
    modal.find('.modal-footer').append(closeButton);
    modal.find('.modal-footer').append(editButton);


    user.then(user => {
        let bodyForm = `
            <form class="form-group" id="editUser">
                <input type="text" class="form-control" id="id" name="id" value="${user.id}" disabled><br>
                <input class="form-control" type="text" id="firstName" value="${user.firstName}"><br>
                <input class="form-control" type="text" id="lastName" value="${user.lastName}"><br>
                <input class="form-control" id="age" type="number" value="${user.age}">
                <input class="form-control" type="text" id="email" value="${user.email}"><br>    
                <input class="form-control" type="text" id="password" value=""><br>  
                <select multiple class="form-control" style="height: 60px" id="editRoles" required>
                </select>
            </form>
        `;
        modal.find('.modal-body').append(bodyForm);

        for(let i in rolesList) {
            document.getElementById("editRoles").options[i] = new Option(rolesList[i].name, rolesList[i].id);
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

        let filteredRoles = [];
        let idRolesFilter;
        for (let i in roles) {
            idRolesFilter = rolesList.filter((role) => roles[i].includes(role.id));
            filteredRoles = filteredRoles.concat(idRolesFilter);
        }

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
    })
}

// удаление юзера
async function deleteUser(modal, id) {

    let preuser = await userFetchService.findUserById(id);
    let user = preuser.json();

    modal.find('.modal-title').html('Delete user');

    let deleteButton = `<button  class="btn btn-outline-success" id="deleteButton">Delete</button>`;
    let closeButton = `<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>`
    modal.find('.modal-footer').append(closeButton);
    modal.find('.modal-footer').append(deleteButton);

    user.then(user => {
        let bodyForm = `
            <form class="form-group" id="deleteUser">
                <input type="text" class="form-control" id="id" name="id" value="${user.id}" disabled><br>
                <input class="form-control" type="text" id="firstName" value="${user.firstName}" disabled><br>
                <input class="form-control" type="text" id="lastName" value="${user.lastName}" disabled><br>
                <input class="form-control" id="age" type="number" value="${user.age}" disabled>
                <input class="form-control" type="text" id="email" value="${user.email}" disabled><br>    
                <input class="form-control" type="text" id="password" value="" disabled><br>  
                <select multiple class="form-control" style="height: 60px" id="deleteRoles" disabled>
                </select>
            </form>
        `;
        modal.find('.modal-body').append(bodyForm);

        for(let i in rolesList) {
            document.getElementById("deleteRoles").options[i] = new Option(rolesList[i].name, rolesList[i].id);
        }
    })

    $("#deleteButton").on('click', async () => {
        await userFetchService.deleteUser(id);
        getUsersTable();
        modal.modal('hide');
    })
}
