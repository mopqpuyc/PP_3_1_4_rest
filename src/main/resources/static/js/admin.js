$(async function () {
    await getUsersTable();
    //getEditUserModal();
    getDeleteUserModal();
    getNewUserModal();
})

async function getUsersTable() {
    let table = $('#mainTableWithUsers tbody');
    table.empty();
    await fetch("/admin/all").then(res=>{
        res.json().then(data=>{
            data.forEach(user=> {
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
                    " data-userId="+ user.id +
                    " data-firstName=" + user.firstName +
                    " data-lastName=" + user.lastName +
                    " data-age=" + user.age +
                    " data-email=" + user.email +
                    " data-password=" + user.password +
                    " data-roles=" + user.roles +
                    " data-toggle=\"modal\"" +
                    " data-target=\"#editModal\">Edit" +
                    "</button>" +
                    "</td>" +
                    "<td>" +
                    "<button type=\"button\" class=\"btn btn-danger btn-delete\"" +
                    " data-userId="+ user.id +
                    " data-firstName=" + user.firstName +
                    " data-lastName=" + user.lastName +
                    " data-age=" + user.age +
                    " data-email=" + user.email +
                    " data-password=" + user.password +
                    " data-roles=" + user.roles +
                    " data-toggle=\"modal\"" +
                    " data-target=\"#deleteModal\">Delete" +
                    "</button>" +
                    "</td>" +
                    "</tr>";
                table.append(tableFilling);

                $("#mainTableWithUsers").find('button').on('click', (event) => {
                    let defaultModal = $('#defaultModal');

                    let targetButton = $(event.target);
                    let buttonUserId = targetButton.attr('data-userid');
                    let buttonAction = targetButton.attr('data-action');

                    defaultModal.attr('data-userid', buttonUserId);
                    defaultModal.attr('data-action', buttonAction);
                    //defaultModal.modal('show');
                    //defaultModal.style.display = "block";
                    alert("hey!");
                    //editUser(defaultModal, buttonUserId);
                    getEditUserModal();

                })
            })
        })
    })
}

async function getEditUserModal() {
    alert("123");
    $("#editModal").modal({
        keyboard: true,
        backdrop: "static",
        show: false
    })
        .on("show.bs.modal", (event) => {
            alert("show");
            let thisModal = $(event.target);
            let userId = thisModal.attr('data-userId');
            let firstName = thisModal.attr('data-firstName');
            let lastName = thisModal.attr('data-lastName');
            let age = thisModal.attr('data-age');
            let email = thisModal.attr('data-email');
            let password = thisModal.attr('data-password');
            let roles = thisModal.attr('data-roles');

            $("#editId").attr('value', userId);
            $("#editFirstName").attr('value', firstName);
            $("#editLastName").attr('value', lastName);
            $("#editAge").attr('value', age);
            $("#editEmail").attr('value', email);
            //$("#editPassword").attr('value', password);
            $("#editRole").attr('value', roles);
            thisModal.off("show.bs.modal");
            editUser(thisModal, userId);
        })
        .on("hidden.bs.modal", (e) => {
            alert("hid");
            let thisModal = $(e.target);
            thisModal.find('.modal-title').html('');
            thisModal.find('.modal-body').html('');
            thisModal.find('.modal-footer').html('');
        })
}

async function editUser(modal, id) {
    let editButton = `<button id="editConfirm" class="btn btn-primary rounded">Edit</button>`;
    modal.find('.modal-footer').append(editButton);

    let firstName = modal.find("#editFirstName").val();
    let lastName = modal.find("#editLastName").val();
    let age = modal.find("#editAge").val();
    let email = modal.find("#editEmail").val();
    let password = modal.find("#editPassword").val();
    let roles = modal.find("#editRoles").val();
    let user = {
        id: id,
        firstName: firstName,
        lastName: lastName,
        age: age,
        email: email,
        password: password,
        roles: roles
    };
    /*$.ajax({
        type: 'PUT', // метод
        url: "/admin", // путь, куда отправлять данные
        data: user // данные в формате {параметр:'значение'}, на сервере парсятся $_POST['параметр'] равен 'значение'
    })*/
    /*let response = await fetch('/admin', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify(user)
    });

    if (response.ok) {
        getUsersTable();
        modal.modal('hide');
    }*/
    //getUsersTable();
    //modal.modal('hide');

}





