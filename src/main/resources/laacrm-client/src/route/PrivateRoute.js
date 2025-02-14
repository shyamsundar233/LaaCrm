import React from 'react';
import { Navigate } from 'react-router-dom';
import authService from "../api/authService";

const PrivateRoute = ({ component: Component, ...rest }) => {

    const token = authService.getAuthData().token;

    return token ? <Component {...rest} /> : <Navigate to="/login" />;
};

export default PrivateRoute;
