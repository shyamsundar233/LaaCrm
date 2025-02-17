import axios from "axios";

const authenticateToken = () => {
    if(!localStorage.getItem("token") || !localStorage.getItem("username")) {
        return Promise.reject();
    }
    return axios({
        method: "GET",
        url: "/v1/api/users/authenticate",
        params: {
            token: localStorage.getItem("token"),
            username: localStorage.getItem("username")
        }
    })
}

const getAuthData = () => {
    return {
        token: localStorage.getItem("token"),
        username: localStorage.getItem("username")
    }
}

const setAuthData = (token, username) => {
    localStorage.setItem("token", token);
    localStorage.setItem("username", username);
}

const clearAuthData = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("username");
}

export default {authenticateToken, getAuthData, setAuthData, clearAuthData}