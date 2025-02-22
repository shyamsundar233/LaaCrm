import "./RecordCreate.css";
import {Alert, Box, Container, Grid2, MenuItem, Select, TextField, Typography} from "@mui/material";
import {useEffect, useState} from "react";
import {useSelector} from "react-redux";
import {useNavigate, useParams} from "react-router-dom";
import apiEngine from "../../../../api/apiEngine";
import {XButton} from "../../../UIComponents";

const RecordCreate = ({operation}) => {

    const navigate = useNavigate();
    const {moduleName, layoutId, recordId} = useParams();
    const modules = useSelector(state => state.module.modules);
    const [isLoading, setIsLoading] = useState(true);
    const [module, setModule] = useState({});
    const [layout, setLayout] = useState([]);
    const [fields, setFields] = useState([]);
    const [recordDetails, setRecordDetails] = useState({});
    const [errorMessage, setErrorMessage] = useState("");

    useEffect(() => {
        operation = operation === null ? recordId ? "edit" : "create" : operation;
        if(operation === "edit") {
            editInitialLoad();
        }else {
            createInitialLoad();
        }
    },[modules, moduleName]);

    useEffect(() => {
        setTimeout(() => {
            setErrorMessage("");
        }, 3000)
    },[errorMessage]);

    const createInitialLoad = () => {
        let moduleObj = modules.find(elem => elem.moduleName === moduleName);
        if(moduleObj) {
            setModule(moduleObj);
            setIsLoading(false);
            let layoutObj = moduleObj.layouts.find(layout => layout.layoutId === layoutId);
            setLayout(layoutObj);
            setFields(layoutObj.fields);
        }
    }

    const editInitialLoad = () => {
        let moduleObj = modules.find(elem => elem.moduleName === moduleName);
        apiEngine.requestHelper("GET", `/v1/api/${moduleObj.moduleId}/record/${recordId}`).then((response) => {
            let recordObj = response.data.data.record;
            if(moduleObj) {
                setModule(moduleObj);
                setIsLoading(false);
                let layoutObj = moduleObj.layouts.find(layout => layout.layoutId === recordObj.layoutId);
                setLayout(layoutObj);
                setFields(layoutObj.fields);
                setRecordDetails(recordObj.recordDetails);
            }
        })
    }

    const onFieldValueChange = (fieldName, fieldValue) => {
        setRecordDetails(prevState => ({
            ...prevState,
            [fieldName]: fieldValue
        }));
    };

    const onRecordCreate = () => {
        if(validate()){
            let data = {
                moduleId: module.moduleId,
                layoutId: layout.layoutId,
                recordDetails: recordDetails
            }
            if(operation === "edit") {
                data.recordId = recordId;
                apiEngine.requestHelper("PUT", `/v1/api/${module.moduleId}/record`, data).then(res => {
                    debugger
                    navigate(`/app/module/${module.moduleName}`);
                })
            }else {
                apiEngine.requestHelper("POST", `/v1/api/${module.moduleId}/record`, data).then(res => {
                    navigate(`/app/module/${module.moduleName}`);
                })
            }
        }
    }

    const validate = () => {
        for(let index = 0; index < fields.length; index++) {
            let field = fields[index];
            let props = field.fieldProperties;
            for(let index1 = 0; index1 < props.length; index1++) {
                let prop = props[index1];
                if(prop.propertyName === "isMandatory" && prop.propertyValue === 'true' && !recordDetails[field.fieldName]){
                    setErrorMessage(`${field.fieldName} is mandatory`);
                    return false;
                }else if(prop.propertyName === "maxChar" && recordDetails[field.fieldName]){
                    let maxChar = parseInt(prop.propertyValue);
                    if(recordDetails[field.fieldName].length > maxChar){
                        setErrorMessage(`Limit Exceeded, ${field.fieldName} allows only ${maxChar} characters`);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    return (
        <Container maxWidth="">
            {errorMessage &&
                <Box className={`alert-pop-rec-create`}>
                    <Alert severity="error">{errorMessage}</Alert>
                </Box>
            }
            {isLoading ? (
                <Box>Loading....</Box>
            ) : (
                <Box className={`p-1 mt-4`}>
                    <Box className={`d-flex`}>
                        <Typography className={`heading-1`}>Create {module.singularName} - {layout.layoutName}</Typography>
                        <Box className={`ms-auto me-3`}>
                            <XButton label="Cancel" variant={`outlined`} className={`me-2`} onClick={() => navigate(`/app/module/${moduleName}/list`)}/>
                            <XButton label="Create" variant={`contained`} onClick={onRecordCreate}/>
                        </Box>
                    </Box>
                    <Box className={`mt-5`}>
                        <Grid2 container spacing={4}>
                            {fields.map((field, index) => (
                                <Grid2 size={6}>
                                    <Field field={field} fieldProps={field.fieldProperties} recordDetails={recordDetails} onValueChange={onFieldValueChange} />
                                </Grid2>
                            ))}
                        </Grid2>
                    </Box>
                </Box>
            )}
        </Container>
    )
}

const Field = ({ field, fieldProps, recordDetails, onValueChange }) => {
    const [options, setOptions] = useState([]);
    const [defValue, setDefValue] = useState(null);
    const [isMandatory, setIsMandatory] = useState(false);

    useEffect(() => {
        setIsMandatory(fieldProps.find(prop => prop.propertyName === "isMandatory")?.propertyValue === "true");
        if (field.fieldType === '4') {
            setOptions(JSON.parse(fieldProps.find(prop => prop.propertyName === "options")?.propertyValue.replace(/'/g, '"')));
            setDefValue(fieldProps.find(prop => prop.propertyName === "defValue")?.propertyValue);
        }
    }, []);

    return (
        <Container key={`field_${field.fieldId}_elem`} className={`d-flex-a-center w-100`}>
            <Typography variant={`subtitle1`} style={{ width: "30%" }}>
                {field.fieldName}
                {isMandatory ? <span className={`text-danger ms-1`}>*</span> : null}
            </Typography>
            {field.fieldType === '1' || field.fieldType === '6' || field.fieldType === '7' ? (
                <TextField
                    variant="outlined"
                    fullWidth
                    value={recordDetails[field.fieldName] || ""}
                    onChange={(event) => onValueChange(field.fieldName, event.target.value)}
                />
            ) : field.fieldType === '3' || field.fieldType === '9' ? (
                <TextField
                    variant="outlined"
                    type="number"
                    fullWidth
                    value={recordDetails[field.fieldName] || ""}
                    onChange={(event) => onValueChange(field.fieldName, event.target.value)}
                />
            ) : field.fieldType === '4' ? (
                <Select
                    fullWidth
                    variant="outlined"
                    value={recordDetails[field.fieldName] || defValue || ""}
                    onChange={(event) => onValueChange(field.fieldName, event.target.value)}
                >
                    {options.map((option, index) => (
                        <MenuItem key={index} value={option}>{option}</MenuItem>
                    ))}
                </Select>
            ) : field.fieldType === '8' ? (
                <TextField
                    variant="outlined"
                    type="email"
                    fullWidth
                    value={recordDetails[field.fieldName] || ""}
                    onChange={(event) => onValueChange(field.fieldName, event.target.value)}
                />
            ) : field.fieldType === '10' ? (
                <TextField
                    variant="outlined"
                    type="url"
                    fullWidth
                    value={recordDetails[field.fieldName] || ""}
                    onChange={(event) => onValueChange(field.fieldName, event.target.value)}
                />
            ) : null}
        </Container>
    );
};

export default RecordCreate;