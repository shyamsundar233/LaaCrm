import axios from "axios";

const requestHelper = (method, url, data, params) => {
    return axios({
        method: method,
        url: url,
        data: data,
        params: params,
        headers: {
            'Authorization': `Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJUZXN0IFVzZXIgMSIsImlhdCI6MTczOTQ2MDUzNiwiZXhwIjoxNzM5NTQ2OTM2fQ.LSsybzrYloyxnFXCIQ0eSc-_dAhamdH8efp6advmiqw`,
        }
    });
}

export default {requestHelper};