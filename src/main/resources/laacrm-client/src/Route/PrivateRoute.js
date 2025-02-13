import React from 'react';
import { Navigate } from 'react-router-dom';

const PrivateRoute = ({ component: Component, ...rest }) => {
    const currentUser = true;

    return currentUser ? <Component {...rest} /> : <Navigate to="/login" />;
};

export default PrivateRoute;
