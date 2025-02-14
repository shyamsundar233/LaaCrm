import React from 'react';
import { Navigate } from 'react-router-dom';
import {useSelector} from "react-redux";

const PrivateRoute = ({ component: Component, ...rest }) => {

    const token = useSelector((state) => state.auth.token);

    return token ? <Component {...rest} /> : <Navigate to="/login" />;
};

export default PrivateRoute;
