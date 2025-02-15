import "./SideBar.css";
import {Box, Container} from "@mui/material";
import {useSelector} from "react-redux";
import {useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import LeadsIcon from "../asset/target.png";
import ContactsIcon from "../asset/youth.png";
import AccountsIcon from "../asset/building.png";
import ProductsIcon from "../asset/box.png";
import VendorsIcon from "../asset/handshake.png";
import SalesIcon from "../asset/salary.png";

const SideBar = () => {

    const navigate = useNavigate();
    const modules = useSelector(state => state.module.modules);
    const [activeMod, setActiveMod] = useState("");
    const {moduleName} = useParams();
    const modVsIcon = {
        "Leads": LeadsIcon,
        "Contacts": ContactsIcon,
        "Accounts": AccountsIcon,
        "Products": ProductsIcon,
        "Vendors": VendorsIcon,
        "Sales": SalesIcon,
    }

    useEffect(() => {
        if(moduleName && modules && modules.length > 0){
            let moduleRec = modules.find(mod => mod.moduleName === moduleName);
            if(moduleRec){
                setActiveMod(moduleRec.moduleId);
            }
        }
    },[modules, moduleName]);

    const handleActiveMod = (module) => {
        setActiveMod(module.moduleId);
        navigate(`/app/module/${module.moduleName}/banner`);
    }

    return (
        <Container maxWidth="" className={`side-bar-parent-cont`}>
            <Box className={`mt-4 mb-4 ms-2 heading-1`}>Modules</Box>
            {modules && modules.length > 0 && modules.map((module) => (
                <Box id={`mod_${module.moduleId}`} className={`${module.moduleId === activeMod ? "mod-elem-active" : ""} mod-elem h6 p-2 ps-3 pointer mb-3 d-flex align-items-center`} onClick={() => handleActiveMod(module)}>
                    <img className={`mod-icon`} src={modVsIcon[module.pluralName]} alt="Module icon" />
                    {module.pluralName}
                </Box>
            ))}
        </Container>
    )
}

export default SideBar;