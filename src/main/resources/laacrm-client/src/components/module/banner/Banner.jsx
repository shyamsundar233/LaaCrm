import "./Banner.css";
import {Box, Container} from "@mui/material";
import {useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {useSelector} from "react-redux";
import LeadsIcon from "../../asset/target.png";
import ContactsIcon from "../../asset/youth.png";
import AccountsIcon from "../../asset/building.png";
import ProductsIcon from "../../asset/box.png";
import VendorsIcon from "../../asset/handshake.png";
import SalesIcon from "../../asset/salary.png";
import {XButton} from "../../UIComponents";

const Banner = () => {

    const navigate = useNavigate();
    const {moduleName} = useParams();
    const modules = useSelector(state => state.module.modules);
    const [isLoading, setIsLoading] = useState(true);
    const [module, setModule] = useState(null);
    const modVsIcon = {
        "Leads": LeadsIcon,
        "Contacts": ContactsIcon,
        "Accounts": AccountsIcon,
        "Products": ProductsIcon,
        "Vendors": VendorsIcon,
        "Sales": SalesIcon,
    }

    useEffect(() => {
        let currentMod = modules.find(mod => mod.moduleName === moduleName);
        setModule(currentMod);
        setIsLoading(false);
    },[moduleName, modules]);

    return (
        <Container maxWidth="">
            {isLoading ? (
                <Box>Loading...</Box>
            ) : (
                <Box>
                    <Box className={`banner-box h1`}>
                        <Box className={`d-flex-j-center-a-center`}>
                            <img className="mod-icon-banner" src={module ? modVsIcon[module.pluralName] : ""} alt="Mod Icon"/> &nbsp;
                            Welcome to <b> &nbsp; {module ? module.pluralName : ""} &nbsp; </b> Module
                        </Box>
                        <Box className={`d-flex-j-center-a-center mt-4 pointer`}>
                            <XButton label={`Create ${module ? module.singularName : ""}`} onClick={() => navigate(`/app/module/${module.moduleName}/create`)} className={`create-btn-banner`} variant="contained"/>
                        </Box>
                    </Box>
                </Box>
            )}
        </Container>
    )
}

export default Banner;