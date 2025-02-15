import {Outlet, useNavigate, useParams} from "react-router-dom";
import {useSelector} from "react-redux";
import {useEffect} from "react";

const Create = () => {

    const navigate = useNavigate();
    const {moduleName, layoutId} = useParams();
    const modules = useSelector(state => state.module.modules);

    useEffect(() => {
        if(!layoutId && moduleName && modules && modules.length > 0) {
            let module = modules.find(mod => mod.moduleName === moduleName);
            if(module) {
                let layout = module.layouts[0];
                navigate(`/app/module/${moduleName}/create/${layout.layoutId}`);
            }
        }
    },[moduleName, layoutId, modules]);

    return (
        <Outlet/>
    )
}

export default Create;