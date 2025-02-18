import {Outlet, useLocation, useNavigate, useParams} from "react-router-dom";
import {useSelector} from "react-redux";
import {useEffect} from "react";

const Redirect = () => {

    const {moduleName} = useParams();
    const navigate = useNavigate();
    const location = useLocation();
    const modules = useSelector((state) => state.module.modules);

    useEffect(() => {
        switch (location.pathname) {
            case "/app/module":
            case `/app/module/${moduleName}`:
                moduleHelper();
                break;
            case `/app/module/${moduleName}/create`:
                createPageHelper();
                break;
        }
    },[location.pathname]);

    const moduleHelper = () => {
        if(moduleName){
            navigate(`/app/module/${moduleName}/banner`);
        }else{
            navigate(`/app/module/${modules[0].moduleName}/banner`);
        }
    }

    const createPageHelper = () => {
        let module = modules.find(mod => mod.moduleName === moduleName);
        if(module) {
            let layout = module.layouts[0];
            navigate(`/app/module/${moduleName}/create/${layout.layoutId}`);
        }
    }

    return (<Outlet/>);

}

export default Redirect;