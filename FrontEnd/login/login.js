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
        console.log(response)
        console.log(response.status)
        if(response.status === 404){
            throw new Error(response.text().then(body => console.log(body)))
        }
        console.log(...response.headers)
        document.getElementById("login-form").innerHTML = "<h4 id='welcome'> Welcome to the Employee Reimbursement System, " + username + "</h4>"
        console.log(password)
        logoutButton()
        window.localStorage.setItem("token", response.headers.get("authorization"))
    })
    .catch(error => {
        console.error(error)
        document.getElementById("login-form").innerHTML = `${loginInitial} <h4>Login Failed. Please try again.</h4>`
    })
}

function logoutButton(){
    let button = document.createElement("button")
    let node = document.createTextNode("Logout")

    button.appendChild(node)
    button.setAttribute("onclick", "logout()")
    const welcomeH1 = document.getElementById("welcome")
    welcomeH1.appendChild(button)
}

function logout(){
    document.getElementById("login-form").innerHTML = loginBlank;
}