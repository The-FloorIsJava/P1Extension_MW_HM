let loginBlank = document.getElementById("login-form").innerHTML
const url = "http://localhost:8080/"

function userLogin(form){
    let username = form.username.value;
    let password = form.password.value;

    fetch(`${url}login`, {
        method: 'POST',
        body: JSON.stringify({
            userName: username,
            password: password
        })
    })
    .then(response =>{
        /*response will go here*/
    })
    .catch(error => {
        /*error handling will go here*/
    })
}

function logoutButton(){

}

function logout(){

}