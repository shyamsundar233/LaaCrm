import App from "../App";
import PrivateRoute from "./PrivateRoute";
import Dashboard from "../components/dashboard/Dashboard";
import {BrowserRouter, Navigate, Route, Routes} from "react-router-dom";
import Login from "../components/login/Login";
import Banner from "../components/module/banner/Banner";
import RecordCreate from "../components/module/create/RecordCreatePage/RecordCreate";
import Redirect from "./Redirect";
import RecordList from "../components/module/RecordList/RecordList";

const Router = () => {
    return (
        <BrowserRouter>
            <Routes>
                <Route exact path="/" element={<Navigate to="/app/dashboard"/>}/>
                <Route exact path="/app" element={<Navigate to="/app/dashboard"/>}/>
                <Route path="/app/*" element={<PrivateRoute component={authRoutes}/>} />
                <Route path="/login" element={<Login operation="login"/>} />
                <Route path="/register" element={<Login operation="register"/>} />
            </Routes>
        </BrowserRouter>
    );
}

const authRoutes = () => (
    <Routes>
        <Route path="/" element={<App />}>
            <Route path="dashboard" element={<Dashboard/>}/>
            <Route exact path="module" element={<Redirect/>}>
                <Route exact path=":moduleName" element={<Redirect/>}>
                    <Route path="banner" element={<Banner/>}/>
                    <Route exact path="create" element={<Redirect/>}>
                        <Route path=":layoutId" element={<RecordCreate/>} />
                    </Route>
                    <Route path="list" element={<RecordList/>}/>
                </Route>
            </Route>
        </Route>
    </Routes>
);

export default Router;