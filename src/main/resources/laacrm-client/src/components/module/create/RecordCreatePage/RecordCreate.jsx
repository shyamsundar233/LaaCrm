import {Box, Container, Grid2, TextField, Typography} from "@mui/material";
import {useEffect, useState} from "react";
import {useSelector} from "react-redux";
import {useParams} from "react-router-dom";

const RecordCreate = () => {

    const {moduleName, layoutId} = useParams();
    const modules = useSelector(state => state.module.modules);
    const [isLoading, setIsLoading] = useState(true);
    const [module, setModule] = useState({});
    const [layout, setLayout] = useState([]);
    const [fields, setFields] = useState([]);

    useEffect(() => {
        let moduleObj = modules.find(elem => elem.moduleName === moduleName);
        if(moduleObj) {
            setModule(moduleObj);
            setIsLoading(false);
            let layoutObj = moduleObj.layouts.find(layout => layout.layoutId === layoutId);
            setLayout(layoutObj);
            setFields(layoutObj.fields);
        }
    },[modules, moduleName]);

    return (
        <Container maxWidth="" className={`outlet-parent-cont`}>
            {isLoading ? (
                <Box>Loading....</Box>
            ) : (
                <Box className={`p-4`}>
                    <Box>
                        <Typography className={`heading-1`}>Create {module.singularName} - {layout.layoutName}</Typography>
                    </Box>
                    <Box className={`mt-4`}>
                        <Grid2 container spacing={4}>
                            {fields.map((field, index) => (
                                <Grid2 size={6}>
                                    <Field field={field}/>
                                </Grid2>
                            ))}
                        </Grid2>
                    </Box>
                </Box>
            )}
        </Container>
    )
}

const Field = ({field}) => {

    const FieldElem = () => {
        switch (field.fieldType) {
            case '1':
                return <TextField
                    variant="outlined"
                    fullWidth
                />;
        }
    }

    return (
        <Container key={`field_${field.fieldId}_elem`} className={`d-flex-a-center w-100`}>
            <Typography variant={`subtitle1`} className={`w-25`}>{field.fieldName}</Typography>
            <FieldElem/>
        </Container>
    )
}

export default RecordCreate;