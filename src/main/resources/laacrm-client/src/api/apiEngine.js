import axios from "axios";
import authService from "./authService";

const requestHelper = (method, url, data, params) => {
    return axios({
        method: method,
        url: url,
        data: data,
        params: params,
        headers: {
            'Authorization': `Bearer ${authService.getAuthData().token}`,
        }
    });
}

export default {requestHelper};