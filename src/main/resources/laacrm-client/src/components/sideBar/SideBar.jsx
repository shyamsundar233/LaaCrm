import "./SideBar.css";
import {Box, Container} from "@mui/material";
import {useSelector} from "react-redux";
import {useState} from "react";

const SideBar = () => {

    const modules = useSelector(state => state.module.modules);
    const [activeMod, setActiveMod] = useState("");

    const handleActiveMod = (module) => {
        setActiveMod(module.moduleId)
    }

    return (
        <Container maxWidth="" className={`side-bar-parent-cont`}>
            <Box className={`mt-4 mb-4 ms-2 h4 fw-bold`}>Modules</Box>
            {modules && modules.length > 0 && modules.map((module) => (
                <Box id={`mod_${module.moduleId}`} className={`${module.moduleId === activeMod ? "mod-elem-active" : ""} mod-elem h6 p-2 ps-3 pointer mb-3`} onClick={() => handleActiveMod(module)}>{module.pluralName}</Box>
            ))}
        </Container>
    )
}

export default SideBar;