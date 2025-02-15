import {Outlet, useNavigate, useParams} from "react-router-dom";
import {useSelector} from "react-redux";
import {useEffect} from "react";

const Module = () => {

    const navigate = useNavigate();
    const modules = useSelector((state) => state.module.modules);
    const {moduleName} = useParams();

    useEffect(() => {
        if (modules && modules.length > 0 && !moduleName) {
            navigate(`/app/module/${modules[0].moduleName}/banner`);
        }
    },[modules])

    return (
        <Outlet/>
    )
}

export default Module;