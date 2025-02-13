import {BrowserRouter, Navigate, Route, Routes} from "react-router-dom";
import App from "../App";
import PrivateRoute from "./PrivateRoute";
import Dashboard from "../Dashboard/Dashboard";

const Router = () => {
    return (
        <BrowserRouter>
            <Routes>
                <Route exact path="/" element={<Navigate to="/app/dashboard"/>}/>
                <Route path="/app/*" element={<PrivateRoute component={authRoutes}/>} />
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