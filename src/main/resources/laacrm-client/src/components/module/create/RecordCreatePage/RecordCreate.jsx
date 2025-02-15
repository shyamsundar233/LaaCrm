import {Box, Container, Typography} from "@mui/material";
import {useEffect, useState} from "react";
import {useSelector} from "react-redux";
import {useParams} from "react-router-dom";

const RecordCreate = () => {

    const {moduleName} = useParams();
    const modules = useSelector(state => state.module.modules);
    const [isLoading, setIsLoading] = useState(true);
    const [module, setModule] = useState(null);

    useEffect(() => {
        let moduleObj = modules.find(elem => elem.moduleName === moduleName);
        if(moduleObj) {
            setModule(moduleObj);
            setIsLoading(false);
        }
    },[modules, moduleName]);

    return (
        <Container maxWidth="" className={`outlet-parent-cont`}>
            {isLoading ? (
                <Box>Loading....</Box>
            ) : (
                <Box className={`p-4`}>
                    <Box>
                        <Typography className={`heading-1`}>Create {module.singularName}</Typography>
                    </Box>
                </Box>
            )}
        </Container>
    )
}

export default RecordCreate;