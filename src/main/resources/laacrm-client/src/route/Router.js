import App from "../App";
import PrivateRoute from "./PrivateRoute";
import Dashboard from "../components/dashboard/Dashboard";
import {BrowserRouter, Navigate, Route, Routes} from "react-router-dom";
import Login from "../components/login/Login";
import Banner from "../components/module/banner/Banner";
import Modules from "../components/module/Module";
import RecordCreate from "../components/module/create/RecordCreatePage/RecordCreate";
import Create from "../components/module/create/Create";

const Router = () => {
    return (
        <BrowserRouter>
            <Routes>
                <Route exact path="/" element={<Navigate to="/app/dashboard"/>}/>
                <Route exact path="/app" element={<Navigate to="/app/dashboard"/>}/>
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
            <Route path="module" element={<Modules/>}>
                <Route path=":moduleName/banner" element={<Banner/>}/>
                <Route path=":moduleName/create" element={<Create/>}>
                    <Route path=":layoutId" element={<RecordCreate/>} />
                </Route>
            </Route>
        </Route>
    </Routes>
);

export default Router;