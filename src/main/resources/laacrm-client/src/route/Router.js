import App from "../App";
import PrivateRoute from "./PrivateRoute";
import Dashboard from "../components/dashboard/Dashboard";
import {BrowserRouter, Navigate, Route, Routes} from "react-router-dom";
import Login from "../components/login/Login";

const Router = () => {
    return (
        <BrowserRouter>
            <Routes>
                <Route exact path="/" element={<Navigate to="/app/dashboard"/>}/>
                <Route path="/app/*" element={<PrivateRoute component={authRoutes}/>} />
                <Route path="/login" element={<Login />} />
            </Routes>
        </BrowserRouter>
    );
}

const authRoutes = () => (
    <Routes>
        <Route path="/" element={<App />}>
            <Route path="dashboard" element={<Dashboard/>}/>
        </Route>
    </Routes>
);

export default Router;